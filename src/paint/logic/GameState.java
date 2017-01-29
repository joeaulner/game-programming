package paint.logic;

import paint.util.KeyboardInput;
import paint.util.MouseInput;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GameState {

    private ArrayList<ColorPoint> points = new ArrayList<>();
    private boolean drawingLine;
    private Point mousePos;

    final Color[] COLORS = {
        Color.BLUE,
        Color.RED,
        Color.GREEN,
        Color.BLACK
    };
    private int colorIndex = 0;

    public ArrayList<ColorPoint> getPoints() {
        return points;
    }

    public Point getMousePos() {
        return mousePos;
    }

    public void processInput(KeyboardInput keyboard, MouseInput mouse) {
        keyboard.poll();
        mouse.poll();

        mousePos = mouse.getPosition();

        // update color using scroll wheel input
        colorIndex += mouse.getNotches();
        Color color = COLORS[Math.abs(colorIndex % COLORS.length)];

        if (keyboard.keyDownOnce(MouseEvent.BUTTON1)) {
            drawingLine = true;
        }

        if (mouse.buttonDown(MouseEvent.BUTTON1)) {
            points.add(new ColorPoint(mouse.getPosition(), color));
        } else {
            points.add(null);
            drawingLine = false;
        }

        if (keyboard.keyDownOnce(KeyEvent.VK_C)) {
            points.clear();
        }
    }
}
