package synergy.Views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;


public class PhotosPanel extends JPanel {
    JFileChooser fileChooser;
    JPanel mainPanel;
    JPanel mainImagePanel;
    JPanel mainThumbnailPanel;
    JLabel mainImage;

    JPanel mainGridPanel;
    JPanel gridPanel;

    public static ArrayList<File> listOfImageFiles;
    TagPanel tagPanel;

    boolean isMainView;

    public PhotosPanel() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        listOfImageFiles = new ArrayList<File>();

        mainImage = new JLabel();
        mainImage.setPreferredSize(new Dimension(800, 600));

        isMainView = true;


        setUpJMenuBar();
        setUpMainFrame();
        setMainImagePanel(null);


        tagPanel = new TagPanel(listOfImageFiles);
        add(tagPanel, BorderLayout.EAST);

        System.out.println(PhotosPanel.listOfImageFiles);
        tagPanel.initiateListOfMetaDataValues();
        System.out.println(tagPanel.listOfMetaData);
        setImportedImages();

        setSize(500, 300);
        setVisible(true);
    }

    public void setUpJMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu viewMenu = new JMenu("View");
        JMenu fileMenu = new JMenu("File");

        JMenuItem switchToMainView = new JMenuItem("Switch to Main View");
        switchToMainView.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isMainView == false) {
                    mainPanel.setVisible(true);
                    mainGridPanel.setVisible(false);
                    isMainView = true;
                }

            }
        });

        JMenuItem switchToGridView = new JMenuItem("Switch to Grid View");
        switchToGridView.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isMainView == true) {
                    setGridImages();
                    mainGridPanel.setVisible(true);
                    mainPanel.setVisible(false);
                    isMainView = false;
                }
            }
        });


        viewMenu.add(switchToMainView);
        viewMenu.add(switchToGridView);
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);

        add(menuBar, BorderLayout.NORTH);

    }

    public void setUpMainFrame() {
        mainPanel = new JPanel();
        mainImagePanel = new JPanel();
        mainImagePanel.add(mainImage);


        mainThumbnailPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(mainThumbnailPanel);
        scrollPane.setPreferredSize(new Dimension(200, 600));
        mainThumbnailPanel.setLayout(new GridLayout(2, 1));


        mainPanel.add(scrollPane, BorderLayout.WEST);
        mainPanel.add(mainImagePanel, BorderLayout.CENTER);


        add(mainPanel, BorderLayout.CENTER);

    }

    public void setImportedImages() {
        mainThumbnailPanel.removeAll();
        mainThumbnailPanel.setLayout(new GridLayout(0, 1));
        setImagesToPanel(mainThumbnailPanel, 200, 200);
        setImagesToPanel(gridPanel, 300, 300);
        if (listOfImageFiles.size() > 0) {
            setMainImagePanel(listOfImageFiles.get(listOfImageFiles.size() - 1).toString());
        }
        this.validate();
        this.repaint();
    }

    public void setMainImagePanel(String fileName) {
        if (fileName == null) {
            mainImage.setText("Please import files");
        } else {
            ImageIcon pic1Icon = new ImageIcon(fileName);
            Image pic1img = pic1Icon.getImage();
            Image newimg = pic1img.getScaledInstance(mainImage.getWidth(), mainImage.getHeight(),
                    java.awt.Image.SCALE_SMOOTH);
            pic1Icon = new ImageIcon(newimg);
            mainImage.setIcon(pic1Icon);
        }
    }


    public void setGridImages() {
        mainGridPanel = new JPanel();
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(0, 3, 20, 20));
        setImagesToPanel(gridPanel, 300, 300);
        JScrollPane gridPanelPane = new JScrollPane(gridPanel);
        gridPanelPane.setPreferredSize(new Dimension(1000, 600));
        mainGridPanel.add(gridPanelPane, BorderLayout.CENTER);
        add(mainGridPanel, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();

    }


    public void setImagesToPanel(JPanel panel, int imageWidth, int imageHeight) {
        for (int i = 0; i < listOfImageFiles.size(); i++) {
            JLabel pic = new JLabel();
            pic.setSize(imageWidth, imageHeight);
            ImageIcon picIcon = new ImageIcon(listOfImageFiles.get(i).toString());
            Image picimg = picIcon.getImage();
            Image newimg = picimg.getScaledInstance(pic.getWidth(), pic.getHeight(), java.awt
                    .Image.SCALE_SMOOTH);
            picIcon = new ImageIcon(newimg);
            pic.setIcon(picIcon);

            final int index = i;

            pic.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent arg0) {
                    // TODO Auto-generated method stub
                    setMainImagePanel(listOfImageFiles.get(index).toString());
                    tagPanel.setIndex(index);
                    if (arg0.getClickCount() == 2 && isMainView == false) {
                        mainPanel.setVisible(true);
                        mainGridPanel.setVisible(false);
                        isMainView = true;
                    }
                }

            });
            panel.add(pic);
        }

    }
}




