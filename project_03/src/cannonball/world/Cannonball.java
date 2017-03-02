package cannonball.world;

import cannonball.util.Vector2f;

import java.awt.*;

/**
 * Extension of VectorObject representing a projectile with a
 * velocity represented by a Vector2f.
 */
public class Cannonball extends VectorObject {

    private Vector2f velocity;

    /**
     * Calls upon the parent constructor to initialize all of the VectorObject
     * properties; also initializes the velocity of the object.
     * @param shape A shape whose pathIterator will be used to construct the vectors ArrayList.
     * @param x The object's x-coordinate.
     * @param y The object's y-coordinate.
     * @param color The object's color.
     * @param velocity The object's velocity.
     */
    public Cannonball(Shape shape, float x, float y, Color color, Vector2f velocity) {
        super(shape, x, y, color);
        this.velocity = velocity;
    }

    /**
     * Returns the velocity of the Object.
     * @return A Vector2f representing the object's velocity.
     */
    public Vector2f getVelocity() {
        return velocity;
    }
}
