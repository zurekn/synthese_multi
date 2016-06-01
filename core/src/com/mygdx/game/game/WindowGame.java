package com.mygdx.game.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

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
 * Main class which handle the game Contain the init method for creating all
 * game objects
 *
 * @author bob
 */
public class WindowGame extends Game {

    @Override
    public void create(){
        Data.createDirectoryIA();
        //Data.generateXIA(Data.Number_Generated_IA);
        Data.readParamAI();
//        Gdx.app.log("WndowGame", "** On a read le fichier de param. Best mob rate =" + Data.bestMobRate + ", Worst Mob Rate =" + Data.worstMobRate + ", Cross Mob Rate=" + Data.crossMobRate);
        //this.setScreen(new GameScreen(this));
    }

}
