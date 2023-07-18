package com.example.pqchatserver.Model;

import java.sql.*;

public class DatabaseDriver {
    private Connection connection;
    public DatabaseDriver(){
        try{
            this.connection = DriverManager.getConnection("jdbc:sqlite:pqchat_database.db");

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    // ----------------------------- Evaluate Account ------------------------------------- //
    public boolean evaluatedAccount(String email, String password){
        ResultSet resultSet = null;
        try{
            String sql = "SELECT * FROM Account Where Username = ? AND Password = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1,email );
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }else{
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error at evaluatedAccount function!");
        }
        return false;
    }

    // ---------------------------- Get current account id ---------------------------------//
    public ResultSet getCurrentAccount(String email, String password){
        ResultSet resultSet = null;
        try{
            String sql = "SELECT * FROM Account WHERE Username = ? AND Password = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error at getCurrentAccount Function in DatabaseDriver Class");
        }
        return resultSet;
    }
}
