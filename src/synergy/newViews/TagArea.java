package synergy.newViews;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class TagArea extends StackPane {
    GridPane layoutGridPane;
    HBox locationPane, childrenPane;
    FlowPane locationTags, childrenTags;
    ToggleButton locationA, locationB;


    public TagArea() {
        setUpUI();
        getChildren().add(layoutGridPane);
    }

    public void setUpUI() {
        layoutGridPane = new GridPane();
        layoutGridPane.setHgap(10);
        layoutGridPane.setVgap(10);
        layoutGridPane.setPadding(new Insets(0, 0, 20, 0));

        setUpLocationPanel();
        setUpChildrenPanel();
        setGridPaneLayout();
    }

    public void setGridPaneLayout() {
        //Locations
        layoutGridPane.add(new Label("Location"), 0, 0);
        layoutGridPane.add(locationPane, 0, 1);
        layoutGridPane.add(locationTags, 0, 2);

        //Children
        layoutGridPane.add(new Label("Children"), 0, 3);
        layoutGridPane.add(childrenPane, 0, 4);
        layoutGridPane.add(childrenTags, 0, 5);
    }

    public void setUpLocationPanel() {
        locationPane = new HBox();
        locationPane.setSpacing(10);

        locationTags = new FlowPane();

        locationA = new ToggleButton("Location A");
        locationB = new ToggleButton("Location B");

        addLocationEventHandler(locationA);
        addLocationEventHandler(locationB);

        locationPane.getChildren().add(locationA);
        locationPane.getChildren().add(locationB);

    }

    public void setUpChildrenPanel() {
        childrenPane = new HBox();
        childrenPane.setSpacing(10);

        childrenTags = new FlowPane();
        final TextField childrenTextField = new TextField();
        Button addChildrenTagButton = new Button("+");

        EventHandler childrenEventHandler = new EventHandler() {
            public void handle(Event event) {
                HBox hBox = new HBox();
                hBox.getChildren().add(new Label(childrenTextField.getText()));
                hBox.getChildren().add(new Button("-"));
                childrenTags.getChildren().add(hBox);
                childrenTextField.setText("");

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
    }

    public void addLocationEventHandler(final ToggleButton location) {
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
