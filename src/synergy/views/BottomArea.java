package synergy.views;

import controlsfx.controlsfx.control.GridView;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import synergy.models.Photo;

import java.io.File;
import java.util.ArrayList;

/**
 * Created on 09/03/2015.
 * Codrin make it the way you like it. I prefer the "thumbnailpictures" to be in this class.
 */
public class BottomArea extends VBox {

    private Button gridViewBtn, fullViewBtn, deleteBtn, zoomMinusBtn, zoomPlusBtn;
    private Label zoomLabel;
    private HBox centerBox, rightBox, leftBox;

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

        deleteBtn = new Button("Delete");
        setupNodeStyle(deleteBtn, "deleteBtn");

        leftBox = new HBox();
        leftBox.getChildren().addAll(gridViewBtn, fullViewBtn);
        leftBox.setAlignment(Pos.CENTER_LEFT);
        deleteBtn.setOnAction(event->{
            ArrayList<Photo> photos = PhotoGrid.getSelectedPhotos();
            for (int i = 0 ; i < photos.size(); i++){
                File f = new File(photos.get(i).getPath());
                PhotoGrid.getSelectedImages().remove(i);
                PhotoGrid.getDisplayedImagesList().remove(i);
                PhotoGrid.getSelectedPhotos().remove(i);
                f.delete();
            }

        });

        centerbox = new HBox();
        centerbox.getChildren().addAll(gridViewBtn, fullViewBtn);
        centerbox.setAlignment(Pos.CENTER_LEFT);

        centerBox = new HBox(5);
        centerBox.getChildren().addAll(zoomMinusBtn, zoomLabel, zoomPlusBtn);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setHgrow(centerBox, Priority.ALWAYS);

        rightBox = new HBox();
        rightBox.getChildren().add(deleteBtn);
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        rightBox.setHgrow(rightBox, Priority.ALWAYS);

        bottomBar.getItems().addAll(leftBox, centerBox, rightBox);

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
                if (cellWidth < 1000 & cellHeight < 1000) {
                    photoGrid.setCellWidth(cellWidth + 100);
                    photoGrid.setCellHeight(cellHeight + 100);
                }
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