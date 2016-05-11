package com.mygdx.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.data.Data;
import com.mygdx.game.data.SpellD;
import com.mygdx.game.data.Stats;
import com.mygdx.game.exception.IllegalActionException;
import com.mygdx.game.exception.IllegalMovementException;
import com.mygdx.game.javacompiler.CompileString;
import com.mygdx.game.javacompiler.IAGenetic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

/**
 * Class representing a character which can be either a player or a monster.
 * 
 */
public abstract class Character {
    private static final String TAG = "Character";
    private int x;
	private int y;
	private int lastX;
	private int lastY;
	private String id;
	private String trueID;
	
	private int sizeCharacter;

    /* Animation */
	private TextureRegion[][] animationFrames;
    private Animation animationUp;
    private Animation animationDown;
    private Animation animationLeft;
    private Animation animationRight;

    /* movement */
    int direction = Data.DOWN;

    private Stats stats;
	private IAFitness fitness;
	private boolean myTurn = false;
	private ArrayList<Spell> spells = new ArrayList<Spell>();
	private String name;
	private String aiType;
	private Character focusedOn;
	private boolean npc = true;
	protected boolean monster = true;

	private Class<?> cl = null;
	private Object obj = null;
	GameStage gameStage = GameStage.gameStage;

	public abstract void render(SpriteBatch batch, ShapeRenderer shapeRenderer);

	public abstract void init();


	/**
	 * Genere le script pour l'IA
	 */
	public void generateScriptGenetic() {// génération d'un script génétique
		CompileString.generate(this.trueID);
	}

	/**
	 *  Compile le script d'IA
	 */
	void compileScriptGenetic()
	{
		IAGenetic ch = CompileString.CompileAndInstanciateClass(this.trueID);
		cl = ch.getC();
		obj = ch.getObj();
	}

	public void findScriptAction(int compteur){// Ici mettre l'instanciation de la nouvelle classe propre à CE charactère
		System.out.println(this.id +"-compteur = "+compteur);
		String result = "";
		if(compteur>=10)
		{
			try {
				gameStage.decodeAction("p");
			} catch (IllegalActionException e) {
				e.printStackTrace();
			}
		}
		else
		{	try
			{
				Method method = cl.getDeclaredMethod("run", Character.class);
				result = (String) method.invoke(obj, this);
				if(result !="")
				{
					System.out.println("### Character.findScriptAction : result = "+result);
					int index = 0;
					String[] decode  = result.split("!!");
					for(String st : decode)
					{
						if(!st.equals("") && !(index==0 && compteur > 0)  ) gameStage.decodeAction(st);
						index++;

					}
					method = cl.getDeclaredMethod("setActionString", String.class);
					method.invoke(obj, "");
				}
				else
				{
					if(compteur == 0) getFitness().debugFile("error : mob = "+getTrueID()+" print= emptyAction", true);
					findScriptAction(++compteur);
					return;
				}
			} catch (IllegalAccessException e) {

				if(compteur == 0)
				{
					e.printStackTrace();
					getFitness().debugFile("error : mob = "+getTrueID()+"action = "+result+", print= illegalAccess", true);
				}
				findScriptAction(++compteur);
				return;
			} catch (SecurityException e) {
				if(compteur == 0)e.printStackTrace();findScriptAction(++compteur);
			} catch (NoSuchMethodException e) {
				if(compteur == 0)e.printStackTrace();
			} catch (IllegalArgumentException e) {
				if(compteur == 0)e.printStackTrace();
			} catch (InvocationTargetException e) {
				if(compteur == 0)e.printStackTrace();
			} catch (IllegalActionException e) {

				if(compteur == 0)
				{
					e.printStackTrace();
					getFitness().debugFile("error : mob = "+getTrueID()+"action = "+result+", print= illegalAction", true);

				}
				findScriptAction(++compteur);
				return;
			}
		}
	}

	/**
	 * Move the character to the position x:y if it's possible
	 * 
	 * @param position
	 * @throws IllegalMovementException
	 */

