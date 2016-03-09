package com.mygdx.game.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.data.Data;
import com.mygdx.game.data.Hero;
import com.mygdx.game.data.HeroData;
import com.mygdx.game.data.SpellData;
import com.mygdx.game.data.Stats;
import com.mygdx.game.exception.IllegalCaracterClassException;

public class Player extends Character {

    private Texture icon;
    private int number;

    /**
     * This constructor is not available
     *
     * @param x
     * @param y
     * @param id
     * @param stats
     * @deprecated
     */
    public Player(int x, int y, String id, Stats stats) {
        this.setX(x);
        this.setY(y);
        this.setId(id);
        this.setStats(stats);
        this.setSpells(SpellData.getSpellForClass(this.getStats().getCharacterClass()));
        init();

        if (Data.debug) {
            System.out.println("Debug : Player " + getId() + " created");
        }
    }

    @SuppressWarnings("unused")
    public Player(int x, int y, String id, String caracterClass) throws IllegalCaracterClassException {
        monster = false;
        this.setX(x);
        this.setY(y);
        this.setId(id);
        this.setAiType("player");
        this.setTrueID(id);
        this.setName(id);
        this.setNpc(false);
        Hero h = HeroData.getHeroByClass(caracterClass);
        icon = h.getIcon();
        if (h == null) {
            throw (new IllegalCaracterClassException(caracterClass + "Doesn't exist in hero.xml"));
        }

        this.setStats(h.getStat().clone());
        this.setSpells(h.getSpells());

        if (Data.debug) {
            System.out.println("Debug : Player " + this.toString() + " created");
        }
    }

    public void init() {

    }

    public Texture getIcon() {
        return icon;
    }

    public void render(Batch batch, ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        if (isMyTurn())
            batch.draw(Data.IMAGE_HALO, getX() * Data.BLOCK_SIZE_X + Data.MAP_X - 10, getY() * Data.BLOCK_SIZE_Y + Data.MAP_Y - 10, Data.BLOCK_SIZE_X + 20, Data.BLOCK_SIZE_Y + 20);
        if (Data.DISPLAY_PLAYER)
            shapeRenderer.rect(Data.MAP_X + getX() * Data.BLOCK_SIZE_X, Data.MAP_Y + getY() * Data.BLOCK_SIZE_Y,
                    Data.BLOCK_SIZE_X, Data.BLOCK_SIZE_Y);

//			int posX = Data.MAP_X + getX() * Data.BLOCK_SIZE_X + Data.BLOCK_SIZE_X / 2 - getStats().getMovementPoints() * Data.BLOCK_SIZE_X - Data.BLOCK_SIZE_X / 2;
//			int posY = Data.MAP_Y + getY() * Data.BLOCK_SIZE_Y + Data.BLOCK_SIZE_Y / 2 - getStats().getMovementPoints() * Data.BLOCK_SIZE_Y - Data.BLOCK_SIZE_Y / 2;
//			int sizeX = 2 * getStats().getMovementPoints() * Data.BLOCK_SIZE_X + Data.BLOCK_SIZE_X ;
//			int sizeY = 2 * getStats().getMovementPoints() * Data.BLOCK_SIZE_Y + Data.BLOCK_SIZE_Y ;
//			g.drawOval(posX, posY, sizeX, sizeY);

    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int n) {
        this.number = n;
    }
}
