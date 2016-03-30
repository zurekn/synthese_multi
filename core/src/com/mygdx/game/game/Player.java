package com.mygdx.game.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.data.Data;
import com.mygdx.game.data.Hero;
import com.mygdx.game.data.HeroData;
import com.mygdx.game.data.SpellData;
import com.mygdx.game.data.Stats;
import com.mygdx.game.exception.IllegalCaracterClassException;

import static com.mygdx.game.data.Data.scale;

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

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.BLACK);
        if (isMyTurn())
            batch.draw(Data.IMAGE_HALO, getX() * Data.BLOCK_SIZE_X / scale + Data.MAP_X - 10 / scale, getY() * Data.BLOCK_SIZE_Y / scale + Data.MAP_Y - 10 / scale, (Data.BLOCK_SIZE_X + 20) / scale, (Data.BLOCK_SIZE_Y + 20) / scale);
        if (Data.DISPLAY_PLAYER)
            shapeRenderer.rect(Data.MAP_X + getX() * Data.BLOCK_SIZE_X / scale, Data.MAP_Y + getY() * Data.BLOCK_SIZE_Y / scale,
                    Data.BLOCK_SIZE_X / scale, Data.BLOCK_SIZE_Y / scale);

    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int n) {
        this.number = n;
    }
}
