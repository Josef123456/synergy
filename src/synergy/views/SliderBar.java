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
    private Pos flapBarLocation;
    public static int counter;
    public static Animation showPanel, hidePanel;


    public SliderBar(double expandedSize, PhotoGrid photo, Pos location, TaggingArea area) {
        setExpandedSize(expandedSize);
        setVisible(false);

        if (location == null) {
            flapBarLocation = Pos.TOP_CENTER;
        }
        flapBarLocation = location;

        initPosition();

	    setupAnimations ();

        getChildren().addAll(area);

        photo.setOnMouseClicked(event -> {
            if (!(PhotoGrid.getSelectedImages().isEmpty())) {
                if (counter == 0) {
                    showPanel.play();
                    counter++;
                }
            } else if (PhotoGrid.getSelectedImages().isEmpty()) {
                hidePanel.play();
            }
            counter = PhotoGrid.getSelectedImages().size();
            System.out.println(counter + "counter");
        });
    }

	private void setupAnimations () {
		hidePanel = new Transition () {
		    {
		        setCycleDuration(Duration.millis (250));
		    }

		    @Override
		    protected void interpolate(double fraction) {
		        final double size = getExpandedSize() * (1.0 - fraction);
		        translateByPos(size);
		    }
		};

		hidePanel.onFinishedProperty().set(actionEvent -> setVisible(false));

		showPanel = new Transition() {
		    {
		        setCycleDuration(Duration.millis(250));
		    }

		    @Override
		    protected void interpolate(double fraction) {
		        final double size = getExpandedSize() * fraction;
		        translateByPos(size);
		        setVisible (true);
		    }
		};
	}

	private void initPosition() {
        switch (flapBarLocation) {
            case BASELINE_RIGHT:
                setPrefWidth(0);
                setMinWidth(0);
                break;
        }
    }

    private void translateByPos(double size) {
        switch (flapBarLocation) {
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