/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eson.component.table;

import eson.component.EsonSearch;
import eson.core.util.ImageRenderer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 *
 * @author Codey Hyacinth
 */
public class EsonTableHolder extends Exception{
    
    protected EsonTableHolder(EsonTable table){
        this.table = table;
    }
    
    protected List<TableViewWorker> viewWorkers = new ArrayList();
    protected void viewPage(List<Object[]> values,int pageNumber){
        pageNumber--;
        resetRows();
        for(TableViewWorker w:viewWorkers){
            w.stop(); w.cancel(true);
        }
        viewWorkers.clear();
        TableViewWorker worker = new TableViewWorker(values,pageNumber*table.VISIBLE_ROW_COUNT);
        worker.execute();
        viewWorkers.add(worker);
        StackTraceElement[] stackTrace = super.getStackTrace();
        StackTraceElement[] newArray = Arrays.stream(stackTrace).filter(i -> i.getMethodName().equals("setValueAt"))
                .toArray(StackTraceElement[]::new);
        super.setStackTrace(newArray);
    }
    
    protected class TableViewWorker extends SwingWorker{
        protected boolean STOP = false;
        protected List<Object[]> data;      
        protected int INDEX = 0;
        protected TableViewWorker(List<Object[]> d,int startIndex){
            INDEX = startIndex; data = d;
        }
        protected void stop(){ STOP = true; }       
        @Override protected Object doInBackground() throws Exception {
            int count = 0;
            while (INDEX< data.size() && count<table.VISIBLE_ROW_COUNT) {
                if(STOP){
                    resetRows(); break;
                }
                insertRow(data.get(INDEX));
                INDEX++; count++;
            }
            table.refreshViewPort();
            return 0;
        }
    }
    
    protected String SEARCH_KEY = "";
    protected List<SearchWorker> searchWorkers = new ArrayList();
    protected void search(String KEY){
        if(!KEY.trim().equals(SEARCH_KEY)){
            resetRows();
            SEARCH_KEY = KEY;
            for (SearchWorker w : searchWorkers) {
                w.stop(); w.cancel(true);
            }
            searchWorkers.clear();
            SearchWorker worker = new SearchWorker(KEY.trim().equals(""),table.VALUES);
            worker.execute();
            searchWorkers.add(worker);
        }
    }
    
    protected class SearchWorker extends SwingWorker{
        protected boolean STOP = false;
        protected boolean isRepopulate = false;
        protected List<Object[]> values = null;
        protected SearchWorker(boolean isRepopulate, List<Object[]> values){
            this.isRepopulate = isRepopulate; this.values = values;
        }
        protected void stop(){ STOP = true; }      
        @Override protected Object doInBackground() throws Exception {
            String key = SEARCH_KEY.toUpperCase().trim();
            List<Object[]> searchResult = table.VALUES.parallelStream()
                .filter((element) -> element[1].toString().toUpperCase().trim().contains(key))
                .collect(Collectors.toList());
            table.setActionEnabled(false);
            for(Object[] obj:searchResult){
                if(STOP){ resetRows(); break; }
                if(!insertRow(obj)){break;}; 
            }
            table.refreshViewPort();
            table.scrollToTop();
            table.CURRENT_VALUES = searchResult;
            table.FOOTER.setValue(isRepopulate?"TOTAL ROW":"SEARCHED",searchResult, table.VISIBLE_ROW_COUNT);
            table.setActionEnabled(true);
            return 0;
        }
    }
    
    protected List<TableSorter> tableSorters = new ArrayList();
    protected void sort(int columnIndex, boolean ascending){
        changeScrollSize(table.SCROLL_MIN_SIZE);
        EsonSearch esonSearch = table.getEsonSearch();
        if(esonSearch!=null){if(!esonSearch.getText().isBlank()){esonSearch.clear();}}
        if(!table.VALUES.isEmpty()){
            for(TableSorter w:tableSorters){
                w.stop(); w.cancel(true);
            }
            tableSorters.clear();
            TableSorter sorter = new TableSorter(columnIndex,ascending);
            tableSorters.add(sorter);
            sorter.execute();
        }
    }
    
