package synergy.views;


import com.j256.ormlite.logger.LocalLog;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import synergy.engines.suggestion.Engine;
import synergy.models.Photo;
import synergy.models.Tag;
import synergy.utilities.CSVGetter;
import synergy.views.panes.BottomPane;
import synergy.views.panes.CenterPane;
import synergy.views.panes.RightPane;
import synergy.views.panes.TopPane;

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


	public static PhotoGrid photosGrid;
    public static BorderPane root;
    public static TaggingArea taggingArea;

	public void start(final Stage primaryStage) {
        root = new BorderPane();
        root.setId("background");
        taggingArea = new TaggingArea();


		new CenterPane ();
		new RightPane ();
		new BottomPane ();
		System.out.println(photosGrid==null);
		new TopPane (photosGrid, primaryStage);
        Scene scene = new Scene(root, 1050, 800);
        primaryStage.setTitle("Instatag");
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(500);
        primaryStage.setMinWidth(1050);
        primaryStage.centerOnScreen();
        scene.getStylesheets().add("background1.css");
        primaryStage.show();
    }

    public static void main(String[] args) {
        System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, "ERROR");
        Engine.prepare();

        Application.launch();
    }
}
