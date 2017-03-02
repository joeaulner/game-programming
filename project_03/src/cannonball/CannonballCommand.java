package cannonball;

import cannonball.util.Matrix3x3f;
import cannonball.util.SimpleFramework;
import cannonball.util.Vector2f;
import cannonball.world.CannonballManager;
import cannonball.world.CityManager;
import cannonball.world.VectorObject;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Simple game based on the concept of Missile Command. The player defends a collection of
 * cities from a barrage of falling projectiles. Clicking on a projectile destroys it and
 * reward the player with 50 points. If a projectile collides with a city, both are destroyed.
 * The player receives 10 points per second, multiplied by the number of cities remaining
 * in play. Once all cities are destroyed, the game is over.
 */
public class CannonballCommand extends SimpleFramework {

    private CannonballManager cannonballManager;
    private CityManager cityManager;
    private Matrix3x3f viewport;
    private VectorObject cursor;
    private boolean gameOver;
    private float score;
    private float cityCount;

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
        cityCount = 1;
    }

    /**
     * Initialize the game by getting the manager references and initial viewport
     * and creating a VectorObject to be rendered in place of the default cursor.
     */
    @Override
    protected void initialize() {
        super.initialize();
        cannonballManager = CannonballManager.getInstance();
        cityManager = CityManager.getInstance();
        viewport = getViewportTransform();

        cursor = new VectorObject(new Vector2f[]{
                new Vector2f(-1, 0),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1),
                new Vector2f(0, -1)
        }, 0, 0, Color.RED);
        cursor.setScale(0.5f, 0.5f);
    }

    /**
     * When the JFrame/Canvas are resized, get the new viewport transformation matrix.
     * @param e The resize event (not used).
     */
    @Override
    protected void onComponentResized(ComponentEvent e) {
        super.onComponentResized(e);
        viewport = getViewportTransform();
    }

    /**
     * Process user input. The SPACE key event resets the game (if one is not in progress)
     * and a mouse click sends the current mouse location to the cannonball manager for
     * collision detection.
     * @param delta Time elapsed since the last frame.
     */
    @Override
    protected void processInput(float delta) {
        super.processInput(delta);
        if (gameOver && keyboard.keyDownOnce(KeyEvent.VK_SPACE)) {
            score = 0;
            gameOver = false;
            cannonballManager.initialize();
            cityManager.initialize();
        }
        if (mouse.buttonDownOnce(MouseEvent.BUTTON1)) {
            cannonballManager.setClickPos(getWorldMousePosition());
        }
    }

    /**
     * Update the game objects using the time delta. Most game object updates are delegated
     * to the respective managers, with the cursor and game score updated in this method.
     * @param delta Time elapsed since the last frame.
     */
    @Override
    protected void updateObjects(float delta) {
        super.updateObjects(delta);

        // Update the cursor VectorObject's location and viewport
        Vector2f worldMousePos = getWorldMousePosition();
        cursor.setLocation(worldMousePos.x, worldMousePos.y);
        cursor.setViewport(viewport);
        cursor.updateWorld();

        // Do no further processing if the game is not in progress
        if (gameOver) {
            return;
        }

        // Delegate game object updates to appropriate managers
        cannonballManager.update(delta, viewport);
        cityManager.update(viewport);

        // The game is over when there are no cities remaining
        cityCount = cityManager.getCities().size();
        if (cityCount == 0) {
            gameOver = true;
            cannonballManager.onGameOver();
        }

        /*
        The score is increased by 10 per second multiplied by the number of cities remaining.
        An additional 50 points is rewarded for every cannonball destroyed
        */
        score += 10 * delta * this.cityCount;
        if (cannonballManager.getCannonballDestroyed()) {
            score += 50;
        }
    }

    /**
     * Override the default cursor, render the current game status,
     * and delegate object rendering to appropriate managers.
     * @param g The Graphics object used to draw to the Canvas.
     */
    @Override
    protected void render(Graphics g) {
        overrideCursor();

        float windVelocity = cannonballManager.getWindVelocity();
        char windDirection = windVelocity > 0 ? 'E' : 'W';

        // Render correct game not started, game over, or game in progress status
        g.setColor(Color.BLACK);
        if (gameOver && score == 0) {
            g.drawString("Press [SPACE] to start.", 20, 20);
        } else if (gameOver) {
            g.drawString("Game Over!", 20, 20);
            g.drawString(String.format("Final Score: %,.0f", score), 20, 40);
            g.drawString("Press [SPACE] to restart.", 20, 60);
        } else {
            g.drawString("Defend the factories!", 20, 20);
            g.drawString(String.format("Wind: %.1f%c", Math.abs(windVelocity), windDirection), 20, 40);
            g.drawString(String.format("Multiplier: x%.0f", cityCount), 20, 60);
            g.drawString(String.format("Score: %,.0f", score), 20, 80);
        }

        // Render all vector objects
        cannonballManager.render(g);
        cityManager.render(g);
        cursor.render(g);
    }

    /**
     * Override the default cursor by replacing it with a cursor created from an empty image.
     */
    private void overrideCursor() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image image = tk.createImage("");
        Point point = new Point(0, 0);
        String name = "Empty Cursor";
        setCursor(tk.createCustomCursor(image, point, name));
    }

    /**
     * No additional cleanup necessary.
     */
    @Override
    protected void terminate() {
        super.terminate();
    }

    /**
     * Launches the game.
     * @param args Not used.
     */
    public static void main(String[] args) {
        launchApp(new CannonballCommand());
    }
}
