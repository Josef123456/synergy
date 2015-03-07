package synergy.utilities;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.awt.image.BufferedImage;

/**
 * Created by alexstoick on 3/7/15.
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
