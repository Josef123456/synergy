package synergy.Utilities;

import synergy.Views.ThumbnailPanel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Cham on 19/02/2015.
 */
public class SetActionListeners {
    ThumbnailPanel thumbnailPanel;
    public SetActionListeners(ThumbnailPanel thumbnailPanel){
        this.thumbnailPanel = thumbnailPanel;
    }

    public void setThumbnailPanelListener(JLabel label, int i){
        final int index = i;
        label.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent arg0) {
                thumbnailPanel.getMainPhotoPanel().setMainImage(index);
            }
        });
    }
}
