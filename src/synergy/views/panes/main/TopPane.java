package synergy.views.panes.main;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.swing.filechooser.FileSystemView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import synergy.engines.suggestion.Engine;
import synergy.models.Photo;
import synergy.models.Tag;
import synergy.utilities.CSVGetter;
import synergy.views.Main;
import synergy.views.PhotoGrid;
import synergy.views.PrintingInterface;
import synergy.views.SearchArea;

/**
 * The TopPane class is a VBox that consist of two parts.
 * The first part of this class contains a HBox that has
 * buttons with the functionality of importing, importing to the database and printing.
 * The second part of the VBox is a HBox,as well, which includes features such as searching;
 * Searching by room a or room b, searching by the names and has a DatePicker which lets the user search by time as well.
 */
public class TopPane extends VBox{

	Stage stage;

	public TopPane (Stage stage) {
		setupTopArea ();
		this.stage = stage;
		setPadding(new Insets (0, 0, 8, 0));
		addEventHandlers();
	}

	private Button importBtn;
	private Button importDBBtn;

	private void setupTopArea () {
		ToolBar toolBar = new ToolBar ();
		Region spacer = new Region ();
		spacer.getStyleClass ().setAll ("spacer");

		HBox leftButtonsBox = new HBox (1);
        leftButtonsBox.setPadding(new Insets(0,0,0,35));
		leftButtonsBox.getStyleClass ().setAll ("button-bar");
		importBtn = new Button ("Import");
		setupButtonStyle(importBtn, "importButton");

		importDBBtn = new Button ("Import DataBase");
		setupButtonStyle (importDBBtn, "importDbbtn");

		leftButtonsBox.getChildren ().addAll (importBtn, importDBBtn);

		HBox rightButtonsBox = new HBox (1);
        rightButtonsBox.setPadding(new Insets(0,35,0,0));
		rightButtonsBox.getStyleClass ().setAll ("button-bar");

		Button printingViewBtn = new Button ("Printing");
		setupButtonStyle (printingViewBtn, "printingButton");
		printingViewBtn.setOnAction (event -> {
			Stage stage = new Stage ();
			PrintingInterface printer = new PrintingInterface ();
			try {
				printer.start (stage);
			} catch (IOException e) {
				e.printStackTrace ();
			}
		});
		rightButtonsBox.getChildren ().addAll (printingViewBtn);

		leftButtonsBox.setAlignment (Pos.CENTER_LEFT);
		rightButtonsBox.setAlignment (Pos.CENTER_RIGHT);
		leftButtonsBox.getChildren ().add (rightButtonsBox);
		HBox.setHgrow (leftButtonsBox, Priority.ALWAYS);

		SearchArea searchArea = new SearchArea ();

		toolBar.getItems ().addAll (leftButtonsBox, rightButtonsBox);
		getChildren ().addAll (toolBar, searchArea);
		Main.root.setTop (this);
	}

	private void addEventHandlers() {
		addEventHandlerToImport();
		addEventHandlerToImportDBButton();
	}

	private void addEventHandlerToImport() {
		final FileChooser fileChooser = new FileChooser();


		importBtn.setOnAction(event -> {
			List<File> list = fileChooser.showOpenMultipleDialog(stage);
			ArrayList<Photo> lastImported = new ArrayList<>();
			long t1 = System.currentTimeMillis();
			Photo photo;

			FileSystemView fsv = FileSystemView.getFileSystemView();
			String removableDrive = "";
			CopyOption[] options = new CopyOption[]{
					StandardCopyOption.REPLACE_EXISTING,
					StandardCopyOption.COPY_ATTRIBUTES,
			};

			for(File f : File.listRoots()) {
				if(fsv.getSystemTypeDescription(f) != null) //so unix-like systems can continue without getting exception.
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
					if(fileRoot.contentEquals(removableDrive)){
						String fileName = file.getName();
						System.out.println("File copied to output directory");
						Path inputDir = Paths.get (file.getPath ());
						Path outputDir = Paths.get("photos\\" + fileName);
						try {
							java.nio.file.Files.copy(inputDir,outputDir, options);
						} catch (IOException e) {
							e.printStackTrace();
						}
					 	photo = new Photo(outputDir.toString());
						photo.save();
					} else {
						photo = new Photo(file.toString());
						photo.save();
					}
					if ( PhotoGrid.getDisplayedImagesMap ().get(photo) == null)
						lastImported.add(photo);
				}
				if ( PhotoGrid.displayingImported ) {
					PhotoGrid.addPhotosToGrid (lastImported);
				} else {
					PhotoGrid.displayingImported = true;
					PhotoGrid.setGridPhotos(lastImported);
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

	private void addEventHandlerToImportDBButton() {
		final FileChooser fileChooser = new FileChooser();

		importDBBtn.setOnAction(event -> {
			File selectedFile = fileChooser.showOpenDialog(stage);

			if(selectedFile != null)
			{
				CSVGetter.getCSVData (selectedFile);
				System.out.println(Tag.getAllPlacesTags ()+"\n"+Tag.getAllChildrenTags());
			}
			Thread refreshEngine = new Thread(Engine::prepare);
			refreshEngine.setDaemon(true);
			refreshEngine.start();
		});
	}

	private void setupButtonStyle(Button btn, String buttonName) {
		btn.setStyle("-fx-text-fill: #ffffff");
		btn.getStyleClass().add(buttonName);
		btn.setMinWidth(160);
	}
}