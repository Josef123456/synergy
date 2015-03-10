package synergy.newViews;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Created by Josef on 09/03/2015.
 */
public class ThumbnailArea extends VBox {

    private Button gridViewBtn, fullViewBtn, deleteBtn;
    private HBox centerbox, rightBox;
    private ToolBar toolbarBottom;

    public ThumbnailArea() {
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
