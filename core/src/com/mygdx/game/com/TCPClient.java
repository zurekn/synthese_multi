package com.mygdx.game.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by gregory on 15/03/16.
 */
public class TCPClient {
    private SocketHints hints;
    private int port;
    private Socket clientSocket;
    private String hostname;

    private static final int DEFAULT_TIME_OUT = 500;

    public TCPClient(int port){
        this("localhost", port, DEFAULT_TIME_OUT);
    }

    public TCPClient(int port, int timeout) {
        this("localhost", port, timeout);
    }

    public TCPClient(String hostname, int port, int timeout) {
        this.port = port;
        this.hints = new SocketHints();
        this.hints.connectTimeout = timeout;
        this.hints.socketTimeout = 0;
        this.hints.performancePrefBandwidth = 0;
        this.hints.performancePrefConnectionTime = 0;
        this.hints.performancePrefLatency = 0;
        this.hostname = hostname;
        clientSocket = Gdx.net.newClientSocket(Net.Protocol.TCP, hostname, port, this.hints);
    }

    public void sendToServer(String data) {
        try {
            String message = data + "\n";
            clientSocket.getOutputStream().write(message.getBytes());
            Gdx.app.log("Client", "Sent : "+message);
        } catch (IOException e) {
            Gdx.app.log("ClientError", "Error sending message to server: " + e.getMessage());
        }
    }

    public String receive() {
        try {
            String message = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())).readLine();
            Gdx.app.log("Client", "Received :"+message);
            return message;
        } catch (IOException e) {
            Gdx.app.log("ClientError", "Error receiving message from server : " + e.getMessage());
        }
        return null;
    }
}
