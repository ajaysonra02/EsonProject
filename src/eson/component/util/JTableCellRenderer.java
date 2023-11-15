/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eson.component.util;

import eson.core.util.StringUtils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Wawa Eson
 */
public class JTableCellRenderer extends DefaultTableCellRenderer{
    
    private EsonStringType STRING_TYPE = EsonStringType.NO_FORMAT;
            
    public enum EsonStringType{
        CAPITALIZE, UPPERCASE_ALL, LOWERCASE_ALL, NO_FORMAT
    }
    
    public JTableCellRenderer(Color background[], Color foreground[]){
        this.background = background;
        this.foreground = foreground;
    }
    
    public JTableCellRenderer(Color background[], Color foreground[], int alignment){
        this.background = background;
        this.foreground = foreground;
        this.HORIZONTAL_ALIGNMENT = alignment;
    }
    
    public JTableCellRenderer(Color background[], Color foreground[], Font font, EsonStringType stringType, StringUtils stringUtils){
        this(background,foreground,stringType,stringUtils);
        this.font = font;
    }
    
    public JTableCellRenderer(Color background[], Color foreground[], Font font, EsonStringType stringType, StringUtils stringUtils, int alignment){
        this(background,foreground,stringType,stringUtils);
        this.font = font;
        this.HORIZONTAL_ALIGNMENT = alignment;
    }
        
    public JTableCellRenderer(Color background[], Color foreground[], Font font, String numberFormat, EsonStringType stringType, StringUtils stringUtils){
        this(background,foreground,font,stringType,stringUtils);
        this.numberFormat = numberFormat;
    }
        
    public JTableCellRenderer(Color background[], Color foreground[], Font font, String numberFormat, EsonStringType stringType, StringUtils stringUtils, int alignment){
        this(background,foreground,font,stringType,stringUtils);
        this.numberFormat = numberFormat;
        this.HORIZONTAL_ALIGNMENT = alignment;
    }
        
    public JTableCellRenderer(Color background[], Color foreground[], Font font, String numberFormat){
        this(background,foreground,font);
        this.numberFormat = numberFormat;
    }
        
    public JTableCellRenderer(Color background[], Color foreground[], Font font, String numberFormat, int alignment){
        this(background,foreground,font);
        this.numberFormat = numberFormat;
        this.HORIZONTAL_ALIGNMENT = alignment;
    }
        
    public JTableCellRenderer(Color background[], Color foreground[], Font font){
        this(background,foreground);
        this.font = font;
    }
        
    public JTableCellRenderer(Color background[], Color foreground[], Font font, int alignment){
        this(background,foreground);
        this.font = font;
        this.HORIZONTAL_ALIGNMENT = alignment;
    }
        
    public JTableCellRenderer(Color background[], Color foreground[], EsonStringType stringType, StringUtils stringUtils){
        this(background,foreground);
        this.STRING_TYPE = stringType;
        this.stringUtils = stringUtils;
    }
        
    public JTableCellRenderer(Color background[], Color foreground[], EsonStringType stringType, StringUtils stringUtils, int alignment){
        this(background,foreground);
        this.STRING_TYPE = stringType;
        this.stringUtils = stringUtils;
        this.HORIZONTAL_ALIGNMENT = alignment;
    }
        
    public JTableCellRenderer(Color background[], Color foreground[], String numberFormat){
        this(background,foreground);
        this.numberFormat = numberFormat;
    }
        
    public JTableCellRenderer(Color background[], Color foreground[], String numberFormat, int alignment){
        this(background,foreground);
        this.numberFormat = numberFormat;
        this.HORIZONTAL_ALIGNMENT = alignment;
    }
    
    public JTableCellRenderer(Color background[], String caseValue[], Color caseColor[]){
        this.background = background;
        this.caseValue = caseValue;
        this.caseColor = caseColor;
    }
    
    public JTableCellRenderer(Color background[], String caseValue[], Color caseColor[], int alignment){
        this.HORIZONTAL_ALIGNMENT = alignment;
        this.background = background;
        this.caseValue = caseValue;
        this.caseColor = caseColor;
    }
    
    public JTableCellRenderer(Color background[], String caseValue[], Color caseColor[], EsonStringType stringType){
        this.background = background;
        this.caseValue = caseValue;
        this.caseColor = caseColor;
        this.STRING_TYPE = stringType;
    }
    
