/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eson.component.table;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;
import eson.core.EsonProject;
import eson.core.util.GaussianFilter;

/**
 *
 * @author Codey Hyacinth
 */
public class EsonTableRow extends JPanel {
    
    protected EsonTableColumn ESON_COLUMNS[] = null;
    protected Color H_FOREGROUND = null,
            H_BACKGROUND = null,
            S_BACKGROUND = null,
            S_FOREGROUND = null,
            N_FOREGROUND = null,
            N_BACKGROUND = null;
    protected boolean isSelected = false;
    protected int ROW_HEIGHT = 2;
    protected List<Integer> COLUMN_WIDTHS = new ArrayList<>();
    protected List<Boolean> RESIZABLE_COLUMNS = new ArrayList<>();
    protected List<Boolean> VISIBLE_COLUMNS = new ArrayList<>();
    protected List<Integer> COLUMN_ALIGNMENT = new ArrayList<>();
    protected boolean isHeader = false;
    protected EsonTable TABLE = null;
    protected Font FONT = null;
    protected EsonTableColumn MARGIN = null;
    
    public EsonTableRow(EsonTable esonTable, List<Object[]> esonColumn, int rowHeight, boolean isHeader){
        TABLE = esonTable;
        this.isHeader = isHeader;
        ROW_HEIGHT = rowHeight;
        
        ESON_COLUMNS = new EsonTableColumn[esonColumn.size()];
        boolean hasResizableColumn = false;
        for(Object[] obj:esonColumn){
            if(!hasResizableColumn && (boolean)obj[2] && (boolean)obj[4]){
                hasResizableColumn = true;
            }
            COLUMN_WIDTHS.add((int)obj[1]);
            RESIZABLE_COLUMNS.add((boolean)obj[2]);
            COLUMN_ALIGNMENT.add((int)obj[3]);
            VISIBLE_COLUMNS.add((boolean)obj[4]);
        }
        if(!hasResizableColumn){
            RESIZABLE_COLUMNS.replaceAll(e -> true);
        }
        setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
        initColumns();
    }
    
    public EsonTable getEsonTable(){
        return TABLE;
    }
    
    public void setRowFont(Font font){
        FONT = font;
        if(ESON_COLUMNS!=null){
            for(EsonTableColumn column:ESON_COLUMNS){
                column.setColumnFont(font);
            }
        }
    }
    
    public Font getRowFont(){
        return FONT;
    }
    
    public void setCornerRadius(int radius){
        radius = radius>25?25:radius;
        CORNER_RADIUS = radius;
        repaint();
    }
    
    public int getCornerRadius(){
        return CORNER_RADIUS;
    }
    
    private void initRowListeners(){
        addMouseListener(new MouseListener(){
            @Override public void mouseClicked(MouseEvent e) { }
            @Override public void mousePressed(MouseEvent e) { }
            @Override public void mouseReleased(MouseEvent e) { rowAction(e); }
            @Override public void mouseEntered(MouseEvent e) { hoverAction(); }
            @Override public void mouseExited(MouseEvent e) {exitedAction(); }
        });
    }
    
    private void rowAction(MouseEvent e){
        if (e.getButton() == 1 && TABLE.isActionEnabled()) {
            if (!isSelected) {
                TABLE.resetSelection();
                isSelected = true;
                changeColor(S_BACKGROUND, S_FOREGROUND);
            } else {
                isSelected = false;
                changeColor(N_BACKGROUND, N_FOREGROUND);
            }
        }
    }
    
    private void hoverAction(){
        if (!isSelected && TABLE.isActionEnabled()) {
            changeColor(H_BACKGROUND, H_FOREGROUND);
        }
    }
    
    private void exitedAction() {
        if(TABLE.isActionEnabled()){
            if (isSelected) {
                changeColor(S_BACKGROUND, S_FOREGROUND);
            } else {
                /*Fade Animation Under Construction*/
                //EventQueue.invokeLater(EXIT_ANIMATION);
                changeColor(N_BACKGROUND, N_FOREGROUND);
            }
        }
    }

    private int CORNER_RADIUS = 10;
    protected GaussianFilter FILTER = new GaussianFilter();
    private Color BACKGROUND = new Color(230,230,230);
    
    @Override
    public void paintComponent(Graphics g){
        if(EsonProject.start()){
            if(isHeader){
                paintHeader(g);
            }else{
                paintRounded(g); 
            }
        }
    }
    
