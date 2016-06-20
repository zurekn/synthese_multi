package com.mygdx.game.com;

import com.badlogic.gdx.Game;
import com.mygdx.game.data.Data;

public class WindowClient extends Game {

    @Override
    public void create(){
        Data.createDirectoryIA();
        if(!Data.singlePlayer)
            Data.readParamAI();
        Data.setPoolsArrays();
        this.setScreen(new ClientScreen(this));
    }

}


