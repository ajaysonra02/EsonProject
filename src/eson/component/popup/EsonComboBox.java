package eson.component.popup;

import javax.swing.JComboBox;

public class EsonComboBox<E> extends JComboBox<E> {
    
    public EsonComboBox() {
        EsonComboUI ui = new EsonComboUI();
        setUI(ui);
    }

}