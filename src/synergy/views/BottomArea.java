package synergy.views;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
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

    private Button gridViewBtn, fullViewBtn, deleteBtn;
    private HBox centerbox, rightBox;
    private ToolBar toolbarBottom;

    public BottomArea() {
        getStyleClass().setAll("button-bar");
        getChildren().addAll(bottomArea());
    }

    /**
     * Whoever is working on thumbnailView. please insert code inside this method.
     */
    public void thumbnailView() {

    }

    public ToolBar bottomArea() {

        toolbarBottom = new ToolBar();
        gridViewBtn = new Button("Grid");
        setupButtonStyle(gridViewBtn, "firstButton");

        fullViewBtn = new Button("Full");
        setupButtonStyle(fullViewBtn, "secondButton");

        deleteBtn = new Button("Delete");
        setupButtonStyle(deleteBtn, "fourthButton");

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

        rightBox = new HBox();
        rightBox.getChildren().add(deleteBtn);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        centerbox.getChildren().addAll(rightBox);
        HBox.setHgrow(centerbox, Priority.ALWAYS);

        toolbarBottom.getItems().addAll(centerbox, deleteBtn);

        return toolbarBottom;
    }

    public void setupButtonStyle(Button btn, String buttonName) {
        btn.setStyle("-fx-text-fill: antiquewhite");
        btn.getStyleClass().add(buttonName);
        btn.setMinWidth(130);
    }

}