package com.mygdx.game.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.mygdx.game.com.Hadoop;
import com.mygdx.game.game.WindowGame;
import com.mygdx.game.javacompiler.CompileString;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Class witch contains all static variables
 *
 * @author bob
 */
public class Data {
    private static String LABEL = "DATA";

    public static final boolean FULLSCREEN = false;
    public static final boolean DEBUG_DEPARTURE = true;
    public static final boolean tiDebug = true;
    private static final String CONFIG_FILE_PATH = "project_properties.txt";
    public static boolean HADOOP = false;
    public static boolean RELOAD_GAME_WHEN_ENDED = true;
    public static boolean debug = true;
    public static final boolean DISPLAY_PLAYER = true;
    public static final boolean runQRCam = false;
    public static boolean RUN_APIX = false;
    public static boolean debugPicture = false;
    public static final boolean inTest = true;
    public static final boolean debugQR = false;
    public static final int DEBUG_PLAYER = 4;
    public static final int DEBUG_NB_GENETIC_PLAYER = 4;
    public static boolean ANDROID = false;
    //public static String IMAGE_DIR ="C:/Users/boby/Google Drive/Master1/Synth�se/ImageDeTest/";
    public static String IMAGE_DIR = "C:/Users/fr�d�ric/Google Drive/Master Cergy/Projet_PlateauJeu/Synth�se/ImageDeTest/";

    public static BitmapFont font;

    //For Genetic IA=
    public static boolean autoIA = true;
    public static boolean jvm = false;//Joueur versus machine
    public static boolean generateIA = true;
    public static boolean combiningMode = true;
    public static int maxTurn = 20;
    public static String rootDir="";
    public static Array<String> allFilesToTest;
    public static Array<String> allFilesTested;
    // IA Param File Vars
    // Directories
    public static String paramDir = "ai"+File.separator;
    public static String poolsDir = "aiFiles"+File.separator;
    public static String paramFileNAme = "ParamAI.txt";

    public static String poolToTestDir = "PoolATester"+File.separator;
    public static String poolTestedDir = "PoolTestee"+File.separator;
    // vars in param
    public static String poolTestName = "PoolTest";
    public static String bestRateName = "BestMobRate";
    public static String worstRateName = "WorstMobRate";
    public static String crossRateName = "CrossMobRate";
    public static String crossingPoliticName = "CrossingPolitic";

    // IA Param File vars
    public static int bestMobRate = 0;
    /** Taux de récupération des pires mobs pour le prochain jeu  (en %) */
    public static int worstMobRate = 0;
    /** Taux de combinaison des mobs pour le prochain jeu  (en %) */
    public static int crossMobRate = 0;

    public static int MAX_GAME_LOOP = 10;
    public static int Number_Generated_IA = 20;
    public static int Number_Combine_IA = 20;
    public static int All_Players_Number = 8;
    public static Array <String> selectedIAFiles;

    // Const TI Part
    public static long WAIT_TI = 5000;
    public static int SEUILINITTI = 100;
    public static int SEUILETI = 200;
    public static int MIN_SEUIL_FORM = 50;
    public static int MAX_SEUIL_FORM = 5000;
    public static int QRCamSeuil = 60;//Data.SEUILETI;

    public static String TITLE = "Jeu de plateau";
    public static int MAP_WIDTH;
    public static int MAP_HEIGHT;
    public static int BLOCK_SIZE_X;
    public static int BLOCK_SIZE_Y;
    public static float MAP_X;
    public static float MAP_Y;
    public static int BLOCK_NUMBER_X;
    public static int BLOCK_NUMBER_Y;
    public static int DECK_AREA_SIZE_X;
    public static int DECK_AREA_SIZE_Y;
    public static int RELATIVE_X_POS;
    public static int RELATIVE_Y_POS;
    public static float SCALE;
    public static int SCREEN_WIDTH = 1920;
    public static int SCREEN_HEIGHT = 1080;
    public static int TOTAL_WIDTH;
    public static int TOTAL_HEIGHT;

    public static int TURN_MAX_TIME = 3000; // in sec

    //For the stat display
    //Player
    public static int PLAYER_LIFE_RECT_X_POS = 10;
    public static int PLAYER_LIFE_RECT_Y_POS = 10;
    public static int PLAYER_LIFE_RECT_X_SIZE = 100;
    public static int PLAYER_LIFE_RECT_Y_SIZE = 10;
    public static int PLAYER_MANA_RECT_X_POS = 10;
    public static int PLAYER_MANA_RECT_Y_POS = 25;
    public static int PLAYER_MANA_RECT_X_SIZE = 50;
    public static int PLAYER_MANA_RECT_Y_SIZE = 10;
    public static int PLAYER_ICON_X_POS = 10;
    public static int PLAYER_ICON_Y_POS = 50;
    public static int PLAYER_MESSAGE_X_POS = 120;
    public static int PLAYER_MESSAGE_Y_POS = 10;

