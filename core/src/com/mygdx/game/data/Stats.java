package com.mygdx.game.data;

public class Stats {
	private int life = 1;
	private int maxLife = 1;
	private int maxMana = 1;
	private int armor = 1;
	private int mana = 1;
	private int strength = 1;
	private int magicPower = 1;
	private int luck = 1;
	private int movementPoints = 1;
	private int magicResist = 1;
	private int eyeSight;
	private String characterClass = "";
	private float lifePercentage = 1.f;
	private float manaPercentage = 1.f;

	public Stats(int life, int mana) {
		this.life = life;
		this.mana = mana;
		this.maxLife = life;
		this.maxMana = mana;
	}

	public Stats(int life, int armor, int mana, int strength, int magicPower,
			int luck, int movementPoints, int magicResist, int eyeSight) {
		this.life = life;
		this.maxLife = life;
		this.armor = armor;
		this.mana = mana;
		this.maxMana = mana;
		this.strength = strength;
		this.magicPower = magicPower;
		this.luck = luck;
		this.movementPoints = movementPoints;
		this.magicResist = magicResist;
		this.eyeSight = eyeSight;
	}

	public Stats(int life, int armor, int mana, int strength, int magicPower,
			int luck, int movementPoints, int magicResist, int eyeSight,
			String caracterClass) {
		this.life = life;
		this.maxLife = life;
		this.armor = armor;
		this.mana = mana;
		this.maxMana = mana;
		this.strength = strength;
		this.magicPower = magicPower;
		this.luck = luck;
		this.movementPoints = movementPoints;
		this.magicResist = magicResist;
		this.eyeSight = eyeSight;
		this.characterClass = caracterClass;
	}

	public String getCharacterClass() {
		return characterClass;
	}

	public void setCharacterClass(String characterClass) {
		this.characterClass = characterClass;
	}

	public int getMagicResist() {
		return magicResist;
	}

	public void setMagicResist(int magicResist) {
		this.magicResist = magicResist;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
		if (this.life > this.maxLife)
			this.life = maxLife;
		
		float l = (float) life, m = (float) maxLife;
		this.lifePercentage = l / m;
	}

	public int getArmor() {
		return armor;
	}

	public int getMaxLife() {
		return maxLife;
	}

	public void setMaxLife(int maxLife) {
		this.maxLife = maxLife;
	}

	public float getLifePercentage() {
		return lifePercentage;
	}

	public int getMaxMana() {
		return maxMana;
	}

	public void setMaxMana(int maxMana) {
		this.maxMana = maxMana;
	}

	public void setArmor(int armor) {
		this.armor = armor;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
		if (this.mana > this.maxMana)
			this.mana = maxMana;
		
		float n = (float) mana, m = (float) maxMana;
		this.manaPercentage = n / m;
	}
	
	public float getManaPercentage(){
		return manaPercentage;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int getMagicPower() {
		return magicPower;
	}

	public void setMagicPower(int magicPower) {
		this.magicPower = magicPower;
	}

	public int getLuck() {
		return luck;
	}

	public void setLuck(int luck) {
		this.luck = luck;
	}

	public int getMovementPoints() {
		return movementPoints;
	}

	public void setMovementPoints(int movementPoints) {
		this.movementPoints = movementPoints;
	}

	public int getEyeSight() {
		return eyeSight;
	}

	public void setEyeSight(int eyeSight) {
		this.eyeSight = eyeSight;
	}

	public Stats clone(){
		Stats s = new Stats(life,armor,mana, strength, magicPower, luck, movementPoints, magicResist, eyeSight, characterClass);
		s.setLife(life);
		return s;
	}
	
	@Override
	public String toString() {
		return "Stats [life=" + life + ", armor=" + armor + ", mana=" + mana
				+ ", strength=" + strength + ", magicPower=" + magicPower
				+ ", luck=" + luck + ", movementPoints=" + movementPoints
				+ ", magicResist=" + magicResist + ", characterClass="
				+ characterClass + "]";
	}
}
