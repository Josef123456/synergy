package synergy.Reformat;

import synergy.Utilities.SetImages;
import synergy.Utilities.StaticObjects;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Cham on 19/02/2015.
 */
class ThumbnailPanel extends JPanel {
    MainPhotoPanel mainPhotoPanel;

    public ThumbnailPanel(MainPhotoPanel mainPhotoPanel){
        this.mainPhotoPanel = mainPhotoPanel;
        setUpUI();
        setUpImages();
    }

    public void setUpUI(){

        setLayout(new GridLayout(0, 1));
    }

    public void setUpImages(){
        SetImages setImages = new SetImages(this);
        SetActionListeners actionListeners = new SetActionListeners(this);
        for(int i = 0; i < StaticObjects.LIST_OF_IMAGE_FILES.size();i++){
            final JLabel pic = new JLabel();
            pic.setPreferredSize(new Dimension(200, 200));
            setImages.setImagesToPanel(pic, i, 200, 200);
            actionListeners.setThumbnailPanelListener(pic, i);
        }
        updateUI();
    }

    public MainPhotoPanel getMainPhotoPanel(){
        return mainPhotoPanel;
    }
}
