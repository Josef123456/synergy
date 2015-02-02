package synergy;


import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Created by Josef on 02/02/2015.
 */
public class Main extends JFrame {

    private JPanel  mainPanel,searchPanel, photoMainPanel, southPanel;
    private JButton importButton, exportButton, changeViewButton;
    private JSlider zoomSlider;
    private JMenuBar menuBar;
    private JMenu instTagMenu, fileMenu, editMenu, windowMenu, helpMenu;
    //private JMenuItem ...;
    private JTextField searchField;


    public Main() {
        super("InstaTag");
        setSize(600, 500);
        setLocationRelativeTo(null);
        buildInterface();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void buildInterface() {
        panelConstruct();
        menuConstruct();
        setJMenuBar(menuBar);
        photoMainAreaContruct();
        mainPanel.add(BorderLayout.NORTH, searchPanel);


        add(mainPanel);
    }

    private void panelConstruct() {

        mainPanel = new JPanel(new BorderLayout());
        photoMainPanel = new JPanel(new BorderLayout());
        searchPanel = new JPanel(new GridLayout());
        southPanel = new JPanel(new GridLayout());
    }
    /*
    This method is for constructing the menu bar. It inlcudes the import and export button
     */
    private void menuConstruct(){

        menuBar = new JMenuBar();

        instTagMenu = new JMenu("InstaTag");
        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit");
        windowMenu = new JMenu("Window");
        helpMenu = new JMenu("Help");

        menuBar.add(instTagMenu);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(windowMenu);
        menuBar.add(helpMenu);
        menuBar.add(Box.createGlue());
        importButton = new JButton("Import");
        exportButton = new JButton("Export");

        menuBar.add(importButton);
        menuBar.add(exportButton);
    }
    /*
    This method inlcudes the search bar and will show the photos.
     */
    private void photoMainAreaContruct(){

        searchField = new JTextField("Search",10);
        searchField.setBackground(new Color(204, 204, 204));
        searchField.setFont(new Font("Times New Roman", 2, 24));
        searchPanel.add(searchField);
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
            }

            @Override
            public void focusGained(FocusEvent e) {
                searchField.setText("");
            }
        });

    }

    public static void main(String[] args) {
        Main instaTag = new Main();

    }
}
