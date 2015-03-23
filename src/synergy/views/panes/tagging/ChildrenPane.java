package synergy.views.panes.tagging;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import synergy.models.Photo;
import synergy.models.Tag;
import synergy.views.AutoCompleteUtil;
import synergy.views.PhotoGrid;
import synergy.views.TaggingArea;
import synergy.views.panes.base.BaseVerticalPane;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by alexstoick on 3/17/15.
 */
public class ChildrenPane extends BaseVerticalPane {

	private FlowPane childrenTags;
	private ComboBox<CharSequence> childrenComboBox;
    private List<String> childrenData;
	private TaggingArea taggingArea;
	//Tag.getAllChildrenTags().stream ().map (Tag::getValue).collect(Collectors.toList ());


	public ChildrenPane(TaggingArea taggingArea) {
        childrenData = new ArrayList<>();
        List<Tag> allTags = Tag.getAllChildrenTags();
        childrenData.addAll(allTags.stream().map(Tag::getValue).collect(Collectors.toList()));
		this.taggingArea = taggingArea;
		setupChildrenPane ();
	}

	public void update() {
		childrenTags.getChildren().clear();
		Set<Tag> tagSet = new HashSet<> ();
		final ArrayList<Photo> selectedPhotos = PhotoGrid.getSelectedPhotos ();
		for (int i = 0; i < selectedPhotos.size(); ++i) {
			if(i == 0) {
				tagSet.addAll(selectedPhotos.get(i).getChildTags());
			} else{
				tagSet.retainAll(selectedPhotos.get(i).getChildTags());
			}
		}
		System.out.println("List of children tags: " + tagSet);
		Tag[] tagArray = tagSet.toArray(new Tag[tagSet.size()]);

		if (tagArray.length > 0) {
			updateTagList (selectedPhotos, tagArray);
		}
	}

	private void addChildrenTag(String name){
		String tagName = null;
		Set<String> hashSet = new HashSet<String>(childrenData){
			public boolean contains(Object o){
				String paramStr = (String)o;
				for (String s : this) {
					if (paramStr.equalsIgnoreCase(s)) return true;
				}
				return false;
			}
		};
		if(hashSet.contains(name)){
            for (String s : hashSet) {
                if (s.equalsIgnoreCase(name)) {
                    tagName = s;
                    break;
                }
            }
			final ArrayList<Photo> selectedPhotos = PhotoGrid.getSelectedPhotos();
			Tag tag = new Tag(Tag.TagType.KID, tagName);
            for (Photo selectedPhoto : selectedPhotos) {
                selectedPhoto.addTag(tag);
            }
			taggingArea.update ();
			childrenComboBox.getEditor().setText("");
		}
	}

	private void setupChildrenPane () {
		this.setSpacing (10);
		getStyleClass ().add ("grid");

		GridPane gridNorthern = new GridPane ();
		childrenTags = new FlowPane (10, 10);
		childrenTags.setPadding(new Insets (10, 10, 10, 10));
		childrenTags.setPrefWrapLength(4.0);
		HBox childrenPane = new HBox (10);
		childrenComboBox = new ComboBox<> ();
		childrenComboBox.setId("searching");
		for (String childrenNames : childrenData ) {
			childrenComboBox.getItems ().add(childrenNames);
		}
		AutoCompleteUtil.autoCompleteComboBox(childrenComboBox, AutoCompleteUtil.AutoCompleteMode.STARTS_WITH);
		Button addChildrenTagButton = new Button ("+");
		addChildrenTagButton.setStyle ("-fx-text-fill: #ffffff");
		addChildrenTagButton.setStyle ("-fx-background-color: #595959");
		addChildrenTagButton.setStyle ("-fx-text-fill: #ffffff");

		Text childrenText = new Text (" Children: ");
		childrenText.setId ("leftText");
		childrenText.setFont (Font.font ("Arial", FontWeight.BOLD, 16));

		EventHandler childrenEventHandler = event -> {
			String name = childrenComboBox.getEditor().getText();
			addChildrenTag(name);
		};
		childrenComboBox.getEditor().setText ("");


		addChildrenTagButton.setOnAction (childrenEventHandler);
		childrenComboBox.getEditor().addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent E) -> {
			if (E.getCode() == KeyCode.ENTER) {
				System.out.println("It worked!");
				String name = childrenComboBox.getEditor().getText();
				addChildrenTag(name);
			}
		});
		childrenPane.getChildren().addAll(childrenComboBox, addChildrenTagButton);

		gridNorthern.add (childrenText, 0, 0);
		gridNorthern.add (childrenPane, 1, 0);

		getChildren ().addAll(gridNorthern, childrenTags);
	}

	private void updateTagList (ArrayList<Photo> selectedPhotos, Tag[] tagArray) {
        for (Tag aTagArray : tagArray) {
            final String tagValue = aTagArray.getValue();

            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            Label label = new Label();
            label.setText(tagValue);


            Button removeButton = new Button("-");
            removeButton.setOnAction(e -> {
                Tag tag = new Tag(Tag.TagType.KID, tagValue);
                for (Photo selectedPhoto : selectedPhotos) {
                    selectedPhoto.removeTag(tag);
                }
                taggingArea.update();
            });
            hBox.getChildren().add(label);
            hBox.getChildren().add(removeButton);
            childrenTags.getChildren().add(hBox);
        }
	}
}
