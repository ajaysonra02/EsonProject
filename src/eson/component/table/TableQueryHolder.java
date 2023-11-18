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
import javax.swing.SwingWorker.StateValue;
import static javax.swing.SwingWorker.StateValue.DONE;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author HP-QXO8AV
 */
public class TableQueryHolder {
    
    public TableQueryHolder(EsonTable esonTable){
        this.esonTable = esonTable;
    }
    
    public void setSeparator(String separator){
        this.separator = separator;
    }
    
    public void sqlWork(Statement st, String sqlQuery, String queryTableColumnNames[], boolean showCounter){
        sqlWorker = new TableQueryWorker(esonTable, st,sqlQuery, queryTableColumnNames, showCounter);
        if (separator != null) {
            sqlWorker.setSplitter(separator);
        }
        sqlWorker.addPropertyChangeListener((PropertyChangeEvent evt) -> {
            switch (evt.getPropertyName()) {
                case "state" -> {
                    switch ((StateValue) evt.getNewValue()) {
                        case DONE -> {
                            try {
                                sqlWorker.get();
                            } catch (final CancellationException e) {
                                JOptionPane.showMessageDialog(null, "Query was cancelled!!", "EsonProject", JOptionPane.WARNING_MESSAGE);
                            } catch (final InterruptedException | ExecutionException e) {
                                JOptionPane.showMessageDialog(null, "Query Failed!!", "EsonProject", JOptionPane.ERROR_MESSAGE);
                            }
                            sqlWorker = null;
                            separator = null;
        }}}}});
        sqlWorker.execute();
    }
    
    private EsonTable esonTable;
    private TableQueryWorker sqlWorker;
    private String separator = null;
    
}
