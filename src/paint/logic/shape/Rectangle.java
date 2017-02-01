package paint.logic.shape;

import java.awt.*;

public class Rectangle extends DrawShape {

    private int x;
    private int y;
    private int w;
    private int h;

    public Rectangle(int x, int y, int w, int h, Color color) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.color = color;
    }

    @Override
    public void render(Graphics g) {
        g.setColor(color);
        g.drawRect(x, y, w, h);
    }
}
