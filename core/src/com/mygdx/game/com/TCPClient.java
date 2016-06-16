package com.mygdx.game.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.net.InetSocketAddress;
import java.net.Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class TCPClient {
    private SocketHints hints;
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
        this.hints = new SocketHints();
        this.hints.connectTimeout = timeout;
        this.hints.socketTimeout = 0;
        this.hints.performancePrefBandwidth = 0;
        this.hints.performancePrefConnectionTime = 0;
        this.hints.performancePrefLatency = 0;
        this.hostname = hostname;
        //clientSocket = Gdx.net.newClientSocket(Net.Protocol.TCP, hostname, port, this.hints);

        try {
            //this.hints.acceptTimeout = timeout;
           // this.hints.reuseAddress = reuseAddr;
            //this.hints.performancePrefBandwidth = 0;
            //this.hints.performancePrefConnectionTime = 0;
            //this.hints.performancePrefLatency = 0;


            // initialize
            InetSocketAddress address = new InetSocketAddress(hostname, port);
            clientSocket = new java.net.Socket();
            clientSocket.bind(address);
            clientSocket.setSoTimeout(60000);

            /*if (hints != null) {
                serverSocket.setPerformancePreferences(hints.performancePrefConnectionTime,
                        hints.performancePrefLatency,
                        hints.performancePrefBandwidth);
                serverSocket.setReuseAddress(hints.reuseAddress);

                serverSocket.setReceiveBufferSize(hints.receiveBufferSize);
            }*/
            //serverSocket.setSoTimeout(hints.acceptTimeout);
            // and bind the server...
            //InetSocketAddress address = new InetSocketAddress(hostname, port);

            //  serverSocket.bind(address);

        }
        catch (Exception e) {
            throw new GdxRuntimeException("Cannot create a server socket at port " + port + ".", e);
        }
    }

    public TCPClient(Socket socket){
        clientSocket = socket;
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

    public Socket getSocket(){
        return clientSocket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || clientSocket.getClass() != o.getClass()) return false;

        TCPClient client = (TCPClient) o;
        Socket socket = client.getSocket();

        if(!socket.getInetAddress().getHostAddress().equals(clientSocket.getInetAddress().getHostAddress())) return false;
        if(!socket.getInetAddress().getHostName().equals(clientSocket.getInetAddress().getHostName())) return false;
        if(socket.getPort() != clientSocket.getPort()) return false;
        if(socket.getLocalPort() != clientSocket.getLocalPort()) return false;
        if(socket.getLocalAddress().getHostAddress().equals(clientSocket.getLocalAddress().getHostAddress())) return false;
        if(socket.getLocalAddress().getHostName().equals(clientSocket.getLocalAddress().getHostName())) return false;

        return true;
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
