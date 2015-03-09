package synergy.newViews;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 *
 */
public class TagArea extends StackPane {
    GridPane layoutGridPane;
    HBox locationPane, childrenPane; //This is an HBox pane with a textfield and an "add" button
    FlowPane locationTags, childrenTags; //These are where the tags are displayed
    ToggleButton locationA, locationB;
    FlowPane childrenSuggestion;
    Button childrenSuggestionLabel;

    final String[] array = {"Cham", "Mike", "Tobi", "Alex", "Sari", "Codrin", "Josef", "Amit"};


    public TagArea() {
        setUpUI();
        getChildren().add(layoutGridPane);
        getStyleClass().setAll("toggle-button");
    }

    public void setUpUI() {
        layoutGridPane = new GridPane();
        layoutGridPane.setHgap(30);
        layoutGridPane.setVgap(30);
        layoutGridPane.setPadding(new Insets(0, 0, 20, 0));

        setUpLocationPanel();
        setUpChildrenPanel();
        setGridPaneLayout();
    }

    public void setGridPaneLayout() {
        //Locations
        Label labelLocation = new Label("Location");
        labelLocation.setFont(new Font("Arial", 30));
        labelLocation.setTextFill(Color.DARKGRAY);
        layoutGridPane.add(labelLocation, 0, 0);
        layoutGridPane.add(locationPane, 0, 1);
        layoutGridPane.add(locationTags, 0, 2);

        //Children
        Label labelChildren = new Label("Children");
        labelChildren.setFont(new Font("Arial", 30));
        labelChildren.setTextFill(Color.DARKGRAY);
        layoutGridPane.add(labelChildren, 0, 3);
        layoutGridPane.add(childrenPane, 0, 4);
        layoutGridPane.add(childrenTags, 0, 5);
        layoutGridPane.add(childrenSuggestion, 0 , 6);
    }

    public void setUpLocationPanel() {
        locationPane = new HBox();
        locationPane.setSpacing(10);

        locationTags = new FlowPane();

        Button locationA = new Button("Location A");
        locationB = new ToggleButton("Location B");
        locationA.getStyleClass().add("firstButton");

        addLocationEventHandler(locationA);
        //addLocationEventHandler(locationB);

        locationPane.getChildren().add(locationA);
        locationPane.getChildren().add(locationB);

    }

    public void setUpChildrenPanel() {
        childrenTags = new FlowPane(10, 10);


        childrenPane = new HBox();
        childrenPane.setSpacing(10);

        final TextField childrenTextField = new TextField();
        Button addChildrenTagButton = new Button("+");

        EventHandler childrenEventHandler = new EventHandler() {
            public void handle(Event event) {
                HBox hBox = new HBox();
                Label label = new Label(childrenTextField.getText());
                label.setFont(new Font("Arial", 20));
                label.setTextFill(Color.GRAY);
                hBox.getChildren().add(label);
                hBox.getChildren().add(new Button("-"));
                childrenTextField.setText("");
                childrenTags.getChildren().add(hBox);
                setSuggestions();

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

        childrenPane.getChildren().add(childrenTextField);
        childrenPane.getChildren().add(addChildrenTagButton);

        childrenSuggestion = new FlowPane(10, 10);
        childrenSuggestionLabel = new Button("Suggestions: ");
        childrenSuggestionLabel.setFont(new Font("Arial", 15));
        childrenSuggestionLabel.setTextFill(Color.DARKGRAY);
        childrenSuggestion.getChildren().add(childrenSuggestionLabel);
        setSuggestions();

    }

    public void setSuggestions(){
        childrenSuggestion.getChildren().retainAll(childrenSuggestionLabel);
        String[] suggestions = getSuggestions();
        for(int i = 0; i < suggestions.length; i++){
            HBox hBox = new HBox();
            Label suggestion = new Label(suggestions[i]+ " ");
            suggestion.setFont(new Font("Arial", 20));
            suggestion.setTextFill(Color.GRAY);
            Button addButton = new Button("+");
            addButton.setOnAction(new EventHandler() {
                public void handle(Event event) {
                    HBox hBox = new HBox();
                    Label label = new Label(suggestion.getText());
                    label.setFont(new Font("Arial", 20));
                    label.setTextFill(Color.GRAY);
                    hBox.getChildren().add(label);
                    hBox.getChildren().add(new Button("-"));
                    childrenTags.getChildren().add(hBox);
                    setSuggestions();

                    /*final Integer[] selectedIndexes = photosPanel.getSelectedIndexesAsArray ();
                    Tag tag = new Tag(Tag.TagType.KID, (String) childrenComboBox.getSelectedItem());
                    for ( int i = 0 ; i < selectedIndexes.length; ++ i ) {
                        photosPanel.getPhotos ().get (selectedIndexes[ i ]).addTag (tag);
                    }
                    updateChildrenTags();*/
                }
            });
            hBox.getChildren().add(suggestion);
            hBox.getChildren().add(addButton);
            childrenSuggestion.getChildren().add(hBox);
        }
    }

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

    //testing with sample suggestions
    public String[] getSuggestions(){
        Random random = new Random();
        String[] suggestions = new String[random.nextInt(8)];
        ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(array));
        Collections.shuffle(arrayList);
        for(int i = 0; i < suggestions.length; i++){
            suggestions[i] = arrayList.get(i);
        }
        return suggestions;
    }
}
