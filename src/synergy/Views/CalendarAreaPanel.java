package synergy.Views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import synergy.models.Photo;
import synergy.nachocalendar.components.DatePanel;
import synergy.nachocalendar.components.DayPanel;
import synergy.nachocalendar.components.MonthPanel;
import synergy.nachocalendar.model.DateSelectionModel;

public class CalendarAreaPanel extends JPanel {

    private JPanel photoMainPanel, southPanel;
    DatePanel datePanel;

    public CalendarAreaPanel() {
        this.setLayout(new BorderLayout());
        setSize(700, 600);
        setMinimumSize(new Dimension(600, 500));
        buildInterface();
        setVisible(true);
    }

    private void buildInterface() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        panelConstruct();
        calendarContruct();
        bottomConstruct();

        this.add(BorderLayout.CENTER, photoMainPanel);
        this.add(BorderLayout.SOUTH, southPanel);
    }

    private void panelConstruct() {

        photoMainPanel = new JPanel(new BorderLayout());
        southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        photoMainPanel.setBackground(Color.decode("#001E28"));
        southPanel.setBackground(Color.decode("#001E28"));
    }

    private void calendarContruct() {

        datePanel = new DatePanel();
        datePanel.setSelectionMode(DateSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        datePanel.setAutoscrolls(true);
        datePanel.setAntiAliased(true);

        final CalendarAreaPanel thisPanel = this;

        datePanel.getYearScroller().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                thisPanel.repaint();
            }

        });

        datePanel.getMonthScroller().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                thisPanel.repaint();
            }
        });

        datePanel.setBorder(BorderFactory.createLineBorder(Color.decode("#001E28"), 5));
        photoMainPanel.add(datePanel);
    }

    public void markPhotoDates() {
        MonthPanel monthPanel = datePanel.getMonthPanel();
        Component[] monthPanelComponents = ((JPanel) monthPanel.getComponent(0)).getComponents();
        ArrayList<Date> uniquePhotoDates = Photo.getUniqueDates();

        for (int i = 7; i < monthPanelComponents.length; i++) {
            DayPanel dayPanel = (DayPanel) monthPanelComponents[i];

            for (Date date : uniquePhotoDates) {
                if (setTimeToMidnight(date).equals(setTimeToMidnight(dayPanel.getDate()))) {
                    dayPanel.setHasPhotos(true);
                }
            }
        }
    }

    @Override
    public void repaint() {
        super.repaint();
        if (datePanel != null) {
            markPhotoDates();
        }
    }

    public Date setTimeToMidnight(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    private void bottomConstruct() {
        JButton buttonGetPhotos = new JButton("Get Photos");
        southPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        buttonGetPhotos.setFont(new Font("Tahoma", Font.PLAIN, 15));
        buttonGetPhotos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        southPanel.add(buttonGetPhotos);
    }
}
