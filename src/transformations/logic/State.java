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

public class State {

    private static State instance = new State();

    private VectorObject square;
    private float dx = 2;
    private float dy = 2;

    private VectorObject hexagon;
    private float rotStep = (float) Math.toRadians(1);
    private float rotDir = 1.0f;

    private VectorObject triangle;

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

    public static State getInstance() {
        return instance;
    }

    public VectorObject[] getVectorObjects() {
        return new VectorObject[] { square, hexagon, triangle };
    }

    public void processInput(KeyboardInput keyboard, MouseInput mouse) {
        keyboard.poll();
        mouse.poll();

        updateSquare();
        updateHexagon(keyboard);
        updateTriangle(mouse);
    }

    private void updateSquare() {
        Point.Float location = square.getLocation();
        float x = location.x + dx;
        float y = location.y + dy;

        if (x - 41 < -SCREEN_W / 2) {
            x = -SCREEN_W / 2 + 41;
            dx = -dx;
        } else if (x + 41 > SCREEN_W / 2) {
            x = SCREEN_W / 2 - 41;
            dx = -dx;
        }

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

    private void updateHexagon(KeyboardInput keyboard) {
        Point.Float location = hexagon.getLocation();
        float x = location.x;
        float y = location.y;

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

        if (keyboard.keyDownOnce(KeyEvent.VK_Q)) {
            rotStep -= (float) Math.toRadians(0.5);
            if (rotStep < 0) {
                rotStep = 0;
            }
        }
        if (keyboard.keyDownOnce(KeyEvent.VK_E)) {
            rotStep += (float) Math.toRadians(0.5);
        }
        if (keyboard.keyDownOnce(KeyEvent.VK_SPACE)) {
            rotDir = -rotDir;
        }
        float rot = hexagon.getRotation() + (rotStep * rotDir);
        hexagon.setRotation(rot);

        hexagon.updateWorld();
    }

    private void updateTriangle(MouseInput mouse) {
        Point pos = mouse.getPosition();
        Vector2f mouseVector = Matrix3x3f.identity()
                .mul(Matrix3x3f.translate(SCREEN_W / 2, SCREEN_H / 2))
                .mul(new Vector2f(pos.x - SCREEN_W, -pos.y));

        triangle.setLocation(mouseVector);
        triangle.updateWorld();
    }
}
