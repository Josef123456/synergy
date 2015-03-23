package synergy.views.panes.main;

import javafx.geometry.Insets;
import synergy.views.BottomArea;
import synergy.views.Main;

/**
 * The BottomPane class creates a new instance of the BottomArea and sets it to the bottom of the Main class.
 */
public class BottomPane {
	public BottomPane () {
		setupBottomArea();
	}

	private void setupBottomArea () {
		BottomArea bottomArea = new BottomArea ();
		Main.root.setBottom (bottomArea);
		bottomArea.setPadding(new Insets (8, 0, 0, 0));
	}
}