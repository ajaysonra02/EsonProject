
package eson.component;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolTip;
import eson.core.EsonColor;
import eson.core.EsonProject;
import eson.core.util.GaussianFilter;
import eson.core.util.ImageRenderer;

/**
 *
 * @author WAWA ESON
 */
public class EsonText extends javax.swing.JPanel {

    /**
     * Creates new form EsonText
     */
    private Color TEXT_FOREGROUND = new Color(0,0,0),
            ERROR_FOREGROUND = new Color(255,0,0),
            WARNING_FOREGROUND = new Color(226,206,0),
            HINT_FOREGROUND = new Color(120,120,120),
            LABEL_FOREGROUND = new Color(10,10,10),
            GAINED_BACKGROUND = new Color(255,255,255),
            GAINED_FOREGROUND = new Color(0,0,0),
            NORMAL_BACKGROUND = new Color(240,240,240),
            NORMAL_FOREGROUND = new Color(30,30,30),
            GAINED_BORDER = new Color(255,255,255),
            NORMAL_BORDER = new Color(60,60,60),
            SELECTION_BACKGROUND = new Color(75,110,175),
            SELECTION_FOREGROUND = new Color(255,255,255),
            PARENT_BACKGROUND = new Color(240,240,240),
            BACKGROUND = new Color(240,240,240),
            BORDER = new Color(0,0,0);
    private boolean HINT_SHOWN = false,
            AS_PASSWORD = false,
            LABEL_SHOWN = true,
            BUTTON_SHOWN = false,
            FOCUS_GAINED = false,
            DRAGGED = false,
            INFO_LABEL_SHOWN = false;
    private int CORNER_RADIUS = 20;
    private String LABEL_TEXT;
    private char PASSWORD_ECHO_CHAR = '●';
    private EsonTextAlignment ALIGNMENT = EsonTextAlignment.LEADING;
    private BasicStroke BORDER_STROKE = new BasicStroke(2f);
    private GaussianFilter FILTER = null;
    private ImageRenderer RENDERER = null;
    private boolean ENABLED = true;
    private Color BUTTON_N_FOREGROUND = EsonColor.PRIMARY_N_FOREGROUND,
            BUTTON_H_FOREGROUND = EsonColor.PRIMARY_N_FOREGROUND,
            BUTTON_P_FOREGROUND = EsonColor.PRIMARY_N_FOREGROUND;
    
    public static enum EsonTextAlignment{
        LEFT, CENTER, RIGHT, LEADING, TRAILING;
    }
    
    public EsonText() {
        RENDERER = new ImageRenderer();
        FILTER = new GaussianFilter();
        DRAGGED = false;
        ENABLED = true;
        initComponents();
        loadInitialConfiguration();
        if(EsonProject.start()){
            initListeners();
        }
    }
    
    private void applyQualityRenderingHints(Graphics2D g2d){
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
    
    private void paintBackground(Graphics g){
        if(EsonProject.start()){
            paintBody(g);
        }
    }
    
    private int labely = 9;
    private void paintBody(Graphics g){
        if(!DRAGGED){
            int width = container.getWidth();
            int height = container.getHeight();
            Graphics2D g2d = (Graphics2D) g;
            applyQualityRenderingHints(g2d);
            int ex = 3;
            g2d.setColor(BACKGROUND);
            g2d.fillRoundRect(5, 5+ex, width - 10, height - (10+ex), CORNER_RADIUS, CORNER_RADIUS);
            g2d.setStroke(new BasicStroke(FOCUS_GAINED?1.6f:1f));
            if(ENABLED){
                g2d.setColor(BORDER);
                label.setForeground(BORDER);
                g2d.drawRoundRect(5, 5+ex, width - 10, height - (10+ex), CORNER_RADIUS, CORNER_RADIUS);
            }
            if(!label.getText().trim().equals("")){
                g2d.setColor(PARENT_BACKGROUND);
                g2d.fillRect(label.getX(), 0, label.getWidth(), label.getHeight());
                g2d.setColor(BACKGROUND);
                g2d.fillRect(label.getX(), labely, label.getWidth(), label.getHeight()-labely);
            }
        }
    }
        
    public BufferedImage generateShadow(BufferedImage imgSource, int size, Color color, float alpha) {

        int imgWidth = imgSource.getWidth();
        int imgHeight = imgSource.getHeight();

        BufferedImage imgMask = createCompatibleImage(imgWidth, imgHeight);
        Graphics2D g2d = imgMask.createGraphics();
        applyQualityRenderingHints(g2d);

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
        applyQualityRenderingHints(g2d);

        g2d.drawImage(imgSource, 0, 0, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, alpha));
        g2d.setColor(color);

        g2d.fillRect(0, 0, imgWidth, imgHeight);
        g2d.dispose();

        FILTER.setRadius(10);
        imgBlur = FILTER.filter(imgBlur, null);

        return imgBlur;
    }
    
