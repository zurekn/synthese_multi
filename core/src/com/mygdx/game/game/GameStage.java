package com.mygdx.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.ai.AStar;
import com.mygdx.game.ai.ActionEvent;
import com.mygdx.game.ai.CharacterData;
import com.mygdx.game.ai.CommandHandler;
import com.mygdx.game.ai.CommandListener;
import com.mygdx.game.ai.WindowGameData;
import com.mygdx.game.data.Data;
import com.mygdx.game.data.Event;
import com.mygdx.game.data.HeroData;
import com.mygdx.game.data.MonsterData;
import com.mygdx.game.data.SpellData;
import com.mygdx.game.exception.IllegalActionException;
import com.mygdx.game.exception.IllegalCaracterClassException;
import com.mygdx.game.exception.IllegalMovementException;
import com.mygdx.game.imageprocessing.APIX;
import com.mygdx.game.imageprocessing.APIXAdapter;
import com.mygdx.game.imageprocessing.MovementEvent;
import com.mygdx.game.imageprocessing.QRCodeEvent;

import java.util.ArrayList;
import java.util.Collection;

import static com.mygdx.game.data.Data.ACTION_PER_TURN;
import static com.mygdx.game.data.Data.BLOCK_NUMBER_Y;
import static com.mygdx.game.data.Data.BLOCK_REACHABLE_COLOR;
import static com.mygdx.game.data.Data.BLOCK_SIZE_X;
import static com.mygdx.game.data.Data.BLOCK_SIZE_Y;
import static com.mygdx.game.data.Data.DEBUG_DEPARTURE;
import static com.mygdx.game.data.Data.DEBUG_PLAYER;
import static com.mygdx.game.data.Data.DECK_AREA_SIZE_X;
import static com.mygdx.game.data.Data.DECK_AREA_SIZE_Y;
import static com.mygdx.game.data.Data.DEPARTURE_BLOCK_ERROR;
import static com.mygdx.game.data.Data.EAST;
import static com.mygdx.game.data.Data.ERROR_TOO_MUCH_ACTION;
import static com.mygdx.game.data.Data.INIT_MAX_TIME;
import static com.mygdx.game.data.Data.INIT_PLAYER_TEXT;
import static com.mygdx.game.data.Data.MAIN_TEXT;
import static com.mygdx.game.data.Data.MAP_HEIGHT;
import static com.mygdx.game.data.Data.MAP_WIDTH;
import static com.mygdx.game.data.Data.MAP_X;
import static com.mygdx.game.data.Data.MAP_Y;
import static com.mygdx.game.data.Data.MAX_PLAYER;
import static com.mygdx.game.data.Data.MAX_RANGE;
import static com.mygdx.game.data.Data.MESSAGE_TYPE_ERROR;
import static com.mygdx.game.data.Data.NORTH;
import static com.mygdx.game.data.Data.RELATIVE_Y_POS;
import static com.mygdx.game.data.Data.RUN_APIX;
import static com.mygdx.game.data.Data.SELF;
import static com.mygdx.game.data.Data.SHOW_MOB_REACHABLE_BLOCKS;
import static com.mygdx.game.data.Data.SOUTH;
import static com.mygdx.game.data.Data.TURN_MAX_TIME;
import static com.mygdx.game.data.Data.TURN_TEXT;
import static com.mygdx.game.data.Data.WEST;
import static com.mygdx.game.data.Data.debug;
import static com.mygdx.game.data.Data.departureBlocks;
import static com.mygdx.game.data.Data.loadGame;
import static com.mygdx.game.data.Data.loadMap;
import static com.mygdx.game.data.Data.tiledMapRenderer;
import static com.mygdx.game.data.Data.untraversableBlocks;

/**
 * Created by nicolas on 15/03/2016.
 */
public class GameStage extends Stage {


    //
    private APIX apix;
    private Thread thread;
    private CommandHandler commands;
    private GameHandler handler;

    //LIBGDX Variables
    private CameraHandler camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private final String LABEL = "GameStage";
    //
    private MobHandler mobHandler;
    private ArrayList<Mob> mobs;
    private PlayerHandler playerHandler;
    private ArrayList<Player> players;
    private MovementHandler movementHandler;
    private MessageHandler messageHandler;
    private ArrayList<Event> events = new ArrayList<Event>();
    private ArrayList<Trap> traps = new ArrayList<Trap>();
    private Character previousCharacter = null;
    private Character currentCharacter;
    private ArrayList<int[]> reachableBlock = new ArrayList<int[]>();

