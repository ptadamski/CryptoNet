using System;
using System.Net.Sockets;
using System.Net;
using System.Text;
using System.Security.Cryptography;

namespace Multi_Client
{
    class Program
    {
        private static RSACryptoServiceProvider encrypter = null;
        private static RSACryptoServiceProvider decrypter = new RSACryptoServiceProvider();
        private static string pubKeyXml;

        private const int _BUFFER_SIZE = 2048;
        private const int _PORT = 10028;
        private static readonly byte[] _buffer = new byte[_BUFFER_SIZE];


        private static readonly Socket clientSock = new Socket
            (AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
        
        static void Main()
        {
            Console.Title = "Client";
            pubKeyXml = decrypter.ToXmlString(false);
            ConnectToServer();
            RequestLoop();
            Exit();
        }
        
        private static void ConnectToServer()
        {
             int attempts = 0;


            while (!clientSock.Connected)
            {
                try
                {
                    attempts++;
                    Console.WriteLine("Connection attempt " + attempts);
                    //clientSock.BeginConnect(Dns.GetHostAddresses("localhost")[0], _PORT, ConnectCallback, clientSock);
                    clientSock.Connect(Dns.GetHostAddresses("localhost")[0], _PORT);
                    KeyExchangeGet(clientSock);
                    KeyExchangePut(clientSock);
                }
                catch (SocketException e) 
                {
                    Console.Clear();
                    Console.WriteLine(e.ErrorCode);

                }
            }

            //Console.Clear();
            Console.WriteLine("Connected");
        }



        //trzeba zastapic procedurami asynchronicznymi
        private static void RequestLoop()
        {
            while (true)
                SendRequest();
        }

        /// <summary>
        /// Close socket and exit app
        /// </summary>
        private static void Exit()
        {
            clientSock.Shutdown(SocketShutdown.Both);
            clientSock.Close();
            Environment.Exit(0);
        }

        private static void SendRequest()
        {
            string request = Console.ReadLine();
            SendString(request);
        }

        /// <summary>
        /// Sends a string to the server with ASCII encoding
        /// </summary>
        private static void SendString(string text)
        {
            byte[] data = Encoding.ASCII.GetBytes(text);
            clientSock.Send(CryptoDotNet.Cryptography.EncryptRSA(encrypter, data));
            //clientSock.Send(CryptoDotNet.Cryptography.EncryptDES(encDES, data));

        }

        private static void KeyExchangePut(Socket socket)
        {
            byte[] data = Encoding.ASCII.GetBytes(pubKeyXml);
            //des wysylamy po prostu klucz
            socket.Send(data);
        }

        private static void KeyExchangeGet(Socket socket)
        {
            socket.BeginReceive(_buffer, 0, _BUFFER_SIZE, SocketFlags.None, KeyExchangeGetCallback, socket);
        }

        #region Callback

        private static void KeyExchangeGetCallback(IAsyncResult ar)
        {
            Socket socket = (Socket) ar.AsyncState;

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

                DESCryptoServiceProvider des = new DESCryptoServiceProvider();
                //AsymmetricKeyExchangeFormatter kex = new ;
                //des.
                //des.Key = new byte[length];
                //Array.Copy(_buffer, des.Key, length);

                RSACryptoServiceProvider rsa = new RSACryptoServiceProvider();
                try
                {
                    rsa.FromXmlString(xmlString);
                }
                catch (Exception)
                {
                }
                finally
                {
                    encrypter = rsa;
                }
            }

            socket.BeginReceive(_buffer, 0, _BUFFER_SIZE, SocketFlags.None, ReceiveCallback, socket);
        }

        //private static void DisconnectCallback(IAsyncResult ar)
        //{
        //    Socket socket = (Socket) ar.AsyncState;
        //    socket.EndDisconnect(ar);

        //    socket.BeginConnect(socket.RemoteEndPoint, ConnectCallback, socket);
        //}

        //private static void ConnectCallback(IAsyncResult ar)
        //{
        //    Socket socket = (Socket) ar.AsyncState;
        //    socket.EndConnect(ar);

        //    KeyExchangeGet(socket);
        //    KeyExchangePut(socket);

        //    //socket.BeginDisconnect(false, DisconnectCallback, socket);
        //}

        private static void ReceiveCallback(IAsyncResult ar)
        {
            Socket current = (Socket) ar.AsyncState;
            int length = 0;

            try
            {
                length = current.EndReceive(ar);
            }
            catch (SocketException)
            {
            }
            finally
            {
                byte[] recBuf = new byte[length];
                Array.Copy(_buffer, recBuf, length);
                string text = Encoding.ASCII.GetString(recBuf);
                Console.WriteLine("Received Text: " + text);
            }

            current.BeginReceive(_buffer, 0, _BUFFER_SIZE, SocketFlags.None, ReceiveCallback, current);
        }

        #endregion
    }
}
