package synergy.views;


import java.util.ArrayList;
import java.util.List;

import controlsfx.controlsfx.control.GridView;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import synergy.models.Photo;
import synergy.tasks.ThumbnailLoaderTask;

/**
 * Created by iHack1337 on 3/2/2015.
 */
public class PhotoGrid extends GridView<Image> {

    private static ArrayList<Image> selectedImages = new ArrayList<>();
	private static ArrayList<Photo> photos = new ArrayList<>();
	private static ArrayList<Photo> selectedPhotos = new ArrayList<> ();
    private static GridView<Image> photosGrid;
    private static ObservableList<Image> displayedImagesList;
    private TaggingArea taggingArea;

	public static List<Photo> getPhotos () {
		return photos;
	}

	public TaggingArea getTaggingArea () {
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

	public static ArrayList<Photo> getSelectedPhotos () {
		return selectedPhotos;
	}

	public PhotoGrid(ObservableList imagesList, TaggingArea taggingArea) {
        displayedImagesList = imagesList;
        photosGrid = this;
        this.taggingArea = taggingArea;
        this.setItems(displayedImagesList);

        this.setCellFactory( new GridCellFactory (this));

        this.setCellHeight(300);
        this.setCellWidth(300);
    }

    public void addPhotosToGrid(final List<Photo> photosToDisplay) {
	    photos.addAll(photosToDisplay);
        Thread setPhotosThread = new Thread(new ThumbnailLoaderTask(photosToDisplay));
        setPhotosThread.setDaemon(true);
        setPhotosThread.start();
    }
}
