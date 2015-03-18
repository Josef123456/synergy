package synergy.views.panes;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import synergy.models.Photo;
import synergy.models.Tag;
import synergy.views.PhotoGrid;
import synergy.views.TaggingArea;
import synergy.views.panes.base.BaseVerticalPane;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by alexstoick on 3/18/15.
 */
public class SuggestionsPane extends BaseVerticalPane {

	private TaggingArea taggingArea;

	public SuggestionsPane (TaggingArea taggingArea) {
		this.taggingArea = taggingArea;
		setupSuggestionPane ();
	}

	private void setupSuggestionPane () {
		getStyleClass().add("grid");
	}

	public void update() {
		FlowPane childrenSuggestions = new FlowPane (10, 10);
		childrenSuggestions.setPadding (new Insets (10, 10, 10, 10));
		childrenSuggestions.setPrefWrapLength (4.0);
		String[] suggestions = getSuggestions();
		getChildren ().clear();
		Label childrenSuggestionLabel = new Label ("Suggestions: ");
		childrenSuggestionLabel.setStyle ("-fx-text-fill: antiquewhite");
		childrenSuggestionLabel.setFont (Font.font ("Arial", FontWeight.BOLD, 16));

		System.out.println ("in updating suggestion");
		getChildren ().add(childrenSuggestionLabel);

		for (int i = 0; i < suggestions.length; i++) {
			HBox boxSuggestion = new HBox ();
			String suggestion = (suggestions[i]);
			Button buttonName = new Button(suggestion + " +");
			buttonName.setStyle("-fx-text-fill: antiquewhite");
			buttonName.setOnAction(event -> {
				final ArrayList<Photo> selectedPhotos = PhotoGrid.getSelectedPhotos ();
				Tag tag = new Tag(Tag.TagType.KID, suggestion);
				for (int j = 0; j < selectedPhotos.size(); ++j) {
					selectedPhotos.get(j).addTag(tag);
				}
				((Button)event.getSource ()).setVisible (false);
				update();
			});
			boxSuggestion.getChildren ().add(buttonName);
			childrenSuggestions.getChildren ().add(boxSuggestion);
		}

		getChildren ().add (childrenSuggestions);
		System.out.println (suggestions.length);
		if(suggestions.length == 0 ) {
			setVisible (false);
		} else {
			setVisible (true);
		}
	}

	private String[] getSuggestions() {
		final ArrayList<Photo> selectedPhotos = PhotoGrid.getSelectedPhotos ();
		if ( selectedPhotos.size () > 0 ) {
			List<String> suggestions = selectedPhotos.get (0).getSuggestedTags ().stream ().map (Tag::getValue).collect (Collectors.toList ());
			return suggestions.toArray (new String[ suggestions.size () ]);
		}

		return new String[ 0 ];
	}

}
