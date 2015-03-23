package synergy.views.panes.main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import synergy.views.Main;
import synergy.views.PhotoGrid;

import java.util.ArrayList;
/**
 * The CenterPane class creates a new instance of the PhotoGrid class with the list of images
 * and sets it to the center of the Main class.
 */
public class CenterPane {
	public CenterPane () {
		setupCenterArea ();
	}

	private void setupCenterArea() {
		ObservableList<Image> displayedImagesList = FXCollections.observableArrayList (new ArrayList<> ());
		Main.photosGrid = new PhotoGrid (displayedImagesList, Main.taggingArea);
		Main.root.setCenter (Main.photosGrid);
	}
}