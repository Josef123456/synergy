package synergy.Views;

import net.sf.nachocalendar.components.DatePanel;
import net.sf.nachocalendar.components.DayPanel;
import net.sf.nachocalendar.components.MonthPanel;
import net.sf.nachocalendar.components.MonthScroller;
import net.sf.nachocalendar.components.YearScroller;
import net.sf.nachocalendar.model.DateSelectionModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.UIManager;

public class CalendarPanel extends JPanel {

    private JPanel photoMainPanel, southPanel;

    public CalendarPanel() {
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
        calendarConstruct ();
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

    private void calendarConstruct () {
        photoMainPanel.removeAll();
        DatePanel datePanel = new DatePanel();
        datePanel.setSelectionMode(DateSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        datePanel.setAutoscrolls(true);

        MonthScroller monthScroller = (MonthScroller) ((JPanel) datePanel.getComponent(0))
                .getComponents()[0];
        for (Component component : monthScroller.getComponents()) {
            setComponentFont(component);
        }

        YearScroller yearScroller = (YearScroller) ((JPanel) datePanel.getComponent(0))
                .getComponents()[1];
        for (Component component : yearScroller.getComponents()) {
            setComponentFont(component);
        }

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
            photoMainPanel.add(datePanel);
        }
    }

    public void setComponentFont(Component component) {
        component.setFont(new Font("Tahoma", Font.PLAIN, 15));
    }

    private void bottomConstruct() {}
}
