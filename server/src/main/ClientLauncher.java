package main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.com.WindowClient;
import com.mygdx.game.data.Data;

public class ClientLauncher {
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

		Data.setForAndroid(false);

        new LwjglApplication(new WindowClient(), config);
	}
}
