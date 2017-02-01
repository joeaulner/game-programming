package paint.logic;

import paint.logic.shape.DrawShape;
import paint.logic.tool.*;
import paint.util.KeyboardInput;
import paint.util.MouseInput;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

final public class GameState {

    private ArrayList<DrawShape> shapes;
    private Point mousePos;

    private ArrayList<DrawTool> tools;
    private int toolIndex;

    public static final Color[] COLORS = {Color.BLUE, Color.RED, Color.GREEN, Color.BLACK};
    private int colorIndex;

    private static GameState instance = new GameState();

    private GameState() {
        tools = new ArrayList<>();
        tools.add(new LineTool(this));
        tools.add(new RectangleTool(this));
        tools.add(new PolyLineTool(this));
        tools.add(new FreeDrawTool(this));

        shapes = new ArrayList<>();
        colorIndex = 0;
    }

    public static GameState getInstance() {
        return instance;
    }

    public Point getMousePos() {
        return mousePos;
    }

    public Color getColor() {
        return COLORS[colorIndex % COLORS.length];
    }

    public void setColorIndex(int colorIndex) {
        this.colorIndex = colorIndex;
    }

    public void addShape(DrawShape shape) {
        shapes.add(shape);
    }

    public ArrayList<DrawShape> getShapes() {
        return shapes;
    }

    public ArrayList<DrawTool> getTools() {
        return tools;
    }

    public void setActiveTool(int toolIndex) {
        this.toolIndex = toolIndex;
    }

    public DrawTool getActiveTool() {
        return tools.get(toolIndex);
    }

    public void processInput(KeyboardInput keyboard, MouseInput mouse, ArrayList<paint.render.MenuItem> menuItems) {
        keyboard.poll();
        mouse.poll();

        // cycle through colors/tools on scroll up/down, respectively
        int notches = mouse.getNotches();
        if (notches > 0 && !tools.get(toolIndex).isDrawing()) {
            toolIndex = (toolIndex + 1) % tools.size();
        } else if (notches < 0) {
            colorIndex++;
        }

        // if a single click event falls on a menu item and drawing is not active, set the correct color/tool
        mousePos = mouse.getPosition();
        for (paint.render.MenuItem menuItem : menuItems) {
            if (mouse.buttonDownOnce(MouseEvent.BUTTON1) && menuItem.contains(mousePos.x, mousePos.y)) {
                if (!tools.get(toolIndex).isDrawing()) {
                    menuItem.onSelected();
                }
                return;
            }
        }

        tools.get(toolIndex).processInput(mouse);

        // clear all drawn shapes on C keystroke
        if (keyboard.keyDownOnce(KeyEvent.VK_C)) {
            shapes.clear();
        }
    }
}