    private void loadInitialConfiguration(){
        textfield.setOpaque(false);
        peek.setVisible(false);
        setAsPasswordField(false);
        LABEL_TEXT = "Label";
        PASSWORD_ECHO_CHAR = '●';
        INFO_LABEL_SHOWN = false;
        footer.setVisible(false);
        setOpaque(false);
        setButtonShown(false);
        textfield.setBorder(null);
        textfield.setBackground(NORMAL_BACKGROUND);
        BACKGROUND = NORMAL_BACKGROUND;
        initButtonConfigurations();
        label.setFont(new Font("SansSerif",0,11));
        labely = 9;
    }
    
    private void initButtonConfigurations(){
        button.addMouseListener(new MouseListener() {
            @Override public void mouseClicked(MouseEvent e) { }
            @Override public void mousePressed(MouseEvent e) { if(e.getButton()==1){buttonPressed(); } }
            @Override public void mouseReleased(MouseEvent e) { }
            @Override public void mouseEntered(MouseEvent e) { buttonHover(); }
            @Override public void mouseExited(MouseEvent e) { buttonExited(); }
        });
        button.addFocusListener(new FocusListener() {
            @Override public void focusGained(FocusEvent e) { buttonHover(); }
            @Override public void focusLost(FocusEvent e) { buttonExited(); }});
        button.addKeyListener(new KeyListener() {
            @Override public void keyTyped(KeyEvent e) { }
            @Override public void keyPressed(KeyEvent e) { }
            @Override public void keyReleased(KeyEvent e) { 
                if((e.getKeyCode()==KeyEvent.VK_ENTER || e.getKeyCode()==KeyEvent.VK_SPACE) && ENABLED){ animateAction(); } 
            }});
        buttonExited();
    }
    
    public void animateAction(){
        if(EsonProject.start()){
            new Thread() {
                @Override public void run() {
                    try {
                        buttonPressed();
                        Thread.sleep(150);
                        buttonExited();
                        Thread.sleep(50);
                        buttonAction();
                    } catch (InterruptedException ex) {                    }
                }
            }.start();
        }
    }
    
    private void buttonAction(){
        if(ENABLED){
            MouseListener[] mouse = button.getMouseListeners();
            if(mouse.length>0){
                for(MouseListener m:mouse){
                    buttonExited();
                    m.mouseReleased(null);
                }
            }
        }
    }
    
    public void setInfoLabelForeground(Color color){
        footer.setForeground(color);
    }
    
    public Color getInfoLabelForeground(){
        return footer.getForeground();
    }
    
    public void setInfoText(String text){
        footer.setText(text);
    }
    
    public String getInfoText(){
        return footer.getText();
    }
    
    public JLabel getInfoLabel(){
        return footer;
    }
    
    public void setInfoLabelShown(boolean flag){
        INFO_LABEL_SHOWN = flag;
        footer.setVisible(flag);
    }
    
    public boolean isInfoLabelShown(){
        return INFO_LABEL_SHOWN;
    }
    
