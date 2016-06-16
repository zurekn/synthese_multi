package com.mygdx.game.com;

import com.badlogic.gdx.Game;
import com.mygdx.game.data.Data;

/**
 * Created by gregory on 30/03/16.
 */
    public class WindowServer extends Game {

        @Override
        public void create(){
            Data.createDirectoryIA();
            Data.setPoolsArrays();

            this.setScreen(new ServerScreen());
        }

    }


