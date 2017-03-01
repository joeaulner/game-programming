package cannonball;

import cannonball.util.SimpleFramework;
import cannonball.world.CannonballManager;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

public class CannonballCommand extends SimpleFramework {

    private CannonballManager cannonballManager;
    private boolean gameOver;

    public CannonballCommand() {
        appBackground = Color.WHITE;
        appBorder = Color.LIGHT_GRAY;
        appFont = new Font("Courier New", Font.PLAIN, 14);
        appBorderScale = 0.9f;
        appWidth = 1280;
        appHeight = 720;
        appMaintainRatio = true;
        appSleep = 10L;
        appTitle = "FrameworkTemplate";
        appWorldWidth = 40.0f;
        appWorldHeight = 20.0f;

        cannonballManager = new CannonballManager();
        gameOver = true;
    }

    @Override
    protected void initialize() {
        super.initialize();
    }

    @Override
    protected void onComponentResized(ComponentEvent e) {
        super.onComponentResized(e);
        cannonballManager.setViewport(getViewportTransform());
    }

    @Override
    protected void processInput(float delta) {
        super.processInput(delta);
        if (keyboard.keyDownOnce(KeyEvent.VK_SPACE)) {
            gameOver = false;
        }
    }

    @Override
    protected void updateObjects(float delta) {
        super.updateObjects(delta);
        if (gameOver) {
            return;
        }

        cannonballManager.update(delta, getViewportTransform());
    }

    @Override
    protected void render(Graphics g) {
        super.render(g);
        cannonballManager.render(g);
    }

    @Override
    protected void terminate() {
        super.terminate();
    }

    public static void main(String[] args) {
        launchApp(new CannonballCommand());
    }
}
