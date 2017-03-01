package cannonball.world;

import cannonball.util.Matrix3x3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class CannonballManager {

    private ArrayList<VectorObject> cannonballs;
    private float spawnRate;
    private float timeSinceSpawn;

    public CannonballManager() {
        cannonballs = new ArrayList<>();
        spawnRate = timeSinceSpawn = 0.5f;
    }

    public ArrayList<VectorObject> getCannonballs() {
        return cannonballs;
    }

    public void setViewport(Matrix3x3f viewport) {
        for (VectorObject cannonball : cannonballs) {
            cannonball.setViewport(viewport);
        }
    }

    public void update(float delta, Matrix3x3f viewport) {
        float dX = -0.0f * delta;
        float dY = -3.0f * delta;
        for (VectorObject cannonball : cannonballs) {
            Point.Float loc = cannonball.getLocation();
            cannonball.setLocation(loc.x + dX, loc.y + dY);
            // TODO: find out if viewport can be accurately set using just initialize &
            // onComponentResized to avoid this extra step during the game loop
            cannonball.setViewport(viewport);
            cannonball.updateWorld();
        }

        timeSinceSpawn += delta;
        if (timeSinceSpawn >= spawnRate) {
            Random random = new Random();
            int x = random.nextInt(31) - 15;
            spawnCannonball(x, 11, viewport);
            timeSinceSpawn = 0.0f;
        }
    }

    private void spawnCannonball(float x, float y, Matrix3x3f viewport) {
        Shape cannonballShape = new Polygon(
                new int[] { 2, -2, -5, -5, -2, 2, 5, 5, 2 },
                new int[] { -5, -5, -2, 2, 5, 5, 2, -2, -5 },
                9
        );
        VectorObject cannonball = new VectorObject(cannonballShape, x, y, Color.BLACK, viewport);
        cannonball.setScale(0.1f, 0.1f);
        cannonballs.add(cannonball);
    }

    public void render(Graphics g) {
        for (VectorObject cannonball : cannonballs) {
            cannonball.render(g);
        }
    }
}
