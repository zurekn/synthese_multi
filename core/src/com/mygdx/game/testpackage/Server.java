package com.mygdx.game.testpackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.io.OutputStream;
import java.net.Socket;

public class Server
{
    private static String        _message = "Hello I'm your server.";
    private static int           _port;
    private static ServerSocket  _socket;

    public static void main(String[] args)
    {
        try
        {
            _port   = (args.length == 1) ? Integer.parseInt(args[0]) : 42666;
            _socket = new ServerSocket(_port);

            System.out.println("TCP server is running on " + _port + "...");

            while (true)
            {
                // Accept new TCP client
                Socket client       = _socket.accept();
                // Open output stream
                OutputStream output = client.getOutputStream();

                System.out.println("New client, address " + client.getInetAddress() + " on " + client.getPort() + ".");

                int n = 0;
                do {
                    System.out.println(n);
                    System.out.println("Received " + new BufferedReader(new InputStreamReader(client.getInputStream())).readLine());
                    n++;

                }while(n <2);
                // Write the message and close the connection
                output.write(_message.getBytes());
                client.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                _socket.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}