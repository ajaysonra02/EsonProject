package eson.component;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import eson.component.EsonButton.EsonButtonType;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolTip;
import javax.swing.SwingWorker;
import eson.core.EsonProject;
import eson.core.util.ImageRenderer;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 *
 * @author Wawa Eson
 */
public class EsonComboButton extends javax.swing.JPanel {

    /**
     * Creates new form EsonButton
     */
    private Color BACKGROUND = new Color(13,110,253),
            FOREGROUND = new Color(255, 255, 255),
            BORDER = new Color(11, 94, 215),
            NORMAL_BACKGROUND = new Color(13, 110, 253),
            NORMAL_FOREGROUND = new Color(255, 255, 255),
            HOVER_BACKGROUND = new Color(11, 94, 215),
            HOVER_FOREGROUND = new Color(255, 255, 255),
            NORMAL_BORDER = HOVER_BACKGROUND,
            HOVER_BORDER = NORMAL_BACKGROUND,
            SELECTED_FOREGROUND = new Color(0, 0, 0),
            SELECTED_BACKGROUND = new Color(255, 255, 255),
            PRESSED_BACKGROUND = HOVER_BACKGROUND,
            PRESSED_FOREGROUND = HOVER_FOREGROUND,
            PRESSED_BORDER = new Color(152, 193, 254);
    private int CORNER_RADIUS = 15, GAP = 0;
    private boolean PRESSED = false,
            SHADOW = false,
            ENABLED = true,
            AS_TAB_TOGGLE = false,
            UNDERLINE_ON_HOVER = false;
    private float SHADOW_OPACITY = 0.5f;
    private ImageRenderer RENDERER = null;
    private Image DROP_ICON = null;
    private List<String> VALUES = new ArrayList<>();
    
    public EsonComboButton() {
        initComponents();
        RENDERER = new ImageRenderer();
        DROP_ICON = new ImageIcon(getClass().getResource("/json/asidera/images/drop_down.png")).getImage();
        setItems(new String[]{"ESONCOMBO 1","ESONCOMBO 2","ESONCOMBO 3"});
        if(EsonProject.start()){
            setOpaque(false);
            initConfigurations();
        }
    }
    
