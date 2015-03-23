package synergy.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;
import synergy.views.panes.ChildrenPane;
import synergy.views.panes.DatePane;
import synergy.views.panes.LocationPane;
import synergy.views.panes.QueryPane;

/**
 * Created by Cham on 06/03/2015.
 */
public class SearchArea extends FlowPane {

    private ChildrenPane childrenPane;
    private QueryPane queryPane;
    private LocationPane locationPane;
    private DatePane datePane;

    public SearchArea() {
        setUpUI();
        getStyleClass().add("my-list-cell");
    }

    public ChildrenPane getChildrenPane() {
        return childrenPane;
    }

    public LocationPane getLocationPane() {
        return locationPane;
    }

    public DatePane getDatePane() {
        return datePane;
    }

    private void setUpUI() {
        childrenPane = new ChildrenPane();
        datePane = new DatePane();
        locationPane = new LocationPane();
        queryPane = new QueryPane(this);

        setPadding(new Insets(1, 135, 1, 0));
        setAlignment(Pos.CENTER);
        setHgap(10);
        getChildren().addAll(datePane, locationPane, childrenPane, queryPane);
    }
}
