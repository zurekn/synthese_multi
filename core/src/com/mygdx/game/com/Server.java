package com.mygdx.game.com;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.data.Data;

public class Server {
    public Server(String id){
        this(id, Data.TITLE);
    }

    public Server(String id, String title) {
        Data.loadProperties();

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = title;
        Data.PLAYER_MESSAGE_X_POS = 10;
        Data.PLAYER_MESSAGE_Y_POS = 10;

        Data.setForServer();

        config.height = Data.SCREEN_HEIGHT;
        config.width = Data.SCREEN_WIDTH;

        Data.checkValuesIni("paramTI.ini");

        //try {
            new LwjglApplication(new WindowServer(id), config);
        //}
    }
}
