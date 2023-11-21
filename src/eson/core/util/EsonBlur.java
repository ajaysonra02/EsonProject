/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eson.core.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import javax.swing.JComponent;
import javax.swing.JLayer;
import javax.swing.plaf.LayerUI;

public class EsonBlur extends LayerUI<Component> {

    private BufferedImage mOffscreenImage;
    private BufferedImageOp mOperation;
    private int BLUR_VALUE = 6;
    private float LIGHT_VALUE = 1.0f;
    private Color BLUR_COLOR = null;

    public EsonBlur() {
        BLUR_VALUE = 6;
        LIGHT_VALUE = 1.0f;
        BLUR_COLOR = null;
        refreshValues();
    }
    
    public EsonBlur(int blur_value){
        BLUR_VALUE = blur_value;
        LIGHT_VALUE = 1.0f;
        BLUR_COLOR = null;
        refreshValues();
    }
    
    public EsonBlur(int blur_value, float light_value){
        BLUR_VALUE = blur_value;
        LIGHT_VALUE = light_value;
        BLUR_COLOR = null;
        refreshValues();
    }
    
    public EsonBlur(int blur_value, float light_value, Color blur_color){
        BLUR_VALUE = blur_value;
        LIGHT_VALUE = light_value;
        BLUR_COLOR = blur_color;
        refreshValues();
    }
    
    public void setBlurValue(int value){
        BLUR_VALUE = value;
        refreshValues();
    }
    
    public int getBlurValue(){
        return BLUR_VALUE;
    }
    
    public void setLightValue(float value){
        LIGHT_VALUE = value;
        refreshValues();
    }
    
    public float getLightValue(){
        return LIGHT_VALUE;
    }
    
    public void setBlurColor(Color color){
        BLUR_COLOR = color;
    }
    
    public Color getBlurColor(){
        return BLUR_COLOR;
    }
    
    private void refreshValues(){
        int blurCount = BLUR_VALUE * BLUR_VALUE;
        float ninth = LIGHT_VALUE / blurCount;
        float[] blurKernel = new float[blurCount];
        for (int i = 0; i < blurCount; i++) {
            blurKernel[i] = ninth;
        }
        mOperation = new ConvolveOp(new Kernel(BLUR_VALUE, BLUR_VALUE, blurKernel), ConvolveOp.EDGE_NO_OP, null);
    }

    @Override
    public void paint (Graphics g, JComponent c) {
        int w = c.getWidth();
        int h = c.getHeight();
        if(w == 0 || h == 0) {
            return;
        }
        // only create the offscreen image if the one we have is the wrong size.
        if(mOffscreenImage == null ||  mOffscreenImage.getWidth() != w ||  mOffscreenImage.getHeight() != h) {
            mOffscreenImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        }
        Graphics2D ig2 = mOffscreenImage.createGraphics();
        ig2.setClip(g.getClip());   
        super.paint(ig2, c);
        ig2.dispose();
        Graphics2D g2 = (Graphics2D)g;
        g2.drawImage(mOffscreenImage, mOperation, 0, 0);
        if(BLUR_COLOR!=null){
            g2.setColor(BLUR_COLOR);
            g2.fillRect(0, 0, w, h);
        }
    }
    
    public void blurComponent(Container container, boolean flag){
        CONTAINER = container;
        exitOnHover = false;
        actionEnabled = false;
        if(flag){ showBlur(); }else{ removeBlur(); }
    }
    
    public void blurComponentWithAction(Container container, boolean exitOnHover, boolean flag){
        CONTAINER = container;
        this.exitOnHover = exitOnHover;
        actionEnabled = true;
        if(flag){ showBlur(); }else{ removeBlur(); }
    }
    
    public void blurComponentWithAction(Container container, boolean flag){
        CONTAINER = container;
        this.exitOnHover = exitOnHover;
        actionEnabled = true;
        if(flag){ showBlur(); }else{ removeBlur(); }
    }
    
    private Container CONTAINER = null;
    private Component VIEW = null;
    private boolean exitOnHover = false, actionEnabled = false, isBlur = false;
    protected void showBlur(){
        VIEW = null;
        if(CONTAINER.getComponentCount()>0){
            isBlur = true;
            VIEW = CONTAINER.getComponent(0);
            JLayer<Component> layer = new JLayer<>(VIEW, this);
            if(actionEnabled){
                layer.addMouseListener(new MouseListener() {
                    @Override public void mouseClicked(MouseEvent arg0) { removeBlur(); }
                    @Override public void mousePressed(MouseEvent arg0) { }
                    @Override public void mouseReleased(MouseEvent arg0) { }
                    @Override public void mouseEntered(MouseEvent arg0) { if(exitOnHover){removeBlur();} }
                    @Override public void mouseExited(MouseEvent arg0) { }
                });
            }
            CONTAINER.removeAll();
            layer.setSize(CONTAINER.getSize());
            CONTAINER.add(layer);
            layer.setVisible(true);
            CONTAINER.revalidate();
            CONTAINER.repaint();
        }
    }
    
    protected void removeBlur(){
        if(isBlur){
            isBlur = false;
            CONTAINER.removeAll();
            VIEW.setSize(CONTAINER.getSize());
            CONTAINER.add(VIEW);
            VIEW.setVisible(true);
            CONTAINER.revalidate(); 
            CONTAINER.repaint(); 
        }
    }
    
}
