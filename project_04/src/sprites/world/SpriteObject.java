package sprites.world;

import sprites.util.Matrix3x3f;
import sprites.util.Utility;
import sprites.util.Vector2f;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
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

    public VectorObject boundingBox;
    public float width;
    public float height;

    public SpriteObject(String filename, float x, float y, float width, float height) {
        loadSprite(filename);
        location = new Vector2f(x, y);
        this.width = width;
        this.height = height;

        Rectangle2D.Float rect = new Rectangle2D.Float(-width / 2, -height / 2, width, height);
        boundingBox = new VectorObject(rect, location.x, location.y, Color.BLUE);
    }

    public Vector2f getLocation() {
        return location;
    }

    public void setlocation(float x, float y) {
        location = new Vector2f(x, y);
        boundingBox.setLocation(location);
    }

    public void setLocation(Vector2f location) {
        this.location = location;
        boundingBox.setLocation(location);
    }

    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public void setViewport(Matrix3x3f viewport) {
        this.viewport = viewport;
        boundingBox.setViewport(viewport);
    }

    public void setSprite(String filename) {
        loadSprite(filename);
    }

    @Override
    public void updateWorld() {
        worldLocation = viewport.mul(location);
        boundingBox.updateWorld();
    }

    @Override
    public void render(Graphics g) {
        BufferedImage scaledSprite = scaleWithGraphics();
        float x = worldLocation.x - scaledSprite.getWidth() / 2;
        float y = worldLocation.y - scaledSprite.getHeight() / 2;
        g.drawImage(scaledSprite, (int) x, (int) y, null);
    }

    public void renderBoundingBox(Graphics g) {
        boundingBox.render(g);
    }

    public boolean intersects(SpriteObject spriteObject) {
        if (boundingBox != null && spriteObject.boundingBox != null) {
            Vector2f[] box1 = boundingBox.getVectors();
            Vector2f[] box2 = spriteObject.boundingBox.getVectors();
            return Utility.rectRectIntersection(box1, box2);
        }
        return false;
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

    private BufferedImage scaleWithGraphics() {
        BufferedImage image = new BufferedImage(
                (int) Math.abs(sprite.getWidth() * scaleX),
                (int) Math.abs(sprite.getHeight() * scaleY),
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR
        );
        float sX = Math.signum(scaleX);
        float sY = Math.signum(scaleY);
        float tX = 0;
        float tY = 0;
        if (sX < 0) {
            tX = image.getWidth() - 1;
        }
        if (sY < 0) {
            tY = image.getHeight() - 1;
        }
        g2d.translate(tX, tY);
        g2d.scale(sX, sY);
        g2d.drawImage(sprite, 0, 0, image.getWidth(), image.getHeight(), null);

        g2d.dispose();

        return image;
    }
}
