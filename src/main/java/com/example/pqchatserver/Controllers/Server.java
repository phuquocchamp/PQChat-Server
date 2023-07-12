package com.example.pqchatserver.Controllers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server {
    public static volatile ServerThreadBus serverThreadBus;
    public static Socket serverOfSocket;

    public static void main(String[] args) {
        ServerSocket listener = null;
        serverThreadBus = new ServerThreadBus();
        System.out.println("Server is waiting to accept user...");
//        int clientID = 1;
        // Open SocketServer;
        try{
            listener = new ServerSocket(7778);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        // ThreadPool
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 100, 10,TimeUnit.SECONDS, new ArrayBlockingQueue<>(8));
        try{
            while (true){
                // Accept request from client.
                // Accept object Socket at Server.
                serverOfSocket = listener.accept();
                String clientID = "";
                ServerThread serverThread = new ServerThread(serverOfSocket);
                serverThreadBus.addServerThread(serverThread);
                System.out.println("Number of running threads : " + serverThreadBus.getServerThreadListSize());
                executor.execute(serverThread);

            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                listener.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }
}
