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
    List<Socket> connections;
    ConnectionListenerMonitor acceptor;
     
    public Server(int port) throws Exception
    {
        //this.port = port;
        this.connections = new ArrayList<Socket>();
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
        System.out.println("listen");
        this.receiver.run();
        System.out.println("after receiver");
        this.acceptor.run();
    }
    
    @Override
    public void send(byte[] buf, int length) throws Exception {
        if(false == isConnected())
            connect();
        if(isConnected())          
            for(Socket socket:connections){
                socket.getOutputStream().write(buf, 0, length);
                socket.getOutputStream().flush();
            }
    }
    
    @Override
    public void receive(byte[] buf, int length) throws Exception {
        for(Socket socket:connections)
            if(socket.getInputStream().available() >= length){
                socket.getInputStream().read(buf, 0, length);
                socket.getInputStream().mark(length);
                socket.getInputStream().reset();
        }
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
        
    public Socket getConnection(int index){
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
            System.out.println("run listener");
            isRunning = true;
            Socket socket = null;
            System.out.println("sdfsfsdfsd");
            while(isRunning){
                try{
                    socket = owner.socket.accept();
                    System.out.println("34567567");
                }
                catch(Exception ex){
                    //System.out.println(ex.getMessage());
                }
                finally{
                    
                    owner.connections.add(socket);
                    //System.out.println(owner.socket.getInetAddress());
                }
            }
        }
        
        public void halt(){
            isRunning = false;
        }
    }
    
}