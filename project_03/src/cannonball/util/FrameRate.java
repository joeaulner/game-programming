package cannonball.util;

/**
 * A class used to calculate the game's frame rate.
 */
public class FrameRate {
    private String frameRate;
    private long lastTime;
    private long delta;
    private int frameCount;

    /**
     * Initialize the lastTime to the time the instance is created and
     * set the initial output value to show 0 frames per second.
     */
    public FrameRate() {
        lastTime = System.currentTimeMillis();
        frameRate = "FPS 0";
    }

    /**
     * Calculate the frames per second using the time since the last frame.
     */
    public void calculate() {
        long current = System.currentTimeMillis();
        delta += current - lastTime;
        lastTime = current;
        frameCount++;

        if(delta > 1000) {
            delta -= 1000;
            frameRate = String.format( "FPS %s", frameCount );
            frameCount = 0;
        }
    }

    /**
     * Returns the formatted string displaying the frame rate.
     * @return The frame rate String.
     */
    public String getFrameRate() {
        return frameRate;
    }
}
