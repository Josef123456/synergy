package synergy.utilities;

import java.awt.image.BufferedImage;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

/**
 * Class used to create a WritableImage from a buffered image.
 */
public class WritableImageCreator {

	public static WritableImage fromBufferedImage(BufferedImage bufferedImage) {
		WritableImage writableImage = null;

		if (bufferedImage != null) {
			writableImage = new WritableImage(bufferedImage.getWidth(),
					bufferedImage.getHeight());
			PixelWriter pw = writableImage.getPixelWriter();
			for (int x = 0; x < bufferedImage.getWidth(); x++) {
				for (int y = 0; y < bufferedImage.getHeight(); y++) {
					pw.setArgb(x, y, bufferedImage.getRGB(x, y));
				}
			}
		}
		return writableImage;
	}
}
