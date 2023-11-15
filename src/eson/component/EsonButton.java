/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eson.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JToolTip;
import eson.core.EsonProject;
import eson.core.util.ImageRenderer;

/**
 *
 * @author Wawa Eson
 */
public class EsonButton extends javax.swing.JPanel{
    
    private Color BACKGROUND = new Color(13,110,253),
            FOREGROUND = new Color(255,255,255),
            BORDER = new Color(11,94,215),
            NORMAL_BACKGROUND = new Color(13,110,253),
            NORMAL_FOREGROUND = new Color(255,255,255),
            HOVER_BACKGROUND = new Color(11,94,215),
            HOVER_FOREGROUND = new Color(255,255,255),
            NORMAL_BORDER = HOVER_BACKGROUND,
            HOVER_BORDER = NORMAL_BACKGROUND,
            SELECTED_FOREGROUND = new Color(0,0,0),
            SELECTED_BACKGROUND = new Color(255,255,255),
            PRESSED_BACKGROUND = HOVER_BACKGROUND,
            PRESSED_FOREGROUND = HOVER_FOREGROUND,
            PRESSED_BORDER = new Color(152,193,254);
    private int CORNER_RADIUS = 15, 
            GAP = 0;
    private boolean PRESSED = false,
            SHADOW = false,
            ENABLED = true,
            AS_TAB_TOGGLE = false,
            UNDERLINE_ON_HOVER = false;
    private float SHADOW_OPACITY = 0.5f;
    private EsonButtonType BUTTON_TYPE = EsonButtonType.PRIMARY;
    private BasicStroke BORDER_STROKE = new BasicStroke(0.2f);
    private ImageRenderer RENDERER = null;
    
    public static enum EsonButtonType{
        PRIMARY, SECONDARY, SUCCESS, SUCCESS_B, ERROR, WARNING, INFORMATION, LIGHT, DARK, LINK, CUSTOM, CUSTOM_NO_BORDER;
    }
    
    public EsonButton() {
        initComponents();
        RENDERER = new ImageRenderer();
        if(EsonProject.start()){
            setOpaque(false);
            initConfigurations();
        }
    }
    
    private void initConfigurations(){
        addMouseListener(new MouseListener() {
            @Override public void mouseClicked(MouseEvent e) { }
            @Override public void mousePressed(MouseEvent e) { if(e.getButton()==1){statePressed(); } }
            @Override public void mouseReleased(MouseEvent e) { if(e.getButton()==1){stateReleased();if(ENABLED){ buttonAction();}} }
            @Override public void mouseEntered(MouseEvent e) { mouseHover(); }
            @Override public void mouseExited(MouseEvent e) { stateExited(); }
        });
        addFocusListener(new FocusListener() {
            @Override public void focusGained(FocusEvent e) { stateHover(); }
            @Override public void focusLost(FocusEvent e) { stateExited(); }});
        addKeyListener(new KeyListener() {
            @Override public void keyTyped(KeyEvent e) { }
            @Override public void keyPressed(KeyEvent e) { }
            @Override public void keyReleased(KeyEvent e) { 
                if((e.getKeyCode()==KeyEvent.VK_ENTER || e.getKeyCode()==KeyEvent.VK_SPACE) && ENABLED){ animateAction(); } 
            }});
        stateExited();
    }
    
