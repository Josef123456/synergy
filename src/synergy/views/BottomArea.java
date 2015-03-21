package synergy.views;

import controlsfx.impl.org.controlsfx.skin.GridViewSkin;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import synergy.models.Photo;

import java.util.ArrayList;

/**
 * Created on 09/03/2015.
 * Codrin make it the way you like it. I prefer the "thumbnailpictures" to be in this class.
 */
public class BottomArea extends VBox {

    private Button deleteBtn, zoomMinusBtn, zoomPlusBtn;
    public static Button selectBtn, deselectBtn;

	public BottomArea() {
        getStyleClass().setAll("button-bar");
        initBottomArea();
        addEventHandlers();
    }

    public void initBottomArea() {
        ToolBar bottomBar = new ToolBar();

        zoomMinusBtn = new Button("-");
        setupNodeStyle(zoomMinusBtn, "zoomMinusBtn");
        zoomMinusBtn.setMinWidth(20);

	    Label zoomLabel = new Label ("Zoom");
        setupNodeStyle(zoomLabel, "zoomLabel");

        zoomPlusBtn = new Button("+");
        setupNodeStyle(zoomPlusBtn, "zoomPlusBtn");
        zoomPlusBtn.setMinWidth (20);

        deselectBtn = new Button("Deselect All");
        setupNodeStyle(deselectBtn, "deselectButton");

        selectBtn = new Button("Select All");
        setupNodeStyle(selectBtn, "selectAllButton");

        deleteBtn = new Button("Delete");
        setupNodeStyle (deleteBtn, "deleteBtn");

	    HBox zoomBox = new HBox (5);
        zoomBox.getChildren ().addAll(zoomMinusBtn, zoomLabel, zoomPlusBtn);
        zoomBox.setAlignment (Pos.CENTER_LEFT);
        HBox.setHgrow (zoomBox, Priority.ALWAYS);

	    HBox selectBox = new HBox (1);
        selectBox.getChildren ().addAll(selectBtn, deselectBtn);
        selectBox.setAlignment (Pos.CENTER);
        HBox.setHgrow (selectBox, Priority.ALWAYS);

	    HBox rightBox = new HBox ();
        rightBox.getChildren ().add(deleteBtn);
        rightBox.setAlignment (Pos.CENTER_RIGHT);
        HBox.setHgrow (rightBox, Priority.ALWAYS);

        bottomBar.getItems().addAll(zoomBox, selectBox, rightBox);

        this.getChildren().addAll(bottomBar);
    }

    public void addEventHandlers() {
        PhotoGrid photoGrid = PhotoGrid.getPhotosGrid();

	    addZoomMinusEventHandler (photoGrid);
	    addZoomPlusEventHandler (photoGrid);
	    addSelectAllEventHandler (photoGrid);
	    addDeselectAllEventHandler (photoGrid);
	    addDeleteEventHandler ();
    }

	private void addDeleteEventHandler () {
		deleteBtn.setOnAction(event -> {
	        ArrayList<Photo> selectedPhotos = new ArrayList<>();
	        selectedPhotos.addAll (PhotoGrid.getSelectedPhotos ());
	        ArrayList<Image> selectedImages = new ArrayList<>();
	        selectedImages.addAll(PhotoGrid.getSelectedImages());

	        for (int i = 0; i < selectedPhotos.size(); i++) {
	            Photo currentPhoto = selectedPhotos.get(i);
	            PhotoGrid.getSelectedImages().remove(selectedImages.get(i));
	            PhotoGrid.getDisplayedImagesList().remove(selectedImages.get(i));
	            PhotoGrid.getSelectedPhotos().remove(selectedPhotos.get(i));
	            currentPhoto.delete();
	        }
	        ((GridViewSkin) PhotoGrid.getPhotosGrid().getSkin()).updateGridViewItems();
	    });
	}

	private void addDeselectAllEventHandler (PhotoGrid photoGrid) {
		deselectBtn.setOnAction(event -> {
	        PhotoGrid.getSelectedImages().clear();
	        PhotoGrid.getSelectedPhotos ().clear ();
	        ((GridViewSkin) PhotoGrid.getPhotosGrid().getSkin()).updateGridViewItems ();
	        photoGrid.getTaggingArea ().update ();

	        if (PhotoGrid.getSelectedImages().isEmpty() && SliderBar.counter >= 1) {
	            SliderBar.hidePanel.play();
	            SliderBar.counter = PhotoGrid.getSelectedImages().size();
	        }
	    });
	}

	private void addSelectAllEventHandler (PhotoGrid photoGrid) {
		selectBtn.setOnAction(event -> {
	        /**
	         * TODO: Fis this part with selectALL please
	         */
	        System.out.println (SliderBar.counter);
	        PhotoGrid.getSelectedImages ().clear();
	        PhotoGrid.getSelectedImages ().addAll(PhotoGrid.getDisplayedImagesList ());
	        PhotoGrid.getSelectedPhotos().clear ();
	        PhotoGrid.getSelectedPhotos ().addAll (PhotoGrid.getDisplayedPhotos ());
	        while(SliderBar.counter==0){
	            ((GridViewSkin) PhotoGrid.getPhotosGrid().getSkin()).updateGridViewItems();
	            photoGrid.getTaggingArea().update();

	            SliderBar.counter = PhotoGrid.getSelectedImages().size();
	            SliderBar.showPanel.play();
	        }
	    });
	}

	private void addZoomPlusEventHandler (PhotoGrid photoGrid) {
		zoomPlusBtn.setOnAction(event -> {
	        double cellWidth = photoGrid.getCellWidth();
	        double cellHeight = photoGrid.getCellHeight();
	        if (cellWidth < 900 & cellHeight < 900) {
	            photoGrid.setCellWidth(cellWidth + 100);
	            photoGrid.setCellHeight(cellHeight + 100);
	        }
	    });
	}

	private void addZoomMinusEventHandler (PhotoGrid photoGrid) {
		zoomMinusBtn.setOnAction(event -> {
		    double cellWidth = photoGrid.getCellWidth();
		    double cellHeight = photoGrid.getCellHeight();
		    if (cellWidth > 100 & cellHeight > 100) {
		        photoGrid.setCellWidth(cellWidth - 100);
		        photoGrid.setCellHeight(cellHeight - 100);
		    }
		});
	}

	public void setupNodeStyle(Node node, String nodeName) {
        node.setStyle("-fx-text-fill: antiquewhite");
        node.getStyleClass().add(nodeName);
        if (node.getClass().equals(Button.class))
            ((Button) node).setMinWidth(130);
    }

}