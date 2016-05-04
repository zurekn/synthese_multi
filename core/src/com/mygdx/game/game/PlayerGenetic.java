package com.mygdx.game.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.data.Data;
import com.mygdx.game.data.Hero;
import com.mygdx.game.data.HeroData;
import com.mygdx.game.data.Monster;
import com.mygdx.game.data.MonsterData;
import com.mygdx.game.data.SpellData;
import com.mygdx.game.data.Stats;
import com.mygdx.game.exception.IllegalCaracterClassException;

import static com.mygdx.game.data.Data.scale;


public class PlayerGenetic extends Character {
	private float elapsedTime;

	public PlayerGenetic(int x, int y, String id, String trueID) {
		this.setX(x);
		this.setY(y);
		this.setId(id);
		this.setTrueID(trueID);
		init();

		if (Data.debug) {
			System.out.println("Debug : Player Genetic created : " + toStringAll());
		}
	}

	public void init(){
		monster=false;
		Monster m = MonsterData.getMonsterById(this.getId());
		this.initAnimation(m.getAnimationFrames(), 1f / 4f);
		this.setStats(m.getStats());
		this.setName(m.getName());
		this.setSpells(m.getSpells());
		this.setAiType(m.getAiType());
		if(Data.generateIA)
			this.generateScriptGenetic();
		this.compileScriptGenetic();
		this.setFitness(new IAFitness(true));
	}

	public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
		TextureRegion reg = getAnimation(direction).getKeyFrame(elapsedTime, true);
		int x = this.getX();
		int y = this.getY();
		if (isMyTurn())
			batch.draw(Data.IMAGE_HALO, getX() * Data.BLOCK_SIZE_X / scale + Data.MAP_X - 10 / scale, getY() * Data.BLOCK_SIZE_Y / scale + Data.MAP_Y - 10 / scale, (Data.BLOCK_SIZE_X + 20) / scale, (Data.BLOCK_SIZE_Y + 20) / scale);
		/*if (isMyTurn()) {
			int posX = Data.MAP_X + getX() * Data.BLOCK_SIZE_X
					+ Data.BLOCK_SIZE_X / 2 - getStats().getMovementPoints()
					* Data.BLOCK_SIZE_X - Data.BLOCK_SIZE_X / 2;
			int posY = Data.MAP_Y + getY() * Data.BLOCK_SIZE_Y
					+ Data.BLOCK_SIZE_Y / 2 - getStats().getMovementPoints()
					* Data.BLOCK_SIZE_Y - Data.BLOCK_SIZE_Y / 2;
			int sizeX = 2 * getStats().getMovementPoints() * Data.BLOCK_SIZE_X
					+ Data.BLOCK_SIZE_X;
			int sizeY = 2 * getStats().getMovementPoints() * Data.BLOCK_SIZE_Y
					+ Data.BLOCK_SIZE_Y;
			g.drawOval(posX, posY, sizeX, sizeY);
		}*/
		if(Data.debug){
			if(getFocusedOn()!=null)
				shapeRenderer.line(Data.MAP_X + x * Data.BLOCK_SIZE_X + Data.BLOCK_SIZE_X / 2, Data.MAP_Y + y
						* Data.BLOCK_SIZE_Y + Data.BLOCK_SIZE_X / 2, Data.MAP_X + getFocusedOn().getX() * Data.BLOCK_SIZE_X + Data.BLOCK_SIZE_X / 2, Data.MAP_Y + getFocusedOn().getY()
						* Data.BLOCK_SIZE_Y + Data.BLOCK_SIZE_X / 2);
		}

	}

	@Override
	public String toString() {
		return "Mob [name=" + getName() + ", x=" + getX() + ", y=" + getY()
				+ ", id=" + getId() + "]";
	}

	public String toStringAll() {
		return "Mob [name=" + getName() + ", x=" + getX() + ", y=" + getY()
				+ ", id=" + getId() + ", " + getStats().toString()
				+ ", Spells " + getSpells().toString() + "]";
	}
}
