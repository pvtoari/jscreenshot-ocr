package com.pvtoari.jocrscreenshot.utils;

import java.awt.image.BufferedImage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class CaptureWindow extends JFrame {

    private static final int FADE_DELAY = 25;

    private Point startPoint;
    private Point endPoint;
    public BufferedImage capturedImage;
    public boolean recentlyCaptured;
    public boolean isCapturing;

    public CaptureWindow() {
        setUndecorated(true);

        startPoint = new Point(0,0);
        endPoint = new Point(1,1);

        capturedImage = captureScreen();
        recentlyCaptured = false;
        isCapturing = false;
    }

    public void startCapturing() {
        isCapturing = true;
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setOpacity(0.0f);  // Inicialmente, la ventana es completamente transparente

        setVisible(true);

        // fade in
        doFadeIn();
        
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent event) {
                startPoint = event.getPoint();
            }

            public void mouseReleased(java.awt.event.MouseEvent event) {
                endPoint = event.getPoint();
                try {
                    capturedImage = captureScreen();
                } catch (Exception e) {
                    //e.printStackTrace();
                    System.out.println("Something went wrong while capturing the screen");
                }

                doFadeOut();
                //debugImage(capturedImage);
                recentlyCaptured = true;
                isCapturing = false;
            }
        });

        
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                endPoint = evt.getPoint();
                repaint();  // Redibujar para mostrar la selección
            }
        });
    }

    private void doFadeOut() {
        // idk why did i do here but somehow i managed to make like a screenshot effect lol
        Timer timer = new Timer(FADE_DELAY, new ActionListener() {
            float opacity = 0.3f;

            @Override
            public void actionPerformed(ActionEvent e) {
                opacity -= 0.05f;
                if (opacity <= 0.0f) {
                    opacity = 0.0f;
                    ((Timer) e.getSource()).stop();
                    dispose();
                }
                setOpacity(opacity);
            }
        });
        timer.start();
    }

    private void doFadeIn() {
        Timer timer = new Timer(FADE_DELAY, new ActionListener() {
            float opacity = 0.0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                opacity += 0.05f;
                if (opacity >= 0.3f) {
                    opacity = 0.3f;
                    ((Timer) e.getSource()).stop();
                }
                setOpacity(opacity);
            }
        });
        timer.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (startPoint != null && endPoint != null) {
            g.setColor(Color.magenta);
            g.drawRect(Math.min(startPoint.x, endPoint.x), Math.min(startPoint.y, endPoint.y),
                    Math.abs(startPoint.x - endPoint.x), Math.abs(startPoint.y - endPoint.y));
        }
    }

    private BufferedImage captureScreen() {
        BufferedImage img = null;

        this.setOpacity(0.0f);
        try {
            Robot robot = new Robot();
            Rectangle captureRect = new Rectangle(Math.min(startPoint.x, endPoint.x),
                    Math.min(startPoint.y, endPoint.y),
                    Math.abs(startPoint.x - endPoint.x),
                    Math.abs(startPoint.y - endPoint.y));

            img = robot.createScreenCapture(captureRect);

            //ImageIO.write(capturedImage, "png", new File("captura.png"));
        } catch (Exception e) {
            System.out.println("Something went wrong while capturing the screen");
            //e.printStackTrace();
        }

        return img;
    }

    @SuppressWarnings("unused")
    private void debugImage(BufferedImage img) {
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(img)));
        frame.pack();
        frame.setVisible(true);
    }
}
