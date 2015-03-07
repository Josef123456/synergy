package synergy.tasks;

import com.bric.image.jpeg.JPEGMetaData;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import synergy.models.Photo;
import synergy.newViews.PhotoGrid;
import synergy.utilities.WritableImageCreator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

/**
 * Created by alexstoick on 3/7/15.
 */
public class ThumbnailLoaderTask extends Task {
	private List<Photo> photos;
	private List<Photo> displayedPhotosList;
	private ObservableList<Image> displayedImagesList;

	public ThumbnailLoaderTask (List<Photo> photos) {
		this.photos = photos;
		this.displayedImagesList = PhotoGrid.getDisplayedImagesList ();
		this.displayedPhotosList = PhotoGrid.getDisplayedPhotosList ();
	}

	@Override
	protected Object call () throws Exception {
		for (Photo photo : photos) {
			BufferedImage initialThumbNail = null;
			System.out.println ("gfdkjh");
			try {
				initialThumbNail = JPEGMetaData.getThumbnail(new File (photo.getPath ()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println ("gfdkjh");
			final WritableImage finalWi = WritableImageCreator.fromBufferedImage (initialThumbNail);
			System.out.println ("fdsgs");
			Platform.runLater (() -> {
				displayedImagesList.add (finalWi);
				displayedPhotosList.add (photo);
			});
			System.out.println ("Loaded thumbnail for: " + photo.getPath ());
		}
		new FullResolutionPhotoLoaderTask (photos).run ();
		return null;
	}
}