package com.mygdx.game.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.badlogic.gdx.utils.XmlReader.*;


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
	
	public static Animation getAnimationFramesById(String id){
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


		Element root = XMLReader.readXML(Data.TRAPS_DATA_XML);

		Array trapsList = root.getChildrenByName("trap");

		Iterator it = trapsList.iterator();
		int damage, celNumber;
		String id, name, damageType, file, sound;
		while (it.hasNext()) {
			try {
			Element el = (Element) it.next();
			damage = Integer.parseInt(el.getChildByName("damage").getText());
			name = el.getChildByName("name").getText();
			id = el.getAttribute("id");
			file = el.getChildByName("file").getText();
			sound = el.getChildByName("sound").getText();
			celNumber = Integer.parseInt(el.getChildByName("celNumber").getText());
			damageType = el.getChildByName("damageType").getText();
			Texture img = new Texture(Gdx.files.internal(el.getChildByName("file").getText()));

			TextureRegion[][] tmpFrames = TextureRegion.split(img,Integer.parseInt(el.getChildByName("celX").getText()),
					Integer.parseInt(el.getChildByName("celY").getText()));
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
