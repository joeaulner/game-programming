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
    private final float scale = 0.125f;

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
        ArrayList<Cannonball> toRemove = new ArrayList<>();
        cannonballDestroyed = false;

        for (Cannonball cannonball : cannonballs) {
            Vector2f vel = cannonball.getVelocity();
            vel.x += windVelocity * delta;
            vel.y += -1.5f * delta;

            Point.Float pos = cannonball.getLocation();
            pos.x = pos.x + vel.x * delta;
            pos.y = pos.y + vel.y * delta;
            // remove cannonball if it passes through edges of game world
            if (pos.y < -10 || Math.abs(pos.x) > 20) {
                toRemove.add(cannonball);
                continue;
            }
            // remove cannonball if it was clicked
            float radius = 5 * scale;
            if (clickPos != null &&
                    clickPos.x > pos.x - radius && clickPos.x < pos.x + radius &&
                    clickPos.y > pos.y - radius && clickPos.y < pos.y + radius) {
                toRemove.add(cannonball);
                cannonballDestroyed = true;
            }
            cannonball.setViewport(viewport);
            cannonball.updateWorld();
        }
        clickPos = null;

        for (Cannonball objectToRemove : toRemove) {
            cannonballs.remove(objectToRemove);
        }

        timeSinceSpawn += delta;
        if (timeSinceSpawn >= spawnRate) {
            int x = random.nextInt(39) - 20;
            spawnCannonball(x, 11);
            timeSinceSpawn = 0.0f;
        }
        float dSpawnRate = -0.025f * delta;
        spawnRate = Math.max(0.25f, spawnRate + dSpawnRate);

        float dWindVelocity = windVelocityDelta * delta;
        windVelocity += dWindVelocity;
        if (windVelocity >= 2.5f) {
            windVelocityDelta = -Math.abs(windVelocityDelta);
        } else if (windVelocity <= -2.5f) {
            windVelocityDelta = Math.abs(windVelocityDelta);
        }
    }

    public void render(Graphics g) {
        for (VectorObject cannonball : cannonballs) {
            cannonball.render(g);
        }
    }

    private void spawnCannonball(float x, float y) {
        Shape cannonballShape = new Polygon(
                new int[] { 2, -2, -5, -5, -2, 2, 5, 5, 2 },
                new int[] { -5, -5, -2, 2, 5, 5, 2, -2, -5 },
                9
        );
        Cannonball cannonball = new Cannonball(cannonballShape, x, y, Color.BLACK, new Vector2f(0, -2.5f));
        cannonball.setScale(scale, scale);
        cannonballs.add(cannonball);
    }
}
