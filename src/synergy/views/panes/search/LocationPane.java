package synergy.views.panes.search;

import javafx.geometry.Pos;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

/**
 * This class creates two toggle buttons for the rooms of the nursery.
 * Created by alexstoick on 3/21/15.
 */
public class LocationPane extends HBox {

	private ToggleButton locationA, locationB;

	/**
	 * Constructor that sets up basic HBox properties.
	 */
	public LocationPane () {
		getStyleClass().add("my-list-cell");
		setAlignment (Pos.CENTER);
		setUpLocationButtons ();
        setSpacing(1);
	}

	/**
	 * Resets the buttons to initial states (not selected).
	 */
	public void resetAll() {
		locationA.setSelected(false);
		locationB.setSelected(false);
	}

	/**
	 *
	 * @return {@link javafx.scene.control.ToggleButton} for the first location
	 */
	public ToggleButton getLocationA () {
		return locationA;
	}

	/**
	 *
	 * @return {@link javafx.scene.control.ToggleButton} for the second location
	 */
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
