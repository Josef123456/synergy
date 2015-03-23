package synergy.views;

import controlsfx.controlsfx.control.GridView;
import controlsfx.controlsfx.control.cell.ImageGridCell;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.print.JobSettings;
import javafx.print.PageLayout;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.imgscalr.Scalr;
import synergy.models.Photo;
import synergy.utilities.ImagePadder;
import synergy.utilities.WritableImageCreator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Josef on 09/03/2015.
 * The PrintingInterface provides the feature of printing.
 * By selecting the printing button in the Main class, the user can select a picture and print the image.
 * Multiple photos and printing in portrait or landscape mode can be done using this class.
 */
public class PrintingInterface extends Application {

    private BorderPane main;
    private Button zoomPlusBtn, zoomMinusBtn;
    private Stage stage;
    private GridView gridPhotos;
    private ObservableList<Image> printImages;
    private PrinterJob job;
    private JobSettings jobSettings;
    private PageLayout pageLayout;

    public void start(Stage primaryStage) throws IOException {
        this.stage = primaryStage;
        main = new BorderPane();
        main.setId("background");

        setupPrintJob();
        setupCenter();
        setupBottom();
        addEventHandlers();

        Scene scene = new Scene(main, pageLayout.getPrintableWidth() + 30, pageLayout
                .getPrintableHeight() + 100);
        scene.getStylesheets().add("css/background1.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Print");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private void setupPrintJob() {
        job = PrinterJob.createPrinterJob();
        job.showPageSetupDialog(null);
        jobSettings = job.getJobSettings();
        pageLayout = jobSettings.getPageLayout();
    }

    private void setupCenter() throws IOException {
        printImages = FXCollections.observableArrayList(new ArrayList<Image>());

        gridPhotos = new GridView(printImages);
        gridPhotos.setCellFactory(gridView -> new ImageGridCell());

        gridPhotos.setCellWidth(300);
        gridPhotos.setCellHeight(300);

        Platform.runLater(() -> {
            for (Photo photo : PhotoGrid.getSelectedPhotos()) {
                try {
                    BufferedImage bufferedImage = ImageIO.read(new File(photo.getPath()));
                    bufferedImage = ImagePadder.padToSize(Scalr.resize(bufferedImage, (int)
                                    gridPhotos.getCellWidth()), (int) gridPhotos.getCellHeight(),
                            (int) gridPhotos.getCellWidth(), java.awt.Color.WHITE);
                    printImages.add(WritableImageCreator.fromBufferedImage(bufferedImage));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        gridPhotos.setMinWidth(pageLayout.getPrintableWidth());
        gridPhotos.setMaxWidth(pageLayout.getPrintableWidth());
        gridPhotos.setMinHeight(pageLayout.getPrintableHeight());
        gridPhotos.setMaxHeight(pageLayout.getPrintableHeight());
        gridPhotos.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        main.setCenter(gridPhotos);
    }

    private void setupBottom() {
        VBox bottomBox = new VBox();
        ToolBar toolbarBottom = new ToolBar();

        Button cancelBtn = new Button("Cancel");
        setupNodeStyle(cancelBtn, "cancelButton");
        cancelBtn.setOnAction(event -> stage.close());

        zoomMinusBtn = new Button("-");
        setupNodeStyle(zoomMinusBtn, "zoomMinusBtn");
        zoomMinusBtn.setMinWidth(20);

        Label zoomLabel = new Label("Zoom");
        setupNodeStyle(zoomLabel, "zoomLabel");

        zoomPlusBtn = new Button("+");
        setupNodeStyle(zoomPlusBtn, "zoomPlusBtn");
        zoomPlusBtn.setMinWidth(20);

        Button printBtn = new Button("Print");
        setupNodeStyle(printBtn, "printButton");
        printBtn.setOnAction(event -> {
            print(main.getCenter());
        });

        HBox zoomBox = new HBox(5);
        zoomBox.getChildren().addAll(zoomMinusBtn, zoomLabel, zoomPlusBtn);
        zoomBox.setAlignment(Pos.CENTER);
        zoomBox.setHgrow(zoomBox, Priority.ALWAYS);

        HBox leftBox = new HBox();
        leftBox.getChildren().addAll(cancelBtn);
        leftBox.setAlignment(Pos.CENTER_LEFT);

        HBox rightBox = new HBox();
        rightBox.getChildren().add(printBtn);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        toolbarBottom.getItems().addAll(leftBox, zoomBox, rightBox);
        bottomBox.getChildren().add(toolbarBottom);
        main.setBottom(bottomBox);
    }

    public void setupNodeStyle(Node node, String nodeName) {
        node.setStyle("-fx-text-fill: antiquewhite");
        node.getStyleClass().add(nodeName);
        if (node.getClass().equals(Button.class))
            ((Button) node).setMinWidth(130);
    }

    private void addEventHandlers() {
        zoomMinusBtn.setOnAction(event -> {
            double cellWidth = gridPhotos.getCellWidth();
            double cellHeight = gridPhotos.getCellHeight();
            if (cellWidth > 100 & cellHeight > 100) {
                gridPhotos.setCellWidth(cellWidth - 50);
                gridPhotos.setCellHeight(cellHeight - 50);
            }
        });

        zoomPlusBtn.setOnAction(event -> {
            double cellWidth = gridPhotos.getCellWidth();
            double cellHeight = gridPhotos.getCellHeight();
            if (cellWidth < 900 & cellHeight < 900) {
                gridPhotos.setCellWidth(cellWidth + 50);
                gridPhotos.setCellHeight(cellHeight + 50);
            }
        });
    }

    public boolean print(Node node) {

        if (job != null && job.showPrintDialog(main.getScene().getWindow())) {
            System.out.println("JOB SETTINGS " + job.getJobSettings());

            if (job.printPage(node)) {
                job.endJob();
                stage.close();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
