/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eson.component;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.Timer;
import eson.core.EsonProject;
import eson.core.util.GaussianFilter;

/**
 *
 * @author Wawa Eson
 */
public class EsonPanel extends javax.swing.JPanel {

    /**
     * Creates new form EsonPanel
     */
    private int CORNER_RADIUS = 15,

    /**
     * Creates new form EsonPanel
     */
    HEADER_HEIGHT = 30;
    private float OPACITY = 0.5f;
    private Color BACKGROUND = new Color(255,255,255),
            BORDER = new Color(240,240,240),
            MODAL_BORDER = new Color(210,210,210),
            MODAL_COLOR = MODAL_BORDER,
            HEADER_BACKGROUND = BACKGROUND,
            SHADOW_COLOR = Color.BLACK;
    private boolean MODAL = false,
            SHADOW = false,
            SHADOW_SHOWN = false,
            HEADER_SHOWN = true,
            BORDER_SHOWN = false;
    private int xlocation = 0, ylocation = 0;
    private boolean enableWindowDrag = false;
    private BasicStroke STROKE = new BasicStroke(1f);
    private GaussianFilter FILTER = new GaussianFilter();
    
    public EsonPanel() {
        initComponents();
        if(EsonProject.start()){
            setOpaque(false);
            initListeners();
        }
    }
    
    private void initListeners(){
        addMouseListener(new MouseListener() {
            @Override public void mouseClicked(MouseEvent e) {}
            @Override public void mousePressed(MouseEvent e) {
                if(enableWindowDrag){
                    xlocation = e.getX();
                    ylocation = e.getY();
                }
            }
            @Override public void mouseReleased(MouseEvent e) {}
            @Override public void mouseEntered(MouseEvent e) {}
            @Override public void mouseExited(MouseEvent e) {}
        });
        addMouseMotionListener(new MouseMotionListener() {
            @Override public void mouseDragged(MouseEvent evt) {
                if(enableWindowDrag){
                    setLocation(getX() + evt.getX() - xlocation,
                        getY() + evt.getY() - ylocation);
                }
                repaint();
            }
            @Override public void mouseMoved(MouseEvent e) {}
        });
    }
    
    private BufferedImage SHADOW_IMAGE = null;
    @Override
    public void paintComponent(Graphics g){
        if(EsonProject.start()){
            paintBody(g);
        }
    }
    
    private void paintBody(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        g2d.setStroke(STROKE);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if(MODAL){
            g2d.setColor(MODAL_COLOR);
            g2d.fillRoundRect(1,1+y,getWidth()-2,getHeight()-(2+y),CORNER_RADIUS+5,CORNER_RADIUS+5);
        }
        if(SHADOW){
            if(SHADOW_IMAGE == null){
                BufferedImage img = createCompatibleImage(getWidth(), getHeight());
                Graphics2D tg2d = img.createGraphics();
                tg2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                tg2d.fillRoundRect(5, 5+y, getWidth() - 12, getHeight() - (14+y), CORNER_RADIUS, CORNER_RADIUS);
                tg2d.dispose();
                SHADOW_IMAGE = generateShadow(img, 5, SHADOW_COLOR, OPACITY);
            }
            g2d.drawImage(SHADOW_IMAGE, 2, 4, null);
        }
        if(BREATH){
            g2d.setColor(BREATH_COLOR);
            g2d.fillRoundRect(1,1+y,getWidth()-2,getHeight()-(2+y),CORNER_RADIUS+5,CORNER_RADIUS+5);
        }
        g2d.setColor(BACKGROUND);
        g2d.fillRoundRect(5,5+y,getWidth()-11,getHeight()-(11+y),CORNER_RADIUS,CORNER_RADIUS);
        if(HEADER_SHOWN){
            g2d.setColor(HEADER_BACKGROUND);
            g2d.fillRoundRect(5,5+y,getWidth()-11,HEADER_HEIGHT-(1+y),CORNER_RADIUS-5,CORNER_RADIUS-5);
            g2d.fillRect(5, 5+(HEADER_HEIGHT/2)+y, getWidth()-11, HEADER_HEIGHT/2-y);
        }
        if(BORDER_SHOWN){
            g2d.setColor(BORDER);
            g2d.drawRoundRect(5,5+y,getWidth()-11,getHeight()-(11+y),CORNER_RADIUS,CORNER_RADIUS);
        }
    }
    