    public void animateAction(){
        if(EsonProject.start()){
            new Thread() {
                @Override
                public void run() {
                    try {
                        statePressed();
                        Thread.sleep(150);
                        stateReleased();
                        Thread.sleep(50);
                        buttonAction();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }.start();
        }
    }
    
    private void refreshIcon(){
        if(label.getIcon()!=null){
            Image img = ((ImageIcon)label.getIcon()).getImage();
            img = RENDERER.maskImage(img, label.getForeground());
            label.setIcon(new ImageIcon(img));
        }
    }
    
    public void setUnderlineOnHover(boolean flag){
        UNDERLINE_ON_HOVER = flag;
        stateExited();
    }
    
    public boolean isUnderlineOnHover(){
        return UNDERLINE_ON_HOVER;
    }
    
    private void mouseHover(){
        stateHover();
        if(UNDERLINE_ON_HOVER && !label.getText().trim().equals("")){
            Font font = label.getFont();
            Map<TextAttribute, Object> attributes = new HashMap<>(font.getAttributes());
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            label.setFont(font.deriveFont(attributes));
        }
    }
    
    public void stateHover(){
        if(!SELECTED){
            BACKGROUND = HOVER_BACKGROUND;
            FOREGROUND = HOVER_FOREGROUND;
            BORDER = HOVER_BORDER;
            label.setForeground(FOREGROUND);
            refreshIcon(); repaint();
        }
    }
    
    public void stateError(){
        BACKGROUND = BUTTON_ERROR_N_BACKGROUND;
        FOREGROUND = BUTTON_ERROR_N_FOREGROUND;
        BORDER = BACKGROUND;
        label.setForeground(FOREGROUND);
        refreshIcon(); repaint();
    }
    
    public void stateExited(){
        if(!SELECTED){
            if(!label.getText().trim().equals("")){
                Font font = label.getFont();
                Map<TextAttribute, Object> attributes = new HashMap<>(font.getAttributes());
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.FONT);
                label.setFont(font.deriveFont(attributes));
            }
            BACKGROUND = NORMAL_BACKGROUND;
            FOREGROUND = NORMAL_FOREGROUND;
            BORDER = NORMAL_BORDER;
            label.setForeground(FOREGROUND);
            refreshIcon(); repaint();
        }
    }
    
    public void setSelected(boolean flag){
        SELECTED = flag;
        if(flag){
            BACKGROUND = SELECTED_BACKGROUND;
            FOREGROUND = SELECTED_FOREGROUND;
            BORDER = NORMAL_BORDER;
            label.setForeground(FOREGROUND);
            refreshIcon(); repaint();
        }else{
            stateExited();
        }
    }
    
    private boolean TEMP_SHADOW = false;
    public void statePressed(){
        if(!SELECTED){
            TEMP_SHADOW = SHADOW;
            SHADOW = false;
            PRESSED = true;
            BACKGROUND = PRESSED_BACKGROUND;
            FOREGROUND = PRESSED_FOREGROUND;
            BORDER = PRESSED_BACKGROUND;
            label.setForeground(FOREGROUND);
            refreshIcon();
            repaint();
        }
    }
    
    public void stateReleased(){
        PRESSED = false;
        SHADOW = TEMP_SHADOW;
        stateExited();
    }
    
    private BufferedImage SHADOW_IMAGE = null;
    
    @Override
    public void paintComponent(Graphics g){
        if(EsonProject.start()){
            paintButton(g);
        }
    }
    
    private void paintButton(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        applyQualityRenderingHints(g2d);
        if(AS_TAB_TOGGLE){
            tabToggleButton(g2d);
        }else{
            if(BUTTON_TYPE!=null) switch (BUTTON_TYPE) {
                case LINK:
                    shadowLink(g2d);
                    break;
                case CUSTOM_NO_BORDER:
                    simpleButton(g2d);
                    break;
                default:
                    roundedButton(g2d);
                    break;
            }else {
                roundedButton(g2d);
            }
        }
    }
    
    public void setAsTabToggle(boolean flag){
        AS_TAB_TOGGLE = flag;
    }
    
    public boolean isAsTabToggle(){
        return AS_TAB_TOGGLE;
    }
    
    @Override
    public void setSize(int w, int h){
        super.setSize(w,h);
        label.setSize(w,h);
    }
    
    @Override
    public void setSize(Dimension dimension){
        super.setSize(dimension);
        label.setSize(dimension);
    }
    
    @Override
    public void setPreferredSize(Dimension dimension){
        super.setPreferredSize(dimension);
        label.setPreferredSize(dimension);
    }
    
    private void shadowLink(Graphics2D g2d){
        if(PRESSED){
            if(SHADOW_IMAGE==null){
                BufferedImage img = RENDERER.createCompatibleImage(getWidth(), getHeight());
                Graphics2D tg2d = img.createGraphics();
                tg2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                tg2d.fillRoundRect(5 + GAP, 5 + GAP, (getWidth() - 10) + (GAP * 2), (getHeight() - 10) + (GAP * 2), CORNER_RADIUS, CORNER_RADIUS);
                tg2d.dispose();
                SHADOW_IMAGE = RENDERER.generateShadow(img, 5, Color.BLACK, SHADOW_OPACITY);
            }
            g2d.drawImage(SHADOW_IMAGE, 0, 1, null);
        }
        g2d.setColor(BACKGROUND);
        g2d.fillRoundRect(5+GAP, 5+GAP, (getWidth()-10)+(GAP*2), (getHeight()-10)+(GAP*2),CORNER_RADIUS,CORNER_RADIUS);
        g2d.setStroke(BORDER_STROKE);
        g2d.setColor(BORDER);
        g2d.drawRoundRect(5+GAP, 5+GAP, (getWidth()-10)+(GAP*2), (getHeight()-10)+(GAP*2),CORNER_RADIUS,CORNER_RADIUS);
    }
    
    private void simpleButton(Graphics2D g2d){
        g2d.setColor(BACKGROUND);
        g2d.fillRoundRect(1, 1, getWidth()-2, getHeight()-2,CORNER_RADIUS,CORNER_RADIUS);
    }
    
    private void roundedButton(Graphics2D g2d){
        int borderx=1, bordery=1, borderSum=2;
        int buttonx=5, buttony=5, buttonSum=10;
        if(PRESSED){
            g2d.setColor(PRESSED_BORDER);
            g2d.fillRoundRect(borderx+GAP ,bordery+GAP ,(getWidth()-borderSum)+(GAP*2),(getHeight()-borderSum)+(GAP*2),CORNER_RADIUS+5,CORNER_RADIUS+5);
        }
        if(SHADOW){
            if(SHADOW_IMAGE==null){
                BufferedImage img = RENDERER.createCompatibleImage(getWidth(), getHeight());
                Graphics2D tg2d = img.createGraphics();
                tg2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                tg2d.fillRoundRect(buttonx + GAP, buttony + GAP, (getWidth() - buttonSum) + (GAP * 2), (getHeight() - buttonSum) + (GAP * 2), CORNER_RADIUS, CORNER_RADIUS);
                tg2d.dispose();
                SHADOW_IMAGE = RENDERER.generateShadow(img, 5, Color.BLACK, SHADOW_OPACITY);
            }
            g2d.drawImage(SHADOW_IMAGE, 0, 1, null);
        }
        g2d.setColor(BACKGROUND);
        g2d.fillRoundRect(buttonx+GAP, buttony+GAP, (getWidth()-buttonSum)+(GAP*2), (getHeight()-buttonSum)+(GAP*2),CORNER_RADIUS,CORNER_RADIUS);
        g2d.setStroke(BORDER_STROKE);
        g2d.setColor(BORDER);
        g2d.drawRoundRect(buttonx+GAP, buttony+GAP, (getWidth()-buttonSum)+(GAP*2), (getHeight()-buttonSum)+(GAP*2),CORNER_RADIUS,CORNER_RADIUS);
    }
    
    private boolean SELECTED = false;
    private void tabToggleButton(Graphics2D g2d){
        applyQualityRenderingHints(g2d);
        g2d.setColor(BACKGROUND);
        g2d.fillRoundRect(2 + GAP, 2 + GAP, (getWidth() - 4) + (GAP * 2), (getHeight() - 4) + (GAP * 2), CORNER_RADIUS, CORNER_RADIUS);
        if(SELECTED){
            g2d.setColor(FOREGROUND);
            g2d.fillRoundRect(5,getHeight()-4, getWidth()-10, 4,5,5);
        }
    }
    
    private void applyQualityRenderingHints(Graphics2D g2d){
        RENDERER.applyDithering(g2d, RenderingHints.VALUE_DITHER_ENABLE);
        RENDERER.applyRendering(g2d, RenderingHints.VALUE_RENDER_QUALITY);
        RENDERER.applyAntiAliasing(g2d, RenderingHints.VALUE_ANTIALIAS_ON);
        RENDERER.applyFractionalMetrics(g2d, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    }
    
    @Override
    public void setFont(Font f){
        super.setFont(f);
        if(label!=null){
            label.setFont(f);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        label = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 6, 5, 6));

        label.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label.setText("BUTTON");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel label;
    // End of variables declaration//GEN-END:variables
    
    public void setShadowOpacity(float f){
        SHADOW_OPACITY = f;
    }
    
    public float getShadowOpacity(){
        return SHADOW_OPACITY;
    }
    
    private Color TOOLTIP_BACKGROUND = Color.BLACK, TOOLTIP_FOREGROUND = Color.WHITE;
    public void setToolTipBackground(Color color){
        TOOLTIP_BACKGROUND = color;
    }
    
    public Color getToolTipBackground(){
        return TOOLTIP_BACKGROUND;
    }
    
    public void setToolTipForeground(Color color){
        TOOLTIP_FOREGROUND = color;
    }
    
    public Color getToolTipForeground(){
        return TOOLTIP_FOREGROUND;
    }
    
    @Override
    public JToolTip createToolTip() {
        JToolTip tip = super.createToolTip();
        tip.setBackground(TOOLTIP_BACKGROUND);
        tip.setForeground(TOOLTIP_FOREGROUND);
        tip.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        tip.setFont(new Font("Arial",0,10));
        return tip;
    }
    
    public void setShadowShown(boolean flag){
        SHADOW = flag;
        repaint();
    }
    
    public boolean isShadowShown(){
        return SHADOW;
    }
    
    private void checkType(){
        switch(BUTTON_TYPE){
            case PRIMARY:
                NORMAL_FOREGROUND = BUTTON_PRIMARY_N_FOREGROUND;
                NORMAL_BACKGROUND = BUTTON_PRIMARY_N_BACKGROUND;
                HOVER_BACKGROUND = BUTTON_PRIMARY_H_BACKGROUND;
                HOVER_FOREGROUND = BUTTON_PRIMARY_N_FOREGROUND;
                PRESSED_BACKGROUND = HOVER_BACKGROUND;
                PRESSED_FOREGROUND = HOVER_FOREGROUND;
                PRESSED_BORDER = BUTTON_PRIMARY_PRESSED_BORDER;
                break;
            case SECONDARY:
                NORMAL_FOREGROUND = BUTTON_SECONDARY_N_FOREGROUND;
                NORMAL_BACKGROUND = BUTTON_SECONDARY_N_BACKGROUND;
                HOVER_BACKGROUND = BUTTON_SECONDARY_H_BACKGROUND;
                HOVER_FOREGROUND = BUTTON_SECONDARY_N_FOREGROUND;
                PRESSED_BACKGROUND = HOVER_BACKGROUND;
                PRESSED_FOREGROUND = HOVER_FOREGROUND;
                PRESSED_BORDER = BUTTON_SECONDARY_PRESSED_BORDER;
                break;
            case SUCCESS_B:
                NORMAL_FOREGROUND = BUTTON_SUCCESS_B_N_FOREGROUND;
                NORMAL_BACKGROUND = BUTTON_SUCCESS_B_N_BACKGROUND;
                HOVER_BACKGROUND = BUTTON_SUCCESS_B_H_BACKGROUND;
                HOVER_FOREGROUND = BUTTON_SUCCESS_B_N_FOREGROUND;
                PRESSED_BACKGROUND = HOVER_BACKGROUND;
                PRESSED_FOREGROUND = HOVER_FOREGROUND;
                PRESSED_BORDER = BUTTON_SUCCESS_B_PRESSED_BORDER;
                break;
            case SUCCESS:
                NORMAL_FOREGROUND = BUTTON_SUCCESS_N_FOREGROUND;
                NORMAL_BACKGROUND = BUTTON_SUCCESS_N_BACKGROUND;
                HOVER_BACKGROUND = BUTTON_SUCCESS_H_BACKGROUND;
                HOVER_FOREGROUND = BUTTON_SUCCESS_N_FOREGROUND;
                PRESSED_BACKGROUND = HOVER_BACKGROUND;
                PRESSED_FOREGROUND = HOVER_FOREGROUND;
                PRESSED_BORDER = BUTTON_SUCCESS_PRESSED_BORDER;
                break;
            case ERROR:
                NORMAL_FOREGROUND = BUTTON_ERROR_N_FOREGROUND;
                NORMAL_BACKGROUND = BUTTON_ERROR_N_BACKGROUND;
                HOVER_BACKGROUND = BUTTON_ERROR_H_BACKGROUND;
                HOVER_FOREGROUND = BUTTON_ERROR_N_FOREGROUND;
                PRESSED_BACKGROUND = HOVER_BACKGROUND;
                PRESSED_FOREGROUND = HOVER_FOREGROUND;
                PRESSED_BORDER = BUTTON_ERROR_PRESSED_BORDER;
                break;
            case WARNING:
                NORMAL_FOREGROUND = BUTTON_WARNING_N_FOREGROUND;
                NORMAL_BACKGROUND = BUTTON_WARNING_N_BACKGROUND;
                HOVER_BACKGROUND = BUTTON_WARNING_H_BACKGROUND;
                HOVER_FOREGROUND = BUTTON_WARNING_N_FOREGROUND;
                PRESSED_BACKGROUND = HOVER_BACKGROUND;
                PRESSED_FOREGROUND = HOVER_FOREGROUND;
                PRESSED_BORDER = BUTTON_WARNING_PRESSED_BORDER;
                break;
            case INFORMATION:
                NORMAL_FOREGROUND = BUTTON_INFO_N_FOREGROUND;
                NORMAL_BACKGROUND = BUTTON_INFO_N_BACKGROUND;
                HOVER_BACKGROUND = BUTTON_INFO_H_BACKGROUND;
                HOVER_FOREGROUND = BUTTON_INFO_N_FOREGROUND;
                PRESSED_BACKGROUND = HOVER_BACKGROUND;
                PRESSED_FOREGROUND = HOVER_FOREGROUND;
                PRESSED_BORDER = BUTTON_INFO_PRESSED_BORDER;
                break;
            case LIGHT:
                NORMAL_FOREGROUND = BUTTON_LIGHT_N_FOREGROUND;
                NORMAL_BACKGROUND = BUTTON_LIGHT_N_BACKGROUND;
                HOVER_BACKGROUND = BUTTON_LIGHT_H_BACKGROUND;
                HOVER_FOREGROUND = BUTTON_LIGHT_N_FOREGROUND;
                PRESSED_BACKGROUND = HOVER_BACKGROUND;
                PRESSED_FOREGROUND = HOVER_FOREGROUND;
                PRESSED_BORDER = BUTTON_LIGHT_PRESSED_BORDER;
                break;
            case DARK:
                NORMAL_FOREGROUND = BUTTON_DARK_N_FOREGROUND;
                NORMAL_BACKGROUND = BUTTON_DARK_N_BACKGROUND;
                HOVER_BACKGROUND = BUTTON_DARK_H_BACKGROUND;
                HOVER_FOREGROUND = BUTTON_DARK_N_FOREGROUND;
                PRESSED_BACKGROUND = HOVER_BACKGROUND;
                PRESSED_FOREGROUND = HOVER_FOREGROUND;
                PRESSED_BORDER = BUTTON_DARK_PRESSED_BORDER;
                break;
            case LINK:
                NORMAL_FOREGROUND = BUTTON_LINK_N_FOREGROUND;
                NORMAL_BACKGROUND = BUTTON_LINK_N_BACKGROUND;
                HOVER_BACKGROUND = BUTTON_LINK_N_BACKGROUND;
                HOVER_FOREGROUND = BUTTON_LINK_H_FOREGROUND;
                PRESSED_BACKGROUND = HOVER_BACKGROUND;
                PRESSED_FOREGROUND = HOVER_FOREGROUND;
                PRESSED_BORDER = BUTTON_LINK_PRESSED_BORDER;
                break;
            default:
                break;
        }
        NORMAL_BORDER = HOVER_BACKGROUND;
        HOVER_BORDER = NORMAL_BACKGROUND;
    }
    
    public void setButtonType(EsonButtonType type){
        BUTTON_TYPE = type;
        checkType();
        stateExited();
    }
    
    public EsonButtonType getButtonType(){
        return BUTTON_TYPE;
    }
    
    public JLabel getLabel(){
        return label;
    }
    
    public void setText(String text){
        label.setText(text);
    }
    
    public String getText(){
        return label.getText();
    }
    
    @Override
    public void setEnabled(boolean flag){
        ENABLED = flag;
    }
    
    @Override
    public boolean isEnabled(){
        return ENABLED;
    }
    
    public void addActionListener(ActionListener l){
        listenerList.add(ActionListener.class, l);
    }
    
    public void removeActionListener(ActionListener l){
        listenerList.remove(ActionListener.class, l);
    }
    
    private void buttonAction(){
        ActionListener[] action = listenerList.getListeners(ActionListener.class);
        if(action.length>0){
            for(ActionListener a:action){
                a.actionPerformed(null);
            }
        }
    }
    
    public void setCornerRadius(int radius){
//        radius = radius>25?25:radius;
        CORNER_RADIUS = radius;
        repaint();
    }
    
    public int getCornerRadius(){
        return CORNER_RADIUS;
    }
    
    public void setNormalBackground(Color color){
        NORMAL_BACKGROUND = color;
        BACKGROUND = color;
        if(BUTTON_TYPE!=EsonButtonType.LINK && BUTTON_TYPE!=EsonButtonType.CUSTOM_NO_BORDER){
            BUTTON_TYPE = EsonButtonType.CUSTOM;
        }
        repaint();
    }
    
    public Color getNormalBackground(){
        return NORMAL_BACKGROUND;
    }
    
    public void setNormalForeground(Color color){
        NORMAL_FOREGROUND = color;
        FOREGROUND = color;
        if(BUTTON_TYPE!=EsonButtonType.LINK && BUTTON_TYPE!=EsonButtonType.CUSTOM_NO_BORDER){
            BUTTON_TYPE = EsonButtonType.CUSTOM;
        }
        repaint();
    }
    
    public Color getNormalForeground(){
        return NORMAL_FOREGROUND;
    }
    
    public void setHoverBackground(Color color){
        HOVER_BACKGROUND = color;
        if(BUTTON_TYPE!=EsonButtonType.LINK && BUTTON_TYPE!=EsonButtonType.CUSTOM_NO_BORDER){
            BUTTON_TYPE = EsonButtonType.CUSTOM;
        }
        repaint();
    }
    
    public Color getHoverBackground(){
        return HOVER_BACKGROUND;
    }
    
    public void setHoverForeground(Color color){
        HOVER_FOREGROUND = color;
        if(BUTTON_TYPE!=EsonButtonType.LINK && BUTTON_TYPE!=EsonButtonType.CUSTOM_NO_BORDER){
            BUTTON_TYPE = EsonButtonType.CUSTOM;
        }
        repaint();
    }
    
    public Color getHoverForeground(){
        return HOVER_FOREGROUND;
    }
    
    public void setNormalBorderColor(Color color){
        NORMAL_BORDER = color;
        BORDER = color;
        if(BUTTON_TYPE!=EsonButtonType.LINK && BUTTON_TYPE!=EsonButtonType.CUSTOM_NO_BORDER){
            BUTTON_TYPE = EsonButtonType.CUSTOM;
        }
        repaint();
    }
    
    public Color getNormalBorderColor(){
        return NORMAL_BORDER;
    }
    
    public void setHoverBorderColor(Color color){
        HOVER_BORDER = color;
        if(BUTTON_TYPE!=EsonButtonType.LINK && BUTTON_TYPE!=EsonButtonType.CUSTOM_NO_BORDER){
            BUTTON_TYPE = EsonButtonType.CUSTOM;
        }
        repaint();
    }
    
    public Color getHoverBorderColor(){
        return HOVER_BORDER;
    }
    
    public void setPressedBackground(Color color){
        PRESSED_BACKGROUND = color;
        if(BUTTON_TYPE!=EsonButtonType.LINK && BUTTON_TYPE!=EsonButtonType.CUSTOM_NO_BORDER){
            BUTTON_TYPE = EsonButtonType.CUSTOM;
        }
        repaint();
    }
    
    public Color getPressedBackground(){
        return PRESSED_BACKGROUND;
    }
    
    public void setPressedForeground(Color color){
        PRESSED_FOREGROUND = color;
        if(BUTTON_TYPE!=EsonButtonType.LINK && BUTTON_TYPE!=EsonButtonType.CUSTOM_NO_BORDER){
            BUTTON_TYPE = EsonButtonType.CUSTOM;
        }
    }
    
    public void setSelectedBackground(Color color){
        SELECTED_BACKGROUND = color;
    }
    
    public Color getSelectedBackground(){
        return SELECTED_BACKGROUND;
    }
    
    public void setSelectedForeground(Color color){
        SELECTED_FOREGROUND = color;
    }
    
    public Color getSelectedForeground(){
        return SELECTED_FOREGROUND;
    }
    
    public Color getPressedForeground(){
        return PRESSED_FOREGROUND;
    }
    
    public void setPressedBorderColor(Color color){
        PRESSED_BORDER = color;
        if(BUTTON_TYPE!=EsonButtonType.LINK && BUTTON_TYPE!=EsonButtonType.CUSTOM_NO_BORDER){
            BUTTON_TYPE = EsonButtonType.CUSTOM;
        }
        repaint();
    }
    
    public Color getPressedBorderColor(){
        return PRESSED_BORDER;
    }
    
    public void setButtonopa(int gap){
        GAP = gap;
    }
    
    public int getButtonGap(){
        return GAP;
    }

    //PRIMARY BUTTON COLORS
    public Color BUTTON_PRIMARY_N_FOREGROUND = new Color(255,255,255),
            BUTTON_PRIMARY_N_BACKGROUND = new Color(13,110,253),
            BUTTON_PRIMARY_H_BACKGROUND = new Color(11,94,215),
            BUTTON_PRIMARY_PRESSED_BORDER = new Color(152,193,254);
    
    //SECONDARY BUTTON COLORS
    public Color BUTTON_SECONDARY_N_FOREGROUND = new Color(255,255,255),
            BUTTON_SECONDARY_N_BACKGROUND = new Color(108,117,125),
            BUTTON_SECONDARY_H_BACKGROUND = new Color(92,99,106),
            BUTTON_SECONDARY_PRESSED_BORDER = new Color(192,196,200);
    
    //SUCCESS BUTTON COLORS
    public Color BUTTON_SUCCESS_N_FOREGROUND = new Color(255,255,255),
            BUTTON_SUCCESS_N_BACKGROUND = new Color(25,135,84),
            BUTTON_SUCCESS_H_BACKGROUND = new Color(21,115,71),
            BUTTON_SUCCESS_PRESSED_BORDER = new Color(157,204,182);
    
    //SUCCESS_B BUTTON COLORS
    public Color BUTTON_SUCCESS_B_N_FOREGROUND = new Color(255,255,255),
            BUTTON_SUCCESS_B_N_BACKGROUND = new Color(66,183,42),
            BUTTON_SUCCESS_B_H_BACKGROUND = new Color(54,164,32),
            BUTTON_SUCCESS_B_PRESSED_BORDER = new Color(173,252,157);
    
    //ERROR BUTTON COLORS
    public Color BUTTON_ERROR_N_FOREGROUND = new Color(255,255,255),
            BUTTON_ERROR_N_BACKGROUND = new Color(220,53,69),
            BUTTON_ERROR_H_BACKGROUND = new Color(187,45,59),
            BUTTON_ERROR_PRESSED_BORDER = new Color(240,169,176);
    
    //WARNING BUTTON COLORS
    public Color BUTTON_WARNING_N_FOREGROUND = new Color(0,0,0),
            BUTTON_WARNING_N_BACKGROUND = new Color(255,193,7),
            BUTTON_WARNING_H_BACKGROUND = new Color(255,202,44),
            BUTTON_WARNING_PRESSED_BORDER = new Color(236,209,130);
    
    //INFO BUTTON COLORS
    public Color BUTTON_INFO_N_FOREGROUND = new Color(0,0,0),
            BUTTON_INFO_N_BACKGROUND = new Color(13,202,240),
            BUTTON_INFO_H_BACKGROUND = new Color(49,210,242),
            BUTTON_INFO_PRESSED_BORDER = new Color(133,213,229);
    
    //LIGHT BUTTON COLORS
    public Color BUTTON_LIGHT_N_FOREGROUND = new Color(0,0,0),
            BUTTON_LIGHT_N_BACKGROUND = new Color(248,249,250),
            BUTTON_LIGHT_H_BACKGROUND = new Color(249,250,251),
            BUTTON_LIGHT_PRESSED_BORDER = new Color(233,233,234);
    
    //DARK BUTTON COLORS
    public Color BUTTON_DARK_N_FOREGROUND = new Color(255,255,255),
            BUTTON_DARK_N_BACKGROUND = new Color(43,47,51),
            BUTTON_DARK_H_BACKGROUND = new Color(28,31,35),
            BUTTON_DARK_PRESSED_BORDER = new Color(160,162,164);
    
    //LINK BUTTON COLORS
    public Color BUTTON_LINK_N_FOREGROUND = new Color(103,175,222),
            BUTTON_LINK_N_BACKGROUND = new Color(255,255,255),
            BUTTON_LINK_H_FOREGROUND = new Color(10,88,202),
            BUTTON_LINK_PRESSED_BORDER = new Color(194,219,254);
    
}
