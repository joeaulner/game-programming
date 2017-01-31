package paint.render;

import paint.logic.*;
import paint.logic.MenuItem;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import static paint.logic.DrawMode.*;
import static paint.logic.MenuItem.*;

public class PaintCanvas extends Canvas {

    private BufferStrategy bs;
    private ArrayList<MenuItem> menuItems;

    public PaintCanvas() {
        setSize(1280, 720);
        setBackground(Color.WHITE);
        setIgnoreRepaint(true);

        menuItems = new ArrayList<>();
        addMenuItems();
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

    private void addMenuItems() {
        final GameState state = GameState.getInstance();

        menuItems.add(new MenuItem(9, 10, null) {
            @Override
            public void onClicked() {
                state.setMode(LINE);
            }
        });
        menuItems.add(new MenuItem(9, 60, null) {
            @Override
            public void onClicked() {
                state.setMode(RECTANGLE);
            }
        });
        menuItems.add(new MenuItem(9, 110, null) {
            @Override
            public void onClicked() {
                state.setMode(POLY_LINE);
            }
        });
        menuItems.add(new MenuItem(9, 160, null) {
            @Override
            public void onClicked() {
                state.setMode(FREE_DRAW);
            }
        });

        menuItems.add(new MenuItem(9, 210, GameState.COLORS[0]) {
            @Override
            public void onClicked() {
                state.setColorIndex(0);
            }
        });
        menuItems.add(new MenuItem(9, 260, GameState.COLORS[1]) {
            @Override
            public void onClicked() {
                state.setColorIndex(1);
            }
        });
        menuItems.add(new MenuItem(9, 310, GameState.COLORS[2]) {
            @Override
            public void onClicked() {
                state.setColorIndex(2);
            }
        });
        menuItems.add(new MenuItem(9, 360, GameState.COLORS[3]) {
            @Override
            public void onClicked() {
                state.setColorIndex(3);
            }
        });
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

        // draw lines
        ArrayList<ColorPoint> points = state.getPoints();
        for (int i = 0; i < points.size() - 1; ++i) {
            ColorPoint p1 = points.get(i);
            ColorPoint p2 = points.get(i + 1);

            if (p1 != null && p2 != null) {
                g.setColor(p1.color);
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }

        drawLinePreview(state, g);

        // draw menu container
        g.setColor(Color.WHITE);
        g.fillRect(-1, -1, 60, 410);
        g.setColor(Color.BLACK);
        g.drawRect(-1, -1, 60, 410);

        // draw menu items
        for (MenuItem menuItem : menuItems) {
            if (menuItem.color == null) {
                g.setColor(Color.BLACK);
                g.drawRect(menuItem.x, menuItem.y, MENU_ITEM_WIDTH, MENU_ITEM_HEIGHT);
            } else {
                g.setColor(menuItem.color);
                g.fillRect(menuItem.x, menuItem.y, MENU_ITEM_WIDTH, MENU_ITEM_HEIGHT);
            }
        }

        // draw cursor
        Point p = state.getMousePos();
        g.setColor(state.getColor());
        g.drawLine(p.x - 10, p.y, p.x + 10, p.y);
        g.drawLine(p.x, p.y - 10, p.x, p.y + 10);
    }

    private void drawLinePreview(GameState state, Graphics g) {
        ArrayList<ColorPoint> points = state.getPoints();
        if (points.size() == 0) {
            return;
        }

        ColorPoint last = points.get(points.size() - 1);
        if (last == null || !state.getDrawingLine()) {
            return;
        }

        Point p = state.getMousePos();
        g.setColor(state.getColor());
        switch (state.getMode()) {
            case LINE:
                g.drawLine(last.x, last.y, p.x, p.y);
                break;
            case RECTANGLE:
                int x, y, w, h;
                if (p.x < last.x) {
                    x = p.x;
                    w = last.x - p.x;
                } else {
                    x = last.x;
                    w = p.x - last.x;
                }
                if (p.y < last.y) {
                    y = p.y;
                    h = last.y - p.y;
                } else {
                    y = last.y;
                    h = p.y - last.y;
                }
                g.drawRect(x, y, w, h);
                break;
            case POLY_LINE:
                g.drawLine(last.x, last.y, p.x, p.y);
                break;
        }
    }
}
