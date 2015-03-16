package synergy.views;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import synergy.models.Photo;
import synergy.models.Tag;

import java.time.LocalDate;
import java.util.*;

/**
 * Created by Cham on 06/03/2015.
 */
public class SearchField extends HBox {

    MultipleDatePickerSelection datePicker;
    HBox queryFieldAndSearch; //this is where the Button, TextField and Search Button go
    HBox searchQueryButtons;
    ComboBox comboBox;

    HBox buttonPane; // This is where LocationA and LocationB button goes
    ToggleButton locationA, locationB;
    Button searchButton, addButton;

    Set<String> listOfSearch;
    private int minHeight;

    private String[] mockChildrenData = { "alex", "cham", "codrin", "sari", "josef", "amit", "mike", "tobi"};
	private PhotoGrid photoGrid;

    public SearchField(PhotoGrid photoGrid) {
	    this.photoGrid = photoGrid;
        listOfSearch = new HashSet<>();
        getStyleClass().addAll("toggle-button");
        setUpUI();

    }

    public void setUpUI() {
        setUpTextFieldAndSearch();
        setUpDatePicker ();
        setUpLocationButtons ();

        getChildren().add(datePicker);
        getChildren().add(buttonPane);
        getChildren().add(queryFieldAndSearch);
    }

    public void setUpTextFieldAndSearch() {

        queryFieldAndSearch = new HBox();
        searchQueryButtons = new HBox();

        comboBox = new ComboBox();
        for (String childName : mockChildrenData) {
            comboBox.getItems().add(childName);
        }
        AutoCompleteComboBoxListener autoComplete = new AutoCompleteComboBoxListener(comboBox);
        comboBox.setOnKeyReleased(autoComplete);
        comboBox.setMaxWidth(Double.MAX_VALUE);
        queryFieldAndSearch.setHgrow(searchQueryButtons, Priority.ALWAYS);
        addButton = new Button("+");
        searchButton = new Button("Search");

        EventHandler eventHandler = event -> {
            addChildrenQuery ((String) comboBox.getValue ());
            updateChildrenQueries ();
        };

        addButton.setOnAction(eventHandler);
	    searchButton.setOnAction (event -> updateSearchDatabase());

        queryFieldAndSearch.getChildren().add(searchQueryButtons);
        queryFieldAndSearch.getChildren().add(comboBox);
        queryFieldAndSearch.getChildren().add(addButton);
        queryFieldAndSearch.getChildren().add(searchButton);
    }

    public void setUpDatePicker() {
        datePicker = new MultipleDatePickerSelection();
    }

    public TextField getDatePickerTextField() {
        return datePicker.getEditor();
    }

    public void setUpLocationButtons() {
        buttonPane = new HBox();
	    ToggleGroup toggleGroup = new ToggleGroup ();
        locationA = new ToggleButton("Room A");
        locationB = new ToggleButton("Room B");

	    locationA.setToggleGroup (toggleGroup);
	    locationB.setToggleGroup (toggleGroup);

        buttonPane.getChildren().add(locationA);
        buttonPane.getChildren().add(locationB);
    }

    public void addChildrenQuery(String addedQuery) {
        Set<String> hashSet = new HashSet<String>(Arrays.asList(mockChildrenData));
        if (listOfSearch.contains(addedQuery)) {

        } else if(hashSet.contains(addedQuery)){
            listOfSearch.add((String) comboBox.getValue());
        }
    }

    public void updateChildrenQueries(){
        searchQueryButtons.getChildren().clear ();
        for(String query: listOfSearch){
            Button queryButton = new Button(query + " - ");
            queryButton.setOnAction(event -> {
                listOfSearch.remove(query);
                updateChildrenQueries();
            });
            queryButton.setMinHeight(minHeight);
            queryButton.setStyle("-fx-text-fill: antiquewhite");
            searchQueryButtons.getChildren().add(queryButton);
        }
        comboBox.getEditor().setText("");
    }

    public ComboBox getComboBox() {
        return comboBox;
    }

    public void setAllMinHeight(int height) {
        this.minHeight = height;
        datePicker.setMinHeight(height - 5);
        buttonPane.setMinHeight(height);
        searchButton.setMinHeight(height);
        comboBox.setMinHeight(height - 5);
        locationA.setMinHeight(height);
        locationB.setMinHeight(height);
        addButton.setMinHeight(height);
    }


    public void updateSearchDatabase() {
        Set<String> listOfSearchedField = listOfSearch;
	    LocalDate date = datePicker.getValue ();
	    LocalDate initialDate = datePicker.getIniDate ();
	    LocalDate endDate = datePicker.getEndDate ();
	    final Date finalInitialDate;
	    final Date finalEndDate;
	    if ( date == null ) {
		    // use to/from
		    finalInitialDate = new Date(initialDate.toEpochDay ());
		    finalEndDate = new Date(endDate.toEpochDay ());
	    } else {
		    finalInitialDate = finalEndDate = new Date(date.toEpochDay ());
	    }
	    Tag roomTag = null ;
	    if ( locationA.isSelected () == true ) {
		    roomTag = new Tag(Tag.TagType.PLACE, locationA.getText ());
	    }
	    if ( locationB.isSelected () == true ) {
		    roomTag = new Tag(Tag.TagType.PLACE, locationB.getText ());
	    }

	    System.out.println(listOfSearchedField);
	    Tag kidTag = null;
	    if ( listOfSearchedField.size () > 0 ) {
		    String kid1 = listOfSearchedField.iterator ().next ();
		    kidTag = new Tag(Tag.TagType.KID, kid1);
	    }

	    List<Photo> photosFromDB = Photo.getPhotosForDatesAndRoomAndKid (
			    finalInitialDate, finalEndDate, roomTag, kidTag
	    );
	    //TODO: do something with photos from DB
    }
}