    public void setCornerRadius(int radius){ 
        radius = radius>25?25:radius;
        CORNER_RADIUS = radius;
        repaint();
    }
    
    public int getCornerRadius(){
        return CORNER_RADIUS;
    }
    
    public void setSelectionBackground(Color color){
        SELECTION_BACKGROUND = color;
        textfield.setSelectionColor(color);
    }
    
    public Color getSelectionBackground(){
        return SELECTION_BACKGROUND;
    }
    
    public void setSelectionForeground(Color color){
        SELECTION_FOREGROUND = color;
        textfield.setSelectedTextColor(color);
    }
    
    public Color getSelectionForeground(){
        return SELECTION_FOREGROUND;
    }
    
    public void setNormalBackground(Color color){
        NORMAL_BACKGROUND = color;
        textfield.setBackground(color);
        BACKGROUND = color;
        repaint();
    }
    
    public void setParentBackground(Color color){
        PARENT_BACKGROUND = color;
        label.repaint();
        repaint();
    }
    
    public Color getParentBackground(){
        return PARENT_BACKGROUND;
    }
    
    public Color getNormalBackground(){
        return NORMAL_BACKGROUND;
    }
    
    public JPanel getContainer(){
        return container;
    }
    
    public void setNormalForeground(Color color){
        NORMAL_FOREGROUND = color;
        BUTTON_N_FOREGROUND = color;
        repaint();
    }
    
    public Color getNormalForeground(){
        return NORMAL_FOREGROUND;
    }
    
    public void setNormalBorderColor(Color color){
        NORMAL_BORDER = color;
        BORDER = color;
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
        BUTTON_H_FOREGROUND = color;
        BUTTON_P_FOREGROUND = color.darker();
        repaint();
    }
    
    public Color getGainedBorderColor(){
        return GAINED_BORDER;
    }
    
    public void setHintForeground(Color color){
        HINT_FOREGROUND = color;
        repaint();
    }
    
    public Color getHintForeground(){
        return HINT_FOREGROUND;
    }
    
    public void setButtonShown(boolean flag){
        BUTTON_SHOWN = flag;
        button.setVisible(flag);
    }
    
    public boolean isButtonShown(){
        return BUTTON_SHOWN;
    }
    
    public void setFieldEnabled(boolean flag){
        ENABLED = flag;
        repaint();
    }
    
    public boolean isFieldEnabled(){
        return ENABLED;
    }
    
    @Override
    public void setToolTipText(String text){
        super.setToolTipText(text);
        textfield.setToolTipText(text);
    }
    
    public void setButtonWidth(int width){
        if(width<81){
            int contWidth = container.getWidth() - 10;
            int textWidth = 81 - width;
            button.setSize(width, button.getHeight());
            button.setPreferredSize(new Dimension(width, button.getHeight()));
            int x = getWidth() - 2 - width;
            button.setLocation(x, button.getY());
            if (!peek.isShowing()) {
                textfield.setSize(contWidth - 20 - textWidth, textfield.getHeight());
                textfield.setBackground(Color.RED);
            }
        }
//        button.revalidate(); button.updateUI();
//        revalidate(); updateUI(); repaint();
    }
    
    public void setPasswordEchoChar(char ch){
        PASSWORD_ECHO_CHAR = ch;
    }
    
    public char getPasswordEchoChar(){
        return PASSWORD_ECHO_CHAR;
    }
    
    public void setHorizontalTextAlignment(EsonTextAlignment al){
        ALIGNMENT = al;
        checkHorizontalAlignment();
    }
    
    public EsonTextAlignment getHorizontalTextAlignment(){
        return ALIGNMENT;
    }
    
