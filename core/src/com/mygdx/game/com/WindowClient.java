package com.mygdx.game.com;

import com.badlogic.gdx.Game;

    public class WindowClient extends Game {

        @Override
        public void create(){
            this.setScreen(new ClientScreen(this));
        }

    }


