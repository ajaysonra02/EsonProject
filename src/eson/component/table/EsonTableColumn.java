/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eson.component.table;

import eson.component.EsonButton;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Codey Hyacinth
 */
public class EsonTableColumn extends JPanel {
    
    private Object OBJECT = new String();
    private int COLUMN_WIDTH = 50, COLUMN_HEIGHT = 35;
    private boolean RESIZABLE = false;
    private Font FONT = null;
    private int HORIZONTAL_ALIGNMENT = javax.swing.SwingConstants.CENTER;
    private Color FOREGROUND = new Color(255,255,255);
    private JLabel label = new JLabel();
    private EsonButton button = new EsonButton();
    private Component COMPONENT = label;
    private EsonTableRow TABLE_ROW = null;
    private EsonTable TABLE = null;
    private int columnIndex = 0;
    private boolean ascending = true;
    
    public EsonTableColumn(EsonTableRow esonRow, int width, int height, boolean isResizable, int index, boolean actionEnabled){
        COLUMN_WIDTH = width;
        COLUMN_HEIGHT = height;
        RESIZABLE = isResizable;
        TABLE_ROW = esonRow;
        TABLE = esonRow.getEsonTable();
        columnIndex = index;
        setOpaque(false);
        validateColumn();
        if(actionEnabled){
            this.addMouseListener(new MouseListener(){
                @Override public void mouseClicked(MouseEvent e) { }
                @Override public void mousePressed(MouseEvent e) { 
                    if(TABLE.isActionEnabled()){
                        setOpaque(true);
                        setBackground(TABLE.getRowSelectionBackground());
                        label.setForeground(TABLE.getRowSelectionForeground());
                        repaint();
                    }
                }
                @Override public void mouseReleased(MouseEvent e) {
                    if(TABLE.isActionEnabled()){
                        setOpaque(false);
                        label.setForeground(TABLE.getHeaderForeground());
                        repaint();
                        TABLE.sort(columnIndex, ascending);
                        ascending = !ascending;
                    }
                }
                @Override public void mouseEntered(MouseEvent e) { }
                @Override public void mouseExited(MouseEvent e) { }
            });
        }
    }
    
    public void setHorizontalAlignment(int alignment){
        HORIZONTAL_ALIGNMENT = TABLE_ROW.isHeader()? JLabel.CENTER:alignment;
        refresh();
    }
    
    public int getHorizontalAlignment(){
        return HORIZONTAL_ALIGNMENT;
    }
    
    public void setColumnFont(Font font){
        FONT = font;
        refresh();
    }
    
    public Font getColumnFont(){
        return FONT;
    }
    
    public void setColumnResizable(boolean flag){
        RESIZABLE = flag;
    }
    
    public boolean isColumnResizable(){
        return RESIZABLE;
    }
    
    public void setColumnWidth(int width){
        COLUMN_WIDTH = width;
        validateColumn();
    }
    
    public int getColumnWidth(){
        return COLUMN_WIDTH;
    }
    
    public void setColumnHeight(int height){
        COLUMN_HEIGHT = height;
        validateColumn();
    }
    
    public int getColumnHeight(){
        return COLUMN_HEIGHT;
    }

    public void setValue(Object obj) {
        OBJECT = obj;
        checkObjectInstance();
    }

    public Object getValue() {
        return OBJECT;
    }
    
    private int MAXIMUM_WIDTH = 0;
    public void setMaximumWidth(int width){
        MAXIMUM_WIDTH = width;
        validateColumn();
    }
    
    public int getMaximumWidth(){
        return MAXIMUM_WIDTH;
    }

    private String VALUE = "";
    private void checkObjectInstance() {
        if (OBJECT instanceof String) {
            setComponentAsLabel(OBJECT.toString());
        } else if (OBJECT instanceof Integer) {
            setComponentAsLabel(OBJECT.toString());
        } else if (OBJECT instanceof Double) {
            setComponentAsLabel(OBJECT.toString());
        } else if (OBJECT instanceof Float) {
            setComponentAsLabel(OBJECT.toString());
        } else if (OBJECT instanceof Long) {
            setComponentAsLabel(OBJECT.toString());
        } else if (OBJECT instanceof Byte) {
            setComponentAsLabel(OBJECT.toString());
        } else if (OBJECT instanceof EsonButton) {
            COMPONENT = (EsonButton)OBJECT;
        }
        refresh();
    }
    
    private void setComponentAsLabel(String value){
        VALUE = value;
        COMPONENT = label;
        COMPONENT.setMinimumSize(getMinimumSize());
        COMPONENT.setPreferredSize(getPreferredSize());
        COMPONENT.setMaximumSize(getMaximumSize());
    }
    
    private void refresh(){
        if(COMPONENT instanceof JLabel){
            JLabel label = (JLabel)COMPONENT;
            label.setText(VALUE);
            label.setHorizontalAlignment(HORIZONTAL_ALIGNMENT); 
            label.setFont(FONT);
            label.setForeground(FOREGROUND);
            label.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 6, 0, 6));
        } 
    }  
    
    /**GROUP LAYOUT**/
    private void validateColumn() {
        removeAll();
        GroupLayout columnLayout = new GroupLayout(this);
        setLayout(columnLayout);
        columnLayout.setHorizontalGroup(
                columnLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(COMPONENT,RESIZABLE?GroupLayout.DEFAULT_SIZE:COLUMN_WIDTH, COLUMN_WIDTH, RESIZABLE?Short.MAX_VALUE:COLUMN_WIDTH)
        );
        columnLayout.setVerticalGroup(
                columnLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(COMPONENT, GroupLayout.DEFAULT_SIZE, COLUMN_HEIGHT, Short.MAX_VALUE)
        );
    }
   
    public void setColumnForeground(Color c) {
        FOREGROUND = c;
        refresh();
    }
}
