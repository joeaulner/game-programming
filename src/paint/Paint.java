package paint;

import paint.util.KeyboardInput;
import paint.logic.GameState;
import paint.render.PaintCanvas;
import paint.util.MouseInput;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Paint extends JFrame implements Runnable {

    private volatile boolean running;
    private Thread gameThread;

    private KeyboardInput keyboard;
    private MouseInput mouse;

    private PaintCanvas canvas;

    public void run() {
        running = true;
        while (running) {
            gameLoop();
        }
    }

    private void gameLoop() {
        GameState.getInstance().processInput(keyboard, mouse, canvas.getMenuItems());
        canvas.renderFrame();
        try {
            Thread.sleep(17L);
        } catch (InterruptedException ex) {
            // ignore thread exceptions when sleeping
        }
    }

    private void createAndShowGUI() {
        canvas = new PaintCanvas();

        getContentPane().add(canvas);
        setTitle("Java Paint");
        setIgnoreRepaint(true);
        pack();

        // add key listeners
        keyboard = new KeyboardInput();
        canvas.addKeyListener(keyboard);

        // add mouse listeners
        mouse = new MouseInput();
        canvas.addMouseListener(mouse);
        canvas.addMouseMotionListener(mouse);
        canvas.addMouseWheelListener(mouse);

        setVisible(true);
        canvas.createBufferStrategy();
        canvas.requestFocus();
        canvas.overrideCursor();

        gameThread = new Thread(this);
        gameThread.start();
    }

    private void onWindowClosing() {
        try {
            running = false;
            gameThread.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        final Paint app = new Paint();
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
