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
 * This pane holds the date part of the query. It is composed of 2 subcomponents:
 * {@link synergy.views.panes.search.date.MonthAndYearPane} and {@link synergy.views.panes.search.date.PeriodDatePane}.
 * It also contains a plain date picker, for the single date.
 * Created by alexstoick on 3/21/15.
 */
public class DatePane extends HBox {

	private final static String[] CATEGORIES = {"Date", "Month", "Period"};
    private ComboBox dateCategories;
    private DatePicker singleDatePicker;
	private StackPane stackCategories;
	private MonthAndYearPane monthAndYear = new MonthAndYearPane ();
	private PeriodDatePane periodPane = new PeriodDatePane ();

	/**
	 * Constructor setups up basic HBox properties and calls {@link #setupDatePickers()}
	 */
	public DatePane () {
		setAlignment (Pos.CENTER);
		getStyleClass().add("my-list-cell");
		setupDatePickers ();
	}

	public void resetAll() {
		singleDatePicker.setValue(null);
		periodPane.reset();
		monthAndYear.reset();
	}

	/**
	 *
	 * @return the {@link javafx.scene.control.ComboBox} that displays the months.
	 */
	public ComboBox getMonths () {
		return monthAndYear.getMonths ();
	}

	/**
	 *
	 * @return the {@link javafx.scene.control.ComboBox} that displays the years.
	 */
	public ComboBox getYears () {
		return monthAndYear.getYears ();
	}


	/**
	 *
	 * @return the single day {@link javafx.scene.control.DatePicker}
	 */
	public DatePicker getSingleDatePicker () {
		return singleDatePicker;
	}

	/**
	 *
	 * @return the initial {@link javafx.scene.control.DatePicker} in the period pane
	 */
	public DatePicker getInitialDatePicker () {
		return periodPane.getInitialDatePicker ();
	}

	/**
	 *
	 * @return the final {@link javafx.scene.control.DatePicker} in the period pane
	 */
	public DatePicker getEndDatePicker () {
		return periodPane.getEndDatePicker ();
	}

	/**
	 *
	 * @return the {@link javafx.scene.control.ComboBox} that holds the type of the date
	 */
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

	private void setupDatePickers () {
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