	public void moveTo(String position) throws IllegalMovementException {
		if (GameStage.gameStage.getAllPositions().contains(position)) {
			throw new IllegalMovementException(
					"Caracter already at the position [" + position + "]");
		}

		if (Data.untraversableBlocks.containsKey(position)) {
			throw new IllegalMovementException("Untraversable block at ["
					+ position + "]");
		} else {
			String tokens[] = position.split(":");
			if (tokens.length != 2) {
				throw new IllegalMovementException("Invalid movement syntax ");
			} else {
				
				int x = Integer.parseInt(tokens[0]);
				int y = Integer.parseInt(tokens[1]);

				if (x < 0 || x >= Data.BLOCK_NUMBER_X || y < 0
						|| y >= Data.BLOCK_NUMBER_Y) {
					throw new IllegalMovementException(
							"Movement is out of the map");
				} else {

					int xTmp = this.x, yTmp = this.y;
					int movePoints = this.stats.getMovementPoints();
					int dist = (int) Math.sqrt(Math.pow(x - xTmp, 2)
							+ Math.pow(y - yTmp, 2));
					if (dist > movePoints) {
						throw new IllegalMovementException(
								"Not enough movements points");
					} else {
						lastX = this.x;
						lastY = this.y;
						this.x = x;
						this.y = y;
						if (Data.debug)
							System.out.println("Current character move to ["
									+ x + ":" + y + "]");
					}
				}
			}
		}
	}

	/**
	 * Is intended to be used with alpha beta
	 * 
	 * @param x, pposition x
	 * @param y, postion y
	 * @throws IllegalMovementException
	 */
	public void moveAiTo(int x, int y) {
		lastX = this.x;
		lastY = this.y;
		this.x = x;
		this.y = y;

	}
	
	/**
	 * Use a spell given by its id, throws an {@link IllegalActionException}
	 * otherwise.
	 * 
	 * @param spellID
	 * @param direction
	 * @throws IllegalActionException
	 * @return damage
	 */
	public String useSpell(String spellID, int direction)
			throws IllegalActionException {
		Spell spell = this.getSpell(spellID);
		if (spell == null)
			throw new IllegalActionException("Spell unkown");
		// TODO handle the heal
		if(spell.getMana() > stats.getMana())
			throw new IllegalActionException("No Enough Mana " + spell.getMana() +" / "+ stats.getMana());
		else{
			int newMana = stats.getMana() - spell.getMana();
			stats.setMana(newMana);
		}

		int damage = spell.getDamage() + stats.getMagicPower()*2 + stats.getStrength()*2;
		int heal = spell.getHeal() + stats.getMagicPower()*2;
		int res = 0;
		Random rand = new Random();
		int i = rand.nextInt(101);
		if(i < stats.getLuck()){
			//coup critique
			res = 1;
			damage += damage/2;
			heal += heal / 2;
		}
		if(i > 90){
			//echec critique
			heal = (heal/2) * -1;
			damage = damage / 2;
			res = -1;
		}
		System.out.println("Character : "+this.id+" used spell "+spell.getName()+", which deal : "+ damage +" *****************************************************************************");
		return damage + ":" + heal+":"+res;
	}
	
	public double distanceFrom(Character c){
		int a = x - c.x;
		int b = y - c.y;
		return Math.sqrt(a*a +b*b);
	}

	/**
	 * 
	 * @param damage
	 *            the damage value
	 * @param type
	 *            , type of damage (fire, ice, shock...)
	 */
	public int takeDamage(int damage, String type) {
		
		System.out.println("Icoming : "+damage+", "+type+", counterP " +getStats().getArmor()+", counterM " +getStats().getMagicResist());
		
		if (type.equals("magic")) {
			damage = damage - getStats().getMagicResist();
		} else if (type.equals("physic")) {
			damage = damage - getStats().getArmor();
		} else {
			System.out.println("Wrong damage type : " + type);
		}
		if (damage < 0)
			damage = 0;
		stats.setLife(stats.getLife() - damage);
		System.out.println(id + " take : [" + damage + "] damage, remaining ["+stats.getLife()+"] HP");
		return damage;
	}

	public void heal(int heal) {
		System.out.println(id + "take : [" + heal + "] heal");
		stats.setLife(stats.getLife() + heal);
	}

