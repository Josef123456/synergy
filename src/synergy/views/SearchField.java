package synergy.views;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.util.Callback;
import synergy.models.Photo;
import synergy.models.Tag;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Cham on 06/03/2015.
 */
public class SearchField extends HBox {

    DatePicker datePicker;
    ComboBox dateCategories;
    ComboBox months;
    StackPane stackCategories;
    DatePicker initialDatePicker, endDatePicker;
    HBox categoryAndItem;
    HBox periodPane; // no pun intended

    HBox queryFieldAndSearch; //this is where the Button, TextField and Search Button go
    HBox searchQueryButtons;
    ComboBox comboBox;

    HBox buttonPane; // This is where LocationA and LocationB button goes
    ToggleButton locationA, locationB;
    Button searchButton, addButton;

    Set<String> listOfSearch;
    private int minHeight;

    String[] arrayMonths = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    String[] arrayCategories = {"Date", "Month", "Period"};
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

        getChildren().add(categoryAndItem);
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
            addChildrenQuery((String) comboBox.getValue());
            updateChildrenQueries();
        };

        addButton.setOnAction(eventHandler);
	    searchButton.setOnAction (event -> updateSearchDatabase());

        queryFieldAndSearch.getChildren().add(searchQueryButtons);
        queryFieldAndSearch.getChildren().add(comboBox);
        queryFieldAndSearch.getChildren().add(addButton);
        queryFieldAndSearch.getChildren().add(searchButton);
    }

    public void setUpDatePicker() {
        datePicker = new DatePicker();
        categoryAndItem = new HBox();
        categoryAndItem.setSpacing(10);
        stackCategories = new StackPane();

        dateCategories = new ComboBox();
        dateCategories.getItems().addAll(arrayCategories);
        dateCategories.setValue(arrayCategories[0]);
        updateCategories();
        dateCategories.setOnAction(event -> updateCategories());

        months = new ComboBox();
        months.getItems().addAll(arrayMonths);

        periodPane = new HBox();
        periodPane.setSpacing(5);
        periodPane.setAlignment(Pos.CENTER);
        initialDatePicker = new DatePicker(LocalDate.now());
        endDatePicker = new DatePicker();
        final Callback<DatePicker, DateCell> initialDateDayCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                setMinSize(50, 50);
                            }
                        };
                    }
                };
        final Callback<DatePicker, DateCell> endDateDayCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                setMinSize(50, 50);
                                if (item.isBefore(
		                                initialDatePicker.getValue().plusDays(1))
                                        ) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }
                            }
                        };
                    }
                };
        datePicker.setDayCellFactory(initialDateDayCellFactory);//this is not part of the period pane, I just added her so the datecellfactory has been initiated
        datePicker.setShowWeekNumbers (false);
	    initialDatePicker.setDayCellFactory (initialDateDayCellFactory);
	    initialDatePicker.setMaxWidth(125);
	    initialDatePicker.setShowWeekNumbers(false);
	    endDatePicker.setDayCellFactory(endDateDayCellFactory);
	    endDatePicker.setMaxWidth(125);
	    endDatePicker.setShowWeekNumbers(false);

        Font font = new Font("Arial", 20);
        Label fromLabel = new Label("From: ");
        fromLabel.setFont(font);
        Label toLabel = new Label("To: ");
        toLabel.setFont(font);
        periodPane.getChildren().add(fromLabel);
        periodPane.getChildren().add(initialDatePicker);
        periodPane.getChildren().add(toLabel);
        periodPane.getChildren().add(endDatePicker);

        categoryAndItem.getChildren().add(dateCategories);
        categoryAndItem.getChildren().add(stackCategories);
    }

    public void updateCategories(){
        if(dateCategories.getValue().equals("Date")){
            stackCategories.getChildren().clear();
            stackCategories.getChildren().add(datePicker);
        } else if(dateCategories.getValue().equals("Month")){
            stackCategories.getChildren().clear();
            stackCategories.getChildren().add(months);
        } else if(dateCategories.getValue().equals("Period")){
            stackCategories.getChildren().clear();
            stackCategories.getChildren().add(periodPane);
        }
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
	    locationB.setToggleGroup(toggleGroup);

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
        categoryAndItem.setMinHeight(height);
        initialDatePicker.setMinHeight (height);
        endDatePicker.setMinHeight (height);
        dateCategories.setMinHeight(height);
        months.setMinHeight(height);
        buttonPane.setMinHeight(height);
        searchButton.setMinHeight(height);
        comboBox.setMinHeight(height - 5);
        locationA.setMinHeight(height);
        locationB.setMinHeight(height);
        addButton.setMinHeight(height);
    }


    public void updateSearchDatabase() {
        Set<String> listOfSearchedField = listOfSearch;
	    LocalDate date = datePicker.getValue();
	    LocalDate initialDate = initialDatePicker.getValue();
	    LocalDate endDate = endDatePicker.getValue ();

	    LocalDate finalInitialDate = null;
	    LocalDate finalEndDate = null ;

	    String selected = (String)dateCategories.getSelectionModel ().getSelectedItem ();

	    System.out.println ();

	    switch(selected) {
		    case "Date": {
			    if ( date != null ) {
				    finalInitialDate = finalEndDate = LocalDate.of (date.getYear (), date.getMonth (), date.getDayOfMonth ());
			    }
			    break ;
		    }
		    case "Period": {
			    //TODO: better handle this
			    if ( initialDate != null && endDate != null ) {
				    finalInitialDate = LocalDate.of (initialDate.getYear (), initialDate.getMonth (), initialDate.getDayOfMonth ());
				    finalEndDate = LocalDate.of (endDate.getYear (), endDate.getMonth (), endDate.getDayOfMonth ());
			    }
			    break ;
		    }
		    case "Month": {
			    //TODO: fix this
			    System.out.println("assign these");
			    System.out.println(months.getSelectionModel ().getSelectedItem ());
			    break;
		    }
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

	    System.out.println("final init date: " + finalInitialDate);
	    System.out.println("final end date: " + finalEndDate);
	    List<Photo> photosFromDB = Photo.getPhotosForDatesAndRoomAndKid (
			    finalInitialDate, finalEndDate, roomTag, kidTag
	    );

        PhotoGrid.getPhotosGrid().setGridPhotos(photosFromDB);
	    System.out.println("Photos from query: " + photosFromDB.size ());
    }
}
