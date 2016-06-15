package com.mygdx.game.com;

import java.io.IOException;
import java.net.Socket;


public class SocketThread implements Runnable {
    private TCPClient clientSocket;
    private boolean keepRunning;
    private ServerMain server;

    public SocketThread(ServerMain server,Socket socket){
        this.clientSocket = new TCPClient(socket);
        this.keepRunning = true;
        this.server = server;

        run();
    }

    private void askGame(Socket socket){
        if(server.askGame(socket)){
            this.end();
        }
    }

    private void askGame(Socket socket, String gameId){
        try {
            if(server.askGame(socket, gameId)){
                this.end();
            }
        } catch (GameNotFoundException e) {
            e.printStackTrace();
        } catch (GameFullException e) {
            e.printStackTrace();
        }
    }

    //TODO handle connexion lost

    @Override
    public void run() {
        int disconnectCount = 0;
        while(keepRunning){
            String message = receive();
            if(message!=null){
                disconnectCount = 0;
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
            } else {
                System.err.println("ServerMain: Disconnected");
                disconnectCount++;
                if(disconnectCount>5){
                    keepRunning = false;
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
        keepRunning = false;
    }
}
