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
		    actionString = ch.getSpells().get(0).getId()+":"+ Data.NORTH;
		if((ch.isCharacterInLine(left))== true )
		{
		    actionString = ch.getSpells().get(0).getId()+":"+ Data.SOUTH;
		    deplacementString = ch.getDeplacement(0,2);
		if(((ch.researchCharacter(up)==null)?defaultInt:ch.researchCharacter(up).getStats().getMaxLife()/5)< ((ch.researchCharacter(down)==null)?defaultInt:ch.researchCharacter(down).getStats().getMaxLife()/2) )
		{
		    actionString = ch.getMaxDamagingSpellId()+":"+ Data.SOUTH;
		    actionString = ch.getSpells().get(0).getId()+":"+ Data.SOUTH;
		}
		}
		else
		{
		    deplacementString = ch.getDeplacement(3,0);
		if((ch.getStats().getMaxMana())<= ((ch.researchCharacter(right)==null)?defaultInt:ch.researchCharacter(right).getStats().getLife()) )
		{
		    actionString = ch.getSpells().get(0).getId()+":"+ Data.EAST;
		    deplacementString = ch.getDeplacement(0,-1);
		}
		else
		{
		    actionString = ch.getMaxHealingSpellId()+":"+ Data.SOUTH;
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
