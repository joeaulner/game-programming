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
        }, 0, 0, Color.RED, viewport);
        cursor.setScale(0.5f, 0.5f);
    }

    @Override
    protected void onComponentResized(ComponentEvent e) {
        super.onComponentResized(e);
        viewport = getViewportTransform();
    }

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

    @Override
    protected void updateObjects(float delta) {
        super.updateObjects(delta);

        Vector2f worldMousePos = getWorldMousePosition();
        cursor.setLocation(worldMousePos.x, worldMousePos.y);
        cursor.setViewport(viewport);
        cursor.updateWorld();

        if (gameOver) {
            return;
        }
        // TODO: find out if viewport can be accurately set using just initialize &
        // onComponentResized to avoid having to set the viewport every game loop
        cannonballManager.update(delta, viewport);
        cityManager.update(viewport);

        cityCount = cityManager.getCities().size();
        if (cityCount == 0) {
            gameOver = true;
            cannonballManager.onGameOver();
        }
        score += 10 * delta * this.cityCount;
        if (cannonballManager.getCannonballDestroyed()) {
            score += 50;
        }
    }

    @Override
    protected void render(Graphics g) {
        overrideCursor();

        float windVelocity = cannonballManager.getWindVelocity();
        char windDirection = windVelocity > 0 ? 'E' : 'W';

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

        cannonballManager.render(g);
        cityManager.render(g);
        cursor.render(g);
    }

    private void overrideCursor() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image image = tk.createImage("");
        Point point = new Point(0, 0);
        String name = "Empty Cursor";
        setCursor(tk.createCustomCursor(image, point, name));
    }

    @Override
    protected void terminate() {
        super.terminate();
    }

    public static void main(String[] args) {
        launchApp(new CannonballCommand());
    }
}
