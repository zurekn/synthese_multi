package com.mygdx.game.com;

import com.mygdx.game.data.Data;

import java.net.Socket;
import java.util.HashMap;

public class ServerNode implements Runnable {
    private int weight;
    private boolean keepRunning;
    private TCPServer server;
    private TCPClient parentServer;
    private HashMap<String, ServerGame> mapGames;
    private static ServerNode instance;

    private ServerNode(String serverAddress, int serverPort){
        server = new TCPServer(serverAddress, serverPort);
        parentServer = new TCPClient(Data.SERVER_IP, Data.SERVER_PORT, Data.SERVER_TIMEOUT);
        new ServerNodeThread(parentServer);
        weight = 0;
        keepRunning = true;
        mapGames = new HashMap<>();

        run();
    }

    public static ServerNode getInstance(String serverAddress, int serverPort){
        if(instance != null)
            instance = new ServerNode(serverAddress, serverPort);
        return instance;
    }

    public static ServerNode getInstance(){
        if(instance != null)
            return instance;
        else
            throw new NullPointerException();
    }

    public int getWeight(){
        return weight;
    }

    public void setWeight(int value){
        weight = value;
    }

    public HashMap<String, ServerGame> getMapGames(){
        return mapGames;
    }

    @Override
    public void run() {
        String mess;
        while(keepRunning){
            //Handle new clients
            try{
                mess = server.acceptNewClient();
                if(mess != null) {
                    if(mess.startsWith("client")) {
                        Socket socket = server.getLastClient();

                        new ClientNodeThread(socket);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        };
    }

    private class ClientNodeThread implements  Runnable{
        private TCPClient client;
        private boolean keepRunning;

        public ClientNodeThread(Socket socket){
            client = new TCPClient(socket);
            keepRunning = true;

            run();
        }

        @Override
        public void run() {
            String mess;
            while(keepRunning){
                //TODO log
                mess = client.receive();

                if(mess != null){
                    String[] split = mess.split(":");
                    try{
                        if(split[0].equals("logToGame")) {

                        }
                    }catch(ArrayIndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private class ServerNodeThread implements Runnable{
        private TCPClient client;
        private boolean keepRunning;

        public ServerNodeThread(TCPClient client){
            this.client = client;
            keepRunning = true;

            run();
        }

        @Override
        public void run() {
            String mess ;
            int disconnectCount=0;

            client.sendToServer("node");

            while(keepRunning){
                mess = client.receive();

                if(mess!=null){
                    disconnectCount = 0;
                    String[] split = mess.split(":");
                    try {
                        if(split[0].equals("askGame")){
                            //TODO create game
                            new Runnable() {
                                @Override
                                public void run() {
                                    new Server();
                                }
                            };


                        } else if (split[0].equals("getGameAddress")) {
                            //TODO try to log client to game
                        }
                    }catch (ArrayIndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("ServerNode: Disconnected");
                    disconnectCount++;
                    if(disconnectCount>5){
                        keepRunning = false;
                    }
                }
            }
        }
    }
}

