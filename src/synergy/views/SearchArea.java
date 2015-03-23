package synergy.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;
import synergy.views.panes.search.ChildrenPane;
import synergy.views.panes.search.DatePane;
import synergy.views.panes.search.LocationPane;
import synergy.views.panes.search.QueryPane;

/**
 * This search area contains all the panes - the children/query/location/date pane that allows that user to search
 * Created by Cham on 06/03/2015.
 */
public class SearchArea extends FlowPane {

    private ChildrenPane childrenPane;
    private QueryPane queryPane;
    private LocationPane locationPane;
    private DatePane datePane;

    /**
     * sets up the ui of the search area
     */
    public SearchArea() {
        setUpUI();
        getStyleClass().add("my-list-cell");
    }

    /**
     *
     * @return the children pane
     */
    public ChildrenPane getChildrenPane() {
        return childrenPane;
    }

    /**
     *
     * @return the location pane
     */
    public LocationPane getLocationPane() {
        return locationPane;
    }

    /**
     *
     * @return the date pane
     */
    public DatePane getDatePane() {
        return datePane;
    }

    private void setUpUI() {
        childrenPane = new ChildrenPane();
        datePane = new DatePane();
        locationPane = new LocationPane();
        queryPane = new QueryPane(this);

        setPadding (new Insets (1, 0, 1, 0));
        setAlignment(Pos.CENTER);
        setHgap(10);
        getChildren().addAll(datePane, locationPane, childrenPane, queryPane);
    }
}
