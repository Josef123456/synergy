package synergy.views;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Josef on 07/03/2015.
 */

public class TaggingArea extends BorderPane {

    private GridPane gridNorthern;
    private VBox vBoxChildren, vBoxSuggestion, paneDate;
    private FlowPane childrenTags, childrenSuggestions;
    private HBox childrenPane, boxLocation, boxMainLocation;
    private ToggleButton button1, button2, addChildrenTagButton;
    private Text locationText, childrenText;
    private Label childrenSuggestionLabel;
    private TextField childrenTextField;
    final String[] array = {"Cham", "Mike", "Tobi", "Alex", "Sari", "Codrin", "Josef", "Amit"};

    public TaggingArea() {
        setCenter(returnGridPane(locationPane(), childrenVboxPane(), suggestionPane(), datePane()));
        getStyleClass().setAll("button-bar");
        getStyleClass().addAll("toggle-button");
        getStyleClass().addAll("grid");
    }

    private HBox locationPane() {

        boxMainLocation = new HBox(20);
        boxMainLocation.getStyleClass().add("grid");

        boxLocation = new HBox(5);
        button1 = new ToggleButton("RoomA");
        button2 = new ToggleButton("RoomB");


// If you guys dont like the background shadow thingy then just delete it...

        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });

        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });

        locationText = new Text(" Location:");
        locationText.setId("leftText");
        locationText.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        boxLocation.getChildren().addAll(button1, button2);
        boxMainLocation.getChildren().addAll(locationText, boxLocation);


        return boxMainLocation;
    }
    private VBox childrenVboxPane() {

        vBoxChildren = new VBox(10);
        vBoxChildren.getStyleClass().add("grid");

        gridNorthern = new GridPane();
        childrenTags = new FlowPane(10, 10);
        childrenTags.setPadding(new Insets(10, 10, 10, 10));
        childrenTags.setPrefWrapLength(4.0);
        childrenPane = new HBox(10);
//        vBoxChildren.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
//        vBoxChildren.getStyleClass().add("grid");

        childrenTextField = new TextField();
        childrenTextField.setId("searching");
        addChildrenTagButton = new ToggleButton("+");
        addChildrenTagButton.setStyle("-fx-text-fill: antiquewhite");
        addChildrenTagButton.setStyle("-fx-background-color: #595959");
        addChildrenTagButton.setStyle("-fx-text-fill: antiquewhite");

        childrenText = new Text(" Children: ");
        childrenText.setId("leftText");
        childrenText.setFont(Font.font("Arial", FontWeight.BOLD, 16));


        EventHandler childrenEventHandler = new EventHandler() {
            public void handle(Event event) {
                HBox hBox = new HBox();
                String name = childrenTextField.getText();
                Button buttonNames = new Button(name + " -");
                buttonNames.setMinWidth(95.0);
                buttonNames.setStyle("-fx-text-fill: antiquewhite");
                hBox.getChildren().addAll(buttonNames);
                childrenTextField.setText("");
                childrenTags.getChildren().add(hBox);

                /*final Integer[] selectedIndexes = photosPanel.getSelectedIndexesAsArray ();
                Tag tag = new Tag(Tag.TagType.KID, (String) childrenComboBox.getSelectedItem());
                for ( int i = 0 ; i < selectedIndexes.length; ++ i ) {
                    photosPanel.getPhotos ().get (selectedIndexes[ i ]).addTag (tag);
                }
                updateChildrenTags();*/

            }
        };
        childrenTextField.setOnAction(childrenEventHandler);
        addChildrenTagButton.setOnAction(childrenEventHandler);

        childrenPane.getChildren().addAll(childrenTextField, addChildrenTagButton);

        gridNorthern.add(childrenText, 0, 0);
        gridNorthern.add(childrenPane, 1, 0);

        vBoxChildren.getChildren().addAll(gridNorthern, childrenTags);
        return vBoxChildren;
    }

    private VBox suggestionPane() {

        vBoxSuggestion = new VBox();
        vBoxSuggestion.getStyleClass().add("grid");

        childrenSuggestionLabel = new Label(" Suggestions: ");
        childrenSuggestionLabel.setStyle("-fx-text-fill: antiquewhite");
        childrenSuggestionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        childrenSuggestions = new FlowPane(10, 10);
        childrenSuggestions.setPadding(new Insets(10, 10, 10, 10));
        childrenSuggestions.setPrefWrapLength(4.0);
        vBoxSuggestion.getStyleClass().add("grid");

        String[] suggestions = getSuggestions();
        for (int i = 0; i < suggestions.length; i++) {
            HBox hBox = new HBox();
            String suggestion = (suggestions[i] + " ");
            Button buttonName = new Button(suggestion + " +");
            buttonName.setStyle("-fx-text-fill: antiquewhite");
            buttonName.setOnAction(new EventHandler() {
                public void handle(Event event) {
                    childrenTextField.setText("");
                    HBox hBox = new HBox();
                    Button buttonNames = new Button(suggestion + " -");
                    buttonNames.setMinWidth(95.0);
                    buttonNames.setStyle("-fx-text-fill: antiquewhite");
                    hBox.getChildren().addAll(buttonNames);
                    childrenTags.getChildren().add(hBox);

                    /*final Integer[] selectedIndexes = photosPanel.getSelectedIndexesAsArray ();
                    Tag tag = new Tag(Tag.TagType.KID, (String) childrenComboBox.getSelectedItem());
                    for ( int i = 0 ; i < selectedIndexes.length; ++ i ) {
                        photosPanel.getPhotos ().get (selectedIndexes[ i ]).addTag (tag);
                    }
                    updateChildrenTags();*/
                }
            });
            hBox.getChildren().addAll(buttonName);
            childrenSuggestions.getChildren().add(hBox);
        }
        vBoxSuggestion.getChildren().addAll(childrenSuggestionLabel, childrenSuggestions);

        return vBoxSuggestion;

    }


    private VBox datePane() {

        paneDate = new VBox();
        paneDate.getStyleClass().add("grid");

        childrenSuggestionLabel = new Label(" Date: ");
        childrenSuggestionLabel.setStyle("-fx-text-fill: antiquewhite");
        childrenSuggestionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        paneDate.getChildren().addAll(childrenSuggestionLabel);

        return paneDate;
    }


    private VBox returnGridPane(HBox box1, VBox box2, VBox box3, VBox box4) {

        VBox hb = new VBox(15);
        hb.getStyleClass().add("hbox");
        hb.getChildren().addAll(box1, box2, box3, box4);

        return hb;
    }


    //testing with sample suggestions by cham
    public String[] getSuggestions() {
        Random random = new Random();
        String[] suggestions = new String[random.nextInt(8)];
        ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(array));
        Collections.shuffle(arrayList);
        for (int i = 0; i < suggestions.length; i++) {
            suggestions[i] = arrayList.get(i);
        }
        return suggestions;
    }

    /**
     * Cham, this is your bit. I Just copy pasted the thing you have
     * commented out.
     */