    protected class TableSorter extends SwingWorker{
        protected boolean STOP = false, ascending = false;
        protected int columnIndex = 0;
        protected TableSorter(int index,boolean descending){
            columnIndex = index; ascending = descending;
        }
        protected void stop(){ STOP = true; }   
        @Override protected Object doInBackground() throws Exception {
            if(STOP){ resetRows(); }
            showLoading("SORTING TABLE ROWS : COLUMN \""+table.HEADER.getColumn(columnIndex).getValue().toString().toUpperCase()+"\" "+(ascending?"ASCENDING":"DESCENDING"));
            if (!ascending) {
                Collections.sort(table.VALUES, Collections.reverseOrder((Object[] a, Object[] b) -> compareInstances(a[columnIndex],b[columnIndex])));
            } else {
                Collections.sort(table.VALUES, (Object[] a, Object[] b) -> compareInstances(a[columnIndex],b[columnIndex]));
            }
            table.repopulate();
            closeLoading();
            return 0;
        }
    }  
    
    protected void showLoading(String message){
        table.setActionEnabled(false);
        if(!isLoading){
            table.setBodyBlurShown(true);
            isLoading = false;
            table.loadingMessage.setText(message);
            table.esonProjectCover.setSize(table.bodyPane.getSize());
            table.esonProjectCover.setLocation(table.bodyPane.getLocation());
            table.add(table.esonProjectCover, 0);
            table.esonProjectCover.setVisible(true);
            table.revalidate(); table.repaint();
            isLoading = true;
            table.loading.setText(loadInfo[loadInfo.length-1]);
            loadingCounter = 0;
            loadingTimer.start();
        }
    }
    
    protected void closeLoading(){
        table.setActionEnabled(true);
        isLoading = false;
        if(table.esonProjectCover.isShowing()){
            table.remove(table.esonProjectCover);
            table.repaint();
            table.setBodyBlurShown(false);
        }
    }
    
