/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eson.core;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 *
 * @author ASUS-B150M
 */
public class EsonUI {
    
    public void setWindowOpaque(Window window, boolean flag){
        //for jdk 8 and below..
        try{
            Class<?> awtClass = Class.forName("com.sun.awt.AWTUtilities");
            if(awtClass!=null){
                Method method = awtClass.getMethod("setWindowOpaque", Window.class, boolean.class);
                method.invoke(null, window, flag);
            }
        }catch(ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException ex){}
    }
    
    public void initLabelAsLink(JLabel label, Color normal, Color pressed){
        linkNormal(label,normal);
        label.addMouseListener(new MouseListener() {
            @Override public void mouseClicked(MouseEvent e) { }
            @Override public void mouseExited(MouseEvent e) { linkNormal(label, normal); }
            @Override public void mouseReleased(MouseEvent e) { linkNormal(label,normal); }
            @Override public void mousePressed(MouseEvent e) { linkPressed(label, pressed); }
            @Override public void mouseEntered(MouseEvent e) { linkHover(label, normal); } 
        });
    }
    
    public void linkPressed(JLabel label, Color color){
        label.setForeground(color);
    }
    
    public void linkHover(JLabel label, Color color){
        label.setForeground(color);
        Font font = label.getFont();
        Map<TextAttribute, Object> attributes = new HashMap<>(font.getAttributes());
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        label.setFont(font.deriveFont(attributes));
            
    }
    
    public void linkNormal(JLabel label, Color color){
        label.setForeground(color);
        Font font = label.getFont();
        Map<TextAttribute, Object> attributes = new HashMap<>(font.getAttributes());
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.FONT);
        label.setFont(font.deriveFont(attributes));
    }
    
    public void blurWindow(Window window){
        setWindowTransparent(window);
        window.setBackground(EsonColor.BLUR_BACKGROUND_DARK);
        window.setSize(window.getToolkit().getScreenSize());
    }
    
    public void setWindowTransparent(Window window){
        window.setBackground(new java.awt.Color(0,0,0,0));
    }
    
    public void setWindowOpacity(Window window, float opacity){
        window.setOpacity(opacity);
    }
    
    public void closeExited(JLabel label){
        label.setOpaque(false);
        if(PRESSED){
            PRESSED = false;
            label.setForeground(LABEL_FOREGROUND);
        }
        label.getParent().repaint();
    }
    
    public void closeEntered(JLabel label){
        label.setOpaque(true);
        label.setBackground(EsonColor.ERROR);
        label.getParent().repaint();
    }
    
    private boolean PRESSED = false;
    private Color LABEL_FOREGROUND = null;
    public void closePressed(JLabel label){
        PRESSED = true;
        LABEL_FOREGROUND = label.getForeground();
        label.setOpaque(true);
        label.setBackground(Color.BLACK);
        label.setForeground(EsonColor.ERROR);
        label.getParent().repaint();
    }
    
    public void startWindowIntro(Window window){
        y = -40; f = 0; INTRO_WINDOW = window;
        setWindowOpacity(window, 0f);
        window.setLocation(0,y);
        EventQueue.invokeLater(INTRO_RUNNABLE);
    }
    
    private Window INTRO_WINDOW = null;
    private int COUNTER = 0;
    private Runnable INTRO_RUNNABLE = new Runnable() {
        @Override
        public void run() {
            Toolkit.getDefaultToolkit().beep();
            if(introTimer.isRunning()){
                introTimer.stop();
            }
            COUNTER = 0;
            introTimer.start();
            }
    };
    private int y = -50;
    private float f = 0;
    private Timer introTimer = new Timer(5, (ActionEvent e) -> {
        y += 10;
        f = f<1?f+0.10f:1f;
        if(COUNTER==5){
            INTRO_WINDOW.setLocation(0,0);
            setWindowOpacity(INTRO_WINDOW,1f);
            ((Timer)e.getSource()).stop();
        }else{
            INTRO_WINDOW.setLocation(0,y);
            setWindowOpacity(INTRO_WINDOW, f);
        }
        COUNTER++;
    });
    
    public String IMAGE_LOCATION = "/json/asidera/images/";
    public URL getImageURL(String name){
        return getClass().getResource(IMAGE_LOCATION+"/"+name);
    }
    
    public ImageIcon getImageIcon(String name){
        return new ImageIcon(getImageURL(name));
    }
    
    public GraphicsDevice[] getScreenDevices(){
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
    }
    
    public int[] getScreenRefreshRate(){
        GraphicsDevice[] gs = getScreenDevices();
        int retval[] = new int[gs.length];
        for (int i = 0; i < gs.length; i++) {
            retval[i] = gs[i].getDisplayMode().getRefreshRate();;
            if (retval[i] == DisplayMode.REFRESH_RATE_UNKNOWN) {
                System.err.println("Screen "+i+": Unknown Refresh Rate");
            }
        }
        return retval;
    }
    
    public int[] getScreenBitDepth(){
        GraphicsDevice[] gs = getScreenDevices();
        int retval[] = new int[gs.length];
        for (int i = 0; i < gs.length; i++) {
            retval[i] = gs[i].getDisplayMode().getBitDepth();
        }
        return retval;
    }
    
    public int[] getScreenNumberOfColors(){
        GraphicsDevice[] gs = getScreenDevices();
        int retval[] = new int[gs.length];
        for (int i = 0; i < gs.length; i++) {
            retval[i] = (int) Math.pow(2, gs[i].getDisplayMode().getBitDepth());
        }
        return retval;
    }
    
}
