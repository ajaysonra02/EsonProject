/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eson.component.table;

import java.beans.PropertyChangeEvent;
import java.sql.Statement;
import java.util.concurrent.CancellationException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingWorker.StateValue;
import static javax.swing.SwingWorker.StateValue.DONE;
import static javax.swing.SwingWorker.StateValue.PENDING;
import static javax.swing.SwingWorker.StateValue.STARTED;
import eson.component.EsonProgress;

/**
 *
 * @author HP-QXO8AV
 */
public class TableQueryHolder {
    
    public TableQueryHolder(JTable table, EsonProgress progress){
        this.jTable = table;
        this.progress = progress;
        isEsonTable = false;
    }
    
    public TableQueryHolder(EsonTable esonTable){
        this.esonTable = esonTable;
        isEsonTable = true;
    }
    
    public boolean isEsonTable(){
        return isEsonTable;
    }
    
    public void setSeparator(String separator){
        this.separator = separator;
    }
    
    public void sqlWork(Statement st, String sqlQuery, String queryTableColumnNames[], boolean showCounter){
        if(!isEsonTable){
            sqlWorker = new TableQueryWorker(jTable, progress, st,sqlQuery, queryTableColumnNames, showCounter);
        }else{
            sqlWorker = new TableQueryWorker(esonTable, st,sqlQuery, queryTableColumnNames, showCounter);
        }
        startSqlWork();
    }
    
    private void startSqlWork(){
        if (separator != null) {
            sqlWorker.setSplitter(separator);
        }
        sqlWorker.addPropertyChangeListener((PropertyChangeEvent evt) -> {
            switch (evt.getPropertyName()) {
                case "state":
                    switch ((StateValue) evt.getNewValue()) {
                        case DONE:
                            try {
                                sqlWorker.get();
                            } catch (final CancellationException e) {
                                e.printStackTrace();
                                JOptionPane.showMessageDialog(null, "Query was cancelled!!", "EsonProject", JOptionPane.WARNING_MESSAGE);
                            } catch (final Exception e) {
                                e.printStackTrace();
                                JOptionPane.showMessageDialog(null, "Query Failed!!", "EsonProject", JOptionPane.ERROR_MESSAGE);
                            }
                            sqlWorker = null;
                            separator = null;
                            break;
                        case STARTED:
                            break;
                        case PENDING:
                            break;
                    }
                    break;
            }
        });
        sqlWorker.execute();
    }
    
    private JTable jTable;
    private EsonTable esonTable;
    private EsonProgress progress;
    private TableQueryWorker sqlWorker;
    private String separator = null;
    private boolean isEsonTable;
    
}
