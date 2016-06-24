package com.mygdx.game.testpackage;

import com.mygdx.game.com.TCPServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.io.OutputStream;
import java.net.Socket;

public class TestServer
{
    private static String        _message = "Hello I'm your server.";
    private static int           _port;
    private static TCPServer _serverSocket;

    public static void main(String[] args)
    {
        try
        {
            _port   = (args.length == 1) ? Integer.parseInt(args[0]) : 42666;
            _serverSocket = new TCPServer(_port);

            System.out.println("TCP server is running on "+_serverSocket.getSocket().getInetAddress()+":" + _port + "...");

            while (true)
            {
                String mess = _serverSocket.acceptNewClient();
                if(mess != null)
                {try {
                    Socket sock = _serverSocket.getLastClient();
                    System.out.println("Received : " + mess + "\nfrom " + sock.getLocalAddress().getHostName() + ":" + sock.getLocalPort()
                            + "\n" + sock.getInetAddress().getHostName() + ":" + sock.getPort());
                    int n = _serverSocket.getClients().size()-1;
                    Socket last = _serverSocket.getClients().get(n);
                    _serverSocket.send("Bienvenue", last);
                    int i=0;
                    while(i < n){
                        _serverSocket.send("Un nouveau !", _serverSocket.getClients().get(i));
                        i++;
                    }
                }catch(Exception e){

                }
                }
                /*
                // Accept new TCP client
                Socket client       = _socket.accept();
                // Open output stream
                OutputStream output = client.getOutputStream();

                System.out.println("New client, address " + client.getInetAddress() + " on " + client.getPort() + ".");

                // Write the message and close the connection
                output.write(_message.getBytes());
                client.close();*/
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}