package synergy.views;


import com.j256.ormlite.logger.LocalLog;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import synergy.engines.suggestion.Engine;
import synergy.models.Photo;
import synergy.models.Tag;
import synergy.utilities.CSVGetter;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private Button importBtn;
    private Button importDBBtn;
    private static Stage primaryStage;
    public static PhotoGrid photosGrid;
    public static BorderPane root;
    public static TaggingArea taggingArea;

    public void start(final Stage primaryStage) {
        Main.primaryStage = primaryStage;
        root = new BorderPane();
        root.setId("background");
        taggingArea = new TaggingArea();

        topArea();
        centerArea();
        rightArea();
        bottomArea();
        addEventHandlers();

        Scene scene = new Scene(root, 1050, 800);
        primaryStage.setTitle("Instatag");
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(500);
        primaryStage.setMinWidth(1050);
        primaryStage.centerOnScreen();
        scene.getStylesheets().add("background1.css");
        primaryStage.show();
    }

    public void topArea() {
        VBox topPane = new VBox();
        ToolBar toolBar = new ToolBar();
        Region spacer = new Region();
        spacer.getStyleClass().setAll("spacer");

        HBox leftButtonsBox = new HBox(1);
        leftButtonsBox.getStyleClass().setAll("button-bar");
        importBtn = new Button("Import");
        setupButtonStyle(importBtn, "importButton");
        importDBBtn = new Button("Import DataBase");
        setupButtonStyle(importDBBtn, "exportButton");
        leftButtonsBox.getChildren().addAll(importBtn, importDBBtn);

        HBox rightButtonsBox = new HBox(1);
        rightButtonsBox.getStyleClass().setAll("button-bar");

        Button allPhotosBtn = new Button("Photos");
        setupButtonStyle(allPhotosBtn, "photosButton");

        Button printingViewBtn = new Button("Printing");
        setupButtonStyle(printingViewBtn, "printingButton");
        printingViewBtn.setOnAction(event -> {
            Stage stage = new Stage();
            PrintingInterface printer = new PrintingInterface();
            try {
                printer.start(stage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        rightButtonsBox.getChildren().addAll(allPhotosBtn, printingViewBtn);

        leftButtonsBox.setAlignment(Pos.CENTER_LEFT);
        rightButtonsBox.setAlignment(Pos.CENTER_RIGHT);
        leftButtonsBox.getChildren().add(rightButtonsBox);
        HBox.setHgrow(leftButtonsBox, Priority.ALWAYS);

        SearchField searchField = new SearchField(photosGrid);
        ComboBox comboBox = searchField.getComboBox();
        comboBox.getEditor().setId("searching");
        comboBox.getEditor().setFont(Font.font("Arial", FontPosture.ITALIC, 25));
        searchField.getDatePickerTextField().setId("searching");
        searchField.getDatePickerTextField().setFont(Font.font("Arial", FontPosture.ITALIC, 25));

        searchField.setAllMinHeight(45);

        toolBar.getItems().addAll(leftButtonsBox, rightButtonsBox);
        topPane.getChildren().addAll(toolBar, searchField);
        root.setTop(topPane);
    }

    public void centerArea() {
        ObservableList<Image> displayedImagesList = FXCollections.observableArrayList(new ArrayList<>());
        photosGrid = new PhotoGrid(displayedImagesList, taggingArea);
        root.setCenter(photosGrid);
    }

    public void rightArea() {
        SliderBar rightFlapBar = new SliderBar(Pos.BASELINE_RIGHT,taggingArea);
        root.setRight(rightFlapBar);
        taggingArea.update();
    }

    public void bottomArea() {
        BottomArea bottomArea = new BottomArea();
        root.setBottom(bottomArea);
    }

    public void setupButtonStyle(Button btn, String buttonName) {
        btn.setStyle("-fx-text-fill: #ffffff");
        btn.getStyleClass().add(buttonName);
        btn.setMinWidth(130);
    }

    private void addEventHandlerToImport() {
        final FileChooser fileChooser = new FileChooser();
        photosGrid.setGridPhotos(Photo.getAllPhotos());

        importBtn.setOnAction(event -> {
            List<File> list = fileChooser.showOpenMultipleDialog(primaryStage);
            ArrayList<Photo> lastImported = new ArrayList<>();
            long t1 = System.currentTimeMillis();

            FileSystemView fsv = FileSystemView.getFileSystemView();
            String removableDrive = "";
            CopyOption[] options = new CopyOption[]{
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.COPY_ATTRIBUTES,
            };

            for (File f : File.listRoots()) {
                if (fsv.getSystemTypeDescription(f).contentEquals("Removable Disk")) {
                    System.out.println("Found removable disk at root " + f);
                    removableDrive = f.toString();
                } else {
                    //No removable drive found
                }
            }

            if (list != null) {
                for (File file : list) {
                    Path filePath = file.toPath();
                    String fileRoot = filePath.getRoot().toString();
                    if (fileRoot.contentEquals(removableDrive)) {
                        String fileName = file.getName();
                        System.out.println("File copied to output directory");
                        Path inputDir = Paths.get(file.getPath());
                        Path outputDir = Paths.get("photos\\" + fileName);
                        try {
                            java.nio.file.Files.copy(inputDir, outputDir, options);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Photo photo = new Photo(file.toString());
                    photo.save();
                    if (photosGrid.getDisplayedImagesMap().get(photo) == null)
                        lastImported.add(photo);
                }
                if (photosGrid.displayingImported) {
                    photosGrid.addPhotosToGrid(lastImported);
                } else {
                    photosGrid.displayingImported = true;
                    photosGrid.setGridPhotos(lastImported);
                }
                System.out.println("Number of files imported: " + Photo.getAllPhotos().size());
            }
            long t2 = System.currentTimeMillis();
            System.out.println(t2 - t1 + " milliseconds");
            Thread refreshEngine = new Thread(Engine::prepare);
            refreshEngine.setDaemon(true);
            refreshEngine.start();
        });
    }

    public void addEventHandlerToImportDBButton() {
        final FileChooser fileChooser = new FileChooser();
        photosGrid.setGridPhotos(Photo.getAllPhotos());

        importDBBtn.setOnAction(event -> {
           File selectedFile = fileChooser.showOpenDialog(primaryStage);

            if(selectedFile != null)
            {
                CSVGetter.getCSVData(selectedFile);
                System.out.println(Tag.getAllPlacesTags()+"\n"+Tag.getAllChildrenTags());
            }
            Thread refreshEngine = new Thread(Engine::prepare);
            refreshEngine.setDaemon(true);
            refreshEngine.start();
        });
    }

    public void addEventHandlers() {
        addEventHandlerToImport();
        addEventHandlerToImportDBButton();
    }

    public static void main(String[] args) {
        System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, "ERROR");
        Engine.prepare();

        Application.launch();
    }
}
