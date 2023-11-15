/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eson.component.util;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author Wawa D. Eson
 */
public class TextFieldFilter extends DocumentFilter{ 
    
    private DocumentCase CASE = null;
    
    public enum DocumentCase{
        UPPERCASE, LOWERCASE;
    }
    
    @SuppressWarnings("empty-statement")
    public void setDocumentCase(JTextField field, DocumentCase c){;
        CASE = c;
        AbstractDocument fieldDoc = (AbstractDocument) field.getDocument();
        fieldDoc.setDocumentFilter(this);
    }

    @Override
    public void insertString(DocumentFilter.FilterBypass fb, int offset, String text, AttributeSet attr)
            throws BadLocationException {
        fb.insertString(offset, checkText(text), attr);
    }

    @Override
    public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {
        fb.replace(offset, length, checkText(text), attrs);
    }
    
    private String checkText(String txt){
        switch(CASE){
            case UPPERCASE -> txt = txt.toUpperCase();
            case LOWERCASE -> txt = txt.toUpperCase();
        }
        return txt;
    }
    
}
