package synergy.tasks;

import org.imgscalr.Scalr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import controlsfx.controlsfx.control.GridView;
import controlsfx.impl.org.controlsfx.skin.GridViewSkin;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import synergy.models.Photo;
import synergy.views.PhotoGrid;
import synergy.utilities.WritableImageCreator;

/**
 * Created by alexstoick on 3/7/15.
 */
public class FullResolutionPhotoLoaderTask extends Task {
    private List<Photo> photosToDisplay;
    private ObservableList<Image> displayedImagesList;
    private HashMap<Photo, Image> displayedImagesMap;
    private ArrayList<Image> selectedImages;
    private GridView<Image> photosGrid;

    public FullResolutionPhotoLoaderTask(List<Photo> photosToDisplay, HashMap<Photo, Image>
            displayedImagesMap) {
        this.photosToDisplay = photosToDisplay;
        this.displayedImagesList = PhotoGrid.getDisplayedImagesList();
        this.displayedImagesMap = displayedImagesMap;
        this.selectedImages = PhotoGrid.getSelectedImages();
        this.photosGrid = PhotoGrid.getPhotosGrid();
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
            BufferedImage scaledImage = Scalr.resize(initialImage, 350);
            initialImage.flush();
            final WritableImage finalWi = WritableImageCreator.fromBufferedImage(scaledImage);

            Platform.runLater(() -> {
                Image toBeReplaced = displayedImagesMap.get(photo);
                int i = displayedImagesList.indexOf(toBeReplaced);
                displayedImagesList.set(i, finalWi);
                i = selectedImages.indexOf(toBeReplaced);
                if (i != -1)
                    selectedImages.set(i, finalWi);
                ((GridViewSkin) photosGrid.getSkin()).updateGridViewItems();
            });
            System.out.println("Replaced " + photo.getPath());
        }
        return null;
    }
}