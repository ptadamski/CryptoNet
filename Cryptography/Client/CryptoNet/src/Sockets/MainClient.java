/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Sockets;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author s287391
 */
public class MainClient implements ISocketEvent {
    
    
    @Override
    public void onConnectionAttempt(Connection connection, String host, int port){
    }
    
    @Override
    public void onConnect(Connection connection, String host, int port){
    }
    
    @Override
    public void onDisconnect(Connection connection, String host, int port){
    }
    
    @Override
    public void onReceive(Connection connection, InputStream stream){
    }
    
    @Override
    public void onSend(Connection connection, OutputStream stream){
    }
    
    @Override
    public void onListen(Connection connection){
    }
    
    public static void main(String args[]) throws Exception {
        Client client = new Client("localhost",5432);
        client.listen();
        
        java.util.Scanner scanner = new java.util.Scanner(System.in);      
        
        
        while(true){
            String s = "some string that shall be send";
            byte[] bytes = s.getBytes();
            client.send(bytes, bytes.length);
        }
    }
    
}
