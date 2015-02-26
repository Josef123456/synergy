package synergy.Views;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Cham on 2015/02/26.
 */
public class ThumbnailView extends JPanel {
    PhotosPanel photosPanel;
    private PhotoGridPanel mainPanel;

    public ThumbnailView(PhotosPanel photosPanel){
        this.photosPanel = photosPanel;
        setUpUI();

    }
    public void setUpUI(){
        mainPanel = new PhotoGridPanel(photosPanel);

        add(mainPanel.getScrollPane(480, 120), BorderLayout.CENTER);
    }

    public void setMainImageToPanel(int width, int height) {
        mainPanel.setImages(width, height);
    }

    public PhotoGridPanel getMainPanel(){
        return mainPanel;
    }
}
