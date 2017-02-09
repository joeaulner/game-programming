package transformations.util;

/**
 * Represents a vector in 2D space. Provides methods
 * for various transformations.
 */
public class Vector2f {

    public float x;
    public float y;
    public float w;

    /**
     * Create the default matrix-transformable vector at the origin.
     */
    public Vector2f() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.w = 1.0f;
    }

    /**
     * Create a new instance from an existing vector.
     * @param v An existing vector to copy.
     */
    public Vector2f(Vector2f v) {
        this.x = v.x;
        this.y = v.y;
        this.w = v.w;
    }

    /**
     * Create a matrix-transformable vector from a pair of coordinates.
     * @param x The vector's x-coordinate.
     * @param y The vector's y-coordinate.
     */
    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
        this.w = 1.0f;
    }

    /**
     * Creates a new vector from a pair of coordinates. The w value is
     * explicitly defined, therefore the vector may not be matrix-transformable.
     * @param x
     * @param y
     * @param w
     */
    public Vector2f(float x, float y, float w) {
        this.x = x;
        this.y = y;
        this.w = w;
    }

    /**
     * Translate the vector by given x and y distances.
     * @param tx The distance to translate along the x-axis.
     * @param ty The distance to translate along the y-axis.
     */
    public void translate(float tx, float ty) {
        x += tx;
        y += ty;
    }

    /**
     * Scale the vector by given x and y ratios.
     * @param sx The scale along the x-axis.
     * @param sy The scale along the y-axis.
     */
    public void scale(float sx, float sy) {
        x *= sx;
        y *= sy;
    }

    /**
     * Rotate the vector about the origin.
     * @param rad Angle of rotation in radians.
     */
    public void rotate(float rad) {
        float tmp = (float)(x * Math.cos(rad) - y * Math.sin(rad));
        y = (float)(x * Math.sin(rad) + y * Math.cos(rad));
        x = tmp;
    }

    /**
     * Shear the vector
     * @param sx Degree to shear along the x-axis.
     * @param sy Degree to shear along the y-axis.
     */
    public void shear(float sx, float sy) {
        float tmp = x + sx * y;
        y = y + sy * x;
        x = tmp;
    }
}
