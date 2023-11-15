/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eson.core.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 *
 * @author Zayn Ezekiel
 */
public class DataConnection {
        
    public Connection getConnection(String NAME){
        try{
            Class.forName("org.postgresql.Driver");
            if(NAME!=null){
                NAME = NAME.trim().equals("")?"":NAME;
            }else{ NAME=""; }
            return DriverManager.getConnection("jdbc:postgresql://"+DATABASE_ADDRESS+":"+DATABASE_PORT+"/"+DATABASE_NAME,DATABASE_USERNAME,DATABASE_PASSWORD);
        }catch(Exception e){
            return null;
        }
    }
    
    public Connection getConnection(){
        return getConnection(DATABASE_NAME);
    }
    
    public Statement createStatement(){
        try{
            return getConnection().createStatement();
        }catch(Exception e){ return null;}
    }
    
    public Statement createScrollableStatement(Connection connection){
        try{
            return connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        }catch(Exception e){ return null;}
    }
    
    public void setDatabaseConfiguration(String NAME, String ADDRESS, String PORT, String USERNAME, String PASSWORD){
        DATABASE_NAME = NAME;
        DATABASE_ADDRESS = ADDRESS;
        DATABASE_PORT = PORT;
        DATABASE_USERNAME = USERNAME;
        DATABASE_PASSWORD = PASSWORD;
    }
    
    public boolean authenticateHostAddress(){
        try{
            getConnection("postgres"); return true;
        }catch(Exception ex){ 
            System.err.println("HOST AUTHENTICATION ERROR : "+ex.getMessage());
            return false;
        }
    }
    
    public boolean isDatabaseExists(){
        boolean retval = false;
        try{
            ResultSet rs = getConnection(null).getMetaData().getCatalogs();
            while(rs.next()){
                if(DATABASE_NAME.compareToIgnoreCase(rs.getString(1))==0){
                    retval = true;
                }
            }
            rs.close();
        }catch(Exception ex){
            System.err.println("CHECK DATABASE EXISTS ERROR : "+ex.getMessage());
            retval = false;
        }
        return retval;
    }
    
    public boolean isTableExists(String schemaName){
        boolean retval = false;
        try{
            Statement st = createStatement();
            ResultSet rs = st.executeQuery("select exists (select * from pg_catalog.pg_namespace where nspname = '"+schemaName+"');");
            if(rs.next()){
                retval = rs.getBoolean("exists");
            }
            rs.close();
            st.close();
        }catch(Exception ex){
            retval = false;
            System.err.println("CHECK TABLE EXISTS ERROR : "+ex.getMessage());
        }
        return retval;
    }
    
    public boolean createDatabase(){
        try{
            Statement st = getConnection(null).createStatement();
            st.executeUpdate("create database "+DATABASE_NAME+";");
            st.close();
            return true;
        }catch(Exception ex){
            if(ex.getMessage().equals("ERROR: database \""+DATABASE_NAME+"\" already exists")){
                return true;
            }
            System.err.println("CREATE DATABASE ERROR : "+ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
    
    private String DATABASE_ADDRESS = "";
    private String DATABASE_PASSWORD = "";
    private String DATABASE_USERNAME = "";
    private String DATABASE_NAME = "";
    private String DATABASE_PORT = "";
}
