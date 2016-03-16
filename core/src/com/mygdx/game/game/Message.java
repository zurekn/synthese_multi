package com.mygdx.game.game;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.data.Data;

public class Message {

    private final String LABEL = "MESSAGE";

	private int x;
	private int y;
	private String message;
	private int type;
	private Color color;
	private long startTime;
	private long duration;
	private int rotation = 0;
	
	public Message(int x, int y, String message) {
		super();
		this.x = x;
		this.y = y;
		this.message = message;
		type = 0;
		init();
	}

	public Message(String string) {
		message = string;
		type = 0;
		init();
	}
	
	public Message(String s, int type){
		message = s;
		this.type = type;
		init();
	}

	private void init(){
		duration = Data.getDurationMessage(type);
		color = Data.getColorMessage(type);
		startTime = System.currentTimeMillis();
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setRotation(int rotat){
		rotation = rotat;
	}
	
	public int getRotation(){
		return rotation;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void render(Batch batch, float x, float y) {
		//g.setColor(color);
		Data.font.setColor(color);
		Data.font.draw(batch, message, x, y);
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
