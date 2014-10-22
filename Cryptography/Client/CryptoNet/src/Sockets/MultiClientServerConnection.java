/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sockets;

import java.io.*;
import java.net.*;


public class MultiClientServerConnection
{
    public MultiClientServerConnection(Socket socket) throws Exception
    {
        this.socket = socket;
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.output = new PrintWriter(socket.getOutputStream(),true);
    }
    
    private BufferedReader input;
    private PrintWriter output;
    private Socket socket;   

    public void send(char[] buffer, int length){
        output.write(buffer, 0, length);
    }

    public void receive(int length, char[] buffer) throws Exception {
        input.read(buffer, 0, length);
        input.mark(0);
        input.reset();
    }

    public Socket getSocket(){
        return socket;
    }
    
    public void close() throws Exception
    {
        socket.close();
    }
    
    public void open() throws Exception
    {
        socket.connect(socket.getRemoteSocketAddress());
    }

}