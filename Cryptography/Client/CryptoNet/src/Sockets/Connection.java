/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sockets;

import java.io.*;
import java.net.*;
import java.util.*;


public class Connection{
    
    public void connect() throws Exception {
    }
    
    public void disconnect() throws Exception {
    }
    
    public void listen()  throws Exception {
        this.receiver.run();
    }
    
    public void send(byte[] buf, int length) throws Exception {
    }
    
    public void receive(byte[] buf, int length) throws Exception {
    }
    
    public Integer getPort() {
        return -1;
    }
    
    public String getHost() {
        return "";
    }
    
    public Boolean isConnected() {
        return false;
    }
        
    public void addListener(ISocketEvent handler){
        listeners.put(handler.hashCode(), handler);
    }
    
    public void removeListener(ISocketEvent handler){
        listeners.remove(handler.hashCode());
    }

    protected HashMap<Integer, ISocketEvent> listeners = new HashMap<>();
    private int lastAvailable = 0;
    protected static StreamInputMonitor receiver = new StreamInputMonitor();
    protected InputStream in;
    protected OutputStream out;
        
    protected static class StreamInputMonitor extends Thread {
        
        private Collection<Connection> connections;
        private Boolean isRunning = false;
        
        public StreamInputMonitor(){
            this.connections = new HashSet<Connection>();
        }
        
        public void addConnection(Connection connection){
            this.connections.add(connection);
        }
        
        public void removeConnection(Connection connection){
            this.connections.remove(connection);
        }
        
        @Override
        public void run(){
            if(false == isRunning){                
                isRunning = true;
                while(isRunning)
                {
                    for(Connection connection:connections)
                        try{
                            if(connection.in.available() > 0
                            && connection.in.available() != connection.lastAvailable ){
                                for(ISocketEvent e : connection.listeners.values())
                                    if(e != null){
                                        e.onReceive(connection, connection.in);
                                    }
                                connection.lastAvailable = connection.in.available();
                            }
                        }
                        catch(IOException ex){
                            continue;
                        }
                        try{
                            Thread.sleep(20);
                        }
                        catch(InterruptedException ex){
                            continue;
                        }
                }
            }
        }
        
        public void halt() {
            isRunning = false;
        }
    }
}