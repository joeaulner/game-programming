package cannonball.world;

import cannonball.util.Matrix3x3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class CannonballManager {

    private Random random;
    private ArrayList<VectorObject> cannonballs;

    private float windVelocity = 0.0f;
    private float windVelocityDelta = 0.1f;

    private float spawnRate = 3f;
    private float timeSinceSpawn = 3f;

    private static CannonballManager instance = new CannonballManager();

    private CannonballManager() {
        random = new Random();
        cannonballs = new ArrayList<>();
    }

    public static CannonballManager getInstance() {
        return instance;
    }

    public ArrayList<VectorObject> getCannonballs() {
        return cannonballs;
    }

    public float getWindVelocity() {
        return windVelocity;
    }

    public void update(float delta, Matrix3x3f viewport) {
        ArrayList<VectorObject> objectsToRemove = new ArrayList<>();
        float dX = windVelocity * delta;
        float dY = -4.0f * delta;

        for (VectorObject cannonball : cannonballs) {
            Point.Float loc = cannonball.getLocation();
            float x = loc.x + dX;
            float y = loc.y + dY;
            if (y < -10 || Math.abs(x) > 20) {
                objectsToRemove.add(cannonball);
                continue;
            }
            cannonball.setLocation(x, y);
            cannonball.setViewport(viewport);
            cannonball.updateWorld();
        }

        for (VectorObject objectToRemove : objectsToRemove) {
            cannonballs.remove(objectToRemove);
        }

        timeSinceSpawn += delta;
        if (timeSinceSpawn >= spawnRate) {
            int x = random.nextInt(39) - 20;
            spawnCannonball(x, 11, viewport);
            timeSinceSpawn = 0.0f;
        }
        float dSpawnRate = -0.05f * delta;
        spawnRate = Math.max(0.5f, spawnRate + dSpawnRate);

        float dWindVelocity = windVelocityDelta * delta;
        windVelocity += dWindVelocity;
        if (windVelocity >= 2) {
            windVelocityDelta = -Math.abs(windVelocityDelta);
        } else if (windVelocity <= -2) {
            windVelocityDelta = Math.abs(windVelocityDelta);
        }
    }

    public void render(Graphics g) {
        for (VectorObject cannonball : cannonballs) {
            cannonball.render(g);
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
}
