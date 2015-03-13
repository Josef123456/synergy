package synergy.views;


import java.util.ArrayList;
import java.util.List;

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
import synergy.models.Photo;
import synergy.tasks.ThumbnailLoaderTask;


/**
 * Created by iHack1337 on 3/2/2015.
 */
public class PhotoGrid extends GridView<Image> {

    private static ArrayList<Image> selectedImages = new ArrayList<>();
	private static List<Photo> photos = new ArrayList<> ();
	private static ArrayList<Photo> selectedPhotos = new ArrayList<> ();
    private static GridView<Image> photosGrid;
    private static ObservableList<Image> displayedImagesList;
    private static ImageGridCell lastSelectedCell = null;

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

	public PhotoGrid(ObservableList imagesList) {
        displayedImagesList = imagesList;
        photosGrid = this;
        this.setItems(displayedImagesList);

        photosGrid.setOnMouseClicked(event -> {
//                System.out.println("REGISTERED EVENT AT " + event.getSceneX() + " " + event
//                        .getSceneY());
//                GridRowSkin.gridRowSkin.getNodeAtCoordinates();
        });

        this.setCellFactory(param -> {
                    final ImageGridCell newImageCell = new ImageGridCell();
                    newImageCell.setOnMouseClicked(event -> {
                        if (event.isShiftDown()) {
                            int lastSelectedIndex = lastSelectedCell.getIndex();
                            int newlySelectedIndex = newImageCell.getIndex();
                            int iterationIndex = newlySelectedIndex;
                            Image shiftSelectedImage = displayedImagesList.get(iterationIndex);
	                        Photo shiftSelectedPhoto = photos.get(iterationIndex);

                            if (newlySelectedIndex < lastSelectedIndex) {
                                if (selectedImages.contains(shiftSelectedImage))
                                    iterationIndex++;
                                while (iterationIndex <= lastSelectedIndex) {
	                                shiftSelectedPhoto = photos.get(iterationIndex);
	                                selectedPhotos.remove(shiftSelectedPhoto);
	                                selectedPhotos.add(shiftSelectedPhoto);

                                    shiftSelectedImage = displayedImagesList.get(iterationIndex);
                                    selectedImages.remove(shiftSelectedImage);
                                    selectedImages.add(shiftSelectedImage);
                                    iterationIndex++;
                                }
                            } else {
                                if (selectedImages.contains(shiftSelectedImage))
                                    iterationIndex--;
                                while (iterationIndex >= lastSelectedIndex) {
	                                shiftSelectedPhoto = photos.get(iterationIndex);
	                                selectedPhotos.remove(shiftSelectedPhoto);
	                                selectedPhotos.add(shiftSelectedPhoto);

                                    shiftSelectedImage = displayedImagesList.get(iterationIndex);
                                    selectedImages.remove(shiftSelectedImage);
                                    selectedImages.add(shiftSelectedImage);
                                    iterationIndex--;
                                }
                            }
                        } else setCellSelection(newImageCell);
                        ((GridViewSkin) this.getSkin()).updateGridViewItems();
                        lastSelectedCell = newImageCell;
                    });
                    return newImageCell;
                }
        );

        this.setCellHeight(300);
        this.setCellWidth(300);
    }

    public void setCellSelection(ImageGridCell imageCell) {
	    Image selectedImage = imageCell.getItem ();
	    int selectedImageIndex = displayedImagesList.indexOf (selectedImage);
        if (imageCell.getBorder() == null) {
	        selectedPhotos.add(photos.get(selectedImageIndex));
            selectedImages.add(selectedImage);

            BorderStroke[] borderStrokeArray = new BorderStroke[4];
            for (int i = 0; i < 4; i++)
                borderStrokeArray[i] = new BorderStroke(javafx.scene.paint.Color
                        .BLUE, BorderStrokeStyle.SOLID, null, BorderStroke.MEDIUM,
                        new Insets(-5, -5, -5, -5));
            imageCell.setBorder(new Border(borderStrokeArray));
        } else {
	        selectedPhotos.remove(photos.get(selectedImageIndex));
            selectedImages.remove (selectedImage);
            imageCell.setBorder(null);
        }
    }

    public void setGridPhotos(final List<Photo> photosToDisplay) {
	    photos = photosToDisplay;
        Thread setPhotosThread = new Thread(new ThumbnailLoaderTask(photosToDisplay));
        setPhotosThread.setDaemon(true);
        setPhotosThread.start();
    }
}
