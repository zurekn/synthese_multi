package com.mygdx.game.com;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.com.ServerGame;
import com.mygdx.game.exception.IllegalActionException;
import com.mygdx.game.game.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by gregory on 30/05/16.
 */
public class ServerThread implements Runnable {
    private Socket socket;
    private boolean keepRunning;
    private Player player;
    private ServerGame game;

    public ServerThread(ServerGame game, Socket socket, Player player){
        this.socket = socket;
        this.player = player;
        this.game = game;
        this.keepRunning = true;

        run();
    }

    //TODO handle connexion lost

    @Override
    public void run() {
        while(keepRunning){
            String message = receive();
            if(message!=null && player.isMyTurn()){
                try {
                    game.decodeAction(message);
                    game.getServer().sendToAllClients(message);
                } catch (IllegalActionException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String receive(){
        String message = null;
        try {
            message = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
            if(message!=null)
            Gdx.app.log("Server",  "Received : "+message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    private void send(String data){
        try {
            String message = data + "\n";
            socket.getOutputStream().write(message.getBytes());
            Gdx.app.log("Server", "Sent : "+message);
        } catch (IOException e) {
            Gdx.app.log("ServerError", "Error sending message to client : " + e.getMessage());
        }
    }
}
