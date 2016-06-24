package com.mygdx.game.com;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;


public class SocketThread implements Runnable {
    private TCPClient clientSocket;
    private boolean keepRunning;
    private ServerMain parent;
    private Thread thread;
    private ArrayList<String> messagesToSend;


    public SocketThread(ServerMain parent,Socket socket){
        this.clientSocket = new TCPClient(socket);
        messagesToSend = new ArrayList<>();
        this.keepRunning = true;
        this.parent = parent;
        thread = new Thread(this);

        thread.start();
    }

    private void askGame(Socket socket){
        if(parent.askGame(socket)){
            //this.end();
        }
    }

    private void askGame(Socket socket, String gameId){
        try {
            parent.askGame(socket, gameId);
                //this.end();

        } catch (GameNotFoundException e) {
            e.printStackTrace();
        } catch (GameFullException e) {
            e.printStackTrace();
        }
    }

    //TODO handle connexion lost

    @Override
    public void run() {
        while(keepRunning){
            synchronized (messagesToSend){
                if(!messagesToSend.isEmpty()){
                    clientSocket.sendToServer(messagesToSend.get(0), true);
                    if(messagesToSend.get(0).startsWith("gameNode"))
                        parent.remove(clientSocket.getSocket());
                    messagesToSend.remove(0);
                }
            }
            String message = receive();
            if(message!=null){
                String[] split = message.split(":");
                try {
                    if (split[0].equals("game")) {
                        if(split.length>1){
                            askGame(clientSocket.getSocket(), split[1]);
                        }else{
                            askGame(clientSocket.getSocket());
                        }
                    }
                }catch (ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }
            }

        }
    }

    public void end(){
        try {
            clientSocket.getSocket().close();

            keepRunning = false;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public TCPClient getClient(){
        return clientSocket;

    }

    public void addMessage(String mess){
        synchronized (messagesToSend){
            messagesToSend.add(mess);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (clientSocket.equals(o)) return true;

        if (o == null || getClass() != o.getClass())  return false;

        SocketThread that = (SocketThread) o;

        if (keepRunning != that.keepRunning) return false;
        return clientSocket.equals(that.clientSocket);
    }

    private String receive(){
        return clientSocket.receive();
    }

    public void kill(){
        clientSocket.close();
        keepRunning = false;
    }

    @Override
    public String toString() {
        return clientSocket.toString();
    }
}
