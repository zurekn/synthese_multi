package com.mygdx.game.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.mygdx.game.game.Mob;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.badlogic.gdx.utils.XmlReader.*;


public class MonsterData {
	
	public static ArrayList<Monster> monsters = new ArrayList<Monster>();
	
	public void addMonster(Monster m){
		monsters.add(m);
	}
	
	public static TextureRegion[][] getAnimationById(String id){
		for(int i = 0; i < monsters.size(); i++){
			if(monsters.get(i).getId().equals(id)){
				return monsters.get(i).getAnimationFrames();
			}
		}
		return null;
	}
	
	public static Monster getMonsterById(String id){
		for(int i = 0; i < monsters.size(); i++){
			if(monsters.get(i).getId().equals(id)){
				return monsters.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Load all Mobs from Data.MAP_XML in a ArrayList
	 * @return ArrayList<Mob>
	 */
	public static ArrayList<Mob> initMobs() {
		ArrayList<Mob> mobs = new ArrayList<Mob>();	
		
		Element root = XMLReader.readXML(Data.MAP_XML);

		Array monsters = root.getChildrenByName("monster");

		Iterator i = monsters.iterator();
		int posX, posY;
		String id;
		int idCount=1;
		List <Element> spells = new ArrayList<Element>();
		while (i.hasNext()) {

			Element el = (Element) i.next();
			id = el.getAttribute("id");
			posX = Integer.parseInt(el.getChildByName("x").getText());
			posY = Integer.parseInt(el.getChildByName("y").getText());
			Mob m = new Mob(posX, posY, id,"m"+idCount);
			mobs.add(m);
			idCount++;
		}


		return mobs;

	}
	
	public static void loadMonster() {
		MonsterData monsterData = new MonsterData();

		Element root = XMLReader.readXML(Data.MONSTER_DATA_XML);
		

		Array monsters = root.getChildrenByName("monster");

		Iterator it = monsters.iterator();
		String name;
		int life, armor, mana, strength, magicPower, luck, movementPoints, magicResist, eyeSight;
		Array <Element> spells = new Array<Element>();
		String aiType ;
		while (it.hasNext()) {

			Element el = (Element) it.next();
			try {
				name = el.getChildByName("name").getText();
				life = Integer.parseInt(el.getChildByName("life").getText());
				armor = Integer.parseInt(el.getChildByName("armor").getText());
				mana = Integer.parseInt(el.getChildByName("mana").getText());
				strength = Integer.parseInt(el.getChildByName("strength").getText());
				magicPower= Integer.parseInt(el.getChildByName("magicPower").getText());
				luck = Integer.parseInt(el.getChildByName("luck").getText());
				movementPoints = Integer.parseInt(el.getChildByName("movementPoints").getText());
				String id = el.getAttribute("id");
				spells = el.getChildByName("spells").getChildrenByName("spell");
				magicResist = Integer.parseInt(el.getChildByName("magicResist").getText());
				eyeSight = Integer.parseInt(el.getChildByName("eyeSight").getText());
				Iterator<Element> ii = spells.iterator();

				Texture img = new Texture(Gdx.files.internal(el.getChildByName("file").getText()));

				TextureRegion[][] tmpFrames = TextureRegion.split(img,Integer.parseInt(el.getChildByName("celDimensionX").getText()),
						Integer.parseInt(el.getChildByName("celDimensionY").getText()));
				TextureRegion[] animationFrames = new TextureRegion[tmpFrames.length*tmpFrames[0].length];
				int index = 0;
				for(int i = 0 ; i < tmpFrames.length;i++)
					for(int j = 0 ; j < tmpFrames[i].length;j++)
						animationFrames[index++] = tmpFrames[i][j];

				Stats stats = new Stats(life, armor, mana, strength,
						magicPower, luck, movementPoints, magicResist, eyeSight);
				aiType = el.getChildByName("aiType").getText();
                Monster m = new Monster(id, aiType, name, tmpFrames, stats);
                while(ii.hasNext()){
					Element e = (Element) ii.next();
					SpellD s = SpellData.getSpellById(e.getText());
					if(s != null)
						m.addSpell(s);
						
				}
				System.out.println("	Monster : "+m.getName());
				monsterData.addMonster(m);
			}catch (NumberFormatException e){
				e.printStackTrace();
			}
		}

	}
	
}
