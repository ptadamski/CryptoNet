using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Security.Cryptography;

namespace Multi_Server
{
    class Program
    {
        //sockets
        private static Socket serverSock;
        private static readonly List<Socket> clientSock = new List<Socket>();
        private const int _BUFFER_SIZE = 2048;
        private const int _PORT = 10028;
        private static readonly byte[] _buffer = new byte[_BUFFER_SIZE];

        //cryptography
        private static readonly Dictionary<Socket, RSA> encryptor = new Dictionary<Socket, RSA>();
        private static RSACryptoServiceProvider decryptor = new RSACryptoServiceProvider();
        private static string pubKeyXml;


        static void Main()
        {
            Console.Title = "Server";
            pubKeyXml = decryptor.ToXmlString(false);
            SetupServer();
            Console.ReadLine(); // When we press enter close everything
            CloseAllSockets();
        }

        private static void SetupServer()
        {
            Console.WriteLine("Setting up server...");
            serverSock = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            serverSock.Bind(new IPEndPoint(IPAddress.Any, _PORT));
            serverSock.Listen(5);
            serverSock.BeginAccept(AcceptCallback, null);
            Console.WriteLine("Server setup complete");
        }

        /// <summary>
        /// Close all connected client (we do not need to shutdown the server socket as its connections
        /// are already closed with the clients)
        /// </summary>
        private static void CloseAllSockets()
        {
            foreach (var item in clientSock)
            {
                item.Shutdown(SocketShutdown.Both);
                item.Close();
            }

            encryptor.Clear();
            serverSock.Close();
        }

        private static void KeyExchangePut(Socket socket)
        {
            byte[] data = Encoding.ASCII.GetBytes(pubKeyXml);
            socket.Send(data);
        }

        private static void KeyExchangeGet(Socket socket) 
        {
            socket.BeginReceive(_buffer, 0, _BUFFER_SIZE, SocketFlags.None, KeyExchangeGetCallback, socket);
        }
        
        #region Callback

        private static void AcceptCallback(IAsyncResult AR)
        {
            Socket socket;

            try
            {
                socket = serverSock.EndAccept(AR);
            }
            catch (ObjectDisposedException) // I cannot seem to avoid this (on exit when properly closing sockets)
            {
                return;
            }

            clientSock.Add(socket);
            Console.WriteLine("Client connected...");

            //KeyExchange
            Console.WriteLine("Key exchanging...");
            KeyExchangeGet(socket);
            KeyExchangePut(socket);

            serverSock.BeginAccept(AcceptCallback, null);
        }

        //private static void KeyExchangePutCallback(IAsyncResult ar)
        //{
        //    //TO DO
        //    Socket socket = (ar as Socket);
        //    SocketAsyncEventArgs e  = new SocketAsyncEventArgs();
        //    byte[] data = Encoding.ASCII.GetBytes(pubKeyXml);
        //    e.SetBuffer(data, 0, data.Length);

        //    bool isDone = socket.SendAsync(e);
        //    socket.EndSend(ar);
        //}

        private static void KeyExchangeGetCallback(IAsyncResult ar) 
        {
            Socket socket = (Socket)ar.AsyncState;

            int length = 0;

            try
            {
                length = socket.EndReceive(ar);
            }
            catch (SocketException)
            {
            }
            finally
            {
                string xmlString = Encoding.ASCII.GetString(_buffer, 0, length);
                Console.WriteLine("Received Key: " + xmlString);
                RSACryptoServiceProvider rsa = new RSACryptoServiceProvider();
                try
                {
                    rsa.FromXmlString(xmlString);
                }
                catch(Exception)
                {
                }
                finally 
                {
                    encryptor.Add(socket, rsa);
                }
            }

            socket.BeginReceive(_buffer, 0, _BUFFER_SIZE, SocketFlags.None, ReceiveCallback, socket);
        }

        private static void ReceiveCallback(IAsyncResult AR)
        {
            Socket current = (Socket)AR.AsyncState;
            int received;

            try
            {
                received = current.EndReceive(AR);
            }
            catch (SocketException)
            {
                Console.WriteLine("Client forcefully disconnected");
                current.Close(); // Dont shutdown because the socket may be disposed and its disconnected anyway
                clientSock.Remove(current);
                encryptor.Remove(current);
                return;
            }

            byte[] recBuf = new byte[received];
            Array.Copy(_buffer, recBuf, received);
            string text = Encoding.ASCII.GetString(CryptoDotNet.Cryptography.DecryptRSA(decryptor, recBuf));
            Console.WriteLine("Received Text: " + text);




            /*if (text.ToLower() == "get time") // Client requested time
            {
                Console.WriteLine("Text is a get time request");
                byte[] data = Encoding.ASCII.GetBytes(DateTime.Now.ToLongTimeString());
                current.Send(data);
                Console.WriteLine("Time sent to client");
            }
            else if (text.ToLower() == "exit") // Client wants to exit gracefully
            {
                // Always Shutdown before closing
                current.Shutdown(SocketShutdown.Both);
                current.Close();
                clientSock.Remove(current);
                Console.WriteLine("Client disconnected");
                return;
            }
            else
            {
                Console.WriteLine("Text is an invalid request");
                byte[] data = Encoding.ASCII.GetBytes("Invalid request");
                current.Send(data);
                Console.WriteLine("Warning Sent");
            }*/

            current.BeginReceive(_buffer, 0, _BUFFER_SIZE, SocketFlags.None, ReceiveCallback, current);
        }

        #endregion
    }
}
