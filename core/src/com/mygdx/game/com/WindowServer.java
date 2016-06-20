package com.mygdx.game.com;

import com.badlogic.gdx.Game;
import com.mygdx.game.data.Data;

public class WindowServer extends Game {

    @Override
    public void create() {
        Data.createDirectoryIA();
        Data.setPoolsArrays();

        this.setScreen(new ServerScreen());
    }

}