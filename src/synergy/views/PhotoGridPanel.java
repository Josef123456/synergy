package synergy.views;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Cham on 25/02/2015.
 */
public class PhotoGridPanel extends JPanel {
    PhotosPanel photosPanel;

    public PhotoGridPanel(PhotosPanel photosPanel){
        this.photosPanel = photosPanel;
    }

    public void setGridLayout(int row, int column){
        setLayout(new GridLayout(row, column));
    }

    public void setImages(int width, int height){
        photosPanel.setImagesToPanel(this, width, height);
    }

    public JScrollPane getScrollPane(int width, int height){
        JScrollPane scrollPane = new JScrollPane(this);
        scrollPane.setPreferredSize(new Dimension(width, height));
        return scrollPane;
    }


}
