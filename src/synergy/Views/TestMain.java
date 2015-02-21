package synergy.Views;

import synergy.Utilities.StaticObjects;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Cham on 19/02/2015.
 */
public class TestMain extends JFrame {

    MainPhotoPanel mainPhotoPanel;
    ThumbnailPanel thumbnailPanel;
    TagPanelView tagPanelView;

    public TestMain(){
        mainPhotoPanel = new MainPhotoPanel();
        thumbnailPanel = new ThumbnailPanel(mainPhotoPanel);
        tagPanelView = new TagPanelView();
        setUpJMenu();
        setUpUI();

    }

    public void setUpUI(){
        JScrollPane scrollPane = new JScrollPane(thumbnailPanel);
        scrollPane.setPreferredSize(new Dimension(200, 600));
        add(scrollPane, BorderLayout.WEST);
        add(mainPhotoPanel, BorderLayout.CENTER);
        add(tagPanelView, BorderLayout.EAST);


        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public void setUpJMenu(){
        JMenuBar menuBar = new JMenuBar();
        JMenu viewMenu = new JMenu("View");
        JMenu fileMenu = new JMenu("File");

        JMenuItem importMenuItem = new JMenuItem("Import");
        JMenuItem exportMenuItem = new JMenuItem("Export");
        fileMenu.add(importMenuItem);
        fileMenu.add(exportMenuItem);
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);

        importMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                int returnValue = fileChooser.showOpenDialog(TestMain.this);
                System.out.println(returnValue);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File[] file = fileChooser.getSelectedFiles();
                    for (int i = 0; i < file.length; i++) {
                        StaticObjects.LIST_OF_IMAGE_FILES.add(file[i]);
                    }
                }
                initiateListOfMetaDataValues();
                mainPhotoPanel.setMainImage(0);
                mainPhotoPanel.updateUI();
                thumbnailPanel.setUpImages();
            }

        });

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        add(menuBar, BorderLayout.NORTH);
    }

    public void initiateListOfMetaDataValues() {
        int metaDataSize = StaticObjects.LIST_OF_METADATA.size();
        for (int i = 0; i < (StaticObjects.LIST_OF_IMAGE_FILES.size() - metaDataSize); i++) {
            StaticObjects.LIST_OF_METADATA.add(new ArrayList<String>());
            StaticObjects.LIST_OF_SELECTED_INDEX.add(0);
        }
    }

    public static void main(String args[]){
        new TestMain();
    }
}
