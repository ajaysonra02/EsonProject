/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eson.core.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 *
 * @author ASUS-B150M
 */
public class ImageRenderer extends GaussianFilter{
    
    public BufferedImage toBufferedImage(Image img){
        if(img instanceof BufferedImage){
            return (BufferedImage)img;
        }else{
            BufferedImage buffImage = new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = buffImage.createGraphics();
            g2d.drawImage(img, 0, 0, null);
            g2d.dispose();
            return buffImage;
        }
    }
    
    public Image createCircleImage(Image image){
        int masterWidth = image.getWidth(null);
        int masterHeight = image.getHeight(null);
        int diameter = Math.min(masterWidth, masterHeight);
        BufferedImage mask = new BufferedImage(masterWidth,masterHeight,BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2d = mask.createGraphics();
        applyQualityRenderingHints(g2d);
        g2d.fillOval(0, 0, diameter-1, diameter-1);
        g2d.dispose();
        
        BufferedImage masked = new BufferedImage(diameter,diameter,BufferedImage.TYPE_INT_ARGB);
        g2d = masked.createGraphics();
        applyQualityRenderingHints(g2d);
        int x = (diameter - masterWidth) / 2;
        int y = (diameter - masterHeight) / 2;
        g2d.drawImage(image, x, y, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN));
        g2d.drawImage(mask, 0, 0, null);
        g2d.dispose();
        
        return new ImageIcon(masked).getImage();
    }
    
    public ArrayList<BufferedImage> generateFrameIcons(Image img){
        int s = 64;
        final int[] sizes = new int[s];
        for (int ii=0; ii<sizes.length; ii++) {
            sizes[ii] = 16+(ii*2);
        }
        ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
        for (int ii = 0; ii < sizes.length; ii++) {
            images.add(resize(toBufferedImage(img),sizes[ii],sizes[ii]));
        }
        return images;
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
    
    public Image createRoundBorderImage(Image image, int roundRadius){
        int masterWidth = image.getWidth(null);
        int masterHeight = image.getHeight(null);
        BufferedImage mask = new BufferedImage(masterWidth,masterHeight,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = mask.createGraphics();
        g2d.setComposite(AlphaComposite.Src);
        applyQualityRenderingHints(g2d);
        g2d.setColor(Color.WHITE);
        g2d.fill(new RoundRectangle2D.Float(0, 0, masterWidth, masterHeight, roundRadius, roundRadius));
        g2d.setComposite(AlphaComposite.SrcAtop);
        g2d.drawImage(image,0,0,null);
        g2d.dispose();
        return mask;
    }
    
    public Image maskImage(Image image, Color color){
        int masterWidth = image.getWidth(null);
        int masterHeight = image.getHeight(null);
        BufferedImage mask = new BufferedImage(masterWidth,masterHeight,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = mask.createGraphics();
        g2d.setComposite(AlphaComposite.Src);
        applyQualityRenderingHints(g2d);
        g2d.setColor(Color.WHITE);
        g2d.drawImage(image,0,0,null);
        g2d.setComposite(AlphaComposite.SrcAtop);
        g2d.setColor(color);
        g2d.fillRect(0, 0, masterWidth, masterHeight);
        g2d.dispose();
        return mask;
    }
    
    public Image maskImage(Image image, int masterWidth, int masterHeight, Color color){
        BufferedImage mask = new BufferedImage(masterWidth,masterHeight,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = mask.createGraphics();
        g2d.setComposite(AlphaComposite.Src);
        applyQualityRenderingHints(g2d);
        g2d.setColor(Color.WHITE);
        g2d.drawImage(image,0,0,null);
        g2d.setComposite(AlphaComposite.SrcAtop);
        g2d.setColor(color);
        g2d.fillRect(0, 0, masterWidth, masterHeight);
        g2d.dispose();
        return mask;
    }
    
    public ImageIcon resizeIcon(Image image,int width, int height, Color color){
        image = maskImage(image,color);
        return resizeIcon(image, width, height);
    }
    
    public ImageIcon resizeIcon(Image image,int width, int height){
        ImageIcon retval = new ImageIcon(image);
        if(image.getWidth(null)!=width && image.getHeight(null)!=height){
            retval = new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH));
        }
        return retval;
    }
    
    public Image getComponentGraphicsAsImage(Component c){
        BufferedImage imag = new BufferedImage(c.getWidth(), c.getHeight(),BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = imag.createGraphics();
        c.printAll(g2);
        g2.dispose();
        return imag;
    }
    
    public void applyQualityRenderingHints(Graphics2D g2d){
        applyAlphaInterpolation(g2d, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        applyAntiAliasing(g2d, RenderingHints.VALUE_ANTIALIAS_ON);
        applyColorRendering(g2d, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        applyDithering(g2d, RenderingHints.VALUE_DITHER_ENABLE);
        applyFractionalMetrics(g2d, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        applyInterpolation(g2d, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        applyRendering(g2d, RenderingHints.VALUE_RENDER_QUALITY);
        applyStrokeControl(g2d, RenderingHints.VALUE_STROKE_PURE);
    }
    
    public void applyRendering(Graphics2D g2d, Object hintValue){
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, hintValue);
    }
    
    public void applyAlphaInterpolation(Graphics2D g2d, Object hintValue){
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, hintValue);
    }
    
    public void applyAntiAliasing(Graphics2D g2d, Object hintValue){
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, hintValue);
    }
    
    public void applyColorRendering(Graphics2D g2d, Object hintValue){
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, hintValue);
    }
    
    public void applyDithering(Graphics2D g2d, Object hintValue){
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, hintValue);
    }
    
    public void applyFractionalMetrics(Graphics2D g2d, Object hintValue){
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, hintValue);
    }
    
    public void applyInterpolation(Graphics2D g2d, Object hintValue){
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hintValue);
    }
    
    public void applyStrokeControl(Graphics2D g2d, Object hintValue){
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, hintValue);
    }
    
    public BufferedImage generateShadow(BufferedImage imgSource, int size, Color color, float alpha) {

        int imgWidth = imgSource.getWidth();
        int imgHeight = imgSource.getHeight();

        BufferedImage imgMask = createCompatibleImage(imgWidth, imgHeight);
        Graphics2D g2d = imgMask.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.drawImage(imgSource, 0, 0, null);
        g2d.dispose();

        //--BLUR HERE--
        BufferedImage imgShadow = generateBlur(imgMask, size, color, alpha);
        return imgShadow;
    }

    public GraphicsConfiguration getGraphicsConfiguration() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    }

    public BufferedImage createCompatibleImage(int width, int height) {
        return createCompatibleImage(width, height, Transparency.TRANSLUCENT);
    }

    public BufferedImage createCompatibleImage(int width, int height, int transparency) {
        BufferedImage image = getGraphicsConfiguration().createCompatibleImage(width, height, transparency);
        image.coerceData(true);
        return image;
    }
    
    public BufferedImage generateBlur(BufferedImage imgSource, int size, Color color, float alpha){
    
        setRadius(size);

        int imgWidth = imgSource.getWidth();
        int imgHeight = imgSource.getHeight();

        BufferedImage imgBlur = createCompatibleImage(imgWidth, imgHeight);
        Graphics2D g2d = imgBlur.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.drawImage(imgSource, 0, 0, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, alpha));
        g2d.setColor(color);

        g2d.fillRect(0, 0, imgWidth, imgHeight);
        g2d.dispose();

        setRadius(10);
        imgBlur = filter(imgBlur, null);

        return imgBlur;
    }
    
}
