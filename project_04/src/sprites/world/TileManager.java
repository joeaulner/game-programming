package sprites.world;

import sprites.util.Matrix3x3f;

import java.awt.*;
import java.util.ArrayList;

public class TileManager {

    private enum TileType {
        TOP_LEFT("1.png"), TOP_CENTER("2.png"), TOP_RIGHT("3.png"),
        LEFT("4.png"), CENTER("5.png"), RIGHT("6.png");

        private String filename;

        TileType(String filename) {
            this.filename = filename;
        }

        public String getFilename() {
            return filename;
        }
    }

    private ArrayList<SpriteObject> tiles;
    private boolean renderBoundingBoxes;

    private static TileManager instance = new TileManager();
    public static TileManager getInstance() {
        return instance;
    }

    private TileManager() {
        tiles = new ArrayList<>();

        addTile(TileType.TOP_LEFT, -1.4f, -0.5f);
        for (float x = -1.2f; x <= 0.8f; x += 0.2) {
            addTile(TileType.TOP_CENTER, x, -0.5f);
        }
        for (float y = -0.7f; y >= -1; y -= 0.2f) {
            addTile(TileType.LEFT, -1.4f, y);
            for (float x = -1.2f; x <= 0.8f; x += 0.2) {
                addTile(TileType.CENTER, x, y);
            }
            addTile(TileType.RIGHT, 1, y);
        }
        addTile(TileType.TOP_RIGHT, 1, -0.5f);
        for (float y = -1; y <= 1; y += 0.2f) {
            addTile(TileType.RIGHT, -2, y);
            addTile(TileType.LEFT, 2, y);
        }
    }

    private void addTile(TileType tileType, float x, float y) {
        String filename = String.format("assets/tiles/%s", tileType.filename);
        SpriteObject sprite = new SpriteObject(filename, x, y, 0.2f, 0.2f);
        sprite.setScale(0.5f, 0.5f);
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
        int i = 0;
        for (SpriteObject sprite : tiles) {
            i++;
            sprite.render(g);
            if (renderBoundingBoxes) {
                sprite.renderBoundingBox(g);
            }
        }
    }
}
