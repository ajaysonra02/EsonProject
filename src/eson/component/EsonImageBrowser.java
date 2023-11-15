/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eson.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileView;
import javax.swing.plaf.basic.BasicComboBoxUI;
import eson.core.EsonColor;
import eson.core.EsonUI;

/**
 *
 * @author ASUS-B150M
 */
public class EsonImageBrowser extends javax.swing.JDialog {

    /**
     * Creates new form EsonFileBrowser
     * @param parent
     * @param modal
     */
    Color background = new Color(20,20,20);
    Image returnImage = null;
    private String imageLocation = "";
    int x = 0, y = 0;
    private int ICON_SIZE = 16;
    private Image LOADING_IMAGE = new BufferedImage(ICON_SIZE,ICON_SIZE,BufferedImage.TYPE_INT_ARGB);
    private final Pattern imageFilePattern = Pattern.compile(".+?\\.(png|jpe?g|gif|tiff?)$",Pattern.CASE_INSENSITIVE);
    private final Map imageCache = new WeakHashMap();
    private EsonUI esonUI = null;
    
    public EsonImageBrowser(java.awt.Frame parent) {
        super(parent, true);
        esonUI = new EsonUI();
        initComponents();
        customizeUI(choose.getComponents());
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        esonUI.setWindowOpaque(this, false);
        setBackground(new Color(100,100,100,75));
        choose.setFileFilter(new FileNameExtensionFilter("Image files",ImageIO.getReaderFileSuffixes()));
        //initView();
    }
    
    private void initView(){
        choose.setFileView(new FileView() {
            
            private final ExecutorService executor = Executors.newCachedThreadPool();
            
            @Override
            public Icon getIcon(File file){
                if(!imageFilePattern.matcher(file.getName()).matches()){
                    return null;
                }
                synchronized(imageCache){
                    ImageIcon icon = (ImageIcon) imageCache.get(file);
                    if(icon==null){
                        icon = new ImageIcon(LOADING_IMAGE);
                        imageCache.put(file, icon);
                        executor.submit(new ThumbnailIconLoader(icon,file));
                    }
                    return icon;
                }
            }
            
            class ThumbnailIconLoader implements Runnable{
                
                private final ImageIcon icon;
                private final File file;
                
                public ThumbnailIconLoader(ImageIcon i, File f){
                    icon = i;
                    file = f;
                }
                
                @Override public void run(){
                    SwingWorker worker = new SwingWorker(){
                        @Override
                        protected Object doInBackground() throws Exception {
                            ImageIcon newIcon = new ImageIcon(file.getAbsolutePath());
                            Image img = newIcon.getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
                            icon.setImage(img);
                    
                            SwingUtilities.invokeLater(() -> {
                                repaint();
                            });
                            return null;
                        }
                    };
                    worker.execute();
                }
                
            }
            
        });
    }
    
    public Image getSelectedImage(){
        setVisible(true);
        while(isShowing()){}
        return returnImage;
    }
    
