/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eson.core.util;

import java.text.DecimalFormat;

/**
 *
 * @author Wawa Eson
 */
public class StringUtils{
    
    public String capitalize(String name){
        String retval = "";
        String st[] = name.split(" ");
        int i = 0;
        while(i<st.length){
            retval += capitalizeWord(st[i]);
            if(i!=st.length-1){
                retval += " ";
            }
            i++;
        }
        return retval;
    }
    
    private String capitalizeWord(String name){
        String st[] = name.split("");
        int i = 1;
        String retval = st[0].toUpperCase();
        if(st[0].equals("(")){
            retval = st[0]+st[1].toUpperCase();
            i = 2;
        }
        while(i<st.length){
            retval+=st[i].toLowerCase();
            i++;
        }
        return retval;
    }
    
    public String getFirstLetter(String name){
        return name.split("")[0].toUpperCase()+".";
    }
    
    public String createIDFormat(int no_of_digit, int id_number){
        int length = (""+id_number).length();
        if(length<=no_of_digit){
            String str = "";
            for(int i=no_of_digit;no_of_digit>0;i--){
                if(i==length){
                    str+=id_number+"";
                    break;
                }else{
                    str+="0";
                }
            }
            return str;
        }else{
            return id_number+"";
        }
    }
    
    public boolean isLong(String text) {
        try {
            Long.valueOf(text); return true;
        } catch (NumberFormatException ex) { return false; }
    }
    
    public boolean isDouble(String text) {
        try {
            Double.valueOf(text); return true;
        } catch (NumberFormatException ex) { return false; }
    }
    
    public boolean isInteger(String text) {
        try {
            Integer.valueOf(text); return true;
        } catch (NumberFormatException ex) { return false; }
    }
    
    public boolean isFloat(String text) {
        try {
            Float.valueOf(text); return true;
        } catch (NumberFormatException ex) { return false; }
    }
    
    public String formatDecimal(String regex, double value){
        return new DecimalFormat(regex).format(value);
    }
    
}
