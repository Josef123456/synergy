package synergy.newViews;


import controlsfx.controlsfx.control.GridCell;
import controlsfx.controlsfx.control.GridView;
import controlsfx.controlsfx.control.cell.ImageGridCell;
import org.imgscalr.Scalr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.util.Callback;
import synergy.models.Photo;


/**
 * Created by iHack1337 on 3/2/2015.
 */
public class PhotoGrid extends GridView<Image> {

    ObservableList<Image> displayedImagesList;
    static ArrayList<Image> imageArrayList;
    GridView<Image> photosGrid;

    public PhotoGrid(ObservableList imagesList) {
        displayedImagesList = imagesList;
        photosGrid = this;
        this.setItems(displayedImagesList);

        this.setCellFactory(new Callback<GridView<Image>, GridCell<Image>>() {
            @Override
            public GridCell<Image> call(GridView<Image> param) {
                final ImageGridCell imageCell = new ImageGridCell();
                imageCell.setOnMouseClicked(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        if (imageCell.getBorder() == null) {

                            BorderStroke[] borderStrokeArray = new BorderStroke[4];
                            for (int i = 0; i < 4; i++)
                                borderStrokeArray[i] = new BorderStroke(javafx.scene.paint.Color
                                        .BLUE, BorderStrokeStyle.SOLID, null, BorderStroke.MEDIUM,
                                        new Insets(-5, -5, -5, -5));
                            imageCell.setBorder(new Border(borderStrokeArray));
                        } else {

                            imageCell.setBorder(null);
                        }
                    }
                });
                return imageCell;
            }
        });
        this.setCellHeight(300);
        this.setCellWidth(300);
    }

    public void setGridPhotos(final List<Photo> photos) {
        (new Thread(new Task() {
            @Override
            protected Object call() throws Exception {

                ObservableList<Photo> observablePhotoList = FXCollections.observableArrayList
                        (photos);
                imageArrayList = new ArrayList<>();

                for (Photo photo : observablePhotoList) {
                    BufferedImage initialImage = null;
                    try {
                        initialImage = ImageIO.read(new File(photo.getPath()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    BufferedImage scaledImage = Scalr.resize(initialImage, 350);
                    initialImage.flush();
                    WritableImage writableImage = null;

                    if (scaledImage != null) {
                        writableImage = new WritableImage(scaledImage.getWidth(), scaledImage
                                .getHeight());
                        PixelWriter pw = writableImage.getPixelWriter();
                        for (int x = 0; x < scaledImage.getWidth(); x++) {
                            for (int y = 0; y < scaledImage.getHeight(); y++) {
                                pw.setArgb(x, y, scaledImage.getRGB(x, y));
                            }
                        }
                    }

                    final WritableImage finalWi = writableImage;
                    imageArrayList.add(writableImage);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            displayedImagesList.add(finalWi);
                        }
                    });

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            ImageView imageView = new ImageView(finalWi);
                            imageView.setPreserveRatio(true);
                            imageView.setSmooth(true);
                            Main.gridPane.add(imageView, Main.column, Main.row);
                            Main.column = (Main.column + 1) % 4;
                            if (Main.column == 0)
                                Main.row = Main.row + 1;
                        }
                    });

                    System.out.println(photo.getPath() + " " + photosGrid.getItems().size());
                }
                return null;
            }
        })).start();
    }

    public void setSelectionListener() {
//        photosGrid.getCellFactory().
    }
}