    public String getSelectedImageLocation(){
        setVisible(true);
        while(isShowing()){}
        return imageLocation;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        MainPanel = new javax.swing.JPanel();
        title = new javax.swing.JLabel();
        close = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        choose = new javax.swing.JFileChooser();
        preview = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        location = new javax.swing.JTextArea();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(581, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(376, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        MainPanel.setBackground(new java.awt.Color(0, 0, 0));
        MainPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(10, 10, 10)));

        title.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        title.setForeground(new java.awt.Color(255, 255, 255));
        title.setText("   Image Browser");
        title.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                titleMouseDragged(evt);
            }
        });
        title.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                titleMousePressed(evt);
            }
        });

        close.setFont(new java.awt.Font("SansSerif", 0, 20)); // NOI18N
        close.setForeground(new java.awt.Color(230, 230, 230));
        close.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        close.setText("x");
        close.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                closeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                closeMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                closeMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                closeMouseReleased(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(20, 20, 20));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 2, 4, 2, new java.awt.Color(0, 0, 0)));

        choose.setApproveButtonText("Select");
        choose.setSelectedFiles(null);
        choose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseActionPerformed(evt);
            }
        });
        choose.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                choosePropertyChange(evt);
            }
        });

        preview.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        preview.setForeground(new java.awt.Color(150, 150, 150));
        preview.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        preview.setText("Image Preview");
        preview.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(60, 60, 60)));

        jScrollPane2.setBorder(null);
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        location.setBackground(new java.awt.Color(20, 20, 20));
        location.setColumns(20);
        location.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        location.setForeground(new java.awt.Color(255, 255, 255));
        location.setLineWrap(true);
        location.setRows(5);
        location.setWrapStyleWord(true);
        location.setBorder(null);
        jScrollPane2.setViewportView(location);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(preview, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addComponent(choose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(choose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(preview, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addGap(2, 2, 2))
        );

        javax.swing.GroupLayout MainPanelLayout = new javax.swing.GroupLayout(MainPanel);
        MainPanel.setLayout(MainPanelLayout);
        MainPanelLayout.setHorizontalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addComponent(title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(close, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        MainPanelLayout.setVerticalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(close, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(MainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(MainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeMouseEntered
        // TODO add your handling code here:
        esonUI.closeEntered(close);
    }//GEN-LAST:event_closeMouseEntered

    private void closeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeMouseExited
        // TODO add your handling code here:
        esonUI.closeExited(close);
    }//GEN-LAST:event_closeMouseExited

    private void closeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeMousePressed
        // TODO add your handling code here:
        esonUI.closePressed(close);
    }//GEN-LAST:event_closeMousePressed

    private void closeMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeMouseReleased
        // TODO add your handling code here:
        closeMouseExited(evt);
        dispose();
    }//GEN-LAST:event_closeMouseReleased

    private void choosePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_choosePropertyChange
        // TODO add your handling code here:
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                preview.setIcon(null);
                preview.setText("Image Preview");
                location.setText("");
               if (choose.getSelectedFile() != null && choose.getSelectedFile().isFile()) {
                    File file = choose.getSelectedFile();
                    String fname = file.getName().replace(" ", "");
                    String st[] = fname.split("\\.");
                    String txt = st[st.length-1].toLowerCase();
                    if (txt.equals("jpg") || txt.equals("jpeg") || txt.equals("png") || txt.equals("gif")) {
                        location.setText("LOCATION: " + file.getAbsolutePath());
                        preview.setText("");
                        Image img = new ImageIcon(file.getAbsolutePath()).getImage();
                        scale_and_fit_icons(preview, img);
                    }
                }
            return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_choosePropertyChange

    private void chooseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chooseActionPerformed
        // TODO add your handling code here:
        if(evt.getActionCommand().equals("ApproveSelection")){
            if(preview.getIcon()!=null){
                imageLocation = choose.getSelectedFile().getAbsolutePath();
                returnImage = new ImageIcon(imageLocation).getImage();
                dispose();
            }
        }else if(evt.getActionCommand().equals("CancelSelection")){
            dispose();
        }
    }//GEN-LAST:event_chooseActionPerformed

    private void titleMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_titleMousePressed
        // TODO add your handling code here:
        x = evt.getX();
        y = evt.getY();
    }//GEN-LAST:event_titleMousePressed

    private void titleMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_titleMouseDragged
        // TODO add your handling code here:
        MainPanel.setLocation(MainPanel.getX() + evt.getX() - x,
                MainPanel.getY() + evt.getY() - y);
    }//GEN-LAST:event_titleMouseDragged

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        // TODO add your handling code here:
        modal();
    }//GEN-LAST:event_formComponentShown

    private void modal(){
         EventQueue.invokeLater(new Runnable() {
            int current = 0;
            Color border_color = new Color(10,10,10);
            @Override
            public void run() {
                javax.swing.Timer ti = new javax.swing.Timer(100, (ActionEvent e) -> {
                    if((current%2)==0){
                        MainPanel.setBorder(BorderFactory.createMatteBorder(1,1,1,1, border_color));
                    }else{
                        if(current<4){
                            MainPanel.setBorder(BorderFactory.createMatteBorder(5,5,5,5, border_color));
                        }else{
                            MainPanel.setBorder(BorderFactory.createMatteBorder(1,1,1,1, border_color));
                            ((Timer)e.getSource()).stop();
                        }
                    }
                    repaintFrame();
                    current++;
                });
                ti.setRepeats(true);
                ti.setCoalesce(true);
                ti.start();
            }
        });
        Toolkit.getDefaultToolkit().beep();
    }
    
    private void repaintFrame(){
        repaint();
    }
    
    private void scale_and_fit_icons(JLabel label, Image img) {
        Image newimg = img.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH); // scale it the smooth way  
        label.setIcon(new ImageIcon(newimg));
    }
    
    public void customizeUI(Component[] c){
        for(Component cc: c){
            if(cc instanceof JPanel){
                JPanel pane = (JPanel)cc;
                pane.setBackground(background);
                if(pane.getComponentCount()!=0){
                    customizeUI(pane.getComponents());
                }
            }
            if(cc instanceof JTextField){
                JTextField field = (JTextField)cc;
                field.setBackground(new Color(30,30,30));
                field.setForeground(new Color(230,230,230));
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(1,1,1,1,Color.GRAY), 
                        BorderFactory.createMatteBorder(4,5,3,5, new Color(30,30,30))
                ));
                field.setCaretColor(field.getForeground());
                field.setSelectionColor(new Color(150,150,150));
                field.setSelectedTextColor(Color.black);
            }
            if(cc instanceof JButton){
                JButton button = (JButton)cc;
                button.setContentAreaFilled(false);
                button.setOpaque(true);
                button.setFocusable(false);
                button.setForeground(Color.black);
                if(button.getIcon()==null){
                    button.setBackground(new Color(150,150,150));
                    button.setBorder(BorderFactory.createEmptyBorder(5,35,5,35));
                }else{
                    button.setBackground(background.brighter().brighter());
                    button.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
                }
                button.repaint();
                button.revalidate();
                button.getParent().revalidate();
                buttonMouseListener(button);
            }
            if(cc instanceof JComboBox){
                JComboBox combo = (JComboBox)cc;
                UIManager.put("ComboBox.selectionBackground", new Color(150,150,150));
                UIManager.put("ComboBox.selectionForeground", Color.black);
                combo.setBackground(new Color(60,60,60));
                combo.setForeground(Color.WHITE);
                combo.updateUI();
                Color comboBackground = new Color(30,30,30);
                combo.setUI(new BasicComboBoxUI(){
                    @Override public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus){
                        Color t = g.getColor();
                        g.setColor(comboBackground);
                        g.fillRect(bounds.x,bounds.y, bounds.width, bounds.height);
                        g.setColor(t);
                    }
                    
                });
                combo.setFocusable(false);
                for(Component co:combo.getComponents()){
                    if(co instanceof JButton){
                        JButton btn = (JButton)co;
                        btn.setContentAreaFilled(false);
                        btn.setBorder(BorderFactory.createMatteBorder(1,1,1,1,comboBackground));
                        btn.setBackground(comboBackground);
                    }
                }
                combo.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(1,1,1,1,Color.GRAY), 
                        BorderFactory.createMatteBorder(2,5,2,5, new Color(30,30,30))
                    ));
                if(combo.getItemCount()>1){
                combo.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(1,1,1,1,Color.GRAY), 
                        BorderFactory.createMatteBorder(0,5,0,0, new Color(30,30,30))
                    ));
                }
            }
            if(cc instanceof JLabel){
                JLabel label = (JLabel)cc;
                label.setForeground(Color.white);
            }
            if(cc instanceof JToggleButton){
                JToggleButton toggle = (JToggleButton)cc;
                toggle.setOpaque(true);
                toggle.setBorder(null);
                toggle.setFocusable(false);
                toggle.getParent().getParent().setBackground(Color.red);
                toggle.setBackground(background.brighter().brighter());
                toggle.setForeground(Color.white);
            }
            if(cc instanceof JScrollPane){
                ((JScrollPane)cc).setBackground(background);
            }
            if(cc instanceof JList){
                ((JList)cc).setBackground(background);
            }
            if(cc instanceof JToolBar){
                JToolBar toolbar = (JToolBar)cc;
                toolbar.setBackground(background.brighter().brighter());
                toolbar.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(1, 1, 1, 1, toolbar.getBackground().brighter()),
                        BorderFactory.createMatteBorder(4, 0, 4, 0, toolbar.getBackground())
                ));
                if(toolbar.getComponentCount()!=0){
                    customizeUI(toolbar.getComponents());
                }
            }
        }
    }

    private void buttonMouseListener(JButton button){
        button.addMouseListener(new MouseListener() {
            @Override public void mouseClicked(MouseEvent e){}
            @Override public void mouseReleased(MouseEvent e){
                if(button.getText()!=null){
                    if(button.getText().toLowerCase().equals("select") || button.getText().toLowerCase().equals("cancel")){
                        button.setBackground(new Color(150,150,150));
                        button.setForeground(Color.BLACK);
                    }
                }
            }
            @Override public void mousePressed(MouseEvent e){
                if(button.getText()!=null){
                    if(button.getText().toLowerCase().equals("select")){
                        button.setForeground(EsonColor.SUCCESS);
                        button.setBackground(Color.BLACK);
                    }else if(button.getText().toLowerCase().equals("cancel")){
                        button.setForeground(EsonColor.ERROR);
                        button.setBackground(Color.BLACK);
                    }
                }
            }
            @Override public void mouseEntered(MouseEvent e){
                if(button.getText()!=null){
                    button.setForeground(Color.WHITE);
                    if(button.getText().toLowerCase().equals("select")){
                        button.setBackground(EsonColor.SUCCESS);
                    }else if(button.getText().toLowerCase().equals("cancel")){
                        button.setBackground(EsonColor.ERROR);
                    }
                }
            }
            @Override public void mouseExited(MouseEvent e){
                if(button.getText()!=null){
                    button.setForeground(Color.BLACK);
                    if(button.getText().toLowerCase().equals("select") || button.getText().toLowerCase().equals("cancel")){
                        button.setBackground(new Color(150,150,150));
                        button.setForeground(Color.BLACK);
                    }
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel MainPanel;
    private javax.swing.JFileChooser choose;
    private javax.swing.JLabel close;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea location;
    private javax.swing.JLabel preview;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables
}
