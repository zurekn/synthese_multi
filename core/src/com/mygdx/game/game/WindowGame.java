package com.mygdx.game.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.data.Data;

public class WindowGame extends Game {

    @Override
    public void create(){
        Data.createDirectoryIA();
        if(!Data.singlePlayer)
            Data.readParamAI();
        Data.setPoolsArrays();
        this.setScreen(new GameScreen(this));
    }

}
