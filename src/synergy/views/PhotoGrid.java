package synergy.views;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import controlsfx.controlsfx.control.GridView;
import controlsfx.impl.org.controlsfx.skin.GridViewSkin;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import synergy.models.Photo;
import synergy.tasks.ThumbnailLoaderTask;

/**
 * Created by iHack1337 on 3/2/2015.
 */
public class PhotoGrid extends GridView<Image> {

    private static ArrayList<Image> selectedImages = new ArrayList<>();
    private static ArrayList<Photo> displayedPhotosList = new ArrayList<>();
    private static ArrayList<Photo> selectedPhotos = new ArrayList<>();
    private static PhotoGrid photosGrid;
    private static HashMap<Photo, Image> displayedImagesMap = new HashMap<>();
    private static ObservableList<Image> displayedImagesList;
    private static ArrayList<Thread> threads = new ArrayList<>();
    private TaggingArea taggingArea;
    public static boolean displayingImported = true;

    public static ArrayList<Thread> getThreads() {
        return threads;
    }

    public static HashMap<Photo, Image> getDisplayedImagesMap() {
        return displayedImagesMap;
    }

    public static List<Photo> getDisplayedPhotos() {
        return displayedPhotosList;
    }

    public TaggingArea getTaggingArea() {
        return taggingArea;
    }

    public static PhotoGrid getPhotosGrid() {
        return photosGrid;
    }

    public static ObservableList<Image> getDisplayedImagesList() {
        return displayedImagesList;
    }

    public static ArrayList<Image> getSelectedImages() {
        return selectedImages;
    }

    public static ArrayList<Photo> getSelectedPhotos() {
        return selectedPhotos;
    }

    public PhotoGrid(ObservableList imagesList, TaggingArea taggingArea) {
        displayedImagesList = imagesList;
        photosGrid = this;
        this.taggingArea = taggingArea;
        this.setItems(displayedImagesList);

        this.setCellFactory(new GridCellFactory(this));

        this.setCellHeight(300);
        this.setCellWidth(300);
    }

    public static void setGridPhotos(List<Photo> photosToDisplay) {
        System.out.println("METHOD WAS CALLED WITH " + photosToDisplay.size() + " PHOTOS");
        for (Photo photo : photosToDisplay)
            System.out.println(photo.getDate());
        Platform.runLater(() -> {
	        threads.forEach (java.lang.Thread::interrupt);
            threads.clear();
            displayedPhotosList.clear ();
            displayedImagesList.clear();
            displayedImagesMap.clear();
            selectedImages.clear();
            selectedPhotos.clear();

            displayedPhotosList.addAll(photosToDisplay);
            GridViewSkin gridViewSkin = (GridViewSkin) photosGrid.getSkin();
            if (gridViewSkin != null)
                (gridViewSkin).updateGridViewItems();

            ThumbnailLoaderTask thumbnailLoaderTask = new ThumbnailLoaderTask(photosToDisplay);
            Thread thumbnailLoaderThread = new Thread(thumbnailLoaderTask);
            thumbnailLoaderTask.setParentThread(thumbnailLoaderThread);
            threads.add(thumbnailLoaderThread);
            thumbnailLoaderThread.setDaemon(true);
            thumbnailLoaderThread.start();
        });
    }

    public static void addPhotosToGrid(final List<Photo> photosToDisplay) {
        displayedPhotosList.addAll(photosToDisplay);
        ThumbnailLoaderTask thumbnailLoaderTask = new ThumbnailLoaderTask(photosToDisplay);
        Thread thumbnailLoaderThread = new Thread(thumbnailLoaderTask);
        thumbnailLoaderTask.setParentThread (thumbnailLoaderThread);
        threads.add(thumbnailLoaderThread);
        thumbnailLoaderThread.setDaemon(true);
        thumbnailLoaderThread.start();
    }
}
