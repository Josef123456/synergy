package synergy.views.panes.main;

import javafx.geometry.Pos;
import synergy.views.Main;
import synergy.views.SliderBar;

/**
 * The RightPane class creates a new instance of the SlideBar class and sets it to the right of the Main class.
 * It updates the tagging area accordingly.
 */
public class RightPane {
	public RightPane () {
		setupRightPane();
	}

	private void setupRightPane () {
		SliderBar rightFlapBar = new SliderBar (Pos.TOP_LEFT, Main.taggingArea);
		Main.root.setRight (rightFlapBar);
		Main.taggingArea.update ();
	}
}