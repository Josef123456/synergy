package synergy.views;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import controlsfx.controlsfx.control.GridView;
import controlsfx.impl.org.controlsfx.skin.GridViewSkin;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
    private static GridView<Image> photosGrid;
    private static HashMap<Photo, Image> displayedImagesMap = new HashMap<>();
    private static ObservableList<Image> displayedImagesList;
    private static ArrayList<Task> tasks = new ArrayList<>();
    private TaggingArea taggingArea;
    public static boolean displayingImported = false;

    public static ArrayList<Task> getTasks() {
        return tasks;
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

    public static GridView<Image> getPhotosGrid() {
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

    public void setGridPhotos(List<Photo> photosToDisplay) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (Task task : tasks)
                    task.cancel(true);
                displayedPhotosList.clear();
                displayedPhotosList.addAll(photosToDisplay);
                displayedImagesMap.clear();
                selectedImages.clear();
                selectedPhotos.clear();
                displayedImagesList.clear();
                if (((GridViewSkin) photosGrid.getSkin()) != null)
                    ((GridViewSkin) photosGrid.getSkin()).updateGridViewItems();
                Thread setPhotosThread = new Thread(new ThumbnailLoaderTask(photosToDisplay));
                setPhotosThread.setDaemon(true);
                setPhotosThread.start();
            }
        });
    }

    public void addPhotosToGrid(final List<Photo> photosToDisplay) {
        displayedPhotosList.addAll(photosToDisplay);
        ThumbnailLoaderTask thumbnailLoaderTask = new ThumbnailLoaderTask(photosToDisplay);
        tasks.add(thumbnailLoaderTask);
        (thumbnailLoaderTask).run();

    }
}
