/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eson.component.table;

import eson.core.util.ResultSQL;
import eson.core.util.StringUtils;
import java.sql.Statement;
import javax.swing.SwingWorker;
import javax.swing.JLabel;

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
    
    public TableQueryWorker(EsonTable esonTable, Statement st, String sqlQuery, String columnNames[], JLabel label){
        this.esonTable = esonTable;
        this.columnNames = columnNames;
        this.label = label;
        STATEMENT = st;
        SQL_QUERY = sqlQuery;
        splitter = " : ";
        isPrepare = false;
    }
    
    public TableQueryWorker(EsonTable esonTable, Statement st, String sqlQuery, String columnNames[], JLabel label, boolean isPrepare){
        this(esonTable,st,sqlQuery,columnNames,label);
        this.isPrepare = isPrepare;
    }

    @Override
    protected Integer doInBackground() throws Exception {
        esonTable.clearRows();
        String txt = "FETCHING DATA FROM SQL SERVER";
        if(!isPrepare){esonTable.showLoading(txt);}else{loadMessage(txt);}
        resultQuery = new ResultSQL(STATEMENT, SQL_QUERY);
        failIfInterrupted();
        int i = 0, maxSize = resultQuery.getRowCount();
        while(resultQuery.next()){
            i++;
            loadMessage("LOADING DATA FROM SQL SERVER [ "+stringUtils.formatDecimal("###,###", i)+" of "+stringUtils.formatDecimal("###,###", maxSize)+" ]");
            failIfInterrupted();    
            String rowString = "";                  
            for(String string:columnNames){
                rowString += resultQuery.getString(string)+splitter;
            }
            if(isPrepare){
                esonTable.addRow(rowString.split(splitter));
            }else{
                esonTable.prepareRowValues(rowString.split(splitter));
            }
        }
        resultQuery.close();
        if(!isPrepare){
            esonTable.scrollToTop();
            esonTable.getFooter().setValue("TOTAL ROW", esonTable.getValues(), esonTable.getMaximumVisibleRowCount());
            esonTable.closeLoading();
        }else{esonTable.pushPreparedValues();}
        return 1;
    }
        
    private void loadMessage(String txt){
        System.out.println(txt);
        if(label!=null){
            label.setText(txt);
        }
    }
        
    private ResultSQL resultQuery = null;
    private Statement STATEMENT = null;
    private String SQL_QUERY = "";
    private String splitter = " : ";
    private String columnNames[] = null;
    private EsonTable esonTable = null;
    private JLabel label = null;
    private final StringUtils stringUtils = new StringUtils();
    private boolean isPrepare = false;
}