    private void checkHorizontalAlignment(){
        switch(ALIGNMENT){
            case LEFT -> textfield.setHorizontalAlignment(JTextField.LEFT);
            case RIGHT -> textfield.setHorizontalAlignment(JTextField.RIGHT);
            case CENTER -> textfield.setHorizontalAlignment(JTextField.CENTER);
            case LEADING -> textfield.setHorizontalAlignment(JTextField.LEADING);
            case TRAILING -> textfield.setHorizontalAlignment(JTextField.TRAILING);
        }
    }
    
    public void setIcon(Image image, int width, int height, Color foreground){
        label.setIcon(RENDERER.resizeIcon(image, width, height, foreground));
    }
    
    public void setIcon(Image image, int width, int height, Color foreground, int gap){
        setIconTextGap(gap);
        label.setIcon(RENDERER.resizeIcon(image, width, height, foreground));
    }
    
    public void setIconTextGap(int gap){
        label.setIconTextGap(gap);
    }
    
    public void setAsPasswordField(boolean flag){
        AS_PASSWORD = flag;
        textfield.setEchoChar(flag?PASSWORD_ECHO_CHAR:(char) 0);
    }
    
    public boolean isAsPasswordField(){
        return AS_PASSWORD;
    }
    
    public void setEditable(boolean flag){
        textfield.setEditable(flag);
    }
    
    //Listeners
    private void initListeners(){
        addFocusListener(new FocusListener() {
            @Override public void focusGained(FocusEvent e){ 
                textfield.requestFocus(true);
            }
            @Override public void focusLost(FocusEvent e){}
        });
        textfield.addFocusListener(new FocusListener() {
            @Override public void focusGained(FocusEvent arg0) {
                isError = false; isWarning = false;
                textFocusGained(); 
            }
            @Override public void focusLost(FocusEvent arg0) { 
                if(!isError && !isWarning){
                    textFocusLost();
                }
            }
        });
    }
    
    private void refreshField(){
        if(HINT_SHOWN){
            String txt = "@" + LABEL_TEXT.toUpperCase();
            if (textfield.getText().equals(txt)) {
                textfield.setText("");
//                label.setOpaque(false);
                if(LABEL_SHOWN){
//                    label.setBackground(PARENT_BACKGROUND);
//                    label.setOpaque(true);
                    label.setText(LABEL_TEXT);
                }
            }
        }
        textfield.setForeground(TEXT_FOREGROUND);
        textfield.setEchoChar(AS_PASSWORD ? PASSWORD_ECHO_CHAR : (char) 0);
        checkHorizontalAlignment();
    }
    
    public void addActionListener(ActionListener l){
        listenerList.add(ActionListener.class, l);
        textfield.addActionListener(l);
    }
    
    public void removeActionListener(ActionListener l){
        listenerList.remove(ActionListener.class, l);
        textfield.removeActionListener(l);
    }
    
//    @Override
//    public void addFocusListener(FocusListener l){
//        textfield.addFocusListener(l);
//    }
//    
//    @Override
//    public void removeFocusListener(FocusListener l){
////        listenerList.remove(FocusListener.class, l);
//        textfield.removeFocusListener(l);
//    }
    
    @Override
    public void addKeyListener(KeyListener l){
        listenerList.add(KeyListener.class, l);
        textfield.addKeyListener(l);
    }
    
    @Override
    public void removeKeyListener(KeyListener l){
        listenerList.remove(KeyListener.class, l);
        textfield.removeKeyListener(l);
    }
    
    //Attributes
    @Override
    public void setNextFocusableComponent(Component c){
        textfield.setNextFocusableComponent(c);
    }
    
    public void focusLost(){
        textFocusLost();
    }
    
    public void setText(String text){
        refreshField();
        setFieldForeground(TEXT_FOREGROUND);
        textfield.setText(text);
        if (HINT_SHOWN && !textfield.hasFocus()) {
            if (textfield.getText().trim().equals("")) {
                textfield.setText("@" + LABEL_TEXT.toUpperCase());
                textfield.setFont(HINT_FONT!=null?HINT_FONT:TEXT_FONT);
                textfield.setForeground(HINT_FOREGROUND);
                textfield.setHorizontalAlignment(JTextField.LEADING);
                if(LABEL_SHOWN){label.setText("");
//                label.setOpaque(false);
                }
                textfield.setEchoChar((char) 0);
            }else{
                if(LABEL_SHOWN){label.setText(LABEL_TEXT);
//                label.setOpaque(true);
                }
            }
        }
    }
    
