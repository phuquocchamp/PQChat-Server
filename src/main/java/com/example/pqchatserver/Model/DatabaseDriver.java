package com.example.pqchatserver.Model;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseDriver {
    private Connection connection;
    public DatabaseDriver(){
        try{
            this.connection = DriverManager.getConnection("jdbc:sqlite:pqchat_database.db");

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
