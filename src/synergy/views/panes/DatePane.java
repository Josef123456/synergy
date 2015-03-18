package synergy.views.panes;

import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import synergy.models.Photo;
import synergy.views.PhotoGrid;
import synergy.views.TaggingArea;
import synergy.views.panes.base.BaseVerticalPane;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by alexstoick on 3/18/15.
 */
public class DatePane extends BaseVerticalPane {

	private TaggingArea taggingArea;
	private Label dateLabel;

	public DatePane (TaggingArea taggingArea) {
		this.taggingArea = taggingArea;
		setupDatePane ();
	}

	private void setupDatePane () {
		getStyleClass ().add("grid");
		dateLabel = new Label (buildDateString ());

		dateLabel.setStyle ("-fx-text-fill: antiquewhite");
		dateLabel.setFont (Font.font ("Arial", FontWeight.BOLD, 16));
		getChildren ().addAll(dateLabel);
	}

	public void update() {
		dateLabel.setText (buildDateString ());
	}

	private String buildDateString() {
		StringBuilder stringBuilder = new StringBuilder ();
		ArrayList<Photo> photos = PhotoGrid.getSelectedPhotos ();
		stringBuilder.append ("Date: ");
		if (photos.size() == 0) {
			return "";
		}
		Photo photo = photos.get(0);
		stringBuilder.append(new SimpleDateFormat ("d MMM 20YY").format (photo.getDate ()));
		return stringBuilder.toString ();
	}

}
