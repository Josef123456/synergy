package synergy.newViews;

import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;

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

    public SearchField(){
        setUpUI();
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
