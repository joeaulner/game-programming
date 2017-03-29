package sprites;

import sprites.util.SimpleFramework;
import sprites.world.SpriteObject;

import java.awt.*;

public class Sprites extends SimpleFramework {

    SpriteObject spriteObject;

    public Sprites() {
        appWidth = 1280;
        appHeight = 640;
        appBorderScale = 1.0f;
        appWorldWidth = 4;
        appWorldHeight = 2;
        appMaintainRatio = true;
    }

    @Override
    protected void initialize() {
        super.initialize();
        spriteObject = new SpriteObject("assets/tiles/1.png", 0, 0);
    }

    @Override
    protected void render(Graphics g) {
        super.render(g);
        spriteObject.setViewport(getViewportTransform());
        spriteObject.updateWorld();
        spriteObject.render(g);
    }

    public static void main(String[] args) {
        launchApp(new Sprites());
    }
}
