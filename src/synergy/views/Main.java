package synergy.views;


import com.j256.ormlite.logger.LocalLog;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import synergy.engines.suggestion.Engine;
import synergy.models.Photo;
import synergy.views.panes.main.BottomPane;
import synergy.views.panes.main.CenterPane;
import synergy.views.panes.main.RightPane;
import synergy.views.panes.main.TopPane;

public class Main extends Application {

    public static PhotoGrid photosGrid;
    public static BorderPane root;
    public static TaggingArea taggingArea;

    public void start(final Stage primaryStage) {
        root = new BorderPane();
        root.setId("background");
        taggingArea = new TaggingArea();

        getClass().getResource("images/logo.png");
        Scene scene = new Scene(root, 1050, 800);
        primaryStage.setTitle("Instatag");
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(500);
        primaryStage.setMinWidth(1050);
        primaryStage.centerOnScreen();
        scene.getStylesheets().add("css/background1.css");
	    new TopPane (primaryStage);
	    new CenterPane ();
	    new RightPane ();
	    new BottomPane ();
	    photosGrid.setGridPhotos(Photo.getAllPhotos ());
        primaryStage.getIcons().add(new Image("images/logo.png"));
        primaryStage.show();
        SliderBar.show();
        SliderBar.hide();
        System.out.println("THIS IS MAIN!");
    }

    public static void main(String[] args) {
        System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, "ERROR");
        Engine.prepare();

        Application.launch();
    }
}