    private void paintHeader(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        applyQualityRenderingHints(g2d);
        g2d.setColor(BACKGROUND);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS,CORNER_RADIUS);
        g2d.fillRect(0, getHeight()/2+2, getWidth(), getHeight()/2);
    }
    
    private void paintRounded(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        applyQualityRenderingHints(g2d);
        g2d.setColor(BACKGROUND);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(),CORNER_RADIUS,CORNER_RADIUS);
    }
    
    private void applyQualityRenderingHints(Graphics2D g2d){
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }
    
    private Color GRID_COLOR = Color.GRAY;
    public void setGridColor(Color color){
        GRID_COLOR = color;
        refreshColumns();
    }
    
    public Color getGridColor(){
        return GRID_COLOR;
    }
    
    private boolean showVerticalGrid = false;
    public void setVerticalGridShown(boolean flag){
        showVerticalGrid = flag;
        refreshColumns();
    }
    
    public boolean isVerticalGridShown(){
        return showVerticalGrid;
    }
    
    private boolean showHorizontalGrid = false;
    public void setHorizontalGridShown(boolean flag){
        showHorizontalGrid = flag;
        refreshColumns();
    }
    
    public boolean isHorizontalGridShown(){
        return showVerticalGrid;
    }

    public void setValues(Object obj[]) {
        int i = 0;
        for (EsonTableColumn column : ESON_COLUMNS) {
            column.setValue(obj[i]);
            i++; 
        }
    }
    
    public void setValues(List<String> ob){
        int i = 0;
        for(EsonTableColumn column : ESON_COLUMNS){
            column.setValue(ob.get(i));
            i++;
        }
        MARGIN.setValue("");
    }
    
    public int getColumnCount(){
        return ESON_COLUMNS.length;
    }

    public EsonTableColumn getColumn(int index) {
        return ESON_COLUMNS[index];
    }

    private void initColumns() {
        setOpaque(false);
        H_FOREGROUND = Color.white;
        H_BACKGROUND = Color.LIGHT_GRAY;
        N_FOREGROUND = Color.white;
        N_BACKGROUND = Color.BLACK;
        S_FOREGROUND = Color.black;
        S_BACKGROUND = Color.yellow;
        FONT = new Font("Arial",1,30);
        for (int i = 0; i <ESON_COLUMNS.length; i++) {
           ESON_COLUMNS[i] = new EsonTableColumn(this,COLUMN_WIDTHS.get(i),ROW_HEIGHT,RESIZABLE_COLUMNS.get(i),i,isHeader);
        }
        MARGIN = new EsonTableColumn(this, 0,ROW_HEIGHT,false,ESON_COLUMNS.length,false);
        refreshColumns();
    }
    
    public boolean isHeader(){
        return isHeader;
    }
    
    public void setMarginSize(int size){
        MARGIN.setColumnWidth(size);
    }
    
    public int getResizableColumnCount(){
        int retval = 0;
        for(boolean res:RESIZABLE_COLUMNS){
            if(res){ retval++; }
        }
        return retval;
    }
    
    public int getTotalWidthofNonResizableColumns(){
        int retval = 0, i = 0;
        for(boolean res:RESIZABLE_COLUMNS){
            if(res){ retval+=COLUMN_WIDTHS.get(i); }
            i++;
        }
        return retval;
    }
    
    /**GROUP LAYOUT**/
    private void refreshColumns() {
        removeAll();
        javax.swing.GroupLayout LAYOUT = new javax.swing.GroupLayout(this);
        setLayout(LAYOUT);
        GroupLayout.SequentialGroup hsg = LAYOUT.createSequentialGroup();   
        GroupLayout.ParallelGroup vpg = LAYOUT.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING);
        int i=0,bottomBorder = 0;
        if(showHorizontalGrid){ bottomBorder = isHeader?0:1; }
        int rightBorder = showVerticalGrid?1:0;
        EsonTableColumn lastColumn = null; 
        for(EsonTableColumn column:ESON_COLUMNS){
            if(VISIBLE_COLUMNS.get(i)){
                column.setColumnFont(FONT);
                column.setHorizontalAlignment(COLUMN_ALIGNMENT.get(i));
                column.setBorder(new MatteBorder(0,0,bottomBorder,rightBorder,GRID_COLOR));
                lastColumn = column;
                if(!column.isColumnResizable()){
                    hsg.addComponent(column, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE);
                }else{
                    hsg.addComponent(column, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
                }
                hsg.addGap(0, 0, 0);
                vpg.addComponent(column, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE);
            }i++;
        }
        if(lastColumn!=null){lastColumn.setBorder(null);}
        //INSERT MARGIN IN LAST COLUMN
        if(isHeader){
            hsg.addComponent(MARGIN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE);
            vpg.addComponent(MARGIN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE);
        }else{ initRowListeners();}
        LAYOUT.setHorizontalGroup(LAYOUT.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(hsg));
        LAYOUT.setVerticalGroup(vpg);
    }

    private void changeColor(Color bg, Color fg) {
        for (EsonTableColumn column : ESON_COLUMNS) {
            column.setColumnForeground(fg);
        }
        BACKGROUND=bg;
        repaint();
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void reset() {
        isSelected = false;         changeColor(N_BACKGROUND, N_FOREGROUND);
    }

    public void setHoverBackground(Color c) {
        H_BACKGROUND = c;
        reset();
    }

    public void setHoverForeground(Color c) {
        H_FOREGROUND = c;
        reset();
    }

    public void setSelectedBackground(Color c) {
        S_BACKGROUND = c;
        reset();
    }
    
    public Color getSelectedBackground(){
        return S_BACKGROUND;
    }

    public void setSelectedForeground(Color c) {
        S_FOREGROUND = c;
        reset();
    }
    
    public Color getSelectedForeground(){
        return S_FOREGROUND;
    }

    public void setNormalBackground(Color c) {
        N_BACKGROUND = c;
        reset();
    }
    
    public Color getNormalBackground(){
        return N_BACKGROUND;
    }

    public void setNormalForeground(Color c) {
        N_FOREGROUND = c;
        reset();
    }
    
    public Color getNormalForeground(){
        return N_FOREGROUND;
    }
    
    public boolean valueContains(String value){
        boolean retval = false;
        for (EsonTableColumn column : ESON_COLUMNS) {
            String v = column.getValue().toString().toLowerCase().trim();
            retval = v.contains(value.toLowerCase().trim());
            if(retval){ break; }
        }
        return retval;
    }
    
}
