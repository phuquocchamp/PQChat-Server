package com.example.pqchatserver.Model;

public class Model {
    private static Model model;
    private final DatabaseDriver databaseDriver;

    private Model(){
        this.databaseDriver = new DatabaseDriver();
    }


    public static synchronized Model getInstance(){
        if(model == null){
            model = new Model();
        }
        return model;
    }

    public DatabaseDriver getDatabaseDriver(){
        return databaseDriver;
    }
}
