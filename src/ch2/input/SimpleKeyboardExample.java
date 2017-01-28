package ch2.input;

import ch2.util.SimpleKeyboardInput;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SimpleKeyboardExample extends JFrame implements Runnable {
    private volatile boolean running;
    private Thread gameThread;
    private SimpleKeyboardInput keys;
    private boolean space;

    private SimpleKeyboardExample() {
        keys = new SimpleKeyboardInput();
    }

    private void createAndShowGUI() {
        setTitle("Keyboard Input");
        setSize(320, 240);
        addKeyListener(keys);
        setVisible(true);

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run() {
        running = true;
        while (running) {
            gameLoop();
        }
    }

    private void gameLoop() {
        if (keys.keyDown(KeyEvent.VK_SPACE)) {
            if (!space) {
                System.out.println("VK_SPACE");
            }
            space = true;
        } else {
            space = false;
        }
        if (keys.keyDown(KeyEvent.VK_UP)) {
            System.out.println("VK_UP");
        }
        if (keys.keyDown(KeyEvent.VK_DOWN)) {
            System.out.println("VK_DOWN");
        }
        if (keys.keyDown(KeyEvent.VK_LEFT)) {
            System.out.println("VK_LEFT");
        }
        if (keys.keyDown(KeyEvent.VK_RIGHT)) {
            System.out.println("VK_RIGHT");
        }
        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {}
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
        final SimpleKeyboardExample app = new SimpleKeyboardExample();
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
