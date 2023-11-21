/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eson.core.autocomplete;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.accessibility.Accessible;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.text.JTextComponent;

/**
 * An implementation of the AbstractAutoCompleteAdaptor that is suitable for JComboBox.
 * 
 * @author Thomas Bierhance
 * @author Karl Schaefer
 */
@SuppressWarnings("nls")
public class ComboBoxAdaptor extends AbstractAutoCompleteAdaptor implements ActionListener {
    
    /** the combobox being adapted */
    private JComboBox comboBox;
    
    /**
     * Creates a new ComobBoxAdaptor for the given combobox.
     * @param comboBox the combobox that should be adapted
     */
    public ComboBoxAdaptor(JComboBox comboBox) {
        this.comboBox = comboBox;
        // mark the entire text when a new item is selected
        comboBox.addActionListener(this);
    }
    
    /**
     * Implementation side effect - do not invoke.
     * @param actionEvent -
     */
    // ActionListener (listening to comboBox)
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        markEntireText();
    }
    
    @Override
    public int getItemCount() {
        return comboBox.getItemCount();
    }
    
    @Override
    public Object getItem(int index) {
        return comboBox.getItemAt(index);
    }
    
    @Override
    public void setSelectedItem(Object item) {
        //SwingX 834: avoid moving when already selected
        if (item == getSelectedItem()) {
            return;
        }
        
        // kgs - back door our way to finding the JList that displays the data.
        // then we ask the list to scroll until the last cell is visible. this
        // will cause the selected item to appear closest to the top.
        //
        // it is unknown whether this functionality will work outside of Sun's
        // implementation, but the code is safe and will "fail gracefully" on
        // other systems
        Accessible a = comboBox.getUI().getAccessibleChild(comboBox, 0);
        
        if (getItemCount() > 0 && a instanceof ComboPopup) {
            JList list = ((ComboPopup) a).getList();
            int lastIndex = list.getModel().getSize() - 1;
            
            Rectangle rect = list.getCellBounds(lastIndex, lastIndex);
            
            if (rect == null) {
                throw new IllegalStateException(
                        "attempting to access index " + lastIndex + " for " + comboBox);
            }
            
            list.scrollRectToVisible(rect);
        }
        
        //setting the selected item should scroll it into the visible region
        comboBox.setSelectedItem(item);
    }
    
    @Override
    public Object getSelectedItem() {
        return comboBox.getModel().getSelectedItem();
    }
    
    @Override
    public JTextComponent getTextComponent() {
        // returning the component of the combobox's editor
        return (JTextComponent) comboBox.getEditor().getEditorComponent();
    }
}
