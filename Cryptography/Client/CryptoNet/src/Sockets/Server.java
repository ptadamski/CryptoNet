/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sockets;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server extends Connection{
    
    int port;
    ServerSocket socket;
    List<Connection> connections;
    ConnectionListenerMonitor acceptor;
     
    public Server(int port) throws Exception
    {
        //this.port = port;
        this.connections = new ArrayList<Connection>();
        this.socket = new ServerSocket(port);
        this.acceptor = new ConnectionListenerMonitor(this);
    }
    
    @Override
    public void connect() throws Exception {
        this.socket = new ServerSocket(port);
        this.receiver.addConnection(this);
    }
    
    @Override
    public void disconnect() throws Exception {
        this.socket.bind(null);
        this.socket.close();      
        this.receiver.removeConnection(this);
    }
    
    @Override
    public void listen()  throws Exception {
        this.receiver.run();
        this.acceptor.run();
    }
    
    @Override
    public void send(byte[] buf, int length) throws Exception {
    }
    
    @Override
    public void receive(byte[] buf, int length) throws Exception {
    }
    
    @Override
    public Integer getPort(){
        return port;
    }
    
    @Override
    public String getHost(){
        return "";
    }
    
    @Override
    public Boolean isConnected(){
        return this.socket != null
                && this.socket.isBound()
                && false == this.socket.isBound();
    }
    
    public static void main(String[] args) throws Exception
    {
        Server srv = new Server(5432); 
    }
        
    public Connection getConnection(int index){
        return connections.get(index);
    }
    
    public int getConnectionsCount(){
        return connections.size();
    }
    
    private class ConnectionListenerMonitor extends Thread
    {
        Server owner;
        Boolean isRunning;

        public ConnectionListenerMonitor(Server owner){
            this.owner = owner;
            this.isRunning = false;
        }

        @Override
        public void run() {
            isRunning = true;
            Socket socket;
            Connection connection;
            
            
            while(isRunning){
                try{
                    socket = owner.socket.accept();
                }
                catch(Exception ex){
                    System.out.println(ex.getMessage());
                }
                finally{
                    
                    owner.connections.add(connection);
                    System.out.println(owner.socket.getInetAddress());
                }
            }
        }
        
        public void halt(){
            isRunning = false;
        }
    }
    
}