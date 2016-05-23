package main;

import com.badlogic.gdx.Game;

/**
 * Created by gregory on 30/03/16.
 */
    public class WindowServer extends Game {

        @Override
        public void create(){
            this.setScreen(new ServerScreen());
        }

    }


