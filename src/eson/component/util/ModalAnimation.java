/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eson.component.util;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import eson.component.EsonClose;

/**
 *
 * @author ASUS-B150M
 */
public class ModalAnimation {
    
    private EsonClose CLOSE = null;
    private javax.swing.JLabel TITLE = null;
    private javax.swing.JPanel PANEL = null;
    private java.awt.Color TITLE_FOREGROUND = null;
    private java.awt.Color CLOSE_FOREGROUND = null;
    private javax.swing.border.Border PANEL_BORDER = null;
    
    public ModalAnimation(JPanel panel){
        PANEL = panel;
        TITLE = null;
        CLOSE = null;
        TITLE_FOREGROUND = null;
        CLOSE_FOREGROUND = null;
        PANEL_BORDER = panel.getBorder();
    }
    
    public ModalAnimation(JPanel panel, JLabel title, EsonClose close){
        PANEL = panel;
        if(title!=null){
            TITLE = title;
            TITLE_FOREGROUND = title.getForeground();
        }
        if(close!=null){
            CLOSE = close;
            CLOSE_FOREGROUND = close.getForeground();
        }
        PANEL_BORDER = panel.getBorder();
    }
    
    public ModalAnimation(JPanel panel, Color textColor){
        PANEL = panel;
        TITLE = null;
        CLOSE = null;
        TITLE_FOREGROUND = textColor;
        CLOSE_FOREGROUND = textColor;
        PANEL_BORDER = panel.getBorder();
    }
    
    public void animateAsBorder(Color color){
         EventQueue.invokeLater(new Runnable() {
            int current = 0; boolean isStop = false, isShow = false;
            @Override
            public void run() {
                javax.swing.Timer ti = new javax.swing.Timer(100, (ActionEvent e) -> {
                    isShow = false;
                    if((current%2)!=0){isShow = current<4;isStop = !isShow;}
                    if(TITLE!=null){TITLE.setForeground(isShow?color:TITLE_FOREGROUND);}
                    if(CLOSE!=null){CLOSE.setForeground(isShow?color:CLOSE_FOREGROUND);}
                    PANEL.setBorder(isShow?javax.swing.BorderFactory.createMatteBorder(5, 5, 5, 5, color):PANEL_BORDER);
                    if(isStop){ ((Timer)e.getSource()).stop();  }
                    current++;
                });ti.setRepeats(true);ti.setCoalesce(true);ti.start();
            }
        });Toolkit.getDefaultToolkit().beep();
    }
    
    public void animateAsBackground(Color color){
         PANEL.setBackground(color);
         EventQueue.invokeLater(new Runnable() {
            int current = 0; boolean isStop = false,opaque = false;
            @Override public void run() {
                javax.swing.Timer ti = new javax.swing.Timer(100, (ActionEvent e) -> {
                    opaque = false; 
                    if((current%2)!=0){opaque = current<4;isStop = !opaque;}
                    PANEL.setOpaque(opaque);
                    if(TITLE!=null){TITLE.setForeground(opaque?color:TITLE_FOREGROUND); }
                    if(CLOSE!=null){CLOSE.setForeground(opaque?color:CLOSE_FOREGROUND);}
                    if(PANEL.getTopLevelAncestor()!=null){PANEL.getTopLevelAncestor().repaint();}
                    if(isStop){ ((Timer)e.getSource()).stop();}
                    current++;
                });ti.setRepeats(true);ti.setCoalesce(true);ti.start();
            }
        });Toolkit.getDefaultToolkit().beep();
    }
    
}
