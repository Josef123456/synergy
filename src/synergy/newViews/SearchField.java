package synergy.newViews;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Created by Cham on 06/03/2015.
 */
public class SearchField extends GridPane {

    StackPane stackFieldPane;
    TextField textField;
    HBox datePane;
    DatePicker datePickerFrom, datePickerTo;
    HBox searchAndCategoryPane;
    ComboBox categoryComboBox;
    FlowPane queriesPane;
    HBox queryLocation, queryChild, queryDate;

    final String[] categories = {"Children", "Location", "Date"};

    public SearchField(){
        setUpUI();
    }

    public void setUpUI(){
        setUpSearchCategoryAndButton();
        setUpDatePicker();
        setUpQueryTags();

        stackFieldPane = new StackPane();
        stackFieldPane.getChildren().add(textField);
        stackFieldPane.getChildren().add(datePane);
        datePane.setVisible(false);

        ColumnConstraints column1 = new ColumnConstraints(50);
        column1.setPercentWidth(new Double(75));
        ColumnConstraints column2 = new ColumnConstraints(50);
        column2.setPercentWidth(new Double(25));
        getColumnConstraints().addAll(column1, column2);

        add(stackFieldPane, 0, 0);
        add(searchAndCategoryPane, 1, 0);
        add(queriesPane, 0, 1);
    }

    public void setUpSearchCategoryAndButton(){
        textField = new TextField();

        searchAndCategoryPane = new HBox();
        categoryComboBox = new ComboBox();
        categoryComboBox.getItems().addAll(categories);

        categoryComboBox.setOnAction(new EventHandler(){
            public void handle(Event event) {
                if(categoryComboBox.getValue().equals("Date")){
                    textField.setVisible(false);
                    datePane.setVisible(true);
                } else{
                    textField.setVisible(true);
                    datePane.setVisible(false);
                }
            }
        });

        EventHandler eventHandler = new EventHandler(){
            public void handle(Event event) {
                if(categoryComboBox.getValue().equals("Location")){
                    String locationQuery = textField.getText();
                    queryLocation.getChildren().add(new Label(" "+locationQuery));
                } else if(categoryComboBox.getValue().equals("Children")){
                    String childQuery = textField.getText();
                    queryChild.getChildren().add(new Label(" "+childQuery));
                }
            }
        };

        Button searchButton = new Button("Search");
        searchButton.setOnAction(eventHandler);
        searchAndCategoryPane.getChildren().add(categoryComboBox);
        searchAndCategoryPane.getChildren().add(searchButton);
    }

    public void setUpDatePicker(){
        datePane = new HBox();
        datePane.setSpacing(new Double(10));

        Label labelFrom = new Label("From: ");
        datePickerFrom = new DatePicker();
        datePane.getChildren().add(labelFrom);
        datePane.getChildren().add(datePickerFrom);

        Label labelTo = new Label("To: ");
        datePickerTo = new DatePicker();
        datePane.getChildren().add(labelTo);
        datePane.getChildren().add(datePickerTo);
    }

    public void setUpQueryTags(){
        queriesPane = new FlowPane();

        queryLocation = new HBox();
        Label locationLabel = new Label("Location: ");
        queryLocation.getChildren().add(locationLabel);

        queryChild = new HBox();
        Label childLabel = new Label("Children: ");
        queryChild.getChildren().add(childLabel);

        queryDate = new HBox();
        Label dateLabel = new Label("Date: ");
        queryDate.getChildren().add(dateLabel);

        queriesPane.getChildren().addAll(queryLocation, queryChild, queryDate);
    }
}
