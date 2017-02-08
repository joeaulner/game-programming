package transformations;

import transformations.logic.State;
import transformations.render.WorldCanvas;
import transformations.util.KeyboardInput;
import transformations.util.MouseInput;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Transformations extends JFrame implements Runnable {

    private volatile boolean running;
    private Thread gameThread;

    private KeyboardInput keyboard;
    private MouseInput mouse;

    private WorldCanvas canvas;

    public void run() {
        running = true;
        while (running) {
            gameLoop();
        }
    }

    private void gameLoop() {
        State.getInstance().processInput(keyboard, mouse);
        canvas.renderFrame();
        try {
            Thread.sleep(16L);
        } catch (InterruptedException ex) {
            // ignore thread exceptions when sleeping
        }
    }

    private void createAndShowGUI() {
        canvas = new WorldCanvas();

        getContentPane().add(canvas);
        setTitle("Transformations");
        setIgnoreRepaint(true);
        pack();

        // add keyboard listeners
        keyboard = new KeyboardInput();
        canvas.addKeyListener(keyboard);

        // add mouse listeners
        mouse = new MouseInput();
        canvas.addMouseMotionListener(mouse);

        setVisible(true);
        canvas.createBufferStrategy(2);
        canvas.requestFocus();

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
        final Transformations app = new Transformations();
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
