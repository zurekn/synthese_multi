package com.mygdx.game.ai;

import java.lang.*;
import java.util.ArrayList;

public abstract class CharacterBuilder {

	public static ArrayList<java.lang.CharacterData> build(ArrayList<java.lang.CharacterData> characters){
		ArrayList<java.lang.CharacterData> list = new ArrayList<java.lang.CharacterData>();
		for(java.lang.CharacterData c : characters)
			list.add(new java.lang.CharacterData(c));
		
		return list;
	}

}
