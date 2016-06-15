package com.mygdx.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.ai.AStar;
import com.mygdx.game.ai.ActionEvent;
import com.mygdx.game.ai.CharacterData;
import com.mygdx.game.ai.CommandHandler;
import com.mygdx.game.ai.CommandListener;
import com.mygdx.game.ai.WindowGameData;
import com.mygdx.game.com.Hadoop;
import com.mygdx.game.data.Data;
import com.mygdx.game.data.Event;
import com.mygdx.game.data.HeroData;
import com.mygdx.game.data.MonsterData;
import com.mygdx.game.data.SpellD;
import com.mygdx.game.data.SpellData;
import com.mygdx.game.exception.IllegalActionException;
import com.mygdx.game.exception.IllegalCaracterClassException;
import com.mygdx.game.exception.IllegalMovementException;
import com.mygdx.game.hud.HUD;
import com.mygdx.game.hud.PlayerHUD;
import com.mygdx.game.hud.emptyHUD;
import com.mygdx.game.imageprocessing.APIX;
import com.mygdx.game.imageprocessing.APIXAdapter;
import com.mygdx.game.imageprocessing.MovementEvent;
import com.mygdx.game.imageprocessing.QRCodeEvent;
import com.mygdx.game.javacompiler.CompileString;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import static com.mygdx.game.data.Data.scale;
import static com.mygdx.game.data.Data.tiledMapRenderer;
import static com.mygdx.game.data.Data.untraversableBlocks;

/**
 * Created by nicolas on 15/03/2016.
 */
public class GameStage extends Stage {


    //
    private APIX apix;
    private Thread thread;
    protected CommandHandler commands;
    private GameHandler handler;

    //LIBGDX Variables
    private CameraHandler camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private final String LABEL = "GameStage";
    //
    private MobHandler mobHandler;
    protected ArrayList<Mob> mobs;
    private ArrayList<Mob> originMobs;
    private PlayerHandler playerHandler;
    protected ArrayList<Player> players;
    private ArrayList<Player> originPlayers;
    private MovementHandler movementHandler;
    protected MessageHandler messageHandler;
    private ArrayList<Event> events = new ArrayList<Event>();
    private ArrayList<Trap> traps = new ArrayList<Trap>();
    protected Character previousCharacter = null;
    protected Character currentCharacter;
    protected ArrayList<int[]> reachableBlock = new ArrayList<int[]>();
    //
    protected int playerNumber;
    protected int turn;
    private int global_turn;
    protected int actionLeft = ACTION_PER_TURN;

    // Game status
    public static boolean gameOn = false;
    public boolean gameEnded = false;
    public boolean gameWin = false;
    public boolean gameLose = false;
    private int timerInitPlayer;
    private int loopNumber = 1;


int lastPlayer = -1;

    protected int turnTimer;
    private long timeStamp = -1;
    public static GameStage gameStage = null;

    //UI
    private HUD ui = new emptyHUD();

    private ArrayList decodeArray;

    public GameStage() {
        create();
        gameStage = this;
        start();
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

        loadMap();
        loadGame();
        SpellData.loadSpell();
        MonsterData.loadMonster();
        HeroData.loadHeros();
        // TrapData.loadTrap();

        initAPIX();
        // Combine 2 mobs together

        Data.getRandomIAGeneticList();
        // Create the monsters and players lists
        loadChallengers();
        messageHandler = new MessageHandler();
        if(!Data.ANDROID && loopNumber == 1)
            initCommandHandler();
        if(loopNumber == 1) {
            camera = new CameraHandler();
            camera.init();
            Data.BACKGROUND_MUSIC.loop(Data.MUSIC_VOLUM);
        }
        // Set the timer
        timerInitPlayer = INIT_MAX_TIME;
    }

    public void loadChallengers(){

        mobs = MonsterData.initMobs();
        mobHandler = new MobHandler(mobs);
        originMobs = new ArrayList<Mob>(mobs);
        players = new ArrayList<Player>();
        playerHandler = new PlayerHandler(players);
        // Fill the player list
        if(Data.autoIA && !Data.jvm)
        {
            Gdx.app.log("create", "autoIA");
            initGeneticPlayers();
            Data.singlePlayer = false;
        }
        else
        {
            initPlayers();
            if (Data.singlePlayer)
            {
                try {
                    createMainPlayer(5, 2);
                    ui = new PlayerHUD(this, playerHandler.getMainPlayer());
                    //this.addActor(ui);
                } catch (IllegalActionException e) {
                    e.printStackTrace();
                } catch (IllegalMovementException e) {
                    e.printStackTrace();
                } catch (IllegalCaracterClassException e) {
                    e.printStackTrace();
                }
            }
        }
        originPlayers = new ArrayList<Player>(players);
    }


