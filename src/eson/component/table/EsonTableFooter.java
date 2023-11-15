/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eson.component.table;

import eson.component.EsonButton;
import eson.component.EsonButton.EsonButtonType;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import eson.core.util.ImageRenderer;
import eson.core.util.StringUtils;

/**
 *
 * @author Codey Hyacinth
 */
public class EsonTableFooter extends JPanel {
    
    private EsonTable TABLE = null;
    private int ROW_COUNT = 0, MAX_COUNT = 0, FOOTER_HEIGHT = 25, PAGE_COUNT = 0, CURRENT_PAGE = 1;
    private String SINGULAR = "Item", PLURAL = "Items";
    private final JLabel loadingLabel = new JLabel();
    private final JLabel multiLabel = new JLabel();
    private final JLabel itemCountLabel = new JLabel();
    private final StringUtils stringUtils = new StringUtils();
    private final EsonButton topButton = new EsonButton();
    private final EsonButton prevButton = new EsonButton();
    private final EsonButton nextButton = new EsonButton();
    private final EsonButton endButton = new EsonButton();
    private List<Object[]> VALUES = null;
    private Image TOP_ICON = null,
            PREV_ICON = null,
            NEXT_ICON = null,
            END_ICON = null;
    
    public EsonTableFooter(EsonTable esonTable, int height){
        TABLE = esonTable;
        FOOTER_HEIGHT = height>27?27:height;
        TOP_ICON = new ImageIcon(getClass().getResource("/json/asidera/images/top_arrow.png")).getImage();
        PREV_ICON = new ImageIcon(getClass().getResource("/json/asidera/images/prev_arrow.png")).getImage();
        NEXT_ICON = new ImageIcon(getClass().getResource("/json/asidera/images/next_arrow.png")).getImage();
        END_ICON = new ImageIcon(getClass().getResource("/json/asidera/images/end_arrow.png")).getImage();
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
        initLabel(loadingLabel);
        initLabel(multiLabel);
        initLabel(itemCountLabel);
        itemCountLabel.setPreferredSize(new Dimension(150,FOOTER_HEIGHT));
        itemCountLabel.setMinimumSize(new Dimension(100,FOOTER_HEIGHT));
        itemCountLabel.setMaximumSize(new Dimension(9999,FOOTER_HEIGHT));
        itemCountLabel.setHorizontalAlignment(JLabel.LEADING);
        itemCountLabel.setBorder(BorderFactory.createEmptyBorder(0,25,0,25));
        initButtons();
    }
    
    private void initButtons(){
        int iconW = (FOOTER_HEIGHT/2)-3;
        int iconH = (FOOTER_HEIGHT/2)-3;
        //TOP BUTTON
        initButtonUI(topButton,TOP_ICON,iconW+2,iconH);
        topButton.addActionListener((ActionEvent e) -> {
            if(CURRENT_PAGE!=1 && TABLE.isActionEnabled()){
                CURRENT_PAGE = 1;
                changePageView();
            }
        });
        //PREV BUTTON
        initButtonUI(prevButton,PREV_ICON,iconW,iconH);
        prevButton.addActionListener((ActionEvent e) -> {
            if(CURRENT_PAGE>1 && TABLE.isActionEnabled()){
                CURRENT_PAGE--;
                changePageView();
            }
        });
        //NEXT BUTTON
        initButtonUI(nextButton,NEXT_ICON,iconW,iconH);
        nextButton.addActionListener((ActionEvent e) -> {
            if(CURRENT_PAGE<PAGE_COUNT && TABLE.isActionEnabled()){
                CURRENT_PAGE++;
                changePageView();
            }
        });
        //END BUTTON
        initButtonUI(endButton,END_ICON,iconW+2,iconH);
        endButton.addActionListener((ActionEvent e) -> {
            if(CURRENT_PAGE!=PAGE_COUNT && TABLE.isActionEnabled()){
                CURRENT_PAGE = PAGE_COUNT;
                changePageView();
            }
        });
    }
    
    public int getCurrentPage(){
        return CURRENT_PAGE;
    }
    
