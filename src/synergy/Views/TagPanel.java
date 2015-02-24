package synergy.Views;

import synergy.models.Photo;
import synergy.models.Tag;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class TagPanel extends JPanel {
	private ArrayList<Photo> photos;


	private JButton addLocationTagButton = new JButton("+");
	private final JComboBox<String> locationTextField = new JComboBox<String>();

	private JButton addChildTagButton = new JButton("+");
	final JComboBox<String> childrenTextField = new JComboBox<String>();

	private JPanel locationPanel;
	private JPanel locationTags;

	private JPanel childrenPanel;
	private JPanel childrenTags;

    PhotosPanel photosPanel;

    public TagPanel(ArrayList<Photo> photos, PhotosPanel photosPanel) {
        this.photos = photos;

        this.photosPanel = photosPanel;
        setUpUILocation();
        setUpUIChildren();
        setLayout(new GridLayout(2, 1));
    }

	private void addActionListenerToLocationTagButton () {
		addLocationTagButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				final Integer[] selectedIndexes = photosPanel.getSelectedIndexesAsArray ();
				Tag tag = new Tag (Tag.TagType.PLACE, (String) locationTextField.getSelectedItem ());
				for ( int i = 0 ; i < selectedIndexes.length ; ++i ) {
					photos.get (selectedIndexes[ i ]).addTag (tag);
				}
				updateLocationTags ();
			}
		});
	}

	private void addActionListenerToChildTagButton() {
		addChildTagButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				final Integer[] selectedIndexes = photosPanel.getSelectedIndexesAsArray ();
				Tag tag = new Tag(Tag.TagType.KID, (String) childrenTextField.getSelectedItem());
				for ( int i = 0 ; i < selectedIndexes.length; ++ i ) {
					photos.get(selectedIndexes[i]).addTag (tag);
				}
				updateChildrenTags();
			}
		});
	}

    private void setUpUILocation() {
        JLabel locationLabel = new JLabel("Location");
        locationTextField.setEditable(true);

	    addActionListenerToLocationTagButton();

        locationPanel = new JPanel();
        locationPanel.setLayout(new GridLayout(3, 1));
        locationTags = new JPanel();

        JPanel locationTagQuery = new JPanel();
        locationTagQuery.setLayout(new GridLayout(1, 2));
        locationTagQuery.add(locationTextField);
        locationTagQuery.add(addLocationTagButton);

        locationPanel.add(locationLabel);
        locationPanel.add(locationTagQuery);
        locationPanel.add(locationTags);
        add(locationPanel);
    }

    public void setUpUIChildren(){
        JLabel childrenLabel = new JLabel("Children");

        childrenTextField.setEditable(true);

	    addActionListenerToChildTagButton();

        childrenPanel = new JPanel();
        childrenPanel.setLayout(new GridLayout(3, 1));
        childrenTags = new JPanel();

        JPanel childrenTagQuery = new JPanel();
        childrenTagQuery.setLayout(new GridLayout(1, 2));
        childrenTagQuery.add(childrenTextField);
        childrenTagQuery.add(addChildTagButton);

        childrenPanel.add(childrenLabel);
        childrenPanel.add(childrenTagQuery);
        childrenPanel.add(childrenTags);
        add(childrenPanel);
    }

	public void update() {
		updateLocationTags ();
		updateChildrenTags ();
	}

    private void updateLocationTags() {
        JPanel tagPanelLocation = new JPanel();
        tagPanelLocation.setLayout(new FlowLayout());

        Set<Tag> tagSet = new HashSet<> ();
	    final Integer[] selectedIndexes = photosPanel.getSelectedIndexesAsArray ();
	    for ( int i = 0 ; i < selectedIndexes.length; ++ i ) {
		    tagSet.addAll (photos.get (i).getLocationTags ());
	    }
	    System.out.println ("List of tags: " + tagSet);
	    Tag[] tagArray = tagSet.toArray (new Tag[tagSet.size()]);

	    if ( tagArray.length > 0 )  {
		    for (int i = 0; i < tagArray.length; ++i) {
			    JLabel label = new JLabel();
			    final String tagValue= tagArray[i].getValue ();
			    label.setText(tagValue);
			    JButton removeButton = new JButton("-");
			    removeButton.addActionListener(new ActionListener() {
				    public void actionPerformed(ActionEvent arg0) {
					    Tag tag = new Tag(Tag.TagType.PLACE, tagValue);
					    for( int i = 0 ; i < selectedIndexes.length ; ++ i ) {
						    photos.get(i).removeTag (tag);
					    }
				    }
			    });
			    tagPanelLocation.add(label);
			    tagPanelLocation.add(removeButton);
		    }
	    }
        locationTags.removeAll ();
        locationTags.add(tagPanelLocation);
        locationTags.updateUI();
    }

    private void updateChildrenTags() {
	    JPanel tagPanelLocation = new JPanel();
	    tagPanelLocation.setLayout(new FlowLayout());

	    Set<Tag> tagSet = new HashSet<> ();
	    final Integer[] selectedIndexes = photosPanel.getSelectedIndexesAsArray ();
	    for ( int i = 0 ; i < selectedIndexes.length; ++ i ) {
		    tagSet.addAll (photos.get (i).getChildTags ());
	    }
	    System.out.println ("List of tags: " + tagSet);
	    Tag[] tagArray = tagSet.toArray (new Tag[tagSet.size()]);

	    if ( tagArray.length > 0 )  {
		    for (int i = 0; i < tagArray.length; ++i) {
			    JLabel label = new JLabel();
			    final String tagValue= tagArray[i].getValue ();
			    label.setText(tagValue);
			    JButton removeButton = new JButton("-");
			    removeButton.addActionListener(new ActionListener() {
				    public void actionPerformed(ActionEvent arg0) {
					    Tag tag = new Tag(Tag.TagType.KID, tagValue);
					    for( int i = 0 ; i < selectedIndexes.length ; ++ i ) {
						    photos.get(i).removeTag (tag);
					    }
				    }
			    });
			    tagPanelLocation.add(label);
			    tagPanelLocation.add(removeButton);
		    }
	    }
	    childrenTags.removeAll ();
	    childrenTags.add(tagPanelLocation);
	    childrenTags.updateUI();
    }
}