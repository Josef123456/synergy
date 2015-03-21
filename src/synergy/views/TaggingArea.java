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
import synergy.views.panes.tagging.ChildrenPane;
import synergy.views.panes.tagging.DatePane;
import synergy.views.panes.tagging.LocationPane;
import synergy.views.panes.tagging.SuggestionsPane;
import synergy.views.panes.base.BaseHorizontalPane;
import synergy.views.panes.base.BaseVerticalPane;

/**
 * Created by Josef on 07/03/2015.
 */

public class TaggingArea extends BorderPane {

	private BaseVerticalPane childrenPane = new ChildrenPane (this);
	private BaseVerticalPane suggestionsPane = new SuggestionsPane (this);
	private BaseHorizontalPane locationPane = new LocationPane ();
	private BaseVerticalPane datePane = new DatePane (this);

    public void update(){
        childrenPane.update();
        locationPane.update ();
	    suggestionsPane.update ();
	    datePane.update ();
        if(PhotoGrid.getSelectedImages().size() == 0){
	        SliderBar.hide();
        } else{
	        SliderBar.show ();
        }
    }

	public BaseHorizontalPane getLocationPane () {
		return locationPane;
	}

	public TaggingArea() {
        setCenter (returnGridPane (locationPane, datePane, childrenPane, suggestionsPane));
    }

    private VBox returnGridPane(Pane box1, Pane box2, Pane box3, Pane box4) {
	    VBox hb = new VBox (10);
        hb.getStyleClass().add("hbox");
        hb.getChildren().addAll(box1, box2, box3, box4);
        return hb;
    }
}
