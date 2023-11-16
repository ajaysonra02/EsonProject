/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eson.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import eson.core.util.ImageRenderer;

/**
 *
 * @author ASUS-B150M
 */
public class EsonTab extends javax.swing.JPanel {

    /**
     * Creates new form EsonTab
     */
    private String[] TAB_NAMES = null;
    private java.awt.Color ICON_NORMAL_COLOR = null;
    private java.awt.Color ICON_HOVER_COLOR = null;
    private java.awt.Color ICON_PRESSED_COLOR = null;
    private java.awt.Color ICON_SELECTED_COLOR = null;
    private java.awt.Color ICON_DROP_COLOR = null;
    private java.awt.Color SELECTED_BACKGROUND = null;
    private java.awt.Color SELECTED_FOREGROUND = null;
    private java.awt.Color HOVER_BACKGROUND = null;
    private java.awt.Color HOVER_FOREGROUND = null;
    private java.awt.Color NORMAL_BACKGROUND = null;
    private java.awt.Color NORMAL_FOREGROUND = null;
    private java.awt.Color PRESSED_BACKGROUND = null;
    private java.awt.Color PRESSED_FOREGROUND = null;
    private java.awt.Color DROP_BACKGROUND = null;
    private java.awt.Color DROP_FOREGROUND = null;
    private java.awt.Color DROP_N_BACKGROUND = null;
    private java.awt.Color DROP_N_FOREGROUND = null;
    private java.awt.Color DROP_H_BACKGROUND = null;
    private java.awt.Color DROP_H_FOREGROUND = null;
    private java.awt.Color DROP_P_BACKGROUND = null;
    private java.awt.Color DROP_P_FOREGROUND = null;
    private java.awt.Color DROP_S_BACKGROUND = null;
    private java.awt.Color DROP_S_FOREGROUND = null;
    private java.awt.Color DROP_BORDER_N_COLOR = null;
    private java.awt.Color DROP_BORDER_H_COLOR = null;
    private java.awt.Color DROP_BORDER_P_COLOR = null;
    private java.awt.Color BLUR_PANEL_BACKGROUND = null;
    private java.awt.Font NORMAL_FONT = null;
    private java.awt.Font SELECTED_FONT = null;
    private java.awt.Font DROP_DOWN_FONT = null;
    private boolean LINE_SHOWING = true;
    private TabColumn TAB_COLUMNS[] = null;
    private String SELECTED_TAB = "";
    private String SELECTION = "";
    private int COLUMN_WIDTH = 80;
    private int SELECTED_INDEX = -1;
    private int DROP_DOWN_Y_LOCATION = 0;
    private int DROP_TEXT_ALIGNMENT = 0;
    private int TAB_ICON_SIZE = 20;
    private int TAB_ICON_TEXT_GAP = 8;
    private int DROP_ICON_SIZE = 11;
    private int DROP_ICON_TEXT_GAP = 8;
    private Component DROP_DOWN_CONTAINER = null;
    protected JPanel BLUR_PANEL = new JPanel(), DES_PANEL = null;
    private ImageIcon DROP_ITEM_ICON = null;
    private ImageRenderer RENDERER = null;
    
    public EsonTab() {
        this(new String[]{"Tab1","Tab2","Tab3","Tab4","Tab5"});
    }
    
    public EsonTab(String[] tabNames){
        RENDERER = new ImageRenderer();
        TAB_NAMES = tabNames;
        DROP_ITEM_ICON = new javax.swing.ImageIcon(getClass().getResource("/json/asidera/images/drop_item_icon.png"));
        DES_PANEL = (JPanel)this;
        initComponents();
        init();
    }
    
    public void setDropIconSize(int size){
        DROP_ICON_SIZE = size;
    }
    
    public int getDropIconSize(){
        return DROP_ICON_SIZE;
    }
    
    public void setDropIconTextGap(int gap){
        DROP_ICON_TEXT_GAP = gap;
    }
    
    public int getDropIconTextGap(){
        return DROP_ICON_TEXT_GAP;
    }
    
    public void setTabIconSize(int size){
        TAB_ICON_SIZE = size;
    }
    
    public int getTabIconSize(){
        return TAB_ICON_SIZE;
    }
    
    public void setTabIconTextGap(int gap){
        TAB_ICON_TEXT_GAP = gap;
    }
    
    public int getTabIconTextGap(){
        return TAB_ICON_TEXT_GAP;
    }
    
    public void setDropItemIcon(ImageIcon icon){
        DROP_ITEM_ICON = icon;
    }
    
