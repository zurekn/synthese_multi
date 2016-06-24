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
    private Thread thread;


    private boolean keepRunning;

    public ServerMain(){
        nodeList = new ArrayList<>();
        socketClients = new ArrayList<>();
        gameIDMap = new HashMap<>();
        server = new TCPServer(Data.SERVER_PORT, Data.SERVER_TIMEOUT);
        keepRunning = true;

        socketLock = new ReentrantLock(true);
        mapLock = new ReentrantLock(true);

        thread = new Thread(this);
        thread.start();
    }

    public void addNode(Socket socket){
        nodeList.add(new ServerNodeThread(this, socket));
    }

    public void addClient(Socket socket){
        synchronized (socketClients) {
            socketClients.add(new SocketThread(this, socket));
        }
    }

    public synchronized void addGame(ServerNodeThread node, String gameId, String mess, Socket client){
        gameIDMap.put(gameId, node);

        getSocketClient(client).addMessage(mess);
    }

    public synchronized void addClient(ServerNodeThread node, String gameId, String mess, Socket client){
        server.send("gameNode:"+mess, client);

        synchronized (socketClients) {
            socketClients.remove(new TCPClient(client));
        }
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
        TCPClient tcp = new TCPClient(client);

        /*String mess;
        do {
            mess = tcp.receive();
        }while(mess == null);*/



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
                        System.out.println("Added client : " + server.getLastClient().getLocalAddress().getHostAddress() + ":" + server.getLastClient().getPort());
                        addClient(server.getLastClient());
                        socketLock.unlock();
                    }else if(mess.startsWith("node")){
                        socketLock.lock();
                        System.out.println("Added node : "+ server.getLastClient().getLocalAddress().getHostAddress()+":"+server.getLastClient().getPort());
                        addNode(server.getLastClient());
                        socketLock.unlock();
                    }
                }
            }catch(Exception e){
            }
        }while(keepRunning);
    }

    public void remove(ServerNodeThread thread){
        socketLock.lock();
        nodeList.remove(thread);
        socketLock.unlock();
    }

    public void remove(Socket sock){
        synchronized (socketClients){
            try {
                SocketThread sT = socketClients.get(socketClients.indexOf(sock));
                sT.kill();
                socketClients.remove(sT);
                System.out.println("Removed "+sT);
            }catch(ArrayIndexOutOfBoundsException e){

            }
        }
    }

    public SocketThread getSocketClient(Socket sock){
        try{
            for(int i = 0 ; i < socketClients.size(); i++){
                SocketThread el = socketClients.get(i);
                if(el.equals(sock))
                    return el;
            }

            //return socketClients.get(socketClients.indexOf(sock));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public TCPServer getServer() {
        return server;
    }
}
