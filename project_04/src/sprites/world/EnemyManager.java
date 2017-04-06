package sprites.world;

import sprites.util.Matrix3x3f;
import sprites.util.Vector2f;

import java.awt.*;

public class EnemyManager {

    private float spriteScale = 0.5f;
    private SpriteObject sprite;
    private Vector2f velocity;
    private boolean renderBoundingBoxes;

    private int direction = 1;
    private float leftBound = -1.25f;
    private float rightBound = 0;

    private static EnemyManager instance = new EnemyManager();
    public static EnemyManager getInstance() {
        return instance;
    }

    private EnemyManager() {

    }

    public void initialize() {
        velocity = new Vector2f(0, 0);
        sprite = new SpriteObject("assets/objects/SnowMan.png", -1.25f, -0.23f, 0.2f, 0.33f);
        sprite.setScale(spriteScale, spriteScale);
    }

    public void setRenderBoundingBoxes(boolean renderBoundingBoxes) {
        this.renderBoundingBoxes = renderBoundingBoxes;
    }

    public SpriteObject getSprite() {
        return sprite;
    }

    public void update(Matrix3x3f viewport, float delta) {
        float dX = delta * 0.25f * direction;
        Vector2f newLoc = sprite.getLocation().add(new Vector2f(dX, 0));
        sprite.setLocation(newLoc);

        if (newLoc.x > rightBound) {
            direction = -1;
            sprite.setScale(-spriteScale, spriteScale);
        } else if (newLoc.x < leftBound) {
            direction = 1;
            sprite.setScale(spriteScale, spriteScale);
        }

        sprite.setViewport(viewport);
        sprite.updateWorld();
    }

    public void render(Graphics g) {
        sprite.render(g);
        if (renderBoundingBoxes) {
            sprite.renderBoundingBox(g);
        }
    }
}
