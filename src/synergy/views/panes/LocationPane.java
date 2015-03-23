package synergy.views.panes;

import javafx.geometry.Pos;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

/**
 * Created by alexstoick on 3/21/15.
 */
public class LocationPane extends HBox {

	private ToggleButton locationA, locationB;

	public LocationPane () {
		getStyleClass().add("my-list-cell");
		setAlignment (Pos.CENTER);
		setUpLocationButtons ();
        setSpacing(1);
	}

	public void resetAll() {
		locationA.setSelected(false);
		locationB.setSelected(false);
	}

	public ToggleButton getLocationA () {
		return locationA;
	}

	public ToggleButton getLocationB () {
		return locationB;
	}

	private void setUpLocationButtons() {
		ToggleGroup toggleGroup = new ToggleGroup();
		locationA = new ToggleButton ("RoomA");
		locationA.setMinWidth(50);
		locationB = new ToggleButton("RoomB");
		locationB.setMinWidth(50);
		locationA.setToggleGroup(toggleGroup);
		locationB.setToggleGroup(toggleGroup);

		getChildren ().add(locationA);
		getChildren ().add(locationB);
	}
}
