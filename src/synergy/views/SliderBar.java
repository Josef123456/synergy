package synergy.views;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Created by Josef on 19/03/2015.
 */

public class SliderBar extends VBox {

    private double expandedSize;
    private Pos flapbarLocation;
    int counter;


    public SliderBar(double expandedSize,  PhotoGrid photo, Pos location, TaggingArea area) {

        setExpandedSize(expandedSize);
        setVisible(false);

        if (location == null) {
            flapbarLocation = Pos.TOP_CENTER;
        }
        flapbarLocation = location;

        initPosition();

        getChildren().addAll(area);

        photo.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {

                final Animation hidePanel = new Transition() {
                    {
                        setCycleDuration(Duration.millis(250));
                    }

                    @Override
                    protected void interpolate(double frac) {
                        final double size = getExpandedSize() * (1.0 - frac);
                        translateByPos(size);
                    }
                };

                hidePanel.onFinishedProperty().set(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        setVisible(false);
                    }
                });

                final Animation showPanel = new Transition() {
                    {
                        setCycleDuration(Duration.millis(250));
                    }

                    @Override
                    protected void interpolate(double frac) {
                        final double size = getExpandedSize() * frac;
                        translateByPos(size);
                    }
                };

                showPanel.onFinishedProperty().set(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                    }
                });

                if (showPanel.statusProperty().get() == Animation.Status.STOPPED
                        && hidePanel.statusProperty().get() == Animation.Status.STOPPED) {

                    if (!(PhotoGrid.getSelectedImages().isEmpty())) {

                           if (counter==0) {
                               setVisible(true);
                               showPanel.play();
                           }

                    }else if (PhotoGrid.getSelectedImages().isEmpty()){
                        hidePanel.play();
                    }
                    counter= PhotoGrid.getSelectedImages().size();
                    System.out.println(counter+"sef");

                }
            }
        });
    }
    private void initPosition() {
        switch (flapbarLocation) {
            case BASELINE_RIGHT:
                setPrefWidth(0);
                setMinWidth(0);
                break;
        }
    }

    private void translateByPos(double size) {
        switch (flapbarLocation) {
            case BASELINE_RIGHT:
                setPrefWidth(size);
                break;
        }
    }

    public double getExpandedSize() {
        return expandedSize;
    }


    public void setExpandedSize(double expandedSize) {
        this.expandedSize = expandedSize;
    }

}