package synergy.views;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Created by Josef on 19/03/2015.
 */

public class SliderBar extends VBox {

    private Pos flapBarLocation;
    public static Animation showPanel, hidePanel;
	private static boolean displayed = false;

	public static boolean isDisplayed () {
		return displayed;
	}

	public static void show() {
		if ( !isDisplayed () ) {
			displayed = true;
			showPanel.play ();
		}
	}
	public static void hide() {
		if ( isDisplayed () ) {
//			displayed = false;
//			hidePanel.play ();
			Main.taggingArea.getLocationPane().update ();
		}
	}

	private static final double EXPANDED_SIZE = 300;

	public SliderBar(Pos location, TaggingArea area) {
		setVisible (true);
        if (location == null) {
            flapBarLocation = Pos.TOP_CENTER;
        }
        flapBarLocation = location;
        initPosition();
	    setupAnimations ();
        getChildren ().addAll(area);
		show();
    }

	private void setupAnimations () {
		hidePanel = new Transition () {
		    {
		        setCycleDuration(Duration.millis (250));
		    }

		    @Override
		    protected void interpolate(double fraction) {
		        final double size = EXPANDED_SIZE * (1.0 - fraction);
		        translateByPos(size);
		    }
		};

		showPanel = new Transition() {
		    {
		        setCycleDuration(Duration.millis(250));
		    }

		    @Override
		    protected void interpolate(double fraction) {
		        final double size = EXPANDED_SIZE * fraction;
		        translateByPos(size);
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

}