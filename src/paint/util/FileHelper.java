package paint.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class FileHelper {

    public BufferedImage getImage(String filename) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(this.getClass().getResource("/paint/render/images/" + filename).toURI()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return image;
    }
}