    protected boolean isLoading = false;
    protected int loadingCounter = 0;
    protected String[] loadInfo = new String[]{"● ◌ ◌","● ● ◌","● ● ●","◌ ● ●","◌ ◌ ●","◌ ◌ ◌"};
    protected javax.swing.Timer loadingTimer = new javax.swing.Timer(300, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(isLoading){
                table.loading.setText(loadInfo[loadingCounter]);
                loadingCounter++;
                if(loadingCounter==loadInfo.length){
                    loadingCounter = 0;
                }
            }else{((Timer)e.getSource()).stop();}
        }
    });
    
    protected int compareInstances(Object a, Object b){
        if(a instanceof Integer){
            return Integer.compare((int)a, (int)b);
        }else if(a instanceof Double){
            return Double.compare((double)a, (double)b);
        }else if(a instanceof Float){
            return Float.compare((float)a, (float)b);
        }else if(a instanceof Date date){
            return date.compareTo((Date)b);
        }else{
            return a.toString().compareToIgnoreCase(b.toString());
        }
    }
    
    protected void loadDefaultSettings(){
        table.VERTICAL_SCROLL_SHOWN = false;
        table.setOpaque(false);
        table.viewport.setOpaque(true);
        table.scroll.setOpaque(true);
        table.scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        table.HEADER_HEIGHT = 25;
        table.FOOTER_HEIGHT = 25;
        table.ROW_HEIGHT = 25;
        table.HEADER_BACKGROUND = Color.LIGHT_GRAY;
        table.HEADER_FOREGROUND = Color.BLACK;
        table.FOOTER_BACKGROUND = table.HEADER_BACKGROUND;
        table.FOOTER_FOREGROUND = table.HEADER_FOREGROUND;
        table.ROW_BACKGROUNDS = new Color[]{new Color(240,240,245),new Color(220,220,225)};
        table.ROW_FOREGROUNDS = new Color[]{Color.BLACK};
        table.ROW_HOVER_BACKGROUND = new Color(152,193,254);
        table.ROW_HOVER_FOREGROUND = Color.BLACK;
        table.ROW_SELECTION_BACKGROUND = new Color(13,110,253);
        table.ROW_SELECTION_FOREGROUND = Color.WHITE;
        table.SCROLL_BACKGROUND = new Color(240,240,245);
        table.SCROLL_FOREGROUND = table.ROW_SELECTION_BACKGROUND;
        table.GRID_COLOR = new Color(240,240,240);
        table.setBodyColor(new Color(240,240,245));
        table.HEADER_FONT = new Font("Arial", Font.PLAIN, 12);
        table.ROW_FONT = new Font("Arial", Font.PLAIN, 12);
        table.FOOTER_FONT = new Font("Arial", Font.PLAIN, 12);
        table.addColumn("esonColumn1", 100, true,JLabel.CENTER);
        table.addColumn("esonColumn2", 100, true,JLabel.CENTER);
        table.addColumn("esonColumn3", 100, true,JLabel.CENTER);
        table.addColumn("esonColumn4", 100, true,JLabel.CENTER);
        table.addColumn("esonColumn5", 100, true,JLabel.CENTER);
        initScrollUI();
        initViewPort();
        table.esonProjectCover.setOpaque(false);
        table.setActionEnabled(true);
        table.FOOTER = new EsonTableFooter(table,table.FOOTER_HEIGHT);
        refreshFooter();
    }  
    
    protected void initScrollUI(){
        table.scroll.getVerticalScrollBar().addMouseListener(new MouseListener() {
            int current = 0;
            javax.swing.Timer timer = new javax.swing.Timer(5, (ActionEvent e) -> {
                    if(current<500){
                        current++;
                    }else{
                        changeScrollSize(table.SCROLL_MIN_SIZE);
                        ((javax.swing.Timer)e.getSource()).stop();
                    }
                });
            @Override public void mouseClicked(MouseEvent e){}
            @Override public void mousePressed(MouseEvent e){}
            @Override public void mouseReleased(MouseEvent e){}
            @Override public void mouseEntered(MouseEvent e){
                if(table.isActionEnabled()){
                    changeScrollSize(table.SCROLL_MAX_SIZE); 
                    if(timer.isRunning()){timer.stop();}
                }
            }
            @Override public void mouseExited(MouseEvent e){
                if(table.isActionEnabled()){
                    current=0;timer.start();
                }
            }
            
        });
    }
    
    protected void changeScrollSize(int size){
        try{
            table.HEADER.setMarginSize(size<=table.SCROLL_MIN_SIZE?(table.VERTICAL_SCROLL_SHOWN?size:0):size);
            UIManager.put("ScrollBar.width", size);
            Color BACKGROUND = table.SCROLL_BACKGROUND == null ? table.ROW_BACKGROUNDS[0] : table.SCROLL_BACKGROUND;
            Color FOREGROUND = table.SCROLL_FOREGROUND == null ? table.ROW_FOREGROUNDS[0] : table.SCROLL_FOREGROUND;
            UIManager.put("ScrollBar.thumb", table.scroll.getBackground());
            UIManager.put("ScrollBar.thumbDarkShadow", FOREGROUND);
            table.scroll.setBackground(BACKGROUND);
            table.scroll.getVerticalScrollBar().updateUI();
            table.scroll.getVerticalScrollBar().getParent().setBackground(BACKGROUND);
            table.scroll.getVerticalScrollBar().setUI(getUpdatedScrollBarUI(FOREGROUND, BACKGROUND));
            table.scroll.getHorizontalScrollBar().updateUI();
            table.scroll.getHorizontalScrollBar().getParent().setBackground(BACKGROUND);
            table.scroll.getHorizontalScrollBar().setUI(getUpdatedScrollBarUI(FOREGROUND, BACKGROUND));
        }catch(Exception ex){}
    }
    
    protected BasicScrollBarUI getUpdatedScrollBarUI(Color foreground, Color background){ 
        return new BasicScrollBarUI(){
            @Override protected void configureScrollBarColors(){ this.thumbColor = foreground; this.trackColor = background; }
            @Override protected JButton createDecreaseButton(int Orientation){ return getScrollButton(null,background,foreground); }
            @Override protected JButton createIncreaseButton(int Orientation){ return getScrollButton(null,background,foreground); }
        };
    } 
    
    protected JButton getScrollButton(ImageIcon icon, Color background, Color foreground){
        JButton button = new JButton();
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setText("O");
        button.setBackground(background);
        button.setForeground(foreground);
        button.addMouseListener(new MouseListener(){
            @Override public void mouseClicked(MouseEvent e) { }
            @Override public void mousePressed(MouseEvent e) { button.setBackground(button.getForeground()); button.setForeground(button.getBackground());}
            @Override public void mouseReleased(MouseEvent e) { button.setBackground(button.getForeground()); button.setForeground(button.getBackground()); }
            @Override public void mouseEntered(MouseEvent e) { }
            @Override public void mouseExited(MouseEvent e) { }
        });
        return button;
    }
    
    /**GROUP LAYOUT*/
    protected void refreshHeader(){
        table.header.removeAll();
        table.HEADER = new EsonTableRow(table,table.COLUMNS,table.HEADER_HEIGHT,true);
        if(table.HEADER_FONT!=null){
            table.HEADER.setRowFont(table.HEADER_FONT);
        }
        table.HEADER.setGridColor(table.GRID_COLOR);
        table.HEADER.setVerticalGridShown(table.GRID_SHOWN);
        table.HEADER.setNormalBackground(table.HEADER_BACKGROUND);
        table.HEADER.setNormalForeground(table.HEADER_FOREGROUND); 
        table.HEADER.setValues(table.HEADER_COLUMNS);
        table.HEADER.setCornerRadius(table.HEADER_RADIUS);
        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(table.header);
        table.header.setLayout(headerLayout);
        table.header.setSize(table.getWidth(),table.HEADER_HEIGHT);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(table.HEADER, javax.swing.GroupLayout.DEFAULT_SIZE, table.getWidth(), Short.MAX_VALUE)
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(table.HEADER, javax.swing.GroupLayout.DEFAULT_SIZE, table.HEADER_HEIGHT, Short.MAX_VALUE)
        );
        table.header.revalidate();
        table.header.repaint();
        changeScrollSize(table.SCROLL_MIN_SIZE);
    }
    
    protected void refreshFooter(){
        table.footer.removeAll();
        table.FOOTER.setValue("TOTAL ROW",table.VALUES, table.VISIBLE_ROW_COUNT);
        javax.swing.GroupLayout footerLayout = new javax.swing.GroupLayout(table.footer);
        table.footer.setLayout(footerLayout);
        table.footer.setSize(table.getWidth(),table.FOOTER_HEIGHT);
        footerLayout.setHorizontalGroup(
            footerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(table.FOOTER, javax.swing.GroupLayout.DEFAULT_SIZE, table.getWidth(), Short.MAX_VALUE)
        );
        footerLayout.setVerticalGroup(
            footerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(table.FOOTER, javax.swing.GroupLayout.DEFAULT_SIZE, table.FOOTER_HEIGHT, Short.MAX_VALUE)
        );
        table.footer.revalidate();
        table.footer.repaint();
    }
    
    protected void showColumn(int indexes[]){
        for(int i=0;i<table.COLUMNS.size();i++){
            Object[] obj = table.COLUMNS.get(i);
            boolean flag = false;
            for(int a:indexes){
                if(i==a){
                    flag = true; break;
                }
            }
            table.COLUMNS.set(i, new Object[]{obj[0],obj[1],obj[2],obj[3],flag});
        }
        refreshHeader();
        viewPage(!table.CURRENT_VALUES.isEmpty()?table.CURRENT_VALUES:table.VALUES,table.FOOTER.getCurrentPage());
    }
    
    protected void showColumn(String columns[]){
        for(int i=0;i<table.COLUMNS.size();i++){
            Object[] obj = table.COLUMNS.get(i);
            boolean flag = false;
            for(String name:columns){
                if(name.toLowerCase().trim().equals(obj[0].toString().toLowerCase().trim())){
                    flag = true; break;
                }
            }
            table.COLUMNS.set(i, new Object[]{obj[0],obj[1],obj[2],obj[3],flag});
        }
        refreshHeader();
        viewPage(!table.CURRENT_VALUES.isEmpty()?table.CURRENT_VALUES:table.VALUES,table.FOOTER.getCurrentPage());
    }
    
    protected void refreshRowColors(){
        int i = 0;
        for(EsonTableRow row:table.ROWS){
            row.setNormalBackground(getRowColor(i,table.ROW_BACKGROUNDS));
            row.setNormalForeground(getRowColor(i,table.ROW_FOREGROUNDS));
            i++;
        }
    }
    
    protected Color getRowColor(int index,Color[] color){
        return index<color.length?color[index]:color[index % color.length];
    }
    
    protected boolean insertRow(Object[] values){
        if(table.ROW_COUNTER<table.VISIBLE_ROW_COUNT){
            EsonTableRow row = new EsonTableRow(table, table.COLUMNS,  table.ROW_HEIGHT, false);
            if(table.ROW_FONT!=null){
                row.setRowFont(table.ROW_FONT);
            }
            row.setGridColor(table.GRID_COLOR);
            row.setVerticalGridShown(table.GRID_SHOWN);
            row.setHoverBackground(table.ROW_HOVER_BACKGROUND);
            row.setHoverForeground(table.ROW_HOVER_FOREGROUND);
            row.setSelectedBackground(table.ROW_SELECTION_BACKGROUND);
            row.setSelectedForeground(table.ROW_SELECTION_FOREGROUND);
            row.setValues(values);
            row.setCornerRadius(table.ROW_CORNER_RADIUS);
            row.setNormalBackground(getRowColor(table.ROWS.size(),table.ROW_BACKGROUNDS));
            row.setNormalForeground(getRowColor(table.ROWS.size(),table.ROW_FOREGROUNDS));
            table.ROWS.add(row);  
            table.viewport.add(row);
            table.ROW_COUNTER++;
            return true;
        }else{ return false; }
    }
    
    protected void initViewPort(){
        table.viewport.setLayout(new javax.swing.BoxLayout(table.viewport, javax.swing.BoxLayout.PAGE_AXIS));
        table.scroll.getViewport().addChangeListener((ChangeEvent e) -> {
            table.VERTICAL_SCROLL_SHOWN = table.scroll.getVerticalScrollBar().isVisible();
            if(table.VERTICAL_SCROLL_SHOWN && !table.HEADER_MIN_UPDATED && !table.VALUES.isEmpty()){
                table.HEADER.setMarginSize(table.SCROLL_MIN_SIZE);
                table.HEADER_MIN_UPDATED = true;
            }else if(!table.VERTICAL_SCROLL_SHOWN && table.HEADER_MIN_UPDATED && !table.VALUES.isEmpty()){
                table.HEADER.setMarginSize(0);
                table.HEADER_MIN_UPDATED = false;
            }
        });
    }
    
    protected void initColumnSizes(String name, int size, boolean resizable){
        table.HEADER_COLUMNS.add(name);
        table.COLUMN_WIDTHS.add(size);
        table.RESIZABLE_COLUMNS.add(resizable);
        table.tableHolder.refreshHeader();
    }
    
    protected void resetRows(){
        table.ROW_COUNTER = 0;
        table.ROWS.clear();
        table.viewport.removeAll();
    }
    
    protected void clearRows(){
        table.ROW_COUNTER = 0;
        table.VALUES.clear();
        table.ROWS.clear();
        table.viewport.removeAll();
        table.FOOTER.setValue("TOTAL ROW",table.VALUES, table.VISIBLE_ROW_COUNT);
    }
    
    protected void clearColumns(){
        table.COLUMNS.clear();
        table.HEADER_COLUMNS.clear();
        table.COLUMN_WIDTHS.clear();
        table.RESIZABLE_COLUMNS.clear();
        table.tableHolder.refreshHeader();
    }
    
    protected int getSelectedRow(){
        int i=0,retval=-1;
        for(EsonTableRow t : table.ROWS) {
           if(t.isSelected()){
               retval = i;
               break;
           }
           i++;
        }
        return retval;
    }
    
    protected boolean hasSelectedRow(){
        int i=0;
        boolean retval=false;
        for(EsonTableRow t : table.ROWS) {
           if(t.isSelected()){
               retval = true;
               break;
           }
           i++;
        }
        return retval;
    }
    
    protected void removeRow(int index){
        table.viewport.remove(table.ROWS.get(index));
        table.viewport.revalidate();
        table.viewport.updateUI();
        table.viewport.repaint();
        table.ROWS.remove(index);
        refreshRowColors();
    }
    
    protected void updateBodyColor(){
        table.viewport.setBackground(table.BODY_COLOR);
        table.scroll.setBackground(table.BODY_COLOR);
        table.setBackground(table.BODY_COLOR);
        table.repaint();
    }
    
    protected void updateLoadingColor(){
        Image image = new ImageIcon(getClass().getResource("/json/asidera/images/eson_project_logo_alt.png")).getImage();
        ImageRenderer RENDERER = new ImageRenderer();
        table.loadingIcon.setIcon(RENDERER.resizeIcon(image, 257, 120, table.SCROLL_FOREGROUND));
    }
    
    protected void updateFooterHeight(){
        table.footer.setSize(table.getWidth(),table.FOOTER_HEIGHT);
        table.footer.setPreferredSize(new Dimension(table.getWidth(),table.FOOTER_HEIGHT));
        table.footer.repaint();
        table.FOOTER = new EsonTableFooter(table,table.FOOTER_HEIGHT);
        refreshFooter();
    }
    
    protected void updateHeaderHeight(){
        table.header.setSize(table.getWidth(),table.HEADER_HEIGHT);
        table.header.setPreferredSize(new Dimension(table.getWidth(),table.HEADER_HEIGHT));
        refreshHeader();
    }
    
    protected void setValueAt(Object object, int rowIndex, int column){
        table.ROWS.get(rowIndex).getColumn(column).setValue(object);
    }
    
    protected Object getValueAt(int rowIndex, int column){
        return table.ROWS.get(rowIndex).getColumn(column).getValue();
    }
    
    protected EsonTable table = null;
}
