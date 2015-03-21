package synergy.views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.util.Callback;
import synergy.models.Photo;
import synergy.models.Tag;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by Cham on 06/03/2015.
 */
public class SearchField extends HBox {

    ComboBox dateCategories;
    DatePicker datePicker;
    HBox monthAndYear;
    ComboBox months, years;
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

    Button resetButton;

    Set<String> listOfSearch;
    private int minHeight;

    String[] arrayMonths = {"January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"};
    String[] arrayCategories = {"Date", "Month", "Period"};
    private String[] mockChildrenData = {"alex", "cham", "codrin", "sari", "josef", "amit",
            "mike", "tobi"};
    private PhotoGrid photoGrid;

    public SearchField(PhotoGrid photoGrid) {
        this.photoGrid = photoGrid;
        listOfSearch = new HashSet<>();
        setUpUI();
        getStyleClass().add("my-list-cell");

    }

    public void setUpUI() {
        setUpTextFieldAndSearch();
        setUpDatePickers();
        setUpLocationButtons();
        setUpResetButton();

        setSpacing(10);
        getChildren().add(categoryAndItem);
        getChildren().add(buttonPane);
        getChildren().add(queryFieldAndSearch);
        getChildren().add(resetButton);
    }

    public void setUpTextFieldAndSearch() {
        queryFieldAndSearch = new HBox();
        searchQueryButtons = new HBox();

        comboBox = new ComboBox();
        comboBox.setMaxWidth(200);
        for (String childName : mockChildrenData) {
            comboBox.getItems().add(childName);
        }
        AutoCompleteComboBoxListener autoComplete = new AutoCompleteComboBoxListener(comboBox);
        comboBox.setOnKeyReleased(autoComplete);
        queryFieldAndSearch.setHgrow(searchQueryButtons, Priority.ALWAYS);
        searchQueryButtons.setMaxWidth(Double.MAX_VALUE);
        addButton = new Button("+");
        searchButton = new Button("Search");
        EventHandler eventHandler = event -> {
            addChildrenQuery((String) comboBox.getValue());
            updateChildrenQueries();
        };


        addButton.setOnAction(eventHandler);
        comboBox.getEditor().addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent E) ->{
            if(E.getCode() == KeyCode.ENTER){
                System.out.println("It worked!");
                addChildrenQuery((String) comboBox.getValue());
                updateChildrenQueries();
            }
        });
	    searchButton.setOnAction (event -> updateSearchDatabase());

        queryFieldAndSearch.getChildren().add(searchQueryButtons);
        queryFieldAndSearch.getChildren().add(comboBox);
        queryFieldAndSearch.getChildren().add(addButton);
        queryFieldAndSearch.getChildren().add(searchButton);
    }

    public void setUpDatePickers() {
        datePicker = new DatePicker();
        categoryAndItem = new HBox();
        categoryAndItem.setSpacing(10);
        stackCategories = new StackPane();

        dateCategories = new ComboBox();
        dateCategories.getItems().addAll(arrayCategories);
        dateCategories.setValue(arrayCategories[0]);
        updateCategories();
        dateCategories.setOnAction(event -> updateCategories());
        dateCategories.setStyle("-fx-text-fill: #ffffff");

        monthAndYear = new HBox();
        months = new ComboBox();
        months.getItems().addAll(arrayMonths);
        years = new ComboBox();

        periodPane = new HBox();
        periodPane.setSpacing(5);
        periodPane.setAlignment(Pos.CENTER);
        initialDatePicker = new DatePicker();
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

                                for (int i = 0; i < Photo.getUniqueDates().size(); i++) {
                                    if (formatDate(item).equals(new SimpleDateFormat
                                            ("dd/MM/yyyy").format(Photo.getUniqueDates().get(i)))) {
                                        setStyle("-fx-background-color: #00c0cb;");
                                    }
                                }

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

                                for (int i = 0; i < Photo.getUniqueDates().size(); i++) {
                                    if (formatDate(item).equals(new SimpleDateFormat
                                            ("dd/MM/yyyy").format(Photo.getUniqueDates().get(i)))) {
                                        setStyle("-fx-background-color: #00c0cb;");
                                    }
                                }
                                if (initialDatePicker.getValue() == null) {
                                    setDisable(true);
                                } else if (item.isBefore(
                                        initialDatePicker.getValue().plusDays(1))
                                        ) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }
                            }
                        };
                    }
                };
        datePicker.setDayCellFactory(initialDateDayCellFactory);//this is not part of the period
        // pane, I just added her so the datecellfactory has been initiated
        datePicker.setShowWeekNumbers(false);
        datePicker.setMaxWidth(200);
        initialDatePicker.setDayCellFactory(initialDateDayCellFactory);
        initialDatePicker.setMaxWidth(125);
        initialDatePicker.setShowWeekNumbers(false);
        initialDatePicker.setPromptText("dd/mm/yyyy");
        endDatePicker.setDayCellFactory(endDateDayCellFactory);
        endDatePicker.setMaxWidth(125);
        endDatePicker.setShowWeekNumbers(false);
        endDatePicker.setPromptText("dd/mm/yyyy");

        Font font = new Font("Arial", 20);
        Label fromLabel = new Label("From: k");
        fromLabel.setStyle("-fx-text-fill: #ffffff");
        fromLabel.setFont(font);
        Label toLabel = new Label("To: ");
        toLabel.setStyle("-fx-text-fill: #ffffff");
        toLabel.setFont(font);
        periodPane.getChildren().add(fromLabel);
        periodPane.getChildren().add(initialDatePicker);
        periodPane.getChildren().add(toLabel);
        periodPane.getChildren().add(endDatePicker);

        categoryAndItem.getChildren().add(dateCategories);
        categoryAndItem.getChildren().add(stackCategories);
    }

    public void updateCategories() {
        if (dateCategories.getValue().equals("Date")) {
            stackCategories.getChildren().clear();
            stackCategories.getChildren().add(datePicker);
        } else if (dateCategories.getValue().equals("Month")) {
            stackCategories.getChildren().clear();
            Set<String> uniqueYears = new HashSet();
            for (int i = 0; i < Photo.getUniqueDates().size(); i++) {
                uniqueYears.add(new SimpleDateFormat("yyyy").format(Photo.getUniqueDates().get(i)));
            }
            Object[] arrayYears = uniqueYears.toArray();
            years.getItems().addAll(arrayYears);
            monthAndYear.getChildren().addAll(months, years);
            stackCategories.getChildren().add(monthAndYear);
        } else if (dateCategories.getValue().equals("Period")) {
            stackCategories.getChildren().clear();
            stackCategories.getChildren().add(periodPane);
        }
    }

    public TextField getDatePickerTextField() {
        return datePicker.getEditor();
    }

    public void setUpLocationButtons() {
        buttonPane = new HBox();
        ToggleGroup toggleGroup = new ToggleGroup();
        locationA = new ToggleButton("RoomA");
        locationA.setMinWidth(50);
        locationB = new ToggleButton("RoomB");
        locationB.setMinWidth(50);
        locationA.setToggleGroup(toggleGroup);
        locationB.setToggleGroup(toggleGroup);

        buttonPane.getChildren().add(locationA);
        buttonPane.getChildren().add(locationB);
    }

    public void addChildrenQuery(String addedQuery) {
        Set<String> hashSet = new HashSet<String>(Arrays.asList(mockChildrenData)) {
            public boolean contains(Object o) {
                String paramStr = (String) o;
                for (String s : this) {
                    if (paramStr.equalsIgnoreCase(s)) return true;
                }
                return false;
            }
        };
        if (addedQuery != null && !listOfSearch.contains(addedQuery) && hashSet.contains
                (addedQuery)) {
            String toAdd = null; // The String to add to the list of search
            Iterator iterator = hashSet.iterator();
            while (iterator.hasNext()) {
                String s = (String) iterator.next();
                if (s.equalsIgnoreCase(addedQuery)) {
                    toAdd = s;
                    break;
                }
            }
            listOfSearch.add(toAdd);
            comboBox.getEditor().setText("");
        }
    }

    public void updateChildrenQueries() {
        searchQueryButtons.getChildren().clear();
        for (String query : listOfSearch) {
            Button queryButton = new Button(query + " - ");
            queryButton.setOnAction(event -> {
                listOfSearch.remove(query);
                updateChildrenQueries();
            });
            queryButton.setMinHeight(minHeight);
            queryButton.setStyle("-fx-text-fill: #ffffff");
            searchQueryButtons.getChildren().add(queryButton);
        }
    }

    public ComboBox getComboBox() {
        return comboBox;
    }

    public void setAllMinHeight(int height) {
        this.minHeight = height;
        datePicker.setMinHeight(height - 5);
        categoryAndItem.setMinHeight(height);
        initialDatePicker.setMinHeight(height);
        endDatePicker.setMinHeight(height);
        dateCategories.setMinHeight(height);
        months.setMinHeight(height);
        years.setMinHeight(height);
        buttonPane.setMinHeight(height);
        searchButton.setMinHeight(height);
        comboBox.setMinHeight(height - 5);
        locationA.setMinHeight(height);
        locationB.setMinHeight(height);
        addButton.setMinHeight(height);
        resetButton.setMinHeight(height);

    }

    public void setUpResetButton() {
        resetButton = new Button("Reset");
        resetButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                datePicker.setValue(null);
                initialDatePicker.setValue(null);
                endDatePicker.setValue(null);
                months.setValue("");
                searchQueryButtons.getChildren().clear();
                listOfSearch.clear();
                locationA.setSelected(false);
                locationB.setSelected(false);
                comboBox.getEditor().setText("");
            }
        });
    }


    public void updateSearchDatabase() {
        Set<String> listOfSearchedField = listOfSearch;
        LocalDate date = datePicker.getValue();
        LocalDate initialDate = initialDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        LocalDate finalInitialDate = null;
        LocalDate finalEndDate = null;

        String selected = (String) dateCategories.getSelectionModel().getSelectedItem();

        System.out.println();

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
                System.out.println(months.getSelectionModel().getSelectedItem());
                break;
            }
        }
        Tag roomTag = null;
        if (locationA.isSelected() == true) {
            roomTag = new Tag(Tag.TagType.PLACE, locationA.getText());
        }
        if (locationB.isSelected() == true) {
            roomTag = new Tag(Tag.TagType.PLACE, locationB.getText());
        }

        System.out.println(listOfSearchedField);
        Tag kidTag = null;
        if (listOfSearchedField.size() > 0) {
            String kid1 = listOfSearchedField.iterator().next();
            kidTag = new Tag(Tag.TagType.KID, kid1);
        }

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

    //formats the given date in the form of dd/mm/yyyy
    private String formatDate(LocalDate item) {
        StringBuilder stringBuilder = new StringBuilder();
        String dayOfMonth = String.valueOf(item.getDayOfMonth());
        if (dayOfMonth.length() == 1) {
            stringBuilder.append("0" + dayOfMonth + "/");
        } else {
            stringBuilder.append(dayOfMonth + "/");
        }
        String monthValue = String.valueOf(item.getMonthValue());
        if (monthValue.length() == 1) {
            stringBuilder.append("0" + monthValue + "/");
        } else {
            stringBuilder.append(monthValue + "/");
        }
        stringBuilder.append(item.getYear());
        return stringBuilder.toString();
    }
}
