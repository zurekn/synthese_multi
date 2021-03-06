package com.mygdx.game.com;

import com.mygdx.game.data.Data;

import java.net.Socket;
import java.util.ArrayList;

public class ServerNodeThread implements Runnable{
    private ServerMain parent;
    private TCPClient client;
    private int weight;
    private boolean keepRunning;
    private ArrayList<Object[]> messagesToSend;
    private Thread thread;
    private long timeSinceLastPing;

    public ServerNodeThread(ServerMain parent, Socket sock){
        this.parent = parent;
        messagesToSend = new ArrayList<>();
        client = new TCPClient(sock);
        keepRunning = true;
        timeSinceLastPing = System.currentTimeMillis();

        thread = new Thread(this);

        thread.start();
    }

    public int getWeight(){
        return weight;
    }

    public void setWeight(int value){
        weight = value;
    }

    public Socket getSocket(){
        return client.getSocket();
    }

    public void send(String mess){
        client.sendToServer(mess);
    }

    public void send(String mess, boolean print){
        client.sendToServer(mess, print);
    }

    public String receive(){
        String mes = client.receive();
        if(mes != null)
            timeSinceLastPing = System.currentTimeMillis();
        return mes;
    }

    public void askGame(Socket client){
        send("createGame", true);

        String res;
        do{
            res = receive();
        } while(res.equals("ping"));

        System.out.println("Received:"+res);
        if(res != null){
            String[] split = res.split(":");
            if(split.length == 4 && split[0].equals("gameId")){
                res = split[1]+":"+split[2]+":"+split[3];
            }
        }

        if(res == null){
            //TODO game not created
        }else if(res.split(":").length == 3){
            String[] split = res.split(":");

            parent.addGame(this, split[2], "gameNode:"+res, client);
        }
    }

    public void getGameAddress(Socket client, String id) throws GameNotFoundException {
        send("getGameAddress:"+id);

        String res = receive();
        if(res != null){
            String[] split = res.split(":");
            if(split.length == 4 && split[0].equals("gameId")){
                res = split[1]+split[2]+split[3];
            }
        }
        
        if(res == null){
            throw new GameNotFoundException("Game with id : "+id+" does not exist");
        }else if(res.split(":").length == 3){
            String[] split = res.split(":");
            //TODO
            parent.addClient(this, split[2], res, client);
        }
    }



    public void addMessage(String mes, Socket client){
        synchronized (messagesToSend){
            messagesToSend.add(new Object[]{mes, client});
        }
    }

    @Override
    public void run() {
        while(keepRunning){
            String mes=null;
            synchronized (messagesToSend){
                if(!messagesToSend.isEmpty()) {
                    try {
                        String str = (String) messagesToSend.get(0)[0];
                        if(str.startsWith("createGame")) {
                            askGame((Socket) messagesToSend.get(0)[1]);
                        }else if(str.startsWith("getGameAddress")) {
                            try {
                                getGameAddress((Socket) messagesToSend.get(0)[1], str);
                            } catch (GameNotFoundException e) {
                                parent.getServer().send("Error:Game not found", (Socket) messagesToSend.get(0)[1]);
                            }
                        } else {
                            mes = str;
                        }
                        messagesToSend.remove(0);
                    }catch(ClassCastException e){
                        e.printStackTrace();
                    }catch (IndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                }
            }
            if(mes != null)
                send(mes);
            try {
                String str = receive();
                if(str!=null && !str.equals("ping"))
                    System.out.println("run: "+str);
                if (str.equals("ping")) {
                    //System.out.println("Received ping from" + client.getSocket().getInetAddress()+":"+client.getSocket().getPort());
                }
            } catch(NullPointerException e){

            }

            long time = System.currentTimeMillis();
            if(!client.getSocket().isConnected() || (time - timeSinceLastPing) > Data.PING_TIMEOUT) {
                keepRunning = false;
                try{
                    send("kill");
                }catch(Exception e){ }
                System.err.println("Connection lost with "+client.getSocket().getInetAddress().getHostAddress()+":"+client.getSocket().getPort());
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (client.equals(o)) return true;

        if (o == null || getClass() != o.getClass())  return false;

        ServerNodeThread that = (ServerNodeThread) o;

        return client.equals(that.client);
    }
}
