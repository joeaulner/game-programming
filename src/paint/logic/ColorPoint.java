package paint.logic;

import java.awt.*;

public class ColorPoint extends Point {

    public Color color;

    public ColorPoint(Point p, Color c) {
        super(p);
        color = c;
    }
}
