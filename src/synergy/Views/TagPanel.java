package synergy.Views;

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


public class TagPanel extends JPanel{
    ArrayList<File> listOfFiles;
    ArrayList<ArrayList<String>> listOfMetaData;
    ArrayList<Integer> listOfSelectedIndex;


    JPanel locationPanel;
    JPanel locationTags;

    int index;

    public TagPanel(ArrayList<File> listOfFiles){
        this.listOfFiles = listOfFiles;
        listOfMetaData = new ArrayList<ArrayList<String>>();
        listOfSelectedIndex = new ArrayList<Integer>();

        setUpUI();
        setLayout(new GridLayout(2,1));
    }

    public void setUpUI(){
        JLabel locationLabel = new JLabel("Location");



        final JComboBox<String> locationTextField = new JComboBox<String>();
        locationTextField.setEditable(true);

        JButton addButtonLocation = new JButton("+");

        addButtonLocation.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                listOfMetaData.get(index).add((String)locationTextField.getSelectedItem());
                updateLocationTags();
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


        add(kidsPanel);
    }

    public void setIndex(int index){
        this.index = index;
    }

    public void addToSelectedIndexList(int index){
        listOfSelectedIndex.set(index, 1);
    }

    public void removeFromSelectedIndexList(int index){
        listOfSelectedIndex.set(index, null);
    }

    public int getIndex(){
        return index;
    }

    public ArrayList<Integer> getArrayOfSelectedIndex(){
        return listOfSelectedIndex;
    }



    public void initiateListOfMetaDataValues(){
        int metaDataSize = listOfMetaData.size();
        for(int i = 0; i < (listOfFiles.size() - metaDataSize); i++){
            listOfMetaData.add(new ArrayList<String>());
            listOfSelectedIndex.add(null);
        }
    }



    public void updateLocationTags(){
        JPanel tagPanelLocation = new JPanel();
        tagPanelLocation.setLayout(new FlowLayout());
        for(int i = 0; i < listOfMetaData.get(getIndex()).size(); i++){
            JLabel label = new JLabel();
            label.setText(listOfMetaData.get(getIndex()).get(i));
            JButton removeButton = new JButton("-");

            tagPanelLocation.add(label);
            tagPanelLocation.add(removeButton);
        }
        locationTags.removeAll();
        locationTags.add(tagPanelLocation);
        locationTags.updateUI();

        this.revalidate();
        this.repaint();
    }
}
