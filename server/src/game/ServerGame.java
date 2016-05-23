package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.mygdx.game.ai.*;
import com.mygdx.game.com.TCPServer;
import com.mygdx.game.data.Data;
import com.mygdx.game.data.SpellData;
import com.mygdx.game.exception.IllegalActionException;
import com.mygdx.game.exception.IllegalMovementException;
import com.mygdx.game.game.GameStage;
import com.mygdx.game.game.Message;
import com.mygdx.game.data.Event;

import java.util.ArrayList;

import gui.LogHandler;

import static com.mygdx.game.data.Data.ACTION_PER_TURN;
import static com.mygdx.game.data.Data.BLOCK_SIZE_X;
import static com.mygdx.game.data.Data.BLOCK_SIZE_Y;
import static com.mygdx.game.data.Data.ERROR_TOO_MUCH_ACTION;
import static com.mygdx.game.data.Data.MAP_X;
import static com.mygdx.game.data.Data.MAP_Y;
import static com.mygdx.game.data.Data.MESSAGE_TYPE_ERROR;
import static com.mygdx.game.data.Data.SHOW_MOB_REACHABLE_BLOCKS;
import static com.mygdx.game.data.Data.TURN_MAX_TIME;
import static com.mygdx.game.data.Data.debug;


@SuppressWarnings({"ConstantConditions", "SuspiciousMethodCalls"})
public class ServerGame extends GameStage {
    public TCPServer server;
    public static ServerGame gameStage;
    private final String LABEL = "ServerGame";
    private LogHandler logHandler;


    public ServerGame(){
        create();
        gameStage = this;
        server = new TCPServer(42666);
    }

    @Override
    public void create(){
        super.create();

        Data.BACKGROUND_MUSIC.stop();

        logHandler = new LogHandler(this);
    }

    @Override
    public void initCommandHandler(){

    }

    /**
     * decode a action and create associated event
     *
     * @param action , a string which defines the action
     * @throws IllegalActionException
     */
    @Override
    public void decodeAction(String action) throws IllegalActionException {
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

            String spellID = tokens[0].split("\n")[0];
            int direction = Integer.parseInt(tokens[1]);

            if (currentCharacter.getSpell(spellID) == null) {
                messageHandler.addPlayerMessage(new Message("Vous n'avez pas le sort : " + SpellData.getSpellById(spellID).getName(), MESSAGE_TYPE_ERROR), turn);
                throw new IllegalActionException("Spell [" + spellID + "] not found");
            }
            float speed = currentCharacter.getSpell(spellID).getSpeed();
            Event e = currentCharacter.getSpell(spellID).getEvent().getCopiedEvent();

            e.setDirection(direction, speed);
            e.setX(MAP_X + currentCharacter.getX() * BLOCK_SIZE_X);
            e.setY(MAP_Y + currentCharacter.getY() * BLOCK_SIZE_Y);
            // Get the range to the next character to hit
            Focus focus = getFirstCharacterRange(getCharacterPositionOnLine(currentCharacter.getX(), currentCharacter.getY(), e.getDirection()), e);
            //Gdx.app.log(LABEL, "get focus : " + focus.toString());
            if (focus.range > e.getRange()) {
                focus.range = e.getRange();
                focus.character = null;
            }
            e.setRange(focus.range);

            try {
                String res = currentCharacter.useSpell(spellID, direction);
                String[] split = res.split(":");
                int damage = Integer.parseInt(split[0]);
                int heal = Integer.parseInt(split[1]);
                int state = Integer.parseInt(split[2]);

                if (state == -1) {
                    messageHandler.addPlayerMessage(new Message("Echec critique du sort " + SpellData.getSpellById(spellID).getName(), MESSAGE_TYPE_ERROR), turn);
                    if (heal > 0) {
                        currentCharacter.heal(heal);
                        messageHandler.addPlayerMessage(new Message("Heal critic " + heal + " to the " + focus.character.getName() + "", MESSAGE_TYPE_ERROR), turn);

                    } else {
                        currentCharacter.takeDamage(damage, e.getType());
                        messageHandler.addPlayerMessage(new Message("Use " + SpellData.getSpellById(spellID).getName() + " on " + currentCharacter.getName() + " and deal critic " + damage, MESSAGE_TYPE_ERROR), turn);

                    }
                    if (currentCharacter.checkDeath()) {
                        // TODO ADD a textual event
                        Gdx.app.log(LABEL, "-----------------------------------------");
                        Gdx.app.log(LABEL, "DEATH FOR" + currentCharacter.toString());
                        Gdx.app.log(LABEL, "-----------------------------------------");
                        messageHandler.addPlayerMessage(new Message(currentCharacter.getName() + "Died "), turn);
                        turn--;
                        players.remove(currentCharacter);
                        mobs.remove(currentCharacter);
                        playerNumber--;
                        checkEndGame();
                    }
                } else {
                    if (focus.character != null) {
                        if (currentCharacter.isMonster() == focus.character.isMonster())
                            if (e.getHeal() > 0) {
                                focus.character.heal(heal);
                                if (state > 0)
                                    messageHandler.addPlayerMessage(new Message("Heal critic " + heal + " to the " + focus.character.getName() + "", MESSAGE_TYPE_ERROR), turn);
                                else
                                    messageHandler.addPlayerMessage(new Message("Heal " + heal + " to the " + focus.character.getName() + ""), turn);

                            } else {
                                damage = focus.character.takeDamage(damage, e.getType());
                                messageHandler.addPlayerMessage(new Message("Use " + SpellData.getSpellById(spellID).getName() + " on " + focus.character.getName() + " and deal " + damage), turn);
                            }
                        else {
                            damage = focus.character.takeDamage(damage, e.getType());
                            if (state > 0)
                                messageHandler.addPlayerMessage(new Message("Use " + SpellData.getSpellById(spellID).getName() + " on " + focus.character.getName() + " and deal critic " + damage, MESSAGE_TYPE_ERROR), turn);
                            else
                                messageHandler.addPlayerMessage(new Message("Use " + SpellData.getSpellById(spellID).getName() + " on " + focus.character.getName() + " and deal " + damage), turn);

                        }
                        if (focus.character.checkDeath()) {
                            Gdx.app.log(LABEL, "-----------------------------------------");
                            Gdx.app.log(LABEL, "DEATH FOR" + focus.character.toString());
                            Gdx.app.log(LABEL, "-----------------------------------------");
                            messageHandler.addPlayerMessage(new Message(focus.character.getName() + "Died "), turn);
                            int index = Math.max(players.indexOf(focus.character), mobs.indexOf(focus.character));
                            int indexCurrent = Math.max(players.indexOf(currentCharacter), mobs.indexOf(currentCharacter));
                            players.remove(focus.character);
                            mobs.remove(focus.character);
                            playerNumber--;
                            if (index <= indexCurrent)
                                turn = (turn - 1) % playerNumber;
                            checkEndGame();
                        }
                    } else {
                        messageHandler.addPlayerMessage(new Message("Vous avez lanc? " + SpellData.getSpellById(spellID).getName() + " mais personne n'a ?t? touch?"), turn);
                    }
                }
                actionLeft--;
            } catch (IllegalActionException iae) {
                //iae.printStackTrace();
                Gdx.app.log(LABEL, iae.getLocalizedMessage() + "----------------------------" + iae.getMessage());
                messageHandler.addPlayerMessage(new Message(iae.getLocalizedMessage(), MESSAGE_TYPE_ERROR), turn);
            }
        } else if (action.startsWith("t")) { // Trap action
            Gdx.app.log(LABEL, "Find a trap action");
        } else if (action.startsWith("m")) {// Movement action
            try {
                String[] tokens = action.split(":");
                if (tokens.length != 3)
                    throw new IllegalActionException("Wrong number of arguments in action string");

                String position = tokens[1] + ":" + tokens[2];
                // TODO call aStar and check if character don't fall into trap
                currentCharacter.moveTo(position);
            } catch (IllegalMovementException ime) {
                throw new IllegalActionException("Mob can't reach this block");
            }
        } else {
            throw new IllegalActionException("Action not found : " + action);
        }

