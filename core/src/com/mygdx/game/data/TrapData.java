package com.mygdx.game.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class TrapData {
	public static ArrayList<TrapD> traps = new ArrayList<TrapD>();

	public void addTrap(TrapD t) {
		traps.add(t);
	}

	public static TrapD getTrapById(String id) {
		for (int i = 0; i < traps.size(); i++) {
			if (traps.get(i).getId().equals(id)) {
				return traps.get(i);
			}
		}
		return null;
	}
	
	public static TextureRegion[] getAnimationById(String id){
		for(int i = 0; i < traps.size(); i++){
			if(traps.get(i).getId().equals(id)){
				return traps.get(i).getEvent().getAnimation();
			}
		}
		return null;
	}
	
	/**
	 * Load all traps data from Data.TRAPS_DATA.XML
	 */
	public static void loadTrap(){


		Document doc = XMLReader.readXML(Data.TRAPS_DATA_XML);
		
		Element root = doc.getRootElement();

		List trapsList = root.getChildren("trap");

		Iterator it = trapsList.iterator();
		int damage, celNumber;
		String id, name, damageType, file, sound;
		while (it.hasNext()) {
			try {
			Element el = (Element) it.next();
			damage = Integer.parseInt(el.getChildText("damage"));
			name = el.getChildText("name");
			id = el.getAttributeValue("id");
			file = el.getChildText("file");
			sound = el.getChildText("sound");
			celNumber = Integer.parseInt(el.getChildText("celNumber"));
			damageType = el.getChildText("damageType");
			Texture img = new Texture(Gdx.files.internal(el.getChildText("file")));

			TextureRegion[][] tmpFrames = TextureRegion.split(img,Integer.parseInt(el.getChildText("celX")),
					Integer.parseInt(el.getChildText("celY")));
			TextureRegion[] animationFrames = new TextureRegion[tmpFrames.length*tmpFrames[0].length];
			int index = 0;
			for(int i = 0 ; i < tmpFrames.length;i++)
				for(int j = 0 ; j < tmpFrames[i].length;j++)
					animationFrames[index++] = tmpFrames[j][i];
			traps.add(new TrapD(id, damage, damageType, name, celNumber, animationFrames, Gdx.audio.newSound(Gdx.files.internal(sound))));
			System.out.println("   Trap : ["+name+"] load end");
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
