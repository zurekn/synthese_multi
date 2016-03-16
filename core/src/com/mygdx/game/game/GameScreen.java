package com.mygdx.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.data.Data;

/**
 * Created by nicolas on 15/03/2016.
 */
public class GameScreen implements Screen {

    private GameStage stage;

    private WindowGame game;

    public GameScreen(final WindowGame game){
        this.game = game;
        stage = new GameStage();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw(delta);
    }

    @Override
    public void resize(int width, int height) {
        Data.SCREEN_WIDTH = width;
        Data.SCREEN_HEIGHT = height;
        stage.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
