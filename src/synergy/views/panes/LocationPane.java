package synergy.views.panes;

import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import synergy.models.Photo;
import synergy.models.Tag;
import synergy.views.PhotoGrid;
import synergy.views.TaggingArea;
import synergy.views.panes.base.BaseHorizontalPane;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by alexstoick on 3/18/15.
 */
public class LocationPane extends BaseHorizontalPane {

	private TaggingArea taggingArea;
	private ToggleButton button1;
	private ToggleButton button2;

	public LocationPane (TaggingArea taggingArea) {
		this.taggingArea = taggingArea;
		setupLocationPane();
	}


	private void setupLocationPane () {
		this.setSpacing (20);
		getStyleClass ().add("grid");

		HBox boxLocation = new HBox (5);
		button1 = new ToggleButton ("RoomA");
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
		getChildren ().addAll(locationText, boxLocation);
	}


	public void update() {
		Set<Tag> tagSet = new HashSet<> ();
		final ArrayList<Photo> selectedPhotos = PhotoGrid.getSelectedPhotos ();
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
}
