/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package eson.core;

import eson.core.util.ImageRenderer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Timer;

/**
 *
 * @author ZOE CODEY
 */
public class LicenseDialog extends javax.swing.JDialog {

    /**
     * Creates new form LicenseDialog
     */
    
    private boolean MODAL = false, BUTTON_SHADOW = false,
            PANEL_SHADOW_SHOWN = false, PANEL_SHADOW = false,
            PRESSED = false;
    private Color MODAL_COLOR = EsonColor.ERROR,
            BUTTON_BACKGROUND,
            BUTTON_PRESSED_BORDER,
            NORMAL_BACKGROUND,
            NORMAL_FOREGROUND,
            HOVER_BACKGROUND,
            HOVER_FOREGROUND,
            PRESSED_BACKGROUND,
            PRESSED_FOREGROUND,
            PRESSED_BORDER;
    private BufferedImage BUTTON_SHADOW_IMAGE = null, PANEL_SHADOW_IMAGE = null;
    private ImageRenderer RENDERER = null;
    private EsonUI esonUI = null;
    
    public LicenseDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        RENDERER = new ImageRenderer();
        esonUI = new EsonUI();
        initComponents();
        initConfigurations();
    }
    
    private void initConfigurations(){
        NORMAL_BACKGROUND = new Color(220,53,69);
        NORMAL_FOREGROUND = new Color(255,255,255);
        HOVER_BACKGROUND = new Color(187,45,59);
        HOVER_FOREGROUND = NORMAL_FOREGROUND;
        PRESSED_BACKGROUND = HOVER_BACKGROUND;
        PRESSED_FOREGROUND = HOVER_FOREGROUND;
        PRESSED_BORDER = new Color(240,169,176);
        setWindowTransparent(this);
        setButtonListeners();
        esonUI.initLabelAsLink(email, EsonColor.PRIMARY, EsonColor.INFORMATION);
        esonUI.initLabelAsLink(facebook, EsonColor.PRIMARY, EsonColor.INFORMATION);
    }
    
    private void setButtonListeners(){
        buttonPanel.addMouseListener(new MouseListener() {
            @Override public void mouseClicked(MouseEvent e) { }
            @Override public void mousePressed(MouseEvent e) { if(e.getButton()==1){statePressed(); } }
            @Override public void mouseReleased(MouseEvent e) { if(e.getButton()==1){stateReleased(); buttonAction();} }
            @Override public void mouseEntered(MouseEvent e) { stateHover(); }
            @Override public void mouseExited(MouseEvent e) { stateExited(); }
        });
        buttonPanel.addFocusListener(new FocusListener() {
            @Override public void focusGained(FocusEvent e) { stateHover(); }
            @Override public void focusLost(FocusEvent e) { stateExited(); }});
        buttonPanel.addKeyListener(new KeyListener() {
            @Override public void keyTyped(KeyEvent e) { }
            @Override public void keyPressed(KeyEvent e) { }
            @Override public void keyReleased(KeyEvent e) { 
                if((e.getKeyCode()==KeyEvent.VK_ENTER || e.getKeyCode()==KeyEvent.VK_SPACE)){ animateAction(); } 
            }});
        stateExited();
    }
    
    public void showUnlicensed(){
        license.setText("UNLICENSED COPY");
        showDialog();
    }
    
    public void showExpiration(){
        try{
            Date EXPIRATION = new SimpleDateFormat("yyyy-MM-dd").parse(EsonProject.getExpirationDate()); 
            license.setText("Expiration: "+new SimpleDateFormat("dd-MMM-yy").format(EXPIRATION));
        }catch(Exception ex){
            System.err.println("LICENSE DIALOG SHOW EXPIRATION: "+ex.getMessage());
        }
        showDialog();
    }
    
    private void showDialog(){
        PANEL_SHADOW = true;
        PANEL_SHADOW_SHOWN = true;
        BUTTON_SHADOW = true;
        MainPanel.repaint();
        startWindowIntro(this);
        buttonPanel.requestFocus(true);
        setVisible(true);
    }
    
    public void startWindowIntro(Window window){
        y = -40; f = 0; INTRO_WINDOW = window;
        setWindowOpacity(window, 0f);
        window.setLocation(0,y);
        EventQueue.invokeLater(INTRO_RUNNABLE);
    }
    
    private Window INTRO_WINDOW = null;
    private int INTRO_COUNTER = 0;
    private Runnable INTRO_RUNNABLE = new Runnable() {
        @Override
        public void run() {
            if(introTimer.isRunning()){
                introTimer.stop();
            }
            INTRO_COUNTER = 0;
            introTimer.start();
            }
    };
    private int y = -50;
    private float f = 0;
    private Timer introTimer = new Timer(5, (ActionEvent e) -> {
        y += 10;
        f = f<1?f+0.10f:1f;
        if(INTRO_COUNTER==5){
            INTRO_WINDOW.setLocation(0,0);
            setWindowOpacity(INTRO_WINDOW,1f);
            animateModal(EsonColor.ERROR);
            ((Timer)e.getSource()).stop();
        }else{
            INTRO_WINDOW.setLocation(0,y);
            setWindowOpacity(INTRO_WINDOW, f);
        }
        INTRO_COUNTER++;
    });
    
    public void setWindowOpacity(Window window, float opacity){
        window.setOpacity(opacity);
    }
    
    public void animateAction(){
        new Thread() {
            @Override public void run() {
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
    
    private void buttonAction(){
        System.exit(0);
    }
    
    private void stateReleased(){
        BUTTON_SHADOW = true;
        PRESSED = false;
        stateExited();
    }
    
    public void stateExited(){
        BUTTON_BACKGROUND = NORMAL_BACKGROUND;
        buttonLabel.setForeground(NORMAL_FOREGROUND);
        buttonPanel.repaint();
    }
    
    public void stateHover(){
        BUTTON_BACKGROUND = HOVER_BACKGROUND;
        buttonLabel.setForeground(HOVER_FOREGROUND);
        buttonPanel.repaint();
    }
    
    public void statePressed(){
        BUTTON_SHADOW = false;
        PRESSED = true;
        BUTTON_BACKGROUND = PRESSED_BACKGROUND;
        BUTTON_PRESSED_BORDER = PRESSED_BORDER;
        buttonLabel.setForeground(PRESSED_FOREGROUND);
        buttonPanel.repaint();
    }
    
    private void setWindowTransparent(Window window){
        window.setBackground(new java.awt.Color(0,0,0,100));
        window.setSize(window.getToolkit().getScreenSize());
    }
    
    private void paintBody(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        g2d.setStroke(new BasicStroke(1f));
        int CORNER_RADIUS = 10;
        Color BACKGROUND = Color.WHITE;
        int y = 0, WIDTH = MainPanel.getWidth(), HEIGHT = MainPanel.getHeight();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if(MODAL){
            g2d.setColor(MODAL_COLOR);
            g2d.fillRoundRect(1,1+y,WIDTH-2,HEIGHT-(2+y),CORNER_RADIUS+5,CORNER_RADIUS+5);
        }
        if(PANEL_SHADOW){
            if(PANEL_SHADOW_IMAGE == null){
                BufferedImage img = RENDERER.createCompatibleImage(WIDTH, HEIGHT);
                Graphics2D tg2d = img.createGraphics();
                tg2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                tg2d.fillRoundRect(5, 5+y, WIDTH - 12, HEIGHT - (14+y), CORNER_RADIUS, CORNER_RADIUS);
                tg2d.dispose();
                PANEL_SHADOW_IMAGE = RENDERER.generateShadow(img, 5, Color.black, 0.5f);
            }
            g2d.drawImage(PANEL_SHADOW_IMAGE, 2, 4, null);
        }
        g2d.setColor(BACKGROUND);
        g2d.fillRoundRect(5,5+y,WIDTH-11,HEIGHT-(11+y),CORNER_RADIUS,CORNER_RADIUS);
    }
    
    private void paintButton(Graphics g){
        int CORNER_RADIUS = 10;
        Graphics2D g2d = (Graphics2D)g;
        if(PRESSED){
            g2d.setColor(BUTTON_PRESSED_BORDER);
            g2d.fillRoundRect(1 ,1 ,buttonPanel.getWidth()-2,buttonPanel.getHeight()-2,CORNER_RADIUS,CORNER_RADIUS);
        }
        if(BUTTON_SHADOW){
            if(BUTTON_SHADOW_IMAGE==null){
                BufferedImage img = RENDERER.createCompatibleImage(buttonPanel.getWidth(), buttonPanel.getHeight());
                Graphics2D tg2d = img.createGraphics();
                tg2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                tg2d.fillRoundRect(5, 5, buttonPanel.getWidth() - 10, buttonPanel.getHeight() - 10, CORNER_RADIUS, CORNER_RADIUS);
                tg2d.dispose();
                BUTTON_SHADOW_IMAGE = RENDERER.generateShadow(img, 5, Color.BLACK, 0.5f);
            }
            g2d.drawImage(BUTTON_SHADOW_IMAGE, 0, 1, null);
        }
        g2d.setColor(BUTTON_BACKGROUND);
        g2d.fillRoundRect(5, 5, buttonPanel.getWidth()-10, buttonPanel.getHeight()-10,CORNER_RADIUS,CORNER_RADIUS);
    }
    
    public void animateModal(Color color){
        MODAL_COLOR = color;
        EventQueue.invokeLater(MODAL_RUNNABLE);
    }
    
    private int COUNTER = 0;
    private Runnable MODAL_RUNNABLE = new Runnable() {
        @Override
        public void run() {
            Toolkit.getDefaultToolkit().beep();
            PANEL_SHADOW = false;
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
            PANEL_SHADOW = PANEL_SHADOW_SHOWN;
        }
        COUNTER++;
        repaint();
    });

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MainPanel = new javax.swing.JPanel(){

            public void paintComponent(Graphics g){
                super.paintComponent(g);
                paintBody(g);
            }

        };
        MainPanel.setOpaque(false);
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        license = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        buttonPanel = new javax.swing.JPanel(){

            public void paintComponent(Graphics g){
                super.paintComponent(g);
                paintButton(g);
            }

        };
        buttonPanel.setOpaque(false);
        buttonLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        facebook = new javax.swing.JLabel();
        email = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        MainPanel.setBackground(new java.awt.Color(255, 255, 255));
        MainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 6, 6, 6));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/json/asidera/images/eson_project_logo.png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("EsonProject Library v1.2.93");

        license.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        license.setForeground(new java.awt.Color(204, 0, 0));
        license.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        license.setText("UNLICENSED COPY");

        jLabel5.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Copyright (c) 2018-2022");

        jLabel6.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("All Rights Reserved");

        jLabel7.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("<html><p align=\"center\">Your copy of this swing library is not licensed. Contact project developer for more info. God Bless! :)</p></html>");
        jLabel7.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 18, 1, 18));

        buttonLabel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        buttonLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        buttonLabel.setText("CLOSE");

        javax.swing.GroupLayout buttonPanelLayout = new javax.swing.GroupLayout(buttonPanel);
        buttonPanel.setLayout(buttonPanelLayout);
        buttonPanelLayout.setHorizontalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(buttonLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
        );
        buttonPanelLayout.setVerticalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(buttonLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        jPanel2.setOpaque(false);

        jLabel9.setFont(new java.awt.Font("SansSerif", 0, 11)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("FACEBOOK");

        jLabel11.setFont(new java.awt.Font("SansSerif", 0, 11)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText(":");

        jLabel8.setFont(new java.awt.Font("SansSerif", 0, 11)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("EMAIL");

        facebook.setFont(new java.awt.Font("SansSerif", 0, 11)); // NOI18N
        facebook.setForeground(new java.awt.Color(0, 0, 0));
        facebook.setText("Wawa Eson Robin Asidera");
        facebook.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 0));
        facebook.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                facebookMouseReleased(evt);
            }
        });

        email.setFont(new java.awt.Font("SansSerif", 0, 11)); // NOI18N
        email.setForeground(new java.awt.Color(0, 0, 0));
        email.setText("ajaysonra02@gmail.com");
        email.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 0));
        email.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                emailMouseReleased(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("SansSerif", 0, 11)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText(":");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10))
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(facebook, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                    .addComponent(email, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(0, 0, 0)
                        .addComponent(jLabel9))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(email, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(facebook, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout MainPanelLayout = new javax.swing.GroupLayout(MainPanel);
        MainPanel.setLayout(MainPanelLayout);
        MainPanelLayout.setHorizontalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MainPanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel1)
                        .addGap(15, 15, 15)
                        .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(license, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(MainPanelLayout.createSequentialGroup()
                        .addGap(225, 225, 225)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(MainPanelLayout.createSequentialGroup()
                        .addGap(225, 225, 225)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(25, 25, 25))
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        MainPanelLayout.setVerticalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1)
                    .addGroup(MainPanelLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel2)
                        .addGap(0, 0, 0)
                        .addComponent(jLabel5)
                        .addGap(0, 0, 0)
                        .addComponent(jLabel6)
                        .addGap(0, 0, 0)
                        .addComponent(license, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        MainPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {buttonPanel, jPanel2});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(MainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(MainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        
    }//GEN-LAST:event_formComponentShown

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        animateModal(EsonColor.ERROR);
    }//GEN-LAST:event_formMouseClicked

    private void emailMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_emailMouseReleased
        EsonProject.sendEmailLink("ajaysonra02@gmail.com", "Eson_Project_License_Request");
    }//GEN-LAST:event_emailMouseReleased

    private void facebookMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_facebookMouseReleased
        EsonProject.openBrowserLink("https://www.facebook.com/wawaeson02");
    }//GEN-LAST:event_facebookMouseReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel MainPanel;
    private javax.swing.JLabel buttonLabel;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JLabel email;
    private javax.swing.JLabel facebook;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel license;
    // End of variables declaration//GEN-END:variables
}
