package paint.render;

import paint.logic.GameState;
import paint.logic.tool.DrawTool;

import java.awt.*;

public class MenuItem {

    public static final int MENU_ITEM_WIDTH = 40;
    public static final int MENU_ITEM_HEIGHT = 40;

    public int x;
    public int y;
    int colorIndex;
    DrawTool tool;
    private GameState state;

    private MenuItem(int x, int y) {
        state = GameState.getInstance();
        this.x = x;
        this.y = y;
    }

    MenuItem(int x, int y, int colorIndex) {
        this(x, y);
        this.colorIndex = colorIndex;
    }

    MenuItem(int x, int y, DrawTool tool) {
        this(x, y);
        this.tool = tool;
    }

    // set the correct color/tool when menu item is selected
    public void onSelected() {
        if (tool == null) {
            state.setColorIndex(colorIndex);
        } else {
            tool.setActive();
        }
    }

    // check if a set of x,y coordinates fall within this menu item
    public boolean contains(int x, int y) {
        Rectangle bounds = new Rectangle(this.x, this.y, MENU_ITEM_WIDTH, MENU_ITEM_HEIGHT);
        return bounds.contains(x, y);
    }
}
