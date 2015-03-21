package synergy.views.panes;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import synergy.views.AutoCompleteComboBoxListener;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by alexstoick on 3/21/15.
 */
public class ChildrenPane extends HBox {

	private ComboBox comboBox;
	private Button addButton;
	private HBox searchQueryButtons;
	private int height = 50 ;
	private Set<String> listOfSearchedKids;

	private String[] mockChildrenData = {"alex", "cham", "codrin", "sari", "josef", "amit",
			"mike", "tobi"};

	public ChildrenPane () {
		getStyleClass().add("my-list-cell");
		setAlignment (Pos.CENTER);
		setUpTextFieldAndSearch();
		listOfSearchedKids = new HashSet<>();
		comboBox.setMinHeight(height - 5);
		comboBox.getEditor ().setId ("searching");
		comboBox.getEditor ().setFont (Font.font ("Arial", FontPosture.ITALIC, 25));
		addButton.setMinHeight(height);
	}

	public void resetAll() {
		searchQueryButtons.getChildren().clear();
		listOfSearchedKids.clear();
		comboBox.getEditor().setText("");
	}

	public Set<String> getListOfSearchedKids () {
		return listOfSearchedKids;
	}


	public void setUpTextFieldAndSearch() {
		searchQueryButtons = new HBox();

		comboBox = new ComboBox ();
		comboBox.setMaxWidth(200);
		for (String childName : mockChildrenData) {
			comboBox.getItems().add(childName);
		}
		AutoCompleteComboBoxListener autoComplete = new AutoCompleteComboBoxListener(comboBox);
		comboBox.setOnKeyReleased(autoComplete);
		setHgrow (this, Priority.ALWAYS);
		addButton = new Button ("+");

		EventHandler eventHandler = event -> {
			addChildrenQuery((String) comboBox.getValue());
			updateChildrenQueries();
		};
		addButton.setOnAction(eventHandler);

		comboBox.getEditor().addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent E) ->{
			if(E.getCode() == KeyCode.ENTER){
				System.out.println("It worked!");
				addChildrenQuery((String) comboBox.getValue());
				updateChildrenQueries();
			}
		});


		getChildren ().addAll(searchQueryButtons, comboBox, addButton);
	}

	public void updateChildrenQueries() {
		searchQueryButtons.getChildren().clear();
		for (String query : listOfSearchedKids ) {
			Button queryButton = new Button(query + " - ");
			queryButton.setOnAction(event -> {
				listOfSearchedKids.remove (query);
				updateChildrenQueries();
			});
			queryButton.setMinHeight(height);
			queryButton.setStyle("-fx-text-fill: #ffffff");
			searchQueryButtons.getChildren().add(queryButton);
		}
	}

	private void addChildrenQuery(String addedQuery) {
		Set<String> hashSet = new HashSet<String> (Arrays.asList (mockChildrenData)) {
			public boolean contains(Object o) {
				String paramStr = (String) o;
				for (String s : this) {
					if (paramStr.equalsIgnoreCase(s)) return true;
				}
				return false;
			}
		};
		if (addedQuery != null && !listOfSearchedKids.contains(addedQuery) && hashSet.contains
				(addedQuery)) {
			String toAdd = null; // The String to add to the list of search
			Iterator iterator = hashSet.iterator();
			while (iterator.hasNext()) {
				String s = (String) iterator.next();
				if (s.equalsIgnoreCase(addedQuery)) {
					toAdd = s;
					break;
				}
			}
			listOfSearchedKids.add(toAdd);
			comboBox.getEditor().setText("");
		}
	}

}
