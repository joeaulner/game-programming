package paint.logic.tool;

import paint.logic.GameState;
import paint.logic.shape.Line;
import paint.util.MouseInput;

import java.awt.*;
import java.awt.event.MouseEvent;

public class FreeDrawTool extends DrawTool {

    public FreeDrawTool(GameState state) {
        super(state, "free-draw.png");
    }

    @Override
    public void processInput(MouseInput mouse) {
        if (mouse.buttonDown(MouseEvent.BUTTON1)) {
            Point mousePos = state.getMousePos();

            if (start == null) {
                start = mousePos;
            } else {
                state.addShape(new Line(start, mousePos, state.getColor()));
                start = mousePos;
            }
        } else {
            start = null;
        }
    }

    @Override
    public void renderDrawPreview(Graphics g) {
        // no draw preview, a new line is added with each new mouse event
    }
}
