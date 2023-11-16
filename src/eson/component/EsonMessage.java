
package eson.component;

import java.awt.Color;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import eson.component.EsonButton.EsonButtonType;
import eson.core.EsonColor;
import eson.core.EsonUI;
import eson.core.util.ImageRenderer;

/**
 *
 * @author ©Wawa Eson Robin Asidera
 */
public final class EsonMessage extends javax.swing.JDialog{

    private boolean shadowShown = false;
    private int retval = 0;
    private MessageType MESSAGE_TYPE = MessageType.INFORMATION_MESSAGE;
    private ImageRenderer RENDERER = null;
    private EsonUI esonUI = null;
    private Color MAIN_BACKGROUND = Color.WHITE, MAIN_FOREGROUND = Color.BLACK;
    
    public static enum MessageType{
        PLAIN, INFORMATION_MESSAGE, ERROR_MESSAGE, SUCCESS, WARNING_MESSAGE;
    };
    
    public EsonMessage(java.awt.Frame parent) {
        super(parent, true);
        esonUI = new EsonUI();
        initComponents();
        initDialog();
        shadowShown = false;
        RENDERER = new ImageRenderer();
    }
    
    public void setMainBackground(Color color){
        MAIN_BACKGROUND = color;
    }
    
    public Color getMainBackground(){
        return MAIN_BACKGROUND;
    }
    
    public void setMainForeground(Color color){
        MAIN_FOREGROUND = color;
    }
    
    public Color getMainForeground(){
        return MAIN_FOREGROUND;
    }
    
    public void initDialog(){
        esonUI.blurWindow(this);
        MainPanel.setCornerRadius(13);
        MainPanel.setEnableWindowDragging(true);
        MainPanel.setHeaderShown(false);
        MainPanel.setHeaderHeight(30);
        MainPanel.setBackground(MAIN_BACKGROUND);
        if(close!=null){
            close.set_normalForeground(MAIN_FOREGROUND);
            close.set_hoverForeground(Color.RED);
            close.set_pressedForeground(Color.RED);
        }
    }
    
    private void initConfiguration(){
        message.setForeground(MAIN_FOREGROUND);
        MainPanel.setOpaque(false);
        MainPanel.requestFocus();
        MainPanel.setBackground(MAIN_BACKGROUND);
        MainPanel.setHeaderShown(false);
        MainPanel.setCornerRadius(10);
        accept.getLabel().setBorder(BorderFactory.createEmptyBorder(0, 11, 0, 11));
        decline.getLabel().setBorder(BorderFactory.createEmptyBorder(0, 11, 0, 11));
        retval = 0;
    }
    
    private void start(String stitle, String smessage, MessageType messageType){
        if (!shadowShown) {
            shadowShown = true;
            MainPanel.setShadowShown(true);
        }
        initConfiguration();
        title.setText(stitle);
        message.setText(smessage);
        MESSAGE_TYPE = messageType;
        checkColor();
        esonUI.startWindowIntro(this);
        setVisible(true);
        message.requestFocus();
    }
    
    private boolean isConfirm = false;
    public void showMessage(String stitle, String smessage, MessageType messageType){
        decline.setVisible(false);
        close.setVisible(false);
        accept.setText("OKEY");
        isConfirm = false;
        start(stitle,smessage,messageType);
    }
    
    public void showMessage(String stitle, String smessage, MessageType messageType, String buttonText){
        decline.setVisible(false);
        close.setVisible(false);
        accept.setText(buttonText);
        isConfirm = false;
        start(stitle,smessage,messageType);
    }
    
    public int confirmMessage(String stitle, String smessage, MessageType messageType){
        retval = 0;
        decline.setVisible(true);
        close.setVisible(true);
        isConfirm = true;
        accept.setButtonType(EsonButtonType.PRIMARY);
        decline.setButtonType(EsonButtonType.ERROR);
        start(stitle,smessage,messageType);
        while(isShowing()){}
        return retval;
    }
    
    public int confirmMessage(String stitle, String smessage, MessageType messageType, String acceptText, String declineText){
        retval = 0;
        decline.setText(declineText);
        accept.setText(acceptText);
        decline.setVisible(true);
        close.setVisible(true);
        isConfirm = true;
        accept.setButtonType(EsonButtonType.PRIMARY);
        decline.setButtonType(EsonButtonType.ERROR);
        start(stitle,smessage,messageType);
        while(isShowing()){}
        return retval;
    }
    
    public boolean waitMessage(String stitle, String smessage, MessageType messageType){
        start(stitle,smessage,messageType);
        decline.setVisible(false);
        close.setVisible(false);
        isConfirm = false;
        while(isShowing()){}
        return true;
    }
    