    /************************************/
    /* RENDER BLOCK */
    /***********************************/

    int i = 0;

    public void draw(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /* Render map */
        //System.out.println("Draw map at "+delta);
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();//MUST BE BEFORE batch.begin()

        this.draw();//MUST BE BEFORE batch.begin()

        /* begin texture rendering */
        if(batch.isDrawing())
            batch.end();
        batch.begin();
        if(shapeRenderer.isDrawing())
            shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        if (gameOn) {
            mobHandler.render(batch, shapeRenderer);

            playerHandler.render(batch, shapeRenderer);
            renderReachableBlocks();
            renderEvents();

        } else {
            playerHandler.renderInitBlock(batch, shapeRenderer);
        }
        playerHandler.renderPlayerStat(batch, shapeRenderer);

        renderText(batch);

        if (gameEnded) {
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
                renderDeckArea();
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
            shapeRenderer.rect(MAP_X + var[0] * BLOCK_SIZE_X / scale, MAP_Y + var[1] * BLOCK_SIZE_Y / scale, BLOCK_SIZE_X / scale, BLOCK_SIZE_Y / scale);
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
    private void renderText(SpriteBatch batch) {
        // render text
        Label myLabel = new Label(MAIN_TEXT, Data.SKIN);
        myLabel.setPosition(10, Data.SCREEN_HEIGHT - myLabel.getHeight() - 10);
        myLabel.draw(batch, 1);
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
                e.render(batch, shapeRenderer);
                x = e.getX();
                y = e.getY();

                if (e.isMobile())
                    e.move();
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
    public void customAct(float delta) {
        ui.act(delta);
        this.act(delta);
        camera.update();
        if (gameEnded && Data.RELOAD_GAME_WHEN_ENDED) {
            reinitAll();
            loopNumber++;
            create();
            start();
            return;
        }
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

        // Process IA
        if(currentCharacter.getHasPlayed() == true)
        {
            currentCharacter.setHasPlayed(false);
            switchTurn();
        }
    }

    public void resize(int width, int height) {
        Gdx.app.log(LABEL, "Resize [" + width + " / " + height + "]");
        Data.SCREEN_HEIGHT = height;
        Data.SCREEN_WIDTH = width;
        if (camera != null) {
            camera.resize(width, height);
            camera.reloadMapPosition();
        }
        if(ui != null)
            ui.resize();
    }

    /**
     * Start the game (call in init player)
     */
    public void start() {
        if (players.size() == 0)
            return;

        turnTimer = TURN_MAX_TIME;
        turn = 0;
        global_turn=1;
        players.get(turn).setMyTurn(true);
        playerNumber = players.size() + mobs.size();
        currentCharacter = players.get(turn);

        gameOn = true;

        if(!Data.autoIA)
            reachableBlock = AStar.getInstance().getReachableNodes(new WindowGameData(players, mobs, turn), new CharacterData(currentCharacter));
        else
            currentCharacter.findScriptAction();//Pour lancer l'action du premier joueur

    }

    /**
     * Create all the player (call in create)
     */
    public void initPlayers() {
        Gdx.app.log(LABEL, "Player Initialisation");


        Collection<String> pos = departureBlocks.keySet();
        pos.iterator();
        if (debug && !Data.singlePlayer) {
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
     * Create all the genetic players (call in create)
     */
    public void initGeneticPlayers()
    {
        //Gdx.app.log(LABEL, "Player Genetic Initialisation");
        Collection<String> pos = Data.departureBlocks.keySet();
        pos.iterator();
        try {
            if (Data.DEBUG_NB_GENETIC_PLAYER > 0)
            {
                addGeneticPlayer(10, 8, -1,"m1", Data.selectedIAFiles.get(4));
            }
            if (Data.DEBUG_NB_GENETIC_PLAYER > 1)
            {
                addGeneticPlayer(11, 10, -1, "m2", Data.selectedIAFiles.get(5));
            }
            if (Data.DEBUG_NB_GENETIC_PLAYER > 2)
            {
                addGeneticPlayer(12, 12, -1, "m3", Data.selectedIAFiles.get(6));
            }
            if (Data.DEBUG_NB_GENETIC_PLAYER > 3)
            {
                addGeneticPlayer(7, 12, -1, "m4", Data.selectedIAFiles.get(7));
            }

        } catch (IllegalCaracterClassException e) {
            e.printStackTrace();
        } catch (IllegalMovementException e) {
            e.printStackTrace();
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a new player (call in initGeneticPlayers)
     */
    @SuppressWarnings("unused")
    public void addGeneticPlayer(int x, int y, int size, String id, String selectedTree) throws IllegalCaracterClassException, IllegalMovementException, IllegalActionException {
        //Gdx.app.log(LABEL, "add Genetic Player id : "+id);
        if (gameOn)
            throw new IllegalActionException("Can not add player genetic when game is on!");

        String position = x + ":" + y;

        if (getAllPositions().contains(position)) {
            // messageHandler.addMessage(new
            // Message("Position ["+position+"] non disponible", 1));

            throw new IllegalMovementException("Caracter already at the position [" + position + "]");
        }

        if (Data.untraversableBlocks.containsKey(position)) {
            messageHandler.addGlobalMessage(new Message("Position [" + position + "] non disponible", 1));
            throw new IllegalMovementException("Untraversable block at [" + position + "]");
        }

        if (Data.departureBlocks.containsKey(position) || (Data.DEBUG_DEPARTURE && Data.debug)) {
            Data.departureBlocks.put(position, true);
        } else {
            messageHandler.addGlobalMessage(new Message(Data.DEPARTURE_BLOCK_ERROR, Data.MESSAGE_TYPE_ERROR));
            throw new IllegalMovementException("Caracter must be at a departure position");
        }

        if (Data.MAX_PLAYER <= players.size())
            return;

        Player p = new Player(x, y, id, "", "g"+players.size(), selectedTree);
        p.setNumber(players.size());
        p.setSizeCharacter(size);
        players.add(p);
        //Gdx.app.log(LABEL, "New Genetic Player : " + p.toString());

        timerInitPlayer = Data.INIT_MAX_TIME;
        if (players.size() >= Data.MAX_PLAYER) {
            System.out.println(" ----Max genetic player reached ----");
            //start();
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

    private void createMainPlayer(int x, int y) throws IllegalActionException, IllegalMovementException, IllegalCaracterClassException {
        String position = x + ":" + y;
        Gdx.app.log(LABEL, "Creating main player at position " + position);
        if (gameOn)
            throw new IllegalActionException("Can not add player when game is on!");


        if (getAllPositions().contains(position)) {
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

        if (playerHandler.getMainPlayer() != null)
            return;
        String id = "P" + players.size();
        String type = "mage";

        Player p = new Player(x, y, id, type);
        p.setNumber(players.size());
        p.setSizeCharacter(-1);
        players.add(p);
        playerHandler.setMainPlayer(p);


        Gdx.app.log(LABEL, " -- Main player created --");
        start();

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
        if (gameEnded)
            return;

        // Reset the timer
        Gdx.app.log(LABEL, "turn = " + turn + ", playerNumber = " + playerNumber + ", turnTimer = " + turnTimer);
        messageHandler.addGlobalMessage(new Message("Next turn"));
        turnTimer = TURN_MAX_TIME;
        turn = (turn + 1) % playerNumber;

        if(turn == 0)
        {

            currentCharacter.getFitness().debugFile("\n\t\t\t\t=== TOUR " + global_turn + " ===", true);
            checkEndGame();

            global_turn++;
            messageHandler.addPlayerMessage(new Message("Tour de jeu numero  "+global_turn, Data.MESSAGE_TYPE_INFO), turn);
            for(Mob mo:mobs){
                mo.getFitness().addTurn();
            }
            if(Data.autoIA){
                    for(Player po:players){
                        po.getFitness().addTurn();
                    }
            }
        }

        previousCharacter = currentCharacter;
        previousCharacter.regenMana();

        if(lastPlayer == turn){
            Gdx.app.log(LABEL,"======= ERROR TURN = LAST PLAYER ========");   // debug si jamais on a turn = last player                                                                                                      Gdx.app.log(LABEL,"======= ERROR TURN = LAST PLAYER ========");Gdx.app.log(LABEL,"======= ERROR TURN = LAST PLAYER ========");Gdx.app.log(LABEL,"======= ERROR TURN = LAST PLAYER ========");Gdx.app.log(LABEL,"======= ERROR TURN = LAST PLAYER ========");Gdx.app.log(LABEL,"======= ERROR TURN = LAST PLAYER ========");Gdx.app.log(LABEL,"======= ERROR TURN = LAST PLAYER ========");Gdx.app.log(LABEL,"======= ERROR TURN = LAST PLAYER ========");Gdx.app.log(LABEL,"======= ERROR TURN = LAST PLAYER ========");Gdx.app.log(LABEL,"======= ERROR TURN = LAST PLAYER ========");Gdx.app.log(LABEL,"======= ERROR TURN = LAST PLAYER ========");
        }
        // Switch the turn
        // Set the new char
        if (turn < players.size()) {
            players.get(turn).setMyTurn(true);
            currentCharacter = players.get(turn);
            lastPlayer = turn;
        } else {
            mobs.get(turn - players.size()).setMyTurn(true);
            currentCharacter = mobs.get(turn - players.size());
            lastPlayer = turn- players.size();
        }

        // set to false the previous character turn
        previousCharacter.setMyTurn(false);
        if (currentCharacter.isMonster() && !SHOW_MOB_REACHABLE_BLOCKS)
            reachableBlock = new ArrayList<int[]>();
        else if(!Data.autoIA)
            reachableBlock = AStar.getInstance().getReachableNodes(new WindowGameData(players, mobs, turn), new CharacterData(currentCharacter));

        messageHandler.addGlobalMessage(new Message("Turn of " + currentCharacter.getName()));
        actionLeft = ACTION_PER_TURN;

        if ( (currentCharacter.isNpc() || (Data.autoIA && Data.jvm) )  && !getCurrentCharacter().getHasPlayed() ) {
            if (!Data.iaUniTest) {
                currentCharacter.findScriptAction();//Pour lancer l'action du premier joueur
            } else {
                TestAIScript tas = new TestAIScript(currentCharacter);
                tas.testAllScripts();
                quitGame();
            }
        }


        // print the current turn in the console
        if (debug) {
            Gdx.app.log(LABEL, "========================");
            if (turn < players.size()) {
                Gdx.app.log(LABEL, "Tour du Joueur " + turn);
                if(players.size() > 0)
                    Gdx.app.log(LABEL, "Player : " + players.get(turn).toString());
            } else {
                Gdx.app.log(LABEL, "Tour du Monster" + (turn - players.size()));
                if(mobs.size() > 0)
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
                SpellD s = SpellData.getSpellById(spellID);
                messageHandler.addPlayerMessage(new Message("Vous n'avez pas le sort : " + (s != null ? s.getName() : null), MESSAGE_TYPE_ERROR), turn);
                throw new IllegalActionException("Spell [" + spellID + "] not found");
            }
            float speed = currentCharacter.getSpell(spellID).getSpeed();
            Event e = currentCharacter.getSpell(spellID).getEvent().getCopiedEvent();

            e.setDirection(direction, speed);
            e.setX(currentCharacter.getX());
            e.setY(currentCharacter.getY());
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
                        if(focus.character != null)
                        {
                            currentCharacter.getFitness().scoreHeal(focus.character, currentCharacter); // scoring
                            currentCharacter.getFitness().addHistory(currentCharacter.getId()+" "+action.toString()+" "+currentCharacter.getFitness().toStringFitness());
                        }
                        else
                        {
                            currentCharacter.getFitness().scoreUnlessSpell();
                            currentCharacter.getFitness().addHistory(currentCharacter.getId()+" "+action.toString()+" "+currentCharacter.getFitness().toStringFitness());
                            currentCharacter.getFitness().debugFile((currentCharacter.isMonster()?"mob ":"genPlayer ")
                                    +currentCharacter.getName()+" "+currentCharacter.getTrueID()+" a soign� personne ."+currentCharacter.getFitness().toStringFitness(),true);
                        }
                        messageHandler.addPlayerMessage(new Message("Heal critic " + heal + " to the " + focus.character.getName() + "", MESSAGE_TYPE_ERROR), turn);

                    } else {
                        currentCharacter.takeDamage(damage, e.getType());
                        if(focus.character != null)
                        {
                            currentCharacter.getFitness().scoreSpell(focus.character, currentCharacter); // scoring
                            currentCharacter.getFitness().addHistory(currentCharacter.getId()+" "+action.toString()+" "+currentCharacter.getFitness().toStringFitness());
                        }
                        else
                        {
                            currentCharacter.getFitness().scoreUnlessSpell();
                            currentCharacter.getFitness().addHistory(currentCharacter.getId()+" "+action.toString()+" "+currentCharacter.getFitness().toStringFitness());
                            currentCharacter.getFitness().debugFile((currentCharacter.isMonster()?"mob ":"genPlayer ")
                                    +currentCharacter.getName()+" "+currentCharacter.getTrueID()+"a attaqu� personne (echec crit) ."+currentCharacter.getFitness().toStringFitness(),true);
                        }
                        messageHandler.addPlayerMessage(new Message("Use " + SpellData.getSpellById(spellID).getName() + " on " + currentCharacter.getName() + " and deal critic " + damage, MESSAGE_TYPE_ERROR), turn);

                    }
                    if (currentCharacter.checkDeath()) {
                        // TODO ADD a textual event
                        Gdx.app.log(LABEL, "-----------------------------------------");
                        Gdx.app.log(LABEL, "DEATH FOR" + currentCharacter.toString());
                        Gdx.app.log(LABEL, "-----------------------------------------");
                        messageHandler.addPlayerMessage(new Message(currentCharacter.getName() + "Died "), turn);
                        if(turn > 0 ) turn--;
                        currentCharacter.getFitness().debugFile("*** " + (currentCharacter.isMonster() ? "mob " : "genPlayer ")
                                + currentCharacter.getName() + " " + currentCharacter.getTrueID() + " est mort ." + currentCharacter.getFitness().toStringFitness(), true);
                        if(players.contains(currentCharacter))
                            players.remove(currentCharacter);
                        else
                            mobs.remove(currentCharacter);
                        playerNumber--;

                        checkEndGame();
                       /* switchTurn();*/
                        currentCharacter.setHasPlayed(true);
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
                                currentCharacter.getFitness().scoreHeal(focus.character, currentCharacter); // scoring
                                currentCharacter.getFitness().addHistory(currentCharacter.getId() + " " + action.toString() + " " + currentCharacter.getFitness().toStringFitness());

                            } else {
                                damage = focus.character.takeDamage(damage, e.getType());
                                messageHandler.addPlayerMessage(new Message("Use " + SpellData.getSpellById(spellID).getName() + " on " + focus.character.getName() + " and deal " + damage), turn);
                                currentCharacter.getFitness().scoreSpell(focus.character, currentCharacter); // scoring
                                currentCharacter.getFitness().addHistory(currentCharacter.getId() + " " + action.toString() + " " + currentCharacter.getFitness().toStringFitness());

                            }
                        else {
                            damage = focus.character.takeDamage(damage, e.getType());
                            if (state > 0)
                                messageHandler.addPlayerMessage(new Message("Use " + SpellData.getSpellById(spellID).getName() + " on " + focus.character.getName() + " and deal critic " + damage, MESSAGE_TYPE_ERROR), turn);
                            else
                                messageHandler.addPlayerMessage(new Message("Use " + SpellData.getSpellById(spellID).getName() + " on " + focus.character.getName() + " and deal " + damage), turn);
                            if(focus.character != null)
                            {
                                currentCharacter.getFitness().scoreSpell(focus.character, currentCharacter); // scoring
                                currentCharacter.getFitness().addHistory(currentCharacter.getId()+" "+action.toString()+" "+currentCharacter.getFitness().toStringFitness());
                            }

                        }
                        if (focus.character.checkDeath()) {
                            Gdx.app.log(LABEL, "-----------------------------------------");
                            Gdx.app.log(LABEL, "DEATH FOR" + focus.character.toString());
                            Gdx.app.log(LABEL, "-----------------------------------------");
                            messageHandler.addPlayerMessage(new Message(focus.character.getName() + "Died "), turn);
                            int index = Math.max(players.indexOf(focus.character), mobs.indexOf(focus.character));
                            int indexCurrent = turn;
                            if(focus.character.isMonster())
                                index+=players.size();
                            currentCharacter.getFitness().debugFile("*** " + (focus.character.isMonster() ? "mob " : "genPlayer ") +
                                    focus.character.getName() + " " + focus.character.getTrueID() + " a �t� tu� par " + (currentCharacter.isMonster() ? "mob " : "genPlayer ") + currentCharacter.getName() + " " + currentCharacter.getTrueID() + ".", true);

                            players.remove(focus.character);

                            mobs.remove(focus.character);
                            playerNumber--;
                            if (index < indexCurrent)
                               turn--;
                            checkEndGame();
                        }
                    } else {
                        messageHandler.addPlayerMessage(new Message("Vous avez lance " + SpellData.getSpellById(spellID).getName() + " mais personne n'a ete touch?"), turn);
                        currentCharacter.getFitness().scoreUnlessSpell();
                        currentCharacter.getFitness().addHistory(currentCharacter.getId() + " " + action.toString()+" "+currentCharacter.getFitness().toStringFitness());
                        currentCharacter.getFitness().debugFile((currentCharacter.isMonster()?"mob ":"genPlayer ")
                                +currentCharacter.getName()+" "+currentCharacter.getTrueID()+" a lanc� un sort sur personne ."+currentCharacter.getFitness().toStringFitness(),true);

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
        } else if (action.startsWith("p")) { // Pass turn
            //currentCharacter.getFitness().scorePassTurn();
            //currentCharacter.getFitness().addHistory(currentCharacter.getId()+";"+action.toString());
            //currentCharacter.getFitness().debugFile((currentCharacter.isMonster()?"mob ":"genPlayer ")+
             //       currentCharacter.getName()+" "+currentCharacter.getTrueID()+" PASSE son tour ."+currentCharacter.getFitness().toStringFitness(),true);
            currentCharacter.getFitness().scorePassTurn();
            currentCharacter.getFitness().addHistory(currentCharacter.getId()+" "+action.toString()+" "+currentCharacter.getFitness().toStringFitness());
            currentCharacter.getFitness().debugFile((currentCharacter.isMonster()?"mob ":"genPlayer ")+
                    currentCharacter.getName()+" "+currentCharacter.getTrueID()+" PASSE son tour ."+currentCharacter.getFitness().toStringFitness(),true);

            currentCharacter.setHasPlayed(true);

        }else if (action.startsWith("m")) {// Movement action
            try {
                String[] tokens = action.split(":");
                if (tokens.length != 3)
                    throw new IllegalActionException("Wrong number of arguments in action string");

                String position = tokens[1] + ":" + tokens[2];
                // TODO call aStar and check if character don't fall into trap
                currentCharacter.moveTo(position);
                currentCharacter.getFitness().scoreMove();
                currentCharacter.getFitness().addHistory(currentCharacter.getId()+" "+action.toString()+" "+currentCharacter.getFitness().toStringFitness());
                currentCharacter.getFitness().debugFile((currentCharacter.isMonster()?"mob ":"genPlayer ")
                        +currentCharacter.getName()+" "+currentCharacter.getTrueID()+" BOUGE en "+position+" ."+currentCharacter.getFitness().toStringFitness(),true);

                currentCharacter.setHasPlayed(true);
                Gdx.app.log(LABEL,"==== in the decode action movement M");
            } catch (IllegalMovementException ime) {

                if(Data.autoIA && currentCharacter.isNpc())
                    currentCharacter.setHasPlayed(true);
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
    protected Focus getFirstCharacterRange(ArrayList<Character> chars, Event e) {
        float range = MAX_RANGE;
        Gdx.app.log(LABEL, "Search the first character range : " + e.toString() + ", " + chars.toString());
        Character focus = null;
        for (Character c : chars) {
            if (e.getDirection() == NORTH || e.getDirection() == SOUTH) {
                float i = (Math.abs(c.getY() - (e.getY())));
                Gdx.app.log(LABEL, "c.getY() = [" + c.getY() + "], e.getYOnBoard = [" + (e.getY()) + "], i = [" + i + "]");

                if (i < range) {
                    range = i;
                    focus = c;
                }
            }
            if (e.getDirection() == EAST || e.getDirection() == WEST) {
                Gdx.app.log(LABEL, "c.getX() = [" + c.getX() + "], e.getXOnBoard = [" + (e.getX()) + "], i = [" + (c.getX() - e.getX())
                        + "]");
                float i = (Math.abs(c.getX() - (e.getX())));

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
    public ArrayList<Character> getCharacterPositionOnLine(int x, int y, int direction) {

        ArrayList<Character> c = new ArrayList<Character>();

        if (direction == SELF) {
            c.add(currentCharacter);
            return c;
        }

        for (int i = 0; i < players.size(); i++) {
            // above
            if (direction == SOUTH && players.get(i).getY() < y && players.get(i).getX() == x)
                c.add(players.get(i));
            // bottom
            if (direction == NORTH && players.get(i).getY() > y && players.get(i).getX() == x)
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
            if (direction == SOUTH && mobs.get(i).getY() < y && mobs.get(i).getX() == x)
                c.add(mobs.get(i));
            // bottom
            if (direction == NORTH && mobs.get(i).getY() > y && mobs.get(i).getX() == x)
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
     * @param x, in block
     * @param y, in block
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
        if(mobs.size() == 0 || players.size() == 0 || (global_turn == Data.maxTurn && Data.autoIA))
        {
            if( mobs.size() <= 0 ){
                //GAME WIN
                gameEnded = true;
                gameWin = true;
            }else
            {
                //GAME LOSE
                gameEnded = true;
                gameLose = true;
            }
        }
        if(gameEnded)
        {

            System.out.println("mob size : " + mobs.size() + " genPlayers size : " + players.size());
            System.out.println("-- FIN DE JEU-- ");
          if(Data.autoIA) {
              endGameLogs();
              if(loopNumber >= Data.MAX_GAME_LOOP) {
                  Gdx.app.log(LABEL,"On a atteint le max de lancements du jeu. Adios amigos !");
                  try {
                      Hadoop.saveWebDataOnHive();
                  } catch (SQLException e) {
                      Gdx.app.error(LABEL, "Error while saving web datas "+e.getMessage());
                  }
                  Data.suppressPoolDir(0);
                  quitGame();
              }
          }
        }
    }

    /**
     * Write all mobs logs (scores)
     */
    public void endGameLogs(){
        originMobs.get(0).getFitness().debugFile("-- FIN DE JEU --", true);
        List<String> hiveList = new ArrayList<String>();
        for(Mob mo : originMobs){
            mo.getFitness().debugFile(	"Mob id="+mo.getTrueID()+" name="+mo.getName()+
                " score final = "+mo.getFitness().calculFinalScore(gameLose, global_turn)+""+
                mo.getFitness().toStringFitness(), true);
            hiveList.add(mo.toStringForHive());
            System.out.println("Mob id=" + mo.getId() + " name=" + mo.getName() + " " + mo.getFitness().toStringFitness() + " score final = " + mo.getFitness().getFinalScore());
            int i = 0;
            for(long time : mo.executionTimeByTurn){
                i++;
                System.out.println("Tour "+i+" - execution: "+time+" milli" );
            }
        }
        for(Player po : originPlayers){
            po.getFitness().debugFile("Player id=" + po.getTrueID() + " name=" + po.getName() +
                    " score final = " + po.getFitness().calculFinalScore(gameWin, global_turn) + "" +
                    po.getFitness().toStringFitness(), true);
            hiveList.add(po.toStringForHive());
            po.getFitness().writeHistory(po, false, loopNumber);
            try {
                Hadoop.saveGeneticDataOnHive(po.getName(), ""+po.getGeneration(), Data.getDate(), po.getFitness().getFinalScore(), po.getFitness().getpAction(), po.getFitness().getpHeal(), po.getFitness().getpPass());
            } catch (SQLException e) {
                Gdx.app.error(LABEL, "Save score on Hive : "+e.getMessage());
            }
            System.out.println("Player id=" + po.getId() + " name=" + po.getName() + " " + po.getFitness().toStringFitness() + " score final = " + po.getFitness().getFinalScore());

            int i = 0;
            for(long time : po.executionTimeByTurn){
                i++;
                System.out.println("Tour "+i+" - execution: "+time+" milli" );
            }

        }
        try {
            Hadoop.saveGeneticDataOnHive(hiveList);
            //Hadoop.saveGeneticDataOnHive(mo.getName(), ""+mo.getGeneration(), Data.getDate(), mo.getFitness().getFinalScore(), mo.getFitness().getpAction(), mo.getFitness().getpHeal(),mo.getFitness().getpPass());
        } catch (SQLException e) {
            Gdx.app.error(LABEL, "Can't save score on Hive : " + e.getMessage());
        }

        originMobs.get(0).getFitness().renameScoreFile();
        //Data.switchAllTestPool();

        Gdx.app.log(LABEL,"On move les scripts sélectionnés");
        Data.moveSelectedTreeTo(CompileString.destPathClass + Data.poolToTestDir, CompileString.destPathClass + Data.poolTestedDir);
        stopAllThread();
    }

    public void stopAllThread() {
        if(Data.RUN_APIX)
            apix.stop();
        CommandHandler.getInstance().getThread().stop();
       // CameraHandler.getInstance().getThread().stop();
        turnTimer = Integer.MAX_VALUE;
    }

    public void quitGame()
    {
        Gdx.app.exit();
    }


    /**
     * TODO correct this function
     * @param screenX, x position on the screen
     * @param screenY, y postion on the screen
     */
    public void checkActionAtPosition(int screenX, int screenY) {
        screenY = Math.abs(Data.SCREEN_HEIGHT - screenY);
        //Gdx.app.log(LABEL, "In check action at position for ["+screenX+"/"+screenY+"] MAP position = ["+MAP_X+"/"+MAP_Y+"]");
        int blockX, blockY;
        blockX = (int) ((screenX - (int)MAP_X)/ (BLOCK_SIZE_X / scale));
        blockY = (int) ((screenY - (int)MAP_Y)/ (BLOCK_SIZE_Y / scale));
        //Gdx.app.log(LABEL, "Supposed "+blockX+"/"+blockY);

        selectBlock(blockX, blockY);
    }

    /**
     *
     * @param x
     * @param y
     */
    public void selectBlock(int x, int y){
        if (playerHandler.getMainPlayer() == currentCharacter){
            //if its main player turn
            try {
                if(playerHandler.getMainPlayer().isSpellSelection()){//try tu use spell
                    int direction = getSelectedDirection(x, y);
                    if(direction != -1) {
                        decodeAction(playerHandler.getMainPlayer().getSpellSelected().getId() + ":" + direction);
                        playerHandler.getMainPlayer().resetSpellSelection();
                        ui.resetSpellSelection();
                    }
                }else//try to move to destination
                    decodeAction("m:"+x+":"+y);
            } catch (IllegalActionException e) {
                Gdx.app.error(LABEL, e.getMessage());
            }
        }
    }

    private int getSelectedDirection(int x, int y){
        int px = playerHandler.getMainPlayer().getX();
        int py = playerHandler.getMainPlayer().getY();
        System.out.println(x+"/"+y);
        if(px == x && py == y){
            //self
            return Data.SELF;
        }

        if(x > px && y == py){
            //EAST
            return Data.EAST;
        }
        if(x < px && y == py){
            // WEST
            return Data.WEST;
        }
        if(x == px && y > py){
            //NORTH
            return Data.NORTH;
        }
        if(x == px && y < py){
            //SOUTH
            return Data.SOUTH;
        }
        return -1;
    }


    protected class Focus {
        public float range;
        public Character character;

        public Focus(float range, Character character) {
            this.range = range;
            this.character = character;
        }

        public String toString() {
            return "Focus [ range, " + range + ", " + character.toString() + "]";
        }
    }

    private void reinitAll(){
        players.clear();
        mobs.clear();
        originPlayers.clear();
        originMobs.clear();
        gameOn = false;
        gameWin = false;
        gameLose = false;
        global_turn = 0;
        turn = 0;
        previousCharacter = null;
        currentCharacter = null;
        /*Gdx.app.log(LABEL,"** Reinit All datas : players size = "+players.size()+", mobs size = "+mobs.size()+"**");
        Gdx.app.log(LABEL,"** Reinit All datas : Origin players size = "+originPlayers.size()+", Origin mobs size = "+originMobs.size()+"**");*/
        gameEnded = false;
    }
}
