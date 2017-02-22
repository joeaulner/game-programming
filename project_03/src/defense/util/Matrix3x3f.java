package defense.util;

public class Matrix3x3f {

    private float[][] m = new float[3][3];

    public Matrix3x3f() {

    }

    /**
     * Create a matrix from a 2-dimensional array.
     * @param m A 2D array of floats.
     */
    public Matrix3x3f(float[][] m) {
        setMatrix(m);
    }

    /**
     * Add a matrix to self.
     * @param m1 The matrix to add to this.
     * @return The result of adding m1 to this matrix.
     */
    public Matrix3x3f add(Matrix3x3f m1) {
        return new Matrix3x3f(new float[][] {
                { this.m[0][0] + m1.m[0][0],
                        this.m[0][1] + m1.m[0][1],
                        this.m[0][2] + m1.m[0][2] },
                { this.m[1][0] + m1.m[1][0],
                        this.m[1][1] + m1.m[1][1],
                        this.m[1][2] + m1.m[1][2] },
                { this.m[2][0] + m1.m[2][0],
                        this.m[2][1] + m1.m[2][1],
                        this.m[2][2] + m1.m[2][2] } }
        );
    }

    /**
     * Subtract a matrix from self.
     * @param m1 The matrix to subtract from this.
     * @return The result of subtracting m1 from this matrix.
     */
    public Matrix3x3f sub(Matrix3x3f m1) {
        return new Matrix3x3f(new float[][] {
                { this.m[0][0] - m1.m[0][0],
                        this.m[0][1] - m1.m[0][1],
                        this.m[0][2] - m1.m[0][2] },
                { this.m[1][0] - m1.m[1][0],
                        this.m[1][1] - m1.m[1][1],
                        this.m[1][2] - m1.m[1][2] },
                { this.m[2][0] - m1.m[2][0],
                        this.m[2][1] - m1.m[2][1],
                        this.m[2][2] - m1.m[2][2] } }
        );
    }

    /**
     * Multiply this with another matrix.
     * @param m1 The matrix to multiply to his.
     * @return The result of multiplying this matrix with m1.
     */
    public Matrix3x3f mul(Matrix3x3f m1) {
        return new Matrix3x3f( new float[][] {
                { this.m[0][0] * m1.m[0][0] // ******
                        + this.m[0][1] * m1.m[1][0] // M[0,0]
                        + this.m[0][2] * m1.m[2][0], // ******
                        this.m[0][0] * m1.m[0][1] // ******
                                + this.m[0][1] * m1.m[1][1] // M[0,1]
                                + this.m[0][2] * m1.m[2][1], // ******
                        this.m[0][0] * m1.m[0][2] // ******
                                + this.m[0][1] * m1.m[1][2] // M[0,2]
                                + this.m[0][2] * m1.m[2][2] },// ******
                { this.m[1][0] * m1.m[0][0] // ******
                        + this.m[1][1] * m1.m[1][0] // M[1,0]
                        + this.m[1][2] * m1.m[2][0], // ******
                        this.m[1][0] * m1.m[0][1] // ******
                                + this.m[1][1] * m1.m[1][1] // M[1,1]
                                + this.m[1][2] * m1.m[2][1], // ******
                        this.m[1][0] * m1.m[0][2] // ******
                                + this.m[1][1] * m1.m[1][2] // M[1,2]
                                + this.m[1][2] * m1.m[2][2] },// ******
                { this.m[2][0] * m1.m[0][0] // ******
                        + this.m[2][1] * m1.m[1][0] // M[2,0]
                        + this.m[2][2] * m1.m[2][0], // ******
                        this.m[2][0] * m1.m[0][1] // ******
                                + this.m[2][1] * m1.m[1][1] // M[2,1]
                                + this.m[2][2] * m1.m[2][1], // ******
                        this.m[2][0] * m1.m[0][2] // ******
                                + this.m[2][1] * m1.m[1][2] // M[2,2]
                                + this.m[2][2] * m1.m[2][2] } // ******
        });
    }

    /**
     * Set the 2D array of points representing this matrix.
     * @param m A 2D array of floats.
     */
    public void setMatrix(float[][] m) {
        this.m = m;
    }

