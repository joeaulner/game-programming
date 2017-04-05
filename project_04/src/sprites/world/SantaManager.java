package sprites.world;

import sprites.util.KeyboardInput;
import sprites.util.Matrix3x3f;
import sprites.util.Vector2f;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static sprites.world.SantaManager.SpriteList.IDLE;
import static sprites.world.SantaManager.SpriteList.RUNNING;

public class SantaManager {

    private final float spriteScale = 0.25f;
    private SpriteObject santa;
    private Vector2f velocity;
    private boolean renderBoundingBoxes;

    private String[][] spriteLists;
    private int currentSpriteList = 0;
    private int currentSprite;
    private float spriteChangeDelta = 0.0f;

    protected enum SpriteList {
        IDLE(0), RUNNING(1);

        private int num;

        SpriteList(int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }
    }

    private static SantaManager instance = new SantaManager();
    public static SantaManager getInstance() {
        return instance;
    }

    private SantaManager() {

    }

    public void initialize() {
        velocity = new Vector2f(0, 0);

        spriteLists = new String[2][];
        spriteLists[0] = new String[17];
        for (int i = 1; i < 17; ++i) {
            spriteLists[IDLE.getNum()][i] = String.format("assets/santa/Idle (%d).png", i);
        }
        spriteLists[1] = new String[12];
        for (int i = 1; i < 12; ++i) {
            spriteLists[RUNNING.getNum()][i] = String.format("assets/santa/Run (%d).png", i);
        }
        currentSprite = 1;
        santa = new SpriteObject(spriteLists[0][1], 0, 0.5f, 0.25f, 0.4f);
        santa.setScale(spriteScale, spriteScale);
    }

    public void setRenderBoundingBoxes(boolean renderBoundingBoxes) {
        this.renderBoundingBoxes = renderBoundingBoxes;
    }

    public void processInput(KeyboardInput keyboard, float delta) {
        float runRate = 0.025f;
        boolean runLeft = keyboard.keyDown(KeyEvent.VK_LEFT);
        boolean runRight = keyboard.keyDown(KeyEvent.VK_RIGHT);
        if (runLeft && velocity.x >= 0) {
            velocity.x = -runRate;

            currentSpriteList = RUNNING.getNum();
            currentSprite = 1;
            santa.setScale(-spriteScale, spriteScale);
            santa.setSprite(spriteLists[currentSpriteList][currentSprite]);
            spriteChangeDelta = 0;
        } else if (runRight && velocity.x <= 0) {
            velocity.x = runRate;

            currentSpriteList = RUNNING.getNum();
            currentSprite = 1;
            santa.setScale(spriteScale, spriteScale);
            santa.setSprite(spriteLists[currentSpriteList][currentSprite]);
            spriteChangeDelta = 0;
        } else if (!runLeft && !runRight) {
            velocity.x = 0;
        }
    }

    public void update(float gravity, Matrix3x3f viewport, float delta) {
        // apply gravity to location
        velocity.y += gravity * delta;
        Vector2f newLoc = santa.getLocation().add(velocity);
        santa.setlocation(newLoc.x, newLoc.y);

        // perform collision detection
        ArrayList<SpriteObject> tiles = TileManager.getInstance().getTiles();
        for (SpriteObject tile : tiles) {
            if (santa.intersects(tile)) {
                Vector2f tileLoc = tile.getLocation();
                // prevent falling through a tile from the top by resetting y-coord
                if (newLoc.y + santa.height / 2 > tileLoc.y) {
                    float y = tileLoc.y + tile.height / 2 + santa.height / 2;
                    santa.setlocation(newLoc.x, y);
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
        float spriteChangeRate = 0.0575f;

        if (velocity.y < 0) {
            santa.setSprite("assets/santa/Jump (13).png");
            return;
        }

        if (velocity.y == 0 && velocity.x == 0 && currentSpriteList != IDLE.getNum()) {
            currentSpriteList = IDLE.getNum();
            currentSprite = 1;
        }

        spriteChangeDelta += delta;
        if (spriteChangeDelta > spriteChangeRate) {
            spriteChangeDelta = 0;
            currentSprite++;
            if (spriteLists[currentSpriteList].length - 1 < currentSprite) {
                currentSprite = 1;
            }
            santa.setSprite(spriteLists[currentSpriteList][currentSprite]);
        }
    }
}
