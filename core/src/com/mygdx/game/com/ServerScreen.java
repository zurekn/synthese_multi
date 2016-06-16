package com.mygdx.game.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.data.Data;

import com.mygdx.game.com.ServerGame;

import java.util.UUID;

public class ServerScreen implements Screen {

    private ServerGame game;
    int  i=0;

    public ServerScreen() {
        String id = UUID.randomUUID().toString();
        game = new ServerGame(id);
        ServerNode.getInstance().getMapGames().put(id,game);
    }

    @Override
    public void show() {
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(game);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta) {
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            //Gdx.gl.glClearColor(1,1,1,1);

            game.act(delta);
            game.draw();
        //System.out.println("Wait for message");
        //game.getLogHandler().addMessage("Wait for message"+i++);
        //try {
        //  String mess = game.server.receive();
        //  game.getLogHandler().addMessage("Received : "+ mess);
        //  game.server.respondToLastClient(mess);
        //  game.getLogHandler().addMessage("Sending : "+ mess);
        //}catch(NullPointerException e){
        //   System.err.println("NPE");
        //}catch(GdxRuntimeException e){
        //   Gdx.app.error("Server",e.getMessage(),e);
        //}
    }

    @Override
    public void resize(int width, int height) {
        Data.SCREEN_WIDTH = width;
        Data.SCREEN_HEIGHT = height;
        game.resize(width, height);
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


