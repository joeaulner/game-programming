package sprites.world;

import sprites.util.KeyboardInput;
import sprites.util.Matrix3x3f;
import sprites.util.Vector2f;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static sprites.world.HeroManager.SpriteList.*;

public class HeroManager {

    private final float spriteScale = 0.25f;
    private SpriteObject sprite;
    private Vector2f velocity;
    private boolean renderBoundingBoxes;

    private String[][] spriteLists;
    private int currentSpriteList;
    private int currentSprite;
    private float spriteChangeDelta;

    protected enum SpriteList {
        IDLE(0), RUNNING(1), JUMPING(2), DYING(3);

        private int num;

        SpriteList(int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }
    }

    private static HeroManager instance = new HeroManager();
    public static HeroManager getInstance() {
        return instance;
    }

    private HeroManager() {

    }

    public void initialize() {
        currentSpriteList = 0;
        spriteChangeDelta = 0;
        velocity = new Vector2f(0, 0);

        spriteLists = new String[SpriteList.values().length][];
        spriteLists[IDLE.getNum()] = new String[17];
        for (int i = 1; i < 17; ++i) {
            spriteLists[IDLE.getNum()][i] = String.format("assets/santa/Idle (%d).png", i);
        }
        spriteLists[RUNNING.getNum()] = new String[12];
        for (int i = 1; i < 12; ++i) {
            spriteLists[RUNNING.getNum()][i] = String.format("assets/santa/Run (%d).png", i);
        }
        spriteLists[JUMPING.getNum()] = new String[13];
        for (int i = 1; i < 13; ++i) {
            spriteLists[JUMPING.getNum()][i] = String.format("assets/santa/Jump (%d).png", i);
        }
        spriteLists[DYING.getNum()] = new String[17];
        for (int i = 1; i < 17; ++i) {
            spriteLists[DYING.getNum()][i] = String.format("assets/santa/Dead (%d).png", i);
        }
        currentSprite = 1;
        sprite = new SpriteObject(spriteLists[0][1], 0, 0.5f, 0.25f, 0.4f);
        sprite.setScale(spriteScale, spriteScale);
    }

    public void setRenderBoundingBoxes(boolean renderBoundingBoxes) {
        this.renderBoundingBoxes = renderBoundingBoxes;
    }

    public void processInput(KeyboardInput keyboard, float delta) {
        if (currentSpriteList == DYING.getNum()) {
            return;
        }

        float runRate = 0.025f;
        boolean runLeft = keyboard.keyDown(KeyEvent.VK_LEFT);
        boolean runRight = keyboard.keyDown(KeyEvent.VK_RIGHT);
        if (runLeft && velocity.x >= 0) {
            velocity.x = -runRate;

            sprite.setScale(-spriteScale, spriteScale);
            if (currentSpriteList != JUMPING.getNum()) {
                currentSpriteList = RUNNING.getNum();
                currentSprite = 1;
                sprite.setSprite(spriteLists[currentSpriteList][currentSprite]);
                spriteChangeDelta = 0;
            }
        } else if (runRight && velocity.x <= 0) {
            velocity.x = runRate;

            sprite.setScale(spriteScale, spriteScale);
            if (currentSpriteList != JUMPING.getNum()) {
                currentSpriteList = RUNNING.getNum();
                currentSprite = 1;
                sprite.setSprite(spriteLists[currentSpriteList][currentSprite]);
                spriteChangeDelta = 0;
            }
        } else if (!runLeft && !runRight) {
            velocity.x = 0;
        }

        if (keyboard.keyDownOnce(KeyEvent.VK_UP) && velocity.y == 0) {
            velocity.y = 0.04f;

            currentSpriteList = JUMPING.getNum();
            currentSprite = 1;
            sprite.setSprite(spriteLists[currentSpriteList][currentSprite]);
            spriteChangeDelta = 0;
        }
    }

    public void update(float gravity, Matrix3x3f viewport, float delta) {
        if (currentSpriteList == DYING.getNum()) {
            cycleImage(delta);
            return;
        }

        // apply gravity to location
        velocity.y += gravity * delta;
        Vector2f oldLoc = sprite.getLocation();
        Vector2f newLoc = oldLoc.add(velocity);
        sprite.setLocation(newLoc.x, newLoc.y);

        // perform collision detection
        ArrayList<SpriteObject> tiles = TileManager.getInstance().getTiles();
        for (SpriteObject tile : tiles) {
            if (sprite.intersects(tile)) {
                Vector2f tileLoc = tile.getLocation();
                // prevent falling through a tile from the top by resetting y-coord
                if (newLoc.y > tileLoc.y && oldLoc.y - sprite.height / 2 >= tileLoc.y + tile.height / 2) {
                    float y = tileLoc.y + tile.height / 2 + sprite.height / 2;
                    sprite.setLocation(newLoc.x, y);
                    velocity.y = 0;

                    if (velocity.x != 0) {
                        currentSpriteList = RUNNING.getNum();
                    }
                } else if (newLoc.x > tileLoc.x && oldLoc.x - sprite.width / 2 >= tileLoc.x + tile.width / 2) {
                    float x = tileLoc.x + tile.width / 2 + sprite.width / 2;
                    sprite.setLocation(x, newLoc.y);
                    velocity.x = 0;
                } else if (newLoc.x < tileLoc.x && oldLoc.x + sprite.width / 2 <= tileLoc.x - tile.width / 2) {
                    float x = tileLoc.x - tile.width / 2 - sprite.width / 2;
                    sprite.setLocation(x, newLoc.y);
                    velocity.x = 0;
                }
            }
        }

        if (EnemyManager.getInstance().getSprite().intersects(sprite)) {
            currentSpriteList = DYING.getNum();
        }

        sprite.setViewport(viewport);
        sprite.updateWorld();

        udpateImage(delta);
    }

    public void render(Graphics g) {
        sprite.render(g);
        if (renderBoundingBoxes) {
            sprite.renderBoundingBox(g);
        }
    }

    private void udpateImage(float delta) {
        // use idle sprites when there is no horizontal or vertical movement
        if (velocity.y == 0 && velocity.x == 0 && currentSpriteList != IDLE.getNum()) {
            currentSpriteList = IDLE.getNum();
            currentSprite = 1;
        }

        cycleImage(delta);
    }

    private void cycleImage(float delta) {
        float spriteChangeRate = 0.0575f;

        // cycle through files in active sprites list
        spriteChangeDelta += delta;
        if (spriteChangeDelta > spriteChangeRate) {
            spriteChangeDelta = 0;
            currentSprite++;
            if (spriteLists[currentSpriteList].length - 1 < currentSprite) {
                if (currentSpriteList == JUMPING.getNum()) {
                    currentSprite = spriteLists[currentSpriteList].length - 1;
                } else if (currentSpriteList == DYING.getNum()) {
                    currentSprite = spriteLists[currentSpriteList].length - 1;
                } else {
                    currentSprite = 1;
                }
            }
            sprite.setSprite(spriteLists[currentSpriteList][currentSprite]);
        }
    }
}
