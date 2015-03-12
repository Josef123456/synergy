package synergy.views;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Cham on 06/03/2015.
 */
public class SearchField extends HBox {
    DatePicker datePicker;

    HBox queryFieldAndSearch; //this is where the Button, TextField and Search Button go
    HBox searchQueryButtons;
    ComboBox comboBox;
    Button searchButton;

    HBox buttonPane; // This is where LocationA and LocationB button goes
    ToggleButton locationA, locationB;

    Set<String> listOfSearch;
    int minHeight;

    private String[] mockChildrenData = {"John", "John Jones", "John James", "John George", "Billy", "Jacob", "Ronald", "Alicia", "Jonah", "Freddie", "Daniel", "David", "Harry", "Harrison", "Isaac", "Toby", "Tom", "Jill"};

    public SearchField() {
        listOfSearch = new HashSet<String>();
        setUpUI();
        getStyleClass().setAll("button-bar");
        getStyleClass().add("gridSearch");
    }

    public void setUpUI() {
        setUpTextFieldAndSearch();
        setUpDatePicker();
        setUpLocationButtons();

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
        searchButton = new Button("Search");
        searchButton.setStyle("-fx-text-fill: antiquewhite");

        EventHandler eventHandler = new EventHandler() {
            public void handle(Event event) {
                System.out.println(event.getEventType());
                listOfSearch.add((String) comboBox.getValue());
                Button queryButton = new Button(comboBox.getValue() + " - ");
                queryButton.setMinHeight(minHeight);
                queryButton.setStyle("-fx-text-fill: antiquewhite");
                searchQueryButtons.getChildren().add(queryButton);
            }
        };
        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println(newValue.toString());
                System.out.println(observable.toString());
                updateQueries((String) comboBox.getValue());
                updateSearchDatabase();

            }
        });

        //searchButton.setOnAction(eventHandler);
        //comboBox.setOnAction(eventHandler);

        queryFieldAndSearch.getChildren().add(searchQueryButtons);
        queryFieldAndSearch.getChildren().add(comboBox);

        queryFieldAndSearch.getChildren().add(searchButton);
    }

    public void setUpDatePicker() {
        datePicker = new DatePicker();
        datePicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateSearchDatabase();
            }
        });

    }

    public TextField getDatePickerTextField() {
        return datePicker.getEditor();
    }

    public void setUpLocationButtons() {
        buttonPane = new HBox();
        locationA = new ToggleButton("Location A");
        locationB = new ToggleButton("Location B");
        //locationA.setStyle("-fx-text-fill: antiquewhite");
        //locationB.setStyle("-fx-text-fill: antiquewhite");

        EventHandler eventHandler = new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                updateSearchDatabase();
            }
        };

        locationA.setOnAction(eventHandler);
        locationB.setOnAction(eventHandler);

        buttonPane.getChildren().add(locationA);
        buttonPane.getChildren().add(locationB);
    }

    public void updateQueries(String addedQuery) {
        System.out.println(addedQuery);
        if (listOfSearch.contains(addedQuery)) {

        } else {
            listOfSearch.add((String) comboBox.getValue());
            Button queryButton = new Button(comboBox.getValue() + " - ");
            queryButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    listOfSearch.remove(queryButton.getText());
                    searchQueryButtons.getChildren().remove(queryButton);
                }
            });
            queryButton.setMinHeight(minHeight);
            queryButton.setStyle("-fx-text-fill: antiquewhite");
            searchQueryButtons.getChildren().add(queryButton);

        }
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
    }


    public void updateSearchDatabase() {
        Set listOfSearchedField = listOfSearch;
        boolean isLocationA = locationA.isPressed();
        boolean isLocationB = locationB.isPressed();
        String date = getDatePickerTextField().getText();
        //@TODO: placeholder for alex
    }
}
