package com.mygdx.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.data.*;

public class Mob extends Character {
    private static final String TAG = "MOB";
	private float elapsedTime;

	public Mob(int x, int y, String id, String trueID) {
		this.setX(x);
		this.setY(y);
		this.setId(id);
		this.setTrueID(trueID);

		init();

		if (Data.debug) {
			System.out.println("Debug : Mob created : " + toStringAll());
		}
	}


	public void init() {
		Monster m = MonsterData.getMonsterById(this.getId());
		this.setAnimationFrames(m.getAnimationFrames());
		//TODO check animation time
		this.setAnimation(new Animation(1f / 4f, getAnimationFrames()));
		this.setStats(m.getStats());
		this.setName(m.getName());
		this.setSpells(m.getSpells());
		this.setAiType(m.getAiType());

	}

	public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
		TextureRegion[] animationFrames = this.getAnimationFrames();
		int x = this.getX();
		int y = this.getY();
		// g.drawRect(getX() * Data.BLOCK_SIZE_X, getY() * Data.BLOCK_SIZE_Y,
		// Data.BLOCK_SIZE_X, Data.BLOCK_SIZE_Y);
        TextureRegion reg = getAnimation().getKeyFrame(elapsedTime, true);
		if (isMyTurn()) {
            batch.draw(Data.IMAGE_HALO, getX() * Data.BLOCK_SIZE_X + Data.MAP_X - 10, getY() * Data.BLOCK_SIZE_Y + Data.MAP_Y - 10, Data.BLOCK_SIZE_X + 20, Data.BLOCK_SIZE_Y + 20);
        }
		elapsedTime+= Gdx.graphics.getDeltaTime();

		batch.draw(getAnimation().getKeyFrame(elapsedTime, true), Data.MAP_X + getX() * Data.BLOCK_SIZE_X / Data.scale, Data.MAP_Y + getY() * Data.BLOCK_SIZE_Y / Data.scale);


        shapeRenderer.setColor(Color.MAGENTA);
		shapeRenderer.rect(100, 100, 100, 100);
		/*animation[6].draw(Data.MAP_X + x * Data.BLOCK_SIZE_X, Data.MAP_Y + y
				* Data.BLOCK_SIZE_Y, Data.BLOCK_SIZE_X, Data.BLOCK_SIZE_Y);*/
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
		}
		if(Data.debug){
			if(getFocusedOn()!=null)
				shapeRenderer.line(Data.MAP_X + x * Data.BLOCK_SIZE_X+ Data.BLOCK_SIZE_X / 2, Data.MAP_Y + y
						* Data.BLOCK_SIZE_Y+ Data.BLOCK_SIZE_X / 2, Data.MAP_X + getFocusedOn().getX() * Data.BLOCK_SIZE_X + Data.BLOCK_SIZE_X / 2, Data.MAP_Y + getFocusedOn().getY()
						* Data.BLOCK_SIZE_Y+ Data.BLOCK_SIZE_X / 2);
		}*/

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
