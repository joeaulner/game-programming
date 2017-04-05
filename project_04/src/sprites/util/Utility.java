package sprites.util;

import java.awt.*;
import java.util.List;

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

    public static void drawPolygon(Graphics g, Vector2f[] polygon) {
        Vector2f start = polygon[polygon.length - 1];
        for (Vector2f end : polygon) {
            g.drawLine((int) start.x, (int) start.y, (int) end.x, (int) end.y);
            start = end;
        }
    }

    public static void drawPolygon(Graphics g, List<Vector2f> polygon) {
        Vector2f start = polygon.get(polygon.size() - 1);
        for (Vector2f end : polygon) {
            g.drawLine((int) start.x, (int) start.y, (int) end.x, (int) end.y);
            start = end;
        }
    }

    public static boolean rectRectIntersection(Vector2f[] A, Vector2f[] B) {
        // separating axis intersection algorithm
        Vector2f N0 = A[0].sub(A[1]).div(2.0f);
        Vector2f N1 = A[1].sub(A[2]).div(2.0f);
        Vector2f CA = A[0].add(A[2]).div(2.0f);

        float D0 = N0.len();
        float D1 = N1.len();
        N1 = N1.div(D1);
        N0 = N0.div(D0);

        Vector2f N2 = B[0].sub(B[1]).div(2.0f);
        Vector2f N3 = B[1].sub(B[2]).div(2.0f);
        Vector2f CB = B[0].add(B[2]).div(2.0f);

        float D2 = N2.len();
        float D3 = N3.len();
        N2 = N2.div(D2);
        N3 = N3.div(D3);

        Vector2f C = CA.sub(CB);

        float DA = D0;
        float DB = D2 * Math.abs(N2.dot(N0));
        DB += D3 * Math.abs(N3.dot(N0));

        if (DA + DB < Math.abs(C.dot(N0)))
            return false;

        DA = D1;
        DB = D2 * Math.abs(N2.dot(N1));
        DB += D3 * Math.abs(N3.dot(N1));

        if (DA + DB < Math.abs(C.dot(N1)))
            return false;

        DA = D2;
        DB = D0 * Math.abs(N0.dot(N2));
        DB += D1 * Math.abs(N1.dot(N2));

        if (DA + DB < Math.abs(C.dot(N2)))
            return false;

        DA = D3;
        DB = D0 * Math.abs(N0.dot(N3));
        DB += D1 * Math.abs(N1.dot(N3));

        if (DA + DB < Math.abs(C.dot(N3)))
            return false;

        return true;
    }
}
