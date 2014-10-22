/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sockets;
import java.io.*;
import java.net.*;
import java.util.*;

public class MultiClientServer {
    
    int port;
    ServerSocket socket;
    List<MultiClientServerConnection> connections;
    static ServerListener listener;
     
    public MultiClientServer(int port) throws Exception
    {
        //this.port = port;
        this.connections = new ArrayList<MultiClientServerConnection>();
        this.socket = new ServerSocket(port);
        this.listener = new ServerListener(this);
        this.listener.run();
    }
    
    public static void main(String[] args) throws Exception
    {
        MultiClientServer srv = new MultiClientServer(5432); 
    }
    
    public MultiClientServerConnection getConnection(int index){
        return connections.get(index);
    }
    
    public int getConnectionCount(){
        return connections.size();
    }
    
    private class ServerListener extends Thread
    {
        MultiClientServer owner;

        public ServerListener(MultiClientServer owner){
            this.owner = owner;
        }

        @Override
        public void run() {
            while(true){
                MultiClientServerConnection client = null;
                try{
                    client = new MultiClientServerConnection(socket.accept());
                }
                catch(Exception ex){
                    System.out.println(ex.getMessage());
                }
                finally{
                    owner.connections.add(client);
                    System.out.println(client.getSocket().getInetAddress());
                }
            }
        }
    }
    
}