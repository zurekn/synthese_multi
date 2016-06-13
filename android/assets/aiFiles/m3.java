//package test;

import com.mygdx.game.data.Data;
import com.mygdx.game.game.*;
import com.mygdx.game.game.Character;

public class m3 {
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
	
	
	public m3() {
	}
	
	public String run(Character ch)
	{
		    deplacementString = ch.getDeplacement(3,1);
		    actionString = ch.getMaxHealingSpellId()+":"+ Data.WEST;
		if(((ch.researchCharacter(down)==null)?defaultBoolean:ch.researchCharacter(down).isCharacterInLine(left))== false )
		{
		    deplacementString = ch.getDeplacement(3,0);
		    deplacementString = ch.getDeplacement(2,0);
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