    /**
     * Get the zero matrix.
     * @return A matrix populated with all zeros.
     */
    public static Matrix3x3f zero() {
        return new Matrix3x3f(new float[][] {
                { 0.0f, 0.0f, 0.0f },
                { 0.0f, 0.0f, 0.0f },
                { 0.0f, 0.0f, 0.0f }
        });
    }

    /**
     * Get the identity matrix.
     * @return A matrix that, when multiplied by, returns the original matrix.
     */
    public static Matrix3x3f identity() {
        return new Matrix3x3f( new float[][] {
                { 1.0f, 0.0f, 0.0f },
                { 0.0f, 1.0f, 0.0f },
                { 0.0f, 0.0f, 1.0f }
        });
    }

    /**
     * Create a translation matrix from a vector.
     * @param v The vector with the (x,y) coordinates of translation.
     * @return The translation matrix for the vector.
     */
    public static Matrix3x3f translate(Vector2f v) {
        return translate(v.x, v.y);
    }

    /**
     * Create a translation matrix from (x,y) coordinates.
     * @param x The x-coordinate of the translation.
     * @param y The y-coordinate of the translation.
     * @return The translation matrix for the (x,y) coordinates.
     */
    public static Matrix3x3f translate(float x, float y) {
        return new Matrix3x3f(new float[][] {
                { 1.0f, 0.0f, 0.0f },
                { 0.0f, 1.0f, 0.0f },
                { x, y, 1.0f }
        });
    }

    /**
     * Create a scaling matrix from a vector.
     * @param v The vector with the x and y scales.
     * @return A scaling matrix from the vector.
     */
    public static Matrix3x3f scale(Vector2f v) {
        return scale(v.x, v.y);
    }

    /**
     * Create a scaling matrix from (x,y) coordinates.
     * @param x The scale of x.
     * @param y The scale of y.
     * @return The scaling matrix from the (x,y) coordinates.
     */
    public static Matrix3x3f scale(float x, float y) {
        return new Matrix3x3f(new float[][] {
                { x, 0.0f, 0.0f },
                { 0.0f, y, 0.0f },
                { 0.0f, 0.0f, 1.0f }
        });
    }

    /**
     * Create a shear matrix from a vector.
     * @param v The vector with the x and y degrees.
     * @return The shearing matrix for the vecotr.
     */
    public static Matrix3x3f shear(Vector2f v) {
        return shear(v.x, v.y);
    }

    /**
     * Create a shear matrix from (x,y) coordinates.
     * @param x The x-degree of shearing.
     * @param y The y-degree of shearing.
     * @return A shearing matrix from the (x,y) coordinates.
     */
    public static Matrix3x3f shear(float x, float y) {
        return new Matrix3x3f(new float[][] {
                { 1.0f, y, 0.0f },
                { x, 1.0f, 0.0f },
                { 0.0f, 0.0f, 1.0f }
        });
    }

    /**
     * Create a rotation matrix.
     * @param rad The angle of rotation in radians.
     * @return The rotation matrix for the angle of rotation.
     */
    public static Matrix3x3f rotate(float rad) {
        return new Matrix3x3f(new float[][] {
                { (float) Math.cos(rad), (float) Math.sin(rad), 0.0f },
                { (float) -Math.sin(rad), (float) Math.cos(rad), 0.0f },
                { 0.0f, 0.0f, 1.0f }
        });
    }

    /**
     * Multiply this by a vector. This effectively transforms
     * the vector using this as the transformation matrix.
     * @param vec The vector to multiply by.
     * @return The vector produced by multiplying this matrix by the vector.
     */
    public Vector2f mul(Vector2f vec) {
        return new Vector2f(
                vec.x * this.m[0][0] //
                        + vec.y * this.m[1][0] // V.x
                        + vec.w * this.m[2][0],//
                vec.x * this.m[0][1] //
                        + vec.y * this.m[1][1] // V.y
                        + vec.w * this.m[2][1],//
                vec.x * this.m[0][2] //
                        + vec.y * this.m[1][2] // V.w
                        + vec.w * this.m[2][2] //
        );
    }

    /**
     * Create a string representation of this matrix.
     * @return A formatted string representing the matrix.
     */
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < 3; ++i) {
            buf.append("[");
            buf.append(m[i][0]);
            buf.append(",\t");
            buf.append(m[i][1]);
            buf.append(",\t");
            buf.append(m[i][2]);
            buf.append("]\n");
        }
        return buf.toString();
    }
}