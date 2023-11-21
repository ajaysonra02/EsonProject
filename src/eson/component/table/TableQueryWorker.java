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

/**
 *
 * @author HP-QXO8AV
 */
public class TableQueryWorker extends SwingWorker<Integer, String> {
    
    public TableQueryWorker(EsonTable esonTable, Statement st, String sqlQuery, String columnNames[]){
        this.table = esonTable;
        this.columnNames = columnNames;
        STATEMENT = st;
        SQL_QUERY = sqlQuery;
        splitter = " : ";
        isPrepare = false;
    }
    
    public TableQueryWorker(EsonTable esonTable, Statement scrollableStatement, String sqlQuery, String columnNames[], boolean isPrepare){
        this(esonTable,scrollableStatement,sqlQuery,columnNames);
        this.isPrepare = isPrepare;
    }

    @Override
    protected Integer doInBackground() throws Exception {
        if(isPrepare){
            return prepare();
        }else{
            return load();
        }
    }
    
    private int prepare() throws Exception{
        table.clearRows();
        table.showPreparing("PREPARING TABLE DATA");
        resultQuery = new ResultSQL(STATEMENT, SQL_QUERY);
        int i = 0, maxSize = resultQuery.getRowCount();
        while(resultQuery.next()){
            i++;
            String rowString = "";                      
            for(String string:columnNames){
                rowString += resultQuery.getString(string)+splitter;
            }
            table.prepareRowValues(rowString.split(splitter));
        }
        resultQuery.close();
        table.closePreparing();
        table.pushPreparedValues(true);
        return 1;
    }
    
    private int load() throws Exception{
        table.clearRows();
        table.showLoading("FETCHING DATA FROM SQL SERVER");
        resultQuery = new ResultSQL(STATEMENT, SQL_QUERY);
        int i = 0, maxSize = resultQuery.getRowCount();
        while(resultQuery.next()){
            i++;
            table.setLoadingText("LOADING DATA FROM SQL SERVER [ "+stringUtils.formatDecimal("###,###", i)+" of "+stringUtils.formatDecimal("###,###", maxSize)+" ]");
            String rowString = ""; 
            for(String string:columnNames){
                rowString += resultQuery.getString(string)+splitter;
            }
            table.addRow(rowString.split(splitter));
        }
        resultQuery.close();
        table.scrollToTop();
        table.getFooter().setValue("TOTAL ROW", table.getValues(), table.getMaximumVisibleRowCount());
        table.closeLoading();
        return 1;
    }
        
    private ResultSQL resultQuery = null;
    private Statement STATEMENT = null;
    private String SQL_QUERY = "";
    private String splitter = " : ";
    private String columnNames[] = null;
    private EsonTable table = null;
    private final StringUtils stringUtils = new StringUtils();
    private boolean isPrepare = false;
}
