/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eson.component.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author WAWA ESON
 */
public class TableSearcher {
    
    private JTable TABLE;
    private JTextField SEARCH;
    private SearchCaretListener caretListener;
    private SearchKeyListener keyListener;
    
    public TableSearcher(JTextField field, JTable table){
        TABLE = table;
        SEARCH = field;
    }
    
    public void startListener(){
        TableRowSorter rowSorter = new TableRowSorter(TABLE.getModel());
        TABLE.setRowSorter(rowSorter);
        SEARCH.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        caretListener = new SearchCaretListener(rowSorter);
        keyListener = new SearchKeyListener(rowSorter);
        SEARCH.addKeyListener(keyListener);
        SEARCH.addCaretListener(caretListener);
    }
    
    public void pauseListener(){
        SEARCH.removeKeyListener(keyListener);
        SEARCH.removeCaretListener(caretListener);
    }
    
    private class SearchKeyListener implements KeyListener{
        private TableRowSorter rowSorter;
        public SearchKeyListener(TableRowSorter sorter){ rowSorter = sorter; }
        @Override public void keyTyped(KeyEvent evt){/**do nothing**/}
        @Override public void keyPressed(KeyEvent evt){/**do nothing**/}
        @Override public void keyReleased(KeyEvent evt){
            try{
                String text = SEARCH.getText();
                rowSorter.setRowFilter(text.trim().length()==0?null:RowFilter.regexFilter("(?i)"+text));
            }catch(Exception ex){/**do nothing**/}
        }
    }
    
    private class SearchCaretListener implements CaretListener{
        private TableRowSorter rowSorter;
        public SearchCaretListener(TableRowSorter sorter){ rowSorter = sorter; }
        @Override public void caretUpdate(CaretEvent e) {
            try{
                String text = SEARCH.getText();
                rowSorter.setRowFilter(text.trim().length()==0?null:RowFilter.regexFilter("(?i)"+text));
            }catch(Exception ex){/**do nothing**/}
        }
    }
}
