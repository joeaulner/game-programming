package transformations.render;

import java.awt.*;

public interface Drawable {
    // Update the World Matrix
    void updateWorld();

    // Draw the object with Graphics parameter
    void render(Graphics g);
}
