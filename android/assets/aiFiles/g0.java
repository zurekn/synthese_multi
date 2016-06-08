//package test;

import com.mygdx.game.data.Data;
import com.mygdx.game.game.*;
import com.mygdx.game.game.Character;

public class g0 {
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
	
	
	public g0() {
	}
	
	public String run(Character ch)
	{
		    deplacementString = ch.getDeplacement(-1,-1);
		if(((ch.researchCharacter(up)==null)?defaultInt:ch.researchCharacter(up).Portee(((ch.researchCharacter(up)==null)?defaultString:ch.researchCharacter(up).getMaxHealingSpellId())))>= ((ch.researchCharacter(down)==null)?defaultInt:ch.researchCharacter(down).Portee(((ch.researchCharacter(down)==null)?defaultString:ch.researchCharacter(down).getSpells().get(0).getId()))) )
		{
		    deplacementString = ch.getDeplacement(1,2);
		if(1<= ((ch.researchCharacter(down)==null)?defaultFloat:ch.researchCharacter(down).getStats().getManaPercentage()) )
		{
		    actionString = ch.getSpells().get(0).getId()+":"+ Data.NORTH;
		}
		}
		else
		{
		    actionString = ch.getSpells().get(0).getId()+":"+ Data.WEST;
		if(1> ((ch.researchCharacter(up)==null)?defaultFloat:ch.researchCharacter(up).getStats().getLifePercentage()) )
		{
		    actionString = ch.getSpells().get(0).getId()+":"+ Data.WEST;
		    deplacementString = ch.getDeplacement(3,-1);
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
