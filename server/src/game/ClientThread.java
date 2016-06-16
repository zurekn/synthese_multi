package game;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.com.TCPClient;
import com.mygdx.game.exception.IllegalActionException;
import com.mygdx.game.game.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.TimeoutException;

/**
 * Created by gregory on 30/05/16.
 */
public class ClientThread implements Runnable {
    private Socket socket;
    private boolean keepRunning;
    private ClientGame game;

    public ClientThread(ClientGame game, Socket socket){
        this.socket = socket;
        this.game = game;
        this.keepRunning = true;

        run();
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
