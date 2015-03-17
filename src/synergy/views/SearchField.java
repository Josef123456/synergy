package synergy.views;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.util.Callback;
import synergy.models.Photo;
import synergy.models.Tag;

/**
 * Created by Cham on 06/03/2015.
 */
public class SearchField extends HBox {

    DatePicker datePicker;
    ComboBox dateCategories;
    ComboBox months;
    StackPane stackCategories;
    DatePicker initialDate, endDate;
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
        dateCategories.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateCategories();
            }
        });

        months = new ComboBox();
        months.getItems().addAll(arrayMonths);

        periodPane = new HBox();
        periodPane.setSpacing(5);
        periodPane.setAlignment(Pos.CENTER);
        initialDate = new DatePicker(LocalDate.now());
        endDate = new DatePicker(LocalDate.now());
        final Callback<DatePicker, DateCell> dayCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item.isBefore(
                                        initialDate.getValue().plusDays(1))
                                        ) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }
                            }
                        };
                    }
                };
        endDate.setDayCellFactory(dayCellFactory);
        initialDate.setMaxWidth(125);
        endDate.setMaxWidth(125);
        Font font = new Font("Arial", 20);
        Label fromLabel = new Label("From: ");
        fromLabel.setFont(font);
        Label toLabel = new Label("To: ");
        toLabel.setFont(font);
        periodPane.getChildren().add(fromLabel);
        periodPane.getChildren().add(initialDate);
        periodPane.getChildren().add(toLabel);
        periodPane.getChildren().add(endDate);

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
        initialDate.setMinHeight(height);
        endDate.setMinHeight(height);
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
	    LocalDate initialDate = this.initialDate.getValue();
	    LocalDate endDate = this.endDate.getValue();

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

        PhotoGrid.getPhotosGrid().setGridPhotos(photosFromDB);
    }
}
