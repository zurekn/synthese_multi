package main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.data.Data;

import game.ServerGame;

public class ServerLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = Data.TITLE;
		Data.SCREEN_HEIGHT = 240;
		Data.SCREEN_WIDTH = 360;
		Data.PLAYER_MESSAGE_X_POS = 10;
		Data.PLAYER_MESSAGE_Y_POS = 10;

		config.height = Data.SCREEN_HEIGHT;
		config.width = Data.SCREEN_WIDTH;

        Data.checkValuesIni("paramTI.ini");

        Data.RUN_APIX = false;
        Data.debug = true;

        new LwjglApplication(new WindowServer(), config);
	}
}
