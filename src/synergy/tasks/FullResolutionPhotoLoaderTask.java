package synergy.tasks;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import org.imgscalr.Scalr;
import synergy.models.Photo;
import synergy.newViews.PhotoGrid;
import synergy.utilities.WritableImageCreator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

/**
 * Created by alexstoick on 3/7/15.
 */
public class FullResolutionPhotoLoaderTask extends Task {

	private List<Photo> photos;
	private List<Photo> displayedPhotosList;
	private ObservableList<Image> displayedImagesList;

	public FullResolutionPhotoLoaderTask (List<Photo> photos) {
		this.photos = photos;
		this.displayedImagesList = PhotoGrid.getDisplayedImagesList ();
		this.displayedPhotosList = PhotoGrid.getDisplayedPhotosList ();
	}

	@Override
	protected Object call () throws Exception {

		for (Photo photo : photos) {
			BufferedImage initialImage = null;
			try {
				initialImage = ImageIO.read (new File (photo.getPath ()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			BufferedImage scaledImage = Scalr.resize (initialImage, 350);
			initialImage.flush();
			final WritableImage finalWi = WritableImageCreator.fromBufferedImage (scaledImage);

			for (int i = 0; i < displayedPhotosList.size(); ++i) {
				final int  j = i;
				Photo currentPhoto = displayedPhotosList.get(i);
				if (currentPhoto.getPath() == photo.getPath()) {
					Platform.runLater (() -> {
						displayedImagesList.remove (j);
						displayedImagesList.add (j, finalWi);

					});
					break;
				}
			}

			System.out.println("Replaced " + photo.getPath() );
		}



		return null;
	}
}