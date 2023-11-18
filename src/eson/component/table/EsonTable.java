/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eson.component.table;

import eson.component.EsonSearch;
import eson.core.EsonProject;
import eson.core.util.EsonBlur;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
/**
 *
 * @author WAWA ESON
 */
public class EsonTable extends javax.swing.JPanel {
    
    public EsonTable() {
        initComponents();
        initConfiguration();
    }
    
    private void initConfiguration(){
        if(EsonProject.start()){
            tableHolder.loadDefaultSettings();
        }
    }
    
    private void paintFooter(Graphics g){
        super.paintComponent(g);
        int width = footer.getWidth();
        int height = footer.getHeight();
        Graphics2D g2d = (Graphics2D)g;
        applyQualityRenderingHints(g2d);
        g2d.setColor(FOOTER_BACKGROUND);
        g2d.fillRoundRect(0, 0, width, height, FOOTER_RADIUS,FOOTER_RADIUS);
        g2d.fillRect(0, 0, width, height/2);
    }
    
    private void applyQualityRenderingHints(Graphics2D g2d){
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }
    
    public void setHeaderCornerRadius(int radius){
        HEADER_RADIUS = radius;
        tableHolder.refreshHeader();
    }
    
    public int getHeaderCornerRadius(){
        return HEADER_RADIUS;
    }
    
    public void setFooterCornerRadius(int radius){
        FOOTER_RADIUS = radius;
        footer.repaint();
    }
    
    public int getFooterCornerRadius(){
        return FOOTER_RADIUS;
    }
    
    public void setMaximumVisibleRowCount(int maximumRow){
        VISIBLE_ROW_COUNT = maximumRow;
    }
    
    public int getMaximumVisibleRowCount(){
        return VISIBLE_ROW_COUNT;
    }
    
    public void clearRows(){
        tableHolder.clearRows();
    }
    
    public void clearColumns(){
        tableHolder.clearColumns();
    }
    
    public void addColumn(String name, int size, boolean resizable){
        COLUMNS.add(new Object[]{name,size,resizable,JLabel.LEADING,true});
        tableHolder.initColumnSizes(name,size,resizable);
    }
    
    public void addColumn(String name, int size, boolean resizable, int alignment){
        COLUMNS.add(new Object[]{name,size,resizable,alignment,true});
        tableHolder.initColumnSizes(name,size,resizable);
    }
    
    public void setGridShown(boolean flag){
        GRID_SHOWN = flag;
        tableHolder.refreshHeader();
    }
    
    public boolean isGridShown(){
        return GRID_SHOWN;
    }
    
    public void setGridColor(Color color){
        GRID_COLOR = color;
        tableHolder.refreshHeader();
    }           
    
    public Color getGridColor(){
        return GRID_COLOR;
    }
    
    public void setRowBackgrounds(Color c[]){
        ROW_BACKGROUNDS = c;
        Color a = ROW_BACKGROUNDS[0];
        a = new Color(a.getRed(), a.getGreen(), a.getBlue(), 200);
        esonBlur.setBlurColor(a);
    }
    public Color[] getRowBackgrounds(){ return ROW_BACKGROUNDS; }
    
    public void setRowForegrounds(Color c[]){
        ROW_FOREGROUNDS = c;
        loadingMessage.setForeground(ROW_FOREGROUNDS[0]);
        loading.setForeground(ROW_FOREGROUNDS[0]);
    }
    public Color[] getRowForegrounds(){
        return ROW_FOREGROUNDS;
    }
    
    public void setRowHoverBackground(Color c){
        ROW_HOVER_BACKGROUND = c;
    }
    
    public Color getRowHoverBackground(){
        return ROW_HOVER_BACKGROUND; 
    }
    
    public void setRowHoverForeground(Color c){
        ROW_HOVER_FOREGROUND = c;
    }
    public Color getRowHoverForeground(){ 
        return ROW_HOVER_FOREGROUND; 
    }
    
    public void setRowSelectionBackground(Color c){
        ROW_SELECTION_BACKGROUND = c;
    }
    
