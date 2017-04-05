package sprites.world;

import sprites.util.Matrix3x3f;
import sprites.util.Vector2f;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

public class SpriteObject implements Drawable {

    private Matrix3x3f viewport;

    private float spriteScale = 0.5f;
    private BufferedImage sprite;
    private Vector2f location;
    private Vector2f worldLocation;

    public SpriteObject(String filename, float x, float y) {
        loadSprite(filename);
        location = new Vector2f(x, y);
    }

    public Vector2f getLocation() {
        return location;
    }

    public void setlocation(float x, float y) {
        location = new Vector2f(x, y);
    }

    public void setLocation(Vector2f location) {
        this.location = location;
    }

    public void setViewport(Matrix3x3f viewport) {
        this.viewport = viewport;
    }

    public void setSpriteScale(float spriteScale) {
        this.spriteScale = spriteScale;
    }

    public void setSprite(String filename) {
        loadSprite(filename);
    }

    private void loadSprite(String filename) {
        URL url = ClassLoader.getSystemResource(filename);
        if (url == null) {
            System.err.printf("Unable to load sprite: %s\n", filename);
            System.exit(1);
        }
        try {

            sprite = ImageIO.read(url);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void updateWorld() {
        worldLocation = viewport.mul(location);
    }

    @Override
    public void render(Graphics g) {
        BufferedImage scaledSprite = scaleWithGraphics();
        float x = worldLocation.x - scaledSprite.getWidth() / 2;
        float y = worldLocation.y - scaledSprite.getHeight() / 2;
        g.drawImage(scaledSprite, (int) x, (int) y, null);
    }

    private BufferedImage scaleWithGraphics() {
        BufferedImage image = new BufferedImage(
                (int) (sprite.getWidth() * spriteScale),
                (int) (sprite.getHeight() * spriteScale),
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR
        );
        g2d.drawImage(sprite, 0, 0, image.getWidth(), image.getHeight(), null);
        g2d.dispose();
        return image;
    }
}
