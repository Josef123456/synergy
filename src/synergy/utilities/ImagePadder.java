package synergy.utilities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Created by alexstoick on 3/21/15.
 */
public class ImagePadder {

    public static BufferedImage padToSize(BufferedImage initialImage, int height, int width,
                                          Color backgroundColor) {
        BufferedImage paddedImage = new BufferedImage(width, height, initialImage.getType());
        Graphics g = paddedImage.getGraphics();
        System.out.println("BACKGROUND BEFORE" + backgroundColor);
        System.out.println("BACKGROUND AFTER" + backgroundColor);
        g.setColor(backgroundColor);
        System.out.println("REAL COLOR" + g.getColor());
        g.fillRect(0, 0, width, height);
        //why is it the same background... it beats me.

        int imageHeight = initialImage.getHeight();
        int imageWidth = initialImage.getWidth();
        int padHeight = height - imageHeight;
        int padWidth = width - imageWidth;

        g.drawImage(initialImage, padWidth / 2, padHeight / 2, backgroundColor, null);
        g.dispose();
        return paddedImage;
    }

}