    private void changePageView(){
        String name = ROW_COUNT==1?SINGULAR:PLURAL;
        TABLE.viewPage(VALUES, CURRENT_PAGE);
        itemCountLabel.setText(TEXT.trim().toUpperCase()+" : "+stringUtils.formatDecimal("###,###", ROW_COUNT)+" "+name);
        multiLabel.setText("Page "+stringUtils.formatDecimal("###,###", CURRENT_PAGE) + " of " + stringUtils.formatDecimal("###,###", PAGE_COUNT));
    }
    
    private void initLabel(JLabel label){
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setForeground(TABLE.getFooterForeground());
        label.setFont(TABLE.getFooterFont());
        label.setBorder(BorderFactory.createEmptyBorder(2,15,0,15));
    }
    
    private void initButtonUI(EsonButton button,Image icon, int iconWidth, int iconHeight){
        button.setText("");
        button.setNormalBackground(TABLE.getRowBackgrounds()[0]);    
        button.setNormalForeground(TABLE.getRowForegrounds()[0]);  
        button.setHoverBackground(TABLE.getRowHoverBackground());
        button.setHoverForeground(TABLE.getRowHoverForeground());
        button.setPressedBackground(TABLE.getFooterBackground());
        button.setPressedForeground(TABLE.getFooterForeground());
        ImageRenderer RENDERER = new ImageRenderer();
        button.getLabel().setIcon(RENDERER.resizeIcon(icon,iconWidth, iconHeight, TABLE.getRowForegrounds()[0]));
        button.setCornerRadius(5);
        int h = FOOTER_HEIGHT;
        int w = (FOOTER_HEIGHT-5)+10;
        button.setPreferredSize(new Dimension(w,h));
        button.setMinimumSize(new Dimension(w,h));
        button.setMaximumSize(new Dimension(w,h));
        button.setButtonType(EsonButtonType.CUSTOM_NO_BORDER);
        button.getLabel().setBorder(BorderFactory.createEmptyBorder(0,2,0,2));
    }
    
    private String TEXT = "TOTAL: ";
    public void setValue(String title, List<Object[]> value, int maxVisibleCount){
        ROW_COUNT = value.size();
        MAX_COUNT = maxVisibleCount;
        PAGE_COUNT = (ROW_COUNT+MAX_COUNT-1)/MAX_COUNT;
        CURRENT_PAGE = 1;
        VALUES = value;
        TEXT = title;
        initFooter();
    }
    
    public EsonTableFooter(EsonTable esonTable, int rowCount, int maxVisibleCount, int height, String singularName, String pluralName){
        TABLE = esonTable;
        ROW_COUNT = rowCount;
        MAX_COUNT = maxVisibleCount;
        SINGULAR = singularName;
        PLURAL = pluralName;
        FOOTER_HEIGHT = height;
        initFooter();
    }
    
    private void initFooter(){
        String name = ROW_COUNT==1?SINGULAR:PLURAL;
        if(ROW_COUNT<=MAX_COUNT){
            itemCountLabel.setHorizontalAlignment(JLabel.CENTER);
            refreshFooter(new Component[]{itemCountLabel});
            String text = ROW_COUNT<=0?(TEXT.equals("SEARCHED")?"NO RESULTS FOUND!":""):TEXT+" : "+stringUtils.formatDecimal("###,##0", ROW_COUNT)+" "+name;
            itemCountLabel.setText(text);
        }else{
            itemCountLabel.setHorizontalAlignment(JLabel.LEADING);
            if(!topButton.isShowing()){
                refreshFooter(new Component[]{itemCountLabel,topButton,prevButton,multiLabel,nextButton,endButton,newMargin()});
            }
            itemCountLabel.setText(TEXT.trim().toUpperCase()+" : "+stringUtils.formatDecimal("###,##0", ROW_COUNT)+" "+name);
            multiLabel.setText("Page "+stringUtils.formatDecimal("###,##0", CURRENT_PAGE) + " of " + stringUtils.formatDecimal("###,##0", PAGE_COUNT));
        }
    }
    
    /**BOX LAYOUT**/
    private void refreshFooter(Component[] COMPONENTS){
        removeAll();
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));
        for(Component c:COMPONENTS){ add(c); }
    }
    
    private JPanel newMargin(){
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(35,FOOTER_HEIGHT));
        panel.setMinimumSize(new Dimension(35,FOOTER_HEIGHT));
        panel.setMaximumSize(new Dimension(35,FOOTER_HEIGHT));
        return panel;
    }
    
}
