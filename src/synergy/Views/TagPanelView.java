package synergy.Views;

import synergy.Utilities.StaticObjects;
import synergy.models.Photo;
import synergy.models.PhotoTag;
import synergy.models.Tag;

import javax.swing.*;
import javax.swing.text.html.HTML;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Cham on 21/02/2015.
 */
public class TagPanelView extends JPanel {

    JPanel locationPanel;
    JPanel locationTags;

    public TagPanelView(){
        setUpUI();
    }

    public void setUpUI(){
        JLabel locationLabel = new JLabel("Location");
        final JTextField textFieldLocation = new JTextField();
        JButton addButtonLocation = new JButton("+");

        addButtonLocation.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                String textFieldContents = textFieldLocation.getSelectedText();
                StaticObjects.LIST_OF_METADATA.get(StaticObjects.SELECTED_INDEX).add(textFieldContents);
                updateLocationMainPanelTags(StaticObjects.SELECTED_INDEX);
            }
        });

        JPanel locationTagQuery = new JPanel();
        locationTagQuery.setLayout(new GridLayout(1, 2));
        locationTagQuery.add(textFieldLocation);
        locationTagQuery.add(addButtonLocation);

        locationPanel = new JPanel();
        locationPanel.setLayout(new GridLayout(3, 1));
        locationTags = new JPanel();

        locationPanel.add(locationLabel);
        locationPanel.add(locationTagQuery);
        locationPanel.add(locationTags);

        add(locationPanel);

    }

    public void updateLocationMainPanelTags(int index){
        JPanel tagPanelLocation = new JPanel();
        for (int i = 0; i < StaticObjects.LIST_OF_METADATA.get(index).size(); i++) {
            JLabel label = new JLabel();
            label.setText(StaticObjects.LIST_OF_METADATA.get(index).get(i));
            JButton removeButton = new JButton("-");

            tagPanelLocation.add(label);
            tagPanelLocation.add(removeButton);
        }
        locationTags.removeAll();
        locationTags.add(tagPanelLocation);
        locationTags.updateUI();
    }

    public void updateLocationGridPanelTags(){
        JPanel tagPanelLocation = new JPanel();
        ArrayList<String> listOfTags = new ArrayList<String>();
        boolean tagExists = false;
        for (int i = 0; i < StaticObjects.LIST_OF_METADATA.size(); i++) {
            if (StaticObjects.LIST_OF_SELECTED_INDEX.get(i) == 1) {
                tagExists = true;
                for (int j = 0; j < StaticObjects.LIST_OF_METADATA.get(i).size(); j++) {
                    listOfTags.add(StaticObjects.LIST_OF_METADATA.get(i).get(j));
                }
                break;
            }
        }

        for (int i = 0; i < StaticObjects.LIST_OF_METADATA.size(); i++) {
            for (int j = 0; j < listOfTags.size(); j++) {
                if (StaticObjects.LIST_OF_SELECTED_INDEX.get(i) == 1 && !StaticObjects.LIST_OF_METADATA.get(i).contains(listOfTags.get(j))) {
                    listOfTags.remove(j);
                }
            }
        }

        System.out.println("List of tags: " + listOfTags);
        if (tagExists && !listOfTags.isEmpty()) {
            for (int i = 0; i < listOfTags.size(); i++) {
                JLabel label = new JLabel();
                label.setText(listOfTags.get(i));
                JButton removeButton = new JButton("-");

                tagPanelLocation.add(label);
                tagPanelLocation.add(removeButton);
            }
        } else if (tagExists && listOfTags.isEmpty()) {
            JLabel label = new JLabel();
            label.setText("No common tags");
            tagPanelLocation.add(label);
        } else if (!tagExists) {
            JLabel label = new JLabel();
            label.setText("No tags");
            tagPanelLocation.add(label);
        }

        locationTags.removeAll();
        locationTags.add(tagPanelLocation);
        locationTags.updateUI();
    }

    public void addTag(Photo photo, Tag.TagType tagType, String value){
        Tag tag = new Tag(tagType, value);
        PhotoTag photoTag = new PhotoTag(photo, tag);

    }
}
