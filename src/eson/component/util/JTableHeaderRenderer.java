
package eson.component.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author WAWA ESON
 */
public class JTableHeaderRenderer extends DefaultTableCellRenderer{
    
    
    private Color BACKGROUND = null;
    private Color FOREGROUND = null;
    private Color GRID = null;
    private int HEIGHT = 25;
    private Font FONT = null;
    private CellVerticalAlignment ALIGNMENT = CellVerticalAlignment.CENTER;
    
    public enum CellVerticalAlignment{
        TOP, CENTER, BOTTOM;
    }
    
    public JTableHeaderRenderer(Color background, Color foreground, Color grid, int height){
        BACKGROUND = background;
        FOREGROUND = foreground;
        GRID = grid;
        HEIGHT = height;
    }
    
    public JTableHeaderRenderer(Color background, Color foreground, Color grid, int height, Font font){
        BACKGROUND = background;
        FOREGROUND = foreground;
        GRID = grid;
        HEIGHT = height;
        FONT = font;
    }
    
    public JTableHeaderRenderer(Color background, Color foreground, Color grid, int height, CellVerticalAlignment align){
        BACKGROUND = background;
        FOREGROUND = foreground;
        GRID = grid;
        HEIGHT = height;
        ALIGNMENT = align;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setBackground(BACKGROUND);
        setForeground(FOREGROUND);
        setBorder(BorderFactory.createMatteBorder(0, column==0?1:0, 0, 1, BACKGROUND));
        setHorizontalAlignment(JLabel.CENTER);
        int vAlignment = JLabel.CENTER;
        switch(ALIGNMENT){
            case TOP: vAlignment = JLabel.TOP; break;
            case BOTTOM: vAlignment = JLabel.BOTTOM; break;
            case CENTER: vAlignment = JLabel.CENTER; break;
        }
        if(FONT!=null){
            setFont(FONT);
        }
        setVerticalAlignment(vAlignment);
        setPreferredSize(new Dimension(getPreferredSize().width,HEIGHT));
        return c;
    }
    
}
