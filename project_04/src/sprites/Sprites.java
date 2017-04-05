package sprites;

import sprites.util.SimpleFramework;
import sprites.world.*;

import java.awt.*;
import java.awt.event.KeyEvent;


public class Sprites extends SimpleFramework {

    private TileManager tileManager;
    private SantaManager santaManager;

    private boolean renderBoundingBoxes = true;
    private float gravity = -0.0125f;

    public Sprites() {
        appWidth = 1280;
        appHeight = 640;
        appBorderScale = 1.0f;
        appWorldWidth = 4;
        appWorldHeight = 2;
        appMaintainRatio = true;
    }

    @Override
    protected void initialize() {
        super.initialize();

        tileManager = TileManager.getInstance();
        tileManager.setRenderBoundingBoxes(renderBoundingBoxes);

        santaManager = SantaManager.getInstance();
        santaManager.setRenderBoundingBoxes(renderBoundingBoxes);
    }

    @Override
    protected void processInput(float delta) {
        super.processInput(delta);
        if (keyboard.keyDownOnce(KeyEvent.VK_B)) {
            renderBoundingBoxes = !renderBoundingBoxes;
            tileManager.setRenderBoundingBoxes(renderBoundingBoxes);
            santaManager.setRenderBoundingBoxes(renderBoundingBoxes);
        }
    }

    @Override
    protected void updateObjects(float delta) {
        super.updateObjects(delta);
        tileManager.update(getViewportTransform());
        santaManager.update(gravity, getViewportTransform(), delta);
    }

    @Override
    protected void render(Graphics g) {
        super.render(g);
        tileManager.render(g);
        santaManager.render(g);
    }

    public static void main(String[] args) {
        launchApp(new Sprites());
    }
}
