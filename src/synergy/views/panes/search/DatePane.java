package synergy.views.panes.search;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import synergy.views.factories.InitialDateCellFactory;
import synergy.views.panes.search.date.MonthAndYearPane;
import synergy.views.panes.search.date.PeriodDatePane;

/**
 * Created by alexstoick on 3/21/15.
 */
public class DatePane extends HBox {

	private final static String[] CATEGORIES = {"Date", "Month", "Period"};
    private ComboBox dateCategories;
    private DatePicker singleDatePicker;
	private StackPane stackCategories;
	private MonthAndYearPane monthAndYear = new MonthAndYearPane ();
	private PeriodDatePane periodPane = new PeriodDatePane ();

	public DatePane () {
		setUpDatePickers ();
		setAlignment (Pos.CENTER);
		getStyleClass().add("my-list-cell");
	}

	public void resetAll() {
		singleDatePicker.setValue(null);
		periodPane.reset();
	}

	public ComboBox getMonths () {
		return monthAndYear.getMonths ();
	}

	public ComboBox getYears () {
		return monthAndYear.getYears ();
	}

	public DatePicker getSingleDatePicker () {
		return singleDatePicker;
	}

	public DatePicker getInitialDatePicker () {
		return periodPane.getInitialDatePicker ();
	}

	public DatePicker getEndDatePicker () {
		return periodPane.getEndDatePicker ();
	}

	public ComboBox getDateCategories () {
		return dateCategories;
	}

	private void setupDateCategories() {
		dateCategories = new ComboBox ();
		dateCategories.getItems().addAll (CATEGORIES);
		dateCategories.setValue (CATEGORIES[ 0 ]);
		updateCategories ();
		dateCategories.setOnAction (event -> updateCategories ());
		dateCategories.setStyle("-fx-text-fill: #ffffff");
	}

	private void setUpDatePickers() {
		singleDatePicker = new DatePicker ();
		singleDatePicker.setDayCellFactory (new InitialDateCellFactory ());
		singleDatePicker.setShowWeekNumbers (false);
		singleDatePicker.setMaxWidth (200);
		setPadding (new Insets (0, 0, 0, 8));
		setSpacing(10);
		stackCategories = new StackPane ();

		setupDateCategories ();

		getChildren ().addAll(dateCategories, stackCategories);
	}

	private void updateCategories() {
		if (dateCategories.getValue().equals("Date")) {
			stackCategories.getChildren().clear();
			stackCategories.getChildren().add(singleDatePicker);
		} else if (dateCategories.getValue().equals("Month")) {
			stackCategories.getChildren().clear();
			stackCategories.getChildren().add(monthAndYear);
		} else if (dateCategories.getValue().equals("Period")) {
			stackCategories.getChildren().clear();
			stackCategories.getChildren().add(periodPane);
		}
	}
}

