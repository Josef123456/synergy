package synergy.Reformat;

import synergy.Reformat.GridViewPanel;
import synergy.Reformat.TagPanelView;
import synergy.Reformat.ThumbnailPanel;
import synergy.Utilities.StaticObjects;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Cham on 19/02/2015.
 */
class SetActionListeners {
    ThumbnailPanel thumbnailPanel;
    GridViewPanel gridViewPanel;
    TagPanelView tagPanelView;

    public SetActionListeners(ThumbnailPanel thumbnailPanel){
        this.thumbnailPanel = thumbnailPanel;
    }

    public SetActionListeners(GridViewPanel gridViewPanel, TagPanelView tagPanelView){
        this.gridViewPanel = gridViewPanel;
        this.tagPanelView = tagPanelView;
    }

    public void setThumbnailPanelListener(JLabel label, int i){
        final int index = i;
        label.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent arg0) {
                thumbnailPanel.getMainPhotoPanel().setMainImage(index);
                StaticObjects.SELECTED_INDEX = index;
            }
        });
    }

    public void setGridViewPanelListener(JLabel label, int i){
        final int index = i;
        label.addMouseListener(new MouseAdapter(){
           public void mouseClicked(MouseEvent arg0){
               tagPanelView.updateLocationMainPanelTags(index);
           }
        });

    }
}
