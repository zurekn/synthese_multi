package com.mygdx.game.com;

import com.mygdx.game.data.Data;

import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

public class ServerNode implements Runnable {
    private int weight;
    private boolean keepRunning;
    private TCPServer server;
    private TCPClient parentServer;
    private ArrayList<ServerGame> gameList;
    private Thread thread;
    private long timeSinceLastMessageSend ;
    private static ServerNode instance;

    private ServerNode(String serverAddress, int serverPort){
        server = new TCPServer(serverAddress, serverPort);
        parentServer = new TCPClient(Data.SERVER_IP, Data.SERVER_PORT, Data.SERVER_TIMEOUT);
        new ServerNodeThread(parentServer);
        weight = 0;
        timeSinceLastMessageSend = System.currentTimeMillis();
        keepRunning = true;
        gameList = new ArrayList<>();

        thread = new Thread(this);
        thread.start();
    }

    public static ServerNode getInstance(String serverAddress, int serverPort){
        if(instance == null)
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

    public ArrayList<ServerGame> getGameList(){
        return gameList;
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
                        System.out.println("Add client "+socket.getLocalAddress().getHostAddress()+":"+socket.getLocalPort());
                        new ClientNodeThread(socket);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public Thread getThread() {
        return thread;
    }

    private class ClientNodeThread implements  Runnable{
        private TCPClient client;
        private boolean keepRunning;
        private Thread thread;

        public ClientNodeThread(Socket socket){
            client = new TCPClient(socket);
            keepRunning = true;
            thread = new Thread(this);

            thread.start();
        }

        @Override
        public void run() {
            String mess;
            while(keepRunning){

                mess = client.receive();

                if(mess != null){
                    System.out.println(mess);
                    String[] split = mess.split(":");
                    try{
                        if(split[0].equals("logToGame")) {
                            gameList.get(gameList.indexOf(split[1])).logToGame(client.getSocket(), split[2]);
                            System.out.println("log");
                            keepRunning = false;
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
        private Thread thread;

        public ServerNodeThread(TCPClient client){
            this.client = client;
            keepRunning = true;
            thread = new Thread(this);

            thread.start();
        }

        @Override
        public void run() {
            String mess ;

            client.sendToServer("node");

            timeSinceLastMessageSend = System.currentTimeMillis();

            while(keepRunning){
                if((System.currentTimeMillis() - timeSinceLastMessageSend) > (Data.PING_TIMEOUT/2)){
                    client.sendToServer("ping");
                    timeSinceLastMessageSend = System.currentTimeMillis();
                }

                mess = client.receive();

                if(mess!=null){
                    System.out.println("Received:"+mess);
                    String[] split = mess.split(":");
                    try {
                        if(split[0].equals("createGame")){
                            //create game
                            final String id = UUID.randomUUID().toString();
                            Thread t = new Thread( new Runnable() {
                                @Override
                                public void run() {
                                new Server(id, "ServerNode "
                                        +server.getSocket().getInetAddress().getHostAddress()
                                        +":"+server.getPort()+" "+id);
                                }
                            });
                            t.start();

                            String message = "gameId:"
                                    + server.getSocket().getInetAddress().getHostName()
                                    + ":" + server.getPort()
                                    + ":" + id;

                            client.sendToServer(message, true);
                            //server.send(message, client.getSocket());
                        } else if (split[0].equals("getGameAddress")) {
                            String gameId = split[1];
                            ServerGame game = gameList.get(gameList.indexOf(gameId));
                            if(game.connect(client)){
                                keepRunning = false;
                            }else{
                                server.send("Cannot connect to game",client.getSocket());
                            }
                            //TODO try to log client to game
                        } else if (split[0].equals("kill")) {
                            System.err.println("Received kill");
                            server.close();
                            keepRunning = false;
                        }
                    }catch (ArrayIndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                    if(mess.equals("null")){
                        System.err.println("ServerNode: Disconnected");
                        keepRunning = false;
                    }
                }
            }
        }
    }
}

