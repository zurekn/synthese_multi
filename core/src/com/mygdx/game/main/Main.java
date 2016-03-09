package com.mygdx.game.main;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import com.mygdx.game.ai.AStar;
import com.mygdx.game.data.Data;
import com.mygdx.game.game.Mob;
import com.mygdx.game.game.WindowGame;

public class Main {
	
	public static void main(String[] args) {
        
		//Load the screen size
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		Data.SCREEN_WIDTH = gd.getDisplayMode().getWidth();
		Data.SCREEN_HEIGHT = gd.getDisplayMode().getHeight();
	
		Data.SCREEN_WIDTH = 1920;
		Data.SCREEN_HEIGHT = 1080;
		Data.checkValuesIni("paramTI.ini"); // V�rification des variables dans le fichier .ini
       // AppGameContainer gameContaineur =  new AppGameContainer(WindowGame.getInstance(), Data.SCREEN_WIDTH, Data.SCREEN_HEIGHT, Data.FULLSCREEN);
    	//gameContaineur.setTargetFrameRate(30);
    	//gameContaineur.start();
    	
	}

}
