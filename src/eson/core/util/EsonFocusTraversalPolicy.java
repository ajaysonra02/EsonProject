/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eson.core.util;

import eson.component.EsonSearch;
import eson.component.EsonText;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Codey Hyacinth
 */
public class EsonFocusTraversalPolicy extends FocusTraversalPolicy {
    
    List<Component> order;

    public EsonFocusTraversalPolicy(Component[] component) {
      order = new ArrayList<>(component.length);
      for(Component c:component){
          switch(c.getClass().getSimpleName().trim()){
              case "EsonText" -> order.add(((EsonText)c).getField());
              case "EsonSearch" -> order.add(((EsonSearch)c).getField());
              default -> order.add(c);
          }
      }
    }

    @Override
    public Component getComponentAfter(Container focusCycleRoot,
        Component aComponent) {
      int idx = order.indexOf(aComponent) + 1;
      if(idx==order.size()){
          idx = 0;
      }
      return order.get(idx);
    }

    @Override
    public Component getComponentBefore(Container focusCycleRoot,
        Component aComponent) {
      int idx = order.indexOf(aComponent) - 1;
      if (idx < 0) {
        idx = order.size() - 1;                                 
      }
      return order.get(idx);
    }

    @Override
    public Component getDefaultComponent(Container focusCycleRoot) {
      return order.get(0);
    }

    @Override
    public Component getLastComponent(Container focusCycleRoot) {
      return order.get(order.size()-1);
    }

    @Override
    public Component getFirstComponent(Container focusCycleRoot) {
      return order.get(0);
    }
}

