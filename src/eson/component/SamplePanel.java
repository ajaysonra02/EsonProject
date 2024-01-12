/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package eson.component;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

/**
 *
 * @author Codey Hyacinth
 */
public class SamplePanel extends javax.swing.JPanel {
    
    private String text;
    private ImageIcon icon;
    private Font font;
    private Color hoverBackground;
    private Color hoverForeground;
    private Color normalBackground;
    private Color normalForeground;
    private Color selectionBackground;
    private Color selectionForeground;

    /**
     * @return the hoverBackground
     */
    public Color getHoverBackground() {
        return hoverBackground;
    }

    /**
     * @param hoverBackground the hoverBackground to set
     */
    public void setHoverBackground(Color hoverBackground) {
        this.hoverBackground = hoverBackground;
    }

    /**
     * @return the hoverForeground
     */
    public Color getHoverForeground() {
        return hoverForeground;
    }

    /**
     * @param hoverForeground the hoverForeground to set
     */
    public void setHoverForeground(Color hoverForeground) {
        this.hoverForeground = hoverForeground;
    }

    /**
     * @return the normalBackground
     */
    public Color getNormalBackground() {
        return normalBackground;
    }

    /**
     * @param normalBackground the normalBackground to set
     */
    public void setNormalBackground(Color normalBackground) {
        this.normalBackground = normalBackground;
    }

    /**
     * @return the normalForeground
     */
    public Color getNormalForeground() {
        return normalForeground;
    }

    /**
     * @param normalForeground the normalForeground to set
     */
    public void setNormalForeground(Color normalForeground) {
        this.normalForeground = normalForeground;
    }

    /**
     * @return the selectionBackground
     */
    public Color getSelectionBackground() {
        return selectionBackground;
    }

    /**
     * @param selectionBackground the selectionBackground to set
     */
    public void setSelectionBackground(Color selectionBackground) {
        this.selectionBackground = selectionBackground;
    }

    /**
     * @return the selectionForeground
     */
    public Color getSelectionForeground() {
        return selectionForeground;
    }

    /**
     * @param selectionForeground the selectionForeground to set
     */
    public void setSelectionForeground(Color selectionForeground) {
        this.selectionForeground = selectionForeground;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the icon
     */
    public ImageIcon getIcon() {
        return icon;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    /**
     * @return the font
     */
    @Override
    public Font getFont() {
        return font;
    }

    /**
     * @param font the font to set
     */
    @Override
    public void setFont(Font font) {
        this.font = font;
    }
    
    private float animateValue = 0;

    /**
     * Creates new form SamplePanel
     */
    public SamplePanel() {
        initComponents();
        normalBackground = new Color(255,50,0);
        normalForeground = new Color(255,255,255);
        hoverBackground = new Color(255,255,0);
        hoverForeground = new Color(0,0,0);
        text = "NAVIGATION";
        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                repaint();
            }

            @Override public void mouseExited(MouseEvent e) {
                repaint();
            }
        });
    }
    
    @Override
    public void paint(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        applyQualityRenderingHints(g2d);
        g2d.setColor(normalBackground);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.drawImage(textImage(), 0,0, null);
    }
    
    private BufferedImage textImage(){
        int masterWidth = getWidth()-20;
        int masterHeight = getHeight();
        BufferedImage mask = new BufferedImage(masterWidth,masterHeight,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = mask.createGraphics();
        g2d.setComposite(AlphaComposite.Src);
        applyQualityRenderingHints(g2d);
        g2d.setColor(normalForeground);
        g2d.setFont(new Font("Arial",0,14));
        FontMetrics ft = g2d.getFontMetrics();
        Rectangle2D r2 = ft.getStringBounds(text, g2d);
        double x = masterWidth/2 - r2.getWidth()/2;
        double y = masterHeight/2 - r2.getHeight()/2;
        g2d.drawString(text, (int)x, (int)(y+g2d.getFont().getSize()-1));
        g2d.setComposite(AlphaComposite.SrcAtop);   
        g2d.setColor(hoverForeground);
        g2d.fillRect(0, 0, getWidth(), (int)animateValue);
        g2d.dispose();
        return mask;
    }
    
    public void applyQualityRenderingHints(Graphics2D g2d){
        applyAlphaInterpolation(g2d, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        applyAntiAliasing(g2d, RenderingHints.VALUE_ANTIALIAS_ON);
        applyColorRendering(g2d, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        applyDithering(g2d, RenderingHints.VALUE_DITHER_ENABLE);
        applyFractionalMetrics(g2d, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        applyInterpolation(g2d, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        applyRendering(g2d, RenderingHints.VALUE_RENDER_QUALITY);
        applyStrokeControl(g2d, RenderingHints.VALUE_STROKE_PURE);
    }
    
    public void applyRendering(Graphics2D g2d, Object hintValue){
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, hintValue);
    }
    
    public void applyAlphaInterpolation(Graphics2D g2d, Object hintValue){
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, hintValue);
    }
    
    public void applyAntiAliasing(Graphics2D g2d, Object hintValue){
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, hintValue);
    }
    
    public void applyColorRendering(Graphics2D g2d, Object hintValue){
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, hintValue);
    }
    
    public void applyDithering(Graphics2D g2d, Object hintValue){
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, hintValue);
    }
    
    public void applyFractionalMetrics(Graphics2D g2d, Object hintValue){
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, hintValue);
    }
    
    public void applyInterpolation(Graphics2D g2d, Object hintValue){
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hintValue);
    }
    
    public void applyStrokeControl(Graphics2D g2d, Object hintValue){
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, hintValue);
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
            .addGap(0, 224, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @return the animateValue
     */
    public float getAnimateValue() {
        return animateValue;
    }

    /**
     * @param animateValue the animateValue to set
     */
    public void setAnimateValue(float animateValue) {
        this.animateValue = animateValue;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
