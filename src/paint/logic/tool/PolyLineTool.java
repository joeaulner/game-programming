package paint.logic.tool;

import paint.logic.GameState;
import paint.logic.shape.Line;
import paint.util.FileHelper;
import paint.util.MouseInput;

import java.awt.*;
import java.awt.event.MouseEvent;

public class PolyLineTool extends LineTool {

    public PolyLineTool() {
        image = new FileHelper().getImage("poly-line.png");
    }

    @Override
    public void processInput(MouseInput mouse) {
        if (mouse.buttonDownOnce(MouseEvent.BUTTON1)) {
            if (start == null) {
                start = mouse.getPosition();
            } else {
                GameState state = GameState.getInstance();
                state.addShape(new Line(start, mouse.getPosition(), state.getColor()));
                start = mouse.getPosition();
            }
        } else if (mouse.buttonDownOnce(MouseEvent.BUTTON3)) {
            start = null;
        }
    }

    @Override
    public void renderDrawPreview(Graphics g) {
        super.renderDrawPreview(g);
    }
}
