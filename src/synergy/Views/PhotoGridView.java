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
    private PhotosPanel photosPanel;
    private JPanel topPanel;

    public PhotoGridView(PhotosPanel photosPanel){
        mainPanel = new PhotoGridPanel(photosPanel);
        this.photosPanel = photosPanel;
        setUpUI();
        setVisible(true);
    }

    public void setUpUI(){
        setTopPanel();
        setMainPanelUI();

    }

    public void setTopPanel(){
        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());


        JButton zoomIn = new JButton("Zoom in");
        JButton zoomOut = new JButton("Zoom out");

        zoomIn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                int width = mainPanel.getWidth();
                mainPanel.setLayout(new GridLayout(0, width/200));
                setGridImageSize(200, 200);
            }
        });

        zoomOut.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                int width = mainPanel.getWidth();
                System.out.println(width);
                mainPanel.setLayout(new GridLayout(0, width/50));
                setGridImageSize(50, 50);
            }
        });
        topPanel.add(zoomIn);
        topPanel.add(zoomOut);
        add(topPanel, BorderLayout.NORTH);
    }

    public void setMainPanelUI(){
        int width = this.getWidth()/3;
        mainPanel.setImages(width, width);
        add(mainPanel.getScrollPane(720, 480), BorderLayout.CENTER);
    }

    public void setImageToPanel(int width, int height){
        mainPanel.setImages(width, height);
    }

    public JPanel getMainPanel(){
        return mainPanel;
    }

    public void setGridImageSize(int row, int column){
        Component[] labelArray =  mainPanel.getComponents();
        for(int i = 0; i < labelArray.length; i++){
            JLabel label = (JLabel) labelArray[i];
            Image image = iconToImage(label.getIcon());
            label.setIcon(new ImageIcon(getScaledImage(image, row, column)));
            labelArray[i] = label;
        }
    }

    private Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics g = resizedImg.getGraphics();
        g.drawImage(srcImg, 0, 0, w, h, null);
        g.dispose();
        return resizedImg;
    }

    private Image iconToImage(Icon icon) {
        if (icon instanceof ImageIcon) {
            return ((ImageIcon)icon).getImage();
        }
        else {
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
