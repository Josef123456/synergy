package synergy.Views;


import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import synergy.models.Photo;

/**
 * Created by Josef on 02/02/2015.
 */
public class Main extends JFrame {

    private JPanel mainPanel, northernPanel, buttonsNorth, cardPanel;
    private JButton importButton, exportButton, calendarButton, albumButton, allPhotoButton,
            gridPhotoButton;
    private JTextField searchField;

    private PhotosPanel photosPanel;

    public Main() throws IOException {
        super("InstaTag");
        setSize(700, 600);
        setMinimumSize(new Dimension(600, 500));
        setLocationRelativeTo(null);
        buildInterface();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void buildInterface() throws IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        panelConstruct();
        northernConstruct();
        mainPanel.add(BorderLayout.NORTH, northernPanel);
        mainPanel.add(BorderLayout.CENTER, cardPanel);
        northernPanel.setBackground(Color.decode("#001E28"));
        buttonsNorth.setBackground(Color.decode("#001E28"));
        add(mainPanel);
    }

    private void panelConstruct() {
        //main panel for all panels
        mainPanel = new JPanel(new BorderLayout());
        cardPanel = new JPanel(new CardLayout());
        cardPanel.add(new CalendarAreaPanel(), "CALENDAR");

        photosPanel = new PhotosPanel();
        cardPanel.add(photosPanel, "PHOTOS");
        //northern panels
        northernPanel = new JPanel(new GridLayout(2, 0));
        buttonsNorth = new JPanel();
        buttonsNorth.setLayout(new BoxLayout(buttonsNorth, BoxLayout.LINE_AXIS));
    }

    private void createButtons() {
        allPhotoButton = new JButton("All Photos");
        gridPhotoButton = new JButton("Grid Photos");
        calendarButton = new JButton("Calendar");
        albumButton = new JButton("Album");
        importButton = new JButton("Import");
        exportButton = new JButton("Export");
    }

    private void addActionListenerToImportButton() {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        System.out.println(Photo.getAllPhotos().size());
        photosPanel.setPhotos(Photo.getAllPhotos());

        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {

                int returnValue = fileChooser.showOpenDialog(Main.this);
                long t1 = System.currentTimeMillis();

                System.out.println(returnValue);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File[] file = fileChooser.getSelectedFiles();
                    for (int i = 0; i < file.length; i++) {
                        Photo photo = new Photo(file[i].toString());
                        photo.save();
                    }
                    photosPanel.setPhotos(Photo.getAllPhotos());
                    System.out.println("Number of files imported: " + Photo.getAllPhotos().size());
                }
                long t2 = System.currentTimeMillis();
                System.out.println(t2 - t1 + " milliseconds");
            }
        });
    }

    private void addActionListenerToCalendarButton() {
        calendarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
                cardLayout.show(cardPanel, "CALENDAR");
            }
        });
    }

    private void addActionListenerToAllPhotoButton() {
        allPhotoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
	            List<Photo> allPhotos = Photo.getAllPhotos();
	            if ( !photosPanel.getPhotos ().equals (allPhotos) ) {
		            photosPanel.setPhotos (allPhotos);
	            }
                CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
                cardLayout.show (cardPanel, "PHOTOS");
                photosPanel.setIsMainView(true);
            }
        });
    }

    private void addActionListenerToGridPhotoButton() {
        gridPhotoButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
                cardLayout.show(cardPanel, "PHOTOS");
                photosPanel.setNoSelection();
                photosPanel.setIsMainView(false);
            }
        });
    }

    private void addActionListeners() {
        addActionListenerToImportButton();
        addActionListenerToAllPhotoButton();
        addActionListenerToCalendarButton();
        addActionListenerToGridPhotoButton();
    }

    private void addButtonsToLayout() {
        buttonsNorth.add(Box.createHorizontalStrut(7));
        buttonsNorth.add(importButton);
        buttonsNorth.add(Box.createHorizontalStrut(5));
        buttonsNorth.add(exportButton);
        buttonsNorth.add(Box.createHorizontalGlue());
        buttonsNorth.add(albumButton);
        buttonsNorth.add(Box.createHorizontalStrut(5));
        buttonsNorth.add(calendarButton);
        buttonsNorth.add(Box.createHorizontalStrut(5));
        buttonsNorth.add(allPhotoButton);
        buttonsNorth.add(Box.createHorizontalStrut(5));
        buttonsNorth.add(gridPhotoButton);
        buttonsNorth.add(Box.createHorizontalStrut(5));
        northernPanel.add(buttonsNorth);
        buttonsNorth.add(Box.createHorizontalStrut(7));
        for (Component jButton : buttonsNorth.getComponents()) {
            setComponentFont(jButton);
        }
    }

    private void addSearchField() {
        searchField = new JTextField("Search", 10);
        searchField.setBackground(new Color(204, 204, 204));
        searchField.setFont(new Font("Tahoma", 2, 24));
        northernPanel.add(searchField);
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                searchField.setText("Search");
            }

            @Override
            public void focusGained(FocusEvent e) {
                searchField.setText("");
            }
        });
    }

    private void northernConstruct() {
        addSearchField();
        createButtons();
        addActionListeners();
        addButtonsToLayout();
    }

    private void setComponentFont(Component component) {
        component.setFont(new Font("Tahoma", Font.PLAIN, 15));
    }

    public static void main(String[] args) throws IOException {
        new Main();
    }
}
