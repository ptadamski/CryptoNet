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
public interface ISocketEvent 
{
    void onConnectionAttempt(Connection connection, String host, int port);
    void onConnect(Connection connection, String host, int port);
    void onDisconnect(Connection connection, String host, int port);
    void onReceive(Connection connection, InputStream stream);
    void onSend(Connection connection, OutputStream stream);
    void onListen(Connection connection);
}
