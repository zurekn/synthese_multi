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
		    actionString = ch.getMaxHealingSpellId()+":"+ Data.EAST;
		if(false== ((ch.researchCharacter(right)==null)?defaultBoolean:ch.researchCharacter(right).isCharacterInLine(up)) )
		{
		    actionString = ch.getMaxDamagingSpellId()+":"+ Data.WEST;
		    actionString = ch.passerTour();
		if(false== ((ch.researchCharacter(left)==null)?defaultBoolean:ch.researchCharacter(left).isCharacterInLine(left)) )
		{
		    deplacementString = ch.getDeplacement(2,2);
		    deplacementString = ch.getDeplacement(2,0);
		}
		}
		else
		{
		    actionString = ch.getMaxDamagingSpellId()+":"+ Data.SOUTH;
		if(((ch.researchCharacter(down)==null)?defaultInt:ch.researchCharacter(down).getStats().getMagicPower())== ((ch.researchCharacter(right)==null)?defaultInt:ch.researchCharacter(right).getStats().getMaxMana()/2) )
		{
		    deplacementString = ch.getDeplacement(0,3);
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
