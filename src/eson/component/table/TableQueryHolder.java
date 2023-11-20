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
import javax.swing.JLabel;

/**
 *
 * @author HP-QXO8AV
 */
public class TableQueryHolder {
    
    public TableQueryHolder(EsonTable esonTable){
        this.esonTable = esonTable;
    }
    
    public void loadTable(Statement st, String sqlQuery, String queryTableColumnNames[], JLabel label){
        sqlWorker = new TableQueryWorker(esonTable, st,sqlQuery, queryTableColumnNames, label, false);
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
        }}}}});
        sqlWorker.execute();
    }
    
    public void prepareTable(Statement st, String sqlQuery, String queryTableColumnNames[], JLabel label){
        sqlWorker = new TableQueryWorker(esonTable, st,sqlQuery, queryTableColumnNames, label, true);
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
        }}}}});
        sqlWorker.execute();
    }
    
    private EsonTable esonTable = null;
    private TableQueryWorker sqlWorker;
    
}
