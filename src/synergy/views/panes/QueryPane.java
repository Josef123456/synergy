package synergy.views.panes;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import synergy.models.Photo;
import synergy.models.Tag;
import synergy.views.PhotoGrid;
import synergy.views.SearchArea;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * Created by alexstoick on 3/21/15.
 */
public class QueryPane extends HBox {

    private Button resetButton;
    private SearchArea searchArea;

    public QueryPane(SearchArea searchArea) {
        this.searchArea = searchArea;
        setAlignment(Pos.CENTER);
        setUpResetButton();
        getStyleClass().add("my-list-cell");
        Button searchButton = new Button("Search");
        searchButton.setMinWidth(80);
        setSpacing(1);
        searchButton.setOnAction(event -> updateSearchDatabase());
        int height = 50;
        searchButton.setMinHeight(height);
        resetButton.setMinHeight(height);
        getChildren().addAll(searchButton, resetButton);
    }

    private void setUpResetButton() {
        resetButton = new Button("Reset");
        resetButton.setMinWidth(80);
        resetButton.setOnAction(event -> {
            searchArea.getDatePane().resetAll();
            searchArea.getLocationPane().resetAll();
            searchArea.getChildrenPane().resetAll();
        });
    }

    private LocalDate finalInitialDate = null;
    private LocalDate finalEndDate = null;

    private void updateSearchDatabase() {
        computeSelectedDates();
        Tag roomTag = computeRoomTag();
        Tag kidTag = computeKidTag();

        System.out.println("final init date: " + finalInitialDate);
        System.out.println("final end date: " + finalEndDate);
        List<Photo> photosFromDB = Photo.getPhotosForDatesAndRoomAndKid(
                finalInitialDate, finalEndDate, roomTag, kidTag
        );

        if (!(finalEndDate == null && finalEndDate == null && roomTag == null && kidTag == null)) {
            PhotoGrid.displayingImported = false;
        } else {
            PhotoGrid.displayingImported = true;
        }

        PhotoGrid.getPhotosGrid().setGridPhotos(photosFromDB);
        System.out.println("Photos from query: " + photosFromDB.size());
    }

    private void computeSelectedDates() {
        LocalDate date = searchArea.getDatePane().getSingleDatePicker().getValue();
        LocalDate initialDate = searchArea.getDatePane().getInitialDatePicker().getValue();
        LocalDate endDate = searchArea.getDatePane().getEndDatePicker().getValue();

        String selected = (String) searchArea.getDatePane().
                getDateCategories().getSelectionModel().getSelectedItem();
        finalInitialDate = finalEndDate = null;
        switch (selected) {
            case "Date": {
                if (date != null) {
                    finalInitialDate = finalEndDate = LocalDate.of(date.getYear(), date.getMonth
                            (), date.getDayOfMonth());
                }
                break;
            }
            case "Period": {
                //TODO: better handle this
                if (initialDate != null && endDate != null) {
                    finalInitialDate = LocalDate.of(initialDate.getYear(), initialDate.getMonth()
                            , initialDate.getDayOfMonth());
                    finalEndDate = LocalDate.of(endDate.getYear(), endDate.getMonth(), endDate
                            .getDayOfMonth());
                }
                break;
            }
            case "Month": {
                //TODO: fix this
                System.out.println("assign these");
                System.out.println(searchArea.getDatePane().getMonths().getSelectionModel().getSelectedItem());
                System.out.println(searchArea.getDatePane().getYears().getSelectionModel().getSelectedItem());
                break;
            }
        }
    }

    private Tag computeKidTag() {
        Set<String> listOfSearchedField = searchArea.getChildrenPane().getListOfSearchedKids();

        if (listOfSearchedField.size() > 0) {
            String kid1 = listOfSearchedField.iterator().next();
            return new Tag(Tag.TagType.KID, kid1);
        }
        return null;
    }

    private Tag computeRoomTag() {
        ToggleButton locationA = searchArea.getLocationPane().getLocationA();
        if (locationA.isSelected()) {
            return new Tag(Tag.TagType.PLACE, locationA.getText());
        }
        ToggleButton locationB = searchArea.getLocationPane().getLocationB();
        if (locationB.isSelected()) {
            return new Tag(Tag.TagType.PLACE, locationB.getText());
        }
        return null;
    }
}