    //
    private int playerNumber;
    private int turn;
    private int actionLeft = ACTION_PER_TURN;

    //
    public static boolean gameOn = false;
    public boolean gameEnded = false;
    public boolean gameWin = false;
    public boolean gameLose = false;
    private int timerInitPlayer;

    //
    private int turnTimer;
    private long timeStamp = -1;
    public static GameStage gameStage = null;


    public GameStage() {
        create();
        gameStage = this;
    }

    /*************************************/
    /* GETTER AND SETTER */

    /*************************************/

    public Mob getMobById(String id) {
        for (int i = 0; i < mobs.size(); i++) {
            if (mobs.get(i).getId().equals(id)) {
                return mobs.get(i);
            }
        }
        return null;
    }

    /**
     * Get all the Character positions
     *
     * @return ArrayList<String>
     */
    public ArrayList<String> getAllPositions() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < players.size(); i++)
            list.add(players.get(i).getX() + ":" + players.get(i).getY());
        for (int i = 0; i < mobs.size(); i++)
            list.add(mobs.get(i).getX() + ":" + mobs.get(i).getY());
        return list;
    }

    public ArrayList<String> getAllTraps() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < traps.size(); i++)
            list.add(traps.get(i).getX() + ":" + traps.get(i).getY());
        return list;
    }

    public ArrayList<Mob> getMobs() {
        return mobs;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public GameHandler getHandler() {
        return handler;
    }


    public Character getCurrentPlayer() {
        return currentCharacter;
    }

    public Character getCurrentCharacter() {
        return currentCharacter;
    }

    public CameraHandler getCamera() {
        return camera;
    }


    public void create() {
        thread = Thread.currentThread();
        handler = new GameHandler(thread);

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        Gdx.input.setInputProcessor(new InputHandler());

        loadMap();
        loadGame();
        SpellData.loadSpell();
        MonsterData.loadMonster();
        HeroData.loadHeros();
        // TrapData.loadTrap();

        initAPIX();
        initCommandHandler();

        // Create the monster list
        mobs = MonsterData.initMobs();
        mobHandler = new MobHandler(mobs);

        messageHandler = new MessageHandler();

        // Create the player list
        initPlayers();

        playerHandler = new PlayerHandler(players);

        //new Thread(movementHandler).start();
        // Set the timer
        timerInitPlayer = INIT_MAX_TIME;


        camera = new CameraHandler();
        camera.init();
        Data.BACKGROUND_MUSIC.loop(Data.MUSIC_VOLUM);

    }


    /************************************/
    /* RENDER BLOCK */
    /***********************************/

    int i = 0;

    public void draw(float delta) {

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        //camera.update();
        /* Render map */
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(camera.position.x, camera.position.y, 20 / camera.zoom, 20 / camera.zoom);

        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(Data.MAP_X, Data.MAP_Y, 10, 10);

        if (gameOn) {
            mobHandler.render(batch, shapeRenderer);
            renderDeckArea();
            playerHandler.render(batch, shapeRenderer);
            renderReachableBlocks();
            renderEvents();
        } else {
            playerHandler.renderInitBlock(batch, shapeRenderer);
        }
        playerHandler.renderPlayerStat(batch, shapeRenderer);
        renderText();

        if (gameEnded) {
            //Data.map.render(Data.MAP_X, Data.MAP_Y);
            tiledMapRenderer.render();
            mobHandler.render(batch, shapeRenderer);
            renderDeckArea();
            playerHandler.render(batch, shapeRenderer);
            if (gameWin) {
                batch.draw(Data.WIN_IMAGE, Data.ENDING_ANIMATION_X, Data.ENDING_ANIMATION_Y, (float) Data.WIN_IMAGE.getWidth() * Data.ENDING_ANIMATION_SCALE, (float) Data.WIN_IMAGE.getHeight() * Data.ENDING_ANIMATION_SCALE);
            }
            if (gameLose) {
                batch.draw(Data.LOSE_IMAGE, Data.ENDING_ANIMATION_X, Data.ENDING_ANIMATION_Y, (float) Data.LOSE_IMAGE.getWidth() * Data.ENDING_ANIMATION_SCALE, (float) Data.LOSE_IMAGE.getHeight() * Data.ENDING_ANIMATION_SCALE);
            }

            if (Data.ENDING_ANIMATION_Y < (Data.MAP_HEIGHT - Data.LOSE_IMAGE.getHeight() * Data.ENDING_ANIMATION_SCALE) / 2)
                Data.ENDING_ANIMATION_Y++;
            return;
        }
        if (Data.RUN_APIX)
            if (!apix.isInit()) {
                shapeRenderer.setColor(Color.BLACK);
                //g.setBackground(Color.white);
                // TOP LEFT
                shapeRenderer.rect(Data.MAP_X - 20, Data.MAP_Y - 20, 40, 40);
                // TOP RIGHT
                shapeRenderer.rect(Data.MAP_X + Data.MAP_WIDTH - 20, Data.MAP_Y - 20, 40, 40);
                // //BOTTOM left
                shapeRenderer.rect(Data.MAP_X - 20, Data.MAP_Y + Data.MAP_HEIGHT - 20, 40, 40);
                i++;
                if (i > 60)
                    apix.initTI();
                // Reset the timer unteal the paix init is over
                timerInitPlayer = Data.INIT_MAX_TIME;

            } else {
                //Data.map.render(Data.MAP_X, Data.MAP_Y);
                tiledMapRenderer.render();
            }
        shapeRenderer.end();
        batch.end();
    }

    /**
     * Display the reachables blocks of the current caracter
     */
    private void renderReachableBlocks() {
        shapeRenderer.setColor(BLOCK_REACHABLE_COLOR);
        for (int[] var : reachableBlock) {
            shapeRenderer.rect(MAP_X + var[0] * BLOCK_SIZE_X, MAP_Y + var[1] * BLOCK_SIZE_Y, BLOCK_SIZE_X, BLOCK_SIZE_Y);
        }
    }

    /**
     * Render the Deck Area
     */
    private void renderDeckArea() {
        shapeRenderer.setColor(Color.MAGENTA);
        // TOP
        shapeRenderer.rect(MAP_X, RELATIVE_Y_POS, DECK_AREA_SIZE_X, DECK_AREA_SIZE_Y);
        // BOTTOM
        shapeRenderer.rect(MAP_X + DECK_AREA_SIZE_X, MAP_HEIGHT + MAP_Y, DECK_AREA_SIZE_X, DECK_AREA_SIZE_Y);
        // LEFT
        shapeRenderer.rect(MAP_X - DECK_AREA_SIZE_Y, MAP_Y + MAP_WIDTH / 2, DECK_AREA_SIZE_Y, DECK_AREA_SIZE_X);
        // RIGHT
        shapeRenderer.rect(MAP_X + MAP_WIDTH, DECK_AREA_SIZE_Y, DECK_AREA_SIZE_Y, DECK_AREA_SIZE_X);
    }

    /**
     * Render the Game Text
     */
    private void renderText() {
        // render text
        Data.font.draw(batch, MAIN_TEXT, 10, 20);
        messageHandler.render(batch);

    }

    /**
     * Render all events in the game
     */
    private void renderEvents() {
        float x, y, xMin, yMin, xMax, yMax;
        xMin = MAP_X;
        xMax = MAP_X + MAP_WIDTH;
        yMin = MAP_Y;
        yMax = MAP_Y + BLOCK_NUMBER_Y * BLOCK_SIZE_Y;

        for (int i = 0; i < events.size(); i++) {
            Event e = events.get(i);
            if (!e.isFinalFrame()) {
                e.render(batch);
                x = e.getX();
                y = e.getY();

                if (e.isMobile())
                    e.move();

                if (e.getRange() <= 1) {
                    //	Gdx.app.log(LABEL, "range <= 1");
                    e.setMobile(false);
                }

                if (x < xMin || x > xMax || y < yMin || y > yMax)
                    e.setFinalFrame(true);
            } else {
                e.renderPostRemove(batch);
                if (e.isNeeDelete())
                    events.remove(i);
            }
        }
        long eventTime = 0;
    }

    /**
     * TODO Switch to the update from libgdx
     */
    public void act(float delta) {
        if (gameEnded)
            return;
        long time = System.currentTimeMillis();
        if (time - timeStamp > 1000) {
            if (gameOn) {
                turnTimer--;
                timeStamp = time;
                MAIN_TEXT = TURN_TEXT + turnTimer;
            } else {
                timerInitPlayer--;
                timeStamp = time;
                MAIN_TEXT = INIT_PLAYER_TEXT + timerInitPlayer;
            }
        }

        // Turn timer
        if (gameOn) {
            if (turnTimer < 0) {
                switchTurn();
            }
        } else {
            if (timerInitPlayer < 0)
                start();
        }

        messageHandler.update();
    }

    public void resize(int width, int height) {
        Gdx.app.log(LABEL, "Resize [" + width + " / " + height + "]");
        if (camera != null)
            camera.resize(width, height);
    }

    /**
     * Start the game (call in init player)
     */
    public void start() {
        if (players.size() == 0)
            return;
        playerNumber = players.size() + mobs.size();

        turnTimer = TURN_MAX_TIME;
        turn = 0;
        players.get(turn).setMyTurn(true);
        currentCharacter = players.get(turn);

        reachableBlock = AStar.getInstance().getReachableNodes(new WindowGameData(players, mobs, turn), new CharacterData(currentCharacter));
        gameOn = true;
    }

    /**
     * Create all the player (call in create)
     */
    public void initPlayers() {

        players = new ArrayList<Player>();

        Collection<String> pos = departureBlocks.keySet();
        pos.iterator();
        if (debug) {
            try {
                if (DEBUG_PLAYER > 0)
                    addChalenger(10, 8, -1);
                // players.add(new Player(10, 12, "P0", "mage"));
                if (DEBUG_PLAYER > 1)
                    addChalenger(15, 15, -1);
                if (DEBUG_PLAYER > 2)
                    addChalenger(19, 15, -1);
                if (DEBUG_PLAYER > 3)
                    addChalenger(7, 12, -1);
            } catch (IllegalCaracterClassException e) {
                e.printStackTrace();
            } catch (IllegalMovementException e) {
                e.printStackTrace();
            } catch (IllegalActionException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * Add a new player
     *
     * @param x
     * @param y
     * @throws IllegalCaracterClassException
     * @throws IllegalMovementException
     * @throws IllegalActionException
     */
    @SuppressWarnings("unused")
    public void addChalenger(int x, int y, int size) throws IllegalCaracterClassException, IllegalMovementException, IllegalActionException {
        if (gameOn)
            throw new IllegalActionException("Can not add player when game is on!");

        String position = x + ":" + y;

        if (getAllPositions().contains(position)) {
            // messageHandler.addMessage(new
            // Message("Position ["+position+"] non disponible", 1));

            throw new IllegalMovementException("Caracter already at the position [" + position + "]");
        }

        if (untraversableBlocks.containsKey(position)) {
            messageHandler.addGlobalMessage(new Message("Position [" + position + "] non disponible", 1));
            throw new IllegalMovementException("Untraversable block at [" + position + "]");
        }

        if (!departureBlocks.containsKey(position) && !DEBUG_DEPARTURE) {
            messageHandler.addGlobalMessage(new Message(DEPARTURE_BLOCK_ERROR, MESSAGE_TYPE_ERROR));
            throw new IllegalMovementException("Caracter must be at a departure position");
        } else {
            departureBlocks.put(position, true);
        }

        if (MAX_PLAYER <= players.size())
            return;
        String id = "P" + players.size();
        String type = HeroData.getRandomHero();

        Player p = new Player(x, y, id, type);
        p.setNumber(players.size());
        p.setSizeCharacter(size);
        players.add(p);

        timerInitPlayer = INIT_MAX_TIME;
        if (players.size() >= MAX_PLAYER) {
            Gdx.app.log(LABEL, " ----Max player reached ----");
            start();
        }
    }

    public void addPlayer(String position) {
        if (!departureBlocks.get(position)) {
            String[] s = position.split(":");
            try {
                players.add(new Player(Integer.parseInt(s[0]), Integer.parseInt(s[1]), "P" + players.size(), "mage"));
                departureBlocks.remove(position);
                departureBlocks.put(position, true);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalCaracterClassException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    /**
     * Init all the command handler
     * TODO SEE FOR SWITCHING IT TO LIGGDX CODE
     */
    public void initCommandHandler() {
        commands = CommandHandler.getInstance();
        commands.addCommandListener(new CommandListener() {

            public void newAction(ActionEvent e) {
                Gdx.app.log(LABEL, "Nouvelle action recup de CommandHandler  : " + e.toString());
                try {
                    decodeAction(e.getEvent());
                } catch (IllegalActionException e1) {
                    System.err.println(e1.getLocalizedMessage());
                }
            }
        });
        commands.begin();
    }

    public void initAPIX() {
        if (!RUN_APIX) {
            return;
        }
        apix = APIX.getInstance();
        movementHandler = new MovementHandler(this);

        apix.addAPIXListener(new APIXAdapter() {
            @Override
            public void newQRCode(QRCodeEvent e) {
                Gdx.app.log(LABEL, "Un nouveau QRCode vien d'?tre recup?rer par WindowGame [" + e.toString() + "]");
                try {
                    decodeAction(e.getId() + ":" + e.getDirection());
                } catch (IllegalActionException e1) {
                    System.err.println(e1.getLocalizedMessage());
                }
            }

            public void newMouvement(MovementEvent e) {
                Gdx.app.log(LABEL, "Une nouvelle position  vient d'?tre r?cup?r?e par WindowGame [" + e.toString() + "], position sur le plateau ["
                        + e.getX() / apix.getBlockSizeX() + ":" + e.getY() / apix.getBlockSizeY() + "]");
                try {
                    if (gameOn)
                        if (!currentCharacter.isMonster())
                            decodeAction("m:" + (int) (e.getX() / apix.getBlockSizeX()) + ":" + (int) (e.getY() / apix.getBlockSizeY()));
                        else
                            System.err.println("R?cup?ration d'une valeur de l'apix durant le tour de l'ia");
                    else
                        addChalenger((int) (e.getX() / apix.getBlockSizeX()), (int) (e.getY() / apix.getBlockSizeY()), e.getSize());
                } catch (IllegalActionException e1) {
                    System.err.println(e1.getLocalizedMessage());
                } catch (IllegalCaracterClassException e1) {
                    System.err.println(e1.getLocalizedMessage());
                } catch (IllegalMovementException e1) {
                    System.err.println(e1.getLocalizedMessage());
                }
            }
        });

        apix.begin();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

    }

    /**
     * Function call for switch the current character turn
     */
    public void switchTurn() {
        // Reset the timer
        Gdx.app.log(LABEL, "turn = " + turn + ", playerNumber = " + playerNumber + ", turnTimer = " + turnTimer);
        messageHandler.addGlobalMessage(new Message("Next turn"));
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

        messageHandler.addGlobalMessage(new Message("Turn of " + currentCharacter.getName()));
        actionLeft = ACTION_PER_TURN;

        if (currentCharacter.isNpc() && !previousCharacter.isNpc())
            commands.startCommandsCalculation(currentCharacter, players, mobs, turn);

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

    /**
     * decode a action and create associated event
     *
     * @param action , a string which defines the action
     * @throws IllegalMovementException
     */
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
                        switchTurn();
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
                events.add(e);
                Gdx.app.log(LABEL, "Created " + e.toString());
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
                switchTurn();

            } catch (IllegalMovementException ime) {
                throw new IllegalActionException("Mob can't reach this block");
            }
        } else {
            throw new IllegalActionException("Action not found : " + action);
        }
    }

    /**
     * Return the distance between the currentCharacter and the closer mob
     *
     * @param chars
     * @param e
     * @return
     */
    private Focus getFirstCharacterRange(ArrayList<Character> chars, Event e) {
        float range = MAX_RANGE;
        Gdx.app.log(LABEL, "Search the first character range : " + e.toString() + ", " + chars.toString());
        Character focus = null;
        for (Character c : chars) {
            if (e.getDirection() == NORTH || e.getDirection() == SOUTH) {
                float i = (Math.abs(c.getY() - (e.getYOnBoard())));
                Gdx.app.log(LABEL, "c.getY() = [" + c.getY() + "], e.getYOnBoard = [" + (e.getYOnBoard()) + "], i = [" + i + "]");

                if (i < range) {
                    range = i;
                    focus = c;
                }
            }
            if (e.getDirection() == EAST || e.getDirection() == WEST) {
                Gdx.app.log(LABEL, "c.getX() = [" + c.getX() + "], e.getXOnBoard = [" + (e.getXOnBoard()) + "], i = [" + (c.getX() - e.getXOnBoard())
                        + "]");
                float i = (Math.abs(c.getX() - (e.getXOnBoard())));

                if (i < range) {
                    range = i;
                    focus = c;
                }
            }
        }

        if (e.getDirection() == SELF) {
            return new Focus(0, currentCharacter);
        }

        if (debug && focus != null)
            Gdx.app.log(LABEL, "The Range is : " + range + ", focus is " + focus.toString());
        return new Focus(range, focus);
    }

    /**
     * Move the current player to the destination x:y if possible
     *
     * @param str , x:y
     */
    @Deprecated
    public void move(String str) {
        Gdx.app.log(LABEL, "WindowGame get new movement : " + str);
        if (turn < players.size()) {
            try {
                players.get(turn).moveTo(str);
            } catch (IllegalMovementException e) {
                e.printStackTrace();
            }
        } else {
            try {
                mobs.get(turn - players.size()).moveTo(str);
            } catch (IllegalMovementException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Get all character on a line line = Horizontal or Vertical
     *
     * @param x
     * @param y
     * @param direction
     * @return ArrayList<Character>
     */
    private ArrayList<Character> getCharacterPositionOnLine(int x, int y, int direction) {

        ArrayList<Character> c = new ArrayList<Character>();

        if (direction == SELF) {
            c.add(currentCharacter);
            return c;
        }

        for (int i = 0; i < players.size(); i++) {
            // above
            if (direction == NORTH && players.get(i).getY() < y && players.get(i).getX() == x)
                c.add(players.get(i));
            // bottom
            if (direction == SOUTH && players.get(i).getY() > y && players.get(i).getX() == x)
                c.add(players.get(i));
            // on left
            if (direction == EAST && players.get(i).getY() == y && players.get(i).getX() > x)
                c.add(players.get(i));
            // on right
            if (direction == WEST && players.get(i).getY() == y && players.get(i).getX() < x)
                c.add(players.get(i));
        }

        for (int i = 0; i < mobs.size(); i++) {
            // above
            if (direction == NORTH && mobs.get(i).getY() < y && mobs.get(i).getX() == x)
                c.add(mobs.get(i));
            // bottom
            if (direction == SOUTH && mobs.get(i).getY() > y && mobs.get(i).getX() == x)
                c.add(mobs.get(i));
            // on left
            if (direction == EAST && mobs.get(i).getY() == y && mobs.get(i).getX() > x)
                c.add(mobs.get(i));
            // on right
            if (direction == WEST && mobs.get(i).getY() == y && mobs.get(i).getX() < x)
                c.add(mobs.get(i));
        }
        Gdx.app.log(LABEL, "getCharacterePositionOnLine From [" + x + ", " + y + "], Found" + c.toString());
        return c;
    }

    /**
     * Return the Character with have the x,y position
     *
     * @param x
     * @param y
     * @return Character
     */
    private Character getCharacterByPosition(int x, int y) {
        for (int i = 0; i < players.size(); i++)
            if (players.get(i).getX() == x && players.get(i).getY() == y)
                return players.get(i);

        for (int i = 0; i < mobs.size(); i++)
            if (mobs.get(i).getX() == x && mobs.get(i).getY() == y)
                return mobs.get(i);

        return null;
    }


    public void checkEndGame() {
        if (mobs.size() <= 0 || players.size() <= 0) {
            //GAME WIN
            stopAllThread();
            gameEnded = true;
            if (mobs.size() <= 0)
                gameWin = true;
            if (players.size() <= 0)
                gameLose = true;
        }
    }

    public void stopAllThread() {
        apix.stop();
        commands.getInstance().getThread().stop();
        turnTimer = Integer.MAX_VALUE;
    }

    private class Focus {
        protected float range;
        protected Character character;

        public Focus(float range, Character character) {
            this.range = range;
            this.character = character;
        }

        public String toString() {
            return "Focus [ range, " + range + ", " + character.toString() + "]";
        }
    }
}
