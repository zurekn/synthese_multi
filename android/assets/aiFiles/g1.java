//package test;

import com.mygdx.game.data.Data;
import com.mygdx.game.game.*;
import com.mygdx.game.game.Character;

public class g1 {
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
	
	
	public g1() {
	}
	
	public String run(Character ch)
	{
		    actionString = ch.getMaxDamagingSpellId()+":"+ Data.SOUTH;
		if(((ch.researchCharacter(left)==null)?defaultInt:ch.researchCharacter(left).getStats().getMagicResist())!= (ch.getStats().getMaxMana()/5) )
		{
		    deplacementString = ch.getDeplacement(2,-2);
		}
		else
		{
		if(((ch.researchCharacter(left)==null)?defaultFloat:ch.researchCharacter(left).getStats().getManaPercentage())<= ((ch.researchCharacter(right)==null)?defaultFloat:ch.researchCharacter(right).getStats().getLifePercentage()) )
		{
		    deplacementString = ch.getDeplacement(0,3);
		}
		else
		{
		    actionString = ch.getMaxHealingSpellId()+":"+ Data.EAST;
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
