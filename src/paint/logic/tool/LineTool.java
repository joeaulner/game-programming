package paint.logic.tool;

import paint.logic.GameState;
import paint.logic.shape.Line;
import paint.util.MouseInput;

import java.awt.*;
import java.awt.event.MouseEvent;

public class LineTool extends DrawTool {

    public LineTool(GameState state) {
        super(state, "line.png");
    }

    LineTool(GameState state, String filename) {
        super(state, filename);
    }

    @Override
    public void processInput(MouseInput mouse) {
        if (!mouse.buttonDownOnce(MouseEvent.BUTTON1)) return;

        if (start == null) {
            start = mouse.getPosition();
        } else {
            state.addShape(new Line(start, mouse.getPosition(), state.getColor()));
            start = null;
        }
    }

    @Override
    public void renderDrawPreview(Graphics g) {
        if (start == null) return;

        Point mousePos = state.getMousePos();
        g.setColor(state.getColor());
        g.drawLine(start.x, start.y, mousePos.x, mousePos.y);
    }
}