    public ImageIcon getDropIcon(){
        return DROP_ITEM_ICON;
    }
    
    public void setTabNames(String[] tabNames){
        TAB_NAMES = tabNames;
        showTabs();
    }
    
    public String[] getTabNames(){
        return TAB_NAMES;
    }
    
    public void setColumnWidth(int width){
        COLUMN_WIDTH = width;
        showTabs();
    }
    
    public int getColumnWidth(){
        return COLUMN_WIDTH;
    }
    
    private void init(){
        initColor();
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(600,40));
        showTabs();
        BLUR_PANEL.addMouseListener(getPopUpListener());
    }
    
    private MouseListener getPopUpListener(){
        return new MouseListener(){
                @Override public void mouseClicked(MouseEvent e) { checkPopups(); }
                @Override public void mouseExited(MouseEvent e) { /*Do Nothing*/ }
                @Override public void mouseEntered(MouseEvent e) { /*Do Nothing*/ }
                @Override public void mousePressed(MouseEvent e) { /*Do Nothing*/ }
                @Override public void mouseReleased(MouseEvent e) { /*Do Nothing*/ }
            };
    }
    
    private void showTabs(){
        removeAll();
        TAB_COLUMNS = new TabColumn[TAB_NAMES.length];
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        
        Group verticalGroup = layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING);
        Group horizontalGroup = layout.createSequentialGroup();
        int i = 0;
        int labelGap = LINE_SHOWING?0:5;
        JPanel firstColumn = getColumn(0,"",false,labelGap,verticalGroup,horizontalGroup,4);
        firstColumn.addMouseListener(getPopUpListener());
        for(String s:TAB_NAMES){
            TAB_COLUMNS[i] = getColumn(i,s,true,labelGap,verticalGroup,horizontalGroup,COLUMN_WIDTH);
            i++;
        }
        TabColumn lastColumn = new TabColumn(this, "", TAB_NAMES.length,false);
        lastColumn.addMouseListener(getPopUpListener());
        javax.swing.GroupLayout lastLayout = new javax.swing.GroupLayout(lastColumn);
        lastColumn.setLayout(lastLayout);
        javax.swing.JLabel label2 = lastColumn.getLabel();
        javax.swing.JSeparator separator = lastColumn.getSeparator();
        lastLayout.setHorizontalGroup(
                lastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(label2, javax.swing.GroupLayout.DEFAULT_SIZE, COLUMN_WIDTH, Short.MAX_VALUE)
                        .addGroup(lastLayout.createSequentialGroup()
                                .addComponent(separator))
        );
        lastLayout.setVerticalGroup(
                lastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(lastLayout.createSequentialGroup()
                                .addGap(labelGap,labelGap,labelGap)
                                .addComponent(label2, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                                .addGap(0, 0, 0)
                                .addComponent(separator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0))
        );
        horizontalGroup.addGap(0, 0, 0)
                .addComponent(lastColumn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
        verticalGroup.addComponent(lastColumn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(horizontalGroup)
        );
        layout.setVerticalGroup(verticalGroup);
        TAB_COLUMNS[0].setSelected(true);
    }
    
    private TabColumn getColumn(int index, String s, boolean flag, int labelGap, Group verticalGroup, Group horizontalGroup, int width){
        TabColumn column = new TabColumn(this, s, index, flag);
        javax.swing.GroupLayout tabLayout = new javax.swing.GroupLayout(column);
        column.setLayout(tabLayout);
        javax.swing.JLabel label1 = column.getLabel();
        javax.swing.JSeparator separator = column.getSeparator();
        tabLayout.setHorizontalGroup(
                tabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(label1, javax.swing.GroupLayout.DEFAULT_SIZE, width, Short.MAX_VALUE)
                        .addGroup(tabLayout.createSequentialGroup()
                                .addComponent(separator))
        );
        tabLayout.setVerticalGroup(
                tabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(tabLayout.createSequentialGroup()
                                .addGap(labelGap, labelGap, labelGap)
                                .addComponent(label1, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                                .addGap(0, 0, 0)
                                .addComponent(separator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0))
        );
        verticalGroup.addComponent(column, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
        horizontalGroup.addGap(0, 0, 0)
                .addComponent(column, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE);
        return column;
    }
    
    private void initColor(){
        LINE_SHOWING = false;
        
        ICON_NORMAL_COLOR = new Color(255,255,255);
        ICON_HOVER_COLOR = new Color(255,255,255);
        ICON_PRESSED_COLOR =  new Color(255,51,0);
        ICON_DROP_COLOR = new Color(255,255,255);
        ICON_SELECTED_COLOR = new Color(255,255,255);
        
        SELECTED_BACKGROUND = new Color(255,51,0);
        SELECTED_FOREGROUND = new Color(255,255,255);
        NORMAL_BACKGROUND = new Color(240,240,240);
        NORMAL_FOREGROUND = new Color(0,0,0);
        HOVER_BACKGROUND = NORMAL_BACKGROUND;
        HOVER_FOREGROUND = new Color(255,51,0);
        PRESSED_BACKGROUND = new Color(200,200,200);
        PRESSED_FOREGROUND = new Color(255,51,0);
        DROP_BACKGROUND = new Color(180,180,180);
        DROP_FOREGROUND = new Color(0,0,0);
        
        DROP_N_BACKGROUND = new Color(255,255,255);
        DROP_N_FOREGROUND = new Color(0,0,0);
        DROP_H_BACKGROUND = DROP_N_BACKGROUND;
        DROP_H_FOREGROUND = new Color(255,51,0);
        DROP_P_BACKGROUND = new Color(255,51,0);
        DROP_P_FOREGROUND = new Color(255,255,255);
        DROP_S_BACKGROUND = new Color(255,51,0);
        DROP_S_FOREGROUND = new Color(255,255,255);
        
        
        DROP_BORDER_N_COLOR = new Color(180,180,180);
        DROP_BORDER_H_COLOR = new Color(180,180,180);
        DROP_BORDER_P_COLOR = new Color(180,180,180);
        
        BLUR_PANEL_BACKGROUND = new Color(0,0,0,10);
        
        SELECTED_FONT = new java.awt.Font("SansSerif",0,12);
        NORMAL_FONT = new java.awt.Font("SansSerif",0,12);
        DROP_DOWN_FONT = new java.awt.Font("SansSerif",0,12);
        
        DROP_TEXT_ALIGNMENT = JLabel.LEADING;
        TAB_ICON_SIZE = 20;
        TAB_ICON_TEXT_GAP = 8;
        DROP_ICON_SIZE = 11;
        DROP_ICON_TEXT_GAP = 8;
    }
    
    public void setIconNormalColor(Color color){
        ICON_NORMAL_COLOR = color;
    }
    
    public Color getIconNormalColor(){
        return ICON_NORMAL_COLOR;
    }
    
    public void setIconHoverColor(Color color){
        ICON_HOVER_COLOR = color;
    }
    
    public Color getIconHoverColor(){
        return ICON_HOVER_COLOR;
    }
    
    public void setIconPressedColor(Color color){
        ICON_PRESSED_COLOR = color;
    }
    
    public Color getIconPressedColor(){
        return ICON_PRESSED_COLOR;
    }
    
    public void setIconSelectedColor(Color color){
        ICON_SELECTED_COLOR = color;
    }
    
    public Color getIconSelectedColor(){
        return ICON_SELECTED_COLOR;
    }
    
    public void setIconDropColor(Color color){
        ICON_DROP_COLOR = color;
    }
    
    public Color getIconDropColor(){
        return ICON_DROP_COLOR;
    }
    
    public void setDropBorderNormalColor(Color color){
        DROP_BORDER_N_COLOR = color;
    }
    
    public Color getDropBorderNormalColor(){
        return DROP_BORDER_N_COLOR;
    }
    
    public void setDropBorderHoverColor(Color color){
        DROP_BORDER_H_COLOR = color;
    }
    
    public Color getDropBorderHoverColor(){
        return DROP_BORDER_H_COLOR;
    }
    
    public void setDropBorderPressedColor(Color color){
        DROP_BORDER_P_COLOR = color;
    }
    
    public Color getDropBorderPressedColor(){
        return DROP_BORDER_P_COLOR;
    }
    
    public void setDropDownFont(Font f){
        DROP_DOWN_FONT = f;
    }
    
    public Font getDropDownFont(){
        return DROP_DOWN_FONT;
    }
    
    public void setDropTextHorizontalAlignment(int alignment){
        DROP_TEXT_ALIGNMENT = alignment;
    }
    
    public int getDropTextHorizontalAlignment(){
        return DROP_TEXT_ALIGNMENT;
    }
    
    public void setDropDownContainer(Component c){
        DROP_DOWN_CONTAINER = c;
    }
    
    public Component getDropDownContainer(){
        return DROP_DOWN_CONTAINER;
    }
    
    public void setDropDownYLocation(int y){
        DROP_DOWN_Y_LOCATION = y;
    }
    
    public int getDropDownYLocation(){
        return DROP_DOWN_Y_LOCATION;
    }
    
    public void setBlurPanelBackground(Color color){
        BLUR_PANEL_BACKGROUND = color;
    }
    
    public Color getBlurPanelBackground(){
        return BLUR_PANEL_BACKGROUND;
    }
    
    public void set_hoverBackground(java.awt.Color color){
        HOVER_BACKGROUND = color;
        if(TAB_COLUMNS!=null){
            showTabs();
        }
    }
    
    public java.awt.Color get_hoverBackground(){
        return HOVER_BACKGROUND;
    }
    
    public void set_hoverForeground(java.awt.Color color){
        HOVER_FOREGROUND = color;
        if(TAB_COLUMNS!=null){
            showTabs();
        }
    }
    
    public java.awt.Color get_hoverForeground(){
        return HOVER_FOREGROUND;
    }
    
    public void set_selectedBackground(java.awt.Color color){
        SELECTED_BACKGROUND = color;
        if(TAB_COLUMNS!=null){
            showTabs();
        }
    }
    
    public java.awt.Color get_selectedBackground(){
        return SELECTED_BACKGROUND;
    }
    
    public void set_selectedForeground(java.awt.Color color){
        SELECTED_FOREGROUND = color;
        if(TAB_COLUMNS!=null){
            showTabs();
        }
    }
    
    public java.awt.Color get_selectedForeground(){
        return SELECTED_FOREGROUND;
    }
    
    public void set_normalBackground(java.awt.Color color){
        NORMAL_BACKGROUND = color;
        if(TAB_COLUMNS!=null){
            showTabs();
        }
    }
    
    public java.awt.Color get_normalBackground(){
        return NORMAL_BACKGROUND;
    }
    
    public void set_normalForeground(java.awt.Color color){
        NORMAL_FOREGROUND = color;
        if(TAB_COLUMNS!=null){
            showTabs();
        }
    }
    
    public java.awt.Color get_normalForeground(){
        return NORMAL_FOREGROUND;
    }
    
    public void set_pressedBackground(java.awt.Color color){
        PRESSED_BACKGROUND = color;
        if(TAB_COLUMNS!=null){
            showTabs();
        }
    }
    
    public java.awt.Color get_pressedBackground(){
        return PRESSED_BACKGROUND;
    }
    
    public void set_pressedForeground(java.awt.Color color){
        PRESSED_FOREGROUND = color;
    }
    
    public java.awt.Color get_pressedForeground(){
        return PRESSED_FOREGROUND;
    }
    
    public void set_dropBackground(java.awt.Color color){
        DROP_BACKGROUND = color;
        if(TAB_COLUMNS!=null){
            showTabs();
        }
    }
    
    public java.awt.Color get_dropBackground(){
        return DROP_BACKGROUND;
    }
    
    public void set_dropForeground(java.awt.Color color){
        DROP_FOREGROUND = color;
    }
    
    public java.awt.Color get_dropForeground(){
        return DROP_FOREGROUND;
    }
    
    public void set_normalDropBackground(java.awt.Color color){
        DROP_N_BACKGROUND = color;
    }
    
    public java.awt.Color get_normalDropBackground(){
        return DROP_N_BACKGROUND;
    }
    
    public void set_normalDropForeground(java.awt.Color color){
        DROP_N_FOREGROUND = color;
    }
    
    public java.awt.Color get_normalDropForeground(){
        return DROP_N_FOREGROUND;
    }
    
    public void set_hoverDropBackground(java.awt.Color color){
        DROP_H_BACKGROUND = color;
    }
    
    public java.awt.Color get_hoverDropBackground(){
        return DROP_H_BACKGROUND;
    }
    
    public void set_hoverDropForeground(java.awt.Color color){
        DROP_H_FOREGROUND = color;
    }
    
    public java.awt.Color get_hoverDropForeground(){
        return DROP_H_FOREGROUND;
    }
    
    public void set_pressedDropBackground(java.awt.Color color){
        DROP_P_BACKGROUND = color;
    }
    
    public java.awt.Color get_pressedDropBackground(){
        return DROP_P_BACKGROUND;
    }
    
    public void set_pressedDropForeground(java.awt.Color color){
        DROP_P_FOREGROUND = color;
    }
    
    public java.awt.Color get_pressedDropForeground(){
        return DROP_P_FOREGROUND;
    }
    
    public void set_selectedDropBackground(java.awt.Color color){
        DROP_S_BACKGROUND = color;
    }
    
    public java.awt.Color get_selectedDropBackground(){
        return DROP_S_BACKGROUND;
    }
    
    public void set_selectedDropForeground(java.awt.Color color){
        DROP_S_FOREGROUND = color;
    }
    
    public java.awt.Color get_selectedDropForeground(){
        return DROP_S_FOREGROUND;
    }
    
    @Override
    public void setBackground(java.awt.Color color){
        NORMAL_BACKGROUND = color;
        if(TAB_COLUMNS!=null){
            showTabs();
        }
    }
    
    @Override
    public void setForeground(java.awt.Color color){
        NORMAL_FOREGROUND = color;
        if(TAB_COLUMNS!=null){
            showTabs();
        }
    }
    
    @Override
    public void setFont(java.awt.Font font){
        if(TAB_COLUMNS!=null){
            for(TabColumn column:TAB_COLUMNS){
                column.getLabel().setFont(font);
            }
            showTabs();
        }
        NORMAL_FONT = font;
    }
    
    public void setSelectedFont(java.awt.Font font){
        SELECTED_FONT = font;
    }
    
    public java.awt.Font getSelectedFont(){
        return SELECTED_FONT;
    }
    
    private void resetTab(){
        for(TabColumn column:TAB_COLUMNS){
            column.setSelected(false);
            column.exited();
        }
    }
    
    public void checkSelectedTab(){
        int i = 0, index = 0;
        for(TabColumn column:TAB_COLUMNS){
            if(column.isSelected()){
                index = i;
                break;
            }
            i++;
        }
        i = 0;
        for(TabColumn column:TAB_COLUMNS){
           if(i!=index){
               column.setSelected(false);
               column.exited();
           }else{
               column.setSelected(true);
           }
           i++;
       }
    }
    
    public void setLineShown(boolean flag){
        LINE_SHOWING = flag;
        showTabs();
    }
    
    public boolean isLineShown(){
        return LINE_SHOWING;
    }
    
    public void setSelectedTab(String st){
        SELECTED_TAB = st;
        tabAction();
    }
    
    public String getSelectedTab(){
        return SELECTED_TAB;
    }
    
    private void setSelectionValue(int tab_index){
        SELECTED_INDEX = tab_index;
        SELECTED_TAB = TAB_NAMES[tab_index];
        resetTab();
        TAB_COLUMNS[tab_index].setSelected(true);
        tabAction();
    }
    
    public void setSelection(int tab_index, int item_index){
        if(tab_index>-1 && tab_index<TAB_NAMES.length){
            String ITEMS[] = TAB_COLUMNS[tab_index].getDropDown().getDropMenuItems();
            if(item_index>-1 && item_index<ITEMS.length){
                SELECTION = ITEMS[item_index];
                setSelectionValue(tab_index);
            }
        }
    }
    
    public void setSelection(int tab_index, String select){
        if(tab_index>0 && tab_index<TAB_NAMES.length){
            for(String s:TAB_COLUMNS[tab_index].getDropDown().getDropMenuItems()){
                if(s.equals(select)){
                    SELECTION = select;
                    setSelectionValue(tab_index);
                }
            }
        }
    }
    
    public void setSelection(String tab_name, String select){
        int i = 0, index = 0;
        for(String t:TAB_NAMES){
            if(t.equals(tab_name)){
                SELECTED_TAB = tab_name;
                index = i;
                break;
            }
            i++;
        }
        setSelection(index, select);
    }
    
    public String getSelection(){
        return SELECTION;
    }
    
    private void setSelectedIndex(int index){
        SELECTED_INDEX = index;
        //tabAction();
    }
    
    public int getSelectedIndex(){
        return SELECTED_INDEX;
    }
    
    public void addActionListener(ActionListener l){
        listenerList.add(ActionListener.class, l);
    }
    
    public void removeActionListener(ActionListener l){
        listenerList.remove(ActionListener.class, l);
    }
    
    private void tabAction(){
        ActionListener[] action = listenerList.getListeners(ActionListener.class);
        if(action.length>0){
            for(ActionListener a:action){
                if(getSelectedIndex()!=-1){
                    a.actionPerformed(null);
                }
            }
        }
    }
    
    public TabColumn  getTab(int index){
        return TAB_COLUMNS[index];
    }
    
    public TabColumn getTab(String tabName){
        TabColumn retval = null;
        for(TabColumn r:TAB_COLUMNS){
            if(r.getTabName().equals(tabName)){
                retval = r;
                break;
            }
        }
        return retval;
    }
    
    public void checkPopups(){
        for(TabColumn t:TAB_COLUMNS){
            if (t.getDropDown().isShowing()) {
                closeDrop(t.getDropDown());
            }
        }
        checkSelectedTab();
    }
    
    private void closeDrop(DropDown t){
        if (DROP_DOWN_CONTAINER instanceof JPanel pane) {
            t.setVisible(false);
            pane.remove(t);
            BLUR_PANEL.setVisible(false);
            pane.remove(BLUR_PANEL);
            panelUpdate(pane);
        }
    }
    
    private void panelUpdate(JPanel panel){
        panel.revalidate();
        panel.updateUI();
        panel.repaint();
    }
    
    public class TabColumn extends javax.swing.JPanel{
        
        private javax.swing.JSeparator separator = null;
        private javax.swing.JLabel label = null;
        private boolean isSelected = false;
        private boolean isActionEnabled = false;
        private DropDown DROP_MENU = null;
        private int INDEX = 0;
        private ImageIcon ICON = null;
        
        public TabColumn(javax.swing.JPanel parent, String name, int index, boolean actionEnable){
            INDEX = index;
            DROP_MENU = new DropDown();
            ICON = null;
            label = new javax.swing.JLabel(name);
            separator = new javax.swing.JSeparator(JSeparator.HORIZONTAL);
            initTabColumn();
            isActionEnabled = actionEnable;
            initListeners();
        }
        
        public void setIcon(ImageIcon icon){
            ICON = icon;
            setIconForeground(ICON_NORMAL_COLOR);
        }
        
        public ImageIcon getIcon(){
            return ICON;
        }
        
        protected DropDown getDropDown(){
            return DROP_MENU;
        }
        
        private void initTabColumn(){
            label.setHorizontalAlignment(JLabel.CENTER);
            exited();
        }
        
        private void initListeners(){
            addMouseListener(new MouseListener() {
                @Override public void mouseClicked(MouseEvent e) {
                    /*Do Nothing*/
                }
                @Override public void mousePressed(MouseEvent e) { 
                    if(e.getButton()==1 && isActionEnabled){
                        pressed();
                    }
                }
                @Override public void mouseEntered(MouseEvent e) { 
                     if(isActionEnabled && !DROP_MENU.isShowing()){
                         hover();
                     }
                }
                @Override public void mouseExited(MouseEvent e) {
                    if(isActionEnabled && !DROP_MENU.isShowing()){
                        exited();
                    }
                }
                @Override public void mouseReleased(MouseEvent e) { 
                    DES_PANEL.requestFocus();
                    if(e.getButton()==1 && isActionEnabled){
                        if (DROP_MENU.getDropMenuItems() != null) {
                            checkSelectedTab();
                            if (DROP_MENU.isShowing()) {
                                closeDrop(DROP_MENU);
                            } else {
                                if(DROP_DOWN_CONTAINER!=null){
                                    checkPopups();
                                    showDrop();
                                }else{
                                    System.err.println("ESON NULL POINTER ERROR : DROP_DOWN_CONTAINER NOT SPECIFIED! ");
                                }
                            }
                        } else {
                            checkPopups();
                            resetTab();
                            setSelected(true);
                            SELECTION = label.getText();
                        }
                    }
                }
            });
        }
        
        private void setIconForeground(Color color){
            if(ICON!=null){
                label.setIcon(RENDERER.resizeIcon(ICON.getImage(), TAB_ICON_SIZE, TAB_ICON_SIZE, color));
                label.setIconTextGap(TAB_ICON_TEXT_GAP);
            }
        }
        
        private void showDrop() {
            drop();
            int w = getDropDown().getItemWidth();
            int h = getDropDown().getItemHeight();
            int count = getDropDown().getDropMenuItems().length;
            DROP_MENU.refreshLayout();
            BLUR_PANEL.setSize(DROP_DOWN_CONTAINER.getSize());
            BLUR_PANEL.setBackground(BLUR_PANEL_BACKGROUND);
            DROP_MENU.setSize(w, h * count +5);
            DROP_MENU.setLocation(getX(),DROP_DOWN_Y_LOCATION);
            DROP_MENU.setOpaque(false);
            if (DROP_DOWN_CONTAINER instanceof JPanel pane) {
                pane.add(BLUR_PANEL,0);
                BLUR_PANEL.setVisible(true);
                panelUpdate(BLUR_PANEL);
                pane.add(DROP_MENU,0);
                DROP_MENU.setVisible(true);
                panelUpdate(pane);
            }
            DROP_MENU.animateDrop();
        }
        
        private void selected(){
            setOpaque(true);
            label.setOpaque(false);
            setBackground(SELECTED_BACKGROUND);
            label.setForeground(SELECTED_FOREGROUND);
            label.setFont(SELECTED_FONT);
            separator.setVisible(LINE_SHOWING);
            separator.setBackground(SELECTED_FOREGROUND);
            separator.setForeground(SELECTED_FOREGROUND);
            setIconForeground(ICON_SELECTED_COLOR);
            int i = 0;
            for(String tab:TAB_NAMES){
                if(tab.equals(getSelectedTab())){
                    setSelectedIndex(i);
                    break;
                }
                i++;
            }
            if(DROP_MENU.getDropMenuItems()==null){
                tabAction();
            }
        }
        
        private void hover(){
            if(!isSelected){
                setOpaque(false);
                label.setOpaque(true);
                label.setBackground(HOVER_BACKGROUND);
                label.setForeground(HOVER_FOREGROUND);
                label.setFont(NORMAL_FONT);
                setIconForeground(ICON_HOVER_COLOR);
                showSeparator();
            }
        }
        
        private void exited(){
            if(!isSelected){
                setOpaque(false);
                label.setOpaque(true);
                setBackground(SELECTED_BACKGROUND);
                label.setBackground(NORMAL_BACKGROUND);
                label.setForeground(NORMAL_FOREGROUND);
                label.setFont(NORMAL_FONT);
                setIconForeground(ICON_NORMAL_COLOR);
                showSeparator();
            }
        }
        
        private void pressed(){
            if(!isSelected){
                setOpaque(false);
                label.setOpaque(true);
                label.setBackground(PRESSED_BACKGROUND);
                label.setForeground(PRESSED_FOREGROUND);
                label.setFont(NORMAL_FONT);
                setIconForeground(ICON_PRESSED_COLOR);
                showSeparator();
            }
        }
        
        private void drop(){
            setOpaque(false);
            label.setOpaque(true);
            label.setBackground(DROP_BACKGROUND);
            label.setForeground(DROP_FOREGROUND);
            label.setFont(NORMAL_FONT);
            setIconForeground(ICON_DROP_COLOR);
            showSeparator();
        }
        
        private void showSeparator(){
            separator.setVisible(LINE_SHOWING);
            separator.setBackground(new java.awt.Color(200,200,200));
            separator.setForeground(new java.awt.Color(210,210,210));
        }
        
        public void setSelected(boolean flag){
            isSelected = flag;
            setOpaque(flag);
            if(flag){
                SELECTED_TAB = label.getText();
                selected();
            }
            repaint();
        }
        
        public boolean isSelected(){
            return isSelected;
        }
        
        public javax.swing.JLabel getLabel(){
            return label;
        }
        
        public javax.swing.JSeparator getSeparator(){
            return separator;
        }
        
        public String getTabName(){
            return label.getText();
        }
        
        public void addDropDown(String[] items, int width, int height){
            DROP_MENU.createDropDown(INDEX, items, width, height);
        }
        
        public void addDropDown(String[] items){
            DROP_MENU.createDropDown(INDEX, items);
        }
        
    }
    
    private class DropDown extends JPanel{
        
        private String DROP_MENU_ITEMS[];
        private int DROP_ITEM_WIDTH = 165;
        private int DROP_ITEM_HEIGHT = 35;
        private int COUNTER = 0;
        private JLabel DROP_LABELS[];
        private int TAB_INDEX = 0;
        
        public DropDown(){
            DROP_MENU_ITEMS = null;
            DROP_ITEM_WIDTH = 165;
            DROP_ITEM_HEIGHT = 35;
        }
        
        public String[] getDropMenuItems(){
            return DROP_MENU_ITEMS;
        }
        
        public int getItemWidth(){
            return DROP_ITEM_WIDTH;
        }
        
        public int getItemHeight(){
            return DROP_ITEM_HEIGHT;
        }
        
        public void createDropDown(int index, String[] items){
            TAB_INDEX = index;
            DROP_MENU_ITEMS = items;
            refreshLayout();
        }
        
        public void createDropDown(int index, String[] items, int width, int height){
            TAB_INDEX = index;
            DROP_MENU_ITEMS = items;
            DROP_ITEM_WIDTH = width;
            DROP_ITEM_HEIGHT = height;
            refreshLayout();
        }
        
        public void animateDrop(){
            COUNTER = 0;
//            setBorder(esonUI.shadowBorder(0, 1, 1, 0, DROP_BORDER_N_COLOR));
            for(JLabel l:DROP_LABELS){
                l.setVisible(false);
            }
            new javax.swing.Timer(17, (ActionEvent e) -> {
                if(COUNTER<DROP_LABELS.length){
                    DROP_LABELS[COUNTER].setVisible(true);
                }else{
                    ((javax.swing.Timer) e.getSource()).stop();
                }
                COUNTER++;
            }).start();
        }
        
        public void refreshLayout(){
            removeAll();
            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
            this.setLayout(layout);
            SequentialGroup sgroup = layout.createSequentialGroup();
            GroupLayout.ParallelGroup hgroup = layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING);
            GroupLayout.ParallelGroup vgroup = layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING);
            vgroup.addGroup(sgroup);
            int i = 0;
            DROP_LABELS = new JLabel[DROP_MENU_ITEMS.length];
            for(String item:DROP_MENU_ITEMS){
                DROP_LABELS[i] = new JLabel(item);
                initLabel(DROP_LABELS[i],i);
                hgroup.addComponent(DROP_LABELS[i], javax.swing.GroupLayout.DEFAULT_SIZE, DROP_ITEM_WIDTH, Short.MAX_VALUE);
                sgroup.addComponent(DROP_LABELS[i], javax.swing.GroupLayout.PREFERRED_SIZE, DROP_ITEM_HEIGHT, javax.swing.GroupLayout.PREFERRED_SIZE);
                sgroup.addGap(0, 0, 0);
                i++;
            }      
            layout.setHorizontalGroup(hgroup);
            layout.setVerticalGroup(vgroup);
        }
        
        private void initLabel(JLabel label, int i){
            label.setOpaque(true);
            label.setFont(DROP_DOWN_FONT);
            label.setHorizontalAlignment(DROP_TEXT_ALIGNMENT);
            label.setIconTextGap(DROP_ICON_TEXT_GAP);
            dropExited(label,i);
            label.addMouseListener(new MouseListener(){
                @Override public void mouseClicked(MouseEvent e) { /*Do Nothing*/}
                @Override public void mouseExited(MouseEvent e) { dropExited(label,i); }
                @Override public void mouseEntered(MouseEvent e) { dropHover(label,i); }
                @Override public void mousePressed(MouseEvent e) {  dropPressed(label,i); }
                @Override public void mouseReleased(MouseEvent e) {
                    resetTab();
                    TAB_COLUMNS[TAB_INDEX].setSelected(true);
                    checkPopups();
                    dropExited(label,i);
                    SELECTION = label.getText();
                    tabAction();
                }
            });
        }
        
        private void setLabelBorder(int i, Color color){
            DROP_LABELS[i].setBorder(javax.swing.BorderFactory.createCompoundBorder(
                    javax.swing.BorderFactory.createMatteBorder(i == 0 ? 1 : 0, 1, 1, 1, color),
                    javax.swing.BorderFactory.createEmptyBorder(0, 12, 0, 0)));
        }
        
        private void setIcon(JLabel label, Color color){
            label.setIcon(RENDERER.resizeIcon(DROP_ITEM_ICON.getImage(), DROP_ICON_SIZE, DROP_ICON_SIZE, color));
        }
        
        private void dropExited(JLabel label, int i){
            label.setBackground(DROP_N_BACKGROUND);
            label.setForeground(DROP_N_FOREGROUND);
            setIcon(label,DROP_N_FOREGROUND);
            setLabelBorder(i,DROP_BORDER_N_COLOR);
        }
        
        private void dropHover(JLabel label, int i){
            label.setBackground(DROP_H_BACKGROUND);
            label.setForeground(DROP_H_FOREGROUND);
            setIcon(label,DROP_H_FOREGROUND);
            setLabelBorder(i,DROP_BORDER_H_COLOR);
        }
        
        private void dropPressed(JLabel label, int i){
            label.setBackground(DROP_P_BACKGROUND);
            label.setForeground(DROP_P_FOREGROUND);
            setIcon(label,DROP_P_FOREGROUND);
            setLabelBorder(i,DROP_BORDER_P_COLOR);
        }
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setOpaque(false);
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                formFocusLost(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 856, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 24, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusLost
        checkPopups();
    }//GEN-LAST:event_formFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
