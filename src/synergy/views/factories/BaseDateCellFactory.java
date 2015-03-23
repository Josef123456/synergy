package synergy.views.factories;

import java.time.LocalDate;

/**
 * Created by alexstoick on 3/21/15.
 */
public class BaseDateCellFactory {
	//formats the given date in the form of dd/mm/yyyy
	String formatDate (LocalDate item) {
		StringBuilder stringBuilder = new StringBuilder ();
		String dayOfMonth = String.valueOf (item.getDayOfMonth ());
		if ( dayOfMonth.length () == 1 ) {
			stringBuilder.append ("0" + dayOfMonth + "/");
		} else {
			stringBuilder.append (dayOfMonth + "/");
		}
		String monthValue = String.valueOf (item.getMonthValue ());
		if ( monthValue.length () == 1 ) {
			stringBuilder.append ("0" + monthValue + "/");
		} else {
			stringBuilder.append (monthValue + "/");
		}
		stringBuilder.append (item.getYear ());
		return stringBuilder.toString ();
	}
}
