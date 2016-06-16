package com.mygdx.game.com;

import com.mygdx.game.data.Data;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;


public class ServerMain implements Runnable {
    private ArrayList<ServerNodeThread> nodeList;
    private TCPServer server;
    private ArrayList<SocketThread> socketClients;
    private HashMap<String,ServerNodeThread> gameIDMap;
    private ReentrantLock socketLock ;
    private ReentrantLock mapLock;


    private boolean keepRunning;

    public ServerMain(){
        nodeList = new ArrayList<>();
        socketClients = new ArrayList<>();
        gameIDMap = new HashMap<>();
        server = new TCPServer(Data.SERVER_PORT, Data.SERVER_TIMEOUT);
        keepRunning = true;

        socketLock = new ReentrantLock(true);
        mapLock = new ReentrantLock(true);

        run();
    }

    public void startNode(){
        //try {
        //    Process p = Runtime.getRuntime().exec("ssh....");
        //
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
    }

    public void addNode(Socket socket){
        nodeList.add(new ServerNodeThread(this, server.getLastClient()));
    }

    public void addClient(Socket socket){
        socketClients.add(new SocketThread(this, socket));
    }

    public synchronized void addGame(ServerNodeThread node, String gameId, String mess, Socket client){
        gameIDMap.put(gameId, node);
        server.send("gameNode:"+mess, client);

        socketClients.remove(new TCPClient(client));
    }

    public synchronized void addClient(ServerNodeThread node, String gameId, String mess, Socket client){
        server.send("gameNode:"+mess, client);

        socketClients.remove(new TCPClient(client));
    }

    public boolean askGame(Socket client){
        //Select the node with less weight
        ServerNodeThread selectedNode = null;
        int weight = Integer.MAX_VALUE;

        for(ServerNodeThread node : nodeList){
            if(node.getWeight()<weight){
                selectedNode = node;
                weight = node.getWeight();
            }
        }

        //nodeList.get(nodeList.indexOf(client));
        //String address = selectedNode.getSocket().getInetAddress().getHostAddress();
        //int port = selectedNode.getSocket().getPort();

        //TODO
        selectedNode.addMessage("createGame", client);
        /*String res = selectedNode.askGame();
        if(res == null){
            //TODO game not created
        }else if(res.split(":").length == 3){
            String[] split = res.split(":");

            gameIDMap.put(split[2], selectedNode);
            server.send("gameNode:"+res, client);

            socketClients.remove(new TCPClient(client));

        }*/
        return false;
    }

    public boolean askGame(Socket client, String gameId) throws GameNotFoundException, GameFullException {
        if(gameIDMap.get(gameId) == null) {
            throw new GameNotFoundException("This game does not exist");
        }else {
            //if (!gameIDMap.get(gameId).addClient(gameId, client))
            //   throw new GameFullException("No room left for this game");
            ServerNodeThread node ;
            mapLock.lock();
            node = gameIDMap.get(gameId);
            mapLock.unlock();
            if(node != null){
                node.addMessage("getAddress:"+gameId, client);
            }
        }
        return false;
    }

    @Override
    public void run() {
        String mess;
        do{
            //Handle new clients
            try{
                mess = server.acceptNewClient();
                if(mess != null) {
                    if(mess.startsWith("client")) {
                        socketLock.lock();
                        addClient(server.getLastClient());
                        socketLock.unlock();
                    }else if(mess.startsWith("node")){
                        socketLock.lock();
                        addNode(server.getLastClient());
                        socketLock.unlock();
                    }
                }
            }catch(Exception e){
            }
        }while(keepRunning);
    }

    public TCPServer getServer() {
        return server;
    }
}
