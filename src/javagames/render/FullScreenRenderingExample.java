package javagames.render;

import javagames.util.FrameRate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

public class FullScreenRenderingExample extends JFrame implements Runnable {
    private FrameRate frameRate;
    private BufferStrategy bs;
    private Thread gameThread;
    private GraphicsDevice graphicsDevice;
    private DisplayMode currentDisplayMode;
    private volatile boolean running;

    public FullScreenRenderingExample() {
        frameRate = new FrameRate();
    }

    protected void createAndShowGUI() {
        setIgnoreRepaint(true);
        setUndecorated(true);
        setBackground(Color.BLACK);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        graphicsDevice = ge.getDefaultScreenDevice();
        currentDisplayMode = graphicsDevice.getDisplayMode();

        if (!graphicsDevice.isFullScreenSupported()) {
            System.err.println("ERROR: Not Supported!");
            System.exit(0);
        }

        graphicsDevice.setFullScreenWindow(this);
        graphicsDevice.setDisplayMode(getDisplayMode());

        createBufferStrategy(2);
        bs = getBufferStrategy();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    shutDown();
                }
            }
        });

        gameThread = new Thread(this);
        gameThread.start();
    }

    private DisplayMode getDisplayMode() {
        return new DisplayMode(1920, 1080, 32, DisplayMode.REFRESH_RATE_UNKNOWN);
    }

    public void run() {
        running = true;
        while (running) {
            gameLoop();
        }
    }

    public void gameLoop() {
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
        frameRate.calculate();
        g.setColor(Color.GREEN);
        g.drawString(frameRate.getFrameRate(), 30, 30);
        g.drawString("Press ESC to exit...", 30, 60);
    }

    protected void shutDown() {
        try {
            running = false;
            gameThread.join();
            System.out.println("Game loop stopped!!!");
            graphicsDevice.setDisplayMode(currentDisplayMode);
            graphicsDevice.setFullScreenWindow(null);
            System.out.println("Display Restored...");
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        final FullScreenRenderingExample app = new FullScreenRenderingExample();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                app.createAndShowGUI();
            }
        });
    }
}
