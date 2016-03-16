package com.mygdx.game.game;

import java.awt.Shape;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.data.Data;
import com.mygdx.game.data.Stats;

public class PlayerHandler {
	private ArrayList<Player> player ;
	
	public PlayerHandler(ArrayList<Player> player2){
		this.player = player2;
		
	}

public void renderPlayerStat(SpriteBatch batch, ShapeRenderer shapeRenderer){

		for(int i = 0; i < player.size(); i++){
			Stats s = player.get(i).getStats();
			switch(player.get(i).getNumber()){
			case 0: //bottom
				//LIFE BAR*
				shapeRenderer.setColor(Color.GREEN);
				shapeRenderer.rect(Data.PLAYER_LIFE_RECT_X_POS + Data.MAP_X, Data.PLAYER_LIFE_RECT_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT, ((float) s.getLife() / (float) s.getMaxLife()) * Data.PLAYER_LIFE_RECT_X_SIZE, Data.PLAYER_LIFE_RECT_Y_SIZE);
				//MANA BAR
				shapeRenderer.setColor(Color.BLUE);

				shapeRenderer.rect(Data.PLAYER_MANA_RECT_X_POS + Data.MAP_X, Data.PLAYER_MANA_RECT_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT, ((float) s.getMana() / (float) s.getMaxMana()) * Data.PLAYER_MANA_RECT_X_SIZE, Data.PLAYER_MANA_RECT_Y_SIZE);

				shapeRenderer.setColor(Color.BLACK);
				//LIFE BAR
				shapeRenderer.rect(Data.PLAYER_LIFE_RECT_X_POS + Data.MAP_X, Data.PLAYER_LIFE_RECT_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT, Data.PLAYER_LIFE_RECT_X_SIZE, Data.PLAYER_LIFE_RECT_Y_SIZE);
				//MANA BAR
				shapeRenderer.rect(Data.PLAYER_MANA_RECT_X_POS + Data.MAP_X, Data.PLAYER_MANA_RECT_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT, Data.PLAYER_MANA_RECT_X_SIZE, Data.PLAYER_MANA_RECT_Y_SIZE);
				batch.draw(player.get(i).getIcon(), Data.MAP_X + Data.PLAYER_ICON_X_POS, Data.PLAYER_ICON_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT);
				break;
			case 1: //left

                shapeRenderer.setColor(Color.BLACK);
                //LIFE BAR
                shapeRenderer.rect(Data.MAP_X - Data.PLAYER_LIFE_RECT_Y_POS - Data.PLAYER_LIFE_RECT_Y_SIZE, Data.MAP_Y + Data.PLAYER_LIFE_RECT_X_POS, Data.PLAYER_LIFE_RECT_Y_SIZE, Data.PLAYER_LIFE_RECT_X_SIZE);

                //MANA BAR
                shapeRenderer.rect(Data.MAP_X - Data.PLAYER_MANA_RECT_Y_POS - Data.PLAYER_MANA_RECT_Y_SIZE, Data.MAP_Y + Data.PLAYER_MANA_RECT_X_POS, Data.PLAYER_MANA_RECT_Y_SIZE, Data.PLAYER_MANA_RECT_X_SIZE);

                //LIFE BAR*
				shapeRenderer.setColor(Color.GREEN);
                shapeRenderer.rect(Data.MAP_X - Data.PLAYER_LIFE_RECT_Y_POS - Data.PLAYER_LIFE_RECT_Y_SIZE, Data.MAP_Y + Data.PLAYER_LIFE_RECT_X_POS, Data.PLAYER_LIFE_RECT_Y_SIZE, ((float) s.getLife() / (float) s.getMaxLife()) * Data.PLAYER_LIFE_RECT_X_SIZE);

                //MANA BAR
				shapeRenderer.setColor(Color.BLUE);
                shapeRenderer.rect(Data.MAP_X - Data.PLAYER_MANA_RECT_Y_POS - Data.PLAYER_MANA_RECT_Y_SIZE, Data.MAP_Y + Data.PLAYER_MANA_RECT_X_POS, Data.PLAYER_MANA_RECT_Y_SIZE, ((float) s.getMana() / (float) s.getMaxMana()) * Data.PLAYER_MANA_RECT_X_SIZE);

				batch.draw(player.get(i).getIcon(), Data.MAP_X - Data.PLAYER_ICON_Y_POS, Data.MAP_Y - Data.PLAYER_ICON_X_POS, player.get(i).getIcon().getWidth() / 2, player.get(i).getIcon().getHeight() / 2, player.get(i).getIcon().getWidth(), player.get(i).getIcon().getHeight(), 1f, 1f, -90f, 0, 0, 0, 0, false, false);

				break;
			case 2: //TOP

                shapeRenderer.setColor(Color.BLACK);
                //LIFE BAR
                shapeRenderer.rect(Data.MAP_X + Data.MAP_WIDTH - Data.PLAYER_LIFE_RECT_X_POS, Data.MAP_Y - Data.PLAYER_LIFE_RECT_Y_POS, Data.PLAYER_LIFE_RECT_X_SIZE, -Data.PLAYER_LIFE_RECT_Y_SIZE);
                //MANA BAR
                shapeRenderer.rect(Data.MAP_X + Data.MAP_WIDTH - Data.PLAYER_MANA_RECT_X_POS, Data.MAP_Y - Data.PLAYER_MANA_RECT_Y_POS, Data.PLAYER_MANA_RECT_X_SIZE, -Data.PLAYER_MANA_RECT_Y_SIZE);

                //LIFE BAR*
                shapeRenderer.setColor(Color.GREEN);
				shapeRenderer.rect(Data.MAP_X + Data.MAP_WIDTH - Data.PLAYER_LIFE_RECT_X_POS, Data.MAP_Y - Data.PLAYER_LIFE_RECT_Y_POS, -((float) s.getLife() / (float) s.getMaxLife()) * Data.PLAYER_LIFE_RECT_X_SIZE, -Data.PLAYER_LIFE_RECT_Y_SIZE);
				//MANA BAR
                shapeRenderer.setColor(Color.BLUE);
                shapeRenderer.rect(Data.MAP_X + Data.MAP_WIDTH - Data.PLAYER_MANA_RECT_X_POS, Data.MAP_Y - Data.PLAYER_MANA_RECT_Y_POS, -((float) s.getMana() / (float) s.getMaxMana()) * Data.PLAYER_MANA_RECT_X_SIZE, -Data.PLAYER_MANA_RECT_Y_SIZE);

                batch.draw(player.get(i).getIcon(), Data.MAP_X + Data.MAP_WIDTH - Data.PLAYER_ICON_Y_POS, Data.MAP_Y - Data.PLAYER_ICON_X_POS, player.get(i).getIcon().getWidth() / 2, player.get(i).getIcon().getHeight() / 2, player.get(i).getIcon().getWidth(), player.get(i).getIcon().getHeight(), 1f, 1f, 180f, 0, 0, 0, 0, false, false);


                break;
			case 3: //right

                shapeRenderer.setColor(Color.BLACK);
                //LIFE BAR
                shapeRenderer.rect(Data.MAP_X + Data.MAP_WIDTH + Data.PLAYER_LIFE_RECT_Y_POS, Data.MAP_Y + Data.MAP_HEIGHT + Data.PLAYER_LIFE_RECT_X_POS, Data.PLAYER_LIFE_RECT_Y_SIZE, Data.PLAYER_LIFE_RECT_X_SIZE );
                //MANA BAR
                shapeRenderer.rect(Data.MAP_X + Data.MAP_WIDTH + Data.PLAYER_MANA_RECT_Y_POS, Data.MAP_Y + Data.MAP_HEIGHT + Data.PLAYER_MANA_RECT_X_POS, Data.PLAYER_MANA_RECT_Y_SIZE, Data.PLAYER_MANA_RECT_X_SIZE );

                //LIFE BAR*
                shapeRenderer.setColor(Color.GREEN);
                shapeRenderer.rect(Data.MAP_X + Data.MAP_WIDTH + Data.PLAYER_LIFE_RECT_Y_POS, Data.MAP_Y + Data.MAP_HEIGHT + Data.PLAYER_LIFE_RECT_X_POS, Data.PLAYER_LIFE_RECT_Y_SIZE, Data.PLAYER_LIFE_RECT_X_SIZE * ((float) s.getLife() / (float) s.getMaxLife()));
				//MANA BAR
                shapeRenderer.setColor(Color.BLUE);
                shapeRenderer.rect(Data.MAP_X + Data.MAP_WIDTH + Data.PLAYER_MANA_RECT_Y_POS, Data.MAP_Y + Data.MAP_HEIGHT + Data.PLAYER_MANA_RECT_X_POS, Data.PLAYER_MANA_RECT_Y_SIZE, Data.PLAYER_MANA_RECT_X_SIZE * ((float) s.getMana() / (float) s.getMaxMana()));


                batch.draw(player.get(i).getIcon(), Data.MAP_X + Data.MAP_WIDTH + Data.PLAYER_ICON_Y_POS, Data.MAP_Y  + Data.MAP_HEIGHT- Data.PLAYER_ICON_X_POS, player.get(i).getIcon().getWidth() / 2, player.get(i).getIcon().getHeight() / 2, player.get(i).getIcon().getWidth(), player.get(i).getIcon().getHeight(), 1f, 1f, 180f, 0, 0, 0, 0, false, false);


                break;
			}
			
		}
	}

	public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
		for(int i = 0; i < player.size(); i++)
			player.get(i).render(batch, shapeRenderer);
	}

	public void renderInitBlock(SpriteBatch batch, ShapeRenderer shapeRenderer) {
		String []split;
		String var;
		boolean used;
		int x, y;
		Set<String> set = Data.departureBlocks.keySet();
		Iterator<String> it = set.iterator();

		shapeRenderer.setColor(Data.BLOCK_REACHABLE_COLOR);
		//g.setColor(Data.BLOCK_REACHABLE_COLOR);
        Vector3 v = GameStage.gameStage.getCamera().position;
        float scale = GameStage.gameStage.getCamera().zoom;
		while(it.hasNext()){
			var = ((String) it.next());
			if(!Data.departureBlocks.get(var)){
				split = var.split(":");
				x = Integer.parseInt(split[0]);
				y = Integer.parseInt(split[1]);
				shapeRenderer.rect(Data.MAP_X + x * Data.BLOCK_SIZE_X, Data.MAP_Y + y * Data.BLOCK_SIZE_Y, Data.BLOCK_SIZE_X / scale,  Data.BLOCK_SIZE_Y / scale);

            }
		}
	}
	
	
}
