package com.pvtoari.jocrscreenshot.utils;

import net.sourceforge.tess4j.*;
import java.awt.image.BufferedImage;

public class TesseractManager {

    private Tesseract tessInstance;

    public TesseractManager() {
        tessInstance = new Tesseract();
        tessInstance.setDatapath("project\\src\\main\\java\\com\\pvtoari\\jocrscreenshot\\tessdata");
        tessInstance.setLanguage("eng");
    }

    public String getTextFromImage(BufferedImage image) throws TesseractException {
        return tessInstance.doOCR(image);
    }
}
