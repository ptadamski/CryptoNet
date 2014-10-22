/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sockets;
import java.io.*;
import java.net.*;
import java.util.*;

public class SocketServer {
    
    int port;
    ServerSocket socket;
    List<SocketConnection> connections;
    
    public class SocketServerListenThread extends Thread
    {
        public SocketServerListenThread(SocketServer srv)
        {
            SocketConnection connection = new SocketConnection(srv);
            srv.connections.add()
        }
        
        public void run()
        {
            while(true)
            {
                Socket client = socket.accept();
            }
            
        }
    }
 
    public SocketServer(int port) throws Exception
    {
        this.port = port;
        this.socket = new ServerSocket(port);
        this.connections = new ArrayList<SocketConnection>();
        
    }
    
    public static void main(String[] args) throws Exception
    {
        SocketServer srv = new SocketServer(5432); 
    }
    
}
