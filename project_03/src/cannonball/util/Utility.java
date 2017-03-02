package cannonball.util;

/**
 * Catch-all class for static utility methods.
 */
public class Utility {

    /**
     * Generate a viewport matrix to transform screen coordinates to world coordinates.
     * @param worldWidth The width of the game world.
     * @param worldHeight The height of the game world.
     * @param screenWidth The width of the screen.
     * @param screenHeight The height of the screen.
     * @return The viewport transformation matrix.
     */
    public static Matrix3x3f createViewport(
            float worldWidth, float worldHeight,
            float screenWidth, float screenHeight
    ) {
        float sx = (screenWidth - 1) / worldWidth;
        float sy = (screenHeight - 1) / worldHeight;
        float tx = (screenWidth - 1) / 2.0f;
        float ty = (screenHeight - 1) / 2.0f;
        return Matrix3x3f.identity()
                .mul(Matrix3x3f.scale(sx, -sy))
                .mul(Matrix3x3f.translate(tx, ty));
    }

    /**
     * Generate a reverse viewport matrix to transform world coordinates to screen coordinates.
     * @param worldWidth The width of the game world.
     * @param worldHeight The height of the game world.
     * @param screenWidth The width of the screen.
     * @param screenHeight The height of the screen.
     * @return The reverse viewport transformation matrix.
     */
    public static Matrix3x3f createReverseViewport(
            float worldWidth, float worldHeight,
            float screenWidth, float screenHeight
    ) {
        float sx = worldWidth / (screenWidth - 1);
        float sy = worldHeight / (screenHeight - 1);
        float tx = (screenWidth - 1) / 2.0f;
        float ty = (screenHeight - 1) / 2.0f;
        return Matrix3x3f.identity()
                .mul(Matrix3x3f.translate(-tx, -ty))
                .mul(Matrix3x3f.scale(sx, -sy));
    }
}
