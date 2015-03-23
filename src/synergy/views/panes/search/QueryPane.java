package synergy.views.panes.search;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import synergy.models.Photo;
import synergy.models.Tag;
import synergy.views.PhotoGrid;
import synergy.views.SearchArea;

/**
 * Generates an HBox which contains 2 buttons: search & reset. These buttons use the information from the rest of the
 * SearchArea to create the right query for the database.
 * Created by alexstoick on 3/21/15.
 */
public class QueryPane extends HBox {

    private Button resetButton;
    private SearchArea searchArea;
	private LocalDate finalInitialDate = null;
	private LocalDate finalEndDate = null;

	/**
	 * Constructor for the QueryPane. This generates a HBox which is then displayed within SearchArea
	 * @param searchArea the area in which the pane will be displayed. This is used for triggering button actions
	 */
    public QueryPane(SearchArea searchArea) {
        this.searchArea = searchArea;
        setAlignment(Pos.CENTER);
        setUpResetButton();
        getStyleClass().add ("my-list-cell");
        Button searchButton = new Button("Search");
        searchButton.setMinWidth(80);
        setSpacing (1);
        searchButton.setOnAction (event -> updateSearchDatabase ());
        int height = 50;
        searchButton.setMinHeight(height);
        resetButton.setMinHeight(height);
        getChildren().addAll (searchButton, resetButton);
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

    private void updateSearchDatabase() {
        computeSelectedDates();
        Tag roomTag = computeRoomTag();
        List<Tag> kidTags = computeKidTag();

        System.out.println("QUERY PANE: final init date: " + finalInitialDate);
        System.out.println("QUERY PANE: final end date: " + finalEndDate);
        List<Photo> photosFromDB = Photo.getPhotosForDatesAndRoomAndKids (
		        finalInitialDate, finalEndDate, roomTag, kidTags
        );

        if (!(finalEndDate == null && finalEndDate == null && roomTag == null && kidTags == null)) {
            PhotoGrid.displayingImported = false;
        } else {
            PhotoGrid.displayingImported = true;
        }

        PhotoGrid.setGridPhotos (photosFromDB);
        System.out.println("Photos from query: " + photosFromDB.size());
    }

    private void computeSelectedDates() {
        LocalDate date = searchArea.getDatePane().getSingleDatePicker().getValue();
        LocalDate initialDate = searchArea.getDatePane().getInitialDatePicker().getValue();
        LocalDate endDate = searchArea.getDatePane().getEndDatePicker().getValue();

        String selected = (String) searchArea.getDatePane().
                getDateCategories ().getSelectionModel().getSelectedItem();
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
                if (initialDate != null && endDate != null) {
                    finalInitialDate = LocalDate.of(initialDate.getYear(), initialDate.getMonth()
                            , initialDate.getDayOfMonth());
                    finalEndDate = LocalDate.of(endDate.getYear(), endDate.getMonth(), endDate
                            .getDayOfMonth ());
                }
                break;
            }
            case "Month": {
                System.out.println("assign these");
	            SelectionModel monthsSelectionModel = searchArea.getDatePane ().getMonths().getSelectionModel();
	            SelectionModel yearsSelectionModel = searchArea.getDatePane ().getYears ().getSelectionModel ();

	            System.out.println ( "$$$$$" + monthsSelectionModel.getSelectedIndex () +
			            " " + yearsSelectionModel.getSelectedIndex ());

	            if ( monthsSelectionModel.getSelectedIndex () > -1 && yearsSelectionModel.getSelectedIndex () > -1 ) {
		            int selectedMonth = monthsSelectionModel.getSelectedIndex ()+1;
		            int selectedYear = Integer.parseInt ((String)yearsSelectionModel.getSelectedItem ());
		            finalInitialDate = LocalDate.of (selectedYear, selectedMonth, 1);
		            finalEndDate = LocalDate.of (selectedYear, selectedMonth + 1, 1);
	            }
                break;
            }
        }
    }

    private List<Tag> computeKidTag() {
        Set<String> listOfSearchedField = searchArea.getChildrenPane().getListOfSearchedKids();
	    if(listOfSearchedField.size() == 0 ) {
		    return null;
	    }
	    return listOfSearchedField.stream ().map (name -> new Tag (Tag.TagType.KID, name)).collect (Collectors.toList ());
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
