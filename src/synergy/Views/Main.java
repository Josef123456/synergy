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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 * Created by Josef on 02/02/2015.
 */
public class Main extends JFrame {

    private JPanel mainPanel, northernPanel, buttonsNorth, cardPanel;
    private JButton importButton, exportButton, calendarButton, albumButton, allPhotoButton;
    private JTextField searchField;

    private PhotosPanel photosPanel;
    private TagPanel tagPanel;

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
        cardPanel.add(new CalendarPanel(), "CALENDAR");


        photosPanel = new PhotosPanel();
        tagPanel = photosPanel.getTagPanel();
        cardPanel.add(photosPanel, "PHOTOS");

        //northern panels
        northernPanel = new JPanel(new GridLayout(2, 0));
        buttonsNorth = new JPanel();
        buttonsNorth.setLayout(new BoxLayout(buttonsNorth, BoxLayout.LINE_AXIS));
    }

    private void northernConstruct() {

        //search part
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

        allPhotoButton = new JButton("All Photos");
        calendarButton = new JButton("Calendar");
        albumButton = new JButton("Album");
        importButton = new JButton("Import");
        exportButton = new JButton("Export");

        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);

        importButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                int returnValue = fileChooser.showOpenDialog(Main.this);
                System.out.println(returnValue);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File[] file = fileChooser.getSelectedFiles();
                    for (int i = 0; i < file.length; i++) {
                        PhotosPanel.listOfImageFiles.add(file[i]);
                    }
                }
                System.out.println(PhotosPanel.listOfImageFiles);
                tagPanel.initiateListOfMetaDataValues();
                System.out.println(tagPanel.listOfMetaData);
                photosPanel.setImportedImages();

            }
        });

        calendarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
                cardLayout.show(cardPanel, "CALENDAR");
            }
        });

        allPhotoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
                cardLayout.show(cardPanel, "PHOTOS");
            }
        });

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
        northernPanel.add(buttonsNorth);
        buttonsNorth.add(Box.createHorizontalStrut(7));

        for (Component jButton : buttonsNorth.getComponents()) {
            setComponentFont(jButton);
        }

    }

    public void setComponentFont(Component component) {
        component.setFont(new Font("Tahoma", Font.PLAIN, 15));
    }

    public static void main(String[] args) throws IOException {
        new Main();
    }
}