    public Color getRowSelectionBackground(){ 
        return ROW_SELECTION_BACKGROUND; 
    }
    
    public void setRowSelectionForeground(Color c){
        ROW_SELECTION_FOREGROUND = c;
    }
    
    public Color getRowSelectionForeground(){ 
        return ROW_SELECTION_FOREGROUND; 
    }
    
    public boolean isVerticalScrollShown(){
        return VERTICAL_SCROLL_SHOWN;
    }
    
    public void setActionEnabled(boolean flag){
        actionEnabled = flag;
    }
    
    public boolean isActionEnabled(){
        return actionEnabled;
    }
    
    public void addRow(Object[] values){
        VALUES.add(values);
        tableHolder.insertRow(values);
        showLoading("LOADING TABLE ROWS");
    }
    
    public void scrollToTop(){
        scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMinimum());
    }
    
    public void scrollToBottom(){
        scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
    }
    
    public void setRowCornerRadius(int radius){
        ROW_CORNER_RADIUS = radius;
    }
    
    public int getRowCornerRadius(){
        return ROW_CORNER_RADIUS;
    }

    public void refreshViewPort(){
        viewport.revalidate();
    }
    
    public int getRowCount(){
        return ROWS.size();
    }
    
    public void setRowHeight(int height){
        ROW_HEIGHT = height;
        scroll.getVerticalScrollBar().setUnitIncrement(height);
    }
    
    public int getRowHeight(){
        return ROW_HEIGHT;
    }
    
    public void setHeaderHeight(int height){
        HEADER_HEIGHT = height;
        tableHolder.updateHeaderHeight();
    }
    
    public int getHeaderHeight(){
        return HEADER_HEIGHT;
    }
    
    public void setFooterHeight(int height){
        FOOTER_HEIGHT = height;
        tableHolder.updateFooterHeight();
    }
    
    public JScrollPane getScrollPane(){
        return scroll;
    }
    
    public int getFooterHeight(){
        return FOOTER_HEIGHT;
    }
    
    public void setHeaderBackground(Color color){
        HEADER_BACKGROUND = color;
        HEADER.setNormalBackground(color);
    }
    
    public Color getHeaderBackground(){
        return HEADER_BACKGROUND;
    }
    
    public void setHeaderForeground(Color color){
        HEADER_FOREGROUND = color;
        HEADER.setNormalForeground(color);
    }
    
    public Color getHeaderForeground(){
        return HEADER_FOREGROUND;
    }
    
    public void setFooterBackground(Color color){
        FOOTER_BACKGROUND = color;
        footer.repaint();
    }
    
    public Color getFooterBackground(){
        return FOOTER_BACKGROUND;
    }
    
    public void setFooterForeground(Color color){
        FOOTER_FOREGROUND = color;
    }
    
    public Color getFooterForeground(){
        return FOOTER_FOREGROUND;
    }
    
    public void setHeaderFont(Font font){
        HEADER_FONT = font;
        tableHolder.refreshHeader();
    }
    
    public void setFooterFont(Font font){
        FOOTER_FONT = font;
        tableHolder.refreshFooter();
    }
    
    public Font getFooterFont(){
        return FOOTER_FONT;
    }
    
    public Font getHeaderFont(){
        return HEADER_FONT;
    }
    
    public void setRowFont(Font font){
        ROW_FONT = font;
    }
    
    public Font getRowFont(){
        return ROW_FONT;
    }

    public void resetSelection(){
        for (EsonTableRow t : ROWS) {
            t.reset();
        }
    }
    
    public Object getValueAt(int index, int column){
        return ROWS.get(index).getColumn(column).getValue();
    }
    
    public void setValueAt(Object object, int index, int column){
        ROWS.get(index).getColumn(column).setValue(object);
    }

    public void removeRow(int index){
        tableHolder.removeRow(index);
    }
    
    public int getSelectedRow(){
        return tableHolder.getSelectedRow();
    }
    
    public boolean hasSelectedRow(){
        return tableHolder.hasSelectedRow();
    }
    
    public void setBodyColor(Color c){
        BODY_COLOR = c;
        tableHolder.updateBodyColor();
    }
    
    public Color getBodyColor(){
        return BODY_COLOR;
    }
  
    public Container getViewPort(){
        return viewport;
    }
    
    public void setScrollBackground(Color color){
        SCROLL_BACKGROUND = color;
    }
    
    public Color getScrollBackground(){
        return SCROLL_BACKGROUND;
    }
    
    public void setScrollForeground(Color color){
        SCROLL_FOREGROUND = color;
        tableHolder.updateLoadingColor();
    }
    
    public Color getScrollForeground(){
        return SCROLL_FOREGROUND;
    }
    
    public void setScrollMaximumSize(int size){
        SCROLL_MAX_SIZE = size;
    }
    
    public int getScrollMaximumSize(){
        return SCROLL_MAX_SIZE;
    }
    
    public void setScrollMinimumSize(int size){
        SCROLL_MIN_SIZE = size;
    }
    
    public int getScrollMinimumSize(){
        return SCROLL_MIN_SIZE;
    }  
    
    public void showColumn(int indexes[]){
        tableHolder.showColumn(indexes);
    }
    
    public void showColumn(String columns[]){
        tableHolder.showColumn(columns);
    }
    
    public EsonTableFooter getFooter(){
        return FOOTER;
    }
    
    public EsonTableRow getHeader(){
        return HEADER;
    }
    
    public List<Object[]> getValues(){
        return VALUES;
    }
    
    public void refresh(){
        tableHolder.search("");
    }
    
    public void prepareRowValues(Object[] obj){
        VALUES.add(obj);
    }
    
    public void sort(int columnIndex, boolean ascending){
        tableHolder.sort(columnIndex, ascending);
    }
    
    public EsonSearch getEsonSearch(){
        return esonSearch;
    }
    
    public void setLoadingText(String text){
        loadingMessage.setText(text);
    }
    
    public void showLoading(String message){
        tableHolder.showLoading(message);
    }
    
    public void closeLoading(){
        tableHolder.closeLoading();
    }
    
    /**
     * @return the bodyBlurShown
     */
    public boolean isBodyBlurShown() {
        return bodyBlurShown;
    }

    /**
     * @param bodyBlurShown the bodyBlurShown to set
     */
    public void setBodyBlurShown(boolean bodyBlurShown) {
        this.bodyBlurShown = bodyBlurShown;
        esonBlur.blurComponent(bodyPane, bodyBlurShown);
    }
    
    public void setEsonSearch(EsonSearch search){
        esonSearch = search;
    }
    
    public void search(String KEY){
        tableHolder.search(KEY);
    }
    
    public void viewPage(List<Object[]> values,int pageNumber){
        tableHolder.viewPage(values, pageNumber);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        esonProjectCover = new javax.swing.JPanel();
        loadingMessage = new javax.swing.JLabel();
        loading = new javax.swing.JLabel();
        loadingIcon = new javax.swing.JLabel();
        header = new javax.swing.JPanel();
        footer = new javax.swing.JPanel(){

            @Override
            public void paintComponent(Graphics g){
                if(EsonProject.start()){
                    paintFooter(g);
                }
            }

        };
        bodyPane = new javax.swing.JPanel();
        scroll = new javax.swing.JScrollPane();
        viewport = new javax.swing.JPanel();

        esonProjectCover.setOpaque(false);

        loadingMessage.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        loadingMessage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        loadingMessage.setText("SORTING TABLE ROWS");

        loading.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        loading.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        loadingIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        loadingIcon.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        loadingIcon.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 5, 1));

        javax.swing.GroupLayout esonProjectCoverLayout = new javax.swing.GroupLayout(esonProjectCover);
        esonProjectCover.setLayout(esonProjectCoverLayout);
        esonProjectCoverLayout.setHorizontalGroup(
            esonProjectCoverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(loading, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
            .addComponent(loadingMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(loadingIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        esonProjectCoverLayout.setVerticalGroup(
            esonProjectCoverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(esonProjectCoverLayout.createSequentialGroup()
                .addContainerGap(82, Short.MAX_VALUE)
                .addComponent(loadingIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(loadingMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(loading, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(82, Short.MAX_VALUE))
        );

        setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

        header.setOpaque(false);

        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        footer.setOpaque(false);

        javax.swing.GroupLayout footerLayout = new javax.swing.GroupLayout(footer);
        footer.setLayout(footerLayout);
        footerLayout.setHorizontalGroup(
            footerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        footerLayout.setVerticalGroup(
            footerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        bodyPane.setOpaque(false);

        scroll.setBorder(null);

        javax.swing.GroupLayout viewportLayout = new javax.swing.GroupLayout(viewport);
        viewport.setLayout(viewportLayout);
        viewportLayout.setHorizontalGroup(
            viewportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 831, Short.MAX_VALUE)
        );
        viewportLayout.setVerticalGroup(
            viewportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 590, Short.MAX_VALUE)
        );

        scroll.setViewportView(viewport);

        javax.swing.GroupLayout bodyPaneLayout = new javax.swing.GroupLayout(bodyPane);
        bodyPane.setLayout(bodyPaneLayout);
        bodyPaneLayout.setHorizontalGroup(
            bodyPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodyPaneLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(scroll)
                .addGap(0, 0, 0))
        );
        bodyPaneLayout.setVerticalGroup(
            bodyPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodyPaneLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(scroll)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(header, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(footer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(bodyPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(bodyPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(footer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JPanel bodyPane;
    protected javax.swing.JPanel esonProjectCover;
    protected javax.swing.JPanel footer;
    protected javax.swing.JPanel header;
    protected javax.swing.JLabel loading;
    protected javax.swing.JLabel loadingIcon;
    protected javax.swing.JLabel loadingMessage;
    protected javax.swing.JScrollPane scroll;
    protected javax.swing.JPanel viewport;
    // End of variables declaration//GEN-END:variables
    protected int HEADER_HEIGHT = 25,
            FOOTER_HEIGHT = 25,
            ROW_HEIGHT = 25,
            HEADER_RADIUS = 25,
            FOOTER_RADIUS = 25,
            ROW_COUNTER = 0,
            VISIBLE_ROW_COUNT = 25,
            ROW_CORNER_RADIUS = 5,
            SCROLL_MAX_SIZE = 15, 
            SCROLL_MIN_SIZE = 2;
    protected Color HEADER_BACKGROUND = null,
            HEADER_FOREGROUND = null,
            FOOTER_BACKGROUND = null,
            FOOTER_FOREGROUND = null,
            ROW_HOVER_BACKGROUND = null,
            ROW_HOVER_FOREGROUND = null,
            ROW_SELECTION_BACKGROUND = null,
            ROW_SELECTION_FOREGROUND = null,
            ROW_BACKGROUNDS[] = null,
            ROW_FOREGROUNDS[] = null,
            SCROLL_BACKGROUND = null,
            SCROLL_FOREGROUND = null,
            BODY_COLOR = null,
            GRID_COLOR = Color.GRAY;
    protected Font HEADER_FONT = null,
            FOOTER_FONT = null,
            ROW_FONT = null;
    protected boolean VERTICAL_SCROLL_SHOWN = false,
            HEADER_MIN_UPDATED = false,
            bodyBlurShown = false,
            GRID_SHOWN = false,
            actionEnabled = true;
    protected List<Object[]> VALUES = new ArrayList();
    protected List<Object[]> CURRENT_VALUES = new ArrayList();
    protected final List<EsonTableRow> ROWS = new ArrayList();
    protected final List<String> HEADER_COLUMNS = new ArrayList();
    protected final List<Integer> COLUMN_WIDTHS = new ArrayList();
    protected final List<Integer> COLUMN_ALIGNMENT = new ArrayList();
    protected final List<Boolean> RESIZABLE_COLUMNS = new ArrayList();
    protected final List<Object[]> COLUMNS = new ArrayList();
    protected final EsonTableHolder tableHolder = new EsonTableHolder(this);
    protected final EsonBlur esonBlur = new EsonBlur(5,1f);
    protected EsonSearch esonSearch = null;
    protected EsonTableRow HEADER = null;
    protected EsonTableFooter FOOTER = null;
}
