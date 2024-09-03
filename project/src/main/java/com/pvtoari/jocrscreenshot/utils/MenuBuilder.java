package com.pvtoari.jocrscreenshot.utils;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.*;

public class MenuBuilder{
    private JMenuBar menuBar;
    private JMenu lastAdded;
    private ArrayList<JMenu> menus;

    public MenuBuilder(){
        menuBar = new JMenuBar();
        menus = new ArrayList<>();
        lastAdded = null;
    }

    public MenuBuilder addMenu(String menuName){
        JMenu menu = new JMenu(menuName);

        menus.add(menu);
        lastAdded = menu;

        return this;
    }

    public MenuBuilder withItem(String itemName, ActionListener listener) throws NullPointerException {
        if(lastAdded == null) throw new NullPointerException("No menu has been added yet");

        JMenuItem item = new JMenuItem(itemName);
        item.addActionListener(listener);
        lastAdded.add(item);

        return this;
    }

    public JMenuBar build(){
        for(JMenu menu : menus){
            menuBar.add(menu);
        }

        return menuBar;
    }
}