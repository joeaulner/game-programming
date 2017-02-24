package javagames.timeandspace;

import javagames.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

public class ViewportRatio extends JFrame implements Runnable {
    private FrameRate frameRate;
    private BufferStrategy bs;
    private volatile boolean running;
    private Thread gameThread;
    private RelativeMouseInput mouse;
    private KeyboardInput keyboard;
    private Canvas canvas;
    private Vector2f[] tri;
    private Vector2f[] triWorld;
    private float worldWidth;
    private float worldHeight;

    public ViewportRatio() {

    }

    protected void createAndShowGUI() {
        canvas = new Canvas();
        canvas.setBackground(Color.WHITE);
        canvas.setIgnoreRepaint(true);
        getContentPane().setBackground(Color.LIGHT_GRAY);
        setLayout(null);
        setTitle("Viewport Ratio");
        setSize(640, 640);
        // had to set size here in addition to sample code to avoid
        // InvalidArgumentException caused by canvas size not being initialized
        canvas.setSize(640, 640);
        getContentPane().add(canvas);
        keyboard = new KeyboardInput();
        canvas.addKeyListener(keyboard);
        mouse = new RelativeMouseInput(canvas);
        canvas.addMouseListener(mouse);
        canvas.addMouseMotionListener(mouse);
        canvas.addMouseWheelListener(mouse);
        getContentPane().addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                onComponentResized(e);
            }
        });
        setVisible(true);
        canvas.createBufferStrategy(2);
        bs = canvas.getBufferStrategy();
        canvas.requestFocus();
        gameThread = new Thread(this);
        gameThread.start();
    }

    protected void onComponentResized(ComponentEvent e) {
        Dimension size = getContentPane().getSize();
        int vw = size.width * 3 / 4;
        int vh = size.height * 3 / 4;
        int vx = (size.width - vw) / 2;
        int vy = (size.height - vh) / 2;
        int newW = vw;
        int newH = (int) (vw * worldHeight / worldWidth);
        if (newH > vh) {
            newW = (int) (vh * worldWidth / worldHeight);
            newH = vh;
        }
        // center
        vx += (vw - newW) / 2;
        vy += (vh - newH) / 2;
        canvas.setLocation(vx, vy);
        canvas.setSize(newW, newH);
    }

    public void run() {
        running = true;
        initialize();
        long curTime = System.nanoTime();
        long lastTime = curTime;
        double nsPerFrame;
        while (running) {
            curTime = System.nanoTime();
            nsPerFrame = curTime - lastTime;
            gameLoop(nsPerFrame / 1.0E9);
            lastTime = curTime;
        }
    }

    private void gameLoop(double delta) {
        processInput(delta);
        updateObjects(delta);
        renderFrame();
        sleep(10L);
    }

    private void renderFrame() {
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

    private void sleep(long sleep) {
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException ex) {
            // ignore exceptions trying to sleep
        }
    }

    private void initialize() {
        frameRate = new FrameRate();
        tri = new Vector2f[] {
                new Vector2f(0.0f, 0.5f),
                new Vector2f(-0.5f, -0.5f),
                new Vector2f(0.5f, -0.5f)
        };
        triWorld = new Vector2f[tri.length];
        worldWidth = 16.0f;
        worldHeight = 9.0f;
    }

    private void processInput(double delta) {
        keyboard.poll();
        mouse.poll();
    }

    private void updateObjects(double delta) {

    }

    private void render(Graphics g) {
        g.setColor(Color.BLACK);
        frameRate.calculate();
        g.drawString(frameRate.getFrameRate(), 20, 20);

        float sx = (canvas.getWidth() - 1) / worldWidth;
        float sy = (canvas.getHeight() - 1) / worldHeight;
        float tx = (canvas.getWidth() - 1) / 2.0f;
        float ty = (canvas.getHeight() - 1) / 2.0f;
        Matrix3x3f viewport = Matrix3x3f.identity()
                .mul(Matrix3x3f.scale(sx, -sy))
                .mul(Matrix3x3f.translate(tx, ty));
        for (int i = 0; i < tri.length; ++i) {
            triWorld[i] = viewport.mul(tri[i]);
        }
        drawPolygon(g, triWorld);
    }

    private void drawPolygon(Graphics g, Vector2f[] polygon) {
        Vector2f start = polygon[polygon.length - 1];
        for (Vector2f end : polygon) {
            g.drawLine((int) start.x, (int) start.y, (int) end.x, (int) end.y);
            start = end;
        }
    }

    protected void onWindowClosing() {
        try {
            running = false;
            gameThread.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        final ViewportRatio app = new ViewportRatio();
        app.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                app.onWindowClosing();
            }
        });
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                app.createAndShowGUI();
            }
        });
    }
}