    public void refreshShadow(){
        SHADOW_IMAGE = null;
        repaint();
    }
    
    public void setBorderStroke(BasicStroke s){
        STROKE = s;
        repaint();
    }
    
    public BasicStroke getBorderStroke(){
        return STROKE;
    }
        
    public BufferedImage generateShadow(BufferedImage imgSource, int size, Color color, float alpha) {

        int imgWidth = imgSource.getWidth();
        int imgHeight = imgSource.getHeight();

        BufferedImage imgMask = createCompatibleImage(imgWidth, imgHeight);
        Graphics2D g2d = imgMask.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.drawImage(imgSource, 0, 0, null);
        g2d.dispose();

        //--BLUR HERE--
        BufferedImage imgShadow = generateBlur(imgMask, size, color, alpha);
        return imgShadow;
    }

    @Override
    public GraphicsConfiguration getGraphicsConfiguration() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    }

    public BufferedImage createCompatibleImage(int width, int height) {
        return createCompatibleImage(width, height, Transparency.TRANSLUCENT);
    }

    public BufferedImage createCompatibleImage(int width, int height, int transparency) {
        BufferedImage image = getGraphicsConfiguration().createCompatibleImage(width, height, transparency);
        image.coerceData(true);
        return image;
    }
    
    public BufferedImage generateBlur(BufferedImage imgSource, int size, Color color, float alpha){
        
        FILTER.setRadius(size);

        int imgWidth = imgSource.getWidth();
        int imgHeight = imgSource.getHeight();

        BufferedImage imgBlur = createCompatibleImage(imgWidth, imgHeight);
        Graphics2D g2d = imgBlur.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.drawImage(imgSource, 0, 0, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, alpha));
        g2d.setColor(color);

        g2d.fillRect(0, 0, imgWidth, imgHeight);
        g2d.dispose();

        FILTER.setRadius(10);
        imgBlur = FILTER.filter(imgBlur, null);

        return imgBlur;
    }
    
    public void animateModal(Color color){
        MODAL_COLOR = color;
        EventQueue.invokeLater(MODAL_RUNNABLE);
    }
    
    public void animateModal(){
        MODAL_COLOR = MODAL_BORDER;
        EventQueue.invokeLater(MODAL_RUNNABLE);
    }
    
    private int COUNTER = 0;
    private Runnable MODAL_RUNNABLE = new Runnable() {
        @Override
        public void run() {
            Toolkit.getDefaultToolkit().beep();
            SHADOW = false;
            if(modalTimer.isRunning()){
                modalTimer.stop();
            }
            COUNTER = 0;
            modalTimer.start();
            }
    };
    private Timer modalTimer = new Timer(100, (ActionEvent e) -> {
        MODAL = (COUNTER%2)==0;
        if(COUNTER==4){
            ((Timer)e.getSource()).stop();
            MODAL = false; repaint();
            SHADOW = SHADOW_SHOWN;
        }
        COUNTER++;
        repaint();
    });
    
    private Runnable BREATH_RUNNABLE = new Runnable() {
        @Override
        public void run() {
            if(breathTimer.isRunning()){
                breathTimer.stop();
            }
            COUNTER = 0;
            breathTimer.start();
            }
    };
    
    private boolean increment = true, delay = false, BREATH = false, tempShadow = false;
    private int BREATH_SPEED = 100, delayCount = 0, breathAlpha = 0;
    private Color BREATH_COLOR = Color.RED;
    private Timer breathTimer = new Timer(BREATH_SPEED, (ActionEvent e) -> {
        Color c = BREATH_COLOR;
        if(!delay){
            if(increment) {
                if(breathAlpha<250){
                    breathAlpha+=25;
                }else{
                    increment = false;
                    breathAlpha=255;
                    delay = true;
                }
            } else {
                if(breathAlpha>5){
                    breathAlpha-=25;
                }else{
                    increment = true;
                    breathAlpha=0;
                    delay = true;
                }
            }
        }else{
            if(delayCount<1){
                delayCount++;
            }else{
                delayCount = 0;
                delay = false;
            }
        }
        BREATH_COLOR = new Color(c.getRed(),c.getGreen(),c.getBlue(),breathAlpha);
        repaint();
    });
    
    public void startBreathing(int speed){
        BREATH = true;
        BREATH_SPEED = speed;
        EventQueue.invokeLater(BREATH_RUNNABLE);
        increment = true;
        tempShadow = SHADOW;
//        SHADOW = false;
    }
    
    public void startBreathing(int speed, Color breathColor){
        BREATH_COLOR = breathColor;
        startBreathing(speed);
    }
    
    public boolean stopBreathing(){
        if (breathTimer.isRunning()) {  
            BREATH = false;
            SHADOW = tempShadow;
            breathTimer.stop(); 
        }
        repaint();
        while(breathTimer.isRunning()){}
        return true;
    }
    
    public void setEnableWindowDragging(boolean flag){
        enableWindowDrag = flag;
    }
    
    public boolean getEnableWindowDragging(){
        return enableWindowDrag;
    }
    
    public void setShadowColor(Color color){
        SHADOW_COLOR = color;
    }
    
    public Color getShadowColor(){
        return SHADOW_COLOR;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 275, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 178, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    private int y = 0;
    public void setYLocation(int a){
        y = a;
        repaint();
    }
    
    public int getYLocation(){
        return y;
    }
    
    public void setShadowOpacity(float f){
        OPACITY = f;
        repaint();
    }
    
    public float getShadowOpacity(){
        return OPACITY;
    }
    
    public void setHeaderShown(boolean flag){
        HEADER_SHOWN = flag;
        repaint();
    }
    
    public boolean isHeaderShown(){
        return HEADER_SHOWN;
    }
    
    public void setBorderShown(boolean flag){
        BORDER_SHOWN = flag;
        repaint();
    }
    
    public boolean isBorderShown(){
        return BORDER_SHOWN;
    }
    
    public void setShadowShown(boolean flag){
        SHADOW_SHOWN = flag;
        SHADOW = flag;
        repaint();
    }
    
    public void repaintShadow(){
        SHADOW_IMAGE = null;
        repaint();
    }
    
    public boolean isShadowShown(){
        return SHADOW_SHOWN;
    }
    
    @Override
    public void setBackground(Color color){
        BACKGROUND = color;
        repaint();
    }
    
    @Override
    public Color getBackground(){
        return BACKGROUND;
    }
    
    public void setBorderColor(Color color){
        BORDER = color;
        repaint();
    }
    
    
    public Color getBorderColor(){
        return BORDER;
    }
    
    public void setModalColor(Color color){
        MODAL_BORDER = color;
        repaint();
    }
    
    public Color getModalColor(){
        return MODAL_BORDER;
    }
    
    public void setHeaderBackground(Color color){
        HEADER_BACKGROUND = color;
        repaint();
    }
    
    public Color getHeaderBackground(){
        return HEADER_BACKGROUND;
    }
    
    public void setHeaderHeight(int height){
        HEADER_HEIGHT = height;
        repaint();
    }
    
    public int getHeaderHeight(){
        return HEADER_HEIGHT;
    }
    
    public void setCornerRadius(int radius){ 
        radius = radius>25?25:radius;
        CORNER_RADIUS = radius;
        repaint();
    }
    
    public int getCornerRadius(){
        return CORNER_RADIUS;
    }
    
}
