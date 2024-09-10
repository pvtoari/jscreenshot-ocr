package com.pvtoari.jocrscreenshot.utils;

import javax.imageio.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.util.*;

import net.sourceforge.tess4j.TesseractException;

public class ComponentManager {

    private static final Component SEPARATOR = Box.createRigidArea(new Dimension(0,10));

    private Thread instanceThread;

    private CaptureWindow ssManager;
    private TesseractManager tessManager;

    private JPanel instancePanel;
    private JMenuBar instanceMenu;
    private JLabel instanceHeader;
    private JButton instanceButton;
    private JTextPane instanceContent;
    private JScrollPane instanceContentScroll;
    private JLabel instanceImage;
    private JFrame instanceFrame;

    ArrayList<Component> components;

    public ComponentManager() throws Exception {
        components = new ArrayList<>();
        ssManager = new CaptureWindow();
        
        tessManager = new TesseractManager();
        
        if(ssManager == null || tessManager == null) {
            throw new Exception("Failed to initialize CaptureWindow or TesseractManager instances");
        }
        
        setupComponents();
    }

    private JPanel getPanelComponent() {
        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        components.add(instanceMenu);
        return panel;
    }

    private JFrame getFrameComponent() {
        JFrame frame = new JFrame("JScreenshot-OCR by pvtoari");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setIconImage(getResourceImage("/img/logo.png"));

        frame.setSize(400, 600);
        frame.setLocationRelativeTo(null);
        Point pos = frame.getLocation();
        pos.translate(-550, 0);
        frame.setLocation(pos);

        frame.setLayout(new BorderLayout());
        frame.setResizable(true);

        frame.setJMenuBar(instanceMenu);

        components.add(frame);
        return frame;
    }

