package synergy.views;

import javafx.scene.layout.*;
import synergy.views.panes.*;
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
        VBox hb = new VBox(15);
        hb.getStyleClass().add("hbox");
        hb.getChildren().addAll(box1, box2, box3, box4);
        return hb;
    }





}
