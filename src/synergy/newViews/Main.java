package synergy.newViews;


import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;
import org.controlsfx.control.cell.ImageGridCell;
import org.imgscalr.Scalr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import synergy.models.Photo;

public class Main extends Application {

    Button importBtn, exportBtn, allPhotosBtn, calendarViewBtn;
    private static Stage primaryStage;
    GridView<Image> photosGrid;
    ObservableList<Image> displayedImagesList;
    ArrayList<Image> imageArrayList;

    public void start(final Stage primaryStage) {
        this.primaryStage = primaryStage;
        final BorderPane root = new BorderPane();
        root.setId("background");

        final VBox topPane = new VBox();
        final ToolBar toolBar = new ToolBar();

        Region spacer = new Region();
        spacer.getStyleClass().setAll("spacer");

        HBox leftBox = new HBox();
        leftBox.getStyleClass().setAll("button-bar");

        importBtn = new Button("Import");
        importBtn.setStyle("-fx-text-fill: antiquewhite");
        importBtn.getStyleClass().add("firstButton");

        exportBtn = new Button("Export");
        exportBtn.setStyle("-fx-text-fill: antiquewhite");
        exportBtn.getStyleClass().add("secondButton");

        leftBox.getChildren().addAll(importBtn, exportBtn);

        HBox rightBox = new HBox();
        rightBox.getStyleClass().setAll("button-bar");

        allPhotosBtn = new Button("InstaTag");
        allPhotosBtn.setStyle("-fx-text-fill: antiquewhite");
        allPhotosBtn.getStyleClass().addAll("thirdButton");

        calendarViewBtn = new Button("Calendar");
        calendarViewBtn.setStyle("-fx-text-fill: antiquewhite");

        rightBox.getChildren().addAll(allPhotosBtn, calendarViewBtn);

        importBtn.setMinWidth(130);
        exportBtn.setMinWidth(130);
        allPhotosBtn.setMinWidth(130);
        calendarViewBtn.setMinWidth(130);

        leftBox.setAlignment(Pos.CENTER_LEFT);
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        leftBox.getChildren().add(rightBox);
        HBox.setHgrow(leftBox, Priority.ALWAYS);

        toolBar.getItems().addAll(leftBox, rightBox);

        final TextField searchField = new TextField("Search");
        searchField.setId("searching");

        searchField.setMinHeight(45);
        searchField.setFont(Font.font("Arial", FontPosture.ITALIC, 25));
        searchField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean
                    oldPropertyValue, Boolean newPropertyValue) {
                if (newPropertyValue) {
                    System.out.println(arg0.getClass().toString());
                }
            }
        });

        displayedImagesList = FXCollections.observableArrayList(new ArrayList<Image>());
        photosGrid = new GridView<>(displayedImagesList);
        photosGrid.setCellFactory(new Callback<GridView<Image>, GridCell<Image>>() {
            @Override
            public GridCell<Image> call(GridView<Image> param) {
                return new ImageGridCell();
            }
        });
        photosGrid.setCellHeight(300);
        photosGrid.setCellWidth(300);

        //cardlayout part:
        addEventHandlers();
        topPane.getChildren().addAll(toolBar, searchField);
        root.setTop(topPane);

        root.setCenter(photosGrid);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Instatag");
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(500);
        primaryStage.centerOnScreen();
        scene.getStylesheets().add("background.css");
        primaryStage.show();
    }

    public void initButtonArea() {

    }

    public void setupButtonStyle(Button btn, String buttonName) {
        btn.setStyle("-fx-text-fill: antiquewhite");
        btn.getStyleClass().add(buttonName);
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
                    BufferedImage scaledImage = Scalr.resize(initialImage, 400);
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
                    System.out.println(photo.getPath() + " " + photosGrid.getItems().size());
                }
                return null;
            }
        })).start();
    }

    private void addEventHandlerToImport() {
        final FileChooser fileChooser = new FileChooser();
        setGridPhotos(Photo.getAllPhotos());

        importBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                List<File> list = fileChooser.showOpenMultipleDialog(primaryStage);
                long t1 = System.currentTimeMillis();

                if (list != null) {
                    for (File file : list) {
                        Photo photo = new Photo(file.toString());
                        photo.save();
                    }
                    setGridPhotos(Photo.getAllPhotos());
                    System.out.println("Number of files imported: " + Photo.getAllPhotos().size());
                }

                long t2 = System.currentTimeMillis();
                System.out.println(t2 - t1 + " milliseconds");
            }
        });
    }

    public void addEventHandlerToExport() {
    }

    public void addEventHandlers() {
        addEventHandlerToImport();
        addEventHandlerToExport();
    }

    public static void main(String[] args) {
        launch();
    }
}