    //Mob
    public static boolean MOB_LIFE_SHOW = true;
    public static int MOB_LIFE_RECT_X_SIZE = 30;
    public static int MOB_LIFE_RECT_Y_SIZE = 3;

    public static final int UP = 0;
    public static final int LEFT = 1;
    public static final int DOWN = 2;
    public static final int RIGHT = 3;
    public static final int SELF = 360;
    public static final int SOUTH = 0;
    public static final int SOUTH_EAST = 45;
    public static final int SOUTH_WEST = -45;
    public static final int EAST = 90;
    public static final int NORTH_EAST = 135;
    public static final int NORTH = 180;
    public static final int NORTH_WEST = -135;
    public static final int WEST = -90;

    public static final int INF = 500;

    public static String MAP_FILE = "";
    public static final String MONSTER_DATA_XML = "xml/monstersData.xml";
    public static final String SPELLS_DATA_XML = "xml/spells.xml";
    public static final String TRAPS_DATA_XML = "xml/traps.xml";
    public static final String MAP_XML = "xml/map.xml";
    public static final String HERO_XML = "xml/hero.xml";

    public static final HashMap<String, Event> eventMap = new HashMap<String, Event>();
    public static final HashMap<String, Boolean> untraversableBlocks = new HashMap<String, Boolean>();
    public static final HashMap<String, Boolean> departureBlocks = new HashMap<String, Boolean>();
    public static final int MAX_RANGE = Integer.MAX_VALUE;
    public static final long WAINTING_TIME = 1000;
    public static Color BLOCK_REACHABLE_COLOR = new Color(1f, 0f, 0f, .1f);
    public static final Color TEXT_COLOR = new Color(Color.BLACK);
    public static boolean SHOW_MOB_REACHABLE_BLOCKS = false;
    public static int MAX_PLAYER = 4;
    public static int INIT_MAX_TIME = 40;

    private static boolean initImageDir = false;

    public static TiledMap map;
    public static TiledMapRenderer tiledMapRenderer;
    public static MonsterData monsterData;
    public static WindowGame game;
    public static long beginTime;

    public static final long REFRESH_TIME_EVENT = 500;//in milli

    public static Sound BACKGROUND_MUSIC;
    public static long backgroudMusicId;

    //AI
    public static final long TIME_LIMIT = 10000;
    public static final float[] AI_BORDERS = {-10.f, -5.f, -2.f};
    public static final int AI_DEPTH_MAX = 1;
    public static final float[][] AI_FACTORS = {
            {2.f, 1.5f, 0.75f, 0.2f, 1.0f, 0.8f}, // coward
            {1.f, 0.5f, 0.40f, 0.3f, 1.8f, 5.0f}, // lonewolf
            {1.f, 1.0f, 0.50f, 0.5f, 2.0f, 1.5f}, // normal
            {1.f, 1.0f, 0.50f, 0.5f, 2.0f, 1.5f} // player
    };

    public static final float[][] AI_HEURISITCS = {
            {20.f, 20.f, 30.f, 2.0f, 4.0f},    //coward
            {10.f, 5.0f, 20.f, 2.0f, 2.0f},    //lonewolf
            {10.f, 10.f, 20.f, 2.0f, 2.0f},    //normal
            {10.f, 10.f, 20.f, 2.0f, 2.0f}    //player
    };

    public static final int COWARD = 0;
    public static final int LONEWOLF = 1;
    public static final int NORMAL = 2;
    public static final int PLAYER = 3;

    public static final int LIFE = 2;
    public static final int ALLIES_LIFE = 1;
    public static final int MANA = 3;
    public static final int TARGET_DETECTION = 4;

    public static final int ENEMIES_DETECTION = 0;
    public static final int ALLIES_DETECTION = 1;
    public static final int MIN_LIFE = 2; //min percentage life before unfocus
    public static final int BACKUP_ALLIES = 3; //min percentage of allies' life to heal them
    public static final int RUNAWAY = 4; // min danger level to run away
    public static final int STICK_TO_ALLIES = 5; //min danger level to go near allies

