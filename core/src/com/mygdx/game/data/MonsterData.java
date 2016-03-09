package com.mygdx.game.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.game.Mob;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;

public class MonsterData {
	
	public static ArrayList<Monster> monsters = new ArrayList<Monster>();
	
	public void addMonster(Monster m){
		monsters.add(m);
	}
	
	public static TextureRegion[] getAnimationById(String id){
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
		
		Document doc = XMLReader.readXML(Data.MAP_XML);
			
		Element root = doc.getRootElement();
		List monsters = root.getChildren("monster");

		Iterator i = monsters.iterator();
		int posX, posY;
		String id;
		int idCount=1;
		List <Element> spells = new ArrayList<Element>();
		while (i.hasNext()) {

			Element el = (Element) i.next();
			id = el.getAttributeValue("id");
			posX = Integer.parseInt(el.getChildText("x"));
			posY = Integer.parseInt(el.getChildText("y"));
			Mob m = new Mob(posX, posY, id,"m"+idCount);
			mobs.add(m);
			idCount++;
		}
		
		
		return mobs;
		
	}
	
	public static void loadMonster() {
		MonsterData monsterData = new MonsterData();

		Document doc = XMLReader.readXML(Data.MONSTER_DATA_XML);
		
		Element root = doc.getRootElement();

		List monsters = root.getChildren("monster");

		Iterator it = monsters.iterator();
		String name;
		int life, armor, mana, strength, magicPower, luck, movementPoints, magicResist, eyeSight;
		List <Element> spells = new ArrayList<Element>();
		String aiType ;
		while (it.hasNext()) {

			Element el = (Element) it.next();
			try {
				name = el.getChildText("name");
				life = Integer.parseInt(el.getChildText("life"));
				armor = Integer.parseInt(el.getChildText("armor"));
				mana = Integer.parseInt(el.getChildText("mana"));
				strength = Integer.parseInt(el.getChildText("strength"));
				magicPower= Integer.parseInt(el.getChildText("magicPower"));
				luck = Integer.parseInt(el.getChildText("luck"));
				movementPoints = Integer.parseInt(el.getChildText("movementPoints"));
				String id = el.getAttributeValue("id");
				spells = el.getChild("spells").getChildren("spell");
				magicResist = Integer.parseInt(el.getChildText("magicResist"));
				eyeSight = Integer.parseInt(el.getChildText("eyeSight"));
				Iterator<Element> ii = spells.iterator();

				Texture img = new Texture(Gdx.files.internal(el.getChildText("file")));

				TextureRegion[][] tmpFrames = TextureRegion.split(img,Integer.parseInt(el.getChildText("celDimensionX")),
						Integer.parseInt(el.getChildText("celDimensionY")));
				TextureRegion[] animationFrames = new TextureRegion[tmpFrames.length*tmpFrames[0].length];
				int index = 0;
				for(int i = 0 ; i < tmpFrames.length;i++)
					for(int j = 0 ; j < tmpFrames[i].length;j++)
						animationFrames[index++] = tmpFrames[i][j];

				Stats stats = new Stats(life, armor, mana, strength,
						magicPower, luck, movementPoints, magicResist, eyeSight);
				aiType = el.getChildText("aiType");
				Monster m = new Monster(id, aiType, name, animationFrames, stats);
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
