package synergy.tasks;

import com.bric.image.jpeg.JPEGMetaData;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import synergy.models.Photo;
import synergy.newViews.PhotoGrid;
import synergy.utilities.WritableImageCreator;

/**
 * Created by alexstoick on 3/7/15.
 */
public class ThumbnailLoaderTask extends Task {
    private List<Photo> photosToDisplay;
    private ObservableList<Image> displayedImagesList;
    private HashMap<Photo, Image> displayedImagesMap;

    public ThumbnailLoaderTask(List<Photo> photosToDisplay) {
        this.photosToDisplay = photosToDisplay;
        this.displayedImagesList = PhotoGrid.getDisplayedImagesList();
        this.displayedImagesMap = new HashMap<>();
    }

    @Override
    protected Object call() throws Exception {
        for (Photo photo : photosToDisplay) {
            BufferedImage initialThumbNail = null;
            try {
                initialThumbNail = JPEGMetaData.getThumbnail(new File(photo.getPath()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            final WritableImage finalWi = WritableImageCreator.fromBufferedImage(initialThumbNail);

            Platform.runLater(() -> {
                displayedImagesList.add(finalWi);
                displayedImagesMap.put(photo, finalWi);
            });
            System.out.println("Loaded thumbnail for: " + photo.getPath());
        }

        Thread setQualityPhotosThread = new Thread(new FullResolutionPhotoLoaderTask
                (photosToDisplay, displayedImagesMap));
        setQualityPhotosThread.setDaemon(true);
        setQualityPhotosThread.start();
        return null;
    }
}