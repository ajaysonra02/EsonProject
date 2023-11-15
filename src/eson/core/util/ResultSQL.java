/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eson.core.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.Date;

/**
 *
 * @author HP-QXO8AV
 */
public class ResultSQL{
    
    public ResultSQL(Statement st, String sql){
        this.sql = sql;
        this.st = st;
        fresh = false;
    }
    
    public ResultSet getResultSet() throws SQLException{
        execute();
        return rs;
    }
    
    public void execute() throws SQLException{
        if(!fresh){
            rs = st.executeQuery(sql);
            fresh = true;
        }
    }
    
    public int getRowCount() throws SQLException{
        int retval = 0;
        execute();
        rs.last();  
        retval = rs.getRow();
        rs.beforeFirst();
        return retval;
    }
    
    public boolean next() throws SQLException{
            execute();
        return rs.next();
    }
    
    public void close() throws SQLException{
        st.close();
        rs.close();
    }
    
    public String getString(String column) throws SQLException{
        return rs.getString(column);
    }
    
    public String getString(int index) throws SQLException{
        return rs.getString(index);
    }
    
    public int getInt(String column) throws SQLException{
        return rs.getInt(column);
    }
    
    public int getInt(int index) throws SQLException{
        return rs.getInt(index);
    }
    
    public Date getDate(String column) throws SQLException{
        return rs.getDate(column);
    }
    
    public Date getDate(int index) throws SQLException{
        return rs.getDate(index);
    }
    
    public boolean getBoolean(String column) throws SQLException{
        return rs.getBoolean(column);
    }
    
    public boolean getBoolean(int index) throws SQLException{
        return rs.getBoolean(index);
    }
    
    public double getDouble(String column) throws SQLException{
        return rs.getDouble(column);
    }
    
    public double getDouble(int index) throws SQLException{
        return rs.getDouble(index);
    }
    
    public Time getTime(String column) throws SQLException{
        return rs.getTime(column);
    }
    
    public Time getTime(int index) throws SQLException{
        return rs.getTime(index);
    }
    
    public float getFloat(String column) throws SQLException{
        return rs.getFloat(column);
    }
    
    public float getFloat(int index) throws SQLException{
        return rs.getFloat(index);
    }
    
    public byte[] getBytes(String column) throws SQLException{
        return rs.getBytes(column);
    }
    
    public byte[] getBytes(int index) throws SQLException{
        return rs.getBytes(index);
    }
    
    public Long getLong(String column) throws SQLException{
        return rs.getLong(column);
    }
    
    public Long getLong(int index) throws SQLException{
        return rs.getLong(index);
    }
    
    public Object getObject(String column) throws SQLException{
        return rs.getObject(column);
    }
    
    public Object getObject(int index) throws SQLException{
        return rs.getObject(index);
    }
    
    private String sql = "";
    private ResultSet rs = null;
    private Statement st = null;
    private boolean fresh = true;
    
}
