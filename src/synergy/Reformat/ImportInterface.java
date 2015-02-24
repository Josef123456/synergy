package synergy.Reformat;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Josef on 03/02/2015.
 */
class ImportInterface extends JFrame {

    private JPanel mainPanel, leftbarPanel, rightPhotoPanel, southPanel, zoom, buttonsSouth;
    private JButton cancelButton, importButton, calendarButton, albumButton, allPhotoButton;
    private JSlider zoomSlider;
    private JLabel sliderLabel;
    private static final int zoomMin = 0;
    private static final int zoomMax = 30;
    private static final int zoomInit = 15;
    private FileFilter filter;
    private JFileChooser chooser;
    private JFrame importJFrame;


    public ImportInterface() {
        super("Import");
        setSize(600, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setBackground(new Color(1.0f,1.0f,1.0f,0.5f));
        setVisible(true);
        buildinterface();
    }

    private void buildinterface() {
        panelConstruct();
        bottomConstruct ();
        mainPanel.add(BorderLayout.SOUTH, southPanel);
        add(mainPanel);
    }

    private void panelConstruct() {
        mainPanel = new JPanel(new BorderLayout());
        leftbarPanel = new JPanel();
        rightPhotoPanel = new JPanel();
        //southern panels
        southPanel = new JPanel(new GridLayout());
        zoom = new JPanel(new GridLayout(2,0));
        buttonsSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    }

    private void bottomConstruct() {

        zoomSlider = new JSlider(JSlider.HORIZONTAL, zoomMin, zoomMax, zoomInit);
        sliderLabel = new JLabel("Zoom", JLabel.CENTER);
        sliderLabel.setForeground(Color.WHITE);
        sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        zoom.add(zoomSlider);
        zoom.add(sliderLabel);

        cancelButton = new JButton("Cancel");
        importButton = new JButton("Import");

        buttonsSouth.add(cancelButton);
        buttonsSouth.add(importButton);
        southPanel.add(zoom);
        southPanel.add(buttonsSouth);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        });

        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filter = new FileNameExtensionFilter("Bilder",
                        "gif", "png", "jpg");
                chooser = new JFileChooser("c:/programmierung/beispieldateien");
                chooser.addChoosableFileFilter(filter);
                importJFrame = new JFrame("Dateiauswahl");
                importJFrame.setSize(450,300);
                importJFrame.getContentPane().add(chooser);
                importJFrame.setLocationRelativeTo(null);
                importJFrame.setVisible(true);
            }
        });


    }
}