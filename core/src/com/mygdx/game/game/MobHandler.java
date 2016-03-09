package com.mygdx.game.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class MobHandler {

	private ArrayList<Mob> mobs;
	
	public MobHandler(ArrayList<Mob> mobs){
		this.mobs = mobs;
	}
	
	public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
		for(int i = 0; i < mobs.size(); i++){
			mobs.get(i).render(batch, shapeRenderer);
		}
	}

}
