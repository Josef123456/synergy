package synergy.views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Callback;
import synergy.models.Photo;
import synergy.models.Tag;
import synergy.views.panes.ChildrenPane;
import synergy.views.panes.DatePane;
import synergy.views.panes.LocationPane;
import synergy.views.panes.QueryPane;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by Cham on 06/03/2015.
 */
public class SearchArea extends HBox {

	ChildrenPane childrenPane;
	QueryPane queryPane;
	LocationPane locationPane;
	DatePane datePane;

	public ChildrenPane getChildrenPane () {
		return childrenPane;
	}

	public LocationPane getLocationPane () {
		return locationPane;
	}

	public DatePane getDatePane () {
		return datePane;
	}

    public SearchArea () {
        setUpUI();
	    getStyleClass().add("my-list-cell");
    }

    public void setUpUI() {
	    childrenPane = new ChildrenPane ();
	    datePane = new DatePane ();
        locationPane = new LocationPane ();
	    queryPane = new QueryPane (this);

        setSpacing(10);
	    setAlignment (Pos.CENTER);
	    getChildren ().addAll(datePane, locationPane, childrenPane, queryPane);
    }
}
