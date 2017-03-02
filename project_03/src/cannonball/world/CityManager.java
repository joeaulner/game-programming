package cannonball.world;

import cannonball.util.Matrix3x3f;

import java.awt.*;
import java.util.ArrayList;

/**
 * A manager for the city blocks the player is defending. Each city block
 * is represented by a VectorObject, and is destroyed upon a collision
 * with a cannonball. The Singleton pattern was used to ensure other world
 * object managers would have access to the cities should they need it.
 */
public class CityManager {

    private ArrayList<VectorObject> cities;
    private final float scale = 0.5f;

    private static CityManager instance = new CityManager();

    /**
     * Internal constructor used to create the Singleton instance.
     * Initializes the empty cities ArrayList.
     */
    private CityManager() {
        cities = new ArrayList<>();
    }

    /**
     * Returns the single instance of this class.
     * @return The CityManager instance.
     */
    public static CityManager getInstance() {
        return instance;
    }

    /**
     * Generates cities located across the bottom of the world.
     */
    public void initialize() {
        for (int x = -18; x <= 18; x += 4) {
            spawnCity(x, -10);
        }
    }

    /**
     * Returns the cities ArrayList.
     * @return The cities ArrayList.
     */
    public ArrayList<VectorObject> getCities() {
        return cities;
    }

    /**
     * Updates the city VectorObjects by setting their viewport matrix and
     * checking for collisions with cannonballs from the CannonballManager.
     * @param viewport The matrix used to scale/translate the object in relation to the world viewport.
     */
    public void update(Matrix3x3f viewport) {
        ArrayList<Cannonball> cannonballs = CannonballManager.getInstance().getCannonballs();
        ArrayList<VectorObject> destroyedObjects = new ArrayList<>();
        final int rad = 4;

        for (VectorObject city : cities) {
            city.setViewport(viewport);
            city.updateWorld();

            /*
            Check each cannonball for a collision with the current city.
            Pseudo-collision detection is being used here by comparing the
            center of the cannonball with the left, right, and top bounds
            of the city. The top bound is shifted slightly lower to better
            match the appearance of the drawn model.
             */
            Point.Float cityLoc = city.getLocation();
            for (VectorObject cannonball : cannonballs) {
                Point.Float ballLoc = cannonball.getLocation();
                if (Math.abs(cityLoc.x - ballLoc.x) < (rad * scale) && ballLoc.y <= (-10.5 + rad * scale)) {
                    destroyedObjects.add(city);
                    destroyedObjects.add(cannonball);
                }
            }
        }

        // Remove all cities and cannonballs flagged for deletion during the collision detection phase.
        for (VectorObject destroyedObject : destroyedObjects) {
            cities.remove(destroyedObject);
            cannonballs.remove(destroyedObject);
        }
    }

    /**
     * Render each city object managed by this class.
     * @param g The Graphics object used to render the VectorObjects.
     */
    public void render(Graphics g) {
        for (VectorObject city : cities) {
            city.render(g);
        }
    }

    /**
     * Spawn a new city VectorObject at (x,y) coordinates.
     * @param x The x-coordinate of the city.
     * @param y The y-coordinate of the city.
     */
    private void spawnCity(int x, int y) {
        Shape cityShape = new Polygon(
                new int[] { -4, -4, -2, -2, 0, 0, 2, 2, 4, 4, -4 },
                new int[] { 0, 3, 2, 3, 2, 3, 2, 4, 4, 0, 0 },
                11
        );
        VectorObject city = new VectorObject(cityShape, x, y, Color.BLUE);
        city.setScale(scale, scale);
        cities.add(city);
    }
}
