package cannonball.world;

import cannonball.util.Matrix3x3f;

import java.awt.*;
import java.util.ArrayList;

public class CityManager {

    private ArrayList<VectorObject> cities;
    private final float scale = 0.5f;

    private static CityManager instance = new CityManager();

    private CityManager() {
        cities = new ArrayList<>();
    }

    public static CityManager getInstance() {
        return instance;
    }

    public void initialize() {
        for (int x = -18; x <= 18; x += 4) {
            spawnCity(x, -10, null);
        }
    }

    public ArrayList<VectorObject> getCities() {
        return cities;
    }

    public void update(Matrix3x3f viewport) {
        ArrayList<Cannonball> cannonballs = CannonballManager.getInstance().getCannonballs();
        ArrayList<VectorObject> destroyedObjects = new ArrayList<>();
        final int rad = 4;

        for (VectorObject city : cities) {
            city.setViewport(viewport);
            city.updateWorld();

            Point.Float cityLoc = city.getLocation();
            for (VectorObject cannonball : cannonballs) {
                Point.Float ballLoc = cannonball.getLocation();
                if (Math.abs(cityLoc.x - ballLoc.x) < (rad * scale) && ballLoc.y <= (-10.5 + rad * scale)) {
                    destroyedObjects.add(city);
                    destroyedObjects.add(cannonball);
                }
            }
        }

        for (VectorObject destroyedObject : destroyedObjects) {
            cities.remove(destroyedObject);
            cannonballs.remove(destroyedObject);
        }
    }

    public void render(Graphics g) {
        for (VectorObject city : cities) {
            city.render(g);
        }
    }

    private void spawnCity(int x, int y, Matrix3x3f viewport) {
        Shape cityShape = new Polygon(
                new int[] { -4, -4, -2, -2, 0, 0, 2, 2, 4, 4, -4 },
                new int[] { 0, 3, 2, 3, 2, 3, 2, 4, 4, 0, 0 },
                11
        );
        VectorObject city = new VectorObject(cityShape, x, y, Color.BLUE, viewport);
        city.setScale(scale, scale);
        cities.add(city);
    }
}
