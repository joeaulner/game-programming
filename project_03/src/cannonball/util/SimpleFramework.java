package cannonball.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

/**
 * A simple framework for constructing a windowed game. It is intended to be
 * extended by a new class to implement the game logic, but provides reasonable
 * defaults for most properties.
 */
public class SimpleFramework extends JFrame implements Runnable {

    private BufferStrategy bs;
    private volatile boolean running;
    private Thread gameThread;

    protected FrameRate frameRate;
    protected Canvas canvas;
    protected RelativeMouseInput mouse;
    protected KeyboardInput keyboard;

    protected Color appBackground = Color.BLACK;
    protected Color appBorder = Color.LIGHT_GRAY;
    protected Color appFPSColor = Color.GREEN;
    protected Font appFont = new Font("Courier New", Font.PLAIN, 14);
    protected String appTitle = "TBD-Title";

    protected float appBorderScale = 0.8f;
    protected int appWidth = 640;
    protected int appHeight = 480;
    protected float appWorldWidth = 2.0f;
    protected float appWorldHeight = 2.0f;
    protected long appSleep = 10L;
    protected boolean appMaintainRatio = false;

    /**
     * Empty constructor.
     */
    public SimpleFramework() {

    }

    /**
     * Creates a canvas on which the game will be drawn. Resizes the canvas based on
     * the app height/width, the border scale, and whether the app ratio is to be maintained.
     */
    protected void createAndShowGUI() {
        canvas = new Canvas();
        canvas.setBackground(appBackground);
        canvas.setIgnoreRepaint(true);
        getContentPane().add(canvas);
        setLocationByPlatform(true);
        if (appMaintainRatio) {
            getContentPane().setBackground(appBorder);
            setSize(appWidth, appHeight);
            // set initial canvas dimensions using border scale
            // so viewport matrix is correct during initialization
            int cW = (int) (appWidth * appBorderScale);
            int cH = (int) (appHeight * appBorderScale);
            canvas.setSize(cW, cH);
            setLayout(null);
            getContentPane().addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    onComponentResized(e);
                }
            });
        } else {
            canvas.setSize(appWidth, appHeight);
            pack();
        }
        setTitle(appTitle);

        // Register key event listener
        keyboard = new KeyboardInput();
        canvas.addKeyListener(keyboard);

        // Register mouse event listeners
        mouse = new RelativeMouseInput(canvas);
        canvas.addMouseListener(mouse);
        canvas.addMouseMotionListener(mouse);
        canvas.addMouseWheelListener(mouse);

        // Make the window visible, create the buffer strategy, request focus, and start the game thread
        setVisible(true);
        canvas.createBufferStrategy(2);
        bs = canvas.getBufferStrategy();
        canvas.requestFocus();
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * When the component is resized and the app ratio is being maintained,
     * calculate the new dimensions required to maintain the same canvas ratio.
     * @param e Unused.
     */
    protected void onComponentResized(ComponentEvent e) {
        Dimension size = getContentPane().getSize();
        int vw = (int) (size.width * appBorderScale);
        int vh = (int) (size.height * appBorderScale);
        int vx = (size.width - vw) / 2;
        int vy = (size.height - vh) / 2;
        int newW = vw;
        int newH = (int) (vw * appWorldHeight / appWorldWidth);
        if (newH > vh) {
            newW = (int) (vh * appWorldWidth / appWorldHeight);
            newH = vh;
        }
        // center
        vx += (vw - newW) / 2;
        vy += (vh - newH) / 2;
        canvas.setLocation(vx, vy);
        canvas.setSize(newW, newH);
    }

    /**
     * Returns the transformation matrix for the viewport generated using the app and world dimensions.
     * @return The transformation matrix.
     */
    protected Matrix3x3f getViewportTransform() {
        return Utility.createViewport(appWorldWidth, appWorldHeight, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * Returns the reverse transformation matrix for the viewport generated using the app and world dimensions.
     * @return The reverse transformation matrix.
     */
    protected Matrix3x3f getReverseViewportTransform() {
        return Utility.createReverseViewport(appWorldWidth, appWorldHeight, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * Get the mouse position relative to the world coordinates.
     * @return The mouse position.
     */
    protected Vector2f getWorldMousePosition() {
        Matrix3x3f screenToWorld = getReverseViewportTransform();
        Point mousePos = mouse.getPosition();
        return screenToWorld.mul(new Vector2f(mousePos.x, mousePos.y));
    }

    /**
     * Get the mouse position relative to the application.
     * @return The mouse position.
     */
    protected Vector2f getRelativeWorldMousePosition() {
        float sx = appWorldWidth / (canvas.getWidth() - 1);
        float sy = appWorldHeight / (canvas.getHeight() - 1);
        Point p = mouse.getPosition();
        return Matrix3x3f.identity()
                .mul(Matrix3x3f.scale(sx, -sy))
                .mul(new Vector2f(p.x, p.y));
    }

    /**
     * Initialize the application and start the game loop,
     * calculating time delta with each iteration.
     */
    public void run() {
        running = true;
        initialize();
        long curTime = System.nanoTime();
        long lastTime = curTime;
        double nsPerFrame;
        while (running) {
            curTime = System.nanoTime();
            nsPerFrame = curTime - lastTime;
            gameLoop((float) (nsPerFrame / 1.0E9));
            lastTime = curTime;
        }
        terminate();
    }

    /**
     * Initialize the frameRate object used during development.
     */
    protected void initialize() {
        frameRate = new FrameRate();
    }

    /**
     * Empty method that can be overridden for any cleanup on app close.
     */
    protected void terminate() {

    }

    /**
     * Execute one iteration of the game loop, processing input, updating
     * the game objects, rendering the frame, and sleeping the thread.
     * @param delta
     */
    private void gameLoop(float delta) {
        processInput(delta);
        updateObjects(delta);
        renderFrame();
        sleep(appSleep);
    }

    /**
     * Render the frame by getting the graphics object used by the buffer strategy,
     * clearing the image, and calling the render method to draw the game image.
     * Instabilities with the buffer strategy are taken into account using
     * do/while loops.
     */
    private void renderFrame() {
        do {
            do {
                Graphics g = null;
                try {
                    g = bs.getDrawGraphics();
                    g.clearRect(0, 0, getWidth(), getHeight());
                    render(g);
                } finally {
                    if (g != null) {
                        g.dispose();
                    }
                }
            } while (bs.contentsRestored());
            bs.show();
        } while (bs.contentsLost());
    }

    /**
     * Sleep the main app thread a specified amount of time (ns).
     * @param appSleep The length of time to sleep the thread.
     */
    private void sleep(long appSleep) {
        try {
            Thread.sleep(appSleep);
        } catch (InterruptedException ex) {
            // ignore exceptions when only sleeping
        }
    }

    /**
     * Poll keyboard and mouse input.
     * @param delta Unused.
     */
    protected void processInput(float delta) {
        keyboard.poll();
        mouse.poll();
    }

    /**
     * Empty method intended to be overridden to provide a place
     * to update game objects.
     * @param delta Unused.
     */
    protected void updateObjects(float delta) {

    }

    /**
     * Render the current frame using the provided Graphics object.
     * Displays the calculated frames per second.
     * @param g The Graphics object used to render.
     */
    protected void render(Graphics g) {
        g.setFont(appFont);
        g.setColor(appFPSColor);
        frameRate.calculate();
        g.drawString(frameRate.getFrameRate(), 20, 20);
    }

    /**
     * When the application is being closed, end the game loop by
     * setting running to false, then join the thread.
     */
    protected void onWindowClosing() {
        try {
            running = false;
            gameThread.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.exit(0);
    }

    /**
     * Launch the application by registering the windowClosing event
     * and asynchronously invoking the createAndShowGUI method.
     * @param app
     */
    protected static void launchApp(final SimpleFramework app) {
        app.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                app.onWindowClosing();
            }
        });
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                app.createAndShowGUI();
            }
        });
    }
}
