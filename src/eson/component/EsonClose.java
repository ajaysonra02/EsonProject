/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eson.component;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import eson.core.util.ImageRenderer;

/**
 *
 * @author ASUS-B150M
 */
public class EsonClose extends javax.swing.JLabel{
    
    
    private java.awt.Color HOVER_FOREGROUND = null;
    private java.awt.Color NORMAL_FOREGROUND = null;
    private java.awt.Color PRESSED_FOREGROUND = null;
    private java.awt.Image closeImage = null;
    private int iconWidth = 15, iconHeight = 15;
    private EsonCloseType ACTION_TYPE = EsonCloseType.DISPOSE_ONLY;
    private ImageRenderer RENDERER = null;

    public static enum EsonCloseType{
        TERMINATE,
        DISPOSE_ONLY;
    }
    
    public EsonClose(){
        RENDERER = new ImageRenderer();
        initComponent();
        CUSTOM_ACTION = false;
    }
    
    private void initComponent(){
        iconWidth = 10;
        iconHeight = 10;
        ACTION_TYPE = EsonCloseType.DISPOSE_ONLY;
        setOpaque(false);
        setBorder(null);
        setFocusable(false);
        initBasicColor();
        initListeners();
        initIcon();
        setHorizontalAlignment(SwingConstants.CENTER);
        setHorizontalTextPosition(SwingConstants.CENTER);
        setText(" ");
        setPreferredSize(new Dimension(20,20));
    }
    
    private void initIcon(){
        closeImage = new javax.swing.ImageIcon(getClass().getResource("/json/asidera/images/close.png")).getImage();
        repaintIcon();
    }
    
    public void repaintIcon(){
        setIcon(RENDERER.resizeIcon(closeImage, iconWidth, iconHeight, getForeground()));
    }
    
    /**
     * Sets the ACTION to:
     *      TERMINATE the program or JVM
     *      or
     *      DISPOSE_ONLY the window
     *
     * @param type button action should be the following:
     *                  <code>1 - TERMINATE</code>,
     *                  or <code>2 - DISPOSE_ONLY</code>
     * @beaninfo
     *   preferred: true
     *        enum: TERMINATE EsonClose.TERMINATE
     *              DISPOSE_ONLY EsonClose.DISPOSE_ONLY
     */
    public void setActionOnClick(EsonCloseType type){
        ACTION_TYPE = type;
    }
    
    public EsonCloseType getActionOnClick(){
        return ACTION_TYPE;
    }
    
    public void setIconSize(Dimension size){
        iconWidth = size.width;
        iconHeight = size.height;
        repaintIcon();
    }
    
    public Dimension getIconSize(){
        return new Dimension(iconWidth,iconHeight);
    }
    
    public void animateAction(){
        new Thread() {
            @Override
            public void run() {
                try{
                    statePressed();
                    Thread.sleep(150);
                    stateNormal();
                    Thread.sleep(50);
                    buttonAction();
                }catch(InterruptedException ex){}
            }
        }.start();
    }
    
    private void buttonAction(){
        if(CUSTOM_ACTION){
            for(ActionListener l:listenerList.getListeners(ActionListener.class)){
                l.actionPerformed(null);
            }
        }else{
            java.awt.Container ancestor = getTopLevelAncestor();
            if (ancestor instanceof JDialog || ancestor instanceof JFrame) {
                switch (ACTION_TYPE) {
                    case TERMINATE:
                        System.exit(0);
                        break;
                    case DISPOSE_ONLY:
                        if (ancestor instanceof JDialog) {
                            ((JDialog) ancestor).dispose();
                        } else {
                            ((JFrame) ancestor).dispose();
                        }
                        break;
                }
            }
        }
        stateNormal();
    }
    
    private boolean CUSTOM_ACTION = false;
    public void addActionListener(ActionListener l){
        CUSTOM_ACTION = true;
        listenerList.add(ActionListener.class, l);
    }
    
    public void removeActionListener(ActionListener l){
        listenerList.remove(ActionListener.class, l);
    }
    
    private void initBasicColor(){
        NORMAL_FOREGROUND = new java.awt.Color(20,20,20);
        HOVER_FOREGROUND = new java.awt.Color(255,255,255);
        PRESSED_FOREGROUND = new java.awt.Color(255,255,255);
        setBackground(new java.awt.Color(232,17,35));
        setForeground(NORMAL_FOREGROUND);
    }
    
    public void set_normalForeground(java.awt.Color color){
        NORMAL_FOREGROUND = color;
        setForeground(NORMAL_FOREGROUND);
        repaintIcon();
    }
    
    public java.awt.Color get_normalForeground(){
        return NORMAL_FOREGROUND;
    }
    
    public void set_hoverForeground(java.awt.Color color){
        HOVER_FOREGROUND = color;
    }
    
    public java.awt.Color get_hoverForeground(){
        return HOVER_FOREGROUND;
    }
    
    public void set_pressedForeground(java.awt.Color color){
        PRESSED_FOREGROUND = color;
    }
    
    public java.awt.Color get_pressedForeground(){
        return PRESSED_FOREGROUND;
    }
    
    private void initListeners(){
        addMouseListener(new MouseListener() {
            @Override public void mouseClicked(MouseEvent e) {}
            @Override public void mousePressed(MouseEvent e) { statePressed(); }
            @Override public void mouseReleased(MouseEvent e) {
                buttonAction();
            }
            @Override public void mouseEntered(MouseEvent e) { stateHover(); }
            @Override public void mouseExited(MouseEvent e) { stateNormal(); }
        });
        addFocusListener(new FocusListener() {
            @Override public void focusGained(FocusEvent e) { stateHover(); } 
            @Override public void focusLost(FocusEvent e) { stateNormal(); }
        });
    }
    
    public void stateHover(){
        closeImage = new javax.swing.ImageIcon(getClass().getResource("/json/asidera/images/plus.png")).getImage();
        setForeground(HOVER_FOREGROUND);
        repaintIcon();
    }
    
    public void stateNormal(){
        closeImage = new javax.swing.ImageIcon(getClass().getResource("/json/asidera/images/close.png")).getImage();
        setForeground(NORMAL_FOREGROUND);
        repaintIcon();
    }
    
    public void statePressed(){
        closeImage = new javax.swing.ImageIcon(getClass().getResource("/json/asidera/images/close.png")).getImage();
        setForeground(PRESSED_FOREGROUND);
        repaintIcon();
    }
    
}
