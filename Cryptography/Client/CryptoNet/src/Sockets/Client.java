/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sockets;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client extends Connection {
    
    private int port;
    private String host;
    private Socket socket;    
                 
    public Client(String host, int port) throws Exception {
        this.port = port;
        this.host = host;    
    }
    
    @Override
    public void connect(){
        if(this.host != "" && this.port != -1)
            this.connect(this.host, this.port);
    }
    
    public void connect(String host, int port){
        if(isConnected())
            disconnect();
        try{
            this.socket = new Socket(host, port);
            for(ISocketEvent eventHandler : listeners.values())
                if(eventHandler != null){
                    eventHandler.onConnectionAttempt(this, host, port);
                }
        }
        catch(Exception ex){
            this.disconnect();
        }
        finally{
            for(ISocketEvent eventHandler : listeners.values())
                if(eventHandler != null){
                    eventHandler.onConnect(this, host, port);
                }
            try{
                this.in = this.socket.getInputStream();
                this.out = this.socket.getOutputStream();   
            }
            catch(IOException ex){
                this.disconnect();
            }
            finally{
                this.port = port;
                this.host = host;
                this.receiver.addConnection(this);
            }
        }
    }
    
    @Override
    public void disconnect(){
        try{
            this.socket.close();
        }
        catch(IOException ex){
        }
        finally{
            for(ISocketEvent e : listeners.values())
                if(e != null && this.socket != null){
                    e.onDisconnect(this, this.host, this.port);
                }
            this.receiver.removeConnection(this);
            this.port = -1;
            this.host = "";
            this.socket = null;
        }
    }

    @Override
    public void send(byte[] buf, int length) throws Exception{
        if(false == isConnected())
            connect();
        if(isConnected())          
            out.write(buf, 0, length);
    }

    @Override
    public void receive(byte[] buf, int length) throws Exception {
        if(in.available() >= length){
            in.read(buf, 0, length);
            in.mark(length);
            in.reset();
        }
    }
      
    @Override
    public Boolean isConnected() {
        return socket != null
                && this.socket.isBound() 
                && this.socket.isConnected()
                && false == this.socket.isClosed();
   }
    
    @Override
    public Integer getPort(){
        return this.port;
    }
    
    @Override
    public String getHost(){
        return this.host;
    }
    
    public static void main(String[] args) throws Exception
    {
        //Client client = new Client("localhost",5432);
    }
    
    
}