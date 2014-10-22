/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sockets;
import java.net.Socket;
import java.nio.Buffer;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author progger
 */
public interface ISocketListener 
{
    void onConnect(Socket socket);
    void onDisconnect(Socket socket);
    void onReceive(Socket socket, InputStream stream);
    void onSend(Socket socket, OutputStream stream);
    void onError(Socket socket);
    void onListen(Socket socket);
}
