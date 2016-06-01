package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
import com.mygdx.game.data.Data;

import game.ClientGame;
import game.ClientInputHandler;

/**
 * Created by gregory on 30/03/16.
 */
public class ClientScreen implements Screen {

        private ClientGame stage;

        private WindowClient game;

        public ClientScreen(final WindowClient game){
            this.game = game;
            stage = new ClientGame();
        }

        @Override
        public void show() {
            InputMultiplexer inputMultiplexer = new InputMultiplexer();
            inputMultiplexer.addProcessor(stage);
            GestureDetector detector = new GestureDetector(new ClientInputHandler());
            if(Data.ANDROID)
                inputMultiplexer.addProcessor(detector);
            else
                inputMultiplexer.addProcessor(new ClientInputHandler());
            Gdx.input.setInputProcessor(inputMultiplexer);
        }

        @Override
        public void render(float delta) {
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            //try{
            //    stage.client.sendToServer("coucou");
            //}catch(Exception e){
            //    System.err.println(e.getMessage());
            //}

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


