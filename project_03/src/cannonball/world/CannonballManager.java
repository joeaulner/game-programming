package cannonball.world;

import cannonball.util.Matrix3x3f;
import cannonball.util.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class CannonballManager {

    private Random random;
    private ArrayList<Cannonball> cannonballs;
    private Vector2f clickPos;

    private float windVelocity;
    private float windVelocityDelta;

    private float spawnRate;
    private float timeSinceSpawn;

    private boolean cannonballDestroyed;
    private final float scale = 0.1f;

    private static CannonballManager instance = new CannonballManager();

    private CannonballManager() {
        random = new Random();
        cannonballs = new ArrayList<>();
    }

    public static CannonballManager getInstance() {
        return instance;
    }

    public void initialize() {
        windVelocity = 0.0f;
        windVelocityDelta = 0.3f;
        spawnRate = timeSinceSpawn = 2.0f;
        cannonballDestroyed = false;
    }

    public void onGameOver() {
        cannonballs.clear();
    }

    public ArrayList<Cannonball> getCannonballs() {
        return cannonballs;
    }

    public float getWindVelocity() {
        return windVelocity;
    }

    public void setClickPos(Vector2f clickPos) {
        this.clickPos = clickPos;
    }

    public boolean getCannonballDestroyed() {
        return cannonballDestroyed;
    }

    public void update(float delta, Matrix3x3f viewport) {
        ArrayList<VectorObject> toRemove = new ArrayList<>();
        float dX = windVelocity * delta;
        float dY = -4.0f * delta;
        cannonballDestroyed = false;

        for (VectorObject cannonball : cannonballs) {
            Point.Float loc = cannonball.getLocation();
            float x = loc.x + dX;
            float y = loc.y + dY;
            // remove cannonball if it passes through edges of game world
            if (y < -10 || Math.abs(x) > 20) {
                toRemove.add(cannonball);
                continue;
            }
            // remove cannonball if it was clicked
            float radius = 5 * scale;
            if (clickPos != null &&
                    clickPos.x > x - radius && clickPos.x < x + radius &&
                    clickPos.y > y - radius && clickPos.y < y + radius) {
                toRemove.add(cannonball);
                cannonballDestroyed = true;
            }
            cannonball.setLocation(x, y);
            cannonball.setViewport(viewport);
            cannonball.updateWorld();
        }
        clickPos = null;

        for (VectorObject objectToRemove : toRemove) {
            cannonballs.remove(objectToRemove);
        }

        timeSinceSpawn += delta;
        if (timeSinceSpawn >= spawnRate) {
            int x = random.nextInt(39) - 20;
            spawnCannonball(x, 11, viewport);
            timeSinceSpawn = 0.0f;
        }
        float dSpawnRate = -0.025f * delta;
        spawnRate = Math.max(0.25f, spawnRate + dSpawnRate);

        float dWindVelocity = windVelocityDelta * delta;
        windVelocity += dWindVelocity;
        if (windVelocity >= 3.0f) {
            windVelocityDelta = -Math.abs(windVelocityDelta);
        } else if (windVelocity <= -3.0f) {
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
        Cannonball cannonball = new Cannonball(cannonballShape, x, y, Color.BLACK, viewport, null);
        cannonball.setScale(scale, scale);
        cannonballs.add(cannonball);
    }
}
