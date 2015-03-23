package synergy.views;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Created by Josef on 19/03/2015.
 * The SlideBar class provides the feature of the TaggingArea sliding in and out
 * if the pictures are selected or unselected. It provides an animation.
 */

public class SliderBar extends VBox {

    private Pos flapBarLocation;
    public static Animation showPanel, hidePanel;
    private static boolean displayed = false;

    public static boolean isDisplayed() {
        return displayed;
    }

    public static void show() {
        System.out.println("showing");
        if (!isDisplayed()) {
            displayed = true;
            hidePanel.play();
        }
    }

    public static void hide() {
        System.out.println("hiding");
        if (isDisplayed()) {
            displayed = false;
            showPanel.play();
            Main.taggingArea.getLocationPane().update();
        }
    }

    private static final double EXPANDED_SIZE = 350;

    public SliderBar(Pos location, TaggingArea area) {
        if (location == null) {
            flapBarLocation = Pos.BOTTOM_RIGHT;
        }
        flapBarLocation = location;
        initPosition();
        setupAnimations();
        getChildren().addAll(area);
        //	hide();
//		show();
    }

    private void setupAnimations() {
        hidePanel = new Transition() {
            {
                setCycleDuration(Duration.millis(250));
            }

            @Override
            protected void interpolate(double fraction) {
                final double size = EXPANDED_SIZE * (1.0 - fraction);
//		        translateByPos(size);
                setTranslateX(size);
//			    setVisible (false);
            }
        };

        showPanel = new Transition() {
            {
                setCycleDuration(Duration.millis(250));
            }

            @Override
            protected void interpolate(double fraction) {
                final double size = EXPANDED_SIZE * fraction;
                setTranslateX(size);
                setVisible(true);
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