        server.sendToAllClients(action);

        if(currentCharacter.checkDeath())
            switchTurn();
        if(action.startsWith("m"))
            switchTurn();
    }

    /**
     * Function call for switch the current character turn
     */
    @Override
    public void switchTurn() {
        // Reset the timer
        Gdx.app.log(LABEL, "turn = " + turn + ", playerNumber = " + playerNumber + ", turnTimer = " + turnTimer);
        turnTimer = TURN_MAX_TIME;
        turn = (turn + 1) % playerNumber;

        previousCharacter = currentCharacter;
        previousCharacter.regenMana();
        // Switch the turn
        // Set the new character turn
        if (turn < players.size()) {
            players.get(turn).setMyTurn(true);
            currentCharacter = players.get(turn);
        } else {
            mobs.get(turn - players.size()).setMyTurn(true);
            currentCharacter = mobs.get(turn - players.size());
        }

        // set to false the previous character turn
        previousCharacter.setMyTurn(false);
        if (currentCharacter.isMonster() && !SHOW_MOB_REACHABLE_BLOCKS)
            reachableBlock = new ArrayList<int[]>();
        else
            reachableBlock = AStar.getInstance().getReachableNodes(new WindowGameData(players, mobs, turn), new CharacterData(currentCharacter));

        actionLeft = ACTION_PER_TURN;

        if (currentCharacter.isNpc() && !previousCharacter.isNpc()) {
            commands.startCommandsCalculation(currentCharacter, players, mobs, turn);
        }else{
            String mes =server.receive();
            try {
                decodeAction(mes);
            } catch (IllegalActionException e) {
                e.printStackTrace();
            }
        }


        // print the current turn in the console
        if (debug) {
            Gdx.app.log(LABEL, "========================");
            if (turn < players.size()) {
                Gdx.app.log(LABEL, "Tour du Joueur " + turn);
                Gdx.app.log(LABEL, "Player : " + players.get(turn).toString());
            } else {
                Gdx.app.log(LABEL, "Tour du Monster" + (turn - players.size()));
                Gdx.app.log(LABEL, "Monster " + mobs.get(turn - players.size()).toString());
            }
            Gdx.app.log(LABEL, "========================");
        }
    }

    public LogHandler getLogHandler(){
        return logHandler;
    }
}
