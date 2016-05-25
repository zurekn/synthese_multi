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
		    deplacementString = ch.getDeplacement(-1,-2);
		    deplacementString = ch.getDeplacement(1,2);
		if(((ch.researchCharacter(up)==null)?defaultFloat:ch.researchCharacter(up).getStats().getLifePercentage())== (ch.getStats().getManaPercentage()) )
		{
		    deplacementString = ch.getDeplacement(-2,3);
		if(((ch.researchCharacter(up)==null)?defaultInt:ch.researchCharacter(up).getStats().getMaxMana()/5)> ((ch.researchCharacter(left)==null)?defaultInt:ch.researchCharacter(left).Portee(((ch.researchCharacter(left)==null)?defaultString:ch.researchCharacter(left).getMaxHealingSpellId()))) )
		{
		    actionString = ch.getMaxHealingSpellId()+":"+ Data.WEST;
		}
		}
		else
		{
		if(0.3> ((ch.researchCharacter(right)==null)?defaultFloat:ch.researchCharacter(right).getStats().getManaPercentage()) )
		{
		    deplacementString = ch.getDeplacement(2,-2);
		}
		else
		{
		    deplacementString = ch.getDeplacement(-2,0);
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
