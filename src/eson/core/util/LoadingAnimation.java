/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eson.core.util;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingWorker;
import javax.swing.Timer;


/**
 *
 * @author Codey Hyacinth
 */
public class LoadingAnimation{
    
    private int simpleCounter = 0;
    private boolean isLoading = false;
    private JLabel label = null;
    private ImageRenderer RENDERER = new ImageRenderer();
    
    public void stopLoading(){
        isLoading = false;
    }
    
    public boolean isLoading(){
        return isLoading;
    }
    
    public void simpleLoading(JLabel label, int charCount){
        simpleTimer.stop();
        this.label = label;
        simpleCounter = 0;
        isLoading = true;
        initSimpleLoading(charCount);
        simpleTimer.start();
    }
    
    private void initSimpleLoading(int length){
        new SwingWorker(){
            @Override protected Object doInBackground() throws Exception {
                loadInfo = new String[length * 2];
                char a = '●', b = '◌';
                int aCount = 0, bCount = length;
                boolean flip = false;
                for (int i = 0; i < loadInfo.length; i++) {
                    loadInfo[i] = sequence(flip ? b : a, aCount) + sequence(flip ? a : b, bCount);
                    if (!flip) { flip = aCount == length && bCount == 0; }
                    aCount = aCount == length ? 1 : aCount + 1;
                    bCount = bCount == 0 ? length - 1 : bCount - 1;
                }
                return 0;
            }   
            
        }.execute();
    }
    
    private String sequence(char a,int count){
        String txt = "";
        for(int i=0;i<count;i++){
            txt += a+" ";
        }
        return txt;
    }
    
    protected String[] loadInfo;
    protected javax.swing.Timer simpleTimer = new javax.swing.Timer(300, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(isLoading){
                label.setText(loadInfo[simpleCounter]);
                simpleCounter++;
                if(simpleCounter==loadInfo.length){
                    simpleCounter = 0;
                }
            }else{((Timer)e.getSource()).stop();}
        }
    });
    
    public Image[] getWindowsLoadingImages(){
        Image[] retval = new Image[27];
        for(int i=0;i<retval.length;i++){
            retval[i] = new ImageIcon(getClass().getResource("/json/asidera/images/loading/win"+(i+1)+".png")).getImage();
        }
        return retval;
    }
    
    public Image[] getWindowsLoadingImages(int size, Color color){
        Image[] retval = new Image[27];
        for(int i=0;i<retval.length;i++){
            Image img = new ImageIcon(getClass().getResource("/json/asidera/images/loading/win"+(i+1)+".png")).getImage();
            retval[i] = RENDERER.maskImage(img,size,size,color);
        }
        return retval;
    }
    
    private Image[] loadingImage;
    private int windowsCounter, iconSize, colorCounter;
    private List<Color> windowsColor = new ArrayList<>();
    public void windowsLoading(JLabel label, Color[] color, int iconSize){
        this.label = label;
        this.iconSize = iconSize;
        new SwingWorker(){
            @Override protected Object doInBackground() throws Exception {
                windowsTimer.stop();
                windowsColor.clear();
                windowsColor.addAll(Arrays.asList(color));
                windowsCounter = 0;
                isLoading = true;
                colorCounter = 0;
                loadingImage = new Image[27];
                for (int i = 0; i < loadingImage.length; i++) {
                    loadingImage[i] = new ImageIcon(getClass().getResource("/json/asidera/images/loading/win" + (i + 1) + ".png")).getImage();
                }
                windowsTimer.start();
                return 0;
            }
        }.execute();
    }
    
    public void windowsLoading(JLabel label, Color color, int iconSize){
        windowsLoading(label,new Color[]{color},iconSize);
    }
    
    protected javax.swing.Timer windowsTimer = new javax.swing.Timer(50, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {    
            if(isLoading){  
                label.setIcon(RENDERER.resizeIcon(loadingImage[windowsCounter],iconSize, iconSize, windowsColor.get(colorCounter)));
                label.repaint();
                windowsCounter++;
                if(windowsCounter==loadingImage.length){
                    colorCounter = colorCounter<windowsColor.size()-1?(colorCounter+1):0;
                    windowsCounter = 0;
                }
            }else{((Timer)e.getSource()).stop();}
        }
    });
    
}
