package synergy.views.panes;

import synergy.views.BottomArea;
import synergy.views.Main;

public class BottomPane {
	public BottomPane () {
		setupBottomArea();
	}

	private void setupBottomArea () {
		BottomArea bottomArea = new BottomArea ();
		Main.root.setBottom (bottomArea);
	}
}