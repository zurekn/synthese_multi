package com.mygdx.game.com;

import com.badlogic.gdx.Gdx;

import com.mygdx.game.data.Data;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.exception.IllegalActionException;
import com.mygdx.game.exception.IllegalCaracterClassException;
import com.mygdx.game.exception.IllegalMovementException;

import com.mygdx.game.game.GameStage;
import com.mygdx.game.game.Message;
import com.mygdx.game.game.Mob;
import com.mygdx.game.game.MobHandler;
import com.mygdx.game.game.Player;

import java.util.ArrayList;

import static com.mygdx.game.data.Data.ERROR_TOO_MUCH_ACTION;

import static com.mygdx.game.data.Data.debug;



public class ClientGame extends GameStage {
    public TCPClient client;
    private Player player;
    public static ClientGame gameStage;
    protected final String LABEL = "ClientGame";
    private int state;
    public static final int INIT = 0;
    public static final int LOAD = 1;
    public static final int GAME = 2;

    public ClientGame() {
        //create();

    }

    @Override
    public void create() {
        super.create();
        state = INIT;
        gameStage = this;
        initClient();
    }

    private void initClient(){
        client = new TCPClient(Data.SERVER_IP, Data.SERVER_PORT, Data.SERVER_TIMEOUT);
        //new ClientThread(gameStage, client.getSocket());"
        client.sendToServer("client", true);
        client.sendToServer("game", true);

        String mess;
        do{
            mess = waitMessage();
            System.out.println(mess);
        }while(!mess.startsWith("gameNode"));

        client.close();

        long fuck = Long.MIN_VALUE;
        while(fuck< Long.MAX_VALUE)
            fuck++;

        String[] split = mess.split(":");
        try {
            client = new TCPClient(split[1], Integer.parseInt(split[2]), Data.SERVER_TIMEOUT);
            client.sendToServer("client");
            client.sendToServer("logToGame:"+split[3]+":barbarian");

            String str = null;
            do{
                try {
                    str = client.receive();
                    if(str.startsWith("mob")){
                        addMob(str);
                    }else if(str.startsWith("player")){
                        addPlayerFromServer(str);
                    }
                }catch(NullPointerException ignored){}
            }while(str == null || str.equals("endLoading"));
        }catch(Exception ignored){
            ignored.printStackTrace();
        }
    }

    @Override
    public void initPlayers(){

    }

    @Override
    public void initMobs(){
        mobs = new ArrayList<>();
        mobHandler = new MobHandler(mobs);
        originMobs = new ArrayList<>(mobs);
    }

    public void addPlayerFromServer(String str){
        try {
            String[] split = str.split(":");
            int x,y;
            String id, charClass;
            x = Integer.parseInt(split[0]);
            y = Integer.parseInt(split[1]);
            id = split[2];
            charClass = split[3];
            Player p = new Player(x,y,id,charClass);
            addPlayerFromServer(p);
            if(split[4].equals("mainPlayer"))
                player = p;
        }catch(NumberFormatException ignored){}
        catch(IndexOutOfBoundsException ignored){}
        catch (IllegalCaracterClassException e) {
            e.printStackTrace();
        }
    }

    public void addPlayerFromServer(Player p){
        synchronized (players) {
            players.add(p);
        }
        System.out.println("Added "+p);
    }

    public void addMob(String str){
        try {
            String[] split = str.split(":");
            int x,y;
            String id, trueId;
            x = Integer.parseInt(split[0]);
            y = Integer.parseInt(split[1]);
            id = split[2];
            trueId = split[3];
            Mob m = new Mob(x,y,id,trueId, split[4]);
            addMob(m);
        }catch(NumberFormatException ignored){}
        catch(IndexOutOfBoundsException ignored){}
    }

    public void addMob(Mob m){
        synchronized (mobs){
            mobs.add(m);
            originMobs.add(m);
        }
        System.out.println("Added "+m);
    }

    @Override
    public void customAct(float delta){
        getUI().act(delta);
        this.act(delta);
        getCamera().update();
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
        if(player.isMyTurn())
            gameStage.client.sendToServer(action);
        // super.decodeAction(action);
    }

    public String waitMessage(){
        String mess = null;
        while(mess == null){
            mess = client.receive();
        }
        return mess;
    }

    public void decodeServerAction(String action) throws IllegalActionException {
        super.decodeAction(action);
    }

}