    public String getText(){
        return textfield.getText();
    }
    
    //returns if hasSelectedtext
    public boolean hasSelectedText(){
        return textfield.getSelectedText()!=null;
    }
    
    public String getSelectedText(){
        return textfield.getSelectedText();
    }
    
    //getting JComponents
    public JLabel getJLabel(){
        return label;
    }
    
    public JTextField getJTextField(){
        return textfield;
    }
    
    public void selectAll(){
        textfield.selectAll();
    }
    
    //Label Attributes
    public void setLabel(String text){
        LABEL_TEXT = text;
        if(LABEL_SHOWN){label.setText("");
//        label.setOpaque(false);
        }
        refreshHint();
    }
    
    public String getLabel(){
        return LABEL_TEXT;
    }
    
    public void setLabelForeground(Color color){
        LABEL_FOREGROUND = color;
        if(LABEL_SHOWN){label.setForeground(color);}
    }
    
    public Color getLabelForeground(){
        return LABEL_FOREGROUND;
    }
    
    public void setTextFieldBackground(Color color){
        textfield.setBackground(color);
        button.setBorder(BorderFactory.createMatteBorder(2, 0, 2, 2, textfield.getBackground()));
    }
    
    public Color getTextFieldBackground(){
        return textfield.getBackground();
    }
    
    public void setTextFieldForeground(Color color){
        TEXT_FOREGROUND = color;
        setFieldForeground(color);
    }
    
    public Color getTextFieldForeground(){
        return TEXT_FOREGROUND;
    }
    
    public void setErrorForeground(Color color){
        ERROR_FOREGROUND = color;
    }
    
    public Color getErrorForeground(){
        return ERROR_FOREGROUND;
    }
    
    public void setWarningForeground(Color color){
        WARNING_FOREGROUND = color;
    }
    
    public Color getWarningForeground(){
        return WARNING_FOREGROUND;
    }
    
    private Font TEXT_FONT = new Font("Arial",0,14);
    @Override
    public void setFont(Font font){
        TEXT_FONT = font;
        if(textfield!=null){
            textfield.setFont(font);
            button.setFont(font);
        }
    }
    
    @Override
    public Font getFont(){
        return textfield!=null?textfield.getFont():new Font("Arial",0,14);
    }
    
    private Font HINT_FONT = null;
    public void setHintFont(Font font){
        HINT_FONT = font;
    }
    
    public Font getHintFont(){
        return HINT_FONT;
    }
    
    public void setLabelShown(boolean flag){
        LABEL_SHOWN = flag;
        if(!flag){
//            label.setOpaque(false);
            label.setText("");
        }
        revalidate(); repaint();
    }
    
    public boolean isLabelShown(){
        return LABEL_SHOWN;
    }
    
    private boolean isError = false, isWarning = false;
    public void ERROR(){
        textFocusLost();
        isError = true;
        BACKGROUND = NORMAL_BACKGROUND;
        BORDER = ERROR_FOREGROUND;
        setFieldForeground(ERROR_FOREGROUND);
        BORDER_STROKE = new BasicStroke(1f);
        label.setForeground(!textfield.getText().equals("@"+LABEL_TEXT.toUpperCase())?ERROR_FOREGROUND:PARENT_BACKGROUND);
        if(BUTTON_SHOWN){
            buttonError();
        }
        if(label.getIcon()!=null){
            label.setIcon(new ImageIcon(RENDERER.maskImage(((ImageIcon)label.getIcon()).getImage(), ERROR_FOREGROUND)));
        }
        if(peek.isVisible()){
            ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/json/asidera/images/eye_close.png"));
            peek.setIcon(RENDERER.resizeIcon(icon.getImage(), peek.getWidth()-15, peek.getWidth()-15, ERROR_FOREGROUND));  
        }
        repaint();
    }
    
