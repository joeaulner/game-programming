package transformations.logic;

import transformations.render.VectorObject;
import transformations.util.KeyboardInput;
import transformations.util.MouseInput;

import java.awt.*;

public class State {

    private Point mousePos;
    private static State instance = new State();

    private VectorObject square;

    private State() {
        square = new VectorObject(new Rectangle(-20, -20, 40, 40), 0, 0, Color.BLUE);
    }

    public static State getInstance() {
        return instance;
    }

    public Point getMousePos() {
        return mousePos;
    }

    public VectorObject[] getVectorObjects() {
        return new VectorObject[] { square };
    }

    public void processInput(KeyboardInput keyboard, MouseInput mouse) {
        keyboard.poll();
        mouse.poll();

        mousePos = mouse.getPosition();
        square.updateWorld();
    }

    private void updateSquarePosition() {

    }
}
