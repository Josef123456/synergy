package synergy.newViews;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Cham on 06/03/2015.
 */
public class SearchField extends HBox {

    private DatePicker datePicker;
    private HBox queryFieldAndSearch, searchQueryButtons, buttonPane;
    private ComboBox comboBox;
    private ToggleButton searchButton,locationA, locationB;
    private Set<String> listOfSearch;
    private int minHeight;

    private String[] mockChildrenData = {"John", "John Jones", "John James", "John George", "Billy", "Jacob", "Ronald", "Alicia", "Jonah", "Freddie", "Daniel", "David", "Harry", "Harrison", "Isaac", "Toby", "Tom", "Jill"};

    public SearchField(){
        listOfSearch = new HashSet<String>();
        setUpUI();
        getStyleClass().setAll("button-bar");
        getStyleClass().add("gridSearch");
    }

    public void setUpUI(){
        setUpTextFieldAndSearch();
        setUpDatePicker();
        setUpLocationButtons();

        getChildren().add(datePicker);
        getChildren().add(buttonPane);
        getChildren().add(queryFieldAndSearch);
    }

    public void setUpTextFieldAndSearch(){
        queryFieldAndSearch = new HBox();

        searchQueryButtons = new HBox();
        comboBox = new ComboBox();
        for(String childName: mockChildrenData){
            comboBox.getItems().add(childName);
        }
        AutoCompleteComboBoxListener autoComplete = new AutoCompleteComboBoxListener(comboBox);
        comboBox.setOnKeyReleased(autoComplete);
        comboBox.setMaxWidth(Double.MAX_VALUE);
        queryFieldAndSearch.setHgrow(searchQueryButtons, Priority.ALWAYS);
        searchButton = new ToggleButton("Search");
       searchButton.getStyleClass().add("toggle-button");

        EventHandler eventHandler = new EventHandler(){
            public void handle(Event event) {
                System.out.println(event.getEventType());
                listOfSearch.add((String) comboBox.getValue());
                Button queryButton = new Button(comboBox.getValue() + " - ");
                queryButton.setMinHeight(minHeight);
                queryButton.setStyle("-fx-text-fill: antiquewhite");
                searchQueryButtons.getChildren().add(queryButton);
            }
        };
        //comboBox.itemsProperty().addListener();

        //searchButton.setOnAction(eventHandler);
        //comboBox.setOnAction(eventHandler);

        queryFieldAndSearch.getChildren().add(searchQueryButtons);
        queryFieldAndSearch.getChildren().add(comboBox);

        queryFieldAndSearch.getChildren().add(searchButton);
    }

    public void setUpDatePicker(){
        datePicker = new DatePicker();

    }

    public void setUpLocationButtons(){
        buttonPane = new HBox();
        locationA = new ToggleButton("Location A");
        locationB = new ToggleButton("Location B");

        buttonPane.getChildren().add(locationA);
        buttonPane.getChildren().add(locationB);
    }

    public ComboBox getComboBox(){
        return comboBox;
    }

    public void setAllMinHeight(int height){
        this.minHeight = height;
        datePicker.setMinHeight(height);
        buttonPane.setMinHeight(height);
        searchButton.setMinHeight(height);
        comboBox.setMinHeight(height);
        locationA.setMinHeight(height);
        locationB.setMinHeight(height);
    }


}
