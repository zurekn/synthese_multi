package com.mygdx.game.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by nicolas on 23/03/2016.
 */
public class Message extends Label {

    private final String LABEL = "MESSAGE";

    private int x;
    private int y;
    private String message;
    private int type;
    private Color color;
    private long startTime;
    private long duration;
    private int rotation = 0;

    public Message(CharSequence text, Skin skin) {
        super(text, skin);
    }

    /* return true if the message duration is over*/
    public boolean isEnded(float delta){
        return duration <= 0;
    }
}
