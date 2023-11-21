/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eson.core.autocomplete;

import java.awt.Color;
import java.awt.Font;

import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Karl George Schaefer
 */
public class AutoCompleteStyledDocument extends AutoCompleteDocument implements
        StyledDocument {
    /**
     * @param adaptor
     * @param strictMatching
     * @param stringConverter
     * @param delegate
     */
    public AutoCompleteStyledDocument(AbstractAutoCompleteAdaptor adaptor,
            boolean strictMatching, ObjectToStringConverter stringConverter,
            StyledDocument delegate) {
        super(adaptor, strictMatching, stringConverter, delegate);
    }

    /**
     * @param adaptor
     * @param strictMatching
     * @param stringConverter
     */
    public AutoCompleteStyledDocument(AbstractAutoCompleteAdaptor adaptor,
            boolean strictMatching, ObjectToStringConverter stringConverter) {
        super(adaptor, strictMatching, stringConverter);
    }

    /**
     * @param adaptor
     * @param strictMatching
     */
    public AutoCompleteStyledDocument(AbstractAutoCompleteAdaptor adaptor,
            boolean strictMatching) {
        super(adaptor, strictMatching);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected Document createDefaultDocument() {
        return new DefaultStyledDocument();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Style addStyle(String nm, Style parent) {
        return ((StyledDocument) delegate).addStyle(nm, parent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Color getBackground(AttributeSet attr) {
        return ((StyledDocument) delegate).getBackground(attr);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Element getCharacterElement(int pos) {
        return ((StyledDocument) delegate).getCharacterElement(pos);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Font getFont(AttributeSet attr) {
        return ((StyledDocument) delegate).getFont(attr);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Color getForeground(AttributeSet attr) {
        return ((StyledDocument) delegate).getForeground(attr);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Style getLogicalStyle(int p) {
        return ((StyledDocument) delegate).getLogicalStyle(p);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Element getParagraphElement(int pos) {
        return ((StyledDocument) delegate).getParagraphElement(pos);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Style getStyle(String nm) {
        return ((StyledDocument) delegate).getStyle(nm);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeStyle(String nm) {
        ((StyledDocument) delegate).removeStyle(nm);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCharacterAttributes(int offset, int length,
            AttributeSet s, boolean replace) {
                ((StyledDocument) delegate).setCharacterAttributes(offset, length, s, replace);
            }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLogicalStyle(int pos, Style s) {
        ((StyledDocument) delegate).setLogicalStyle(pos, s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setParagraphAttributes(int offset, int length,
            AttributeSet s, boolean replace) {
                ((StyledDocument) delegate).setParagraphAttributes(offset, length, s, replace);
            }
}