    //MESSAGES PARAM
    public static final long MESSAGE_DURATION = 3000;
    public static final Color MESSAGE_COLOR_TYPE_1 = new Color(Color.RED);
    public static final Color MESSAGE_COLOR_TYPE_0 = new Color(Color.BLACK);
    private static final Color MESSAGE_COLOR_TYPE_2 = new Color(Color.GREEN);
    public static final int MESSAGE_TYPE_INFO = 0;
    public static final int MESSAGE_TYPE_ERROR = 1;
    public static final int MESSAGE_TYPE_CONSTANT = -1;
    private static final int MESSAGE_TYPE_SUCCES = 2;
    public static final Color DEFAULT_COLOR = Color.BLACK;
    public static final int ACTION_PER_TURN = 1;

    //ERROR MESSAGES
    public static final String ERROR_TOO_MUCH_ACTION = "Une seul action par tour !";

    //MESSAGES
    public static final String INIT_PLAYER_TEXT = "Time until the game begin :";
    public static final String TURN_TEXT = "End of turn in : ";
    public static final String DEPARTURE_BLOCK_ERROR = "Le pion doit �tre sur une case de d�part !";
    public static final int FONT_SIZE = 10;
    public static final int FONT_HEIGHT = 12;
    public static Texture IMAGE_HALO = null;
    public static float MUSIC_VOLUM = .1f;
    public static float MUSIC_PITCH = 1;
    public static String MAIN_TEXT = "";
    public static int MESSAGE_MAX_LENGTH;

    public static Texture WIN_IMAGE;
    public static Texture LOSE_IMAGE;
    public static float ENDING_ANIMATION_Y = 0;
    public static float ENDING_ANIMATION_X = 0;
    public static float ENDING_ANIMATION_SCALE = 0;

    /* Zoom scale */
    public static float scale = 1.0f;

    /* game launched on android or pc player not on the projected game */
    public static boolean singlePlayer = false;

    /* SKIN for HUD */
    public static Skin SKIN;
    public static TextureAtlas ATLAS;
    private static boolean isInitHadoop = false;

    /**
     * Load all game variables
     */
    public static void loadGame() {

        System.out.println(" Begin data init ");
        beginTime = System.currentTimeMillis();

        map = new TmxMapLoader().load(Data.MAP_FILE);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map);

        MapProperties mapP = map.getProperties();
        Data.BLOCK_NUMBER_X = mapP.get("height", Integer.class);
        Data.BLOCK_NUMBER_Y = mapP.get("width", Integer.class);
        Data.BLOCK_SIZE_X = mapP.get("tilewidth", Integer.class);
        Data.BLOCK_SIZE_Y = mapP.get("tileheight", Integer.class);
        Data.MAP_HEIGHT = Data.BLOCK_NUMBER_Y * Data.BLOCK_SIZE_Y;
        Data.MAP_WIDTH = Data.BLOCK_NUMBER_X * Data.BLOCK_SIZE_X;
        Data.DECK_AREA_SIZE_X = Data.MAP_WIDTH / 2; // Voir pour la largeur de la surface des cartes
        Data.DECK_AREA_SIZE_Y = Data.BLOCK_SIZE_Y * 3;
        Data.RELATIVE_X_POS = 288;//Data.DECK_AREA_SIZE_Y * 3;
        Data.RELATIVE_Y_POS = 0;
        Data.MAP_X = 0;
        Data.MAP_Y = 0;
        Data.TOTAL_HEIGHT = Data.MAP_HEIGHT + 2 * Data.DECK_AREA_SIZE_Y;
        Data.TOTAL_WIDTH = Data.MAP_WIDTH + 2 * Data.DECK_AREA_SIZE_Y;
        Data.SCALE = (float) Data.SCREEN_HEIGHT / Data.TOTAL_HEIGHT;
        Data.MESSAGE_MAX_LENGTH = (Data.MAP_WIDTH - Data.DECK_AREA_SIZE_X - Data.PLAYER_LIFE_RECT_X_SIZE) / Data.FONT_SIZE;
        Data.ENDING_ANIMATION_SCALE = (float) Data.MAP_WIDTH / (float) Data.WIN_IMAGE.getWidth();
        Data.ENDING_ANIMATION_X = Data.MAP_X;
        Data.ENDING_ANIMATION_Y = Data.MAP_Y - Data.WIN_IMAGE.getHeight() * Data.ENDING_ANIMATION_SCALE;

        //load font
        font = new BitmapFont(Gdx.files.internal("fonts/Font.fnt"), true);
        font.setColor(TEXT_COLOR);

