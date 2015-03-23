package synergy.tasks;

import org.imgscalr.Scalr;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import controlsfx.impl.org.controlsfx.skin.GridViewSkin;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import synergy.models.Photo;
import synergy.utilities.ImagePadder;
import synergy.utilities.WritableImageCreator;
import synergy.views.PhotoGrid;

/**
 * This is the Task used to load quality photos to replace the thumbnails that are initially
 * added to the grid for efficiency reasons.
 */
public class FullResolutionPhotoLoaderTask extends Task {
    private List<Photo> photosToDisplay;
    private ObservableList<Image> displayedImagesList;
    private HashMap<Photo, Image> displayedImagesMap;
    private ArrayList<Image> selectedImages;
    private PhotoGrid photosGrid;
    private Thread parentThread;

    public FullResolutionPhotoLoaderTask(List<Photo> photosToDisplay) {
        this.photosToDisplay = photosToDisplay;
        this.displayedImagesList = PhotoGrid.getDisplayedImagesList();
        this.displayedImagesMap = PhotoGrid.getDisplayedImagesMap();
        this.selectedImages = PhotoGrid.getSelectedImages();
        this.photosGrid = PhotoGrid.getPhotosGrid();
    }

    public void setParentThread(Thread parentThread) {
        this.parentThread = parentThread;
    }

    @Override
    protected Object call() throws Exception {
        for (Photo photo : photosToDisplay) {
            BufferedImage initialImage = null;

            try {
                initialImage = ImageIO.read(new File(photo.getPath()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            initialImage = Scalr.resize(initialImage, 750);
            initialImage = ImagePadder.padToSize(initialImage, 750, 750, new Color(29, 30, 30));

            final WritableImage finalWi = WritableImageCreator.fromBufferedImage(initialImage);
            initialImage.flush();
            initialImage = null;

            if (!parentThread.isInterrupted()) {
                Platform.runLater(() -> {
                    Image toBeReplaced = displayedImagesMap.get(photo);
                    int i = displayedImagesList.indexOf(toBeReplaced);
                    try {
                        displayedImagesList.set(i, finalWi);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("Photo " + photo.getPath() + " was deleted!");
                    }
                    i = selectedImages.indexOf(toBeReplaced);
                    if (i != -1)
                        selectedImages.set(i, finalWi);
                    ((GridViewSkin) photosGrid.getSkin()).updateGridViewItems();
                    toBeReplaced = null;
                    System.gc();
                });
            } else {
                return null;
            }
            System.out.println("Replaced " + photo.getPath());
        }
        return null;
    }
}