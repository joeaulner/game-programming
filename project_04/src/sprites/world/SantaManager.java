package sprites.world;

import sprites.util.Matrix3x3f;
import sprites.util.Vector2f;

import java.awt.*;
import java.util.ArrayList;

public class SantaManager {

    private SpriteObject santa;
    private Vector2f velocity;
    private boolean renderBoundingBoxes;

    private ArrayList<String> idleSprites;
    private int currentSprite = 0;
    private float spriteChangeDelta = 0.0f;

    private static SantaManager instance = new SantaManager();
    public static SantaManager getInstance() {
        return instance;
    }

    private SantaManager() {
        idleSprites = new ArrayList<>();
        for (int i = 1; i < 17; ++i) {
            idleSprites.add(String.format("assets/santa/Idle (%d).png", i));
        }

        currentSprite = 0;
        santa = new SpriteObject(idleSprites.get(currentSprite), 0.25f, 0, 0, 0.25f, 0.4f);

        velocity = new Vector2f(0, 0);
    }

    public void setRenderBoundingBoxes(boolean renderBoundingBoxes) {
        this.renderBoundingBoxes = renderBoundingBoxes;
    }

    public void update(float gravity, Matrix3x3f viewport, float delta) {
        // apply gravity to location
        float vY = velocity.y += gravity * delta;
        velocity.add(new Vector2f(0, vY));
        Vector2f oldLoc = santa.getLocation();
        Vector2f newLoc = santa.getLocation().add(velocity);
        santa.setlocation(newLoc.x, newLoc.y);

        ArrayList<SpriteObject> tiles = TileManager.getInstance().getTiles();
        for (SpriteObject tile : tiles) {
            if (santa.intersects(tile)) {
                // prevent falling through a tile from the top by resetting y-coord
                if (newLoc.y + santa.height / 2 > tile.getLocation().y) {
                    santa.setlocation(newLoc.x, oldLoc.y);
                    velocity.y = 0;
                }
            }
        }

        santa.setViewport(viewport);
        santa.updateWorld();

        cycleImage(delta);
    }

    public void render(Graphics g) {
        santa.render(g);
        if (renderBoundingBoxes) {
            santa.renderBoundingBox(g);
        }
    }

    private void cycleImage(float delta) {
        float spriteChangeRate = 0.05f;

        spriteChangeDelta += delta;
        if (spriteChangeDelta > spriteChangeRate) {
            spriteChangeDelta = 0;
            currentSprite++;
            if (idleSprites.size() - 1 < currentSprite) {
                currentSprite = 0;
            }
            santa.setSprite(idleSprites.get(currentSprite));
        }
    }
}
