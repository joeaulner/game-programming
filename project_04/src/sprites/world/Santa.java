package sprites.world;

import sprites.util.Vector2f;

import java.awt.*;
import java.util.ArrayList;

public class Santa {

    private ArrayList<String> idleSprites;
    private int currentSprite = 0;

    private SpriteObject sprite;
    private VectorObject boundingBox;

    private final float spriteChangeRate = 0.05f;
    private float spriteChangeDelta = 0.0f;

    private Vector2f velocity = new Vector2f(0, 0);

    public Santa() {
        idleSprites = new ArrayList<>();
        for (int i = 1; i < 17; ++i) {
            idleSprites.add(String.format("assets/santa/Idle (%d).png", i));
        }

        currentSprite = 0;
        sprite = new SpriteObject(idleSprites.get(currentSprite), 0, 0);
        sprite.setSpriteScale(0.25f);

        boundingBox = new VectorObject(
                new Vector2f[] {
                        new Vector2f(-1, 1.05f),
                        new Vector2f(1, 1.05f),
                        new Vector2f(1, -0.95f),
                        new Vector2f(-1, -0.95f)
                },
                0, 0, Color.BLUE
        );
        boundingBox.setScale(0.15f, 0.2f);
    }

    public SpriteObject getSprite() {
        return sprite;
    }

    public VectorObject getBoundingBox() {
        return boundingBox;
    }

    public Vector2f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2f velocity) {
        this.velocity = velocity;
    }

    public void cycleImage(float delta) {
        // cycleImage the image displayed
        spriteChangeDelta += delta;
        if (spriteChangeDelta > spriteChangeRate) {
            spriteChangeDelta = 0;
            currentSprite++;
            if (idleSprites.size() - 1 < currentSprite) {
                currentSprite = 0;
            }
            sprite.setSprite(idleSprites.get(currentSprite));
        }
    }
}
