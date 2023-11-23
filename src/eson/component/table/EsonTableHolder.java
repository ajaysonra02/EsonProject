/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eson.component.table;

import eson.component.EsonSearch;
import eson.component.util.CustomScrollBarUI;
import eson.core.util.DataConnection;
import eson.core.util.ImageRenderer;
import eson.core.util.LoadingAnimation;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import static javax.swing.SwingWorker.StateValue.DONE;
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
    
    protected List<TableViewWorker> viewWorkers = new ArrayList<>();
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
    protected List<SearchWorker> searchWorkers = new ArrayList<>();
    protected void search(String KEY){
        if(!KEY.trim().equals(SEARCH_KEY)){
            resetRows();
            SEARCH_KEY = KEY.trim();
            if(KEY.equals("")){
                repopulate();
            }else{
                startSearch();
            }
        }
    }
    
    protected List<TablePopulator> tablePopulator = new ArrayList<>();
    protected void repopulate(){
        resetRows();
        for (TablePopulator w : tablePopulator) {
            w.stop(); w.cancel(true);
        }
        tablePopulator.clear();
        TablePopulator worker = new TablePopulator();
        tablePopulator.add(worker);
        worker.execute();
    }
    
    protected class TablePopulator extends SwingWorker{
        protected boolean STOP = false;
        protected void stop(){ STOP = true; }    
        @Override protected Object doInBackground() throws Exception {
            table.setActionEnabled(false);
            for(Object[] obj:table.VALUES){ 
                if(STOP){ resetRows(); break; }
                if(!insertRow(obj)){break;}
            }
            table.refreshViewPort();
            table.scrollToTop();
            table.FOOTER.setValue("TOTAL ROW",table.VALUES, table.VISIBLE_ROW_COUNT);
            table.setActionEnabled(true);   
            return 0;
        }
    }
    
    private void startSearch(){
        for (SearchWorker w : searchWorkers) {
            w.stop(); w.cancel(true);
        }
        searchWorkers.clear();
        SearchWorker worker = new SearchWorker(SEARCH_KEY.equals(""), table.VALUES);
        searchWorkers.add(worker);
        worker.execute();
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
            table.setActionEnabled(false);
            String key = SEARCH_KEY.toUpperCase();
            List<Object[]> searchResult = table.VALUES.parallelStream()
                .filter((element) -> element[1].toString().toUpperCase().trim().contains(key))
                .collect(Collectors.toList());
            for(Object[] obj:searchResult){
                if(STOP){ resetRows(); break; }
                if(!insertRow(obj)){break;}
            }
            table.refreshViewPort();
            table.scrollToTop();
            table.CURRENT_VALUES = searchResult;
            table.FOOTER.setValue(isRepopulate?"TOTAL ROW":"SEARCHED",searchResult, table.VISIBLE_ROW_COUNT);
            table.setActionEnabled(true);
            return 0;
        }
    }
    
    protected List<TableSorter> tableSorters = new ArrayList<>();
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
            table.refresh();
            closeLoading();
            return 0;
        }
    }  
    
    protected void showLoading(String message){
        table.setActionEnabled(false);
        if(!loadingHolder.isLoading()){
            table.setBodyBlurShown(true);
            table.loadingMessage.setText(message);
            table.loadingPanel.setSize(table.bodyPane.getSize());
            table.loadingPanel.setLocation(table.bodyPane.getLocation());
            table.add(table.loadingPanel, 0);
            table.loadingPanel.setVisible(true);
            table.revalidate(); table.repaint();
            loadingHolder.simpleLoading(table.loading, 3);
        }
    }
    
    protected void closeLoading(){
        table.setActionEnabled(true);
        loadingHolder.stopLoading();
        table.remove(table.loadingPanel);
        table.repaint();
        table.setBodyBlurShown(false);
    }
    
    protected void showPreparing(String txt){
        table.setActionEnabled(false);
        table.prepareMessage.setText(txt);
        if(!loadingHolder.isLoading()){
            table.preparePanel.setSize(table.bodyPane.getSize());
            table.preparePanel.setLocation(table.bodyPane.getLocation());
            table.add(table.preparePanel, 0);
            table.preparePanel.setVisible(true);
            table.revalidate();
            table.repaint();
            loadingHolder.windowsLoading(table.prepareLoading, table.getRowForegrounds(), 55);
        }
    }
    
    protected void closePreparing(){
        table.setActionEnabled(true);
        loadingHolder.stopLoading();
        table.remove(table.preparePanel);
        table.repaint();
        table.setBodyBlurShown(false);
    }
    
    protected int compareInstances(Object a, Object b){
        if(a instanceof Integer){
            return Integer.compare((int)a, (int)b);
        }else if(a instanceof Double){
            return Double.compare((double)a, (double)b);
        }else if(a instanceof Float){
            return Float.compare((float)a, (float)b);
        }else if(a instanceof Date date){
            return date.compareTo((Date)b);
        }else if(a instanceof String){
            return a.toString().compareToIgnoreCase(b.toString());
        }else{
            return -1;
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
        table.loadingPanel.setOpaque(false);
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
        table.HEADER.setMarginSize(size<=table.SCROLL_MIN_SIZE?(table.VERTICAL_SCROLL_SHOWN?size:0):size);
        Color BACKGROUND = table.SCROLL_BACKGROUND == null ? table.ROW_BACKGROUNDS[0] : table.SCROLL_BACKGROUND;
        Color FOREGROUND = table.SCROLL_FOREGROUND == null ? table.ROW_FOREGROUNDS[0] : table.SCROLL_FOREGROUND;
        table.scroll.setBackground(BACKGROUND);
        setScrollBarModernUI(table.scroll.getVerticalScrollBar(), FOREGROUND, BACKGROUND, size);
        setScrollBarModernUI(table.scroll.getHorizontalScrollBar(), FOREGROUND, BACKGROUND, size);
    }

    private void setScrollBarModernUI(JScrollBar sbar, Color foreground, Color background,int size) {
        sbar.setUI(new CustomScrollBarUI());
        sbar.setPreferredSize(new Dimension(size, size));
        sbar.setForeground(foreground);
        sbar.setBackground(background);
        sbar.getParent().setBackground(background);
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
        table.PREPARED_VALUES.clear();
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
    
    protected void prepareValues(Connection connection, String sqlQuery, String tableColumnNames[]){
        clearRows();
        loadTable(dataConnection.createScrollableStatement(connection),sqlQuery, tableColumnNames, true);
    }
    
    public void loadTable(Connection connection, String sqlQuery, String tableColumnNames[]){
        loadTable(dataConnection.createScrollableStatement(connection),sqlQuery, tableColumnNames, false);
    }
    
    private void loadTable(Statement st, String sqlQuery, String queryTableColumnNames[], boolean isPrepare){
        sqlWorker = new TableQueryWorker(table, st,sqlQuery, queryTableColumnNames, isPrepare);
        sqlWorker.addPropertyChangeListener((PropertyChangeEvent evt) -> {
            switch (evt.getPropertyName()) {
                case "state" -> {
                    switch ((SwingWorker.StateValue) evt.getNewValue()) {
                        case DONE -> {  
                            try {
                                sqlWorker.get();
                            } catch (final CancellationException | InterruptedException | ExecutionException e) {
                                JOptionPane.showMessageDialog(null, "Query Failed!!", "EsonProject", JOptionPane.ERROR_MESSAGE);
                            }
                            sqlWorker = null;
        }}}}});
        sqlWorker.execute();
    }
    
    private TableQueryWorker sqlWorker;
    protected EsonTable table = null;
    private final DataConnection dataConnection = new DataConnection();
    private final LoadingAnimation loadingHolder = new LoadingAnimation();
}
