package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Game;
import com.mygdx.game.data.Data;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = Data.TITLE;
		config.height = Data.SCREEN_HEIGHT;
		config.width = Data.SCREEN_WIDTH;
		new LwjglApplication(new Game(), config);
	}
}