    private Image getResourceImage(String path) {

        Image img = null;

        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is != null) {
                img = ImageIO.read(is);
            } else {
                System.err.println("Logo image not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return img;
    }

    private JMenuBar getMenuComponent() {
        JMenuBar menubar = new MenuBuilder()
            .addMenu("File")
            .withItem("Save content", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    saveContentToFileGUI(instanceContent.getText());
                }
            })
            .withItem("Save image", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    saveImageToFileGUI();
                }
            })
            .withItem("Open image", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openImageGUI();
                }
            })
            .withItem("Exit", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            })
            .addMenu("Edit")
            .withItem("Clear content", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setContentText("");
                }
            })
            .withItem("Clear image", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    instanceImage.setIcon(null);
                }
            })
            .withItem("Clear all", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setContentText("");
                    instanceImage.setIcon(null);
                }
            })
            .withItem("Reload window", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    reloadGUI();
                }
            })
            .addMenu("Help")
            .withItem("Configuration", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    configurationGUI();
                }
            })
            .withItem("About", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showAboutGUI();
                }

            })
            .withItem("Source code", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Desktop.getDesktop().browse(new URI("https://github.com/pvtoari/jscreenshot-ocr"));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            })
            .withItem("Report issue", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Desktop.getDesktop().browse(new URI("https://github.com/pvtoari/jscreenshot-ocr/issues/new"));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            })
            
            .build();
            
            components.add(menubar);
            return menubar;
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
            
            components.add(captureButton);
            return captureButton;
        }
  
    private JTextPane getContentComponent() {
            JTextPane content = new JTextPane();

        content.setText("Content goes here...");
        content.setEditable(false);
        content.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        components.add(content);
        return content;
    }

    private JScrollPane getScrollPaneComponent() {
        JScrollPane scrollPane = new JScrollPane(instanceContent);

        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        components.add(scrollPane);
        return scrollPane;
    }

    private JLabel getImageComponent() {
        JLabel image = new JLabel();

        image.setAlignmentX(Component.CENTER_ALIGNMENT);

        Image img = ssManager.capturedImage.getScaledInstance(400, 200, Image.SCALE_SMOOTH);
        image.setIcon(new ImageIcon(img));

        components.add(image);
        return image;
    }

    private JLabel getImageComponent(BufferedImage img) throws NullPointerException {

        if(img == null) {
            throw new NullPointerException("Image is null");
        }

        JLabel image = instanceImage;

        image.setAlignmentX(Component.CENTER_ALIGNMENT);

        image.setIcon(new ImageIcon(img.getScaledInstance(400, 200, Image.SCALE_SMOOTH)));

        components.add(image);
        return image;
    }

    private JLabel getHeaderComponent() {
        JLabel header = new JLabel("OCRed content:");
        
        header.setAlignmentX(Component.CENTER_ALIGNMENT);

        components.add(header);
        return header;
    }

    private void setupComponents() {
        instancePanel = getPanelComponent();

        instanceMenu = getMenuComponent();

        instanceHeader = getHeaderComponent();
        instanceButton = getCaptureButtonComponent();
        instanceContent = getContentComponent();
        instanceContentScroll = getScrollPaneComponent();
        instanceImage = getImageComponent();

        instanceFrame = getFrameComponent();
    }

    private void showAboutGUI() {
        JPanel textsPanel = new JPanel();
        textsPanel.setLayout(new BoxLayout(textsPanel, BoxLayout.Y_AXIS));
        textsPanel.setBorder(new EmptyBorder(15, 30, 10, 10));

        JLabel label1 = new JLabel("JScreenshot-OCR");
        JLabel label2 = new JLabel("Developed by pvtoari");

        label1.setAlignmentX(Component.LEFT_ALIGNMENT);
        label2.setAlignmentX(Component.LEFT_ALIGNMENT);

        textsPanel.add(label1);
        textsPanel.add(label2);

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        ImageIcon icon = new ImageIcon(getResourceImage("/img/logo.png").getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        JLabel imgLabel = new JLabel(icon);
        logoPanel.add(imgLabel);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(textsPanel, BorderLayout.WEST);
        mainPanel.add(logoPanel, BorderLayout.EAST);

        JDialog dialog = new JDialog(instanceFrame, "About", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.getContentPane().add(mainPanel);
        dialog.setSize(250, 100);
        dialog.setLocationRelativeTo(instanceFrame);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }
    
    private void drawGUI() {

        // añadir Componentos al panel
        //instancePanel.add(instanceMenu);
        instancePanel.add(instanceImage);
        instancePanel.add(instanceHeader);
        instancePanel.add(SEPARATOR); // separación
        instancePanel.add(instanceContentScroll);
        instancePanel.add(SEPARATOR); // separación
        instancePanel.add(instanceButton);
        instancePanel.add(SEPARATOR); // separación

        // añadir panel a la ventana
        instanceFrame.add(instancePanel, BorderLayout.CENTER);

        // mostrar la ventana
        instanceFrame.pack();
        instanceFrame.setVisible(true);
    }

    private void reloadGUI() {
        for(Component c : components) {
            if(c instanceof JFrame) {
                ((JFrame) c).dispose();
            }
        }

        setupComponents();
        drawGUI();
    }

    private void saveContentToFileGUI(String toWrite) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save content to file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text format (.txt)", "txt"));
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

        int userSelection = fileChooser.showSaveDialog(instanceFrame);
    
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            fileToSave = new File(fileToSave.getAbsolutePath() + ".txt");

            try (FileWriter writer = new FileWriter(fileToSave)) {
                writer.write(toWrite);
                JOptionPane.showMessageDialog(instanceFrame, "File saved successfully!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(instanceFrame, "Error saving file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void saveImageToFileGUI() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save image to file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image format (.png)", "png"));
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

        int userSelection = fileChooser.showSaveDialog(instanceFrame);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            fileToSave = new File(fileToSave.getAbsolutePath() + ".png");
    
            try {
                ImageIO.write(ssManager.capturedImage, "png", fileToSave);
                JOptionPane.showMessageDialog(instanceFrame, "Image saved successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(instanceFrame, "Error saving image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void openImageGUI() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open image file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image format (.png, .jpg, .jpeg)", "png", "jpg", "jpeg"));
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

        int userSelection = fileChooser.showOpenDialog(instanceFrame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();
            try {
                BufferedImage img = ImageIO.read(fileToOpen);
                
                ssManager.capturedImage = img;
                ssManager.recentlyCaptured = true;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(instanceFrame, "Error opening image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void configurationGUI() {
        JFrame configFrame = new JFrame();
        configFrame.setTitle("Configuration");
        configFrame.setSize(500, 300);
        configFrame.setIconImage(getResourceImage("/img/logo.png"));
        configFrame.setLocationRelativeTo(null);
        configFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        configFrame.setResizable(true);
        configFrame.setLayout(new BorderLayout());

        JTabbedPane configTabs = new ConfigTabs(tessManager);

        configFrame.add(configTabs, BorderLayout.CENTER);

        configFrame.setVisible(true);
    }

    private void firstTimeSetup() {
        JFrame setupFrame = new JFrame();
        setupFrame.setTitle("Setup");
        setupFrame.setSize(400, 300);
        setupFrame.setIconImage(getResourceImage("/img/logo.png"));
        setupFrame.setLocationRelativeTo(null);
        setupFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupFrame.setResizable(false);
        setupFrame.setLayout(new BorderLayout());

        JPanel setupPanel = new JPanel();
        setupPanel.setLayout(new BoxLayout(setupPanel, BoxLayout.Y_AXIS));

        JLabel setupLabel = new JLabel("Thanks for using JScreenshot-OCR! Looks like it's your first time using it, let's get started. You can configure the application later on the configuration menu.");
        JLabel setupLabel2 = new JLabel("JScreenshot-OCR will be installed at " + tessManager.getInstallationPath());
        setupLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void setContentText(String text) {
        instanceContent.setText(text);

        instanceContent.revalidate();
        instanceContent.repaint();
    }

    public void startThread() {
        drawGUI();

        instanceThread = new Thread(() -> {
            while (true) {

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // TODO feature: hide GUI when capturing

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
}