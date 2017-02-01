package paint.logic;

import paint.logic.shape.DrawShape;
import paint.logic.tool.*;
import paint.util.KeyboardInput;
import paint.util.MouseInput;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static paint.logic.MenuItem.MENU_ITEM_HEIGHT;
import static paint.logic.MenuItem.MENU_ITEM_WIDTH;

final public class GameState {

    private ArrayList<DrawShape> shapes;
    private Point mousePos;
    private Color color;

    private ArrayList<DrawTool> tools;
    private int toolIndex;

    public static final Color[] COLORS = {Color.BLUE, Color.RED, Color.GREEN, Color.BLACK};
    private int colorIndex;

    private static GameState instance = new GameState();

    private GameState() {
        tools = new ArrayList<>();
        tools.add(new LineTool());
        tools.add(new RectangleTool());
        tools.add(new PolyLineTool());
        tools.add(new FreeDrawTool());

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
        return color;
    }

    public void setColorIndex(int colorIndex) {
        this.colorIndex = colorIndex;
    }

    public void setActiveTool(int toolIndex) {
        this.toolIndex = toolIndex;
    }

    public void addShape(DrawShape shape) {
        shapes.add(shape);
    }

    public ArrayList<DrawTool> getTools() {
        return tools;
    }

    public DrawTool getActiveTool() {
        return tools.get(toolIndex);
    }

    public ArrayList<DrawShape> getShapes() {
        return shapes;
    }

    public void processInput(KeyboardInput keyboard, MouseInput mouse, ArrayList<MenuItem> menuItems) {
        keyboard.poll();
        mouse.poll();

        int notches = mouse.getNotches();
        if (notches > 0 && !tools.get(toolIndex).isDrawing()) {
            toolIndex = (toolIndex + 1) % tools.size();
        } else if (notches < 0) {
            colorIndex++;
            color = COLORS[colorIndex % COLORS.length];
        }

        mousePos = mouse.getPosition();

        for (MenuItem menuItem : menuItems) {
            Rectangle rectangle = new Rectangle(menuItem.x, menuItem.y, MENU_ITEM_WIDTH, MENU_ITEM_HEIGHT);
            if (mouse.buttonDownOnce(MouseEvent.BUTTON1) && rectangle.contains(mousePos.x, mousePos.y)) {
                if (!tools.get(toolIndex).isDrawing()) {
                    if (menuItem.tool == null) {
                        menuItem.setColor();
                    } else {
                        menuItem.setActiveTool();
                    }
                }
                return;
            }
        }

        tools.get(toolIndex).processInput(mouse);

        if (keyboard.keyDownOnce(KeyEvent.VK_C)) {
            shapes.clear();
        }
    }
}
