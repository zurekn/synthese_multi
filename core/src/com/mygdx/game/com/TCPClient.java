package com.mygdx.game.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.mygdx.game.data.Data;

import java.net.InetSocketAddress;
import java.net.Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;


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
            //InetSocketAddress address = new InetSocketAddress(hostname, port);
            clientSocket = new java.net.Socket(hostname, port);
            //clientSocket.bind(address);
            clientSocket.setSoTimeout(timeout);

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
        sendToServer(data,false);
    }

    public void sendToServer(String data, boolean print){
        try {
            String message = data + "\n";
            clientSocket.getOutputStream().write(message.getBytes());
            if(print)
                System.out.println("Client : Sent : "+message);
        } catch (IOException e) {
            System.err.println("ClientError : Error sending message to server: " + e.getMessage());
        }
    }

    public Socket getSocket(){
        return clientSocket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        Socket socket = new Socket();
        if (o == null || (clientSocket.getClass() != o.getClass() && socket.getClass() != o.getClass())) return false;

        try {
            TCPClient client = (TCPClient) o;
            socket = client.getSocket();
        }catch(ClassCastException e){
            socket = (Socket) o;
        }

        if(!socket.getInetAddress().getHostAddress().equals(clientSocket.getInetAddress().getHostAddress())) return false;
        if(!socket.getInetAddress().getHostName().equals(clientSocket.getInetAddress().getHostName())) return false;
        if(socket.getPort() != clientSocket.getPort()) return false;
        if(socket.getLocalPort() != clientSocket.getLocalPort()) return false;
        if(!socket.getLocalAddress().getHostAddress().equals(clientSocket.getLocalAddress().getHostAddress())) return false;
        if(!socket.getLocalAddress().getHostName().equals(clientSocket.getLocalAddress().getHostName())) return false;

        return true;
    }

    public String receive() {
        try {
            clientSocket.setSoTimeout(Data.SERVER_TIMEOUT);
            String message = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())).readLine();
            //System.out.println("Client: Received :"+message);
            return message;
        } catch (IOException e) {
            //System.err.println("ClientError: Error receiving message from server : " + e.getMessage());
        }
        return null;
    }

    public void close() {
        try {
            clientSocket.close();
        } catch (IOException e) {}

    }
}
