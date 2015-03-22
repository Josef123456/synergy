package synergy.views.panes.main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import synergy.views.Main;
import synergy.views.PhotoGrid;

import java.util.ArrayList;

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