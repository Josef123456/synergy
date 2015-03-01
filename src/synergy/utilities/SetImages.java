package synergy.utilities;

import synergy.models.Photo;

import javax.swing.*;

/**
 * Created by Cham on 19/02/2015.
 */
public class SetImages {
    JPanel panel;
    Photo photo;

    public SetImages(JPanel panel){
        this.panel = panel;
    }

    public void setImagesToPanel(JLabel pic, int index, int width, int height){
        photo = new Photo(StaticObjects.LIST_OF_PHOTOS.get(index).toString());
        ImageIcon icon = new ImageIcon(photo.getPath ());
        pic.setIcon(icon);
        panel.add(pic);
    }

    public Photo getPhoto(){
        return photo;
    }

}
