package transformations.render;

import transformations.util.Matrix3x3f;
import transformations.util.Vector2f;

import java.awt.*;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Arrays;

public class VectorObject implements Drawable {

    private ArrayList<Vector2f> vectors;
    private ArrayList<Vector2f> worldVectors;

    private Color color;
    private Point.Float location;
    private float scaleX = 1;
    private float scaleY = 1;
    private float rotation = 0;

    private VectorObject(float x, float y, Color color) {
        this.location = new Point.Float(x, y);
        this.color = color;
        worldVectors = new ArrayList<>();
    }

    public VectorObject(ArrayList<Vector2f> vectors, float x, float y, Color color) {
        this(x, y, color);
        this.vectors = vectors;
    }

    public VectorObject(Vector2f[] vectors, float x, float y, Color color) {
        this(new ArrayList<>(Arrays.asList(vectors)), x, y, color);
    }

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

    public void setColor(Color color) {
        this.color = color;
    }

    public Point.Float getLocation() {
        return location;
    }

    public void setLocation(float x, float y) {
        location = new Point.Float(x, y);
    }

    public void setLocation(Point.Float location) {
        this.location = location;
    }

    public void setLocation(Vector2f location) {
        this.location = new Point.Float(location.x, location.y);
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

    @Override
    public void updateWorld() {
        Matrix3x3f worldMatrix = Matrix3x3f.identity()
                .mul(Matrix3x3f.rotate(rotation))
                .mul(Matrix3x3f.scale(scaleX, scaleY))
                .mul(Matrix3x3f.translate(location.x, -location.y))
                .mul(Matrix3x3f.translate(WorldCanvas.SCREEN_W / 2, WorldCanvas.SCREEN_H / 2));

        worldVectors.clear();
        for (Vector2f vector : vectors) {
            worldVectors.add(worldMatrix.mul(vector));
        }
    }

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
