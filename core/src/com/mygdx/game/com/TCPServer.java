package com.mygdx.game.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
//import com.badlogic.gdx.net.ServerSocket;
//import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.mygdx.game.data.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


/**
 * Created by gregory on 15/03/16.
 */
public class TCPServer {

    private ServerSocketHints hints;
    private int port;
    private ServerSocket serverSocket;
    private String hostname;
    private Socket lastClient;
    private ArrayList<Socket> clients;

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

    public TCPServer(String hostname, int port){
        this(hostname, port, DEFAULT_TIME_OUT, false);
    }

    public TCPServer(String hostname, int port, int timeout) {
        this(hostname, port, timeout, false);
    }

    public TCPServer(String hostname, int port, Boolean reuseAddr) {
        this(hostname, port, DEFAULT_TIME_OUT, reuseAddr);
    }

    public int getPort(){
        return port;
    }

    public ServerSocket getSocket(){
        return serverSocket;
    }

    public TCPServer(String hostname, int port, int timeout, Boolean reuseAddr) {
        this.clients = new ArrayList<Socket>();
        this.port = port;
        /*this.hints = new ServerSocketHints();
        this.hints.acceptTimeout = timeout;
        this.hints.reuseAddress = reuseAddr;
        this.hints.performancePrefBandwidth = 0;
        this.hints.performancePrefConnectionTime = 0;
        this.hints.performancePrefLatency = 0;
        this.hostname = hostname;
        System.out.println("Bind at port "+port);
        serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, hostname, port, this.hints);*/
        // create the server socket
        try {
            hints = new ServerSocketHints();
            this.hints.acceptTimeout = timeout;
            this.hints.reuseAddress = reuseAddr;
            //this.hints.performancePrefBandwidth = 0;
            //this.hints.performancePrefConnectionTime = 0;
            //this.hints.performancePrefLatency = 0;


            // initialize
            serverSocket = new java.net.ServerSocket(port);
            serverSocket.setSoTimeout(timeout);
//            serverSocket.setReuseAddress(true);


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

    public void send(String data, Socket clientSocket) {
        try {
            String message = data + "\n";
            clientSocket.getOutputStream().write(message.getBytes());
            System.out.println("Server: Sent : "+message);
        } catch (IOException e) {
            System.err.println("ServerError: Error sending message to client : " + e.getMessage());
        }
    }

    public String receive() {
        try {
            lastClient =serverSocket.accept();
            if(!clients.contains(lastClient))
                clients.add(lastClient);

            String message = new BufferedReader(new InputStreamReader(lastClient.getInputStream())).readLine();
            System.out.println("Server: Received : "+message);
            return message;
        } catch (IOException e) {
            System.err.println("ServerError: Error receiving message : " + e.getMessage());
        }
        return null;
    }

    public String acceptNewClient() {
        try {
            lastClient =serverSocket.accept();
            if(!clients.contains(lastClient))
                clients.add(lastClient);

            String message = new BufferedReader(new InputStreamReader(lastClient.getInputStream())).readLine();
            //if(Data.debug)
                //System.out.println("Server: Received : "+message);
            return message;
        } catch (IOException e) {
            //System.err.println("ServerError: Error receiving message : " + e.getMessage());
        }
        return null;
    }

    public void respondToLastClient(String data){
        send(data, lastClient);
        try {
            lastClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getLastClient(){
        return lastClient;
    }

    public void setTimeOut(int timeOut){
        hints.acceptTimeout = timeOut;
    }

    public void sendToAllClients(String message) {
        for(Socket sock : clients){
            send(message, sock);
        }
    }

    public void close(){
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disposeAll(){
        for(Socket sock : clients){
            try {
                sock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Socket> getClients() {
        return clients;
    }
}
