package com.pvtoari.jocrscreenshot;

import javax.swing.SwingUtilities;

import com.pvtoari.jocrscreenshot.utils.ComponentManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ComponentManager cm = null;
            try {
                cm = new ComponentManager();
            } catch (Exception e) {
                e.printStackTrace();
            }
            cm.startThread();
        });
    }
}