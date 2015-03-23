package synergy.views.factories;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;
import synergy.models.Photo;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

/**
 * A datecellfactory that formats the cells in the initialdatecell datepicker
 * Created by alexstoick on 3/21/15.
 */
public class InitialDateCellFactory extends BaseDateCellFactory implements Callback<DatePicker, DateCell> {
	@Override
	public DateCell call (final DatePicker datePicker) {
		return new DateCell () {
			@Override
			public void updateItem (LocalDate item, boolean empty) {
				super.updateItem (item, empty);
				setMinSize (50, 50);
				ArrayList<Date> uniqueDates = Photo.getUniqueDates();
				for ( int i = 0 ; i < uniqueDates.size () ; i++ ) {
					if ( formatDate (item).equals (new SimpleDateFormat ("dd/MM/yyyy").format(uniqueDates.get(i))) ) {
						setStyle ("-fx-background-color: #00c0cb;");
					}
				}
			}
		};
	}
}
