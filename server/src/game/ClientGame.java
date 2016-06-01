package game;

import com.badlogic.gdx.Gdx;

import com.mygdx.game.com.TCPClient;

import com.mygdx.game.data.Data;
import com.mygdx.game.exception.IllegalActionException;
import com.mygdx.game.exception.IllegalMovementException;

import com.mygdx.game.game.GameStage;
import com.mygdx.game.game.Message;

import java.io.IOException;
import java.net.Socket;

import static com.mygdx.game.data.Data.ERROR_TOO_MUCH_ACTION;

import static com.mygdx.game.data.Data.debug;



public class ClientGame extends GameStage {
    public TCPClient client;

    public static ClientGame gameStage;

    protected final String LABEL = "ClientGame";

    public ClientGame() {
        //create();

    }

    @Override
    public void create() {
        super.create();

        gameStage = this;
        try {
            client = new TCPClient(new Socket((String)null, 42666));

            client.sendToServer("12:12:p1:mage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkAction(String action) throws IllegalActionException {
        if (gameEnded || !gameOn)
            return;
        if (action.startsWith("s")) { // Spell action
            if (actionLeft <= 0) {
                if (!debug) {
                    messageHandler.addPlayerMessage(new Message(ERROR_TOO_MUCH_ACTION, 1), turn);
                    return;
                } else {
                    messageHandler.addPlayerMessage(new Message("Action interdite, mais on est en mode debug... ", 1), turn);
                }
            }

            String[] tokens = action.split(":");
            if (tokens.length != 2)
                throw new IllegalActionException("Wrong number of arguments in action string");

        } else if (action.startsWith("t")) { // Trap action
            Gdx.app.log(LABEL, "Find a trap action");
        } else if (action.startsWith("m")) {// Movement action
            try {
                String[] tokens = action.split(":");
                if (tokens.length != 3)
                    throw new IllegalActionException("Wrong number of arguments in action string");

                String position = tokens[1] + ":" + tokens[2];
                // TODO call aStar and check if character don't fall into trap
                String tmpPos = currentCharacter.getX()+":"+"";

                int x= currentCharacter.getX(),y=currentCharacter.getY();

                currentCharacter.moveTo(position);

                currentCharacter.setX(currentCharacter.getLastX());
                currentCharacter.setY(currentCharacter.getLastY());
                currentCharacter.setLastX(x);
                currentCharacter.setLastY(y);


            } catch (IllegalMovementException ime) {
                throw new IllegalActionException("Mob can't reach this block");
            }
        } else {
            throw new IllegalActionException("Action not found : " + action);
        }
        client.sendToServer(action);
    }

    @Override
    public void decodeAction(String action) throws IllegalActionException {
        gameStage.client.sendToServer(action);
        super.decodeAction(action);
    }

    public void decodeServerAction(String action) throws IllegalActionException {
        super.decodeAction(action);
    }
}
