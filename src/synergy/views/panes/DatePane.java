package synergy.views.panes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.util.Callback;
import synergy.models.Photo;
import synergy.views.factories.EndDateCellFactory;
import synergy.views.factories.InitialDateCellFactory;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by alexstoick on 3/21/15.
 */
public class DatePane extends HBox {

	String[] arrayMonths = {"January", "February", "March", "April", "May", "June", "July",
			"August", "September", "October", "November", "December"};
	String[] arrayCategories = {"Date", "Month", "Period"};

	private ComboBox dateCategories;
	private DatePicker singleDatePicker;
	private ComboBox months, years;
	private StackPane stackCategories;
	private DatePicker initialDatePicker, endDatePicker;

	public DatePane () {
		setUpDatePickers ();
		setAlignment (Pos.CENTER);
		getStyleClass().add("my-list-cell");
	}

	public void resetAll() {
		singleDatePicker.setValue(null);
		initialDatePicker.setValue(null);
		endDatePicker.setValue(null);
		months.setValue("");
	}

	public ComboBox getMonths () {
		return months;
	}

	public ComboBox getYears () {
		return years;
	}

	public DatePicker getSingleDatePicker () {
		return singleDatePicker;
	}

	public DatePicker getInitialDatePicker () {
		return initialDatePicker;
	}

	public DatePicker getEndDatePicker () {
		return endDatePicker;
	}

	public ComboBox getDateCategories () {
		return dateCategories;
	}

	private HBox monthAndYear;
	private HBox periodPane;


	public void setUpDatePickers() {
		singleDatePicker = new DatePicker ();
		setPadding (new Insets (0, 0, 0, 8));
		setSpacing(10);
		stackCategories = new StackPane ();

		dateCategories = new ComboBox ();
		dateCategories.getItems().addAll (arrayCategories);
		dateCategories.setValue (arrayCategories[ 0 ]);
		updateCategories ();
		dateCategories.setOnAction (event -> updateCategories ());
		dateCategories.setStyle("-fx-text-fill: #ffffff");

		monthAndYear = new HBox();
		monthAndYear.setAlignment (Pos.CENTER);
		months = new ComboBox();
		months.getItems().addAll(arrayMonths);
		years = new ComboBox();

		periodPane = new HBox();
		periodPane.setSpacing (5);
		periodPane.setAlignment(Pos.CENTER);
		initialDatePicker = new DatePicker();
		endDatePicker = new DatePicker();
		final Callback<DatePicker, DateCell> initialDateDayCellFactory = new InitialDateCellFactory ();

		final Callback<DatePicker, DateCell> endDateDayCellFactory = new EndDateCellFactory (initialDatePicker);

		singleDatePicker.setDayCellFactory (initialDateDayCellFactory);
		singleDatePicker.setShowWeekNumbers (false);
		singleDatePicker.setMaxWidth (200);

		initialDatePicker.setDayCellFactory(initialDateDayCellFactory);
		initialDatePicker.setMaxWidth(125);
		initialDatePicker.setShowWeekNumbers(false);
		initialDatePicker.setPromptText("dd/mm/yyyy");

		endDatePicker.setDayCellFactory(endDateDayCellFactory);
		endDatePicker.setMaxWidth(125);
		endDatePicker.setShowWeekNumbers(false);
		endDatePicker.setPromptText("dd/mm/yyyy");

		Font font = new Font("Arial", 20);
		Label fromLabel = new Label("From: ");
		fromLabel.setStyle("-fx-text-fill: #ffffff");
		fromLabel.setFont(font);
		Label toLabel = new Label("To: ");
		toLabel.setStyle("-fx-text-fill: #ffffff");
		toLabel.setFont(font);

		periodPane.getChildren().addAll(fromLabel, initialDatePicker, toLabel, endDatePicker);
		getChildren ().addAll(dateCategories, stackCategories);
	}

	public void updateCategories() {
		if (dateCategories.getValue().equals("Date")) {
			stackCategories.getChildren().clear();
			stackCategories.getChildren().add(singleDatePicker);
		} else if (dateCategories.getValue().equals("Month")) {
			stackCategories.getChildren().clear();
			Set<String> uniqueYears = new HashSet ();
			for (int i = 0; i < Photo.getUniqueDates().size(); i++) {
				uniqueYears.add(new SimpleDateFormat("yyyy").format(Photo.getUniqueDates().get(i)));
			}
			Object[] arrayYears = uniqueYears.toArray();
			years.getItems().addAll(arrayYears);
			monthAndYear.getChildren().addAll(months, years);
			stackCategories.getChildren().add(monthAndYear);
		} else if (dateCategories.getValue().equals("Period")) {
			stackCategories.getChildren().clear();
			stackCategories.getChildren().add(periodPane);
		}
	}
}

