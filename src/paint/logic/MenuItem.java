package paint.logic;

import paint.logic.tool.DrawTool;

public class MenuItem {

    public static final int MENU_ITEM_WIDTH = 40;
    public static final int MENU_ITEM_HEIGHT = 40;

    public int x;
    public int y;
    public int colorIndex;
    public DrawTool tool;

    public MenuItem(int x, int y, int colorIndex) {
        this.x = x;
        this.y = y;
        this.colorIndex = colorIndex;
    }

    public MenuItem(int x, int y, DrawTool tool) {
        this.x = x;
        this.y = y;
        this.tool = tool;
    }

    void setActiveTool() {
        tool.setActive();
    }

    void setColor() {
        GameState.getInstance().setColorIndex(colorIndex);
    }
}
