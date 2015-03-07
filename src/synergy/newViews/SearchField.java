package synergy.newViews;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

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
        categoryComboBox.setValue(categories[0]);

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
                    Label label = new Label(" "+locationQuery);
                    label.setFont(new Font("Arial", 30));
                    label.setTextFill(Color.GRAY);

                    queryLocation.getChildren().add(label);
                    queryLocation.getChildren().add(new Button("-"));
                } else if(categoryComboBox.getValue().equals("Children")){
                    String childQuery = textField.getText();
                    Label label = new Label(" "+childQuery);
                    label.setFont(new Font("Arial", 30));
                    label.setTextFill(Color.GRAY);

                    queryChild.getChildren().add(label);
                    queryChild.getChildren().add(new Button("-"));
                }
            }
        };

        Button searchButton = new Button("Search");
        searchButton.setOnAction(eventHandler);

        categoryComboBox.setMinHeight(45);
        searchButton.setMinHeight(45);

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
        queriesPane.setHgap(40);

        queryLocation = new HBox();
        Label locationLabel = new Label("Location: ");
        locationLabel.setFont(new Font("Arial", 30));
        locationLabel.setTextFill(Color.DARKGRAY);
        queryLocation.getChildren().add(locationLabel);

        queryChild = new HBox();
        Label childLabel = new Label("Children: ");
        childLabel.setFont(new Font("Arial", 30));
        childLabel.setTextFill(Color.DARKGRAY);
        queryChild.getChildren().add(childLabel);

        queryDate = new HBox();
        Label dateLabel = new Label("Date: ");
        dateLabel.setFont(new Font("Arial", 30));
        dateLabel.setTextFill(Color.DARKGRAY);
        queryDate.getChildren().add(dateLabel);

        queriesPane.getChildren().addAll(queryLocation, queryChild, queryDate);
    }

    public TextField getTextField(){
        return textField;
    }
}