    public void ERROR_B(){
        textFocusLost();
        isError = true;
        BACKGROUND = NORMAL_BACKGROUND;
        BORDER = ERROR_FOREGROUND;
        setFieldForeground(ERROR_FOREGROUND);
        BORDER_STROKE = new BasicStroke(1f);
        label.setForeground(!textfield.getText().equals("@"+LABEL_TEXT.toUpperCase())?LABEL_FOREGROUND:PARENT_BACKGROUND);
        if(BUTTON_SHOWN){
            buttonError();
        }
        if(label.getIcon()!=null){
            label.setIcon(new ImageIcon(RENDERER.maskImage(((ImageIcon)label.getIcon()).getImage(), LABEL_FOREGROUND)));
        }
        if(peek.isVisible()){
            ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/json/asidera/images/eye_close.png"));
            peek.setIcon(RENDERER.resizeIcon(icon.getImage(), peek.getWidth()-15, peek.getWidth()-15, ERROR_FOREGROUND));  
        }
        repaint();
    }
    
    public void WARNING(){
        textFocusLost();
        isWarning = false;
        BACKGROUND = NORMAL_BACKGROUND;
        BORDER = WARNING_FOREGROUND;
        setFieldForeground(WARNING_FOREGROUND);
        BORDER_STROKE = new BasicStroke(1f);
        label.setForeground(!textfield.getText().equals("@"+LABEL_TEXT.toUpperCase())?WARNING_FOREGROUND:PARENT_BACKGROUND);
        if(label.getIcon()!=null){
            label.setIcon(new ImageIcon(RENDERER.maskImage(((ImageIcon)label.getIcon()).getImage(), WARNING_FOREGROUND)));
        }
        repaint();
    }
    
    public void CLEAR(){
        setText("");
        textFocusLost();
        peek.setVisible(false);
        footer.setText("");
        BACKGROUND = NORMAL_BACKGROUND;
        BORDER = NORMAL_BORDER;
        setFieldForeground(HINT_FOREGROUND);
        BORDER_STROKE = new BasicStroke(1f);
        label.setText(HINT_SHOWN?"":LABEL_TEXT);
        if(label.getIcon()!=null){
            label.setIcon(new ImageIcon(RENDERER.maskImage(((ImageIcon)label.getIcon()).getImage(), NORMAL_FOREGROUND)));
        }
        repaint();
    }
    
    private void setFieldForeground(Color color){
        textfield.setForeground(color);
        textfield.setCaretColor(color);
        label.setForeground(LABEL_FOREGROUND);
        if(label.getIcon()!=null){
            label.setIcon(new ImageIcon(RENDERER.maskImage(((ImageIcon)label.getIcon()).getImage(), LABEL_FOREGROUND)));
        }
        peek.setBackground(LABEL_FOREGROUND);
    }
    
    public void setHintShown(boolean flag){
        HINT_SHOWN = flag;
        refreshHint();
    }
    
    private void refreshHint(){
        if(HINT_SHOWN){
            textfield.setText("@"+label.getText().toUpperCase());
            textfield.setForeground(HINT_FOREGROUND);
            textfield.setHorizontalAlignment(JTextField.LEADING);
            label.setForeground(PARENT_BACKGROUND);
            textfield.setEchoChar((char) 0);
        }else{
            textfield.setText(("@"+label.getText().toUpperCase()).equals(textfield.getText().trim().toUpperCase())?"":textfield.getText());
            label.setForeground(LABEL_FOREGROUND);
        }
    }
    
    public boolean getHintShown(){
        return HINT_SHOWN;
    }
    
