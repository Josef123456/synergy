package synergy.views.panes;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import synergy.models.Photo;
import synergy.models.Tag;
import synergy.views.AutoCompleteComboBoxListener;
import synergy.views.PhotoGrid;
import synergy.views.TaggingArea;
import synergy.views.panes.base.BaseVerticalPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by alexstoick on 3/17/15.
 */
public class ChildrenPane extends BaseVerticalPane {

	private FlowPane childrenTags;
	private ComboBox<CharSequence> childrenComboBox;
	String[] kidsInDatabase = { "alex", "cham", "codrin", "sari", "josef", "amit", "mike", "tobi"};
	private TaggingArea taggingArea;
	//Tag.getAllChildrenTags().stream ().map (Tag::getValue).collect(Collectors.toList ());

	public ChildrenPane(TaggingArea taggingArea) {
		this.taggingArea = taggingArea;
		setupChildrenPane ();
	}

	public void update() {
		childrenTags.getChildren().clear();
		Set<Tag> tagSet = new HashSet<> ();
		final ArrayList<Photo> selectedPhotos = PhotoGrid.getSelectedPhotos ();
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
			updateTagList (selectedPhotos, tagArray);
		}
	}

	private void addChildrenTag(String name){
		Set<String> hashSet = new HashSet<>(Arrays.asList (kidsInDatabase));
		if(hashSet.contains(name)){
			childrenComboBox.getEditor().setText("");
			final ArrayList<Photo> selectedPhotos = PhotoGrid.getSelectedPhotos();
			Tag tag = new Tag(Tag.TagType.KID, name);
			for (int i = 0; i < selectedPhotos.size(); ++i) {
				selectedPhotos.get(i).addTag(tag);
			}
			taggingArea.update ();
		}
	}

	private void setupChildrenPane () {
		this.setSpacing (10);
		getStyleClass ().add ("grid");

		GridPane gridNorthern = new GridPane ();
		childrenTags = new FlowPane (10, 10);
		childrenTags.setPadding(new Insets (10, 10, 10, 10));
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

		getChildren ().addAll(gridNorthern, childrenTags);
	}

	private void updateTagList (ArrayList<Photo> selectedPhotos, Tag[] tagArray) {
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
				taggingArea.update ();
			});
			hBox.getChildren().add(label);
			hBox.getChildren().add(removeButton);
			childrenTags.getChildren().add(hBox);
		}
	}
}
