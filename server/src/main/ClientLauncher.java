package main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.data.Data;

public class ClientLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = Data.TITLE;
		config.height = Data.SCREEN_HEIGHT;
		config.width = Data.SCREEN_WIDTH;

        Data.checkValuesIni("paramTI.ini");

        Data.RUN_APIX = false;
        Data.debug = true;

        new LwjglApplication(new WindowClient(), config);
	}
}
