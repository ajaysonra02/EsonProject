/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eson.core.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.Timer;


/**
 *
 * @author Codey Hyacinth
 */
public class LoadingAnimation{
    
    private int loadingCounter = 0;
    private boolean isLoading = false;
    private JLabel label = null;
    
    public void stopLoading(){
        isLoading = false;
    }
    
    public boolean isLoading(){
        return isLoading;
    }
    
    public void simpleLoading(JLabel label, int charCount){
        this.label = label;
        loadingCounter = 0;
        isLoading = true;
        initLoadInfo(charCount);
        simpleLoading.start();
    }
    
    private void initLoadInfo(int length){
        loadInfo = new String[length*2];
        char a = '●', b = '◌';
        int aCount = 0, bCount = length;
        boolean flip = false;
        for(int i=0;i<loadInfo.length;i++){
            loadInfo[i] = sequence(flip?b:a,aCount)+sequence(flip?a:b,bCount);
            if(!flip){flip = aCount==length && bCount==0;}
            aCount = aCount==length?1:aCount+1;
            bCount = bCount==0?length-1:bCount-1;
        }
    }
    
    private String sequence(char a,int count){
        String txt = "";
        for(int i=0;i<count;i++){
            txt += a+" ";
        }
        return txt;
    }
    
    protected String[] loadInfo = new String[]{"◌ ◌ ◌","● ◌ ◌","● ● ◌","● ● ●","◌ ● ●","◌ ◌ ●"};
    protected javax.swing.Timer simpleLoading = new javax.swing.Timer(300, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(isLoading){
                label.setText(loadInfo[loadingCounter]);
                loadingCounter++;
                if(loadingCounter==loadInfo.length){
                    loadingCounter = 0;
                }
            }else{((Timer)e.getSource()).stop();}
        }
    });
    
}
