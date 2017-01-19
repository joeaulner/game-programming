package ch1.javagames.render;

import java.awt.*;
import javax.swing.*;
import ch1.javagames.util.*;

public class HelloWorldApp extends JFrame {
    private FrameRate frameRate;

    private HelloWorldApp() {
        frameRate = new FrameRate();
    }

    private void createAndShowGUI() {
        GamePanel gamePanel = new GamePanel();
        gamePanel.setBackground(Color.BLACK);
        gamePanel.setPreferredSize(new Dimension(320, 240));
        getContentPane().add(gamePanel);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Hello World!");
        pack();
        setVisible(true);
    }

    private class GamePanel extends JPanel {
        public void paint(Graphics g) {
            super.paint(g);
            onPaint(g);
        }
    }

    private void onPaint(Graphics g) {
        frameRate.calculate();
        g.setColor(Color.WHITE);
        g.drawString(frameRate.getFrameRate(), 30, 30);
        repaint();
    }

    public static void main(String[] args) {
        final HelloWorldApp app = new HelloWorldApp();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                app.createAndShowGUI();
            }
        });
    }
}