    private Color COLOR = null;
    private void animate(){
        MainPanel.animateModal(COLOR);
    }
    
    private void checkColor(){
        COLOR = Color.BLACK;
        String image_name = "information_h20px.png";
        switch(MESSAGE_TYPE){
            case PLAIN:
                COLOR = Color.BLACK;
                image_name = "message_h25px.png";
                if(!isConfirm){accept.setButtonType(EsonButtonType.PRIMARY);}
                break;
            case INFORMATION_MESSAGE: 
                COLOR = EsonColor.PRIMARY; 
                image_name = "information_h20px.png";
                if(!isConfirm){accept.setButtonType(EsonButtonType.PRIMARY);}
                break;
            case ERROR_MESSAGE: 
                COLOR = EsonColor.ERROR;
                image_name = "error_h20px.png";
                if(!isConfirm){accept.setButtonType(EsonButtonType.ERROR);}
                break;
            case SUCCESS: 
                COLOR = EsonColor.SUCCESS_B;
                image_name = "success_h20px.png";
                if(!isConfirm){accept.setButtonType(EsonButtonType.SUCCESS_B);}
                break;
            case WARNING_MESSAGE: 
                COLOR = EsonColor.WARNING;
                image_name = "warning_h20px.png";
                if(!isConfirm){accept.setButtonType(EsonButtonType.WARNING);}
                break;
        }
        title.setForeground(COLOR);
        title.setIcon(RENDERER.resizeIcon(esonUI.getImageIcon(image_name).getImage(), 20, 20, COLOR));
        accept.setForeground(COLOR);
    }
    
    
    private void keyAction(KeyEvent evt){
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_ENTER -> accept.animateAction();
            case KeyEvent.VK_ESCAPE -> {
                if(decline.isShowing()){
                    decline.animateAction();
                }else{
                    accept.animateAction();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MainPanel = new eson.component.EsonPanel();
        message = new javax.swing.JLabel();
        title = new javax.swing.JLabel();
        accept = new eson.component.EsonButton();
        decline = new eson.component.EsonButton();
        close = new eson.component.EsonClose();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        MainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 18, 16, 16));
        MainPanel.setCornerRadius(10);
        MainPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                MainPanelComponentResized(evt);
            }
        });
        MainPanel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                MainPanelKeyReleased(evt);
            }
        });

        message.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        message.setForeground(new java.awt.Color(30, 30, 30));
        message.setText("Something went terribly wrong.");
        message.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 1));
        message.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                messageKeyReleased(evt);
            }
        });

        title.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        title.setForeground(new java.awt.Color(0, 0, 0));
        title.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project/core/images/error_h20px.png"))); // NOI18N
        title.setText("Process Failed!");
        title.setIconTextGap(5);

        accept.setCornerRadius(5);
        accept.setText("YES");
        accept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptActionPerformed(evt);
            }
        });

        decline.setButtonType(eson.component.EsonButton.EsonButtonType.ERROR);
        decline.setCornerRadius(5);
        decline.setText("NO");
        decline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                declineActionPerformed(evt);
            }
        });

        close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout MainPanelLayout = new javax.swing.GroupLayout(MainPanel);
        MainPanel.setLayout(MainPanelLayout);
        MainPanelLayout.setHorizontalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addComponent(title, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(close, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MainPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(decline, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(accept, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(message, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        MainPanelLayout.setVerticalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(close, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(message, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                .addGap(22, 22, 22)
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(decline, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(accept, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(MainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(8, Short.MAX_VALUE))
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

    int x=0, y=0;
    
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        animate();
    }//GEN-LAST:event_formMouseClicked

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        keyAction(evt);
    }//GEN-LAST:event_formKeyReleased

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        
    }//GEN-LAST:event_formWindowClosed

    private void acceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptActionPerformed
        retval = 1;
        dispose();
    }//GEN-LAST:event_acceptActionPerformed

    private void declineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_declineActionPerformed
        retval = 2;
        dispose();
    }//GEN-LAST:event_declineActionPerformed

    private void closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeActionPerformed
        retval = 0;
        dispose();
    }//GEN-LAST:event_closeActionPerformed

    private void messageKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_messageKeyReleased
        keyAction(evt);
    }//GEN-LAST:event_messageKeyReleased

    private void MainPanelKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MainPanelKeyReleased
        keyAction(evt);
    }//GEN-LAST:event_MainPanelKeyReleased

    private void MainPanelComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_MainPanelComponentResized
        MainPanel.refreshShadow();
    }//GEN-LAST:event_MainPanelComponentResized
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private eson.component.EsonPanel MainPanel;
    private eson.component.EsonButton accept;
    private eson.component.EsonClose close;
    private eson.component.EsonButton decline;
    private javax.swing.JLabel message;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables
}
