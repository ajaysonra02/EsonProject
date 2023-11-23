package eson.component.util;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class CustomScrollBarUI extends BasicScrollBarUI {
    
    public CustomScrollBarUI(){
        CORNER_RADIUS = 10;
    }
    
    public CustomScrollBarUI(int radius){
        this.CORNER_RADIUS = radius;
    }

    @Override
    protected void paintTrack(Graphics g, JComponent component, Rectangle rect) {
        Graphics2D g2d = (Graphics2D) g;
        applyQualityRenderingHints(g2d);
        g2d.setColor(scrollbar.getBackground());
        boolean isVertical = scrollbar.getOrientation()==JScrollBar.VERTICAL;
        int size = (isVertical?rect.width:rect.height)/2;
        int x = isVertical?(rect.x+((rect.width-size)/2)):0;
        int y = isVertical?rect.y:(rect.y+((rect.height-size)/2));
        int width = isVertical?size:rect.width;
        int height = isVertical?rect.height:size;
        g2d.fillRect(x, y, width, height);
    }

    @Override
    protected void paintThumb(Graphics g, JComponent component, Rectangle rect) {
        Graphics2D g2d = (Graphics2D) g;
        applyQualityRenderingHints(g2d);
        g2d.setColor(scrollbar.getForeground());
        boolean isVertical = scrollbar.getOrientation()==JScrollBar.VERTICAL;
        int x = rect.x+(isVertical?0:8);
        int y = rect.y+(isVertical?8:0);
        int width = rect.width-(isVertical?0:16);
        int height = rect.height-(isVertical?16:0);
        g2d.fillRoundRect(x, y, width, height, CORNER_RADIUS, CORNER_RADIUS);
    }
    
    private void applyQualityRenderingHints(Graphics2D g2d){
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }

    @Override
    protected JButton createIncreaseButton(int i) {
        JButton button = new JButton();
        button.setBorder(BorderFactory.createEmptyBorder());
        return button;
    }

    @Override
    protected JButton createDecreaseButton(int i) {
        JButton button = new JButton();
        button.setBorder(BorderFactory.createEmptyBorder());
        return button;
    }
    
    @Override 
    protected Dimension getMinimumThumbSize() {
        boolean f = scrollbar.getOrientation() == JScrollBar.VERTICAL;
        return new Dimension(f?0:35,f?35:0);
    }
    
    private int CORNER_RADIUS = 10;
    
}
