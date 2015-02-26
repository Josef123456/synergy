package synergy.Views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * Created by Cham on 25/02/2015.
 */
public class PhotoGridView extends JPanel {
    private PhotoGridPanel mainPanel;
    private PhotoGridPanel zoomedOutPanel;
    private PhotosPanel photosPanel;
    private JPanel topPanel;

    private JPanel cards;

    public PhotoGridView(PhotosPanel photosPanel) {
        this.photosPanel = photosPanel;
        setLayout(new BorderLayout());
        setUpUI();
    }

    public void setUpUI() {
        setTopPanel();
        setMainPanelUI();
    }
    //@TODO: Fix the UI for the top panel(it should be at the top not at the side)
    public void setTopPanel() {
        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        JButton zoomIn = new JButton("Zoom in");
        JButton zoomOut = new JButton("Zoom out");

        zoomIn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout c1 = (CardLayout) cards.getLayout();
                c1.show(cards, "MAIN");
            }
        });

        zoomOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setGridImageSize(50, 50);
                CardLayout c1 = (CardLayout) cards.getLayout();
                c1.show(cards, "ZOOMED OUT");
            }
        });
        topPanel.add(zoomIn);
        topPanel.add(zoomOut);

        add(topPanel, BorderLayout.NORTH);
    }

    public void setMainPanelUI() {
        cards = new JPanel();
        cards.setLayout(new CardLayout());

        mainPanel = new PhotoGridPanel(photosPanel);
        zoomedOutPanel = new PhotoGridPanel(photosPanel);

        cards.add(mainPanel.getScrollPane(720, 480), "MAIN");
        cards.add(zoomedOutPanel.getScrollPane(720, 480), "ZOOMED OUT");

        add(cards, BorderLayout.CENTER);
    }

    public void setMainImageToPanel(int width, int height) {
        mainPanel.setImages(width, height);
    }

    public PhotoGridPanel getMainPanel() {
        return mainPanel;
    }

    public PhotoGridPanel getZoomedOutPanel(){ return zoomedOutPanel; }

    public void setGridImageSize(int row, int column) {
        Component[] labelArray = mainPanel.getComponents();
        //@TODO: set this to automatically change layout based on the panel's width
        zoomedOutPanel.setLayout(new GridLayout(0, 720 / 50));
        zoomedOutPanel.removeAll();
        for (int i = 0; i < labelArray.length; i++) {
            System.out.println(labelArray[i].toString());
            JLabel label = (JLabel) labelArray[i];
            JLabel zoomedLabel = new JLabel();
            Image image = iconToImage(label.getIcon());
            zoomedLabel.setIcon(new ImageIcon(getScaledImage(image, row, column)));
            photosPanel.addMouseListenerToPictureLabel(zoomedLabel, i, image, row, column);
            zoomedOutPanel.add(zoomedLabel);
        }
    }

    private Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics g = resizedImg.getGraphics();
        g.drawImage(srcImg, 0, 0, w, h, null);
        g.dispose();
        return resizedImg;
    }

    private Image iconToImage(Icon icon) {
        if (icon instanceof ImageIcon) {
            return ((ImageIcon) icon).getImage();
        } else {
            int w = icon.getIconWidth();
            int h = icon.getIconHeight();
            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            BufferedImage image = gc.createCompatibleImage(w, h);
            Graphics2D g = image.createGraphics();
            icon.paintIcon(null, g, 0, 0);
            g.dispose();
            return image;
        }
    }
}
