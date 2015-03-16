package synergy.views;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import synergy.models.Photo;
import synergy.models.Tag;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Josef on 07/03/2015.
 */

public class TaggingArea extends BorderPane {

	private VBox vBoxSuggestion;
	private FlowPane childrenTags;
	private ComboBox<CharSequence> childrenComboBox;
	private ToggleButton button1;
	private ToggleButton button2;
	private Label dateLabel;
    String[] kidsInDatabase = { "alex", "cham", "codrin", "sari", "josef", "amit", "mike", "tobi"};
	//Tag.getAllChildrenTags().stream ().map (Tag::getValue).collect(Collectors.toList ());

    public void update(){
        updateChildrenTags();
        updateLocationTags ();
	    updateSuggestions ();
	    updateDate();
        if(PhotoGrid.getSelectedImages().size() == 0){
            this.setVisible(false);
        } else{
            this.setVisible(true);
        }
    }

    public TaggingArea() {
        setCenter (returnGridPane (createLocationPane (), createDatePane (), createChildrenPane (), createSuggestionPane ()));
    }

	private HBox createLocationPane () {
	    HBox boxMainLocation = new HBox (20);
        boxMainLocation.getStyleClass ().add("grid");

	    HBox boxLocation = new HBox (5);
        button1 = new ToggleButton("RoomA");
	    button2 = new ToggleButton ("RoomB");
        ToggleGroup toggleGroup = new ToggleGroup();
        button1.setToggleGroup(toggleGroup);
        button2.setToggleGroup (toggleGroup);

        button1.setOnAction(event -> {
            final ArrayList<Photo> selectedPhotos = PhotoGrid.getSelectedPhotos();
            Tag tag = new Tag(Tag.TagType.PLACE, button1.getText());
            for (int i = 0; i < selectedPhotos.size(); ++i) {
                selectedPhotos.get(i).addTag(tag);
            }
        });

        button2.setOnAction (event -> {
	        final ArrayList<Photo> selectedPhotos = PhotoGrid.getSelectedPhotos ();
	        Tag tag = new Tag (Tag.TagType.PLACE, button2.getText ());
	        for ( int i = 0 ; i < selectedPhotos.size () ; ++i ) {
		        selectedPhotos.get (i).addTag (tag);
	        }
        });

	    Text locationText = new Text (" Location:");
        locationText.setId ("leftText");
        locationText.setFont (Font.font ("Arial", FontWeight.BOLD, 16));

        boxLocation.getChildren ().addAll(button1, button2);
        boxMainLocation.getChildren ().addAll(locationText, boxLocation);

        return boxMainLocation;
    }

    private VBox createChildrenPane () {
	    VBox vBoxChildren = new VBox (10);
        vBoxChildren.getStyleClass ().add("grid");

	    GridPane gridNorthern = new GridPane ();
        childrenTags = new FlowPane(10, 10);
        childrenTags.setPadding(new Insets(10, 10, 10, 10));
        childrenTags.setPrefWrapLength(4.0);
	    HBox childrenPane = new HBox (10);
        childrenComboBox = new ComboBox<> ();
        childrenComboBox.setId("searching");
        for (String childrenNames : kidsInDatabase ) {
            childrenComboBox.getItems ().add(childrenNames);
        }
        AutoCompleteComboBoxListener autoComplete = new AutoCompleteComboBoxListener(childrenComboBox);
        childrenComboBox.setOnKeyReleased (autoComplete);
	    ToggleButton addChildrenTagButton = new ToggleButton ("+");
        addChildrenTagButton.setStyle ("-fx-text-fill: antiquewhite");
        addChildrenTagButton.setStyle ("-fx-background-color: #595959");
        addChildrenTagButton.setStyle ("-fx-text-fill: antiquewhite");

	    Text childrenText = new Text (" Children: ");
        childrenText.setId ("leftText");
        childrenText.setFont (Font.font ("Arial", FontWeight.BOLD, 16));

        EventHandler childrenEventHandler = event -> {
            //TODO: when the user press enter, add the tag
            String name = childrenComboBox.getEditor().getText();
	        //TODO: Make sure the name is in the predefined ones.
            addChildrenTag(name);
        };
        childrenComboBox.getEditor().setText ("");

        addChildrenTagButton.setOnAction (childrenEventHandler);
        childrenPane.getChildren().addAll(childrenComboBox, addChildrenTagButton);

        gridNorthern.add (childrenText, 0, 0);
        gridNorthern.add (childrenPane, 1, 0);

        vBoxChildren.getChildren ().addAll(gridNorthern, childrenTags);
        return vBoxChildren;
    }

    private VBox createSuggestionPane () {
        vBoxSuggestion = new VBox();
        vBoxSuggestion.getStyleClass().add("grid");

        return vBoxSuggestion;
    }

    private void addChildrenTag(String name){
        Set<String> hashSet = new HashSet<>(Arrays.asList(kidsInDatabase));
        if(hashSet.contains(name)){
            childrenComboBox.getEditor().setText("");
            final ArrayList<Photo> selectedPhotos = PhotoGrid.getSelectedPhotos();
            Tag tag = new Tag(Tag.TagType.KID, name);
            for (int i = 0; i < selectedPhotos.size(); ++i) {
                selectedPhotos.get(i).addTag(tag);
            }
            updateChildrenTags ();
	        updateSuggestions ();
        }
    }

	private VBox createDatePane () {
	    VBox paneDate = new VBox ();
        paneDate.getStyleClass ().add("grid");
	    dateLabel = new Label (buildDateString ());

	    dateLabel.setStyle ("-fx-text-fill: antiquewhite");
	    dateLabel.setFont (Font.font ("Arial", FontWeight.BOLD, 16));
        paneDate.getChildren ().addAll(dateLabel);

        return paneDate;
    }

