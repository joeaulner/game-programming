package paint.render;

import paint.logic.*;
import paint.logic.shape.DrawShape;
import paint.logic.tool.DrawTool;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import static paint.render.MenuItem.*;

public class PaintCanvas extends Canvas {

    private BufferStrategy bs;
    private ArrayList<MenuItem> menuItems;

    public PaintCanvas() {
        setSize(1280, 720);
        setBackground(Color.WHITE);
        setIgnoreRepaint(true);

        menuItems = new ArrayList<>();
        initMenuItems();
    }

    public void createBufferStrategy() {
        createBufferStrategy(2);
        bs = getBufferStrategy();
    }

    public void overrideCursor() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image image = tk.createImage("");
        Point point = new Point(0, 0);
        String name = "Empty Cursor";
        Cursor cursor = tk.createCustomCursor(image, point, name);
        setCursor(cursor);
    }

    public ArrayList<MenuItem> getMenuItems() {
        return menuItems;
    }

    private void initMenuItems() {
        final GameState state = GameState.getInstance();
        int y = 10;
        int yOffset = 50;

        for (final DrawTool tool : state.getTools()) {
            menuItems.add(new MenuItem(9, y, tool));
            y += yOffset;
        }

        for (int i = 0; i < 4; i++) {
            menuItems.add(new MenuItem(9, y, i));
            y += yOffset;
        }
    }

    public void renderFrame() {
        do {
            do {
                Graphics g = null;
                try {
                    g = bs.getDrawGraphics();
                    g.clearRect(0, 0, getWidth(), getHeight());
                    render(g);
                } finally {
                    if (g != null) {
                        g.dispose();
                    }
                }
            } while (bs.contentsRestored());
            bs.show();
        } while (bs.contentsLost());
    }

    private void render(Graphics g) {
        GameState state = GameState.getInstance();

        // draw shapes
        ArrayList<DrawShape> shapes = state.getShapes();
        for (DrawShape shape : shapes) {
            shape.render(g);
        }

        state.getActiveTool().renderDrawPreview(g);

        // draw menu container
        g.setColor(Color.WHITE);
        g.fillRect(-1, -1, 60, 410);
        g.setColor(Color.BLACK);
        g.drawRect(-1, -1, 60, 410);

        // draw menu items
        for (MenuItem menuItem : menuItems) {
            if (menuItem.tool == null) {
                g.setColor(GameState.COLORS[menuItem.colorIndex]);
                g.fillRect(menuItem.x, menuItem.y, MENU_ITEM_WIDTH, MENU_ITEM_HEIGHT);

                if (GameState.COLORS[menuItem.colorIndex] == state.getColor()) {
                    g.setColor(Color.RED);
                    g.drawRect(menuItem.x, menuItem.y, MENU_ITEM_WIDTH, MENU_ITEM_HEIGHT);
                }
            } else {
                menuItem.tool.renderMenuItem(menuItem.x, menuItem.y, g);
            }
        }

        // draw cursor
        Point p = state.getMousePos();
        g.setColor(state.getColor());
        g.drawLine(p.x - 10, p.y, p.x + 10, p.y);
        g.drawLine(p.x, p.y - 10, p.x, p.y + 10);
    }
}
