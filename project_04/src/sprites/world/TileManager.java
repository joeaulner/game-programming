package sprites.world;

import sprites.util.Matrix3x3f;

import java.awt.*;
import java.util.ArrayList;

public class TileManager {

    private ArrayList<Tile> tiles;
    private boolean renderBoundingBoxes;

    private static TileManager instance = new TileManager();
    public static TileManager getInstance() {
        return instance;
    }

    private TileManager() {
        tiles = new ArrayList<>();
        tiles.add(new Tile(-0.6f, -0.5f, TileType.LEFT));
        tiles.add(new Tile(-0.4f, -0.5f, TileType.CENTER));
        tiles.add(new Tile(-0.2f, -0.5f, TileType.CENTER));
        tiles.add(new Tile(0, -0.5f, TileType.CENTER));
        tiles.add(new Tile(0.2f, -0.5f, TileType.CENTER));
        tiles.add(new Tile(0.4f, -0.5f, TileType.CENTER));
        tiles.add(new Tile(0.6f, -0.5f, TileType.RIGHT));
    }

    public void setRenderBoundingBoxes(boolean renderBoundingBoxes) {
        this.renderBoundingBoxes = renderBoundingBoxes;
    }

    public void update(Matrix3x3f viewport) {
        for (Tile tile : tiles) {
            SpriteObject sprite = tile.getSprite();
            sprite.setViewport(viewport);
            sprite.updateWorld();

            if (renderBoundingBoxes) {
                VectorObject boundingBox = tile.getBoundingBox();
                boundingBox.setViewport(viewport);
                boundingBox.updateWorld();
            }
        }
    }

    public void render(Graphics g) {
        for (Tile tile : tiles) {
            tile.getSprite().render(g);

            if (renderBoundingBoxes) {
                tile.getBoundingBox().render(g);
            }
        }
    }
}
