/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Sockets;

import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author s287391
 */
public class MainServer implements ISocketEvent {    
    
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
        byte[] bytes = null;
        try{
            bytes = new byte[stream.available()];
            stream.read(bytes, 0, stream.available());
        }
        catch(Exception ex){
        }
        finally{
            if(bytes!=null && bytes.length>0)
                System.out.println(bytes);
        }
    }
    
    @Override
    public void onSend(Connection connection, OutputStream stream){
    }
    
    @Override
    public void onListen(Connection connection){
        System.out.println("proba polaczenia");
    }
    
    public static void main(String args[]) throws Exception {
        Server server = new Server(5432); 
        server.listen();
        server.disconnect();
    }
}