	public boolean checkDeath() {
		return stats.getLife() <= 0;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Stats getStats() {
		return stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}

	public TextureRegion[][] getAnimationFrames() {
		return animationFrames;
	}

	public void setAnimationFrames(TextureRegion[][] frames) {
		this.animationFrames = frames;
	}

	public void setMyTurn(boolean b) {
		myTurn = b;
	}

	public boolean isMyTurn() {
		return myTurn;
	}

	public String getAiType() {
		return aiType;
	}

	public void setAiType(String aiType) {
		this.aiType = aiType;
	}

	public Character getFocusedOn() {
		return focusedOn;
	}

	public void setFocusedOn(Character focusedOn) {
		this.focusedOn = focusedOn;
	}
	
	public String getTrueID() {
		return trueID;
	}

	public void setTrueID(String trueID) {
		this.trueID = trueID;
	}

	public int getSizeCharacter() {
		return sizeCharacter;
	}

	public void setSizeCharacter(int sizeCharacter) {
		this.sizeCharacter = sizeCharacter;
	}

	/**
	 * Add the spell s to this character.
	 * 
	 * @param s
	 * @see Spell
	 */
	public void addSpell(SpellD s) {
		spells.add(new Spell(s.getId(), s.getName(), s.getDamage(),
				s.getHeal(), s.getMana(), s.getRange(), s.getType(), s.getSpeed(), s
						.getEvent()));
	}

	public ArrayList<Spell> getSpells() {
		return spells;
	}

	public void setSpells(ArrayList<Spell> spells) {
		this.spells = spells;
	}

	public boolean isNpc() {
		return this.npc;
	}

	public void setNpc(boolean npc) {
		this.npc = npc;
	}

	public boolean isMonster() {
		return this.monster;
	}

	/**
	 * Get a spell by its id
	 * 
	 * @param spellID
	 * @return the {@link Spell} or null if not found.
	 */
	public Spell getSpell(String spellID) {
		for (Iterator<Spell> it = spells.iterator(); it.hasNext();) {
			Spell s = it.next();
			if (s.getId().equals(spellID))
				return s;
		}
		return null;
	}

	/**
	 * 
	 * @param spellID
	 * @return true if this character know the spell, false otherwise.
	 */
	public boolean isSpellLearned(String spellID) {
		for (Iterator<Spell> it = spells.iterator(); it.hasNext();) {
			Spell s = it.next();
			if (s.getId().equals(spellID))
				return true;
		}
		return false;
	}
	
	public int getLastX() {
		return lastX;
	}

	public int getLastY() {
		return lastY;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Character other = (Character) obj;
		if (trueID == null) {
			if (other.trueID != null)
				return false;
		} else if (!trueID.equals(other.trueID))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Character [x=" + x + ", y=" + y + ", id=" + id + ", animation="
				+ Arrays.toString(animationFrames) + ", stats=" + stats + ", myTurn="
				+ myTurn + ", spells=" + spells + ", name=" + name + "]";
	}

	/**
	 * Regen 10% of the maxMana + the magicPower stat
	 */
	public void regenMana() {

		stats.setMana(stats.getMana() + stats.getMagicPower() + stats.getMaxMana() / 10);
	}

    public Animation getAnimation(int direction) {
        switch(direction){
            case Data.UP:
                return animationUp;
            case Data.DOWN:
                return animationDown ;
            case Data.RIGHT:
                return animationRight ;
            case Data.LEFT:
                return animationLeft;
            default:
                Gdx.app.log(TAG, "Error can't find animation for direction ["+direction+"]");
                return null;
        }
    }
    public void setAnimation(int direction, Animation animation) {
        switch(direction){
            case Data.UP:
                this.animationUp = animation;
                break;
            case Data.DOWN:
                this.animationDown = animation;
                break;
            case Data.RIGHT:
                this.animationRight = animation;
                break;
            case Data.LEFT:
                this.animationLeft = animation;
                break;
            default:
                Gdx.app.log(TAG, "Error : can't set animation for direction ["+direction+"]");
                break;
        }
    }

    public void initAnimation(TextureRegion[][] animationFrames, float v) {
        this.animationFrames = animationFrames;
        animationUp = new Animation(v, animationFrames[Data.UP]);
        animationDown = new Animation(v, animationFrames[Data.DOWN]);
        animationLeft = new Animation(v, animationFrames[Data.LEFT]);
        animationRight = new Animation(v, animationFrames[Data.RIGHT]);
    }

    /**
     * Return the spell with same name.
     * @param name String
     * @return Spell
     */
    public Spell getSpellByName(String name) {
        for(Spell s : spells){
            if (s.getName().equals(name)){
                return s;
            }
        }
        return null;
    }

// 					=========================   GAI METHODS  ====================================

	/**
	 * Génération d'un déplacement
	 * ddx et ddy sont des entiers qui correspondent à :
	 * 0 => petit déplacement
	 * 1 => moyen déplacement
	 * 2 => grand déplacement
	 * Le signe de ddx et ddy déterminent la direction
	 */
	public String getDeplacement(int ddx, int ddy)
	{
		int dx=0;
		int dy=0;
		GameStage gameStage = GameStage.gameStage;
		switch(ddx)
		{
			case -2 :
				dx = -this.stats.getMovementPoints();
				break;
			case -1 :
				dx = -(int)(this.stats.getMovementPoints())/2;
				break;
			case 0 :
				dx = 1;
			case 1 :
				dx = (int)(this.stats.getMovementPoints())/2;
				break;
			case 2 :
				dx = this.stats.getMovementPoints();
				break;
			default :
				dx = 0;
		}
		switch(ddy)
		{
			case -2 :
				dy = -this.stats.getMovementPoints();
				break;
			case -1 :
				dy = -(int)(this.stats.getMovementPoints())/2;
				break;
			case 0 :
				dy = 1;
			case 1 :
				dy = (int)(this.stats.getMovementPoints())/2;
				break;
			case 2 :
				dy = this.stats.getMovementPoints();
				break;
			default :
				dy = 0;
		}


		return "m:" + (gameStage.getCurrentPlayer().getX()+dx) + ":" + (gameStage.getCurrentPlayer().getY()+dy);
	}

	/**
	 * Passer son tour
	 */
	public String passerTour()
	{
		return "p";
	}

	/**
	 * Retourne le premier personnage trouvé dans la direction donnée
	 */
	public Character researchCharacter(int direction)
	{
		GameStage gameStage = GameStage.gameStage;
		return (gameStage.getCharacterPositionOnLine(gameStage.getCurrentPlayer().getX(), gameStage.getCurrentPlayer().getY(), direction).isEmpty()? null : gameStage.getCharacterPositionOnLine(gameStage.getCurrentPlayer().getX(), gameStage.getCurrentPlayer().getY(), direction).get(0));

	}

	/**
	 * Retourne le premier personnage trouvé dans la direction donnée
	 */
	public boolean isCharacterInLine(int direction)
	{
		boolean exist = false;
		GameStage gameStage = GameStage.gameStage;
		exist = (gameStage.getCharacterPositionOnLine(gameStage.getCurrentPlayer().getX(), gameStage.getCurrentPlayer().getY(), direction).isEmpty()? exist : true);
		return exist;
	}

	/**
	 * Retourne la range d'un spell
	 */
	public int Portee(String spellID)
	{
		return this.getSpell(spellID).getRange();
	}

	/**
	 *
	 * @return the id of the most powerful damaging spell of this character
	 */
	public String getMaxDamagingSpellId(){
		String maxPowered = new String();
		int maxPower = Integer.MIN_VALUE;
		for(Spell s : this.getSpells()){
			if(s.getDamage() > maxPower){
				maxPower = s.getDamage();
				maxPowered = s.getId();
			}
		}
		return maxPowered;
	}

	/**
	 *
	 * @return the id of the most powerful healing spell of this character
	 */
	public String getMaxHealingSpellId(){
		String maxPowered = new String();
		int maxPower = Integer.MIN_VALUE;
		for(Spell s : this.getSpells()){
			if(s.getHeal() > maxPower){
				maxPower = s.getHeal();
				maxPowered = s.getId();
			}
		}
		return maxPowered;
	}




	public IAFitness getFitness() {
		return fitness;
	}

	public void setFitness(IAFitness fitness) {
		this.fitness = fitness;
	}

}
