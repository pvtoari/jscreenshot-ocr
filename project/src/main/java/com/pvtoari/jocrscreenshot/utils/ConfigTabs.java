package com.pvtoari.jocrscreenshot.utils;

import java.awt.*;
import java.util.*;

import javax.swing.*;

public class ConfigTabs extends JTabbedPane {
    
    private static final String[] SUPPORTED_LANGUAGES = {"eng", "spa", "por", "deu", "fra"};

    private TesseractManager tessManager;
    private ArrayList<String> languages;

    public ConfigTabs(TesseractManager tesseractManager) {
        super();

        tessManager = tesseractManager;
        languages = tessManager.getAvailableLanguages();

        addTab("General", createGeneralConfigPanel());
        addTab("Tesseract", createTesseractConfigPanel());
        addTab("Hotkeys", createHotkeysConfigPanel());
    }

    private JPanel createGeneralConfigPanel() {
        JPanel generalPanel = new JPanel();

        generalPanel.setLayout(new BoxLayout(generalPanel, BoxLayout.Y_AXIS));
        generalPanel.add(new JLabel("General configuration"));

        return generalPanel;
    }

    private JPanel createTesseractConfigPanel() {
        JPanel tesseractPanel = new JPanel();
        tesseractPanel.setLayout(new BoxLayout(tesseractPanel, BoxLayout.Y_AXIS));

        //////////////////////////////////// using language
        JPanel auxLangPanel = new JPanel();
        auxLangPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel usingLangLabel = new JLabel("Using language: ");
        auxLangPanel.add(usingLangLabel);

        String[] hLangs = humanize(languages.toArray(new String[languages.size()]));

        JComboBox<String> languageSelector = new JComboBox<>(hLangs);
        languageSelector.addActionListener(e -> {
            String selectedLang = (String) languageSelector.getSelectedItem();

            tessManager.setCurrentLanguage(dehumanize(selectedLang));
        });
        auxLangPanel.add(languageSelector);

        tesseractPanel.add(auxLangPanel);

        //////////////////////////////////// available languages
        JPanel auxAvailableLangPanel = new JPanel();
        auxAvailableLangPanel.setLayout(new BoxLayout(auxAvailableLangPanel, BoxLayout.Y_AXIS));

        JPanel auxPanelForAvailableLangs = new JPanel();
        auxPanelForAvailableLangs.setLayout(new FlowLayout(FlowLayout.LEFT));
        auxPanelForAvailableLangs.add(new JLabel("Get more languages:"));
        
        JComboBox<String> downloadLanguageSelector = new JComboBox<>(humanize(SUPPORTED_LANGUAGES));

        JButton downloadButton = new JButton("Download");
        downloadButton.addActionListener(e -> {
            // TODO
            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(false);
            progressBar.setStringPainted(true);
            progressBar.setString("Downloading...");

            JOptionPane optionPane = new JOptionPane(progressBar, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, null, null);
            JDialog dialog = optionPane.createDialog("Downloading language");
            dialog.setModalityType(Dialog.ModalityType.MODELESS);
            dialog.setVisible(true);

            SwingWorker<Void, Integer> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws Exception {
                    for(int i = 0; i <= 100; i++) {
                        Thread.sleep(100);
                        publish(i);
                    }
                    return null;
                }

                @Override
                protected void process(java.util.List<Integer> chunks) {
                    int value = chunks.get(chunks.size() - 1);
                    progressBar.setValue(value);
                }

                @Override
                protected void done() {
                    try {
                        get();
                        progressBar.setString("Download complete!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        dialog.dispose();
                    }
                }

            };

            worker.execute();
            
        });

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            // TODO
        });

        auxPanelForAvailableLangs.add(downloadLanguageSelector);
        auxPanelForAvailableLangs.add(downloadButton);
        auxPanelForAvailableLangs.add(deleteButton);
        
        auxAvailableLangPanel.add(auxPanelForAvailableLangs);

        tesseractPanel.add(auxAvailableLangPanel);


        return tesseractPanel;
    }

    private JPanel createHotkeysConfigPanel() {
        JPanel hotkeysPanel = new JPanel();

        hotkeysPanel.setLayout(new BoxLayout(hotkeysPanel, BoxLayout.Y_AXIS));
        hotkeysPanel.add(new JLabel("Hotkeys configuration"));

        return hotkeysPanel;
    }

    private String humanize(String lang) {
        switch (lang) {
            case "eng":
                return "English";
            case "spa":
                return "Spanish";
            case "por":
                return "Portuguese";
            case "deu":
                return "German";
            case "fra":
                return "French";
            default:
                return "Unknown";
        }
    }

    
    private String[] humanize(String[] langs) {
        String[] humanized = new String[langs.length];

        for(int i = 0; i < langs.length; i++) {
            humanized[i] = humanize(langs[i]);
        }
        
        return humanized;
    }

    private String dehumanize(String lang) {
        switch (lang) {
            case "English":
                return "eng";
            case "Spanish":
                return "spa";
            case "Portuguese":
                return "por";
            case "German":
                return "deu";
            case "French":
                return "fra";
            default:
                return "Unknown";
        }
    }

    private String[] dehumanize(String[] langs) {
        String[] dehumanized = new String[langs.length];

        for(int i = 0; i < langs.length; i++) {
            dehumanized[i] = dehumanize(langs[i]);
        }
        
        return dehumanized;
    }
}
