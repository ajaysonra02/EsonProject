/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eson.component;

import eson.component.table.EsonTable;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import eson.core.util.DataConnection;
import eson.core.EsonProject;
import eson.core.util.ImageRenderer;
import eson.component.table.TableQueryHolder;
import eson.core.util.GaussianFilter;

/**
 *
 * @author Wawa Eson
 */
public class EsonSearch extends javax.swing.JPanel {

    /**
     * Creates new form EsonSearch
     */
    private boolean FOCUS_GAINED = false;
    private Color NORMAL_BACKGROUND = new Color(255,255,255),
                NORMAL_FOREGROUND = new Color(0,0,0),
                NORMAL_BORDER = new Color(100,100,100),
                GAINED_BACKGROUND = new Color(255,255,255),
                GAINED_FOREGROUND = new Color(0,0,0),
                GAINED_BORDER = new Color(0,0,0),
                HINT_FOREGROUND = new Color(100,100,100),
                SELECTION_BACKGROUND = new Color(75,110,175),
                SELECTION_FOREGROUND = new Color(255,255,255);
    private int CORNER_RADIUS = 25;
    private EsonTable ESON_TABLE = null;
    protected GaussianFilter FILTER = null;
    private Image SEARCH_ICON = null;
    private ImageRenderer RENDERER = null;
    private DataConnection dataConnection = null;
    
    public EsonSearch() {
        FILTER = new GaussianFilter();
        SEARCH_ICON = new ImageIcon(getClass().getResource("/json/asidera/images/search.png")).getImage();
        RENDERER = new ImageRenderer();
        initComponents();
        if(EsonProject.start()){
            initConfiguration();
        }
    }
    
    public void clear(){
        text.setText("");
        text.transferFocus();
    }
    
    public JTextField getField(){
        return text;
    }
    
    @Override
    public void setFont(Font font){
        super.setFont(font);
        if(text!=null){
            text.setFont(font);
        }
    }
    
    public void setText(String s){
        text.setText(s);
        textFocusLost();
    }
    
    public String getText(){
        return text.getText();
    }
    
    @Override
    public Font getFont(){
        return text==null?super.getFont():text.getFont();
    }
    
    public void setTable(EsonTable etable){
        ESON_TABLE = etable;
        etable.setEsonSearch(this);
        for(KeyListener l:text.getKeyListeners()){ text.removeKeyListener(l); }
        text.addKeyListener(new KeyListener(){
            @Override public void keyTyped(KeyEvent e) { }
            @Override public void keyPressed(KeyEvent e) { }
            @Override public void keyReleased(KeyEvent e) { 
                etable.search(text.getText()); 
            }
        });
    }
    
    public EsonTable getTable(){
        return ESON_TABLE;
    }
    
    public void setCornerRadius(int radius){ 
//        radius = radius>25?25:radius;
        CORNER_RADIUS = radius;
        repaint();
    }
    
    public int getCornerRadius(){
        return CORNER_RADIUS;
    }
    
    public void setSelectionBackground(Color color){
        SELECTION_BACKGROUND = color;
        text.setSelectionColor(color);
    }
    
    public Color getSelectionBackground(){
        return SELECTION_BACKGROUND;
    }
    
    public void setSelectionForeground(Color color){
        SELECTION_FOREGROUND = color;
        text.setSelectedTextColor(color);
    }
    
    public Color getSelectionForeground(){
        return SELECTION_FOREGROUND;
    }
    
    public void setNormalBackground(Color color){
        NORMAL_BACKGROUND = color;
        text.setBackground(color);
        repaint();
    }
    
    public Color getNormalBackground(){
        return NORMAL_BACKGROUND;
    }
    
    public void setNormalForeground(Color color){
        NORMAL_FOREGROUND = color;
        repaint();
    }
    
    public Color getNormalForeground(){
        return NORMAL_FOREGROUND;
    }
    
