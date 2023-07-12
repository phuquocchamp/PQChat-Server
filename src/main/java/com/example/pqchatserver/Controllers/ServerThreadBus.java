package com.example.pqchatserver.Controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
// Lớp quản lí server thread
public class ServerThreadBus {
    private final List<ServerThread> serverThreadList;

    public ServerThreadBus(){
        serverThreadList = new ArrayList<>();
    }
    public List<ServerThread> getServerThreadList(){
        return serverThreadList;
    }

    // Add Server Thread into List.
    public void addServerThread(ServerThread serverThread){
        serverThreadList.add(serverThread);
    }
    // Get the size of the serverList.

    public int getServerThreadListSize(){
        return serverThreadList.size();
    }

    // multiCastSend
    public void mutilCastSend(String message) throws IOException {
        for (ServerThread serverThread : Server.serverThreadBus.getServerThreadList()){
            serverThread.writeMessage(message);
        }
    }
    //Gửi tin nhắn cho tất cả client ngoại trừ bản thân.
    public void boardCast(String clientID, String message){
        for(ServerThread serverThread : Server.serverThreadBus.getServerThreadList()) {
            if(!serverThread.getClientID().equals(clientID)){
                try{
                    serverThread.writeMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void sendOnlineList() throws IOException {
        String result = "";
        List<ServerThread> threadList = Server.serverThreadBus.getServerThreadList();
        for(ServerThread serverThread:threadList){
            result+= serverThread.getClientID()+"_";
        }
        Server.serverThreadBus.mutilCastSend("updateOnlineList_"+result);
    }

    public void removeServerThread(String clientID){
        for(int i = 0; i < Server.serverThreadBus.getServerThreadListSize(); i++){
            if(Server.serverThreadBus.getServerThreadList().get(i).getClientID().equals(clientID)){
              Server.serverThreadBus.serverThreadList.remove(i);
            }
        }
    }
    // Single Chat (Chat to Client with the ClientID.
    public void singleChat(String clientID, String message ){
        for(ServerThread serverThread : Server.serverThreadBus.getServerThreadList()){
            if(serverThread.getClientID().equals(clientID)){
                try{
                    serverThread.writeMessage(message);
                    break;
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
