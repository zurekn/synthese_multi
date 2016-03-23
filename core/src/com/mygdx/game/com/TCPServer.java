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
public class TCPServer {

    private ServerSocketHints hints;
    private int port;
    private ServerSocket serverSocket;
    private String hostname;
    private Socket lastClient;

    private static final int DEFAULT_TIME_OUT = 500;

    public TCPServer(int port){
        this("localhost", port, DEFAULT_TIME_OUT, false);
    }

    public TCPServer(int port, int timeout) {
        this("localhost", port, timeout, false);
    }

    public TCPServer(int port, Boolean reuseAddr) {
        this("localhost", port, DEFAULT_TIME_OUT, reuseAddr);
    }

    public TCPServer(String hostname, int port, int timeout) {
        this(hostname, port, timeout, false);
    }

    public TCPServer(String hostname, int port, Boolean reuseAddr) {
        this(hostname, port, DEFAULT_TIME_OUT, reuseAddr);
    }

    public TCPServer(String hostname, int port, int timeout, Boolean reuseAddr) {
        this.port = port;
        this.hints = new ServerSocketHints();
        this.hints.acceptTimeout = timeout;
        this.hints.reuseAddress = reuseAddr;
        this.hints.performancePrefBandwidth = 0;
        this.hints.performancePrefConnectionTime = 0;
        this.hints.performancePrefLatency = 0;
        this.hostname = hostname;
        serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, hostname, port, this.hints);
    }

    public void send(String data, Socket clientSocket) {
        try {
            String message = data + "\n";
            clientSocket.getOutputStream().write(message.getBytes());
            Gdx.app.log("Server", "Sent : "+message);
        } catch (IOException e) {
            Gdx.app.log("ServerError", "Error sending message to client : " + e.getMessage());
        }
    }

    public String receive() {
        try {
            SocketHints clientHints = new SocketHints();
            lastClient = serverSocket.accept(null);
            String message = new BufferedReader(new InputStreamReader(lastClient.getInputStream())).readLine();
            Gdx.app.log("Server",  "Received : "+message);
            return message;
        } catch (IOException e) {
            Gdx.app.log("ServerError", "Error receiving message : " + e.getMessage());
        }
        return null;
    }

    public void respondToLastClient(String data){
        send(data, lastClient);
    }

    public Socket getLastClient(){
        return lastClient;
    }
}