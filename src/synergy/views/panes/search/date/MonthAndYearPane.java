package synergy.views.panes.search.date;

import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import synergy.models.Photo;

/**
 * Created by alexstoick on 3/23/15.
 */
public class MonthAndYearPane extends HBox {

	private static final String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	private static final Object[] YEARS = Photo.getUniqueYears ();
	private ComboBox months, years;


	public MonthAndYearPane () {
		setAlignment (Pos.CENTER);
		setupMonthAndYear();
	}

	public void reset() {
		months.setValue("");
		years.setValue ("");
	}

	public ComboBox getMonths () {
		return months;
	}

	public ComboBox getYears () {
		return years;
	}

	private void setupMonthAndYear() {
		months = new ComboBox ();
		months.getItems().addAll(MONTHS);
		years = new ComboBox();
		years.getItems ().addAll (YEARS);
		getChildren ().addAll(months,years);
	}
}
