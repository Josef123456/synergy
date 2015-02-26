package synergy.Views;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import synergy.models.Tag;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;


public class TagPanel extends JPanel {

    private JLabel room1Label = new JLabel("Room 1");
    private JLabel room2Label = new JLabel("Room 2");

    private JButton addRoom1TagButton = new JButton("+");
	private JButton addRoom2TagButton = new JButton("+");
	//private final JComboBox<String> locationTextField = new JComboBox<String>();

	private JButton addChildTagButton = new JButton("+");
    private String[] mockChildrenData = {"John", "John Jones", "John James", "John George", "Billy", "Jacob", "Ronald", "Alicia", "Jonah", "Freddie", "Daniel", "David", "Harry", "Harrison", "Isaac", "Toby", "Tom", "Jill"};
	final JComboBox childrenComboBox = new JComboBox<String>(mockChildrenData);

    private JButton editDateButton = new JButton("Edit");

	private JPanel locationPanel;
	private JPanel locationTags;

	private JPanel childrenPanel;
	private JPanel childrenTags;

    private JPanel datePanel;

    PhotosPanel photosPanel;

    public TagPanel(PhotosPanel photosPanel) {
        this.photosPanel = photosPanel;
        setUpUILocation();
        setUpUIChildren();
        setUpUIDate();
        setLayout(new GridLayout(3, 1));
        AutoComplete();

    }

	private void addActionListenerToLocationTagButton2() {
		addRoom2TagButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                final Integer[] selectedIndexes = photosPanel.getSelectedIndexesAsArray();
                System.out.println(Arrays.toString(selectedIndexes) + " location");
                Tag tag = new Tag(Tag.TagType.PLACE, (String) room2Label.getText());
                for (int i = 0; i < selectedIndexes.length; ++i) {
                    photosPanel.getPhotos().get(selectedIndexes[i]).addTag(tag);
                }
                updateLocationTags();
            }
        });
	}

    private void addActionListenerToLocationTagButton1 () {
        addRoom1TagButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                final Integer[] selectedIndexes = photosPanel.getSelectedIndexesAsArray();
                System.out.println(Arrays.toString(selectedIndexes) + " location");
                Tag tag = new Tag(Tag.TagType.PLACE, (String) room1Label.getText());
                for (int i = 0; i < selectedIndexes.length; ++i) {
                    photosPanel.getPhotos().get(selectedIndexes[i]).addTag(tag);
                }
                updateLocationTags();
            }
        });
    }

    private void addActionListenerToChildTagButton() {
		addChildTagButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				final Integer[] selectedIndexes = photosPanel.getSelectedIndexesAsArray ();
				Tag tag = new Tag(Tag.TagType.KID, (String) childrenComboBox.getSelectedItem());
				for ( int i = 0 ; i < selectedIndexes.length; ++ i ) {
					photosPanel.getPhotos ().get (selectedIndexes[ i ]).addTag (tag);
				}
				updateChildrenTags();
			}
		});
	}

    private void setUpUILocation() {
        JLabel locationLabel = new JLabel("Location");
        //locationTextField.setEditable(true);

	    addActionListenerToLocationTagButton2();
        addActionListenerToLocationTagButton1();

        locationPanel = new JPanel();
        locationPanel.setLayout(new GridLayout(3, 1));
        locationTags = new JPanel();


        JPanel locationTagQuery = new JPanel();
        locationTagQuery.setLayout(new FlowLayout());
        //locationTagQuery.add(locationTextField);
        locationTagQuery.add(room1Label);
        locationTagQuery.add(addRoom1TagButton);
        locationTagQuery.add(room2Label);
        locationTagQuery.add(addRoom2TagButton);

        locationPanel.add(locationLabel);
        locationPanel.add(locationTagQuery);
        locationPanel.add(locationTags);


        add(locationPanel);
    }

    public void setUpUIChildren(){
        JLabel childrenLabel = new JLabel("Children");

        childrenComboBox.setEditable(true);

	    addActionListenerToChildTagButton();

        childrenPanel = new JPanel();
        childrenPanel.setLayout(new GridLayout(3, 1));
        childrenTags = new JPanel();

        JPanel childrenTagQuery = new JPanel();
        childrenTagQuery.setLayout(new GridLayout(1, 2));
        childrenTagQuery.add(childrenComboBox);
        childrenTagQuery.add(addChildTagButton);

        childrenPanel.add(childrenLabel);
        childrenPanel.add(childrenTagQuery);
        childrenPanel.add(childrenTags);
        add(childrenPanel);
    }

    public void setUpUIDate(){
        JLabel dateLabel = new JLabel("Date here");

        datePanel = new JPanel();
        datePanel.setLayout(new GridLayout(1,2));

        datePanel.add(dateLabel);
        datePanel.add(editDateButton);
        add(datePanel);
        //@TODO: add panel between date and children to regularise format? add date.

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
	    System.out.println ( "Selected Indexes: " + Arrays.toString(selectedIndexes));
	    System.out.println( photosPanel.getPhotos ().get (0).getID ());
	    for ( int i = 0 ; i < selectedIndexes.length; ++ i ) {
		    tagSet.addAll (photosPanel.getPhotos ().get (selectedIndexes[i]).getLocationTags ());
	    }
	    System.out.println ("List of location tags: " + tagSet ) ;
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
						    photosPanel.getPhotos ().get(selectedIndexes[i]).removeTag (tag);
					    }
					    updateLocationTags ();
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
		    tagSet.addAll (photosPanel.getPhotos ().get (selectedIndexes[i]).getChildTags ());
	    }
	    System.out.println ("List of children tags: " + tagSet);
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
						    photosPanel.getPhotos ().get(selectedIndexes[i]).removeTag (tag);
					    }
					    updateChildrenTags ();
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
    private void AutoComplete() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                AutoCompleteSupport<String> support = AutoCompleteSupport.install(childrenComboBox, GlazedLists.eventListOf(mockChildrenData));
            }
        });
    }
}