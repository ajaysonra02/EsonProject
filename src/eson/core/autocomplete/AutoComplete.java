/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eson.core.autocomplete;

import static eson.core.autocomplete.AutoCompleteDecorator.createAutoCompleteDocument;
import static eson.core.autocomplete.AutoCompleteDecorator.decorate;
import static eson.core.autocomplete.AutoCompleteDecorator.undecorate;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;

import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.text.JTextComponent;

/**
 * 
 * @author kschaefer
 */
final class AutoComplete {
    static class InputMap extends javax.swing.InputMap {
        private static final long serialVersionUID = 1L;
    }

    static class FocusAdapter extends java.awt.event.FocusAdapter {
        private AbstractAutoCompleteAdaptor adaptor;

        public FocusAdapter(AbstractAutoCompleteAdaptor adaptor) {
            this.adaptor = adaptor;
        }

        @Override
        public void focusGained(FocusEvent e) {
            adaptor.markEntireText();
        }
    }
    
    static class KeyAdapter extends java.awt.event.KeyAdapter {
        private JComboBox comboBox;
        
        public KeyAdapter(JComboBox comboBox) {
            this.comboBox = comboBox;
        }
        
        @Override
        public void keyPressed(KeyEvent keyEvent) {
            // don't popup on action keys (cursor movements, etc...)
            if (keyEvent.isActionKey()) {
                return;
            }
            
            // don't popup if the combobox isn't visible or empty anyway
            if (comboBox.isDisplayable() && !comboBox.isPopupVisible() && comboBox.getModel().getSize() != 0) {
                int keyCode = keyEvent.getKeyCode();
                // don't popup when the user hits shift,ctrl or alt
                if (keyCode==KeyEvent.VK_SHIFT || keyCode==KeyEvent.VK_CONTROL || keyCode==KeyEvent.VK_ALT) return;
                // don't popup when the user hits escape (see issue #311)
                if (keyCode==KeyEvent.VK_ENTER || keyCode==KeyEvent.VK_ESCAPE) return;
                comboBox.setPopupVisible(true);
            }
        }
    }

    static class PropertyChangeListener implements java.beans.PropertyChangeListener {
        private JComboBox comboBox;
        
        public PropertyChangeListener(JComboBox comboBox) {
            this.comboBox = comboBox;
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        @SuppressWarnings("nls")
        public void propertyChange(PropertyChangeEvent evt) {
            if ("editor".equals(evt.getPropertyName())) {
                handleEditor(evt);
            } else if ("enabled".equals(evt.getPropertyName())) {
                handleEnabled(evt);
            }
        }
        
        private void handleEnabled(PropertyChangeEvent evt) {
            if (Boolean.TRUE.equals(evt.getNewValue())) {
                comboBox.setEditable(true);
            } else {
                JTextComponent textComponent = (JTextComponent) comboBox.getEditor().getEditorComponent();
                boolean strictMatching = ((AutoCompleteDocument) textComponent.getDocument()).strictMatching;
                
                comboBox.setEditable(!strictMatching);
            }
        }

        private void handleEditor(PropertyChangeEvent evt) {
            if (evt.getNewValue() instanceof AutoCompleteComboBoxEditor) {
                return;
            }
            
            AutoCompleteComboBoxEditor acEditor = (AutoCompleteComboBoxEditor) evt.getOldValue();
            boolean strictMatching = false;
            
            if (acEditor.getEditorComponent() != null) {
                JTextComponent textComponent = (JTextComponent) acEditor.getEditorComponent();
                strictMatching = ((AutoCompleteDocument) textComponent.getDocument()).strictMatching;
                
                undecorate(textComponent);
                
                for (KeyListener l : textComponent.getKeyListeners()) {
                    if (l instanceof KeyAdapter) {
                        textComponent.removeKeyListener(l);
                        break;
                    }
                }
            }

            JTextComponent editorComponent = (JTextComponent) comboBox.getEditor().getEditorComponent();
            AbstractAutoCompleteAdaptor adaptor = new ComboBoxAdaptor(comboBox);
            AutoCompleteDocument document = createAutoCompleteDocument(adaptor, strictMatching,
                    acEditor.stringConverter, editorComponent.getDocument());
            decorate(editorComponent, document, adaptor);
            
            editorComponent.addKeyListener(new KeyAdapter(comboBox));
            
            //set before adding the listener for the editor
            comboBox.setEditor(new AutoCompleteComboBoxEditor(comboBox.getEditor(), document.stringConverter));
        }
    }

    static class SelectionAction implements Action {
        private Action delegate;
        
        public SelectionAction(Action delegate) {
            this.delegate = delegate;
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox comboBox = (JComboBox) e.getSource();
            JTextComponent textComponent = (JTextComponent) comboBox.getEditor().getEditorComponent();
            AutoCompleteDocument doc = (AutoCompleteDocument) textComponent.getDocument();
            
            // doing this prevents the updating of the selected item to "" during the remove prior
            // to the insert in JTextComponent.setText
            doc.strictMatching = true;
            try {
                delegate.actionPerformed(e);
            } finally {
                doc.strictMatching = false;
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
            delegate.addPropertyChangeListener(listener);
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public void removePropertyChangeListener(java.beans.PropertyChangeListener listener) {
            delegate.removePropertyChangeListener(listener);
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public Object getValue(String key) {
            return delegate.getValue(key);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void putValue(String key, Object value) {
            delegate.putValue(key, value);
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isEnabled() {
            return delegate.isEnabled();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setEnabled(boolean b) {
            delegate.setEnabled(b);
        }
    }
    
    private AutoComplete() {
        // prevent instantiation
    }
}