    private void initConfigurations(){
        addMouseListener(new MouseListener() {
            @Override public void mouseClicked(MouseEvent e) { }
            @Override public void mousePressed(MouseEvent e) { if(e.getButton()==1){statePressed(); } }
            @Override public void mouseReleased(MouseEvent e) { if(e.getButton()==1){stateReleased();if(ENABLED){ showDrop();}} }
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
        initDropContainer();
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
                        showDrop();
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
            BACKGROUND = HOVER_BACKGROUND;
            FOREGROUND = HOVER_FOREGROUND;
            BORDER = HOVER_BORDER;
            label.setForeground(FOREGROUND);
            refreshIcon(); repaint();
    }
    
    public void stateError(){
//        BACKGROUND = BUTTON_ERROR_N_BACKGROUND;
//        FOREGROUND = BUTTON_ERROR_N_FOREGROUND;
        BORDER = BACKGROUND;
        label.setForeground(FOREGROUND);
        refreshIcon(); repaint();
    }
    
    public void stateExited(){
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
    
    public void setSelected(boolean flag){
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
        simpleButton(g2d);
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
    
    private void simpleButton(Graphics2D g2d){
        g2d.setColor(BACKGROUND);
        g2d.fillRoundRect(1, 1, getWidth()-2, getHeight()-2,CORNER_RADIUS,CORNER_RADIUS);
        int iconW = 8, iconH = 8;
        Color a = FOREGROUND;
        g2d.setColor(new Color(a.getRed(),a.getGreen(),a.getBlue(),50));
        g2d.fillRoundRect(7, 1, getWidth()-7, getHeight()-2, CORNER_RADIUS, CORNER_RADIUS);
        g2d.setColor(BACKGROUND);
        g2d.fillRect(7, 1, getWidth()-28, getHeight()-2);
        g2d.drawImage(RENDERER.maskImage(DROP_ICON, FOREGROUND), (getWidth()-15)-1, (getHeight()/2 - iconH/2)+1, iconW, iconH, null);
    }
    
    private void applyQualityRenderingHints(Graphics2D g2d){
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
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

        setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 25));

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
            .addComponent(label, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
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
       
        NORMAL_BORDER = HOVER_BACKGROUND;
        HOVER_BORDER = NORMAL_BACKGROUND;
    }
    
    public JLabel getLabel(){
        return label;
    }
    
    public void setSelectedItem(String text){
        label.setText(text);
    }
    
    private int SELECTED_INDEX = 0;
    public void setSelectedIndex(int index){
        SELECTED_INDEX = index;
        setSelectedItem(VALUES.get(index));
    }
    
    public int getSelectedIndex(){
        return SELECTED_INDEX;
    }
    
    public String getSelectedItem(){
        return VALUES.get(SELECTED_INDEX);
    }
    
    public void setItems(String[] items){
        VALUES.clear();
        for(String s:items){
            VALUES.add(s);
        }
        setSelectedIndex(0);
    }
    
    public List<String> getItems(){
        return VALUES;
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
        
    private void initDropContainer(){
        dropPanel.addMouseListener(new MouseListener(){
            @Override public void mouseClicked(MouseEvent e) {
                closeDrop();
            }
            @Override public void mousePressed(MouseEvent e) { }
            @Override public void mouseReleased(MouseEvent e) { }
            @Override public void mouseEntered(MouseEvent e) { }
            @Override public void mouseExited(MouseEvent e) { }
        });
        dropPanel.addFocusListener(new FocusListener(){
            @Override public void focusGained(FocusEvent e) {}
            @Override public void focusLost(FocusEvent e) { closeDrop(); }
        });
    }
    
    private Container TOP_CONTAINER = null;
    public void closeDrop(){
        if(dropPanel.isShowing()){
            TOP_CONTAINER.remove(dropPanel);
            TOP_CONTAINER.repaint();
        }
    }
    
    private void showDrop(){
        TOP_CONTAINER = getRootPane().getTopLevelAncestor();
        showDropItem();
        dropPanel.requestFocus();
    }
    
    private JPanel dropPanel = new JPanel();
    private void showDropItem(){
        dropPanel.removeAll();
        int width = getWidth();
        int height = getHeight() * (VALUES.size()-1);
        dropPanel.setBackground(getNormalBackground());
        dropPanel.setSize(width, height);
        dropPanel.setPreferredSize(new Dimension(width,height));
        dropPanel.setMinimumSize(new Dimension(width,height));
        dropPanel.setMaximumSize(new Dimension(width,height));
        dropPanel.setLayout(new javax.swing.BoxLayout(dropPanel, javax.swing.BoxLayout.PAGE_AXIS));
        TOP_CONTAINER.add(dropPanel,0);
        Point location = getPopupLocation();
        show(dropPanel,location.x, location.y);
        dropPanel.setOpaque(false);
        TOP_CONTAINER.repaint();
        
        for(DropViewWorker w:viewWorkers){
            w.stop();
            w.cancel(true);
        }
        viewWorkers.clear();
        DropViewWorker worker = new DropViewWorker(width);
        worker.execute();
        viewWorkers.add(worker);
    }
    
    public void show(Component invoker, int x, int y) {
        Point invokerOrigin;
////        if (invoker != null) {
//            invokerOrigin = getLocationOnScreen();
//            
//            // To avoid integer overflow
//            long lx, ly;
//            lx = ((long) invokerOrigin.x) +
//                 ((long) x);
//            ly = ((long) invokerOrigin.y) +
//                 ((long) y);
//            if(lx > Integer.MAX_VALUE) lx = Integer.MAX_VALUE;
//            if(lx < Integer.MIN_VALUE) lx = Integer.MIN_VALUE;
//            if(ly > Integer.MAX_VALUE) ly = Integer.MAX_VALUE;
//            if(ly < Integer.MIN_VALUE) ly = Integer.MIN_VALUE;
//
//            invoker.setLocation((int) lx, (int) ly);
////        } else {
////            invoker.setLocation(x, y);
////        }
        setLocation(getLocation());
        invoker.setVisible(true);
    }   
    
    int desiredLocationX = 0, desiredLocationY = 0;
    public void setLocation(int x, int y) {
        int oldX = desiredLocationX;
        int oldY = desiredLocationY;

        desiredLocationX = x;
        desiredLocationY = y;
        if(x != oldX || y != oldY) {
//            showPopup();
        }
    }
    
    private Point getPopupLocation() {
        Dimension popupSize = getSize();
        Insets insets = getInsets();

        // reduce the width of the scrollpane by the insets so that the popup
        // is the same width as the combo box.
        popupSize.setSize(popupSize.width - (insets.right + insets.left),getHeight()  * (VALUES.size()-1));
        Rectangle popupBounds = computePopupBounds( 0, getBounds().height,
                                                    popupSize.width, popupSize.height);
        Dimension scrollSize = popupBounds.getSize();
        Point popupLocation = popupBounds.getLocation();
        return popupLocation;
    }
    
    protected Rectangle computePopupBounds(int px,int py,int pw,int ph) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Rectangle screenBounds;

        // Calculate the desktop dimensions relative to the combo box.
        GraphicsConfiguration gc = getGraphicsConfiguration();
        Point p = new Point();
        SwingUtilities.convertPointFromScreen(p, this);
        if (gc != null) {
            Insets screenInsets = toolkit.getScreenInsets(gc);
            screenBounds = gc.getBounds();
            screenBounds.width -= (screenInsets.left + screenInsets.right);
            screenBounds.height -= (screenInsets.top + screenInsets.bottom);
            screenBounds.x += (p.x + screenInsets.left);
            screenBounds.y += (p.y + screenInsets.top);
        }
        else {
            screenBounds = new Rectangle(p, toolkit.getScreenSize());
        }
        int borderHeight = 0;
        Border popupBorder = getBorder();
        if (popupBorder != null) {
            Insets borderInsets = popupBorder.getBorderInsets(this);
            borderHeight = borderInsets.top + borderInsets.bottom;
            screenBounds.width -= (borderInsets.left + borderInsets.right);
            screenBounds.height -= borderHeight;
        }
        Rectangle rect = new Rectangle(px, py, pw, ph);
        if (py + ph > screenBounds.y + screenBounds.height) {
            if (ph <= -screenBounds.y - borderHeight) {
                // popup goes above
                rect.y = -ph - borderHeight;
            } else {
                // a full screen height popup
                rect.y = screenBounds.y + Math.max(0, (screenBounds.height - ph) / 2 );
                rect.height = Math.min(screenBounds.height, ph);
            }
        }
        return rect;
    }


    private static Frame getFrame(Component c) {
        Component w = c;
        while(!(w instanceof Frame) && (w!=null)) {
            w = w.getParent();
        }
        return (Frame)w;
    }
    
    private List<DropViewWorker> viewWorkers = new ArrayList<>();
    private class DropViewWorker extends SwingWorker{
        
        private boolean STOP = false;
        private int width = 0;
        
        public DropViewWorker(int w){
            width = w;
        }
        
        public void stop(){
            STOP = true;
        }       
        
        @Override
        protected Object doInBackground() throws Exception {
            int i = 0;
            for(String value:VALUES){
                if(STOP){
                    dropPanel.removeAll();
                    break;
                }
                if(i!=SELECTED_INDEX){
                    dropPanel.add(getNewButtonItem(value,width,i));
                }
                i++; 
            }
            dropPanel.repaint();
            return 0;
        }
    }
    
    private EsonButton getNewButtonItem(String value, int width, int index){
        EsonButton button = new EsonButton();
        button.setText(value);
        button.setFont(label.getFont());
        button.setButtonType(EsonButtonType.CUSTOM_NO_BORDER);
        button.setNormalBackground(getNormalBackground());
        button.setNormalForeground(getNormalForeground());
        button.setHoverBackground(getHoverBackground());
        button.setHoverForeground(getHoverForeground());
        button.setPressedBackground(getPressedBackground());
        button.setPressedForeground(getPressedForeground());
        button.setCornerRadius(getCornerRadius());
        button.setPreferredSize(new Dimension(width, getHeight()));
        button.setMaximumSize(new Dimension(width, getHeight()));
        button.setMinimumSize(new Dimension(width, getHeight()));
        button.stateExited();
        button.addActionListener((ActionEvent e) -> {
            setSelectedIndex(index);
            ActionListener[] action = listenerList.getListeners(ActionListener.class);
            if(action.length>0){
                for(ActionListener a:action){
                    a.actionPerformed(null);
                }
            }
            closeDrop();
        });
        return button;
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
        repaint();
    }
    
    public Color getNormalBackground(){
        return NORMAL_BACKGROUND;
    }
    
    public void setNormalForeground(Color color){
        NORMAL_FOREGROUND = color;
        FOREGROUND = color;
        repaint();
    }
    
    public Color getNormalForeground(){
        return NORMAL_FOREGROUND;
    }
    
    public void setHoverBackground(Color color){
        HOVER_BACKGROUND = color;
        repaint();
    }
    
    public Color getHoverBackground(){
        return HOVER_BACKGROUND;
    }
    
    public void setHoverForeground(Color color){
        HOVER_FOREGROUND = color;
        repaint();
    }
    
    public Color getHoverForeground(){
        return HOVER_FOREGROUND;
    }
    
    public void setNormalBorderColor(Color color){
        NORMAL_BORDER = color;
        BORDER = color;
        repaint();
    }
    
    public Color getNormalBorderColor(){
        return NORMAL_BORDER;
    }
    
    public void setHoverBorderColor(Color color){
        HOVER_BORDER = color;
        repaint();
    }
    
    public Color getHoverBorderColor(){
        return HOVER_BORDER;
    }
    
    public void setPressedBackground(Color color){
        PRESSED_BACKGROUND = color;
        repaint();
    }
    
    public Color getPressedBackground(){
        return PRESSED_BACKGROUND;
    }
    
    public void setPressedForeground(Color color){
        PRESSED_FOREGROUND = color;
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
    
}
