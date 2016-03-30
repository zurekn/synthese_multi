package com.mygdx.game.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.data.Data;

public class Message extends Label{

    private final String TAG = "MESSAGE";

	private String message;
	private int type;
	private long startTime;
	private long duration;
	private int rotation = 0;

	public Message(String string) {
        super(string, Data.SKIN);
        message = string;
		type = 0;
		init();
	}
	
	public Message(String s, int type){
        super(s, Data.SKIN);
        message = s;
		this.type = type;
		init();
	}

	private void init(){
		duration = Data.getDurationMessage(type);
		this.setColor(Data.getColorMessage(type));
		startTime = System.currentTimeMillis();
	}

	public void setRotation(int rotat){
		rotation = rotat;
	}
	
	public float getRotation(){
		return rotation;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void render(Batch batch, float x, float y) {
        setPosition(x, y);
		this.draw(batch, 1);
        //Gdx.app.log(TAG, "Render message "+message+" at position "+getX()+"/"+getY()+", "+this.getColor());
    }

	public boolean update() {
		return System.currentTimeMillis() - startTime > duration;
	}

	public int getType() { 
		return type;
	}

	@Override
	public String toString() {
		return "Message [message=" + message + ", type=" + type + "]";
	}

	
}
