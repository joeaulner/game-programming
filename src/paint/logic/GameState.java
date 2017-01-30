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
            case LINE:
                processLineInput(mouse);
                break;
            case RECTANGLE:
                processRectangleInput(mouse);
                break;
            case POLY_LINE:
                processPolyLineInput(mouse);
                break;
            case FREE_DRAW:
                processFreeDrawInput(mouse);
                break;
        }

        if (keyboard.keyDownOnce(KeyEvent.VK_C)) {
            points.clear();
        }
    }

    private void processLineInput(MouseInput mouse) {
        if (mouse.buttonDownOnce(MouseEvent.BUTTON1)) {
            points.add(new ColorPoint(mouse.getPosition(), color));
            drawingLine = !drawingLine;
            if (!drawingLine) {
                points.add(null);
            }
        }
    }

    private void processRectangleInput(MouseInput mouse) {
        if (mouse.buttonDownOnce(MouseEvent.BUTTON1)) {
            if (drawingLine) {
                Point pos = mouse.getPosition();
                ColorPoint last = points.get(points.size() - 1);

                points.add(new ColorPoint(new Point(last.x, pos.y), color));
                points.add(new ColorPoint(new Point(pos.x, pos.y), color));
                points.add(new ColorPoint(new Point(pos.x, last.y), color));
                points.add(new ColorPoint(new Point(last.x, last.y), color));
                points.add(null);
            } else {
                points.add(new ColorPoint(mouse.getPosition(), color));
            }
            drawingLine = !drawingLine;
        }
    }

    private void processPolyLineInput(MouseInput mouse) {
        if (mouse.buttonDownOnce(MouseEvent.BUTTON1)) {
            points.add(new ColorPoint(mouse.getPosition(), color));
        } else if (mouse.buttonDownOnce(MouseEvent.BUTTON3)) {
            points.add(null);
        }
        drawingLine = !drawingLine;
    }

    private void processFreeDrawInput(MouseInput mouse) {
        if (mouse.buttonDownOnce(MouseEvent.BUTTON1)) {
            points.add(new ColorPoint(mouse.getPosition(), color));
            drawingLine = true;
        } else if (mouse.buttonDown(MouseEvent.BUTTON1) && drawingLine) {
            points.add(new ColorPoint(mouse.getPosition(), color));
        } else if (drawingLine) {
            points.add(null);
            drawingLine = false;
        }
    }
}
