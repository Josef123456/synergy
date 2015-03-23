package synergy.views.panes.tagging;

import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import synergy.models.Photo;
import synergy.views.PhotoGrid;
import synergy.views.TaggingArea;
import synergy.views.panes.base.BaseVerticalPane;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Contains the date of the photo(s) seleted in the form of a label
 * Created by alexstoick on 3/18/15.
 */
public class DatePane extends BaseVerticalPane {

    private TaggingArea taggingArea;
    private Label dateLabel;

    /**
     * Calls {@link #setupDatePane()} that builds the gui
     * @param taggingArea the parent pane of this class
     */
	public DatePane(TaggingArea taggingArea) {
		this.taggingArea = taggingArea;
		setupDatePane ();
	}

	private void setupDatePane () {
		getStyleClass().add("grid");
		dateLabel = new Label (buildDateString ());

        dateLabel.setStyle("-fx-text-fill: #ffffff");
        dateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        getChildren().addAll(dateLabel);
    }

    /**
     * updates the label with the date of the photos
     */
    public void update() {
        dateLabel.setText(buildDateString());
    }

    private String buildDateString() {
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<Photo> photos = PhotoGrid.getSelectedPhotos();
        stringBuilder.append(" Date: ");
        if (photos.size() == 0) {
            return "";
        }

        Date firstDate = photos.get(0).getDate();
        Date lastDate = photos.get(0).getDate();
        for (Photo photo : photos) {
            Date iterationDate = photo.getDate();
            if (firstDate.after(iterationDate)) {
                firstDate = photo.getDate();
            }
            if (lastDate.before(iterationDate)) {
                lastDate = iterationDate;
            }
        }

        String firstPhotoDate = new SimpleDateFormat("d MMM 20yy").format(firstDate);
        stringBuilder.append(firstPhotoDate);
        if (photos.size() > 1) {
            String lastPhotoDate = new SimpleDateFormat("d MMM 20yy").format(lastDate);
            if (!firstPhotoDate.equals(lastPhotoDate)) {
                stringBuilder.append(" - " + lastPhotoDate);
            }
        }

        return stringBuilder.toString();
    }

}
