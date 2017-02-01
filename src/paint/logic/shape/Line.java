package paint.logic.shape;

import java.awt.*;

public class Line extends DrawShape {

    private Point p1;
    private Point p2;
    private Color color;

    public Line(Point p1, Point p2, Color color) {
        this.p1 = p1;
        this.p2 = p2;
        this.color = color;
    }

    @Override
    public void render(Graphics g) {
        g.setColor(color);
        g.drawLine(p1.x, p1.y, p2.x, p2.y);
    }
}
