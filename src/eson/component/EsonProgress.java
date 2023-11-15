/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eson.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author HP-QXO8AV
 */
public class EsonProgress extends javax.swing.JPanel {

    /**
     * Creates new form ProgressBar
     */
    
    public EsonProgress() {
        initComponents();
        initProgress();
    }
    
    @Override
    public boolean isOpaque(){
        return false;
    }
    
    @Override
    public void setOpaque(boolean flag){
       //do nothing
    }
    
    /**
     * Sets the value of <code>barAnimate</code> variable.
     * By default the value is true.
     *
     * @param flag  set RGB Animation enabled/disabled;
     * 
     * <p><br>Credits to Wawa Eson Robin Asidera</p>
     */
    public void setBarAnimated(boolean flag){
        barAnimate = flag;
    }
    
    public boolean isBarAnimated(){
        return barAnimate;
    }
    
    public void stopAnimationOnCompletion(boolean flag){
        stopAnimation = flag;
    }
    
    public void setStringForeground(Color color){
        stringForeground = color;
    }
    
    public Color getStringForeground(){
        return stringForeground;
    }
    
    @Override
    public void setFont(Font font){
        stringFont = font;
    }
    
    @Override
    public Font getFont(){
        return stringFont;
    }
    
    @Override
    public void setForeground(Color color){
        barAnimate = false;
        foreground_color = color;
    }
    
    @Override
    public Color getForeground(){
        return foreground_color;
    }
    
    @Override
    public void setBackground(Color color){
        background_color = color;
        if(text!=null){
            changeColor();
        }
    }
    
    @Override
    public Color getBackground(){
        return background_color;
    }
    
    public void setBorderPainted(boolean flag){
        borderPainted = flag;
    }
    
    public boolean isBorderPainted(){
        return borderPainted;
    }
    
    /**
     * Sets the value of the <code>stringPainted</code> property,
     * which determines whether the progress bar
     * should render a progress string.
     * The default is <code>false</code>, meaning
     * no string is painted.
     * Some look and feels might not support progress strings
     * or might support them only when the progress bar is in determinate mode.
     *
     * @param   flag       <code>true</code> if the progress bar should render a string
     * 
     * <p><br>Credits to Wawa Eson Robin Asidera</p>
     * 
     */
    public void setStringPainted(boolean flag){
        stringPainted = flag;
    }
    
    /**
     * Returns the value of the <code>stringPainted</code> property.
     *
     * @return the value of the <code>stringPainted</code> property
     * 
     * <p><br>Credits to Wawa Eson Robin Asidera</p>
     */
    public boolean isStringPainted(){
        return stringPainted;
    }
    
    public void setIndeterminate(boolean flag){
        indeterminate = flag;
    }
    
    public boolean isIndeterminate(){
        return indeterminate;
    }
    
    public void setString(String string){
        defaultString = false;
        this.string = string;
    }
    
    public String getString(){
        return text.getText();
    }
    
    public void setValue(int value){
        if(value==0){
            r1 = 119; g1 = 255; b1 = 0;
        }
        this.value = value>100?100:value;
        if(value<100 && value>0){
            if(!animTimer.isRunning()){
                animTimer.start();
            }
            timerDelay = 0;
        }
        changeColor();
    }
    
    public int getValue(){
        return value;
    }
    
    @Override
    protected void paintComponent(Graphics g){
        int i = 0, c = 0, t = 0, w;
        while(i<100){
            w = (getWidth()-t)/((100-i)==0?1:(100-i));
            g.setColor(progressColor[c]==null?background_color:progressColor[c]);
            g.fillRect(t, 0, w, getHeight());
            i++; t+=w; c++;
        }
        super.paintComponent(g);
    }
    
    private void changeColor(){
        colorModifier = indeterminate?20:4;
        int values = indeterminate?100:value;
        progressColor[0] = barAnimate ? getNewColor() : (indeterminate?getNewColor():foreground_color);
//        progressColor[0] = foreground_color;
        if (values == 0) {
            progressColor = new Color[100];
        }
        int i = 99;
        while (i > 0) {
            progressColor[i] = i > (values - 1) ? null : progressColor[i - 1];
            i--;
        }
        repaint();
        revalidate();
        if (values > 0) {
            if (progressColor[values - 1] == null) {
                changeColor();
            }
        }
    }
    
    private Color getNewColor(){
        if (r1 < 255 && b1 == 255 && g1 == 0) {
            r1 = (r1+colorModifier)>255?255:(r1+colorModifier);
        } else if (b1 > 0 && r1 == 255) {
            b1 = (b1-colorModifier)<0?0:(b1-colorModifier);
        } else if (r1 == 255 && g1 < 255) {
            g1 = (g1+colorModifier)>255?255:(g1+colorModifier);
        } else if (g1 == 255 && r1 > 0) {
            r1 = (r1-colorModifier)<0?0:(r1-colorModifier);
        } else if (g1 == 255 && b1 < 255) {
            b1 = (b1+colorModifier)>255?255:(b1+colorModifier);
        } else if (b1 == 255 && g1 > 0) {
            g1 = (g1-colorModifier)<0?0:(g1-colorModifier);
        }
        return new Color(r1, g1, b1);
    }
    
    private void initProgress(){
        setOpaque(false);
        progressColor = new Color[100];
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        text = new javax.swing.JLabel();

        text.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        text.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        text.setText("100%");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(text, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(text, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel text;
    // End of variables declaration//GEN-END:variables
    private int r1 = 0;
    private int g1 = 0;
    private int b1 = 255;
    private int value = 100;
    private int timerDelay = 0;
    private int colorModifier = 4;
    private String string = "100%";
    private boolean barAnimate = true;
    private boolean defaultString = true;
    private boolean stringPainted = false;
    private boolean indeterminate = false;
    private boolean borderPainted = false;
    private boolean stopAnimation = false;
    private Color progressColor[];
    private Color stringForeground = new java.awt.Color(0,0,0);
    private Color background_color = new java.awt.Color(240,240,240);
    private Color foreground_color = new java.awt.Color(51,153,255);
    private Font stringFont = new java.awt.Font("SansSerif", 0, 12);
    private javax.swing.Timer animTimer = new javax.swing.Timer(20, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                borderPainted = getBorder()==null?borderPainted:false;
                text.setBorder(borderPainted?javax.swing.BorderFactory.createMatteBorder(1,1,1,1, background_color):null);
                text.setFont(stringFont);
                text.setForeground(stringForeground);
                text.setText(" "+string);
                if (stringPainted && (defaultString)) {
                    text.setText(" "+value + "%");
                }
                changeColor();
                if (value == 100 && stopAnimation) {
                    if (timerDelay < 5) {
                        timerDelay++;
                    } else {
                        timerDelay = 0;
                        ((javax.swing.Timer) e.getSource()).stop();
                    }
                }
            }

        });
    
}