    private VBox returnGridPane(HBox box1, VBox box2, VBox box3, VBox box4) {
        VBox hb = new VBox(15);
        hb.getStyleClass().add("hbox");
        hb.getChildren().addAll(box1, box2, box3, box4);
        return hb;
    }

    private String[] getSuggestions() {
	    final ArrayList<Photo> selectedPhotos = PhotoGrid.getSelectedPhotos ();
	    if ( selectedPhotos.size () > 0 ) {
		    List<String> suggestions = selectedPhotos.get (0).getSuggestedTags ().stream ().map (Tag::getValue).collect (Collectors.toList ());
		    return suggestions.toArray (new String[ suggestions.size () ]);
	    }

	    return new String[ 0 ];
    }

	private void updateDate() {
		dateLabel.setText (buildDateString ());
	}
    
    private void updateLocationTags() {
        Set<Tag> tagSet = new HashSet<>();
        final ArrayList<Photo> selectedPhotos = PhotoGrid.getSelectedPhotos();
        for (int i = 0; i < selectedPhotos.size(); ++i) {
            if(i == 0) {
                tagSet.addAll (selectedPhotos.get (i).getLocationTags ());
            } else{
                tagSet.retainAll(selectedPhotos.get(i).getLocationTags());
            }
        }
        System.out.println("List of location tags: " + tagSet);
        Tag[] tagArray = tagSet.toArray(new Tag[tagSet.size()]);

	    button1.setSelected (false);
	    button2.setSelected (false);
	    if (tagArray.length > 0 ) {
		    Tag tag = tagArray[0];
		    if ( tag.getValue ().equals("RoomA" )) {
			    button1.setSelected (true);
		    } else if(tag.getValue().equals("RoomB")){
			    button2.setSelected (true);
		    }
	    }
    }

    private void updateChildrenTags() {
        childrenTags.getChildren().clear();
        Set<Tag> tagSet = new HashSet<>();
        final ArrayList<Photo> selectedPhotos = PhotoGrid.getSelectedPhotos();
        for (int i = 0; i < selectedPhotos.size(); ++i) {
            if(i == 0) {
                tagSet.addAll(selectedPhotos.get(i).getChildTags());
            } else{
                tagSet.retainAll(selectedPhotos.get(i).getChildTags());
            }
        }
        System.out.println("List of children tags: " + tagSet);
        Tag[] tagArray = tagSet.toArray(new Tag[tagSet.size()]);

        if (tagArray.length > 0) {
            for (int i = 0; i < tagArray.length; ++i) {
                final String tagValue = tagArray[i].getValue();

                HBox hBox = new HBox();
                Label label = new Label();
                label.setText(tagValue);

                Button removeButton = new Button("-");
                removeButton.setOnAction(e -> {
                    Tag tag = new Tag(Tag.TagType.KID, tagValue);
                    for (int i1 = 0; i1 < selectedPhotos.size(); ++i1 ) {
                        selectedPhotos.get(i1).removeTag(tag);
                    }
                    updateChildrenTags();
	                updateSuggestions ();
                });
                hBox.getChildren().add(label);
                hBox.getChildren().add(removeButton);
                childrenTags.getChildren().add(hBox);
            }
        }
    }

	private void updateSuggestions() {
		FlowPane childrenSuggestions = new FlowPane (10, 10);
		childrenSuggestions.setPadding (new Insets (10, 10, 10, 10));
		childrenSuggestions.setPrefWrapLength (4.0);
		String[] suggestions = getSuggestions();
		vBoxSuggestion.getChildren ().clear();
		Label childrenSuggestionLabel = new Label ("Suggestions: ");
		childrenSuggestionLabel.setStyle ("-fx-text-fill: antiquewhite");
		childrenSuggestionLabel.setFont (Font.font ("Arial", FontWeight.BOLD, 16));

		System.out.println ("in updating suggestion");
		vBoxSuggestion.getChildren ().add(childrenSuggestionLabel);

		for (int i = 0; i < suggestions.length; i++) {
			HBox boxSuggestion = new HBox ();
			String suggestion = (suggestions[i]);
			Button buttonName = new Button(suggestion + " +");
			buttonName.setStyle("-fx-text-fill: antiquewhite");
			buttonName.setOnAction(event -> {
				final ArrayList<Photo> selectedPhotos = PhotoGrid.getSelectedPhotos();
				Tag tag = new Tag(Tag.TagType.KID, suggestion);
				for (int j = 0; j < selectedPhotos.size(); ++j) {
					selectedPhotos.get(j).addTag(tag);
				}
				((Button)event.getSource ()).setVisible (false);
				updateChildrenTags ();
				updateSuggestions ();
			});
			boxSuggestion.getChildren ().add(buttonName);
			childrenSuggestions.getChildren ().add(boxSuggestion);
		}

		vBoxSuggestion.getChildren().add (childrenSuggestions);
		System.out.println (suggestions.length);
		if(suggestions.length == 0 ) {
			vBoxSuggestion.setVisible (false);
		} else {
			vBoxSuggestion.setVisible (true);
		}
	}

	private String buildDateString() {
		StringBuilder stringBuilder = new StringBuilder ();
		ArrayList<Photo> photos = PhotoGrid.getSelectedPhotos();
		stringBuilder.append ("Date: ");
		if (photos.size() == 0) {
			return "";
		}
		Photo photo = photos.get(0);
		stringBuilder.append(new SimpleDateFormat("d MMM 20YY").format (photo.getDate ()));
		return stringBuilder.toString ();
	}
}