        SKIN = new Skin(Gdx.files.internal("ui/defaultskin.json"));
        ATLAS = new TextureAtlas(Gdx.files.internal("ui/defaultskin.atlas"));
        System.out.println("MAP_FILE = " + Data.MAP_FILE + ", MAP_WIDTH = "
                + Data.MAP_WIDTH + ", MAP_HEIGHT = " + Data.MAP_HEIGHT
                + ", BLOCK_NUMBER = " + Data.BLOCK_NUMBER_X
                + ", BLOCK_SIZE_X = " + Data.BLOCK_SIZE_X + ", BLOCK_SIZE_Y = "
                + Data.BLOCK_SIZE_Y + ", SCALE = " + Data.SCALE);
        createDirectoryIA();
    }

    /*
    Create directory needed to IA
     */
    public static void createDirectoryIA()
    {
        File f = new File(CompileString.destPathClass);
        if (!f.isDirectory())
            f.mkdir();
        f = new File(CompileString.destPathClass+CompileString.pathHist);
        if (!f.isDirectory())
            f.mkdir();
        f = new File(CompileString.destPathClass+Data.poolToTestDir);
        if (!f.isDirectory())
            f.mkdir();
        f = new File(CompileString.destPathClass+Data.poolTestedDir);
        if (!f.isDirectory())
            f.mkdir();
    }

    /*
    Generate X IA mobs or players
     */
    public static void generateXIA(int x)
    {
        for(int i=1 ; i <= x ; i++)
        {
            CompileString.generateTree("x" + i + "_1");
        }
    }

    /** Rempli des listes avec tout les fichiers de PoolATester et PoolTestee.
     *
     */
    public static void setPoolsArrays(){
        File directory= new File(CompileString.destPathClass+Data.poolToTestDir);
        allFilesToTest = new Array<String>();
        for (File file : directory.listFiles()) {
            if (file.getName().contains(CompileString.serializePrefix+"x") && file.getName().contains(".txt")) {
                Gdx.app.log(LABEL,"SetPoolsArray added file "+file.getName());
                allFilesToTest.add(file.getName());
            }
        }
        directory= new File(CompileString.destPathClass+Data.poolTestedDir);
        allFilesTested = new Array<String>();
        for (File file : directory.listFiles()) {
            if (file.getName().contains(CompileString.serializePrefix+"x") && file.getName().contains(".txt"))
                allFilesTested.add(file.getName());
        }
        Gdx.app.log(LABEL,"First pool to test file name "+allFilesToTest.get(0));
    }

    /*
    Select Random IA from repository
     */
    public static void getRandomIAGeneticList()
    {
        if(allFilesToTest.size < All_Players_Number )
            for(int i = allFilesToTest.size;i<All_Players_Number;i++){
                CompileString.generateTree("x" + i + "_1");
            }
        selectedIAFiles = new Array<String>();
        Random r = new Random();
        int randIdx;
        for(int i=0; i<All_Players_Number;i++) {
            randIdx = r.nextInt(allFilesToTest.size);
            while(isInArray(allFilesToTest.get(randIdx), selectedIAFiles))
                randIdx = r.nextInt(allFilesToTest.size);
            selectedIAFiles.add(allFilesToTest.get(randIdx));
        }
        for(String s : selectedIAFiles)
            System.out.println(s);
    }

    public static boolean isInArray(String st, Array<String> arrayTest)
    {
        for(String s : arrayTest)
            if(s.contains(st))
                return true;
        return false;
    }

    public static void initSpell() {

    }

    public static void loadMap() {
        Element root = XMLReader.readXML(Gdx.files.internal(Data.MAP_XML).path());

        // ----------------- r�cup�ration du chemin de la carte dans le fichier map.xml------//
        Element mapInfo = root.getChildByName("mapInfo");
        Data.MAP_FILE = mapInfo.getChildByName("background").getText();

        Array<Element> blocks = root.getChildrenByName("block");
        int x, y;
        Element block;
        for (Iterator<Element> it = blocks.iterator(); it.hasNext(); ) {
            block = it.next();

            if (block.getChildByName("untraversable") == null)
                continue;

            x = block.getInt("x");
            y = block.getInt("y");
            untraversableBlocks.put(x + ":" + y, new Boolean(true));
            if (debug)
                System.out.println("New untravesable block at : [" + x + ":" + y + "]");
        }

        for (Iterator<Element> it = root.getChildByName("departure").getChildrenByName("block").iterator(); it.hasNext(); ) {
            block = it.next();
            x = block.getInt("x");
            y = block.getInt("y");
            departureBlocks.put(x + ":" + y, false);
            if (debug)
                System.out.println("New departure blok at : [" + x + ":" + y + "]");
        }

        BACKGROUND_MUSIC = Gdx.audio.newSound(Gdx.files.internal(root.getChildByName("music").getText()));
        IMAGE_HALO = new Texture(Gdx.files.internal(root.getChildByName("halo_image").getText()));
        WIN_IMAGE = new Texture(Gdx.files.internal(root.getChildByName("win_image").getText()));
        LOSE_IMAGE = new Texture(Gdx.files.internal(root.getChildByName("lose_image").getText()));


    }

    public static void playBackgroundMusic() {
        backgroudMusicId = BACKGROUND_MUSIC.loop(MUSIC_VOLUM);
    }

    public static void musicVolumeUp(float volume) {
        MUSIC_VOLUM = volume;
        BACKGROUND_MUSIC.setVolume(backgroudMusicId, MUSIC_VOLUM);

    }

    public static String getImageDir() {
        if (!initImageDir) {
            IMAGE_DIR += getDate() + "/";
            new File(IMAGE_DIR).mkdir();

            initImageDir = true;
        }
        return IMAGE_DIR;
    }


    public static String getDate() {
        Date date = new Date();
        DateFormat formater = new SimpleDateFormat("dd-MM-yy-hh-mm-ss");
        return formater.format(date);
    }

    public static void checkValuesIni(String filePath) {
        Scanner scanner;
        try {
            File file = new File(filePath);
            if (file.exists() && file.length() > 0) {
                System.out.println("length = " + file.length());
                scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.contains("=") && !line.contains("#")) {
                        try {
                            line = line.replaceAll(" ", "");
                            line = line.replaceAll(";", "");
                            if (line.contains("seuilinit"))
                                if (!line.substring(line.lastIndexOf("=") + 1).equals(""))
                                    Data.SEUILINITTI = Integer.parseInt(line.substring(line.lastIndexOf("=") + 1));
                            if (line.contains("seuiletiquetage"))
                                if (!line.substring(line.lastIndexOf("=") + 1).equals(""))
                                    Data.SEUILETI = Integer.parseInt(line.substring(line.lastIndexOf("=") + 1));
                            if (line.contains("seuilmin"))
                                if (!line.substring(line.lastIndexOf("=") + 1).equals(""))
                                    Data.MIN_SEUIL_FORM = Integer.parseInt(line.substring(line.lastIndexOf("=") + 1));
                            if (line.contains("seuilmax"))
                                if (!line.substring(line.lastIndexOf("=") + 1).equals(""))
                                    Data.MAX_SEUIL_FORM = Integer.parseInt(line.substring(line.lastIndexOf("=") + 1));
                            if (line.contains("qrcamseuil"))
                                if (!line.substring(line.lastIndexOf("=") + 1).equals(""))
                                    Data.QRCamSeuil = Integer.parseInt(line.substring(line.lastIndexOf("=") + 1));
                            if (line.contains("initmaxtime"))
                                if (!line.substring(line.lastIndexOf("=") + 1).equals(""))
                                    Data.INIT_MAX_TIME = Integer.parseInt(line.substring(line.lastIndexOf("=") + 1));
                            if (line.contains("musicvolum"))
                                if (!line.substring(line.lastIndexOf("=") + 1).equals(""))
                                    Data.MUSIC_VOLUM = Float.parseFloat(line.substring(line.lastIndexOf("=") + 1));
                            if (line.contains("gamewait"))
                                if (!line.substring(line.lastIndexOf("=") + 1).equals("")) {
                                    int tempWait = Integer.parseInt(line.substring(line.lastIndexOf("=") + 1));
                                    if (tempWait > 2000 && tempWait < 10000)
                                        Data.WAIT_TI = tempWait;
                                }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                scanner.close();
            } else // file does not exist
            {
                System.out.println("fail------------------------------");
                try {
                    file.createNewFile();
                    FileWriter writer = new FileWriter(file, true);

                    String texte = "seuilinit=" + Data.SEUILINITTI + ";";
                    writer.write(texte, 0, texte.length());
                    writer.write("\r\n");

                    texte = "seuiletiquetage=" + Data.SEUILETI + ";";
                    writer.write(texte, 0, texte.length());
                    writer.write("\r\n");

                    texte = "seuilmin=" + Data.MIN_SEUIL_FORM + ";";
                    writer.write(texte, 0, texte.length());
                    writer.write("\r\n");

                    texte = "seuilmax=" + Data.MAX_SEUIL_FORM + ";";
                    writer.write(texte, 0, texte.length());
                    writer.write("\r\n");

                    texte = "";
                    writer.write(texte, 0, texte.length());
                    writer.write("\r\n");

                    texte = "qrcamseuil=" + Data.QRCamSeuil + ";";
                    writer.write(texte, 0, texte.length());
                    writer.write("\r\n");

                    texte = "initmaxtime=" + Data.INIT_MAX_TIME + ";";
                    writer.write(texte, 0, texte.length());
                    writer.write("\r\n");

                    texte = "musicvolum=" + Data.MUSIC_VOLUM + ";";
                    writer.write(texte, 0, texte.length());
                    writer.write("\r\n");

                    texte = "#Attention, ce param�tre a un fort impact sur le jeu";
                    writer.write(texte, 0, texte.length());
                    writer.write("\r\n");

                    texte = "gamewait=" + Data.WAIT_TI + ";";
                    writer.write(texte, 0, texte.length());
                    writer.write("\r\n");

                    writer.close(); // fermer le fichier � la fin des traitements
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Color getColorMessage(int type) {
        Color color;
        switch (type) {
            case Data.MESSAGE_TYPE_INFO:
                color = MESSAGE_COLOR_TYPE_0;
                break;
            case Data.MESSAGE_TYPE_ERROR:
                color = MESSAGE_COLOR_TYPE_1;
                break;
            case Data.MESSAGE_TYPE_SUCCES:
                color = MESSAGE_COLOR_TYPE_2;
                break;
            default:
                color = MESSAGE_COLOR_TYPE_0;
                break;
        }
        return color;
    }

    public static long getDurationMessage(int type) {
        switch (type) {
            case 0:
                return MESSAGE_DURATION;
            case 1:
                return MESSAGE_DURATION * 2;
            default:
                return MESSAGE_DURATION;
        }
    }

    /**
     * Dispose all ressource from class Data
     */
    public static void disposeData() {
        BACKGROUND_MUSIC.dispose();
        IMAGE_HALO.dispose();
    }

    public static void setForAndroid(boolean onAndroid){//Appelé lorsqu'on ne veut PAS lancer l'apprentissage
        System.out.println("CONFIG : Launch the game for single player in local (Single Player vs Generated Mob)");
        autoIA = false;
        generateIA = true;
        jvm = true;
        singlePlayer = true;
        ANDROID = onAndroid;
        RUN_APIX = false;
        RELOAD_GAME_WHEN_ENDED = false;
    }

    public static void setForIAGenetic(){//Appelé lorsqu'on VEUT lancer l'apprentissage
        System.out.println("CONFIG : Launch the game for Genetic AI (Mob vs Mob)");
        autoIA = true;
        generateIA = true;
        jvm = false;
        singlePlayer = false;
        ANDROID = true;
        RUN_APIX = false;
        RELOAD_GAME_WHEN_ENDED = true;
    }

    public static void setScreenSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        SCREEN_WIDTH = (int)screenSize.getWidth();
        SCREEN_HEIGHT = (int)screenSize.getHeight();
    }

    public static void setHadoop(){
        HADOOP = true;
    }

    public static void initHadoop(){
        if(!HADOOP)
            setHadoop();
        if(!isInitHadoop){
            try {
                Hadoop.createGeneticTable();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadProperties(){
        System.out.println("Load properties from "+CONFIG_FILE_PATH);
        File file = new File (CONFIG_FILE_PATH);
        if(!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        try {
            Scanner sc = new Scanner(file);
            String line = "";
            while(sc.hasNextLine()){
                line = sc.nextLine();
                if(line.startsWith("#"))
                    continue;
                if(line.contains(":")){
                    int index = line.indexOf(":");
                    String param = line.substring(0, index);
                    String value = line.substring(index+1);
                    switch(param){
                        case "HIVE" :
                            Hadoop.HIVE = value;
                            break;
                        case "HADOOP_PASSWORD":
                            Hadoop.HADOOP_USER_PASSWORD = value;
                            break;
                        case "HADOOP_USER":
                            Hadoop.HADOOP_USER_NAME = value;
                            break;
                        case "JDK":
                            CompileString.JDK_PATH = value;
                            break;
                        default :
                            System.err.println("Can't find : "+param);
                            break;
                    }
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /** This function is used to clean dirty strings.
     * It supresses spaces, tabulations, quotes, crochets and... retours chariot
     * @param toClean
     * @return
     */
    public static String supressUselessShit(String toClean){
        String cleaned = toClean.replaceAll(" ", "");
        cleaned = cleaned.replaceAll("   ","");
        cleaned = cleaned.replaceAll("\t","");
        cleaned = cleaned.replaceAll("\n","");
        cleaned = cleaned.replaceAll("\"", "");
        cleaned = cleaned.replace("[", "");
        cleaned = cleaned.replace("]", "");
        return cleaned;
    }

    /**
     *  Combine les mobs choisi aleatoirement et incrémente la génération
     */
    public static void combineRandomMobs(int numberOfCombine){//String name1, String name2, String resultName){
        Gdx.app.log(LABEL,"On combine en mode Random");
        String name1="", name2="", resultName="";
        Random r = new Random();
        int randomNumber1 = 0;
        int randomNumber2 = 0;
        int generation1 = 0;
        int generation2 = 0;
        int generationMax = 0;
        for(int index=0;index<numberOfCombine;index++) {
            name1="";
            name2="";
            while (name1.equals(name2)) {
                randomNumber1 = r.nextInt(allFilesTested.size);
                randomNumber2 = r.nextInt(allFilesTested.size);
                name1 = allFilesTested.get(randomNumber1);
                name2 = allFilesTested.get(randomNumber2);
            }
            generation1 = getGenerationFromFileName(name1);
            generation2 = getGenerationFromFileName(name2);
            generationMax = java.lang.Math.max(generation1, generation2);
            resultName = "x" + randomNumber1 + "_" + (generationMax+1);

            File resFile = new File(CompileString.destPathClass + Data.poolTestedDir + resultName);
            while (resFile.exists()) {
                resultName = "x" + randomNumber2 + "_" + (generationMax+1);
                generationMax++;
                resFile = new File(CompileString.destPathClass + Data.poolTestedDir + resultName);
            }
            Gdx.app.log(LABEL,"On combine les fichiers "+name1+" et "+name2+ "pour donner le fichier "+resultName);
            CompileString.combineTrees(name1, name2, resultName);
            File file1 = new File(CompileString.destPathClass+Data.poolTestedDir+ name1);
            if(!file1.delete()){
                Gdx.app.log(LABEL,"Supression impossible du fichier "+name1);
            }
            File file2 = new File(name2);
            if(!file2.delete()){
                Gdx.app.log(LABEL,"\"Supression impossible du fichier "+name2);
            }
        }
        Gdx.app.log(LABEL,"Combinaison random terminée");
    }

    public static int getGenerationFromFileName(String fileName){
        int generation=1;
        String[] fileParts;
        fileName = fileName.replace(".txt", "");
        fileName=fileName.replace(CompileString.serializePrefix, "");
        fileParts = fileName.split("_");
        generation = Integer.parseInt(fileParts[1]);
        return generation;
    }

    /** Move files depending on the switch mode
     *  0 = From PoolATester to local
     *  1 = From local to PoolTestee
     *  2 = From PoolTestee to PoolCroisee
     *  3 = From PoolCroisee to PoolATester
     */
    @Deprecated
    public static void switchAllTestPool() {
        Gdx.app.log(LABEL, "**switchTestPool begin");
        File srcPoolDir = new File(Data.poolsDir+Data.poolToTestDir);
        String name = "";
        Path source;
        Path target;
        File[] srcPoolFiles;
        if(!srcPoolDir.isDirectory()) {
            Gdx.app.log(LABEL, "**Switch Test Pool : "+srcPoolDir.getPath()+" is not a directory. !**");
            return;
        }
        srcPoolFiles = srcPoolDir.listFiles();
        for(int i = 0; i < srcPoolFiles.length;i++) {
            name = srcPoolFiles[i].getName();
            source = Paths.get(Data.poolsDir + Data.poolToTestDir + name);
            target = Paths.get(Data.poolsDir + Data.poolTestedDir + name);
            try {
                if (!srcPoolFiles[i].exists()) {
                    Gdx.app.log(LABEL, "**Param Moving File : source File "+srcPoolFiles[i].getName()+" doesn't exists !**");
                }else {
                    Files.move(source, target, REPLACE_EXISTING);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Gdx.app.log(LABEL, "**switchTestPool end");
    }

    /*
    Take all serialized tree in selectedIAFiles from ToTestPool and move them throw TestedPool
     */
    public static void moveSelectedTreeTo(String originPath, String destPath) {
        File origFile, destFile;
        for(String s : selectedIAFiles)
        {
            origFile = new File(originPath + s);
            origFile.renameTo(new File(destPath + s));
        }
    }

    /** Read the file for parameters and update variables.
     */
    public static void readParamFile() {
        Gdx.app.log(LABEL, "**readParamFile begins");
        // File fichier = new File(Data.rootDir+"/core/src/com/mygdx/game/IAPool/" + filename + ".txt");
        File fichier = Gdx.files.internal(Data.paramDir + Data.paramFileNAme).file();
        if (!fichier.exists() || fichier.isDirectory()){
            Gdx.app.log(LABEL, "**readParamFile file : "+fichier.getAbsolutePath()+" don't exist.  rootDir = "+rootDir);
            return;
        }
        boolean inPoolList = false;
        boolean inPolitic = false;
        // lecture du fichier texte
        try {
            InputStream ips = new FileInputStream(fichier);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            String ligne;
            while ((ligne = br.readLine()) != null) {
                // Supprime les espaces inutiles
                ligne = Data.supressUselessShit(ligne);
                if(inPolitic){
                    if (ligne.contains("}")){
                        Gdx.app.log(LABEL, "in Politic -> on trouve un }");
                        inPolitic= false;
                    }else{
                        if(ligne != null && !ligne.equals("") && ligne.contains("=")) {
                            Gdx.app.log(LABEL, "in Politic -> on trouve une ligne non nulle. ligne = "+ligne);
                            String[] rate = ligne.split("=");
                            if(ligne.contains(Data.bestRateName)){
                                bestMobRate = Integer.parseInt(rate[1]);
                            }else if(ligne.contains(Data.worstRateName)){
                                worstMobRate = Integer.parseInt(rate[1]);
                            }else if(ligne.contains(Data.crossRateName)){
                                crossMobRate = Integer.parseInt(rate[1]);
                            }
                        }
                    }
                }else if(inPoolList){
                    if (ligne.contains("}")){
                        Gdx.app.log(LABEL, "inPoolList -> on trouve un }");
                        inPoolList = false;
                    }else{
                        if(ligne != null && !ligne.equals("") && ligne.contains("_")) {
                            Gdx.app.log(LABEL, "in Politic -> on trouve une ligne non nulle. On ajoute la ligne = "+ ligne);
                            Data.selectedIAFiles.add(ligne+".txt");
                        }
                    }
                } else if (ligne.contains(Data.poolTestName)){
                    Data.selectedIAFiles = new Array<String>();
                    Gdx.app.log(LABEL, "Une ligne contient poolTestName");
                    inPoolList = true;
                }else if (ligne.contains(Data.crossingPoliticName)){
                    Gdx.app.log(LABEL, "Une ligne contient crossingPoliticName");
                    inPolitic = true;
                }

            }
            br.close();
            for(int i =selectedIAFiles.size;i<All_Players_Number;i++){
                CompileString.generateTree("x"+i+"_1");
                selectedIAFiles.add(CompileString.serializePrefix+"x"+i+"_1.txt");
            }
            if(Data.combiningMode && Data.allFilesTested.size>0){
                Data.combineRandomMobs(8);
            }
            /*
            if(fichier.delete()){
                Gdx.app.log(LABEL,"readParamFile : file successfully deleted");
            }else{
                Gdx.app.log(LABEL,"readParamFile : file not deleted, error");
            }*/
        } catch (Exception e) {
            System.out.println("Read Param exception... " + e.toString());
        }
        Gdx.app.log(LABEL,"**readParamFile ends");
    }

    /*
    Read File AI Parameter
     */
    public static void readParamAI()
    {
        Array<String> allLoadedFiles = new Array<String>();
        Scanner scanner;
        try {
            File file = Gdx.files.internal(Data.paramDir + Data.paramFileNAme).file();
            if(file.exists() && file.length() > 0)
            {
                scanner = new Scanner(file);
                while (scanner.hasNextLine())
                {
                    String line = scanner.nextLine();
                    line = line.replaceAll(" ", "");
                    if(line.contains("nbLaunchGame") && !line.substring(line.lastIndexOf(":") + 1).equals(""))
                    {
                        MAX_GAME_LOOP = Integer.parseInt(line.split(":")[1]);
                    }
                    if(line.contains("Generation") && !line.substring(line.lastIndexOf(":") + 1).equals(""))
                    {
                        Number_Generated_IA = Integer.parseInt(line.split(":")[1]);
                        // Ajout appel de la fonction de génération
                        generateXIA(Number_Generated_IA);
                        Data.setPoolsArrays();
                        break;
                    }
                    if(line.contains("Combine") && !line.substring(line.lastIndexOf(":") + 1).equals(""))
                    {
                        Number_Combine_IA = Integer.parseInt(line.split(":")[1]);
                        Data.setPoolsArrays();
                        Data.combineRandomMobs(Number_Combine_IA);
                        //Ajout appel de la fonction de combinaison
                        // combiner tous les arbres qui sont dans PoolTestee, mettre enfants dans PoolATester

                        break;
                    }
                    if(line.contains("LoadMob"))
                    {
                        //Ajout appel de la fonction load
                        // Charger tous les arbres qui sont dans PoolATester
                        // Attention à ce qu'il y ait suffisamment de mobs (si non générer x fois)
                        break;
                    }
                }
            }
            else // file does not exist
            {
                Gdx.app.log("Data.readParamAI()", "File does not exist");
            }
        }
        catch (FileNotFoundException e)
        {e.printStackTrace();}
    }

    public static void setForServer(){
        autoIA = false;
        generateIA = false;
        jvm = true;
        RUN_APIX = false;
        SCREEN_HEIGHT = 240;
        SCREEN_WIDTH = 360;
        debug = true;
    }
}
