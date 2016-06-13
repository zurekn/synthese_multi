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
		    actionString = ch.getMaxHealingSpellId()+":"+ Data.SOUTH;
		    deplacementString = ch.getDeplacement(3,-2);
		if(((ch.researchCharacter(right)==null)?defaultInt:ch.researchCharacter(right).getStats().getMaxLife()/2)<= ((ch.researchCharacter(right)==null)?defaultInt:ch.researchCharacter(right).getStats().getMagicResist()) )
		{
		    deplacementString = ch.getDeplacement(-1,2);
		}
		else
		{
		if(true== ((ch.researchCharacter(right)==null)?defaultBoolean:ch.researchCharacter(right).isCharacterInLine(down)) )
		{
		    actionString = ch.getMaxDamagingSpellId()+":"+ Data.SOUTH;
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
