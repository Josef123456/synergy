package synergy.views;

import controlsfx.controlsfx.control.GridCell;
import controlsfx.controlsfx.control.GridView;
import controlsfx.controlsfx.control.cell.ImageGridCell;
import controlsfx.impl.org.controlsfx.skin.GridViewSkin;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import synergy.models.Photo;

/**
 *  This is the class responsible for the creation of the grid cells
 */
public class GridCellFactory implements Callback<GridView<Image>, GridCell<Image>> {

    private PhotoGrid photoGrid;
    private static ImageGridCell lastSelectedCell = null;

    public GridCellFactory(PhotoGrid photoGrid) {
        this.photoGrid = photoGrid;
    }

    /**
     * This method creates the ImageGridCell and sets its mouse listener needed for selection
     * @param gridView
     * @return GridCell<Image>
     */
    @Override
    public GridCell<Image> call(GridView<Image> gridView) {
        final ImageGridCell newImageCell = new ImageGridCell();

        newImageCell.setOnMouseClicked(event -> {
            if (event.isShiftDown()) {
                int lastSelectedIndex = lastSelectedCell.getIndex();
                System.out.println(lastSelectedIndex);
                int newlySelectedIndex = newImageCell.getIndex();

                if (newlySelectedIndex < lastSelectedIndex) {
                    int aux = newlySelectedIndex;
                    newlySelectedIndex = lastSelectedIndex;
                    lastSelectedIndex = aux;
                }
                int iterationIndex = newlySelectedIndex;
                Image shiftSelectedImage = PhotoGrid.getDisplayedImagesList().get(iterationIndex);
                Photo shiftSelectedPhoto = PhotoGrid.getDisplayedPhotos().get(iterationIndex);

                System.out.println(newlySelectedIndex + " " + lastSelectedIndex);

                if (PhotoGrid.getSelectedImages().contains(shiftSelectedImage))
                    iterationIndex--;
                while (iterationIndex >= lastSelectedIndex) {
                    shiftSelectedPhoto = PhotoGrid.getDisplayedPhotos().get(iterationIndex);
                    PhotoGrid.getSelectedPhotos().remove(shiftSelectedPhoto);
                    PhotoGrid.getSelectedPhotos().add(shiftSelectedPhoto);

                    shiftSelectedImage = PhotoGrid.getDisplayedImagesList().get(iterationIndex);
                    PhotoGrid.getSelectedImages().remove(shiftSelectedImage);
                    PhotoGrid.getSelectedImages().add(shiftSelectedImage);
                    iterationIndex--;
                }
            } else {
                setCellSelection(newImageCell);
            }
            ((GridViewSkin) photoGrid.getSkin()).updateGridViewItems();
            lastSelectedCell = newImageCell;
            photoGrid.getTaggingArea().update();
        });
        return newImageCell;
    }

    /**
     * This method is responsible for setting the selection of the cell
     * @param imageCell
     */
    public void setCellSelection(ImageGridCell imageCell) {
        Image selectedImage = imageCell.getItem();
        int selectedImageIndex = PhotoGrid.getDisplayedImagesList().indexOf(selectedImage);
        if (imageCell.getBorder() == null) {
            PhotoGrid.getSelectedPhotos().add(PhotoGrid.getDisplayedPhotos().get
                    (selectedImageIndex));
            PhotoGrid.getSelectedImages().add(selectedImage);

            BorderStroke[] borderStrokeArray = new BorderStroke[4];
            for (int i = 0; i < 4; i++)
                borderStrokeArray[i] = new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID,
                        null, BorderStroke.THICK, new Insets(-10, -10, -10, -10));
            imageCell.setBorder(new Border(borderStrokeArray));
        } else {
            PhotoGrid.getSelectedPhotos().remove(PhotoGrid.getDisplayedPhotos().get
                    (selectedImageIndex));
            PhotoGrid.getSelectedImages().remove(selectedImage);
            imageCell.setBorder(null);
        }
    }

}
