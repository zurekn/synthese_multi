package com.mygdx.game.testpackage;

import com.mygdx.game.com.TCPClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestClient
{
    private static int    _port;
    private static Socket _socket;

    public static void main(String[] args)
    {
        String message      = "ping";
        InputStream input   = null;

        _port   = (args.length == 1) ? Integer.parseInt(args[0]) : 42666;

        Socket socket = null;
        try {
            socket = new Socket("localhost", _port);

            TCPClient client = new TCPClient(socket);

            client.sendToServer("coucou");
            while(true){
                String mes =  client.receive();
                System.out.println("Received : "+mes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Show the server response
        // String response = new BufferedReader(new InputStreamReader(input)).readLine();
        //System.out.println("Server message: " + response);


    }
}