package synergy.Reformat;

import synergy.Utilities.SetImages;
import synergy.Utilities.StaticObjects;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Cham on 20/02/2015.
 */
class GridViewPanel extends JPanel {

    public GridViewPanel(){
        setUpUI();
    }

    public void setUpUI(){
       // setPreferredSize(new Dimension(1000, 600));
        setLayout(new GridLayout(0, 3));

    }

    public void setUpImages(){
        SetImages setImages = new SetImages(this);
        for(int i = 0; i < StaticObjects.LIST_OF_IMAGE_FILES.size(); i++){
            final JLabel pic = new JLabel();
            setImages.setImagesToPanel(pic, i, 300, 300);
        }
        updateUI();
    }

}
