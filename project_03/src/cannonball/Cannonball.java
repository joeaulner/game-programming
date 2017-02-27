package cannonball;

import cannonball.util.SimpleFramework;
import cannonball.world.VectorObject;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Cannonball extends SimpleFramework {

    public Cannonball() {
        appBackground = Color.WHITE;
        appBorder = Color.LIGHT_GRAY;
        appFont = new Font("Courier New", Font.PLAIN, 14);
        appBorderScale = 0.9f;
        appWidth = 640;
        appHeight = 640;
        appMaintainRatio = true;
        appSleep = 10L;
        appTitle = "FrameworkTemplate";
        appWorldWidth = 2.0f;
        appWorldHeight = 2.0f;
    }

    @Override
    protected void initialize() {
        super.initialize();
    }

    @Override
    protected void processInput(float delta) {
        super.processInput(delta);
    }

    @Override
    protected void updateObjects(float delta) {
        super.updateObjects(delta);
    }

    @Override
    protected void render(Graphics g) {
        super.render(g);

        // test rendering vector objects using viewport
        Shape rectangle = new Rectangle2D.Double(-5, -5, 10, 10);
        VectorObject thing = new VectorObject(rectangle, 0, 0, Color.BLACK, getViewportTransform());
        thing.setScale(0.01f);
        thing.updateWorld();
        thing.render(g);
    }

    @Override
    protected void terminate() {
        super.terminate();
    }

    public static void main(String[] args) {
        launchApp(new Cannonball());
    }
}
