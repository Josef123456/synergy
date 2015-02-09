package synergy.Views;


import net.sf.nachocalendar.components.DatePanel;
import net.sf.nachocalendar.components.DayPanel;
import net.sf.nachocalendar.components.MonthPanel;
import net.sf.nachocalendar.model.DateSelectionModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

/**
 * Created by Josef on 02/02/2015.
 */
public class Main extends JFrame {

    private JPanel mainPanel, photoMainPanel, southPanel, northernPanel, buttonsNorth, zoom,
            buttonsSouth;
    private JButton importButton, exportButton, calendarButton, albumButton, allPhotoButton;
    private JSlider zoomSlider;
    private JTextField searchField;
    private JLabel sliderLabel;
    static final int zoomMin = 0;
    static final int zoomMax = 30;
    static final int zoomInit = 15;

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

        panelConstruct();

        northernConstruct();
        calendarContruct();
        bottomConstruct();

        mainPanel.add(BorderLayout.NORTH, northernPanel);
        mainPanel.add(BorderLayout.CENTER, photoMainPanel);
        mainPanel.add(BorderLayout.SOUTH, southPanel);

        northernPanel.setBackground(Color.decode("#001E28"));
        photoMainPanel.setBackground(Color.decode("#001E28"));
        buttonsNorth.setBackground(Color.decode("#001E28"));
        buttonsSouth.setBackground(Color.decode("#001E28"));
        zoom.setBackground(Color.decode("#001E28"));


        add(mainPanel);
    }


    private void panelConstruct() {

        //main panel for all panels
        mainPanel = new JPanel(new BorderLayout());

        //northern panels
        northernPanel = new JPanel(new GridLayout(2, 0));
        buttonsNorth = new JPanel(new FlowLayout(FlowLayout.TRAILING));

        //center panel for photos
        photoMainPanel = new JPanel(new BorderLayout());

        //southern panels
        southPanel = new JPanel(new GridLayout());
        zoom = new JPanel(new GridLayout(2, 0));
        buttonsSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    }

    /*
    This method is for constructing the menu bar. It inlcudes the import and export button
     */
    private void northernConstruct() {

        //search part
        searchField = new JTextField("Search", 10);
        searchField.setBackground(new Color(204, 204, 204));
        searchField.setFont(new Font("Times New Roman", 2, 24));
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

        //photo button according to Codrin's instructions.
        allPhotoButton = new JButton("All Photos");
        calendarButton = new JButton("Calendar");
        albumButton = new JButton("Album");

        buttonsNorth.add(albumButton);
        buttonsNorth.add(calendarButton);
        buttonsNorth.add(allPhotoButton);
        northernPanel.add(buttonsNorth);

    }

    /*
    This method inlcudes the search bar and will show the photos.
    For you to implement Codrin
     */
    private void calendarContruct() {
        photoMainPanel.removeAll();
        DatePanel datePanel = new DatePanel();
        datePanel.setSelectionMode(DateSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        datePanel.setAutoscrolls(true);

        MonthPanel monthPanel = (MonthPanel) datePanel.getComponent(1);
        Component[] monthPanelComponents = ((JPanel) monthPanel.getComponent(0)).getComponents();

        for (int i = 7; i < monthPanelComponents.length; i++) {
            DayPanel dayPanel = (DayPanel) monthPanelComponents[i];
            final MouseListener mouseListener = dayPanel.getMouseListeners()[0];
            dayPanel.removeMouseListener(dayPanel.getMouseListeners()[0]);
            dayPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    mouseListener.mouseClicked(e);
                }
            });
        }
        photoMainPanel.add(datePanel);
    }


    private void bottomConstruct() {

        zoomSlider = new JSlider(JSlider.HORIZONTAL, zoomMin, zoomMax, zoomInit);
        sliderLabel = new JLabel("Zoom", JLabel.CENTER);
        sliderLabel.setForeground(Color.WHITE);
        sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        zoom.add(zoomSlider);
        zoom.add(sliderLabel);

        importButton = new JButton("Import");
        exportButton = new JButton("Export");

        buttonsSouth.add(importButton);
        buttonsSouth.add(exportButton);
        southPanel.add(zoom);
        southPanel.add(buttonsSouth);

        importButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ImportInterface();
            }
        });

    }

    public static void main(String[] args) throws IOException {
        new Main();
    }
}
