package synergy.views;

import java.util.ArrayList;

import controlsfx.impl.org.controlsfx.skin.GridViewSkin;
import javafx.geometry.Insets;
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

/**
 * Created on 09/03/2015.
 * The BottomArea class is a VBox that includes buttons with the functionality of zooming in & out,
 * selecting & deselecting photos and deleting photos.
 */
public class BottomArea extends VBox {

    private Button deleteBtn, zoomMinusBtn, zoomPlusBtn, selectBtn, deselectBtn;

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

        Label zoomLabel = new Label("Zoom");
        setupNodeStyle(zoomLabel, "zoomLabel");

        zoomPlusBtn = new Button("+");
        setupNodeStyle(zoomPlusBtn, "zoomPlusBtn");
        zoomPlusBtn.setMinWidth(20);

        deselectBtn = new Button("Deselect All");
        setupNodeStyle(deselectBtn, "deselectButton");

        selectBtn = new Button("Select All");
        setupNodeStyle(selectBtn, "selectAllButton");

        deleteBtn = new Button("Delete");
        setupNodeStyle(deleteBtn, "deleteBtn");

        HBox zoomBox = new HBox(5);
        zoomBox.setPadding(new Insets(0, 0, 0, 35));
        zoomBox.getChildren().addAll(zoomMinusBtn, zoomLabel, zoomPlusBtn);
        zoomBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(zoomBox, Priority.ALWAYS);

        HBox selectBox = new HBox(1);
        selectBox.getChildren().addAll(selectBtn, deselectBtn);
        selectBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(selectBox, Priority.ALWAYS);

        HBox rightBox = new HBox();
        rightBox.setPadding(new Insets(0, 35, 0, 0));
        rightBox.getChildren().add(deleteBtn);
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(rightBox, Priority.ALWAYS);

        bottomBar.getItems().addAll(zoomBox, selectBox, rightBox);

        this.getChildren().addAll(bottomBar);
    }

    public void addEventHandlers() {
        PhotoGrid photoGrid = PhotoGrid.getPhotosGrid();

        addZoomMinusEventHandler(photoGrid);
        addZoomPlusEventHandler(photoGrid);
        addSelectAllEventHandler(photoGrid);
        addDeselectAllEventHandler(photoGrid);
        addDeleteEventHandler(photoGrid);
    }

    private void addDeleteEventHandler(PhotoGrid photoGrid) {
        deleteBtn.setOnAction(event -> {
            ArrayList<Image> selectedImages = new ArrayList<>();
            selectedImages.addAll(PhotoGrid.getSelectedImages());

            ArrayList<Photo> selectedPhotos = new ArrayList<>();
            selectedPhotos.addAll(PhotoGrid.getSelectedPhotos());

            PhotoGrid.getSelectedImages().clear();
            PhotoGrid.getSelectedPhotos().clear();

	        PhotoGrid.getDisplayedPhotos ().removeAll(selectedPhotos);
            PhotoGrid.getDisplayedImagesList().removeAll(selectedImages);
            PhotoGrid.getDisplayedImagesMap().keySet().removeAll(selectedPhotos);

            selectedPhotos.stream().forEach(Photo::delete);
            photoGrid.getTaggingArea().update();

            SliderBar.hide();
            ((GridViewSkin) PhotoGrid.getPhotosGrid().getSkin()).updateGridViewItems();
        });
    }

    private void addDeselectAllEventHandler(PhotoGrid photoGrid) {
        deselectBtn.setOnAction(event -> {
            PhotoGrid.getSelectedImages().clear();
            PhotoGrid.getSelectedPhotos().clear();
            ((GridViewSkin) PhotoGrid.getPhotosGrid().getSkin()).updateGridViewItems();
            photoGrid.getTaggingArea().update();

            SliderBar.hide();
        });
    }

    private void addSelectAllEventHandler(PhotoGrid photoGrid) {
        selectBtn.setOnAction(event -> {
            PhotoGrid.getSelectedImages().clear();
            PhotoGrid.getSelectedImages().addAll(PhotoGrid.getDisplayedImagesList());
            PhotoGrid.getSelectedPhotos().clear();
            PhotoGrid.getSelectedPhotos().addAll(PhotoGrid.getDisplayedPhotos());

            ((GridViewSkin) PhotoGrid.getPhotosGrid().getSkin()).updateGridViewItems();
            photoGrid.getTaggingArea().update();

            SliderBar.show();
        });
    }

    private void addZoomPlusEventHandler(PhotoGrid photoGrid) {
        zoomPlusBtn.setOnAction(event -> {
            double cellWidth = photoGrid.getCellWidth();
            double cellHeight = photoGrid.getCellHeight();
            if (cellWidth < 900 & cellHeight < 900) {
                photoGrid.setCellWidth(cellWidth + 100);
                photoGrid.setCellHeight(cellHeight + 100);
            }
        });
    }

    private void addZoomMinusEventHandler(PhotoGrid photoGrid) {
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
        node.setStyle("-fx-text-fill: #ffffff");
        node.getStyleClass().add(nodeName);
        if (node.getClass().equals(Button.class))
            ((Button) node).setMinWidth(160);
    }

}