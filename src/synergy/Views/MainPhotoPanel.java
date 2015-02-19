package synergy.Views;

import synergy.Utilities.DisplayPhoto;
import synergy.Utilities.StaticObjects;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by Cham on 19/02/2015.
 */
public class MainPhotoPanel extends JPanel {
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
        DisplayPhoto photo = new DisplayPhoto(StaticObjects.LIST_OF_IMAGE_FILES.get(i));
        BufferedImage image = photo.getBufferedImage(mainImage.getWidth(), mainImage.getHeight());
        mainImage.setIcon(new ImageIcon(image));
        image.flush();
    }


}
