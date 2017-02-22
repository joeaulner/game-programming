package defense;

import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * An extension of Canvas that overrides the default cursor
 * and renders each frame in the game loop.
 */
public class WorldCanvas extends Canvas {

    public static final int SCREEN_W = 1280;
    public static final int SCREEN_H = 760;

    /**
     * Initializes the canvas with the appropriate color and dimensions.
     */
    public WorldCanvas() {
        setSize(SCREEN_W, SCREEN_H);
        setBackground(Color.WHITE);
        setIgnoreRepaint(true);
    }

    /**
     * Overrides the cursor by replacing it with a blank image.
     */
    private void overrideCursor() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image image = tk.createImage("");
        Point point = new Point(0, 0);
        String name = "Empty Cursor";
        Cursor cursor = tk.createCustomCursor(image, point, name);
        setCursor(cursor);
    }

    /**
     * Renders the new frame based on current game state.
     */
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
            } while (bs.contentsRestored()); // re-draw frame if contents were restored from lost state
            bs.show();
        } while (bs.contentsLost()); // re-draw frame if contents of drawing buffer were lost
    }

    /**
     * Render the contents of the frame using the provided graphics object.
     * Iterates through all vector objects and calls their render methods.
     * @param g The graphics context of the drawing buffer.
     */
    public void render(Graphics g) {
        overrideCursor();
    }
}
