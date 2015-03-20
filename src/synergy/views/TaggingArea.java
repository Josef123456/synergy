package synergy.views;

import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import synergy.views.panes.ChildrenPane;
import synergy.views.panes.DatePane;
import synergy.views.panes.LocationPane;
import synergy.views.panes.SuggestionsPane;
import synergy.views.panes.base.BaseHorizontalPane;
import synergy.views.panes.base.BaseVerticalPane;

/**
 * Created by Josef on 07/03/2015.
 */

public class TaggingArea extends BorderPane {

	private BaseVerticalPane childrenPane = new ChildrenPane (this);
	private BaseVerticalPane suggestionsPane = new SuggestionsPane (this);
	private BaseHorizontalPane locationPane = new LocationPane (this);
	private BaseVerticalPane datePane = new DatePane (this);
    private static final double BLUR_AMOUNT = 60;
    private static final Effect frostEffect = new BoxBlur(BLUR_AMOUNT, BLUR_AMOUNT, 3);

    public void update(){
        childrenPane.update();
        locationPane.update ();
	    suggestionsPane.update ();
	    datePane.update ();
        if(PhotoGrid.getSelectedImages().size() == 0){
            this.setVisible(false);
        } else{
            this.setVisible(true);
        }
    }

    public TaggingArea() {

        setCenter (returnGridPane (locationPane, datePane, childrenPane, suggestionsPane ));
    }

    private VBox returnGridPane(Pane box1, Pane box2, Pane box3, Pane box4) {

        VBox hb = freeze(Main.root);
        hb.getStyleClass().add("hbox");
        hb.getChildren().addAll(box1, box2, box3, box4);
        return hb;
    }

    private VBox freeze(Node background) {
        Image frostImage = background.snapshot(
                new SnapshotParameters(),
                null
        );
        ImageView frost = new ImageView(frostImage);


        Pane frostPane = new Pane(frost);
        frostPane.setEffect(frostEffect);

        VBox frostView = new VBox(
                frostPane
        );

        return frostView;
    }
}