    public void textFocusLost(){
        if(ENABLED){
            FOCUS_GAINED = false;
            textfield.setBackground(NORMAL_BACKGROUND);
            BACKGROUND = NORMAL_BACKGROUND;
            BORDER = NORMAL_BORDER;
            if(label.getForeground()!=ERROR_FOREGROUND || label.getForeground()!=WARNING_FOREGROUND){
                if (HINT_SHOWN) {
                    if (textfield.getText().trim().equals("")) {
                        textfield.setText("@" + LABEL_TEXT.toUpperCase());
                        textfield.setFont(HINT_FONT!=null?HINT_FONT:TEXT_FONT);
                        textfield.setForeground(HINT_FOREGROUND);
                        textfield.setHorizontalAlignment(JTextField.LEADING);
//                        label.setForeground(PARENT_BACKGROUND);
                        label.setText("");
//                        label.setOpaque(false);
                        labely = 9;
                        textfield.setEchoChar((char) 0);
                    }  
                } else {
                    refreshField();
                }
                if (BUTTON_SHOWN) {
                    buttonExited();
                }
            }
            repaint(); label.repaint();
        }
    }
    
    public void textFocusGained(){
        if(ENABLED){
            FOCUS_GAINED = true;
            if(BORDER_STROKE.getLineWidth() == 1f){
                BORDER_STROKE = new BasicStroke(0.7f);
            }
            textfield.setForeground(GAINED_FOREGROUND);
            textfield.setBackground(GAINED_BACKGROUND);
            textfield.setFont(TEXT_FONT);
            BACKGROUND = GAINED_BACKGROUND;
            BORDER = GAINED_BORDER;
            TEXT_FOREGROUND = GAINED_FOREGROUND;
            label.setForeground(LABEL_FOREGROUND);
            setFieldForeground(TEXT_FOREGROUND);
            if (HINT_SHOWN) {
                String txt = "@" + LABEL_TEXT.toUpperCase();
                if (textfield.getText().equals(txt)) {
                    labely = 7;
                    refreshField();
                }
            } else {
                refreshField();
            }
            if (BUTTON_SHOWN) {
                buttonExited();
            }
            if(label.getIcon()!=null){
                label.setIcon(new ImageIcon(RENDERER.maskImage(((ImageIcon)label.getIcon()).getImage(), LABEL_FOREGROUND)));
            }
            repaint();
        }
    }
    
    private void buttonHover(){
        button.setForeground(BUTTON_H_FOREGROUND);
        refreshButtonIcon();
    }
    
    private void buttonExited(){
        button.setForeground(BUTTON_N_FOREGROUND);
        refreshButtonIcon();
    }
    
    private void buttonPressed(){
        button.setForeground(BUTTON_P_FOREGROUND);
        refreshButtonIcon();
    }
    
    private void buttonError(){
        button.setForeground(EsonColor.ERROR);
        refreshButtonIcon();
    }
    
    public void refreshButtonIcon(){
        if(BUTTON_IMAGE!=null){
            button.setIcon(RENDERER.resizeIcon(BUTTON_IMAGE, BUTTON_WIDTH, BUTTON_HEIGHT, button.getForeground()));
        }
    }
    
    public JLabel getButton(){
        return button;
    }
    
    public void setButtonText(String text){
        button.setText(text);
    }
    
    public String getButtonText(){
        return button.getText();
    }
    
    
    private Image BUTTON_IMAGE = null;
    private int BUTTON_WIDTH = 11, BUTTON_HEIGHT = 11;
    public void setButtonIcon(Image image, int w, int h){
        BUTTON_IMAGE = image;
        BUTTON_WIDTH = w;
        BUTTON_HEIGHT = h;
        buttonExited();
    }
    
    public void setButtonNormalForeground(Color color){
        BUTTON_N_FOREGROUND = color;
        buttonExited();
    }
    
    public Color getButtonNormalForeground(){
        return BUTTON_N_FOREGROUND;
    }
    
    public void setButtonHoverForeground(Color color){
        BUTTON_H_FOREGROUND = color;
        buttonExited();
    }
    
