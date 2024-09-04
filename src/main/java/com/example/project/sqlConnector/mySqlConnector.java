package com.example.project.sqlConnector;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

public class mySqlConnector {
    public static Connection doConnect(){
        Connection con=null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/java2024","root","Arman#2004");
        }catch(Exception e){
            e.printStackTrace();
        }
        return con;
    }
    public static void main(String []args){
        if(doConnect()==null){
            System.out.println("Sorry");
        }
        else {
            System.out.println("Congo");
        }
    }
}
