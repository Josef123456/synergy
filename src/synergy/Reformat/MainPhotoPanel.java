package synergy.Reformat;

import synergy.Utilities.DisplayPhoto;
import synergy.Utilities.StaticObjects;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Cham on 19/02/2015.
 */
class MainPhotoPanel extends JPanel {
    JLabel mainImage;

    public MainPhotoPanel(){
        mainImage = new JLabel();
        setUpUI();
        add(mainImage);

    }

    public void setUpUI(){
        mainImage.setPreferredSize(new Dimension(800, 600));
    }


    public void setMainImage(int i){
        DisplayPhoto photo = new DisplayPhoto(StaticObjects.LIST_OF_PHOTOS.get(i));
        BufferedImage image = photo.getBufferedImage(mainImage.getWidth(), mainImage.getHeight());
        mainImage.setIcon(new ImageIcon(image));
        image.flush();
    }


}
