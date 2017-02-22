package defense.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * A keyboard input listener that provides methods for
 * checking initial and continuous key presses.
 */
public class KeyboardInput implements KeyListener {
    private boolean[] keys;
    private int[] polled;

    /**
     * Initialize the keys and polled arrays used to track key presses/duration.
     */
    public KeyboardInput() {
        keys = new boolean[256];
        polled = new int[256];
    }

    /**
     * Check whether a key is currently pressed.
     * @param keyCode The key to check for.
     * @return Whether the keyCode is pressed.
     */
    public boolean keyDown(int keyCode) {
        return polled[keyCode] > 0;
    }

    /**
     * Check for an initial key press.
     * @param keyCode The key to check for.
     * @return Whether this is the first cycle the key has been pressed.
     */
    public boolean keyDownOnce(int keyCode) {
        return polled[keyCode] == 1;
    }

    /**
     * Increment the duration of keys currently pressed.
     * Reset the duration of keys not pressed to zero.
     */
    public synchronized void poll() {
        for (int i = 0; i < keys.length; ++i) {
            if (keys[i]) {
                polled[i]++;
            } else {
                polled[i] = 0;
            }
        }
    }

    /**
     * On keyPressed event, mark the key as pressed.
     * @param e The KeyEvent for the pressed key.
     */
    public synchronized void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode >= 0 && keyCode < keys.length) {
            keys[keyCode] = true;
        }
    }

    /**
     * On keyReleased event, mark the key as not pressed.
     * @param e The KeyEvent for the released key.
     */
    public synchronized void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode >= 0 && keyCode < keys.length) {
            keys[keyCode] = false;
        }
    }

    /**
     * The keyTyped events are not needed since we are
     * already tracking key pressed status/duration.
     * @param e Unused.
     */
    public void keyTyped(KeyEvent e) {
        // not needed
    }
}
