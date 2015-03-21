package synergy.utilities;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by alexstoick on 3/21/15.
 */
public class ImagePadder {



	public static BufferedImage padToSize(BufferedImage initialImage, int height, int width) {
		BufferedImage paddedImage = new BufferedImage(width, height, initialImage.getType());
		Graphics g = paddedImage.getGraphics();
		g.setColor(new Color (29,30,30));
		g.fillRect (0, 0, width, height);

		int imageHeight = initialImage.getHeight ();
		int imageWidth = initialImage.getWidth ();
        int padHeight = height - imageHeight;
        int padWidth = width - imageWidth;

        g.drawImage(initialImage,padWidth/2,padHeight/2,null);
        g.dispose();
		return paddedImage;
	}

}
