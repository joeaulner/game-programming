package cannonball.world;

import cannonball.util.Matrix3x3f;
import cannonball.util.Vector2f;

import java.awt.*;

public class Cannonball extends VectorObject {

    private Vector2f velocity;

    public Cannonball(Shape shape, float x, float y, Color color, Vector2f velocity) {
        super(shape, x, y, color);
        this.velocity = velocity;
    }

    public Vector2f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2f velocity) {
        this.velocity = velocity;
    }
}
