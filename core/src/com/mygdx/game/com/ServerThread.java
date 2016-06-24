package com.mygdx.game.com;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.exception.IllegalActionException;
import com.mygdx.game.game.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Objects;

public class ServerThread implements Runnable {
    public static final int INIT = 0;
    public static final int GAME = 1;

    private TCPClient client;
    private boolean keepRunning;
    private Player player;
    private ServerGame game;
    private Thread thread;
    private int state;

    public ServerThread(ServerGame game, TCPClient client){
        this.client = client;
        this.game = game;

    }

    public ServerThread(ServerGame game, Socket socket, Player player){
        this.client = new TCPClient(socket);
        this.player = player;
        this.game = game;
        this.keepRunning = true;

        thread = new Thread(this);

        thread.start();
    }

    //TODO handle connexion lost

    @Override
    public void run() {
        while(keepRunning){
            String message = receive();
            if(message!=null){
                if(state == INIT){
                    game.loadGame(this);
                }else if(state == GAME){
                    synchronized (player){
                        if(player.isMyTurn()){
                            try {
                                game.decodeAction(message);
                                game.getServer().sendToAllClients(message);
                            } catch (IllegalActionException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    private String receive(){
        String message = null;
        message = client.receive();
        return message;
    }

    public Player getPlayer(){
        return player;
    }

    public void setPlayer(Player p){
        player = p;
    }

    public void setState(int state) {
        this.state = state;
    }

    private void send(String data){
        client.sendToServer(data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerThread that = (ServerThread) o;
        return Objects.equals(client, that.client);
    }

    public TCPClient getClient() {
        return client;
    }
}
