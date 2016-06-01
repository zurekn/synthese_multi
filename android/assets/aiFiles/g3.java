//package test;

import com.mygdx.game.data.Data;
import com.mygdx.game.game.*;
import com.mygdx.game.game.Character;

public class g3 {
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
	
	
	public g3() {
	}
	
	public String run(Character ch)
	{
		    deplacementString = ch.getDeplacement(-1,2);
		    deplacementString = ch.getDeplacement(-2,-1);
		if(true== (ch.isCharacterInLine(left)) )
		{
		    actionString = ch.getMaxHealingSpellId()+":"+ Data.EAST;
		if(true== ((ch.researchCharacter(down)==null)?defaultBoolean:ch.researchCharacter(down).isCharacterInLine(up)) )
		{
		    deplacementString = ch.getDeplacement(-1,0);
		    actionString = ch.passerTour();
		}
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
