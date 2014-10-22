/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sockets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author progger
 */


public class SocketClient
{
    private class SocketClientService extends Thread
    {
        public SocketClientService(String name)
        {
            super(name);
        }
        
        public void run()
        {
            while(true)
            {
            }
        }
    }
    
    private List<ISocketListener> listeners;    
    private String host;
    private int port;
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private SocketClientService service;
    
    public SocketClient(String host, int port) throws Exception
    {
        this.listeners = new ArrayList<ISocketListener>();
        this.host = host;
        this.port = port;
        this.socket = new Socket(host, port);        
        this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.output = new PrintWriter(this.socket.getOutputStream());
    }
    
    public void addListener(ISocketListener handler){
        listeners.add(handler);
    }
    
    public void removeListener(ISocketListener handler){
        listeners.remove(handler);
    }
    
    private void doRead()
    {
        //socket.getInputStream().
        for(ISocketListener handler:listeners)
        {
            //handler.onRead(socket, );
        }
    }
    
    private void doWrite()
    {
        for(ISocketListener handler:listeners)
        {
            //handler.onRead(socket, );
        }
    }
    
    //public void 
    
    public static void main(String[] args) throws Exception
    {   
        try
        {
            SocketClient client = new SocketClient("localhost",5432);
        }
        catch(Exception e)
        {
            System.out.println("error " + e.getMessage());
        }
        /*switch(args.length)
        {
            case 2:   
                new Client(args[0], Integer.parseInt(args[1]));
                break;
            case 1:
                new Client("127.0.0.1", Integer.parseInt(args[0]));
                break;
            default :
                break;
        }*/
    }
    
}