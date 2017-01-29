package paint.render;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class GameCanvas extends Canvas {

    private BufferStrategy bs;

    public GameCanvas() {
        setSize(1280, 720);
        setBackground(Color.BLACK);
        setIgnoreRepaint(true);
    }

    public void createBufferStrategy() {
        createBufferStrategy(2);
        bs = getBufferStrategy();
    }

    public void render() {
        bs.show();
    }
}
