package com.mygdx.game.com;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.exception.IllegalActionException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread implements Runnable {
    private Socket socket;
    private boolean keepRunning;
    private ClientGame game;
    private Thread thread;

    public ClientThread(ClientGame game, Socket socket){
        this.socket = socket;
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
                try {
                    game.decodeAction(message);
                } catch (IllegalActionException e) {
                    e.printStackTrace();
                }
            } else {
                Gdx.app.error(game.LABEL, "Disconnected from server");
            }
        }
    }

    private String receive(){
        String message = null;
        try {
            message = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
            if(message != null)
                Gdx.app.log("Client",  "Received : "+message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }
}
