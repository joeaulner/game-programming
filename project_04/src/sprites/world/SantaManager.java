package sprites.world;

import sprites.util.Matrix3x3f;
import sprites.util.Vector2f;

import java.awt.*;

public class SantaManager {

    private Santa santa;
    private Vector2f velocity;
    private boolean renderBoundingBoxes;

    private static SantaManager instance = new SantaManager();
    public static SantaManager getInstance() {
        return instance;
    }

    private SantaManager() {
        santa = new Santa();
        velocity = new Vector2f(0, 0);
    }

    public void setRenderBoundingBoxes(boolean renderBoundingBoxes) {
        this.renderBoundingBoxes = renderBoundingBoxes;
    }

    public void update(float gravity, Matrix3x3f viewport, float delta) {
        float vY = velocity.y += gravity * delta;
        velocity.add(new Vector2f(0, vY));
        SpriteObject sprite = santa.getSprite();
        Vector2f newLoc = sprite.getLocation().add(velocity);
        sprite.setlocation(newLoc.x, newLoc.y);

        sprite.setViewport(viewport);
        sprite.updateWorld();
        if (renderBoundingBoxes) {
            VectorObject boundingBox = santa.getBoundingBox();
            boundingBox.setLocation(newLoc.x, newLoc.y);
            boundingBox.setViewport(viewport);
            boundingBox.updateWorld();
        }

        santa.cycleImage(delta);
    }

    public void render(Graphics g) {
        santa.getSprite().render(g);

        if (renderBoundingBoxes) {
            santa.getBoundingBox().render(g);
        }
    }
}
