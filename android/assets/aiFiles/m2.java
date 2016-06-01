//package test;

import com.mygdx.game.data.Data;
import com.mygdx.game.game.*;
import com.mygdx.game.game.Character;

public class m2 {
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
	
	
	public m2() {
	}
	
	public String run(Character ch)
	{
		    deplacementString = ch.getDeplacement(0,-2);
		if(0.2== ((ch.researchCharacter(left)==null)?defaultFloat:ch.researchCharacter(left).getStats().getManaPercentage()) )
		{
		    actionString = ch.getSpells().get(0).getId()+":"+ Data.WEST;
		if(true== ((ch.researchCharacter(up)==null)?defaultBoolean:ch.researchCharacter(up).isCharacterInLine(right)) )
		{
		    deplacementString = ch.getDeplacement(1,-1);
		}
		else
		{
		    actionString = ch.getMaxHealingSpellId()+":"+ Data.SELF;
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
