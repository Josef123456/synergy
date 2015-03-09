package synergy.newViews;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Cham on 06/03/2015.
 */
public class SearchField extends HBox {
    DatePicker datePicker;
    HBox fieldAndSearch;

    TextField textField;
    Button searchButton;

    HBox buttonPane;
    ToggleButton locationA, locationB;

    Set<String> listOfSearch;

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
        getChildren().add(fieldAndSearch);
    }

    public void setUpTextFieldAndSearch(){
        fieldAndSearch = new HBox();
        textField = new TextField();
        searchButton = new Button("Search");
        searchButton.setStyle("-fx-text-fill: antiquewhite");

        EventHandler eventHandler = new EventHandler(){
            public void handle(Event event) {
                listOfSearch.add(textField.getText());
                textField.requestFocus(); // get focus first
                textField.getCaretPosition();
                textField.selectNextWord();

            }
        };

        EventHandler<MouseEvent> mouseEvent = new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
                if(textField.getCaretPosition() == textField.getLength()){

                } else {
                    textField.previousWord();
                    textField.selectNextWord();
                }

            }
        };

        EventHandler<KeyEvent> keyEventHandler = new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                int position = textField.getCaretPosition();
                System.out.println(position + textField.getText(position - 1, position));
                if(event.getCode().equals(KeyCode.SPACE)){

                }
                if(!event.getCode().equals(KeyCode.BACK_SPACE)){

                } else if(event.getCode().equals(KeyCode.BACK_SPACE) && !textField.getText(position, position).equals(" ")){
                    textField.selectPreviousWord();
                }
            }
        };

        searchButton.setOnAction(eventHandler);
        textField.setOnMouseReleased(mouseEvent);
        textField.setOnKeyReleased(keyEventHandler);
        fieldAndSearch.getChildren().add(textField);

        fieldAndSearch.getChildren().add(searchButton);
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

    public TextField getTextField(){
        return textField;
    }

    public void setAllMinHeight(int height){
        datePicker.setMinHeight(height);
        buttonPane.setMinHeight(height);
        searchButton.setMinHeight(height);
        textField.setMinHeight(height);
        locationA.setMinHeight(height);
        locationB.setMinHeight(height);
    }




}
