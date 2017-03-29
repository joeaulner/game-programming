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

    private BufferedImage sprite;
    private Vector2f location;
    private Vector2f worldLocation;
    private float scaleX = 1;
    private float scaleY = 1;
    private float rotation = 0;

    public SpriteObject(String filename, float x, float y) {
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

    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void setViewport(Matrix3x3f viewport) {
        this.viewport = viewport;
    }

    @Override
    public void updateWorld() {
        Matrix3x3f worldMatrix = Matrix3x3f.identity()
                .mul(Matrix3x3f.rotate(rotation))
                .mul(Matrix3x3f.scale(scaleX, scaleY))
                // translate the object to its location in relation to the origin
                .mul(Matrix3x3f.translate(location.x, location.y))
                // scale/translate the object using the viewport to convert to standard origin
                .mul(viewport);

        worldLocation = worldMatrix.mul(location);
    }

    @Override
    public void render(Graphics g) {
        int x = (int) worldLocation.x - sprite.getWidth() / 2;
        int y = (int) worldLocation.y - sprite.getHeight() / 2;
        g.drawImage(sprite, x, y, null);
    }
}
