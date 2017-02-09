package transformations.render;

import java.awt.*;

/**
 * Represents an entity as part of the game state
 * that can update its internal state and render itself.
 */
public interface Drawable {
    /**
     * Update the internal state of the entity.
     */
    void updateWorld();

    /**
     * Render the entity using the provided Graphics instance.
     * @param g Graphics instance used to render the entity.
     */
    void render(Graphics g);
}
