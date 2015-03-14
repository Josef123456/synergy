package synergy.views;


import java.util.ArrayList;
import java.util.List;

import controlsfx.controlsfx.control.GridCell;
import controlsfx.controlsfx.control.GridView;
import controlsfx.controlsfx.control.cell.ImageGridCell;
import controlsfx.impl.org.controlsfx.skin.GridViewSkin;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.util.Callback;
import synergy.models.Photo;
import synergy.tasks.ThumbnailLoaderTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iHack1337 on 3/2/2015.
 */
public class PhotoGrid extends GridView<Image> {

    private static ArrayList<Image> selectedImages = new ArrayList<>();
	private static List<Photo> photos ;
	private static ArrayList<Photo> selectedPhotos = new ArrayList<> ();
    private static GridView<Image> photosGrid;
    private static ObservableList<Image> displayedImagesList;
    private TaggingArea taggingArea;

	public static List<Photo> getPhotos () {
		return photos;
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

    public void setGridPhotos(final List<Photo> photosToDisplay) {
	    photos = photosToDisplay;
        Thread setPhotosThread = new Thread(new ThumbnailLoaderTask(photosToDisplay));
        setPhotosThread.setDaemon(true);
        setPhotosThread.start();
    }
}
