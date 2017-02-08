package transformations.render;

import transformations.logic.State;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class WorldCanvas extends Canvas {

    public static final int SCREEN_W = 1280;
    public static final int SCREEN_H = 760;

    public WorldCanvas() {
        setSize(SCREEN_W, SCREEN_H);
        setBackground(Color.WHITE);
        setIgnoreRepaint(true);
    }

    public void overrideCursor() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image image = tk.createImage("");
        Point point = new Point(0, 0);
        String name = "Empty Cursor";
        Cursor cursor = tk.createCustomCursor(image, point, name);
        setCursor(cursor);
    }

    public void renderFrame() {
        BufferStrategy bs = getBufferStrategy();
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

    public void render(Graphics g) {
        State state = State.getInstance();

        for (VectorObject vectorObject : state.getVectorObjects()) {
            vectorObject.render(g);
        }

        Point p = state.getMousePos();
        g.setColor(Color.BLACK);
        g.drawLine(p.x - 10, p.y, p.x + 10, p.y);
        g.drawLine(p.x, p.y - 10, p.x, p.y + 10);
    }
}
