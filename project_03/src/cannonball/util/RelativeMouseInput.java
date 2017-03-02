package cannonball.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A mouse input listener that provides methods for
 * tracking mouse movement, checking initial button presses,
 * and continuous button presses.
 */
public class RelativeMouseInput
        implements MouseListener, MouseMotionListener, MouseWheelListener {

    private static final int BUTTON_COUNT = 3;

    private Point mousePos;
    private Point currentPos;
    private boolean[] mouse;
    private int[] polled;
    private int notches;
    private int polledNotches;

    private int dx, dy;
    private Robot robot;
    private Component component;
    private boolean relative;

    /**
     * Initialize the mouse position and button status/duration arrays.
     * @param component The component the mouse position is relative to.
     */
    public RelativeMouseInput(Component component) {
        this.component = component;
        try {
            robot = new Robot();
        } catch(Exception ex) {
            // handle exception (game specific)
            ex.printStackTrace();
        }
        mousePos = new Point(0, 0);
        currentPos = new Point(0, 0);
        mouse = new boolean[BUTTON_COUNT];
        polled = new int[BUTTON_COUNT];
    }

    /**
     * Store the current mouse position and scroll wheel notches.
     * Increment the duration of buttons currently pressed.
     * Reset the duration of buttons not pressed to zero.
     */
    public synchronized void poll() {
        if (isRelative()) {
            mousePos = new Point(dx, dy);
        } else {
            mousePos = new Point(currentPos);
        }
        dx = dy = 0;

        polledNotches = notches;
        notches = 0;

        for (int i = 0; i < mouse.length; ++i) {
            if (mouse[i]) {
                polled[i]++;
            } else {
                polled[i] = 0;
            }
        }
    }

    /**
     * Returns whether the mouse position is set to relative.
     * @returns If the mouse position is relative.
     */
    public boolean isRelative() {
        return relative;
    }

    /**
     * Set whether the mouse position is relative
     * and centers the mouse if it is set to relative.
     * @param relative Whether the mouse position is relative.
     */
    public void setRelative(boolean relative) {
        this.relative = relative;
        if (relative) {
            centerMouse();
        }
    }

    /**
     * Get the last polled mouse position.
     * @return The last polled mouse position.
     */
    public Point getPosition() {
        return mousePos;
    }

    /**
     * Get the last polled mouse wheel notches.
     * @return The mouse wheel notches.
     */
    public int getNotches() {
        return polledNotches;
    }

    /**
     * Check whether a button is currently pressed.
     * @param button The button to check for.
     * @return Whether the button is pressed.
     */
    public boolean buttonDown(int button) {
        return polled[button - 1] > 0;
    }

    /**
     * Check for an initial button press.
     * @param button The button to check for.
     * @return Whether this is the first cycle the button has been pressed.
     */
    public boolean buttonDownOnce(int button) {
        return polled[button - 1] == 1;
    }

    /**
     * On mousePressed event, mark the button as pressed.
     * @param e The MouseEvent for the pressed button.
     */
    public synchronized void mousePressed(MouseEvent e) {
        int button = e.getButton() - 1;
        if (button >= 0 && button < mouse.length) {
            mouse[button] = true;
        }
    }

    /**
     * On mouseReleased event, mark the button as not pressed.
     * @param e The MouseEvent for the released button.
     */
    public synchronized void mouseReleased(MouseEvent e) {
        int button = e.getButton() - 1;
        if (button >= 0 && button < mouse.length) {
            mouse[button] = false;
        }
    }

    /**
     * The mouseClicked events are not needed since we are
     * already tracking mouse pressed status/duration.
     * @param e Unused.
     */
    public void mouseClicked(MouseEvent e) {
        // not needed
    }

    /**
     * Store the mouse position when it enters the component.
     * @param e The MouseEvent with the current position.
     */
    public synchronized void mouseEntered(MouseEvent e) {
        mouseMoved(e);
    }

    /**
     * Store the mouse position when it exits the component.
     * @param e The MouseEvent with the current position.
     */
    public synchronized void mouseExited(MouseEvent e) {
        mouseMoved(e);
    }

    /**
     * Store the mouse position when it is dragged over the component.
     * @param e The MouseEvent with the current position.
     */
    public synchronized void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    /**
     * Store the current mouse position.
     * @param e The MouseEvent with the current position.
     */
    public synchronized void mouseMoved(MouseEvent e) {
        if (isRelative()) {
            Point p = e.getPoint();
            Point center = getComponentCenter();
            dx += p.x - center.x;
            dy += p.y - center.y;
            centerMouse();
        } else {
            currentPos = e.getPoint();
        }
    }

    /**
     * On mouseWheelMoved event, update the notches accumulator.
     * @param e The MouseEvent with the wheel rotation notches.
     */
    public synchronized void mouseWheelMoved(MouseWheelEvent e) {
        notches += e.getWheelRotation();
    }

    /**
     * Returns the center of the component the mouse position is relative to.
     * @return The center of the component.
     */
    private Point getComponentCenter() {
        int w = component.getWidth();
        int h = component.getHeight();
        return new Point(w / 2, h / 2);
    }

    /**
     * Centers the mouse position relative to component.
     */
    private void centerMouse() {
        if (robot != null && component.isShowing()) {
            Point center = getComponentCenter();
            SwingUtilities.convertPointToScreen(center, component);
            robot.mouseMove(center.x, center.y);
        }
    }
}
