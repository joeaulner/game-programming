package cannonball.util;

public class Utility {

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
