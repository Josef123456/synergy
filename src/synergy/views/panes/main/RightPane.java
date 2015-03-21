package synergy.views.panes.main;

import javafx.geometry.Pos;
import synergy.views.Main;
import synergy.views.SliderBar;

public class RightPane {
	public RightPane () {
		setupRightPane();
	}

	private void setupRightPane () {
		SliderBar rightFlapBar = new SliderBar (Pos.BASELINE_RIGHT, Main.taggingArea);
		Main.root.setRight (rightFlapBar);
		Main.taggingArea.update ();
	}
}