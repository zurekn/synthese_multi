package com.mygdx.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
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
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        GestureDetector detector = new GestureDetector(new InputHandler());
        if(Data.ANDROID)
            inputMultiplexer.addProcessor(detector);
        else
            inputMultiplexer.addProcessor(new InputHandler());
        Gdx.input.setInputProcessor(inputMultiplexer);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.customAct(delta);
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
