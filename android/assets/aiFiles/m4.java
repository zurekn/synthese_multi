//package test;

import com.mygdx.game.data.Data;
import com.mygdx.game.game.*;
import com.mygdx.game.game.Character;

public class m4 {
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
	
	
	public m4() {
	}
	
	public String run(Character ch)
	{
		    deplacementString = ch.getDeplacement(2,-1);
		if(0.3< ((ch.researchCharacter(up)==null)?defaultFloat:ch.researchCharacter(up).getStats().getManaPercentage()) )
		{
		    actionString = ch.getMaxDamagingSpellId()+":"+ Data.SELF;
		}
		else
		{
		    deplacementString = ch.getDeplacement(1,-2);
		if(((ch.researchCharacter(left)==null)?defaultInt:ch.researchCharacter(left).getStats().getMaxMana()/5)> ((ch.researchCharacter(left)==null)?defaultInt:ch.researchCharacter(left).getStats().getStrength()) )
		{
		    actionString = ch.getMaxHealingSpellId()+":"+ Data.SELF;
		    deplacementString = ch.getDeplacement(1,0);
		}
		else
		{
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
