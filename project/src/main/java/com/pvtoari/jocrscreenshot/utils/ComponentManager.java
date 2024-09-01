package com.pvtoari.jocrscreenshot.utils;

import javax.swing.*;

import net.sourceforge.tess4j.TesseractException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class ComponentManager {

    private static final Component SEPARATOR = Box.createRigidArea(new Dimension(0,10));

    private Thread instanceThread;

    private CaptureWindow ssManager;
    private TesseractManager tessManager;

    private JPanel instancePanel;
    private JLabel instanceHeader;
    private JButton instanceButton;
    private JTextPane instanceContent;
    private JLabel instanceImage;
    private JFrame instanceFrame;

    public ComponentManager() {
        ssManager = new CaptureWindow();
        tessManager = new TesseractManager();

        instancePanel = getPanelComponent();

        instanceHeader = getHeaderComponent();
        instanceButton = getCaptureButtonComponent();
        instanceContent = getContentComponent();
        instanceImage = getImageComponent();

        instanceFrame = getFrameComponent();

    }

    public void start() {
        drawHUD();

        instanceThread = new Thread(() -> {
            while (true) {

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(ssManager.recentlyCaptured) {
                    System.out.println("Captured image");
                    try {
                        instanceImage = getImageComponent(ssManager.capturedImage);
                        
                        String text = tessManager.getTextFromImage(ssManager.capturedImage);

                        setContentText(text);

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        System.out.println("Catched null pointer exception, image is null");
                    } catch(TesseractException e) {
                        System.out.println("Error while performing OCR from image");
                    }

                    ssManager.recentlyCaptured = false;

                    instanceImage.revalidate();
                    instanceImage.repaint();
                }
            }
        });

        instanceThread.start();
    }

    private void setContentText(String text) {
        instanceContent.setText(text);

        instanceContent.revalidate();
        instanceContent.repaint();
    }

    private void drawHUD() {

        // añadir Componentos al panel
        instancePanel.add(instanceHeader);
        instancePanel.add(SEPARATOR); // separación
        instancePanel.add(instanceImage);
        instancePanel.add(instanceContent);
        instancePanel.add(SEPARATOR); // separación
        instancePanel.add(instanceButton);

        // añadir panel a la ventana
        instanceFrame.add(instancePanel, BorderLayout.CENTER);

        // mostrar la ventana
        //instanceFrame.pack();
        instanceFrame.setVisible(true);
    }

    private JPanel getPanelComponent() {
        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        return panel;
    }

    private JFrame getFrameComponent() {
        JFrame frame = new JFrame("Screen Capture Tool");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        return frame;
    }

    private JButton getCaptureButtonComponent() {
        JButton captureButton = new JButton("Capture Screen");

        captureButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        captureButton.setPreferredSize(new Dimension(150, 25));
        captureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ssManager.startCapturing();
            }
        });

        return captureButton;
    }

    private JTextPane getContentComponent() {
        JTextPane content = new JTextPane();

        content.setText("Content goes here...");
        content.setEditable(false);
        content.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        return content;
    }

    private JLabel getImageComponent() {
        JLabel image = new JLabel();

        image.setAlignmentX(Component.CENTER_ALIGNMENT);

        Image img = ssManager.capturedImage.getScaledInstance(400, 200, Image.SCALE_SMOOTH);
        image.setIcon(new ImageIcon(img));

        return image;
    }

    private JLabel getImageComponent(BufferedImage img) throws NullPointerException {

        if(img == null) {
            throw new NullPointerException("Image is null");
        }

        JLabel image = instanceImage;

        image.setAlignmentX(Component.CENTER_ALIGNMENT);

        image.setIcon(new ImageIcon(img.getScaledInstance(400, 200, Image.SCALE_SMOOTH)));

        return image;
    }

    private JLabel getHeaderComponent() {
        JLabel header = new JLabel("OCRed content:");
        
        header.setAlignmentX(Component.CENTER_ALIGNMENT);

        return header;
    }
}
