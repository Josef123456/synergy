package synergy.views.panes.search.date;

import javafx.geometry.Pos;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.util.Callback;
import synergy.views.factories.EndDateCellFactory;
import synergy.views.factories.InitialDateCellFactory;

/**
 * Created by alexstoick on 3/23/15.
 */
public class PeriodDatePane extends HBox {

	private final DatePicker initialDatePicker = new DatePicker ();
	private final DatePicker endDatePicker  = new DatePicker();
	private final Callback<DatePicker, DateCell> initialDateDayCellFactory = new InitialDateCellFactory ();
	private final Callback<DatePicker, DateCell> endDateDayCellFactory = new EndDateCellFactory (initialDatePicker);
	private final Label fromLabel = new Label("From: ");
	private final Label toLabel = new Label("To: ");

	public PeriodDatePane () {
		setSpacing (5);
		setAlignment(Pos.CENTER);
		setupPeriodPane();
	}

	public DatePicker getInitialDatePicker () {
		return initialDatePicker;
	}

	public DatePicker getEndDatePicker () {
		return endDatePicker;
	}

	public void reset() {
		initialDatePicker.setValue (null);
		endDatePicker.setValue (null);
	}

	private void setupPeriodPane() {
		formatDatePicker (initialDatePicker, initialDateDayCellFactory);
		formatDatePicker (endDatePicker, endDateDayCellFactory);

		formatLabel(fromLabel);
		formatLabel(toLabel);

		getChildren ().addAll(fromLabel, initialDatePicker, toLabel, endDatePicker);
	}

	private void formatLabel(Label label) {
		Font font = new Font("Arial", 20);
		label.setStyle("-fx-text-fill: #ffffff");
		label.setFont (font);
	}

	private void formatDatePicker(DatePicker datePicker, Callback<DatePicker, DateCell> factory){
		datePicker.setDayCellFactory(factory);
		datePicker.setMaxWidth(125);
		datePicker.setShowWeekNumbers(false);
		datePicker.setPromptText("dd/mm/yyyy");
	}

}
