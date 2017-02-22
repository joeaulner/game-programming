package defense;

import defense.util.KeyboardInput;
import defense.util.MouseInput;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The Transformations application is used to demonstrate how
 * matrix transformations can be used to render 2D vector graphics
 * represented by a standard coordinate grid.
 */
public class CannonballDefense extends JFrame implements Runnable {

    private volatile boolean running;
    private Thread gameThread;

    private KeyboardInput keyboard;
    private MouseInput mouse;

    private WorldCanvas canvas;

    /**
     * Invoked when the game thread targeting this is started.
     * Executes the game loop until running is set to false.
     */
    public void run() {
        running = true;
        while (running) {
            gameLoop();
        }
    }

    /**
     * Updates the game state, renders the new frame, and sleeps for 10ms.
     */
    private void gameLoop() {
//        State.getInstance().processInput(keyboard, mouse);
        canvas.renderFrame();
        try {
            Thread.sleep(10L);
        } catch (InterruptedException ex) {
            // ignore thread exceptions when sleeping
        }
    }

    /**
     * Configures the JFrame, creates the world canvas, registers
     * keyboard/mouse event listeners, and starts the game thread.
     */
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
        canvas.addMouseListener(mouse);
        canvas.addMouseMotionListener(mouse);
        canvas.addMouseWheelListener(mouse);

        setVisible(true);
        canvas.createBufferStrategy(2);
        canvas.requestFocus();

        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Stops the game thread and exits the process when the JFrame is closed.
     */
    private void onWindowClosing() {
        try {
            running = false;
            gameThread.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.exit(0);
    }

    /**
     * Entry point to the application. Creates an instance of the Runnable JFrame,
     * registers the custom windowClosing event listener, and asynchronously
     * invokes the createAndShowGUI method.
     * @param args Unused.
     */
    public static void main(String[] args) {
        final CannonballDefense app = new CannonballDefense();
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
