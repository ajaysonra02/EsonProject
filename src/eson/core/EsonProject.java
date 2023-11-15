/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eson.core;

import eson.core.util.EsonCrypt;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author ZOE CODEY
 */
public class EsonProject {
    
    private static String EXPIRATION_DATE = "2024-1-19";
    private static boolean messageShown = false;
    private static LicenseDialog licenseDialog = new LicenseDialog(null,true);
    
    public static String getExpirationDate(){
        return EXPIRATION_DATE;
    }
    
    private static String getLicenseDirectory(){
        return System.getenv("APPDATA")+"\\EsonProject";
    }
    
    private static String getLicensePath(){
        return getLicenseDirectory()+"\\licstat";
    }
    
    private static boolean dateChecked = false;
    public static boolean start(){
        boolean retval = true;
        if (licenseStatusCheck()) {
            if (!dateChecked) {
                retval = checkDate();
                dateChecked = true;
            }
        } else {
            retval = false;
            if (!messageShown) {
                licenseDialog.showUnlicensed();
                messageShown = true;
            }
        }
        return retval;
    }
    
    private static boolean checkDate(){
        boolean retval = true;
        try{
            Date TODAY = new Date();
            Date EXPIRATION = new SimpleDateFormat("yyyy-MM-dd").parse(getExpirationDate());
            retval = TODAY.before(EXPIRATION);
            if (!retval) {
                if (!messageShown) {
                    writeLicenseFile("UNLICENSED COPY OF ESON PROJECT");
                    licenseDialog.showUnlicensed();
                    messageShown = true;
                }
            }
        }catch(Exception ex){ex.printStackTrace();}
        return retval;
    }
    
    public static void openBrowserLink(String link){
        try{
            Desktop.getDesktop().browse(new URI(link));
        }catch(Exception ex){
            System.err.println("OPEN BROWSER LINK: "+link+" \nERROR MESSAGE: "+ex.getMessage());
        }
    }
    
    public static void sendEmailLink(String email, String subject){
        try{
            Desktop.getDesktop().browse(new URI("mailto:"+email+"?subject="+subject));
        }catch(Exception ex){
            System.err.println("SEND EMAIL LINK: "+email+" \nERROR MESSAGE: "+ex.getMessage());
        }
    }
    
    private static boolean writeLicenseFile(String s){
        try{
            File f = new File(getLicenseDirectory());
            if(!f.exists()){f.mkdir();}
            FileWriter fw = new FileWriter(new File(getLicensePath()));
            EsonCrypt esonCrypt = new EsonCrypt();
            fw.append(generateRandomString(40)+"\n");
            fw.append(generateRandomString(35)+"\n");
            fw.append(generateRandomString(50)+"\n");
            fw.append(generateRandomString(52)+"\n");
            fw.append(generateRandomString(34)+"\n");
            fw.append(generateRandomString(42)+"\n");
            fw.append(generateRandomString(45)+"\n");
            fw.append("P1TH4zWawA0oksoKrSsu42ThLEsOnpqmmUjN098lsLqZbH21aJ=\n");
            fw.append(esonCrypt.encrypt("ihavenolife02", s)+"\n");
            fw.append(generateRandomString(51)+"\n");
            fw.append(generateRandomString(34)+"\n");
            fw.append(generateRandomString(42)+"\n");
            fw.append(generateRandomString(45)+"\n");
            fw.append(generateRandomString(49)+"\n");
            fw.append(generateRandomString(38)+"\n");
            fw.append(generateRandomString(50)+"\n");
            fw.close();
        }catch(Exception ex){
            ex.printStackTrace();
            System.err.println("WRITE LICENSE FILE ERROR: "+ex.getMessage());
        }
        return true;
    }
    
    private static String generateRandomString(int targetStringLength){
        int leftLimit = 48,rightLimit = 122;
        return new Random().ints(leftLimit, rightLimit + 1)
            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
            .limit(targetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString()+"=";
    }
    
    private static boolean readLicenseStatus(File f){
        boolean retval = false;
        try{
            FileReader fr = new FileReader(f);
            BufferedReader br = new java.io.BufferedReader(fr);
            String line = "";
            boolean nextLine = false;
            while ((line = br.readLine()) != null) {
                if(nextLine){
                    String s = new EsonCrypt().decrypt("ihavenolife02", line);
                    retval = s.toUpperCase().trim().equals("LICENSED COPY OF ESON PROJECT");
                    break;
                }
                if(line.trim().equals("P1TH4zWawA0oksoKrSsu42ThLEsOnpqmmUjN098lsLqZbH21aJ=")){
                    nextLine = true;
                }
            }fr.close(); br.close();
        }catch(Exception ex){ 
            retval = false;
            ex.printStackTrace();
            System.err.println("READ LICENSE FILE ERROR: "+ex.getMessage());
        }
        return retval;
    }
    
    private static boolean isLicenseCheck = false, licenseFlag = false;
    
    private static boolean licenseStatusCheck(){
        if(!isLicenseCheck){
            try {
                File f = new File(getLicensePath());
                if (f.exists()) {
                    licenseFlag = readLicenseStatus(f);
                } else {
                    licenseFlag = checkDate();
                    writeLicenseFile((licenseFlag?"":"UN")+"LICENSED COPY OF ESON PROJECT");
                }
            } catch (Exception ex) {
                System.err.println("LICENSE STATUS CHECK ERROR: " + ex.getMessage());
            }
            isLicenseCheck = true;
        }
        return licenseFlag;
    }
    
    
}
