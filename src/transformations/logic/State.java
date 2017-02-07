package transformations.logic;

import transformations.util.KeyboardInput;
import transformations.util.MouseInput;

import java.awt.*;

public class State {

    private Point mousePos;
    private static State instance = new State();

    private State() {

    }

    public static State getInstance() {
        return instance;
    }

    public Point getMousePos() {
        return mousePos;
    }

    public void processInput(KeyboardInput keyboard, MouseInput mouse) {
        keyboard.poll();
        mouse.poll();

        mousePos = mouse.getPosition();
    }
}
