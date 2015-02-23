package synergy.Views;

import synergy.models.Tag;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class TagPanel extends JPanel {
    ArrayList<File> listOfFiles;
    ArrayList<ArrayList<String>> listOfMetaData;
    ArrayList<Integer> listOfSelectedIndex;


    JPanel locationPanel;
    JPanel locationTags;

    PhotosPanel photosPanel;

    int index;

    public TagPanel(ArrayList<File> listOfFiles, PhotosPanel photosPanel) {
        this.listOfFiles = listOfFiles;
        listOfMetaData = new ArrayList<ArrayList<String>>();
        listOfSelectedIndex = new ArrayList<Integer>();

        this.photosPanel = photosPanel;
        setUpUI();
        setLayout(new GridLayout(2, 1));
    }

    public void setUpUI() {
        JLabel locationLabel = new JLabel("Location");


        final JComboBox<String> locationTextField = new JComboBox<String>();
        locationTextField.setEditable(true);

        JButton addButtonLocation = new JButton("+");

        addButtonLocation.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {

                Tag tag = new Tag(Tag.TagType.PLACE, (String) locationTextField.getSelectedItem());
                tag.save();
                if (photosPanel.isMainView) {
                    listOfMetaData.get(getIndex()).add((String) locationTextField.getSelectedItem());
                    Main.PHOTO_ARRAY.get(getIndex()).addTag(tag);
                    updateLocationTags();

                } else {
                    for (int i = 0; i < listOfMetaData.size(); i++) {
                        if (listOfSelectedIndex.get(i) == 1 && !listOfMetaData.get(i).contains(locationTextField.getSelectedItem())) {
                            listOfMetaData.get(i).add((String) locationTextField.getSelectedItem());
                            Main.PHOTO_ARRAY.get(i).addTag(tag);
                        }
                    }
                    updateLocationTags();

                }
                System.out.println(listOfMetaData);
            }

        });

        JLabel kidsLabel = new JLabel("Kids");
        JTextField kidsTextField = new JTextField();
        JButton addButtonKids = new JButton("+");


        locationPanel = new JPanel();
        locationPanel.setLayout(new GridLayout(3, 1));
        locationTags = new JPanel();


        JPanel locationTagQuery = new JPanel();
        locationTagQuery.setLayout(new GridLayout(1, 2));
        locationTagQuery.add(locationTextField);
        locationTagQuery.add(addButtonLocation);

        JPanel kidsPanel = new JPanel();
        kidsPanel.setLayout(new GridLayout(1, 2));
        kidsPanel.add(kidsTextField);
        kidsPanel.add(addButtonKids);

        locationPanel.add(locationLabel);
        locationPanel.add(locationTagQuery);
        locationPanel.add(locationTags);
        add(locationPanel);


        //add(kidsPanel);
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void addToSelectedIndexList(int index) {
        listOfSelectedIndex.set(index, 1);
    }

    public void removeFromSelectedIndexList(int index) {
        listOfSelectedIndex.set(index, 0);
    }

    public int getIndex() {
        return index;
    }

    public ArrayList<Integer> getArrayOfSelectedIndex() {
        return listOfSelectedIndex;
    }

    public void initiateListOfMetaDataValues() {
        int metaDataSize = listOfMetaData.size();
        for (int i = 0; i < (listOfFiles.size() - metaDataSize); i++) {
            listOfMetaData.add(new ArrayList<String>());
            listOfSelectedIndex.add(0);
        }
    }

    public void updateLocationTags() {
        JPanel tagPanelLocation = new JPanel();
        tagPanelLocation.setLayout(new FlowLayout());
        if (photosPanel.isMainView) {
            for (int i = 0; i < listOfMetaData.get(getIndex()).size(); i++) {
                final int index = i;
                JLabel label = new JLabel();
                label.setText(listOfMetaData.get(getIndex()).get(i));
                final String stringTag = label.getText();
                final JButton removeButton = new JButton("-");


                tagPanelLocation.add(label);
                tagPanelLocation.add(removeButton);

                removeButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent arg0) {
                        Tag photoTag = new Tag(Tag.TagType.PLACE, stringTag);
                        photoTag.save();
                        Main.PHOTO_ARRAY.get(getIndex()).removeTag(photoTag);
                        listOfMetaData.get(getIndex()).remove(index);
                        updateLocationTags();
                    }

                });
            }
        } else {
            ArrayList<String> listOfTags = new ArrayList<String>();
            boolean tagExists = false;
            for (int i = 0; i < listOfMetaData.size(); i++) {
                if (listOfSelectedIndex.get(i) == 1) {
                    tagExists = true;
                    for (int j = 0; j < listOfMetaData.get(i).size(); j++) {
                        listOfTags.add(listOfMetaData.get(i).get(j));
                    }
                    break;
                }
            }

            for (int i = 0; i < listOfMetaData.size(); i++) {
                for (int j = 0; j < listOfTags.size(); j++) {
                    if (listOfSelectedIndex.get(i) == 1 && !listOfMetaData.get(i).contains(listOfTags.get(j))) {
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

                    final String stringTag = listOfTags.get(i);

                    removeButton.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent arg0) {
                            Tag photoTag = new Tag(Tag.TagType.PLACE, stringTag);
                            photoTag.save();

                            for (int i = 0; i < listOfMetaData.size(); i++) {
                                for (int j = 0; j < listOfMetaData.get(i).size(); j++) {
                                    if (listOfSelectedIndex.get(i) == 1 && listOfMetaData.get(i).get(j).equals(stringTag)) {
                                        listOfMetaData.get(i).remove(j);
                                        Main.PHOTO_ARRAY.get(i).removeTag(photoTag);
                                        updateLocationTags();
                                    }
                                }
                            }
                        }

                    });

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
        }

        locationTags.removeAll();
        locationTags.add(tagPanelLocation);
        locationTags.updateUI();
    }
}