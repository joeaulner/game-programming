package paint.render;

import paint.logic.ColorPoint;
import paint.logic.GameState;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class GameCanvas extends Canvas {

    private BufferStrategy bs;

    public GameCanvas() {
        setSize(1280, 720);
        setBackground(Color.WHITE);
        setIgnoreRepaint(true);
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

        // display mode
        String mode;
        switch (state.getMode()) {
            case FREE_DRAW:
                mode = "free draw";
                break;
            default:
                mode = "undefined mode";
        }
        g.drawString("Current mode: " + mode, 20, 20);

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

        // draw cursor
        g.setColor(state.getColor());
        Point p = state.getMousePos();
        g.drawLine(p.x - 10, p.y, p.x + 10, p.y);
        g.drawLine(p.x, p.y - 10, p.x, p.y + 10);
    }
}
