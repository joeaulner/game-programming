package paint.logic.tool;

import paint.logic.GameState;
import paint.logic.shape.Rectangle;
import paint.util.ImageHelper;
import paint.util.MouseInput;

import java.awt.*;
import java.awt.event.MouseEvent;

public class RectangleTool extends DrawTool {

    public RectangleTool() {
        image = new ImageHelper().getImage("rectangle.png");
    }

    @Override
    public void processInput(MouseInput mouse) {
        if (!mouse.buttonDownOnce(MouseEvent.BUTTON1)) return;

        GameState state = GameState.getInstance();
        Point pos = state.getMousePos();
        if (start == null) {
            start = pos;
        } else {
            int x = Math.min(start.x, pos.x);
            int y = Math.min(start.y, pos.y);
            int w = Math.abs(start.x - pos.x);
            int h = Math.abs(start.y - pos.y);
            state.addShape(new Rectangle(x, y, w, h, state.getColor()));
            start = null;
        }
    }

    @Override
    public void renderDrawPreview(Graphics g) {
        if (start == null) return;

        GameState state = GameState.getInstance();
        Point pos = state.getMousePos();
        int x = Math.min(start.x, pos.x);
        int y = Math.min(start.y, pos.y);
        int w = Math.abs(start.x - pos.x);
        int h = Math.abs(start.y - pos.y);

        g.setColor(state.getColor());
        g.drawRect(x, y, w, h);
    }
}
