/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eson.core.autocomplete;

import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxEditor;

/**
 * <p>
 * Wrapper around the combobox editor that translates combobox items into
 * strings. The methods <tt>setItem</tt> and <tt>getItem</tt> are modified
 * to account for the string conversion.
 * </p><p>
 * This is necessary for those cases where the combobox items have no useful
 * <tt>toString()</tt> method and a custom <tt>ObjectToStringConverter</tt> is
 * used.
 * </p><p>
 * If we do not do this, the interaction between ComboBoxEditor and JComboBox
 * will result in firing ActionListener events with the string value of
 * ComboBoxEditor as the currently selected value.
 * </p>
 * @author Noel Grandin noelgrandin@gmail.com
 * @author Thomas Bierhance
 */
public class AutoCompleteComboBoxEditor implements ComboBoxEditor {

    /** the original combo box editor*/
    final ComboBoxEditor wrapped;
    /** the converter used to convert items into their string representation */
    final ObjectToStringConverter stringConverter;
    /** last selected item */
    private Object oldItem;

    /**
     * Creates a new <tt>AutoCompleteComboBoxEditor</tt>.
     *
     * @param wrapped the original <tt>ComboBoxEditor</tt> to be wrapped
     * @param stringConverter the converter to use to convert items into their
     * string representation.
     */
    public AutoCompleteComboBoxEditor(ComboBoxEditor wrapped, ObjectToStringConverter stringConverter) {
        this.wrapped = wrapped;
        this.stringConverter = stringConverter;
    }

    /* (non-javadoc)
     * @see javax.swing.ComboBoxEditor#getEditorComponent()
     */
    @Override
    public Component getEditorComponent() {
        return wrapped.getEditorComponent();
    }

    /* (non-javadoc)
     * @see javax.swing.ComboBoxEditor#setItem(java.lang.Object)
     */
    @Override
    public void setItem(Object anObject) {
        this.oldItem = anObject;
        wrapped.setItem(stringConverter.getPreferredStringForItem(anObject));
    }

    /* (non-javadoc)
     * @see javax.swing.ComboBoxEditor#getItem()
     */
    @Override
    public Object getItem() {
        final Object wrappedItem = wrapped.getItem();
        
        String[] oldAsStrings = stringConverter.getPossibleStringsForItem(oldItem);
        for (int i=0, n=oldAsStrings.length; i<n; i++) {
            String oldAsString = oldAsStrings[i];
            if (oldAsString != null &&  oldAsString.equals(wrappedItem)) {
                return oldItem;
            }
        }
        return null;
    }

    /* (non-javadoc)
     * @see javax.swing.ComboBoxEditor#selectAll()
     */
    @Override
    public void selectAll() {
        wrapped.selectAll();
    }

    /* (non-javadoc)
     * @see javax.swing.ComboBoxEditor#addActionListener(java.awt.event.ActionListener)
     */
    @Override
    public void addActionListener(ActionListener l) {
        wrapped.addActionListener(l);
    }

    /* (non-javadoc)
     * @see javax.swing.ComboBoxEditor#removeActionListener(java.awt.event.ActionListener)
     */
    @Override
    public void removeActionListener(ActionListener l) {
        wrapped.removeActionListener(l);
    }
}
