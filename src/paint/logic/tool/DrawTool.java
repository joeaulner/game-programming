package paint.logic.tool;

import paint.logic.*;
import paint.util.FileHelper;
import paint.util.MouseInput;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import static paint.render.MenuItem.MENU_ITEM_HEIGHT;
import static paint.render.MenuItem.MENU_ITEM_WIDTH;

public abstract class DrawTool {

    GameState state;

    private BufferedImage image;
    protected Point start;

    DrawTool(GameState state, String filename) {
        this.state = state;
        image = new FileHelper().getImage(filename);
    }

    public boolean isDrawing() {
        return start != null;
    }

    public void setActive() {
        state.setActiveTool(state.getTools().indexOf(this));
    }

    private boolean isActive() {
        return this == state.getActiveTool();
    }

    public void renderMenuItem(int x, int y, Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, 40, 40, new ImageObserver() {
                @Override
                public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                    return false;
                }
            });
        }
        if (isActive()) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.BLACK);
        }
        g.drawRect(x, y, MENU_ITEM_WIDTH, MENU_ITEM_HEIGHT);
    }

    public abstract void processInput(MouseInput mouse);

    public abstract void renderDrawPreview(Graphics g);
}
