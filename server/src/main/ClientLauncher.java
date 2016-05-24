package main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.data.Data;

public class ClientLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        Data.setScreenSize();

		config.title = Data.TITLE;
		config.height = Data.SCREEN_HEIGHT;
		config.width = Data.SCREEN_WIDTH;

        Data.checkValuesIni("paramTI.ini");


        Data.debug = true;

        Data.setForAndroid(false);

        new LwjglApplication(new WindowClient(), config);
	}
}
