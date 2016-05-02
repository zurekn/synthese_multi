package com.mygdx.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.mygdx.game.data.Data;

/**
 * Create a bar which can be display on the screen
 */
public class Bar extends Actor {

    private final NinePatchDrawable loadingBar;
    private final NinePatchDrawable loadingBarBackground;

    private Color backgroundColor;
    private float max;
    private float progress = 1.0f;
    private float current;

    public Bar(Color color, int x, int y, int width, int height, int max, Color backgroundColor){
        this.backgroundColor = color;
        this.backgroundColor = backgroundColor;
        this.max = max;
        this.current = max;
        this.setBounds(x, y, width, height);
        NinePatch loadingBarBackgroundPatch = new NinePatch(Data.ATLAS.findRegion("default-round"), 5, 5, 4, 4);
        NinePatch loadingBarPatch = new NinePatch(Data.ATLAS.findRegion("default-round-down"), 5, 5, 4, 4);
        loadingBar = new NinePatchDrawable(loadingBarPatch).tint(color);
        if(backgroundColor != null)
            loadingBarBackground = new NinePatchDrawable(loadingBarBackgroundPatch).tint(backgroundColor);
        else
            loadingBarBackground = new NinePatchDrawable(loadingBarBackgroundPatch);

    }

    public void setCurrent(int n){
        this.current = n;
        if(current > max)
            current = max;

        progress = current / max;
    }

    public void setMax(int max){
        this.max = max;
        if( current > max)
            current = max;
        progress = current / max;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        loadingBarBackground.draw(batch, getX(), getY(), getWidth() * getScaleX(), getHeight() * getScaleY());
        loadingBar.draw(batch, getX(), getY(), progress * getWidth() * getScaleX(), getHeight() * getScaleY());
    }
}
