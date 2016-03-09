package com.mygdx.game.data;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.game.Spell;

import java.util.ArrayList;


public class Hero {

	private String caracterClass;
	private Stats stat;
	private Texture icon;
	private ArrayList<Spell> spells = new ArrayList<Spell>();

	public Hero(String caracterClass, Texture icon, Stats stat) {
		super();
		this.icon = icon;
		this.caracterClass = caracterClass;
		this.stat = stat;
	}

	public Texture getIcon(){
		return icon;
	}
	
	public String getCaracterClass() {
		return caracterClass;
	}

	public void setCaracterClass(String caracterClass) {
		this.caracterClass = caracterClass;
	}

	public Stats getStat() {
		return stat;
	}

	public void setStat(Stats stat) {
		this.stat = stat;
	}

	public ArrayList<Spell> getSpells() {
		return spells;
	}

	public void setSpells(ArrayList<Spell> spells) {
		this.spells = spells;
	}

	public void addSpell(SpellD s) {
		// TODO
		spells.add(new Spell(s.getId(), s.getName(), s.getDamage(),
				s.getHeal(), s.getMana(), s.getRange(), s.getType(), s.getSpeed(), s.getEvent()));
	}

}