    public Color getButtonHoverForeground(){
        return BUTTON_H_FOREGROUND;
    }
    
    public void setButtonPressedForeground(Color color){
        BUTTON_P_FOREGROUND = color;
        buttonExited();
    }
    
    public Color getButtonPressedForeground(){
        return BUTTON_P_FOREGROUND;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        container = new javax.swing.JPanel(){

            public void paintComponent(Graphics g){
                super.paintComponent(g);
                paintBackground(g);
            }

        };
        peek = new javax.swing.JLabel();
        textfield = new javax.swing.JPasswordField();
        label = new javax.swing.JLabel();
        button = new javax.swing.JLabel(){

            @Override
            public JToolTip createToolTip() {
                JToolTip tip = super.createToolTip();
                tip.setBackground(BUTTON_N_FOREGROUND);
                tip.setForeground(PARENT_BACKGROUND);
                tip.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));
                tip.setFont(new Font("Arial",2,9));
                return tip;
            }

        };
        footer = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });

        container.setOpaque(false);
        container.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 12, 0, 5));
        container.setPreferredSize(new java.awt.Dimension(281, 40));

        peek.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        peek.setOpaque(true);
        peek.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                peekMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                peekMouseReleased(evt);
            }
        });

        textfield.setBorder(null);
        textfield.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textfieldKeyReleased(evt);
            }
        });

        label.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label.setText("label");
        label.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4));

        button.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        button.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout containerLayout = new javax.swing.GroupLayout(container);
        container.setLayout(containerLayout);
        containerLayout.setHorizontalGroup(
            containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(containerLayout.createSequentialGroup()
                        .addComponent(label)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(containerLayout.createSequentialGroup()
                        .addComponent(textfield, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                        .addGap(5, 5, 5)
                        .addComponent(peek, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(button, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        containerLayout.setVerticalGroup(
            containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(label, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(containerLayout.createSequentialGroup()
                        .addGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(peek, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(textfield, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE))
                        .addGap(11, 11, 11))
                    .addGroup(containerLayout.createSequentialGroup()
                        .addComponent(button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        footer.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 8, 0, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(container, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
            .addComponent(footer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(container, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(footer, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void peekMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_peekMousePressed
        textfield.setEchoChar((char) 0);
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/json/asidera/images/eye_open.png"));
        peek.setIcon(RENDERER.resizeIcon(icon.getImage(), peek.getWidth()-15, peek.getWidth()-15, textfield.getForeground()));
    }//GEN-LAST:event_peekMousePressed

    private void peekMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_peekMouseReleased
        textfield.setEchoChar(PASSWORD_ECHO_CHAR);
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/json/asidera/images/eye_close.png"));
        peek.setIcon(RENDERER.resizeIcon(icon.getImage(), peek.getWidth()-15, peek.getWidth()-15, textfield.getForeground()));
    }//GEN-LAST:event_peekMouseReleased

    private void textfieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textfieldKeyReleased
        if(AS_PASSWORD){
            boolean show = !textfield.getText().trim().equals("");
            peek.setVisible(show);
            peek.setOpaque(false);
            ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/json/asidera/images/eye_close.png"));
            if (peek.getWidth() > 0 && peek.getHeight() > 0) {
                peek.setIcon(RENDERER.resizeIcon(icon.getImage(), peek.getWidth() - 15, peek.getWidth() - 15, textfield.getForeground()));
            }
        }
    }//GEN-LAST:event_textfieldKeyReleased

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        DRAGGED = false;
    }//GEN-LAST:event_formMouseReleased

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        DRAGGED = true;
    }//GEN-LAST:event_formMouseDragged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel button;
    private javax.swing.JPanel container;
    private javax.swing.JLabel footer;
    private javax.swing.JLabel label;
    private javax.swing.JLabel peek;
    private javax.swing.JPasswordField textfield;
    // End of variables declaration//GEN-END:variables
}
