package synergy.views.panes.tagging;

import javafx.application.Platform;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import synergy.models.Photo;
import synergy.models.Tag;
import synergy.views.PhotoGrid;
import synergy.views.panes.base.BaseHorizontalPane;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by alexstoick on 3/18/15.
 */
public class LocationPane extends BaseHorizontalPane {

    private ToggleButton roomAbtn;
    private ToggleButton roomBbtn;

    public LocationPane () {
        setupLocationPane();
    }


    private void setupLocationPane() {
        this.setSpacing(20);
        getStyleClass().add("grid");

        HBox boxLocation = new HBox(5);
        roomAbtn = new ToggleButton("Baby Room");
        roomBbtn = new ToggleButton("Main Room");
        ToggleGroup toggleGroup = new ToggleGroup();
        roomAbtn.setToggleGroup(toggleGroup);
        roomBbtn.setToggleGroup(toggleGroup);

        roomAbtn.setOnAction(event -> {
            roomButtonAction(roomAbtn);
        });

        roomBbtn.setOnAction(event -> {
            roomButtonAction(roomBbtn);
        });

        Text locationText = new Text(" Location:");
        locationText.setId("leftText");
        locationText.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        boxLocation.getChildren().addAll(roomAbtn, roomBbtn);
        getChildren().addAll(locationText, boxLocation);
    }

    private void roomButtonAction(ToggleButton roomButton) {
        Platform.runLater(() -> {
            final ArrayList<Photo> selectedPhotos = PhotoGrid
                    .getSelectedPhotos ();
            Tag tag = null;
            boolean roomSelected = roomButton.isSelected();
            if (roomSelected) {
                tag = new Tag(Tag.TagType.PLACE, roomButton.getText());
                for (Photo photo : selectedPhotos) {
                    Tag locationTag = photo.getLocationTag();
                    if (locationTag != null)
                        photo.removeTag(photo.getLocationTag());
                    photo.addTag(tag);
                }
            } else {
                for (Photo photo : selectedPhotos) {
                    photo.removeTag(photo.getLocationTag());
                }
            }
        });
    }

    public void update() {

        final ArrayList<Photo> selectedPhotos = PhotoGrid.getSelectedPhotos();
	    List<Tag> tagArray = selectedPhotos.stream ().
			    map (Photo::getLocationTag).
			    filter (t -> t != null).
			    collect (Collectors.toList ());

        System.out.println("List of location tags: " + tagArray);
	    roomAbtn.setSelected(false);
	    roomBbtn.setSelected(false);
        if (tagArray.size() > 0) {
            int roomA = 0;
            int roomB = 0 ;
            for (Tag tag : tagArray) {
                if (tag.getValue().equals("Baby Room")) {
                    ++ roomA;
                } else if (tag.getValue().equals("Main Room")) {
                    ++roomB;
                }
            }
	        if ( roomA == selectedPhotos.size() ) {
		        roomAbtn.setSelected (true);
	        } else if ( roomB == selectedPhotos.size () ) {
		        roomBbtn.setSelected (true);
	        }
        }
    }
}
