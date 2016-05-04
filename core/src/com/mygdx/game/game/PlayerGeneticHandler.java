package com.mygdx.game.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.data.Data;
import com.mygdx.game.data.Stats;
import com.mygdx.game.game.PlayerGenetic;

public class PlayerGeneticHandler {

	private ArrayList<PlayerGenetic> mobs;
	
	
	public PlayerGeneticHandler(ArrayList<PlayerGenetic> mobs){
		this.mobs = mobs;
	}
	
	public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
		for(int i = 0; i < mobs.size(); i++){
			mobs.get(i).render(batch, shapeRenderer);
		}
	}

	public void renderMobStat(SpriteBatch batch, ShapeRenderer shapeRenderer) {
		// TODO Auto-generated method stub
		if(!Data.MOB_LIFE_SHOW)
			return;
		float lifeRate = 0;
		for(int i = 0; i < mobs.size(); i++){
			Stats s = mobs.get(i).getStats();
				//LIFE BAR
				shapeRenderer.setColor(Color.RED);
				lifeRate= ((float)s.getLife() / (float)s.getMaxLife());
				if(lifeRate < 0 )
					lifeRate = 0;
				shapeRenderer.rect(Data.MAP_X + mobs.get(i).getX() * Data.BLOCK_SIZE_X, Data.MAP_Y + mobs.get(i).getY() * Data.BLOCK_SIZE_Y - 10, lifeRate * Data.MOB_LIFE_RECT_X_SIZE, Data.MOB_LIFE_RECT_Y_SIZE);
				//LIFE BAR
				shapeRenderer.rect(Data.MAP_X + mobs.get(i).getX() * Data.BLOCK_SIZE_X, Data.MAP_Y + mobs.get(i).getY() * Data.BLOCK_SIZE_Y - 10, Data.MOB_LIFE_RECT_X_SIZE, Data.MOB_LIFE_RECT_Y_SIZE);
		}
	}

}
