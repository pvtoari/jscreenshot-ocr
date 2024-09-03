package com.pvtoari.jocrscreenshot.utils;

import net.sourceforge.tess4j.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;

public class TesseractManager {

    private static final String[] TESSDATA_FILES = {
        "eng.traineddata"
    };

    private Tesseract tessInstance;

    public TesseractManager() throws IOException {
        
        Path tempTessdataDir = extractTessdata();
        if(tempTessdataDir == null) {
            throw new IOException("Failed to extract tessdata, directory is wrong");
        }
        
        tessInstance = new Tesseract();

        tessInstance.setDatapath(tempTessdataDir.toString());
        tessInstance.setLanguage("eng");
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
}
