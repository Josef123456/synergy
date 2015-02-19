package synergy.Utilities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Cham on 19/02/2015.
 */
public class DisplayPhoto {
    File file;

    public DisplayPhoto(File file){
        this.file = file;
    }

    public DisplayPhoto(String filePath){
        file = new File(filePath);
    }

    public BufferedImage getBufferedImage(int imageWidth, int imageHeight){
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.drawImage(bufferedImage, 0, 0, imageWidth, imageHeight, null);
        g.dispose();
        return image;
    }

}
