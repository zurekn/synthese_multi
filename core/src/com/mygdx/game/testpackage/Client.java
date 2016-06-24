package com.mygdx.game.testpackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client
{
    private static int    _port;
    private static Socket _socket;

    public static void main(String[] args)
    {
        String message      = "ping";
        InputStream input   = null;

        try
        {
            _port   = (args.length == 1) ? Integer.parseInt(args[0]) : 42666;
            _socket = new Socket((String) null, _port);

            // Open stream
            input = _socket.getInputStream();

            _socket.getOutputStream().write("coucou1\n".getBytes());
            _socket.getOutputStream().write("coucou2\n".getBytes());

            // Show the server response
            String response = new BufferedReader(new InputStreamReader(input)).readLine();
            System.out.println("Server message: " + response);
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                input.close();
                _socket.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}