//    public void setSuggestions(){
//        childrenSuggestion.getChildren().retainAll(childrenSuggestionLabel);
//        String[] suggestions = getSuggestions();
//        for(int i = 0; i < suggestions.length; i++){
//            HBox hBox = new HBox();
//            Label suggestion = new Label(suggestions[i]+ " ");
//            suggestion.setFont(new Font("Arial", 20));
//            suggestion.setTextFill(Color.GRAY);
//            Button addButton = new Button("+");
//            addButton.setOnAction(new EventHandler() {
//                public void handle(Event event) {
//                    HBox hBox = new HBox();
//                    Label label = new Label(suggestion.getText());
//                    label.setFont(new Font("Arial", 20));
//                    label.setTextFill(Color.GRAY);
//                    hBox.getChildren().add(label);
//                    hBox.getChildren().add(new Button("-"));
//                    childrenTags.getChildren().add(hBox);
//                    setSuggestions();
//
//                    /*final Integer[] selectedIndexes = photosPanel.getSelectedIndexesAsArray ();
//                    Tag tag = new Tag(Tag.TagType.KID, (String) childrenComboBox.getSelectedItem());
//                    for ( int i = 0 ; i < selectedIndexes.length; ++ i ) {
//                        photosPanel.getPhotos ().get (selectedIndexes[ i ]).addTag (tag);
//                    }
//                    updateChildrenTags();*/
//                }
//            });
//            hBox.getChildren().add(suggestion);
//            hBox.getChildren().add(addButton);
//            childrenSuggestion.getChildren().add(hBox);
//        }
//    }
    public void addLocationEventHandler(final Button location) {
       /* location.setOnAction(new EventHandler() {

            @Override
            public void handle(Event event) {
                final Integer[] selectedIndexes = photosPanel.getSelectedIndexesAsArray();
                System.out.println(Arrays.toString(selectedIndexes) + " location");
                Tag tag = new Tag(Tag.TagType.PLACE, (String) location.getText());
                for (int i = 0; i < selectedIndexes.length; ++i) {
                    photosPanel.getPhotos().get(selectedIndexes[i]).addTag(tag);
                }
                updateLocationTags();
            }
        });*/
    }

    public void updateLocationTags() {
       /* locationTags.getChildren().clear();

        Set<Tag> tagSet = new HashSet<> ();
        final Integer[] selectedIndexes = photosPanel.getSelectedIndexesAsArray ();
        System.out.println ( "Selected Indexes: " + Arrays.toString(selectedIndexes));
        System.out.println( photosPanel.getPhotos ().get (0).getID ());
        for ( int i = 0 ; i < selectedIndexes.length; ++ i ) {
            tagSet.addAll (photosPanel.getPhotos ().get (selectedIndexes[i]).getLocationTags ());
        }
        System.out.println ("List of location tags: " + tagSet ) ;
        Tag[] tagArray = tagSet.toArray (new Tag[tagSet.size()]);

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(0, 10, 0 ,0));
        if ( tagArray.length > 0 )  {
            for (int i = 0; i < tagArray.length; ++i) {
                final String tagValue= tagArray[i].getValue ();
                Label label = new Label();
                label.setText(tagValue);
                hBox.getChildren().add(label);
            }
        }
        locationTags.getChildren().add(hBox);*/
    }

    public void updateChildrenTags() {
        /*locationTags.getChildren().clear();
        Set<Tag> tagSet = new HashSet<>();
        final Integer[] selectedIndexes = photosPanel.getSelectedIndexesAsArray ();
        for ( int i = 0 ; i < selectedIndexes.length; ++ i ) {
            tagSet.addAll (photosPanel.getPhotos ().get (selectedIndexes[i]).getChildTags ());
        }
        System.out.println ("List of children tags: " + tagSet);
        Tag[] tagArray = tagSet.toArray (new Tag[tagSet.size()]);

        if ( tagArray.length > 0 )  {
            for (int i = 0; i < tagArray.length; ++i) {
                final String tagValue= tagArray[i].getValue ();

                HBox hBox = new HBox();
                Label label = new Label();
                label.setText(tagValue);

                Button removeButton = new Button("-");
                removeButton.setOnAction(new EventHandler(){
                    public void handle(Event e){
                        Tag tag = new Tag(Tag.TagType.KID, tagValue);
                        for(int i = 0; i < selectedIndex.length; ++i){
                            photosPanel.getPhotos ().get(selectedIndexes[i]).removeTag (tag);
                        }
                        updateChildrenTags();
                    }
                });
                hBox.getChildren().add(label);
                hBox.getChildren().add(removeButton);
                childrenTags.getChildren().add(hBox);

            }
        }*/
    }
}
