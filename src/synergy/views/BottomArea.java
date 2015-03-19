package synergy.views;

import java.io.File;
import java.util.ArrayList;

import controlsfx.controlsfx.control.GridView;
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

/**
 * Created on 09/03/2015.
 * Codrin make it the way you like it. I prefer the "thumbnailpictures" to be in this class.
 */
public class BottomArea extends VBox {

    private Button gridViewBtn, fullViewBtn, deleteBtn, zoomMinusBtn, zoomPlusBtn, deselectBtn,
            selectBtn;
    private Label zoomLabel;
    private HBox zoomBox, rightBox, leftBox, selectBox;

    public BottomArea() {
        getStyleClass().setAll("button-bar");
        initBottomArea();
        addEventHandlers();
    }

    public void initBottomArea() {
        ToolBar bottomBar = new ToolBar();

        gridViewBtn = new Button("Grid");
        setupNodeStyle(gridViewBtn, "gridViewBtn");

        fullViewBtn = new Button("Full");
        setupNodeStyle(fullViewBtn, "fullViewBtn");

        zoomMinusBtn = new Button("-");
        setupNodeStyle(zoomMinusBtn, "zoomMinusBtn");
        zoomMinusBtn.setMinWidth(20);

        zoomLabel = new Label("Zoom");
        setupNodeStyle(zoomLabel, "zoomLabel");

        zoomPlusBtn = new Button("+");
        setupNodeStyle(zoomPlusBtn, "zoomPlusBtn");
        zoomPlusBtn.setMinWidth(20);

        deselectBtn = new Button("Deselect All");
        setupNodeStyle(deselectBtn, "deselectBtn");

        selectBtn = new Button("Select All");
        setupNodeStyle(selectBtn, "selectBtn");

        deleteBtn = new Button("Delete");
        setupNodeStyle(deleteBtn, "deleteBtn");

        leftBox = new HBox();
        leftBox.getChildren().addAll(gridViewBtn, fullViewBtn);
        leftBox.setAlignment(Pos.CENTER_LEFT);

        zoomBox = new HBox(5);
        zoomBox.getChildren().addAll(zoomMinusBtn, zoomLabel, zoomPlusBtn);
        zoomBox.setAlignment(Pos.CENTER);
        zoomBox.setHgrow(zoomBox, Priority.ALWAYS);

        selectBox = new HBox();
        selectBox.getChildren().addAll(selectBtn, deselectBtn);
        selectBox.setAlignment(Pos.CENTER);
        selectBox.setHgrow(selectBox, Priority.ALWAYS);

        rightBox = new HBox();
        rightBox.getChildren().add(deleteBtn);
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        rightBox.setHgrow(rightBox, Priority.ALWAYS);

        bottomBar.getItems().addAll(leftBox, zoomBox, selectBox, rightBox);

        this.getChildren().addAll(bottomBar);
    }

    public void addEventHandlers() {
        GridView photoGrid = PhotoGrid.getPhotosGrid();

        zoomMinusBtn.setOnAction(new javafx.event.EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                double cellWidth = photoGrid.getCellWidth();
                double cellHeight = photoGrid.getCellHeight();
                if (cellWidth > 100 & cellHeight > 100) {
                    photoGrid.setCellWidth(cellWidth - 100);
                    photoGrid.setCellHeight(cellHeight - 100);
                }
            }
        });

        zoomPlusBtn.setOnAction(new javafx.event.EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                double cellWidth = photoGrid.getCellWidth();
                double cellHeight = photoGrid.getCellHeight();
                if (cellWidth < 900 & cellHeight < 900) {
                    photoGrid.setCellWidth(cellWidth + 100);
                    photoGrid.setCellHeight(cellHeight + 100);
                }
            }
        });

        selectBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PhotoGrid.getSelectedImages().clear();
                PhotoGrid.getSelectedImages().addAll(PhotoGrid.getDisplayedImagesList());
                PhotoGrid.getSelectedPhotos().clear();
                PhotoGrid.getSelectedPhotos().addAll(PhotoGrid.getDisplayedPhotos());

                ((GridViewSkin) PhotoGrid.getPhotosGrid().getSkin()).updateGridViewItems();
            }
        });

        deselectBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PhotoGrid.getSelectedImages().clear();
                PhotoGrid.getSelectedPhotos().clear();
                ((GridViewSkin) PhotoGrid.getPhotosGrid().getSkin()).updateGridViewItems();
            }
        });

        deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ArrayList<Photo> selectedPhotos = new ArrayList<> ();
                selectedPhotos.addAll (PhotoGrid.getSelectedPhotos ());
                ArrayList<Image> selectedImages = new ArrayList<> ();
                selectedImages.addAll (PhotoGrid.getSelectedImages ());

                for (int i = 0 ; i < selectedPhotos.size(); i++){
                    Photo currentPhoto = selectedPhotos.get(i);
                    PhotoGrid.getSelectedImages().remove(selectedImages.get (i));
                    PhotoGrid.getDisplayedImagesList().remove(selectedImages.get(i));
                    PhotoGrid.getSelectedPhotos().remove(selectedPhotos.get(i));
                    currentPhoto.delete();
                }
                ((GridViewSkin) PhotoGrid.getPhotosGrid().getSkin()).updateGridViewItems();
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