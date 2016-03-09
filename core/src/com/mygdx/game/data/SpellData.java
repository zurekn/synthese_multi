package com.mygdx.game.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.game.Spell;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class SpellData {

	public static ArrayList<SpellD> spells = new ArrayList<SpellD>();
	
	public void addSpell(SpellD spell){
		spells.add(spell);
	}
	
	public static TextureRegion[] getAnimationFramesById(String id){
		for(int i = 0; i < spells.size(); i++){
			if(spells.get(i).getId().equals(id)){
				return spells.get(i).getEvent().getAnimation();
			}
		}
		return null;
	}
	
	/**
	 * Load all spell data from Data.SPELLS_DATA_XML
	 */
	public static void loadSpell(){

		Document doc = XMLReader.readXML(Data.SPELLS_DATA_XML);
		
		Element root = doc.getRootElement();

		List monsters = root.getChildren("spell");

		Iterator it = monsters.iterator();
		int damage, heal, mana, celNumber, range, direction;
		String id, name, type, file;
		while (it.hasNext()) {
			try {
			Element el = (Element) it.next();
			damage = Integer.parseInt(el.getChildText("damage"));
			heal = Integer.parseInt(el.getChildText("heal"));
			mana = Integer.parseInt(el.getChildText("mana"));
			range = Integer.parseInt(el.getChildText("range"));
			name = el.getChildText("name");
			id = el.getAttributeValue("id");
			file = el.getChildText("file");
			type = el.getChildText("type");
			direction = Integer.parseInt(el.getChildText("direction"));
			celNumber = Integer.parseInt(el.getChildText("celNumber"));
			Sound sound = Gdx.audio.newSound(Gdx.files.internal(el.getChildText("sound")));
			float speed = Float.parseFloat(el.getChildText("speed"));

			Texture img = new Texture(Gdx.files.internal(el.getChildText("file")));

			TextureRegion[][] tmpFrames = TextureRegion.split(img,Integer.parseInt(el.getChildText("celX")),
					Integer.parseInt(el.getChildText("celY")));
			TextureRegion[] animationFrames = new TextureRegion[tmpFrames.length*tmpFrames[0].length];
			int index = 0;
			for(int i = 0 ; i < tmpFrames.length;i++)
				for(int j = 0 ; j < tmpFrames[i].length;j++) {
                    animationFrames[index++] = tmpFrames[i][j];
                }

			SpellD spell = new SpellD(id, damage, heal, mana, range, name, celNumber, type, animationFrames, sound, direction, speed);
			spells.add(spell);
			System.out.println("	Spell : "+spell.toString());
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static SpellD getSpellById(String text) {
		for(int i = 0; i < spells.size(); i++){
			if(spells.get(i).getId().equals(text))
				return spells.get(i);
		}
		return null;
	}

	public static ArrayList<Spell> getSpellForClass(String characterClass) {
		ArrayList<Spell> array = new ArrayList<Spell>();
		
		
		return array;
	}
	
}
