package com.mygdx.game.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class Event {
    private static final String TAG = "Event";

	private String id;

	private Animation animationFrames;
    private float elapsedTime;

	private Sound sound;
	private float x;
	private float y;
	private int direction = Data.NORTH;
	private int duration = Data.INF;
	private float range;
	private float xRelative;
	private float yRelative;
	private boolean playSound = false;
	private boolean showFirstFrame = false;
	private boolean finalFrame = false;
	private boolean mobile = false;
	private boolean neeDelete = false;
	private int countShowFirst = 0;
	private int countShowFinal = 0;
	private int damage;
	private int heal;
	private String type;
	private int spriteDirection;
	private float speed;
    private float rangeRemaining;

    public Event(String id) {
		this.id = id;
		String type = id.substring(0, 1);
		if (type.equalsIgnoreCase("S")) {// Spells
			this.animationFrames = SpellData.getAnimationFramesById(id);
		} else if (type.equalsIgnoreCase("T")) { // Traps
			this.animationFrames = TrapData.getAnimationFramesById(id);
		} else if (type.equalsIgnoreCase("D")) { // Deaths
            Gdx.app.error(TAG, "Error their is no  death frame for id"+id);
		}else{
            Gdx.app.error(TAG, "Error their is no frame for id"+id);
        }
	}

	public Event(String id, Sound sound) {
		this.id = id;
		this.sound = sound;
		if (this.sound != null)
			this.playSound = true;
		showFirstFrame = false;
		finalFrame = false;
		mobile=true;
		countShowFirst = 0;
		countShowFinal = 0;
	}

	

	public Event(String id, Animation frames, Sound sound, float x, float y, int direction, int duration, float range, float xRelative, float yRelative,
			int spriteDirection, float speed) {
		super();
		this.id = id;
		this.animationFrames = frames;
		this.sound = sound;
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.duration = duration;
		this.range = range;
		this.xRelative = xRelative;
		this.yRelative = yRelative;
		this.spriteDirection = spriteDirection;
		this.speed = speed;
		if (sound != null)
			playSound = true;
		showFirstFrame = true;
		finalFrame = false;
		mobile = true;
		countShowFirst = 0;
		countShowFinal = 0;
	}

	public int getSpriteDirection() {
		return spriteDirection;
	}

	public void setSpriteDirection(int spriteDirection) {
		this.spriteDirection = spriteDirection;
	}

	public String getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public float getX() {
		return x;
	}

	public float getXOnBoard() {
		return (x - Data.MAP_X) / Data.BLOCK_SIZE_X;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public float getYOnBoard() {
		return (y - Data.MAP_Y) / Data.BLOCK_SIZE_Y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public Animation getAnimation() {
		return animationFrames;
	}

	public void setAnimation(Animation animation) {
		this.animationFrames = animation;
	}

	public Sound getSound() {
		return sound;
	}

	public void setSound(Sound sound) {
		this.sound = sound;
	}

	public boolean isNeeDelete() {
		return neeDelete;
	}

	public void setNeeDelete(boolean neeDelete) {
		this.neeDelete = neeDelete;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction, float speed) {
		this.direction = direction;
		this.speed = speed;
		switch (direction) {
		case Data.SELF:
			this.xRelative = 0;
			this.yRelative = 0;
			break;
		case Data.SOUTH:
			this.xRelative = 0;
			this.yRelative = (0-this.speed);
			break;
		case Data.NORTH:
			this.xRelative = 0;
			this.yRelative = this.speed;
			break;

		case Data.EAST:
			this.xRelative = this.speed;
			this.yRelative = 0;
			break;
		case Data.WEST:
			this.xRelative = (0-this.speed);
			this.yRelative = 0;
			break;
		}
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getHeal() {
		return heal;
	}

	public void setHeal(int heal) {
		this.heal = heal;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;

	}

	public float getRange() {
		return range;
	}

	public void setRange(float range) {
		this.range = range;
        this.rangeRemaining = range;
	}
	
	public boolean isFinalFrame() {
		return finalFrame;
	}

	public void setFinalFrame(boolean isFinalFrame) {
		this.finalFrame = isFinalFrame;
	}
	
	
	public boolean isMobile() {
		return mobile;
	}

	public void setMobile(boolean canMove) {
		this.mobile = canMove;
	}

	public Event getCopiedEvent() {
		Event e = new Event(id, animationFrames, sound, x, y, direction, duration, range, xRelative, yRelative, spriteDirection, speed);
		e.setDamage(damage);
		e.setHeal(heal);
		e.setType(type);
		return e;
	}

	public void render(SpriteBatch batch) {

        TextureRegion region = animationFrames.getKeyFrame(elapsedTime, false);
        int width = region.getRegionWidth();
        int height = region.getRegionHeight();
        elapsedTime += Gdx.graphics.getDeltaTime();
        int finalDirection = direction - spriteDirection;

        Gdx.app.log("Event", "Draw the event "+this.toString());
		batch.draw(region, Data.MAP_X + x * Data.BLOCK_SIZE_X / Data.scale, Data.MAP_Y + y * Data.BLOCK_SIZE_Y / Data.scale, (width / 2) / Data.scale, (height / 2) / Data.scale, width, height, 1f, 1f, finalDirection);

		if (playSound) {
			playSound = false;
			sound.play();
		}
	}
	
	public void renderPostRemove(SpriteBatch batch) {
        Gdx.app.log("Event", "renderPostRemove the event "+this.toString());

        int dx = 0, dy = 0;
		int finalDirection = direction - spriteDirection;
        TextureRegion region = animationFrames.getKeyFrames()[animationFrames.getKeyFrames().length-1];
        int width = region.getRegionWidth();
		int height = region.getRegionHeight();

        batch.draw(region, Data.MAP_X + x * Data.BLOCK_SIZE_X / Data.scale, Data.MAP_Y + y * Data.BLOCK_SIZE_Y / Data.scale, (width / 2) / Data.scale, (height / 2) / Data.scale, width, height, 1f, 1f, finalDirection);

		if(countShowFinal<3){
				countShowFinal += 1;
				if(countShowFinal == 3)
					neeDelete = true;
		}
	}
	
	public void move() {
        if(rangeRemaining > 0 ){
            rangeRemaining -= speed;
            this.x += this.xRelative;
            this.y += this.yRelative;
        }else{
            this.mobile = false;
            this.finalFrame = true;
        }

		switch (direction) {
		case Data.SOUTH:
			range += yRelative / Data.BLOCK_SIZE_Y;
			break;
		case Data.NORTH:
			range -= yRelative / Data.BLOCK_SIZE_Y;
			break;
		case Data.EAST:
			range -= xRelative / Data.BLOCK_SIZE_X;
			break;
		case Data.WEST:
			range += xRelative / Data.BLOCK_SIZE_X;
			break;
		case Data.SELF:
			range -= 1;
		default:
			range -= 1;
			break;
		}
	}

	@Override
	public String toString() {
		return "Event [id=" + id + ", x=" + x + ", y=" + y + ", direction=" + direction + ", duration=" + duration + ", range=" + range
				+ ", xRelative=" + xRelative + ", yRelative=" + yRelative + ", damage=" + damage + ", heal=" + heal + ", type=" + type + "]";
	}

}
