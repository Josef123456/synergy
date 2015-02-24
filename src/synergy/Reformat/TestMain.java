package synergy.Reformat;

import synergy.Utilities.StaticObjects;
import synergy.models.Photo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Cham on 19/02/2015.
 */
class TestMain extends JFrame {

    MainPhotoPanel mainPhotoPanel;
    ThumbnailPanel thumbnailPanel;
    TagPanelView tagPanelView;
    GridViewPanel gridViewPanel;

    JPanel cardViews;
    CardLayout cardLayout;

    final static String GRID_VIEW = "GRID";
    final static String MAIN_VIEW = "MAIN VIEW";

    public TestMain(){

        mainPhotoPanel = new MainPhotoPanel();
        thumbnailPanel = new ThumbnailPanel(mainPhotoPanel);
        tagPanelView = new TagPanelView();
        gridViewPanel = new GridViewPanel();

        cardViews = new JPanel();




        setUpJMenu();
        setUpUI();

    }

    public void setUpUI(){
        JScrollPane scrollPane = new JScrollPane(thumbnailPanel);
        scrollPane.setPreferredSize(new Dimension(200, 600));

        add(scrollPane, BorderLayout.WEST);

        JScrollPane gridScrollPane = new JScrollPane(gridViewPanel);
        gridScrollPane.setPreferredSize(new Dimension(800, 600));

        cardViews.setLayout(new CardLayout());
        cardViews.add(gridScrollPane, GRID_VIEW);
        cardViews.add(mainPhotoPanel, MAIN_VIEW);

        cardLayout = (CardLayout) cardViews.getLayout();

        add(cardViews, BorderLayout.CENTER);

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
                        StaticObjects.LIST_OF_PHOTOS.add(new Photo (file[i].getPath()));
                    }
                    initiateListOfMetaDataValues();
                    mainPhotoPanel.setMainImage(0);
                    mainPhotoPanel.updateUI();

                    thumbnailPanel.setUpImages();
                    gridViewPanel.setUpImages();

                }


            }

        });

        JMenuItem viewGridViewMenu = new JMenuItem("Switch to grid view");

        viewGridViewMenu.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
               cardLayout.next(cardViews);


            }
        });

        JMenuItem viewMainViewMenu = new JMenuItem("Switch to main view");
        viewMainViewMenu.addActionListener(new ActionListener(){


            public void actionPerformed(ActionEvent e) {
               cardLayout.next(cardViews);


            }
        });

        viewMenu.add(viewMainViewMenu);
        viewMenu.add(viewGridViewMenu);


        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        add(menuBar, BorderLayout.NORTH);
    }

    public void initiateListOfMetaDataValues() {
        int metaDataSize = StaticObjects.LIST_OF_METADATA.size();
        for (int i = 0; i < (StaticObjects.LIST_OF_PHOTOS.size() - metaDataSize); i++) {
            StaticObjects.LIST_OF_METADATA.add(new ArrayList<String>());
            StaticObjects.LIST_OF_SELECTED_INDEX.add(0);
        }
    }

    public static void main(String args[]){
        new TestMain();
    }
}
