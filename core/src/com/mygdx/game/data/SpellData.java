package com.mygdx.game.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.mygdx.game.game.Spell;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.badlogic.gdx.utils.XmlReader.*;


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

		Element root = XMLReader.readXML(Data.SPELLS_DATA_XML);
        
		Array monsters = root.getChildrenByName("spell");

		Iterator it = monsters.iterator();
		int damage, heal, mana, celNumber, range, direction;
		String id, name, type, file;
		while (it.hasNext()) {
			try {
			Element el = (Element) it.next();
			damage = Integer.parseInt(el.getChildByName("damage").getText());
			heal = Integer.parseInt(el.getChildByName("heal").getText());
			mana = Integer.parseInt(el.getChildByName("mana").getText());
			range = Integer.parseInt(el.getChildByName("range").getText());
			name = el.getChildByName("name").getText();
			id = el.getAttribute("id");
			file = el.getChildByName("file").getText();
			type = el.getChildByName("type").getText();
			direction = Integer.parseInt(el.getChildByName("direction").getText());
			celNumber = Integer.parseInt(el.getChildByName("celNumber").getText());
			Sound sound = Gdx.audio.newSound(Gdx.files.internal(el.getChildByName("sound").getText()));
			float speed = Float.parseFloat(el.getChildByName("speed").getText());

			Texture img = new Texture(Gdx.files.internal(el.getChildByName("file").getText()));

			TextureRegion[][] tmpFrames = TextureRegion.split(img,Integer.parseInt(el.getChildByName("celX").getText()),
					Integer.parseInt(el.getChildByName("celY").getText()));
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
