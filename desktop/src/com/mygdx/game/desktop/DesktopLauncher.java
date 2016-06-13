package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Game;
import com.mygdx.game.data.Data;
import com.mygdx.game.game.WindowGame;

import java.awt.Dimension;
import java.awt.Toolkit;

public class DesktopLauncher {
	public static void main (String[] arg) {
        Data.loadProperties();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        Data.setScreenSize();

		config.title = Data.TITLE;
		config.height = Data.SCREEN_HEIGHT;
		config.width = Data.SCREEN_WIDTH;
        config.fullscreen = false;
        config.resizable = false;
        Data.checkValuesIni("paramTI.ini");

        Data.RUN_APIX = false;
        Data.debug = true;

        //Data.setForAndroid(false);
        if(Data.FORCE_HADOOP) {
            Data.initHadoop();
        }
        new LwjglApplication(new WindowGame(), config);
	}
}
