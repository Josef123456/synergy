package synergy.newViews;


import com.bric.image.jpeg.JPEGMetaData;

import org.imgscalr.Scalr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import controlsfx.controlsfx.control.GridCell;
import controlsfx.controlsfx.control.GridView;
import controlsfx.controlsfx.control.cell.ImageGridCell;
import controlsfx.impl.org.controlsfx.skin.GridRowSkin;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.util.Callback;
import synergy.models.Photo;
import synergy.tasks.ThumbnailLoaderTask;


/**
 * Created by iHack1337 on 3/2/2015.
 */
public class PhotoGrid extends GridView<Image> {

    private static ArrayList<Image> selectedImages = new ArrayList<>();
    private static GridView<Image> photosGrid;
	private static ObservableList<Image> displayedImagesList;
	private static ArrayList<Photo> displayedPhotosList = new ArrayList<> ();

	public static ArrayList<Photo> getDisplayedPhotosList () {
		return displayedPhotosList;
	}

	public static ObservableList<Image> getDisplayedImagesList () {
		return displayedImagesList;
	}

	public static ArrayList<Image> getSelectedImages () {
		return selectedImages;
	}

	public PhotoGrid(ObservableList imagesList) {
        displayedImagesList = imagesList;
        photosGrid = this;
        this.setItems(displayedImagesList);

        photosGrid.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("REGISTERED EVENT AT " + event.getSceneX() + " " + event
                        .getSceneY());
                GridRowSkin.gridRowSkin.getNodeAtCoordinates();
            }
        });

        this.setCellFactory(param -> {
            final ImageGridCell imageCell = new ImageGridCell();
            imageCell.setOnMouseClicked(event -> {
                if (imageCell.getBorder() == null) {

                    selectedImages.add(imageCell.getItem());
                    BorderStroke[] borderStrokeArray = new BorderStroke[4];
                    for (int i = 0; i < 4; i++)
                        borderStrokeArray[i] = new BorderStroke(javafx.scene.paint.Color
                                .BLUE, BorderStrokeStyle.SOLID, null, BorderStroke.MEDIUM,
                                new Insets(-5, -5, -5, -5));
                    imageCell.setBorder(new Border(borderStrokeArray));
                } else {
                    selectedImages.remove(imageCell.getItem());
                    imageCell.setBorder(null);
                }
            });
            return imageCell;
        });

        this.setCellHeight(300);
        this.setCellWidth(300);
    }

    public void setGridPhotos(final List<Photo> photos) {
        Thread setPhotosThread = new Thread(new ThumbnailLoaderTask (photos));
        setPhotosThread.setDaemon(true);
        setPhotosThread.start();
    }
}
