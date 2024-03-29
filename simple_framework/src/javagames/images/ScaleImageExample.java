package javagames.images;

import java.awt.*;
import java.awt.image.*;
import java.util.Hashtable;
import javagames.util.SimpleFramework;

public class ScaleImageExample extends SimpleFramework {

    private static final int IMG_WIDTH = 320;
    private static final int IMG_HEIGHT = 320;
    private BufferedImage sprite;
    private Image averaged;
    private double averagedSpeed;
    private Image nearestNeighbor;
    private double nearestSpeed;
    private BufferedImage nearest2;
    private double nearest2Speed;
    private BufferedImage bilinear;
    private double bilinearSpeed;
    private BufferedImage bicubic;
    private double bicubicSpeed;
    private BufferedImage stepDownBilinear;
    private double stepDownBilinearSpeed;
    private BufferedImage stepDownBicubic;
    private double stepDownBicubicSpeed;

    public ScaleImageExample() {
        appWidth = 960;
        appHeight = 570;
        appBackground = Color.DARK_GRAY;
        appSleep = 1L;
        appTitle = "Scale Image Example";
    }

    @Override
    protected void initialize() {
        super.initialize();
        System.out.println("Creating test image...");
        createTestImage();
        System.out.println("Generating Averaged Image");
        long start = System.nanoTime();
        for (int i = 0; i < 100; ++i) {
            generateAveragedInstance();
        }
        long end = System.nanoTime();
        averagedSpeed = (end - start) / 1.0E6;
        averagedSpeed /= 100;
        System.out.println("Generating Nearest Neighbor Image");
        start = System.nanoTime();
        for (int i = 0; i < 100; ++i) {
            generateNearestNeighbor();
        }
        end = System.nanoTime();
        nearestSpeed = (end - start) / 1.0E6;
        nearestSpeed /= 100;
        System.out.println("Generating Nearest Neighbor 2");
        start = System.nanoTime();
        for (int i = 0; i < 100; ++i) {
            nearest2 = scaleWithGraphics(RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        }
        end = System.nanoTime();
        nearest2Speed = (end - start) / 1.0E6;
        nearest2Speed /= 100;
        System.out.println("Generating Bilinear");
        start = System.nanoTime();
        for (int i = 0; i < 100; ++i) {
            bilinear = scaleWithGraphics(RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        }
        end = System.nanoTime();
        bilinearSpeed = (end - start) / 1.0E6;
        bilinearSpeed /= 100;
        System.out.println("Generating Bicubic");
        start = System.nanoTime();
        for (int i = 0; i < 100; ++i) {
            bicubic = scaleWithGraphics(RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        }
        end = System.nanoTime();
        bicubicSpeed = (end - start) / 1.0E6;
        bicubicSpeed /= 100;
        System.out.println("Generating Step Down Bilinear");
        start = System.nanoTime();
        for (int i = 0; i < 100; ++i) {
            stepDownBilinear = scaleDownImage(RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        }
        end = System.nanoTime();
        stepDownBilinearSpeed = (end - start) / 1.0E6;
        stepDownBilinearSpeed /= 100;
        System.out.println("Generating Step Down Bicubic");
        start = System.nanoTime();
        for (int i = 0; i < 100; ++i) {
            stepDownBicubic = scaleDownImage(RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        }
        end = System.nanoTime();
        stepDownBicubicSpeed = (end - start) / 1.0E6;
        stepDownBicubicSpeed /= 100;
    }

    private void createTestImage() {
        sprite = new BufferedImage(IMG_WIDTH, IMG_HEIGHT,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = sprite.createGraphics();
        // draw checker board
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, IMG_WIDTH / 2, IMG_HEIGHT / 2);
        g2d.fillRect(IMG_WIDTH / 2, IMG_HEIGHT / 2, IMG_WIDTH, IMG_HEIGHT);
        g2d.setColor(Color.BLACK);
        g2d.fillRect(IMG_WIDTH / 2, 0, IMG_WIDTH, IMG_HEIGHT / 2);
        g2d.fillRect(0, IMG_HEIGHT / 2, IMG_WIDTH / 2, IMG_HEIGHT);
        // draw red diamond
        g2d.setColor(Color.RED);
        g2d.drawLine(0, sprite.getHeight() / 2, sprite.getWidth() / 2, 0);
        g2d.drawLine(sprite.getWidth() / 2, 0, sprite.getWidth(),
                sprite.getHeight() / 2);
        g2d.drawLine(sprite.getWidth(), sprite.getHeight() / 2,
                sprite.getWidth() / 2, sprite.getHeight());
        g2d.drawLine(sprite.getWidth() / 2, sprite.getHeight(), 0,
                sprite.getHeight() / 2);
        // draw circle
        g2d.drawOval(0, 0, sprite.getWidth(), sprite.getHeight());
        // draw hash lines
        g2d.setColor(Color.GREEN);
        int dx = sprite.getWidth() / 18;
        for (int i = 0; i < sprite.getWidth(); i += dx) {
            g2d.drawLine(i, 0, i, sprite.getHeight());
        }
        g2d.setColor(Color.BLUE);
        dx = sprite.getHeight() / 18;
        for (int i = 0; i < sprite.getHeight(); i += dx) {
            g2d.drawLine(0, i, sprite.getWidth(), i);
        }
        // gradient circle
        float x1 = sprite.getWidth() / 4;
        float x2 = sprite.getWidth() * 3 / 4;
        float y1 = sprite.getHeight() / 4;
        float y2 = sprite.getHeight() * 3 / 4;
        GradientPaint gp = new GradientPaint(x1, y1, Color.BLACK, x2, y2,
                Color.WHITE);
        g2d.setPaint(gp);
        g2d.fillOval(sprite.getWidth() / 4, sprite.getHeight() / 4,
                sprite.getWidth() / 2, sprite.getHeight() / 2);
        g2d.setFont(new Font("Arial", Font.BOLD, 42));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawString("Pg.1", sprite.getWidth() / 2 - 40,
                sprite.getHeight() / 2 - 20);
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawString("Pg.2", sprite.getWidth() / 2 - 40,
                sprite.getHeight() / 2 + 40);
        g2d.dispose();
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
        // Test Image
        g.drawImage(sprite, (canvas.getWidth() - sprite.getWidth()) / 2, 50,
                null);
        int sw = averaged.getWidth(null);
        int sh = averaged.getHeight(null);
        int pos = canvas.getHeight() - sh - 50;
        int textPos = pos + sh;
        // Averaged Image
        int imgPos = (sw + 50) * 0 + 50;
        g.drawImage(averaged, imgPos, pos, null);
        String time = String.format("%.4f ms", averagedSpeed);
        g.drawString("Area Avg", 50, textPos + 15);
        g.drawString(time, 50, textPos + 30);
        // Nearest Image
        imgPos = (sw + 50) * 1 + 50;
        g.drawImage(nearestNeighbor, imgPos, pos, null);
        time = String.format("%.4f ms", nearestSpeed);
        g.drawString("Nearest", imgPos, textPos + 15);
        g.drawString(time, imgPos, textPos + 30);
        // Nearest2 Image
        imgPos = (sw + 50) * 2 + 50;
        g.drawImage(nearest2, imgPos, pos, null);
        time = String.format("%.4f ms", nearest2Speed);
        g.drawString("Nearest 2", imgPos, textPos + 15);
        g.drawString(time, imgPos, textPos + 30);
        // Bilinear Image
        imgPos = (sw + 50) * 3 + 50;
        g.drawImage(bilinear, imgPos, pos, null);
        time = String.format("%.4f ms", bilinearSpeed);
        g.drawString("Bilinear", imgPos, textPos + 15);
        g.drawString(time, imgPos, textPos + 30);
        // Bicubic Image
        imgPos = (sw + 50) * 4 + 50;
        g.drawImage(bicubic, imgPos, pos, null);
        time = String.format("%.4f ms", bicubicSpeed);
        g.drawString("Bicubic", imgPos, textPos + 15);
        g.drawString(time, imgPos, textPos + 30);
        // Step Down Bilinear Image
        imgPos = (sw + 50) * 5 + 50;
        g.drawImage(stepDownBilinear, imgPos, pos, null);
        time = String.format("%.4f ms", stepDownBilinearSpeed);
        g.drawString("Bilin-Step", imgPos, textPos + 15);
        g.drawString(time, imgPos, textPos + 30);
        // Step Down Bicubic Image
        imgPos = (sw + 50) * 6 + 50;
        g.drawImage(stepDownBicubic, imgPos, pos, null);
        time = String.format("%.4f ms", stepDownBicubicSpeed);
        g.drawString("Bicub-Step", imgPos, textPos + 15);
        g.drawString(time, imgPos, textPos + 30);
    }

    private void generateAveragedInstance() {
        averaged = sprite.getScaledInstance(sprite.getWidth() / 4,
                sprite.getHeight() / 4, Image.SCALE_AREA_AVERAGING);
        averaged.getSource().startProduction(getConsumer());
    }

    private void generateNearestNeighbor() {
        nearestNeighbor = sprite.getScaledInstance(sprite.getWidth() / 4,
                sprite.getHeight() / 4, Image.SCALE_REPLICATE);
        nearestNeighbor.getSource().startProduction(getConsumer());
    }

    private BufferedImage scaleWithGraphics(Object hintValue) {
        BufferedImage image = new BufferedImage(sprite.getWidth() / 4,
                sprite.getHeight() / 4, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hintValue);
        g2d.drawImage(sprite, 0, 0, image.getWidth(), image.getHeight(), null);
        g2d.dispose();
        return image;
    }

    private BufferedImage scaleDownImage(Object hintValue) {
        BufferedImage ret = sprite;
        int targetWidth = sprite.getWidth() / 4;
        int targetHeight = sprite.getHeight() / 4;
        int w = sprite.getWidth();
        int h = sprite.getHeight();
        do {
            w = w / 2;
            if (w < targetWidth) {
                w = targetWidth;
            }
            h = h / 2;
            if (h < targetHeight) {
                h = targetHeight;
            }
            BufferedImage tmp = new BufferedImage(w, h,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = tmp.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hintValue);
            g2d.drawImage(ret, 0, 0, w, h, null);
            g2d.dispose();
            ret = tmp;
        } while (w != targetWidth || h != targetHeight);
        return ret;
    }

    private ImageConsumer getConsumer() {
        return new ImageConsumer() {
            public void setProperties(Hashtable<?, ?> props) {
            }

            public void setPixels(int x, int y, int w, int h, ColorModel model,
                                  int[] pixels, int off, int scansize) {
            }

            public void setPixels(int x, int y, int w, int h, ColorModel model,
                                  byte[] pixels, int off, int scansize) {
            }

            public void setHints(int hintflags) {
            }

            public void setDimensions(int width, int height) {
            }

            public void setColorModel(ColorModel model) {
            }

            public void imageComplete(int status) {
            }
        };
    }

    public static void main(String[] args) {
        launchApp(new ScaleImageExample());
    }
}
