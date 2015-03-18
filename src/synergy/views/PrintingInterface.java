package synergy.views;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import synergy.models.Photo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Josef on 09/03/2015.
 */
public class PrintingInterface extends Application {

    private BorderPane main;
    private ToolBar toolBar, toolbarBottom;
    private Region spacer;
    private HBox leftButtonsBox, centerbox, rightBox;
    private Button portraitBtn, landscapeBtn, cancelBtn, optionsBtn, printBtn;
    private Stage stage;
    private Pane table;

    public void start(Stage primaryStage) throws IOException {
        this.stage = primaryStage;
        main = new BorderPane();
        main.setId("background");
        setupCenter();

        setTopUI();

        setupBottom();

        Scene scene = new Scene(main, 800, 700);
        scene.getStylesheets().add("background1.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Print");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private void setTopUI() {

        toolBar = new ToolBar();
        spacer = new Region();
        spacer.getStyleClass().setAll("spacer");

        leftButtonsBox = new HBox();
        leftButtonsBox.getStyleClass().setAll("button-bar");
        portraitBtn = new Button("Portrait");
        setupButtonStyle(portraitBtn, "firstButton");
        portraitBtn.setOnAction(event -> {
            Printer printer = Printer.getDefaultPrinter();
            PageLayout pageLayout = printer.createPageLayout(Paper.NA_LETTER, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
            double scaleX = pageLayout.getPrintableWidth() / table.getBoundsInParent().getWidth();
            double scaleY = pageLayout.getPrintableHeight() / table.getBoundsInParent().getHeight();
            table.getTransforms().add(new Scale(scaleX, scaleY));
        });
        landscapeBtn = new Button("Landscape");
        landscapeBtn.setOnAction(event -> {
            Printer printer = Printer.getDefaultPrinter();
            PageLayout pageLayout = printer.createPageLayout(Paper.NA_LETTER, PageOrientation.LANDSCAPE, Printer.MarginType.DEFAULT);
            double scaleX = pageLayout.getPrintableWidth() / table.getBoundsInParent().getWidth();
            double scaleY = pageLayout.getPrintableHeight() / table.getBoundsInParent().getHeight();
            table.getTransforms().add(new Scale(scaleX, scaleY));
        });
        setupButtonStyle(landscapeBtn, "secondButton");
        leftButtonsBox.getChildren().addAll(portraitBtn, landscapeBtn);
        leftButtonsBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(leftButtonsBox, Priority.ALWAYS);

        toolBar.getItems().addAll(leftButtonsBox);
        main.setTop(toolBar);
    }

    private void setupCenter() throws IOException {
        table = new Pane();
        ArrayList<Photo> selectedPhotos = PhotoGrid.getSelectedPhotos();
        StringBuilder sb = new StringBuilder();
        for ( Photo photo : selectedPhotos ) {
            sb.append (photo.getPath ());
        }
        BufferedImage bufferedImage= ImageIO.read(new File(""+sb));
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        ImageView iv1 = new ImageView();
        iv1.isSmooth();
        iv1.setImage(image);
        table.getChildren().add(iv1);
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.NA_LETTER, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
        double scaleX = pageLayout.getPrintableWidth() / table.getBoundsInParent().getWidth();
        double scaleY = pageLayout.getPrintableHeight() / table.getBoundsInParent().getHeight();
        table.getTransforms().add(new Scale(scaleX, scaleY));

        main.setCenter(table);
    }

    private void setupBottom() {
        toolbarBottom = new ToolBar();
        cancelBtn = new Button("Cancel");
        setupButtonStyle(cancelBtn, "firstButton");
        cancelBtn.setOnAction(event -> stage.close());

        optionsBtn = new Button("Options");
        setupButtonStyle(optionsBtn, "secondButton");
        optionsBtn.setOnAction(event -> print(table));

        printBtn = new Button("Print");
        setupButtonStyle(printBtn, "fourthButton");
        printBtn.setOnAction(event -> {
            PrinterJob job = PrinterJob.createPrinterJob();
            if (job != null) {
                boolean success = job.printPage(table);
                if (success) {
                    job.endJob();
                }
            }
        });

        centerbox = new HBox();
        centerbox.getChildren().addAll(cancelBtn);
        centerbox.setAlignment(Pos.CENTER_LEFT);

        rightBox = new HBox();
        rightBox.getChildren().addAll(optionsBtn, printBtn);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        centerbox.getChildren().addAll(rightBox);
        HBox.setHgrow(centerbox, Priority.ALWAYS);

        toolbarBottom.getItems().addAll(centerbox, rightBox);
        main.setBottom(toolbarBottom);
    }

    public void setupButtonStyle(Button btn, String buttonName) {
        btn.setStyle("-fx-text-fill: antiquewhite");
        btn.getStyleClass().add(buttonName);
        btn.setMinWidth(130);
    }

    public void print(final Node node) {
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4,
                PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
        PrinterJob job = PrinterJob.createPrinterJob(printer);
        job.getJobSettings().setPageLayout(pageLayout);
        final boolean print = job.showPrintDialog(null);
        if (job != null && print) {
            System.out.println(job.getJobSettings().getPageLayout());
            boolean success = job.printPage(node);
            if (success)
                job.endJob();
        }
    }
}
