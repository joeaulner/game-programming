package sprites.world;

import sprites.util.Vector2f;

import java.awt.*;

public class Tile {

    private SpriteObject sprite;
    private VectorObject boundingBox;

    public Tile(float x, float y, TileType tileType) {
        String filename;
        switch (tileType) {
            case LEFT:
                filename = "assets/tiles/1.png";
                break;
            case RIGHT:
                filename = "assets/tiles/3.png";
                break;
            case CENTER:
                filename = "assets/tiles/2.png";
                break;
            default:
                filename = null;
        }
        sprite = new SpriteObject(filename, x, y);

        boundingBox = new VectorObject(
                new Vector2f[] {
                        new Vector2f(-1, 1),
                        new Vector2f(1, 1),
                        new Vector2f(1, -1),
                        new Vector2f(-1, -1)
                },
                x, y, Color.BLUE
        );
        boundingBox.setScale(0.1f, 0.1f);
    }

    public SpriteObject getSprite() {
        return sprite;
    }

    public VectorObject getBoundingBox() {
        return boundingBox;
    }
}