    public JTableCellRenderer(Color background[], String caseValue[], Color caseColor[], EsonStringType stringType, int alignment){
        this.HORIZONTAL_ALIGNMENT = alignment;
        this.background = background;
        this.caseValue = caseValue;
        this.caseColor = caseColor;
        this.STRING_TYPE = stringType;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel c = new JLabel();
        c.setFont(font!=null?font:table.getFont());
        c.setOpaque(true);
        checkText(value,c);
        checkBackground(row,c);
        if(hideText){hideForeground(row,c);}else{checkForeground(row,c);}
        checkCase(value,c);
        c.setHorizontalAlignment(JLabel.CENTER);
        checkAlignment(c,value);
        if (isSelected) {
            c.setBackground(table.getSelectionBackground());
            c.setForeground(table.getSelectionForeground());
        }
        c.setBorder(BorderFactory.createMatteBorder(0, column==0?1:0, 0, 0, table.getGridColor()));
        return c;
    }
    
    private void checkAlignment(JLabel c, Object value){
        if(value instanceof String){
            switch(HORIZONTAL_ALIGNMENT){
                case JLabel.LEADING: checkText("     "+value,c); break;
                case JLabel.LEFT: checkText("     "+value,c); break;
                case JLabel.TRAILING: checkText(value+"     ",c); break;
                case JLabel.RIGHT: checkText(value+"     ",c); break;
            }
        }
        c.setHorizontalAlignment(HORIZONTAL_ALIGNMENT);
    }
    
    private void checkText(Object value, JLabel c){
        if(value!=null){
            String text = value.toString();
            if(text.toLowerCase().trim().equals("null") || text.toLowerCase().trim().equals("")){
                c.setText("");
            }else{
                if (!numberFormat.equals("") && isDouble(value.toString().trim())) {
                    text = new DecimalFormat(numberFormat).format(Double.parseDouble(value.toString()));
                }
                switch(STRING_TYPE){
                    case CAPITALIZE: 
                        if(stringUtils==null){c.setText(new StringUtils().capitalize(text));
                        }else{c.setText(stringUtils.capitalize(text));}
                        break;
                    case UPPERCASE_ALL: 
                        c.setText(text.toUpperCase());
                        break;
                    case LOWERCASE_ALL:
                        c.setText(text.toLowerCase());
                        break;
                    default: c.setText(text); break;
                }
            }
        }else{c.setText("");}
        if(c.getText().split("").length>35){
            c.setToolTipText(c.getText());
        }
    }
    
    private boolean isDouble(String text){
        try{Double.parseDouble(text);return true;}catch(Exception e){return false;}
    }
    
    private void checkCase(Object value, JLabel c){
        if(caseValue!=null && value!=null){
            int i = 0;
            for (String v : caseValue) {
                if (v.toLowerCase().trim().equals(value.toString().toLowerCase().trim())) {
                    c.setForeground(caseColor[i]);
                    break;
                }
                i++;
            }
        }
    }
    
    private void checkBackground(int row, JLabel c){
        if(background!=null){
            Color bg = background[0];
            if (row < background.length) {
                int i = 0;
                while (i < background.length) {
                    if (i == row) {
                        bg = background[i];
                    }
                    i++;
                }
            } else {
                bg = background[row % background.length];
            }
            c.setBackground(bg);
        }
    }
    
    private void hideForeground(int row, JLabel c){
        if(background!=null){
            Color bg = background[0];
            if (row < background.length) {
                int i = 0;
                while (i < background.length) {
                    if (i == row) {
                        bg = background[i];
                    }
                    i++;
                }
            } else {
                bg = background[row % background.length];
            }
            c.setForeground(bg);
        }
    }
    
    private void checkForeground(int row, JLabel c){
        if(foreground!=null){
            Color bg = foreground[0];
            if (row < foreground.length) {
                int i = 0;
                while (i < foreground.length) {
                    if (i == row) {
                        bg = foreground[i];
                    }
                    i++;
                }
            } else {
                bg = foreground[row % foreground.length];
            }
            c.setForeground(bg);
        }
    }
    
    public void hideText(boolean flag){
        hideText = flag;
    }
    
    private String numberFormat = "";
    private String caseValue[] = null;
    private Color caseColor[] = null;
    private Color background[] = null;
    private Color foreground[] = null;
    private Font font = null;
    private StringUtils stringUtils = null;
    private int HORIZONTAL_ALIGNMENT = 0;
    private boolean hideText = false;
    
}
