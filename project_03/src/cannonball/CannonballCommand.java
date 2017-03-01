package cannonball;

import cannonball.util.Matrix3x3f;
import cannonball.util.SimpleFramework;
import cannonball.world.CannonballManager;
import cannonball.world.CityManager;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

public class CannonballCommand extends SimpleFramework {

    private CannonballManager cannonballManager;
    private CityManager cityManager;
    private Matrix3x3f viewport;
    private boolean gameOver;
    private float score;
    private float scoreMultiplier;

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

        gameOver = true;
        score = 0;
        scoreMultiplier = 1;
    }

    @Override
    protected void initialize() {
        super.initialize();
        cannonballManager = CannonballManager.getInstance();
        cityManager = CityManager.getInstance();
        viewport = getViewportTransform();
    }

    @Override
    protected void onComponentResized(ComponentEvent e) {
        super.onComponentResized(e);
        viewport = getViewportTransform();
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
        // TODO: find out if viewport can be accurately set using just initialize &
        // onComponentResized to avoid having to set the viewport every game loop
        cannonballManager.update(delta, viewport);
        cityManager.update(viewport);

        int cityCount = cityManager.getCities().size();
        score += 10 * delta * scoreMultiplier * cityCount;
        if (cityCount == 0) {
            gameOver = true;
        }
    }

    @Override
    protected void render(Graphics g) {
        super.render(g);
        float windVelocity = cannonballManager.getWindVelocity();
        String windDirection = windVelocity > 0 ? "East" : "West";

        g.drawString(String.format("Wind direction: %s", windDirection), 20, 40);
        g.drawString(String.format("Wind velocity: %.2f", Math.abs(windVelocity)), 20, 60);
        g.drawString(String.format("Score Multiplier: x%.0f", scoreMultiplier), 20, 80);
        g.drawString(String.format("Score: %.0f", score), 20, 100);

        cannonballManager.render(g);
        cityManager.render(g);
    }

    @Override
    protected void terminate() {
        super.terminate();
    }

    public static void main(String[] args) {
        launchApp(new CannonballCommand());
    }
}
