package synergy.Utilities;

import synergy.models.Photo;

import javax.swing.*;
import java.io.File;

/**
 * Created by Cham on 19/02/2015.
 */
public class SetImages {
    JPanel panel;

    public SetImages(JPanel panel){
        this.panel = panel;
    }

    public void setImagesToPanel(JLabel pic, int index, int width, int height){
        Photo photo = new Photo(StaticObjects.LIST_OF_IMAGE_FILES.get(index).toString());
        DisplayPhoto image = new DisplayPhoto(photo.getPath());
        ImageIcon icon = new ImageIcon(image.getBufferedImage(width, height));
        pic.setIcon(icon);
        panel.add(pic);
    }

}
