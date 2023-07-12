package com.example.pqchatserver.Controllers;

import java.io.*;
import java.net.Socket;

public class ServerThread implements Runnable {
    private final Socket serverSocket;
    private String clientID;
    private BufferedReader serverReader;
    private BufferedWriter serverWriter;
    private boolean isClosed = false;

    public ServerThread(Socket serverSocket) {
        this.serverSocket = serverSocket;
        isClosed = false;
//        System.out.println("Server thread number: " + clientID + " started");
    }

    public String getClientID() {
        return clientID;
    }


    @Override
    public void run() {
        try {
            // Open Read/Write stream on socket server.
            serverReader = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            serverWriter = new BufferedWriter(new OutputStreamWriter(serverSocket.getOutputStream()));

            String streamMessage;
            while (!isClosed) {
                streamMessage = serverReader.readLine();
                if (streamMessage == null) {
                    break;
                }

                // Handle different types of messages
                String[] messageSplit = streamMessage.split("_");
                if(messageSplit[0].equals("clientLogin")){
                    System.out.println("Client : " + messageSplit[1] + " is active!");
                    this.clientID = messageSplit[1];
                }

                if (messageSplit[0].equals("singleChat")) {
                    System.out.println(streamMessage);
                    Server.serverThreadBus.singleChat(messageSplit[2], streamMessage);

                }
                if(messageSplit[0].equals("imageTransfer")){
                    System.out.println(streamMessage);

                    String receiver = messageSplit[2];
                    String fileName = messageSplit[3];

                    saveFile(fileName);
                    Server.serverThreadBus.singleChat(receiver, streamMessage);
                    String projectPath = "D:\\Coding\\Back End\\Java\\JavaFX\\PQChat-Server\\src\\main\\resources\\Files\\";
                    File file = new File( projectPath + fileName);
                    transferFile(file);

                }
            }
        } catch (IOException e) {
            isClosed = true;
            Server.serverThreadBus.removeServerThread(clientID);
            System.out.println("Client " + this.clientID + " disconnected.");
        } finally {
            try {
                serverReader.close();
                serverWriter.close();
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Utilities
    public void writeMessage(String message) throws IOException {
        serverWriter.write(message);
        serverWriter.newLine();
        serverWriter.flush();
    }

    private void saveFile( String fileName) throws IOException {
        // Nhận dữ liệu file từ client
//        String fileName = serverReader.readLine();
        String filePath = "D:\\Coding\\Back End\\Java\\JavaFX\\PQChat-Server\\src\\main\\resources\\Files\\" + fileName;
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = serverSocket.getInputStream().read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, bytesRead);
        }

        fileOutputStream.close();
    }

    private void transferFile(File file){
        try{
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int bytesRead;
            OutputStream outputStream = serverSocket.getOutputStream();
            while ((bytesRead = fileInputStream.read(buffer)) != -1){
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Sending file error!");
        }

    }

}
