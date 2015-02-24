package synergy.Reformat;

import synergy.Utilities.DisplayPhoto;
import synergy.Utilities.StaticObjects;
import synergy.models.Photo;

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
        Photo photo = StaticObjects.LIST_OF_PHOTOS.get(i);
        mainImage.setIcon(new ImageIcon(photo.getPath()));
    }


}
