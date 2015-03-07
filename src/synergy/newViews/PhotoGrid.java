package synergy.newViews;


import com.bric.image.jpeg.JPEGMetaData;

import org.imgscalr.Scalr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import controlsfx.controlsfx.control.GridCell;
import controlsfx.controlsfx.control.GridView;
import controlsfx.controlsfx.control.cell.ImageGridCell;
import controlsfx.impl.org.controlsfx.skin.GridRowSkin;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
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
    static ArrayList<Image> qualityImageArrayList;
    static ArrayList<Photo> displayedPhotoList = new ArrayList<>();
    public static ArrayList<Image> selectedImages = new ArrayList<>();
    public static GridView<Image> photosGrid;

    public PhotoGrid(ObservableList imagesList) {
        displayedImagesList = imagesList;
        photosGrid = this;
        this.setItems(displayedImagesList);

        photosGrid.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
//                System.out.println("REGISTERED CLICK at " + event.getX());
                System.out.println("REGISTERED EVENT AT " + event.getSceneX() + " " + event
                        .getSceneY());
                GridRowSkin.gridRowSkin.getNodeAtCoordinates();
            }
        });

        this.setCellFactory(new Callback<GridView<Image>, GridCell<Image>>() {
            @Override
            public GridCell<Image> call(GridView<Image> param) {
                final ImageGridCell imageCell = new ImageGridCell();
                imageCell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (imageCell.getBorder() == null) {

                            selectedImages.add(imageCell.getItem());
                            BorderStroke[] borderStrokeArray = new BorderStroke[4];
                            for (int i = 0; i < 4; i++)
                                borderStrokeArray[i] = new BorderStroke(javafx.scene.paint.Color
                                        .BLUE, BorderStrokeStyle.SOLID, null, BorderStroke.MEDIUM,
                                        new Insets(-5, -5, -5, -5));
                            imageCell.setBorder(new Border(borderStrokeArray));
                        } else {
                            selectedImages.remove(imageCell.getItem());
                            imageCell.setBorder(null);
                        }
                    }
                });
                return imageCell;
            }
        });

//        this.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        this.setCellHeight(300);
        this.setCellWidth(300);
    }

    public void setGridPhotos(final List<Photo> photos) {
        Thread setPhotosThread = new Thread(new Task() {
            @Override
            protected Object call() throws Exception {
                for (Photo photo : photos) {
                    BufferedImage initialThumbNail = null;
                    try {
                        initialThumbNail = JPEGMetaData.getThumbnail(new File(photo.getPath()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    WritableImage writableImage = null;

                    if (initialThumbNail != null) {
                        writableImage = new WritableImage(initialThumbNail.getWidth(),
                                initialThumbNail.getHeight());
                        PixelWriter pw = writableImage.getPixelWriter();
                        for (int x = 0; x < initialThumbNail.getWidth(); x++) {
                            for (int y = 0; y < initialThumbNail.getHeight(); y++) {
                                pw.setArgb(x, y, initialThumbNail.getRGB(x, y));
                            }
                        }
                    }


                    final WritableImage finalWi = writableImage;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            displayedImagesList.add(finalWi);
                            displayedPhotoList.add(photo);
                        }
                    });
                    System.out.println(photo.getPath() + " " + photosGrid.getItems().size());
                }

                qualityImageArrayList = new ArrayList<>();
                for (Photo photo : photos) {
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

                    qualityImageArrayList.add(writableImage);

                    final WritableImage finalWi = writableImage;

                    for (int i = 0; i < displayedPhotoList.size(); ++i) {
                        final int  j = i;
                        Photo currentPhoto = displayedPhotoList.get(i);
                        if (currentPhoto.getPath() == photo.getPath()) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    displayedImagesList.remove(j);
                                    displayedImagesList.add(j, finalWi);

                                }
                            });
                            break;
                        }
                    }

                    System.out.println("Replaced " + photo.getPath() + " " + photosGrid.getItems
                            ().size());
                }
                return null;
            }
        });

        setPhotosThread.setDaemon(true);
        setPhotosThread.start();
    }

    public void setDisplayPhotos() {

    }

}
