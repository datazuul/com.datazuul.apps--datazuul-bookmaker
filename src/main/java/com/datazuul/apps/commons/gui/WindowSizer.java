package com.datazuul.apps.commons.gui;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

/**
 * This class is used to make window appear in the center of the screen.
 *
 * @author Craig Slusher
 * @version 2.0
 */
public class WindowSizer 
{
    private final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private boolean resize;
    private int width;
    private int height;

    public WindowSizer() 
    {
        this.resize = false;
    }

    /**
     * Keep track of the width and height of a Window.
     *
     * @param width The width of the window.
     * @param height The height of the window.
     */
    public WindowSizer(int width, int height) 
    {
        this.resize = true;
        this.setDimensions(width, height);
    }

    /**
     * Center the provided JFrame on the screen.
     *
     * @param window The JFrame we want to center on the screen.
     */
    public void centerOnScreen(Window window) 
    {
        if (this.resize)
        {
            window.setSize(this.width, this.height);
        }
        else {
            Dimension size = window.getSize();
            this.setDimensions((int)size.getWidth(), (int)size.getHeight());
        }
        
        window.setLocation(this.getHorizontal(), this.getVertical());
    }

    private void setDimensions(int width, int height)
    {
        this.width = width;
        this.height = height;
    }
    
    /**
     * This will return the x-coordinate of the centered window.
     */
    public int getHorizontal() 
    {
        return ((int)SCREEN_SIZE.getWidth() - this.width) / 2;
    }

    /**
     * This will return the y-coordinate of the centered window.
     */
    public int getVertical() 
    {
        return ((int)SCREEN_SIZE.getHeight() - this.height) / 2;
    }
}
