package sprites.world;

import sprites.util.Matrix3x3f;
import sprites.util.Vector2f;

import java.awt.*;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents a Drawable entity utilizing utilizing vector graphics.
 */
public class VectorObject implements Drawable {

    private ArrayList<Vector2f> vectors;
    private ArrayList<Vector2f> worldVectors;
    private Matrix3x3f viewport;

    private Color color;
    private Point.Float location;
    private float scaleX = 1;
    private float scaleY = 1;
    private float rotation = 0;

    /**
     * Internal constructor that initializes location, color, an empty
     * worldVectors list, and a default viewport set to the identity matrix.
     * @param x The object's x-coordinate.
     * @param y The object's y-coordinate.
     * @param color The object's color.
     */
    private VectorObject(float x, float y, Color color) {
        this.location = new Point.Float(x, y);
        this.color = color;
        worldVectors = new ArrayList<>();
        viewport = Matrix3x3f.identity();
    }

    /**
     * Constructor that allows an ArrayList of vectors to be assigned
     * directly to the vectors field.
     * @param vectors An ArrayList of vectors to store internally.
     * @param x The object's x-coordinate.
     * @param y The object's y-coordinate.
     * @param color The object's color.
     */
    public VectorObject(ArrayList<Vector2f> vectors, float x, float y, Color color) {
        this(x, y, color);
        this.vectors = vectors;
    }

    /**
     * Constructor that converts an array of vectors to an ArrayList.
     * @param vectors An array of vectors.
     * @param x The object's x-coordinate.
     * @param y The object's y-coordinate.
     * @param color The object's color.
     */
    public VectorObject(Vector2f[] vectors, float x, float y, Color color) {
        this(new ArrayList<>(Arrays.asList(vectors)), x, y, color);
    }

    /**
     * Constructor that uses a shape's PathIterator
     * to create the vectors ArrayList.
     * @param shape A shape whose PathIterator will be used to construct the vectors ArrayList.
     * @param x The object's x-coordinate.
     * @param y The object's y-coordinate.
     * @param color The object's color.
     */
    public VectorObject(Shape shape, float x, float y, Color color) {
        this(x, y, color);
        this.vectors = new ArrayList<>();

        PathIterator path = shape.getPathIterator(null);
        float[] coords = new float[2];
        int segmentType;

        while (!path.isDone()) {
            segmentType = path.currentSegment(coords);
            if (segmentType == PathIterator.SEG_MOVETO || segmentType == PathIterator.SEG_LINETO) {
                vectors.add(new Vector2f(coords[0], coords[1]));
            }
            path.next();
        }
    }

    /**
     * Set the object's color.
     * @param color New color of the object.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Get the current location of the object.
     * @return The current location of the object.
     */
    public Point.Float getLocation() {
        return location;
    }

    /**
     * Set the location of the object from separate x and y coordinates.
     * @param x The new x-coordinate.
     * @param y The new y-coordinate.
     */
    public void setLocation(float x, float y) {
        location = new Point.Float(x, y);
    }

    /**
     * Set the location of the object from a Point.Float.
     * @param location The new Point.Float location.
     */
    public void setLocation(Point.Float location) {
        this.location = location;
    }

    /**
     * Set the location of the object from a Vector2f.
     * @param location The new location as a Vector2f.
     */
    public void setLocation(Vector2f location) {
        this.location = new Point.Float(location.x, location.y);
    }

    /**
     * Set the x and y scales of the object.
     * @param scaleX The degree of scaling for the x-axis.
     * @param scaleY The degree of scaling for the y-axis.
     */
    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    /**
     * Get the object's current rotation.
     * @return The current rotation of the object.
     */
    public float getRotation() {
        return rotation;
    }

    /**
     * Set the object's rotation.
     * @param rotation The new rotation.
     */
    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    /**
     * Set the viewport used to scale/translate the viewport in relation to the window.
     * @param viewport The new viewport matrix.
     */
    public void setViewport(Matrix3x3f viewport) {
        this.viewport = viewport;
    }

    /**
     * Constructs a world matrix and applies it to each vector.
     */
    @Override
    public void updateWorld() {
        Matrix3x3f worldMatrix = Matrix3x3f.identity()
                .mul(Matrix3x3f.rotate(rotation))
                .mul(Matrix3x3f.scale(scaleX, scaleY))
                // translate the object to its location in relation to the origin
                .mul(Matrix3x3f.translate(location.x, location.y))
                // scale/translate the object using the viewport to convert to standard origin
                .mul(viewport);

        worldVectors.clear();
        for (Vector2f vector : vectors) {
            worldVectors.add(worldMatrix.mul(vector));
        }
    }

    /**
     * Iterates over the vectors, drawing a line between each adjacent pair.
     * @param g Graphics instance used to render the entity.
     */
    @Override
    public void render(Graphics g) {
        g.setColor(color);

        Vector2f start = null;
        for (Vector2f end : worldVectors) {
            if (start == null) {
                start = end;
                continue;
            }
            g.drawLine((int)start.x, (int)start.y, (int)end.x, (int)end.y);
            start = end;
        }
    }
}
