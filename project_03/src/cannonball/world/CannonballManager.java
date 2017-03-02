package cannonball.world;

import cannonball.util.Matrix3x3f;
import cannonball.util.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * A manager for the cannonball objects threatening the cities the player
 * is defending. Each cannonball is represented by the Cannonball class,
 * which is simply a VectorObject with an additional velocity vector.
 * This manager is responsible for updating cannonball positions,
 * spawning new cannonballs, rendering the cannonballs, and removing
 * cannonballs that fall/drift out of the world.
 */
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

    /**
     * Internal constructor used to create the Singleton instance.
     * Initialize random and the empty cannonballs ArrayList.
     */
    private CannonballManager() {
        random = new Random();
        cannonballs = new ArrayList<>();
    }

    /**
     * Returns the single instance of this class.
     * @return The CannonballManager instance.
     */
    public static CannonballManager getInstance() {
        return instance;
    }

    /**
     * Resets the cannonballDestroyed and the wind and spawn values.
     */
    public void initialize() {
        windVelocity = 0.0f;
        windVelocityDelta = 0.3f;
        spawnRate = timeSinceSpawn = 2.0f;
        cannonballDestroyed = false;
    }

    /**
     * Clears the cannonballs list when the game is over.
     */
    public void onGameOver() {
        cannonballs.clear();
    }

    /**
     * Returns the cannonballs managed by this class.
     * @return The list of cannonballs.
     */
    public ArrayList<Cannonball> getCannonballs() {
        return cannonballs;
    }

    /**
     * Returns the current wind velocity.
     * @return The wind velocity.
     */
    public float getWindVelocity() {
        return windVelocity;
    }

    /**
     * Sets the position of a mouse click for use during collision detection.
     * @param clickPos The mouse position transformed to the viewport.
     */
    public void setClickPos(Vector2f clickPos) {
        this.clickPos = clickPos;
    }

    /**
     * Returns whether a cannonball was destroyed during the current frame.
     * @return Whether a cannonball was destroyed.
     */
    public boolean getCannonballDestroyed() {
        return cannonballDestroyed;
    }

    /**
     * Updates the Cannonball objects by setting their viewport matrix and
     * applying vertical and horizontal accelerations to their velocity.
     * This function also adjusts the wind velocity, removes cannonballs
     * outside the bounds of the world, and spawns new cannonballs.
     * @param delta The time elapsed since the last frame.
     * @param viewport The matrix used to scale/translate the object in relation to world viewport.
     */
    public void update(float delta, Matrix3x3f viewport) {
        ArrayList<Cannonball> toRemove = new ArrayList<>();
        cannonballDestroyed = false;

        for (Cannonball cannonball : cannonballs) {
            // Apply wind velocity and reduced gravity to cannonball velocity
            Vector2f vel = cannonball.getVelocity();
            vel.x += windVelocity * delta;
            vel.y += -1.5f * delta;

            // Apply cannonball velocity to current position
            Point.Float pos = cannonball.getLocation();
            pos.x = pos.x + vel.x * delta;
            pos.y = pos.y + vel.y * delta;

            // Remove cannonballs outside the bounds of the game world
            if (pos.y < -10 || Math.abs(pos.x) > 20) {
                toRemove.add(cannonball);
                continue;
            }
            /*
            Remove cannonballs when click event falls within its bound.
            Pseudo-collision detection is being used here by comparing the
            click event location to the square bounds of the cannonball
            calculated using its radius and scale.
             */
            float radius = 5 * scale;
            if (clickPos != null &&
                    Math.abs(clickPos.x - pos.x) < radius &&
                    Math.abs(clickPos.y - pos.y) < radius) {
                toRemove.add(cannonball);
                cannonballDestroyed = true;
            }
            cannonball.setViewport(viewport);
            cannonball.updateWorld();
        }
        clickPos = null;

        /*
        Remove cannonballs flagged for deletion during collision detection.
        This is stored in a collection because overlapping cannonballs are possible.
         */
        for (Cannonball objectToRemove : toRemove) {
            cannonballs.remove(objectToRemove);
        }

        /*
        Spawn a new cannonball every spawnRate seconds just above
        the upper bounds of the viewport at a random x-coordinate.
         */
        timeSinceSpawn += delta;
        if (timeSinceSpawn >= spawnRate) {
            int x = random.nextInt(41) - 20;
            spawnCannonball(x, 11);
            timeSinceSpawn = 0.0f;
        }
        /*
        Decrease time between cannonball spawns as time goes on,
        with a minimum of 0.25 seconds between spawns.
         */
        float dSpawnRate = -0.025f * delta;
        spawnRate = Math.max(0.25f, spawnRate + dSpawnRate);

        /*
        Oscillate the wind velocity between -2.5 and 2.5 units/second.
         */
        float dWindVelocity = windVelocityDelta * delta;
        windVelocity += dWindVelocity;
        if (windVelocity >= 2.5f) {
            windVelocityDelta = -Math.abs(windVelocityDelta);
        } else if (windVelocity <= -2.5f) {
            windVelocityDelta = Math.abs(windVelocityDelta);
        }
    }

    /**
     * Render each cannonball object managed by this class.
     * @param g The Graphics object used to render the Cannonballs.
     */
    public void render(Graphics g) {
        for (VectorObject cannonball : cannonballs) {
            cannonball.render(g);
        }
    }

    /**
     * Spawn a new cannonball at (x,y) coordinates.
     * @param x The x-coordinate of the cannonball.
     * @param y The y-coordinate of the cannonball.
     */
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
