package sprites;

import sprites.util.Matrix3x3f;
import sprites.util.SimpleFramework;
import sprites.world.*;

import java.awt.*;
import java.awt.event.KeyEvent;


public class Sprites extends SimpleFramework {

    private TileManager tileManager;
    private HeroManager heroManager;
    private EnemyManager enemyManager;

    private boolean renderBoundingBoxes = true;
    private float gravity = -0.04f;

    public Sprites() {
        appWidth = 1280;
        appHeight = 640;
        appBorderScale = 1.0f;
        appWorldWidth = 4;
        appWorldHeight = 2;
        appMaintainRatio = true;
        appFPSColor = Color.BLACK;
        appBackground = Color.LIGHT_GRAY;
    }

    @Override
    protected void initialize() {
        super.initialize();

        renderBoundingBoxes = false;

        tileManager = TileManager.getInstance();
        tileManager.setRenderBoundingBoxes(renderBoundingBoxes);

        heroManager = HeroManager.getInstance();
        heroManager.setRenderBoundingBoxes(renderBoundingBoxes);
        heroManager.initialize();

        enemyManager = EnemyManager.getInstance();
        enemyManager.setRenderBoundingBoxes(renderBoundingBoxes);
        enemyManager.initialize();
    }

    @Override
    protected void processInput(float delta) {
        super.processInput(delta);
        if (keyboard.keyDownOnce(KeyEvent.VK_SPACE)) {
            initialize();
        }
        if (keyboard.keyDownOnce(KeyEvent.VK_B)) {
            renderBoundingBoxes = !renderBoundingBoxes;
            tileManager.setRenderBoundingBoxes(renderBoundingBoxes);
            heroManager.setRenderBoundingBoxes(renderBoundingBoxes);
            enemyManager.setRenderBoundingBoxes(renderBoundingBoxes);
        }
        heroManager.processInput(keyboard, delta);
    }

    @Override
    protected void updateObjects(float delta) {
        super.updateObjects(delta);
        Matrix3x3f viewport = getViewportTransform();
        tileManager.update(viewport);
        heroManager.update(gravity, viewport, delta);
        enemyManager.update(viewport, delta);
    }

    @Override
    protected void render(Graphics g) {
        g.setFont(appFont);
        g.setColor(appFPSColor);
        frameRate.calculate();
        g.drawString(frameRate.getFrameRate(), 35, 20);
        g.drawString("Press [LEFT] and [RIGHT] to move", 35, 35);
        g.drawString("Press [UP] to jump", 35, 50);
        g.drawString("Press [SPACE] to reset", 35, 65);
        g.drawString("Press [B] to toggle bounding boxes", 35, 80);

        tileManager.render(g);
        heroManager.render(g);
        enemyManager.render(g);
    }

    public static void main(String[] args) {
        launchApp(new Sprites());
    }
}
