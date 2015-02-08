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
    JPanel locationPanel;

    int index;

    public TagPanel(ArrayList<File> listOfFiles){
        this.listOfFiles = listOfFiles;
        listOfMetaData = new ArrayList<ArrayList<String>>();


        setUpUI();
        setLayout(new GridLayout(4,1));
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
                //updateLocationTags();
                System.out.println(listOfMetaData);
            }

        });

        JLabel kidsLabel = new JLabel("Kids");
        JTextField kidsTextField = new JTextField();
        JButton addButtonKids = new JButton("+");


        locationPanel = new JPanel();
        locationPanel.setLayout(new GridLayout(1, 2));
        locationPanel.add(locationTextField);
        locationPanel.add(addButtonLocation);

        JPanel kidsPanel = new JPanel();
        kidsPanel.setLayout(new GridLayout(1, 2));
        kidsPanel.add(kidsTextField);
        kidsPanel.add(addButtonKids);

        add(locationLabel);
        add(locationPanel);

        add(kidsLabel);
        add(kidsPanel);
    }

    public void setIndex(int index){
        this.index = index;
    }

    public int getIndex(){
        return index;
    }

    public void initiateListOfMetaDataValues(){
        int metaDataSize = listOfMetaData.size();
        for(int i = 0; i < (listOfFiles.size() - metaDataSize); i++){
            listOfMetaData.add(new ArrayList<String>());
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
        locationPanel.add(tagPanelLocation);
        this.revalidate();
        this.repaint();
    }
}
