package paint.logic;

import paint.util.KeyboardInput;
import paint.util.MouseInput;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static paint.logic.DrawMode.*;

final public class GameState {

    private ArrayList<ColorPoint> points;
    private boolean drawingLine;
    private Point mousePos;
    private Color color;
    private DrawMode mode;

    public static final Color[] COLORS = { Color.BLUE, Color.RED, Color.GREEN, Color.BLACK };
    private int colorIndex;

    private static GameState instance;

    private GameState() {
        points = new ArrayList<>();
        colorIndex = 0;
        mode = FREE_DRAW;
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
    public void setColorIndex(int colorIndex) {
        this.colorIndex = colorIndex;
    }

    public DrawMode getMode() {
        return mode;
    }
    public void setMode(DrawMode mode) {
        this.mode = mode;
    }

    public void processInput(KeyboardInput keyboard, MouseInput mouse, ArrayList<MenuItem> menuItems) {
        keyboard.poll();
        mouse.poll();

        colorIndex += mouse.getNotches();
        color = COLORS[Math.abs(colorIndex % COLORS.length)];

        mousePos = mouse.getPosition();

        if (keyboard.keyDownOnce(MouseEvent.BUTTON1)) {
            drawingLine = true;
        }

        for (MenuItem menuItem : menuItems) {
            if (mouse.buttonDownOnce(MouseEvent.BUTTON1) &&
                    mousePos.x >= menuItem.x && mousePos.x <= menuItem.x + MenuItem.MENU_ITEM_WIDTH &&
                    mousePos.y >= menuItem.y && mousePos.y <= menuItem.y + MenuItem.MENU_ITEM_HEIGHT) {
                if (!drawingLine) {
                    menuItem.onClicked();
                }
                return;
            }
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
