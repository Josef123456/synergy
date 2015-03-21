package synergy.views.panes.main;

import javafx.geometry.Insets;
import synergy.views.BottomArea;
import synergy.views.Main;

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