package com.mygdx.game.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class HeroData {

	public static ArrayList<Hero> heros = new ArrayList<Hero>();
	public static ArrayList<String> caraClass = new ArrayList<String>();
	public static final HashMap<String, Integer> CLASSES_VALUES = new HashMap<String, Integer>(); 
	
	public void addHero(Hero h) {
		heros.add(h);
	}
	
	public static Hero getHeroByClass(String caracterClass){
		for(int i = 0; i < heros.size(); i++){
			if(heros.get(i).getStat().getCharacterClass().equals(caracterClass))
				return heros.get(i);
		}
		return null;
	}
	
	public static String getRandomHero(){
		if(Data.debug)
			return "mage";
		String result = "";
		Random rand = new Random();
		int n = rand.nextInt((HeroData.getHeroNumber() - 0)) + 0;
		
		return heros.get(n).getCaracterClass();
	}
	
	public static int getHeroNumber(){
		return heros.size();
	}
	
	public static void loadHeros(){
		HeroData heroData = new HeroData();
		Element root = XMLReader.readXML(Data.HERO_XML);

		Array heros = root.getChildrenByName("hero");

		Iterator i = heros.iterator();
		String id;
		int life, armor, mana, strength, magicPower, luck, movementPoints, magicResist, eyeSight;
		List <Element> spells = new ArrayList<Element>();
		
		while(i.hasNext()){
			Element el = (Element) i.next();
			
			id = el.getAttribute("id");
			life = Integer.parseInt(el.getChildByName("life").getText());
			armor = Integer.parseInt(el.getChildByName("armor").getText());
			mana = Integer.parseInt(el.getChildByName("mana").getText());
			strength = Integer.parseInt(el.getChildByName("strength").getText());
			magicPower = Integer.parseInt(el.getChildByName("magicPower").getText());
			luck = Integer.parseInt(el.getChildByName("luck").getText());
			movementPoints = Integer.parseInt(el.getChildByName("movementPoints").getText());
			magicResist = Integer.parseInt(el.getChildByName("magicResist").getText());
			eyeSight = Integer.parseInt(el.getChildByName("eyeSight").getText());

			Texture icon =new Texture(Gdx.files.internal(el.getChildByName("icon").getText()));

			Hero h = new Hero(id, icon, new Stats(life, armor, mana, strength, magicPower, luck, movementPoints, magicResist, eyeSight ,id));
			CLASSES_VALUES.put(id, Integer.parseInt(el.getChildByName("value").getText()));
			Iterator<Element> ii =  el.getChildByName("spells").getChildrenByName("spell").iterator();
			
			while(ii.hasNext()){
				Element e = (Element) ii.next();
				SpellD s = SpellData.getSpellById(e.getAttribute("id"));
				if(s != null)
					h.addSpell(s);
			}
			heroData.addHero(h);
			caraClass.add(h.getCaracterClass());
			System.out.println("	Hero : "+h.getCaracterClass());
		}
		//TODO 
//		CLASSES_VALUES.put("mage", 0);
//		CLASSES_VALUES.put("barbarian", 10);
//		CLASSES_VALUES.put("rogue", 5);
//		CLASSES_VALUES.put("cleric", 0);
	}

}
