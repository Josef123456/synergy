package synergy.views;


import com.j256.ormlite.logger.LocalLog;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private Button importBtn, exportBtn, allPhotosBtn, printingViewBtn, trashBtn;
    private ToolBar toolBar;
    private VBox topPane;
    private HBox leftButtonsBox, rightButtonsBox;
    private Region spacer;
    private SearchField searchField;
    private ComboBox comboBox;
    private static Stage primaryStage;
    private PhotoGrid photosGrid;
    private ObservableList<Image> displayedImagesList;
    private BorderPane root;
    private TaggingArea taggingArea;

    public void start(final Stage primaryStage) {
        Main.primaryStage = primaryStage;
        root = new BorderPane();
        root.setId("background");
        taggingArea = new TaggingArea();
        ArrayList<Photo> selectedPhotos = PhotoGrid.getSelectedPhotos ();
        StringBuilder sb = new StringBuilder ();
        for ( Photo photo : selectedPhotos ) {
            sb.append (photo.getPath ());
        }
        System.out.println(sb.toString());
        topArea();
        centerArea();
        rightArea();
        bottomArea();
        addEventHandlers();

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setTitle("Instatag");
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(500);
        primaryStage.centerOnScreen();
        scene.getStylesheets().add("background.css");
        primaryStage.show();
    }

    public void topArea() {
        topPane = new VBox();
        toolBar = new ToolBar();
        spacer = new Region();
        spacer.getStyleClass().setAll("spacer");

        leftButtonsBox = new HBox();
        leftButtonsBox.getStyleClass().setAll("button-bar");
        importBtn = new Button("Import");
        setupButtonStyle(importBtn, "firstButton");
        exportBtn = new Button("Export");
        setupButtonStyle(exportBtn, "secondButton");
        leftButtonsBox.getChildren().addAll(importBtn, exportBtn);

        rightButtonsBox = new HBox();
        rightButtonsBox.getStyleClass().setAll("button-bar");

        allPhotosBtn = new Button("Photos");
        setupButtonStyle(allPhotosBtn, "firstButton");

        printingViewBtn = new Button("Printing");
        setupButtonStyle(printingViewBtn, "thirdButton");
        printingViewBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage= new Stage();
                PrintingInterface printer= new PrintingInterface();
                printer.start(stage);
            }
        });

        trashBtn = new Button("Trash");
        setupButtonStyle(trashBtn, "secondButton");
        rightButtonsBox.getChildren().addAll(allPhotosBtn, printingViewBtn, trashBtn);

        leftButtonsBox.setAlignment(Pos.CENTER_LEFT);
        rightButtonsBox.setAlignment(Pos.CENTER_RIGHT);
        leftButtonsBox.getChildren().add(rightButtonsBox);
        HBox.setHgrow(leftButtonsBox, Priority.ALWAYS);

        searchField = new SearchField();
        comboBox = searchField.getComboBox();
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
        displayedImagesList = FXCollections.observableArrayList(new ArrayList<Image>());
        photosGrid = new PhotoGrid(displayedImagesList, taggingArea);
        root.setCenter(photosGrid);
    }

    public void rightArea() {
        taggingArea.update();
        root.setRight(taggingArea);
    }

    public void bottomArea() {
        BottomArea bottomArea = new BottomArea();
        root.setBottom(bottomArea);
    }

    public void setupButtonStyle(Button btn, String buttonName) {
        btn.setStyle("-fx-text-fill: antiquewhite");
        btn.getStyleClass().add(buttonName);
        btn.setMinWidth(130);
    }

    private void addEventHandlerToImport() {
        final FileChooser fileChooser = new FileChooser();
        photosGrid.addPhotosToGrid(Photo.getAllPhotos());

        importBtn.setOnAction(event -> {
            List<File> list = fileChooser.showOpenMultipleDialog(primaryStage);
            ArrayList<Photo> lastImported = new ArrayList<>();
            long t1 = System.currentTimeMillis();

            if (list != null) {
                for (File file : list) {
                    Photo photo = new Photo(file.toString());
                    photo.save();
                    lastImported.add(photo);
                }
                if (photosGrid.getItems().size() == 0) {
                    photosGrid.addPhotosToGrid(Photo.getAllPhotos());
                } else {
                    photosGrid.addPhotosToGrid(lastImported);
                }
                System.out.println("Number of files imported: " + Photo.getAllPhotos().size());
            }
            long t2 = System.currentTimeMillis();
            System.out.println(t2 - t1 + " milliseconds");
            Thread refreshEngine = new Thread(() -> Engine.prepare());
            refreshEngine.setDaemon(true);
            refreshEngine.start();
        });
    }

    public void addEventHandlerToExport() {
    }

    public void addEventHandlers() {
        addEventHandlerToImport();
        addEventHandlerToExport();
    }
    public static void main(String[] args) {
	    System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, "ERROR");
	    Engine.prepare ();
	    Application.launch();

    }
}
