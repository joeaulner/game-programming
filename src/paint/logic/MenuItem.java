package paint.logic;

import java.awt.Color;

abstract public class MenuItem {

    public static final int MENU_ITEM_WIDTH = 40;
    public static final int MENU_ITEM_HEIGHT = 40;

    public int x;
    public int y;
    public Color color;

    protected MenuItem(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public abstract void onClicked();
}
