package com.mygdx.game.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.Game;
import com.mygdx.game.data.Data;
import com.mygdx.game.game.WindowGame;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        Data.checkValuesIni("paramTI.ini");

        Data.singlePlayer = true;

        Data.RUN_APIX = false;

        Data.debug = true;

        Data.ANDROID = true;

		initialize(new WindowGame(), config);
	}
}
