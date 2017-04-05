package sprites.world;

import sprites.util.Matrix3x3f;

import java.awt.*;
import java.util.ArrayList;

public class TileManager {

    private enum TileType {
        LEFT, RIGHT, CENTER
    }

    private ArrayList<SpriteObject> tiles;
    private boolean renderBoundingBoxes;

    private static TileManager instance = new TileManager();
    public static TileManager getInstance() {
        return instance;
    }

    private TileManager() {
        tiles = new ArrayList<>();
        addTile(TileType.LEFT, -0.6f, -0.5f);
        addTile(TileType.CENTER, -0.4f, -0.5f);
        addTile(TileType.CENTER, -0.2f, -0.5f);
        addTile(TileType.CENTER, 0, -0.5f);
        addTile(TileType.CENTER, 0.2f, -0.5f);
        addTile(TileType.CENTER, 0.4f, -0.5f);
        addTile(TileType.RIGHT, 0.6f, -0.5f);
    }

    private void addTile(TileType tileType, float x, float y) {
        String filename = "assets/tiles/";
        switch (tileType) {
            case LEFT:
                filename += "1.png";
                break;
            case CENTER:
                filename += "2.png";
                break;
            case RIGHT:
                filename += "3.png";
                break;
        }
        SpriteObject sprite = new SpriteObject(filename, 0.5f, x, y, 0.2f, 0.2f);
        tiles.add(sprite);
    }

    public ArrayList<SpriteObject> getTiles() {
        return tiles;
    }

    public void setRenderBoundingBoxes(boolean renderBoundingBoxes) {
        this.renderBoundingBoxes = renderBoundingBoxes;
    }

    public void update(Matrix3x3f viewport) {
        for (SpriteObject sprite : tiles) {
            sprite.setViewport(viewport);
            sprite.updateWorld();
        }
    }

    public void render(Graphics g) {
        for (SpriteObject sprite : tiles) {
            sprite.render(g);
            if (renderBoundingBoxes) {
                sprite.renderBoundingBox(g);
            }
        }
    }
}
