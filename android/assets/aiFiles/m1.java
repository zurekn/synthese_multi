//package test;

import com.mygdx.game.data.Data;
import com.mygdx.game.game.*;
import com.mygdx.game.game.Character;

public class m1 {
	GameStage gameStage = GameStage.gameStage;
	String actionString = "";
	String deplacementString = "p";
	int up=Data.NORTH;
	int down=Data.SOUTH;
	int left=Data.WEST;
	int right=Data.EAST;
	com.mygdx.game.game.Character defaultC = null;
	String defaultString = "";
	int defaultInt = 0;
	float defaultFloat = 0f;
	boolean defaultBoolean = false;
	
	
	public m1() {
	}
	
	public String run(Character ch)
	{
		    deplacementString = ch.getDeplacement(3,-2);
		    deplacementString = ch.getDeplacement(2,-2);
		if(false== ((ch.researchCharacter(left)==null)?defaultBoolean:ch.researchCharacter(left).isCharacterInLine(up)) )
		{
		    actionString = ch.getMaxDamagingSpellId()+":"+ Data.SOUTH;
		    deplacementString = ch.getDeplacement(-1,3);
		}
		return actionString+"!!"+deplacementString;
	}

	public String getActionString() {
		return actionString;
	}

	public void setActionString(String actionString) {
		this.actionString = actionString;
	}

	public String getDeplacementString() {
		return deplacementString;
	}

	public void setDeplacementString(String deplacementString) {
		this.deplacementString = deplacementString;
	}
}
