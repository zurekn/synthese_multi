package com.mygdx.game.data;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.game.Spell;

import java.util.ArrayList;

public class Monster {

	private String id;
	private String name;
	private TextureRegion[][] animationFrames;
	private Stats stats;
	private ArrayList<Spell> spells = new ArrayList<Spell>();
	private String aiType;
	
	public Monster(String id, String aiType, String name, TextureRegion[][] frames, Stats stats) {
		super();
		this.id = id;
		this.animationFrames = frames;
		this.stats = stats;
		this.name = name;
		this.aiType = aiType;
	}
	
	public Monster(String id, String aiType, TextureRegion[][] frames, Stats stats, ArrayList<Spell> spells) {
		super();
		this.id = id;
		this.animationFrames = frames;
		this.stats = stats;
		this.spells = spells;
		this.aiType = aiType;
	}

	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TextureRegion[][] getAnimationFrames() {
		return animationFrames;
	}

	public void setAnimationFrames(TextureRegion[][] frames) {
		this.animationFrames = frames;
	}
	
	@Override
	public String toString() {
		return "Monster [id=" + id + ", name=" + name + ", animation="
				+ animationFrames.toString() + "]";
	}

	public void addSpell(SpellD s) {
		//TODO
		spells.add(new Spell(s.getId(), s.getName(), s.getDamage(), s.getHeal(), s.getMana(), s.getRange(), s.getType(), s.getSpeed(), s.getEvent()));		
	}
	
	public Stats getStats(){
		return stats;
	}

	public ArrayList<Spell> getSpells() {
		return spells;
	}

	public String getAiType() {
		return this.aiType;
	}

}
