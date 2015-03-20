package synergy.tasks;

import com.bric.image.jpeg.JPEGMetaData;

import org.imgscalr.Scalr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import synergy.models.Photo;
import synergy.utilities.WritableImageCreator;
import synergy.views.PhotoGrid;

/**
 * Created by alexstoick on 3/7/15.
 */
public class ThumbnailLoaderTask extends Task {
    private List<Photo> photosToDisplay;
    private ObservableList<Image> displayedImagesList;
    private HashMap<Photo, Image> displayedImagesMap;
    private Thread parentThread;

    public ThumbnailLoaderTask(List<Photo> photosToDisplay) {
        this.photosToDisplay = photosToDisplay;
        this.displayedImagesList = PhotoGrid.getDisplayedImagesList();
        this.displayedImagesMap = PhotoGrid.getDisplayedImagesMap();
    }

    public void setParentThread(Thread parentThread) {
        this.parentThread = parentThread;
    }

    @Override
    protected Object call() throws Exception {
        ArrayList<Photo> toEliminate = new ArrayList<>();
        for (Photo photo : photosToDisplay) {
            BufferedImage initialThumbNail = null;
            try {
                initialThumbNail = JPEGMetaData.getThumbnail(new File(photo.getPath()));
                if (initialThumbNail == null) {
                    toEliminate.add(photo);
                    initialThumbNail = ImageIO.read(new File(photo.getPath()));
                    initialThumbNail = Scalr.resize(initialThumbNail, 750);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            final WritableImage finalWi = WritableImageCreator.fromBufferedImage
                    (initialThumbNail);
            initialThumbNail.flush();
            initialThumbNail = null;

            if (!parentThread.isInterrupted()) {
                Platform.runLater(() -> {
                    displayedImagesList.add(finalWi);
                    displayedImagesMap.put(photo, finalWi);
                });
            } else {
                return null;
            }
            System.out.println("Loaded thumbnail for: " + photo.getPath());
        }
        photosToDisplay.removeAll(toEliminate);
        FullResolutionPhotoLoaderTask qualityLoaderTask = new FullResolutionPhotoLoaderTask
                (photosToDisplay);
        Thread qualityLoaderThread = new Thread(qualityLoaderTask);
        qualityLoaderTask.setParentThread(qualityLoaderThread);
        PhotoGrid.getThreads().add(qualityLoaderThread);
        qualityLoaderThread.setDaemon(true);
        qualityLoaderThread.start();
        return null;
    }
}