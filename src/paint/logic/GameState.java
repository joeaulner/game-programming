package paint.logic;

import paint.util.KeyboardInput;
import paint.util.MouseInput;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

final public class GameState {

    private ArrayList<ColorPoint> points = new ArrayList<>();
    private boolean drawingLine;
    private Point mousePos;
    private Color color;

    final Color[] COLORS = {
            Color.BLUE,
            Color.RED,
            Color.GREEN,
            Color.BLACK
    };
    private int colorIndex;

    public enum DrawMode {
        LINE, RECTANGLE, POLY_LINE, FREE_DRAW
    }
    private DrawMode mode;

    private static GameState instance;
    private GameState() {
        colorIndex = 0;
        mode = DrawMode.FREE_DRAW;
    }
    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    public ArrayList<ColorPoint> getPoints() {
        return points;
    }

    public Point getMousePos() {
        return mousePos;
    }

    public Color getColor() {
        return color;
    }

    public DrawMode getMode() {
        return mode;
    }

    public void processInput(KeyboardInput keyboard, MouseInput mouse) {
        keyboard.poll();
        mouse.poll();

        colorIndex += mouse.getNotches();
        color = COLORS[Math.abs(colorIndex % COLORS.length)];

        mousePos = mouse.getPosition();

        if (keyboard.keyDownOnce(MouseEvent.BUTTON1)) {
            drawingLine = true;
        }

        switch (mode) {
            case FREE_DRAW:
                processFreeDrawInput(mouse);
                break;
            default:
                System.out.println("Undefined draw mode");
        }

        if (keyboard.keyDownOnce(KeyEvent.VK_C)) {
            points.clear();
        }
    }

    private void processFreeDrawInput(MouseInput mouse) {
        if (mouse.buttonDown(MouseEvent.BUTTON1)) {
            points.add(new ColorPoint(mouse.getPosition(), color));
        } else {
            points.add(null);
            drawingLine = false;
        }
    }
}
