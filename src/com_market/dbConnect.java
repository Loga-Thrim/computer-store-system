package com_market;
import java.sql.*;

public class dbConnect {
    private static String username = "root";
    private static String password = "godframedark8654";
    private static String con = "jdbc:mysql://localhost/com_store";
    
    public static Connection dbcon(){
        Connection conn = null;
        
        try{
            conn = DriverManager.getConnection(con, username, password);
            System.out.println("connected");
        } catch(SQLException e){
            System.err.println(e);
        }
        return conn;
    }
}
