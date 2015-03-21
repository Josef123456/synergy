package synergy.utilities;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by alexstoick on 3/21/15.
 */
public class ImagePadder {



	public static BufferedImage padToSize(BufferedImage initialImage, int height, int width) {
		BufferedImage paddedImage = new BufferedImage(height, width, initialImage.getType());
		Graphics g = paddedImage.getGraphics();
		g.setColor(new Color (29,30,30));
		g.fillRect (0, 0, width, height);

		int imageHeight = initialImage.getHeight ();
		int imageWidth = initialImage.getWidth ();

		if ( imageHeight < height ) {
			// Pad height
			int totalPad = height - imageHeight ;
			g.drawImage (initialImage, 0, totalPad/2, null);
			g.dispose();
		} else {
			// Pad width
			int totalPad = width - imageWidth ;
			g.drawImage (initialImage,totalPad/2, 0,null);
			g.dispose();
		}

		return paddedImage;
	}

}
