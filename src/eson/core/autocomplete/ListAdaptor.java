/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eson.core.autocomplete;

import javax.swing.JList;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;

/**
 * An implementation of the AbstractAutoCompleteAdaptor that is suitable for a
 * JList in conjunction with a JTextComponent.
 * 
 * @author Thomas Bierhance
 */
public class ListAdaptor extends AbstractAutoCompleteAdaptor implements ListSelectionListener {
    
    /** the list containing the items */
    JList list;
    /** the text component that is used for automatic completion*/
    JTextComponent textComponent;
    /** the converter used to transform items to strings */
    ObjectToStringConverter stringConverter;
    
    /**
     * Creates a new JListAdaptor for the given list and text component.
     * @param list the list that contains the items that are used for automatic
     * completion
     * @param textComponent the text component that will be used automatic
     * completion
     */
    public ListAdaptor(JList list, JTextComponent textComponent) {
        this(list, textComponent, ObjectToStringConverter.DEFAULT_IMPLEMENTATION);
    }
    
    /**
     * Creates a new JListAdaptor for the given list and text component.
     * @param list the list that contains the items that are used for automatic
     * completion
     * @param textComponent the text component that will be used automatic
     * completion
     * @param stringConverter the converter used to transform items to strings
     */
    public ListAdaptor(JList list, JTextComponent textComponent, ObjectToStringConverter stringConverter) {
        this.list = list;
        this.textComponent = textComponent;
        this.stringConverter = stringConverter;
        // when a new item is selected set and mark the text
        list.addListSelectionListener(this);
    }
    
    /**
     * Implementation side effect - do not invoke.
     * @param listSelectionEvent -
     */
    // ListSelectionListener (listening to list)
    @Override
    public void valueChanged(javax.swing.event.ListSelectionEvent listSelectionEvent) {
        // set the text to the currently selected item
        getTextComponent().setText(stringConverter.getPreferredStringForItem(list.getSelectedValue()));
        // mark the entire text
        markEntireText();
    }
    
    @Override
    public Object getSelectedItem() {
        return list.getSelectedValue();
    }
    
    @Override
    public int getItemCount() {
        return list.getModel().getSize();
    }
    
    @Override
    public Object getItem(int index) {
        return list.getModel().getElementAt(index);
    }
    
    @Override
    public void setSelectedItem(Object item) {
        list.setSelectedValue(item, true);
    }
    
    @Override
    public JTextComponent getTextComponent() {
        return textComponent;
    }
}
