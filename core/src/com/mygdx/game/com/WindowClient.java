package com.mygdx.game.com;

import com.badlogic.gdx.Game;

/**
 * Created by gregory on 30/03/16.
 */
    public class WindowClient extends Game {

        @Override
        public void create(){
            this.setScreen(new ClientScreen(this));
        }

    }