    public void setNormalBorderColor(Color color){
        NORMAL_BORDER = color;
        repaint();
    }
    
    public Color getNormalBorderColor(){
        return NORMAL_BORDER;
    }
    
    public void setGainedBackground(Color color){
        GAINED_BACKGROUND = color;
        repaint();
    }
    
    public Color getGainedBackground(){
        return GAINED_BACKGROUND;
    }
    
    public void setGainedForeground(Color color){
        GAINED_FOREGROUND = color;
        repaint();
    }
    
    public Color getGainedForeground(){
        return GAINED_FOREGROUND;
    }
    
    public void setGainedBorderColor(Color color){
        GAINED_BORDER = color;
        repaint();
    }
    
    public Color getGainedBorderColor(){
        return GAINED_BORDER;
    }
    
    public void setHintForeground(Color color){
        HINT_FOREGROUND = color;
        textFocusLost();
        repaint();
    }
    
    public Color getHintForeground(){
        return HINT_FOREGROUND;
    }
    
    public void setHorizontalAlignment(int alignment){
        text.setHorizontalAlignment(alignment);
    }
    
    public int getHorizontalAlignment(){
        return text.getHorizontalAlignment();
    }
    
    private void initConfiguration(){
        addFocusListener(new FocusListener() {
            @Override public void focusGained(FocusEvent e){ 
                text.requestFocus();
            }
            @Override public void focusLost(FocusEvent e){}
        });
        text.setBorder(null);
        text.addFocusListener(new FocusListener() {
            @Override public void focusGained(FocusEvent arg0) {
                textFocusGained(); 
            }
            @Override public void focusLost(FocusEvent arg0) { 
                textFocusLost(); 
            }
        });
        setOpaque(false);
        textFocusLost();
    }
    
    private void textFocusLost(){
        FOCUS_GAINED = false;
        repaint();
        if(text.getText().trim().equals("")){
            text.setText("SEARCH");
            text.setForeground(HINT_FOREGROUND);
        }else{
            text.setForeground(NORMAL_FOREGROUND);
        }
        text.setBackground(NORMAL_BACKGROUND);
    }
    
    private void textFocusGained(){
        if(text.getText().trim().toUpperCase().equals("SEARCH")){
            text.setText("");
        }
        FOCUS_GAINED = true;
        repaint();
        text.setCaretColor(GAINED_FOREGROUND);
        text.setForeground(GAINED_FOREGROUND);
        text.setBackground(GAINED_BACKGROUND);
    }
    
    private void applyQualityRenderingHints(Graphics2D g2d){
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(EsonProject.start()){
            paintBody(g);
        }
    }
    
    private void paintBody(Graphics g){
        //paint component
        Graphics2D g2d = (Graphics2D)g;
        applyQualityRenderingHints(g2d);
        g2d.setColor(FOCUS_GAINED?GAINED_BORDER:NORMAL_BORDER);
        g2d.fillRoundRect(1,1,getWidth()-2,getHeight()-2,CORNER_RADIUS,CORNER_RADIUS);
        g2d.setColor(FOCUS_GAINED?GAINED_BACKGROUND:NORMAL_BACKGROUND);
        g2d.fillRoundRect(2,2,getWidth()-4,getHeight()-4,CORNER_RADIUS,CORNER_RADIUS);
        
        int iconSize = getHeight()<22?getHeight()-4:18;
        int iconX = getHeight()/2 - iconSize/2;
        g2d.drawImage(RENDERER.maskImage(SEARCH_ICON, text.getForeground()), iconX-2, iconX, iconSize, iconSize, null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        text = new javax.swing.JTextField();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(7, 28, 7, 12));
        setPreferredSize(new java.awt.Dimension(281, 40));

        text.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        text.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        text.setText("SEARCH");
        text.setPreferredSize(new java.awt.Dimension(15, 26));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(text, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(text, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField text;
    // End of variables declaration//GEN-END:variables
}
