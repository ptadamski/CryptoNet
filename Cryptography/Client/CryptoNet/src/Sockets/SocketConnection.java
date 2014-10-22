/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sockets;

import java.io.*;
import java.net.*;


public class SocketConnection
{
    public SocketConnection(Socket socket) throws Exception
    {
        this.socket = socket;
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.output = new PrintWriter(socket.getOutputStream(),true);
    }
    
    private BufferedReader input;
    private PrintWriter output;
    private Socket socket;   

    void send(char[] buffer, int length){
        output.write(buffer, 0, length);
    }

    void receive(int length, char[] buffer) throws Exception {
        input.read(buffer, 0, length);
        input.mark(0);
        input.reset();
    }

    Socket getSocket(){
        return socket;
    }
    
    void close() throws Exception
    {
        socket.close();
    }
    
    void open() throws Exception
    {
        socket.connect(socket.getRemoteSocketAddress());
    }

}