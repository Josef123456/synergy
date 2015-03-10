package synergy.newViews;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Created by Josef on 09/03/2015.
 */
public class ThumbnailArea extends VBox {

    GridPane bottomgrid;
    Button gridView, fullView;
    VBox boxslider;
    Label zoom;

    public ThumbnailArea() {
        getStyleClass().setAll("button-bar");
        getChildren().addAll(bottomArea());
    }

    /**
     * Whoever is working on thumbnailView. please insert code inside this method.
     */
    public void thumbnailView(){

    }

    public GridPane bottomArea() {
        bottomgrid = new GridPane();

        zoom = new Label("                     Zoom");
        zoom.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        zoom.setStyle("-fx-text-fill: antiquewhite");

        Slider slider = new Slider(0, 1, 0.5);

        boxslider = new VBox();
        boxslider.getChildren().addAll(zoom, slider);

        gridView = new Button("Grid");
        gridView.setStyle("-fx-text-fill: antiquewhite");
        gridView.setMinWidth(130);
        fullView = new Button("Full");
        fullView.setStyle("-fx-text-fill: antiquewhite");
        fullView.setMinWidth(130);

        bottomgrid.add(boxslider, 0, 0);
        bottomgrid.add(gridView, 3, 0);
        bottomgrid.add(fullView, 4, 0);


        return bottomgrid;
    }
}
