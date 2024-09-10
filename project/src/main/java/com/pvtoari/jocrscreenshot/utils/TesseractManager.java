package com.pvtoari.jocrscreenshot.utils;

import net.sourceforge.tess4j.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class TesseractManager {

    private static final String[] TESSDATA_FILES = {
        "eng.traineddata"
    };

    private final String INSTALLATION_PATH = getInstallPath();

    
    private Tesseract tessInstance;
    private String currentLanguage;
    private ArrayList<String> availableLanguages;
    
    
    public TesseractManager() throws IOException {
        
        Path tempTessdataDir = extractTessdata();
        if(tempTessdataDir == null) {
            throw new IOException("Failed to extract tessdata, directory is wrong");
        }
        
        
        tessInstance = new Tesseract();
        
        tessInstance.setDatapath(tempTessdataDir.toString());
        setCurrentLanguage("eng");
    }
    
    private String getInstallPath() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return "C:\\Program Files\\jscreenshot-ocr";
        } else if (osName.contains("mac")) {
            return "/Applications/jscreenshot-ocr";
        } else if (osName.contains("nix") || osName.contains("nux")) {
            return "/usr/local/jscreenshot-ocr";
        } else {
            return null;
        }
    }
    
    public String getTextFromImage(BufferedImage image) throws TesseractException {
        return tessInstance.doOCR(image);
    }

    private Path extractTessdata() throws IOException {
        
        Path tempDir = Files.createTempDirectory("tessdata");
        
        System.out.println("Extracting tessdata to: " + tempDir);
        
        for (String tessdataFile : TESSDATA_FILES) {
            try (InputStream is = getClass().getResourceAsStream("/tessdata/" + tessdataFile)) {
                if (is == null) {
                    throw new IOException("Resource not found: " + tessdataFile);
                }
                Path tempFile = tempDir.resolve(tessdataFile);
                Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new IOException("Failed to extract tessdata: " + tessdataFile, e);
            }
        }
        
        return tempDir;
    }
    
    public void setCurrentLanguage(String language) {
        tessInstance.setLanguage(language);
        currentLanguage = language;
    }
    
    public String getCurrentLanguage() {
        return currentLanguage;
    }
    
    public ArrayList<String> getAvailableLanguages() {
        return new ArrayList<>(Arrays.asList(new String[]{"eng", "spa", "por", "deu", "fra"}));
    }

    public String getInstallationPath() {
        return INSTALLATION_PATH;
    }
}
