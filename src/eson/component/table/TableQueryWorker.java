/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eson.component.table;

import eson.core.util.ResultSQL;
import eson.core.util.StringUtils;
import java.sql.Statement;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import eson.component.EsonProgress;

/**
 *
 * @author HP-QXO8AV
 */
public class TableQueryWorker extends SwingWorker<Integer, String> {
    
    private static void failIfInterrupted() throws InterruptedException{
        if(Thread.currentThread().isInterrupted()){
            throw new InterruptedException("Data Query Interrupted");
        }
    }
    
    public TableQueryWorker(EsonTable esonTable, Statement st, String sqlQuery, String columnNames[], boolean showCounter){
        this.esonTable = esonTable;
        isEsonTable = true;
        setupValues(progress,st,sqlQuery,columnNames, showCounter);
    }
    
    private void setupValues(EsonProgress progress, Statement st, String sqlQuery, String columnNames[], boolean showCounter){
        this.columnNames = columnNames;
        this.progress = progress;
        this.showCounter = showCounter;
        splitter = " : ";
        stringUtils = new StringUtils();
        STATEMENT = st;
        SQL_QUERY = sqlQuery;
    }
    
    public void setColumns(String columnNames[]){
        this.columnNames = columnNames;
    }
    
    public void setTable(JTable table){
        model = (DefaultTableModel)table.getModel();
    }
    
    public void setProgressBar(EsonProgress progress){
        this.progress = progress;
    }
    
    public void setResultSQL(ResultSQL rs){
        resultQuery = rs;
    }
    
    public void setSplitter(String splitter){
        this.splitter = splitter;
    }

    @Override
    protected Integer doInBackground() throws Exception {
        if(!isEsonTable){
            loadJTable();
        }else{
            loadEsonTable();
        }
        return 1;
    }
    
    private void loadJTable() throws Exception{
        resultQuery = new ResultSQL(STATEMENT,SQL_QUERY);
        failIfInterrupted();
        int i = 0, maxSize = 0;
        if(showCounter){ maxSize = resultQuery.getRowCount(); }
        while(resultQuery.next()){
            failIfInterrupted();
            String rowString = "";
            for(String string:columnNames){
                rowString += resultQuery.getString(string)+splitter;
            }
            if(showCounter){
                progress.setValue((i+1)*100/maxSize);
                progress.setString(progress.getValue()+"%  -  ("+(i+1)+" out of "+maxSize+")");
            }
            if(i==0){
                model.setRowCount(0);
            }
            i++;
            model.addRow(rowString.split(splitter));
        }
        resultQuery.close();
    }
    
    private void loadEsonTable() throws Exception{
        esonTable.showLoading("FETCHING DATA FROM SQL SERVER");
        resultQuery = new ResultSQL(STATEMENT, SQL_QUERY);
        failIfInterrupted();
        int i = 0, maxSize = 0;
        if(showCounter){ maxSize = resultQuery.getRowCount(); }
        while(resultQuery.next()){
            i++;
            if(showCounter){
                esonTable.setLoadingText("LOADING DATA FROM SQL SERVER [ "+stringUtils.formatDecimal("###,###", i)+" of "+stringUtils.formatDecimal("###,###", maxSize)+" ]");
            }
            failIfInterrupted();    
            String rowString = "";                  
            for(String string:columnNames){
                rowString += resultQuery.getString(string)+splitter;
            }
            if(i==1){
                if(!showCounter){esonTable.setLoadingText("LOADING TABLE ROWS");}
                esonTable.clearRows();
            }
            esonTable.addRow(rowString.split(splitter));
        }
        resultQuery.close();
        esonTable.scrollToTop();
        esonTable.getFooter().setValue("TOTAL ROW", esonTable.getValues(), esonTable.getMaximumVisibleRowCount());
        esonTable.closeLoading();
    }
        
    private EsonProgress progress = null;
    private DefaultTableModel model = null;
    private String splitter = " : ";
    private String columnNames[] = null;
    private EsonTable esonTable;
    private boolean isEsonTable = false;
    private boolean showCounter;
    private StringUtils stringUtils;
    ResultSQL resultQuery = null;
    Statement STATEMENT = null;
    String SQL_QUERY = "";
}
