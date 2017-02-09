package transformations.logic;

import transformations.render.VectorObject;
import transformations.util.KeyboardInput;
import transformations.util.Matrix3x3f;
import transformations.util.MouseInput;
import transformations.util.Vector2f;

import java.awt.*;
import java.awt.event.KeyEvent;

import static transformations.render.WorldCanvas.SCREEN_H;
import static transformations.render.WorldCanvas.SCREEN_W;

/**
 * The State class is responsible for managing the state of
 * the application. It stores the vector objects representing
 * drawable shapes with location, rotation, and scale modifiers.
 * It is responsible for updating these objects in response
 * to user input.
 */
public class State {

    private VectorObject square;
    private float dx = 2;
    private float dy = 2;

    private VectorObject hexagon;
    private float rotStep = (float) Math.toRadians(1);
    private float rotDir = 1.0f;

    private VectorObject triangle;

    /**
     * Internal constructor that initializes the game state
     * by creating the 3 vector objects.
     */
    private State() {
        Shape shape;

        shape = new Rectangle(-40, -40, 80, 80);
        square = new VectorObject(shape, 0, 0, Color.BLUE);

        shape = new Polygon(
                new int[] { 40, -40, -80, -40, 40, 80, 40 },
                new int[] { -69, -69, 0, 69, 69, 0, -69 },
                7
        );
        hexagon = new VectorObject(shape, 0, 0, Color.RED);

        shape = new Polygon(
                new int[] { 0, -35, 35, 0 },
                new int[] { -40, 20, 20, -40 },
                4
        );
        triangle = new VectorObject(shape, 0, 0, Color.GREEN);
    }

    private static State instance = new State();

    /**
     * Retrieve the singleton's instance serving as the central source
     * of game state and primary means of input processing.
     * @return The singleton instance.
     */
    public static State getInstance() {
        return instance;
    }

    /**
     * Retrieve all of the vector objects.
     * @return An array containing the vector objects managed by State.
     */
    public VectorObject[] getVectorObjects() {
        return new VectorObject[] { square, hexagon, triangle };
    }

    /**
     * Updates the game state in response to user input. The keyboard
     * and mouse listeners are polled and the updates to each
     * vector object are delegated to internal methods.
     * @param keyboard The keyboard input listener.
     * @param mouse The mouse input listener.
     */
    public void processInput(KeyboardInput keyboard, MouseInput mouse) {
        keyboard.poll();
        mouse.poll();

        updateSquare();
        updateHexagon(keyboard);
        updateTriangle(mouse);
    }

    /**
     * Updates the square vector object. The square moves in a
     * 45 degree angle at a constant rate with each frame,
     * changing direction when the edges of the canvas are
     * reaches.
     */
    private void updateSquare() {
        Point.Float location = square.getLocation();
        float x = location.x + dx;
        float y = location.y + dy;

        /*
        If the left or right edge of the canvas is passed,
        make the square flush with the edge and change the
        direction of delta x.
         */
        if (x - 41 < -SCREEN_W / 2) {
            x = -SCREEN_W / 2 + 41;
            dx = -dx;
        } else if (x + 41 > SCREEN_W / 2) {
            x = SCREEN_W / 2 - 41;
            dx = -dx;
        }

        /*
        If the top or bottom edge of the canvas is passed,
        make the square flush with the edge and change the
        direction of delta y.
         */
        if (y - 41 < -SCREEN_H / 2) {
            y = -SCREEN_H / 2 + 41;
            dy = -dy;
        } else if (y + 41 > SCREEN_H / 2) {
            y = SCREEN_H / 2 - 41;
            dy = -dy;
        }

        square.setLocation(x, y);
        square.updateWorld();
    }

    /**
     * Updates the hexagon vector object in response to key
     * presses. The WASD keys move the object in the direction
     * corresponding to each key. The Q and E keys decrease and
     * increase the object's speed of rotation, respectively.
     * The spacebar reverses the object's direction of rotation.
     * @param keyboard The keyboard input listener.
     */
    private void updateHexagon(KeyboardInput keyboard) {
        Point.Float location = hexagon.getLocation();
        float x = location.x;
        float y = location.y;

        /*
        Change the object's translation in response to the
        movement keys: WASD.
         */
        float dt = 3;
        if (keyboard.keyDown(KeyEvent.VK_A)) {
            x -= dt;
        }
        if (keyboard.keyDown(KeyEvent.VK_D)) {
            x += dt;
        }
        if (keyboard.keyDown(KeyEvent.VK_W)) {
            y += dt;
        }
        if (keyboard.keyDown(KeyEvent.VK_S)) {
            y -= dt;
        }
        hexagon.setLocation(x, y);

        /*
        Decrease/increase the rotation step when Q and E are pressed.
         */
        if (keyboard.keyDownOnce(KeyEvent.VK_Q)) {
            rotStep -= (float) Math.toRadians(0.5);
            if (rotStep < 0) {
                rotStep = 0;
            }
        }
        if (keyboard.keyDownOnce(KeyEvent.VK_E)) {
            rotStep += (float) Math.toRadians(0.5);
        }
        // Reverse the direction of rotation when SPACE is pressed.
        if (keyboard.keyDownOnce(KeyEvent.VK_SPACE)) {
            rotDir = -rotDir;
        }
        float rot = hexagon.getRotation() + (rotStep * rotDir);
        hexagon.setRotation(rot);

        hexagon.updateWorld();
    }

    /**
     * Updates the triangle vector object in response to mouse movement.
     * The triangle is drawn at the current mouse location.
     * @param mouse The mouse input listener.
     */
    private void updateTriangle(MouseInput mouse) {
        Point pos = mouse.getPosition();
        Vector2f mouseVector = Matrix3x3f.identity()
                .mul(Matrix3x3f.translate(SCREEN_W / 2, SCREEN_H / 2))
                .mul(new Vector2f(pos.x - SCREEN_W, -pos.y));

        triangle.setLocation(mouseVector);
        triangle.updateWorld();
    }
}
