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
		
		((ch.researchCharacter(up)==null)?defaultInt:ch.researchCharacter(up).Portee(((ch.researchCharacter(up)==null)?defaultString:ch.researchCharacter(up).getMaxHealingSpellId())))>= ((ch.researchCharacter(down)==null)?defaultInt:ch.researchCharacter(down).Portee(((ch.researchCharacter(down)==null)?defaultString:ch.researchCharacter(down).getSpells().get(0).getId()))) 
		if(((ch.researchCharacter(left)==null)?defaultBoolean:ch.researchCharacter(left).isCharacterInLine(down))== ((ch.researchCharacter(right)==null)?defaultBoolean:ch.researchCharacter(right).isCharacterInLine(down)) )
		{
		    deplacementString = ch.getDeplacement(2,-2);
		    deplacementString = ch.getDeplacement(2,2);
		if(((ch.researchCharacter(right)==null)?defaultInt:ch.researchCharacter(right).Portee(((ch.researchCharacter(right)==null)?defaultString:ch.researchCharacter(right).getSpells().get(0).getId())))< ((ch.researchCharacter(down)==null)?defaultInt:ch.researchCharacter(down).getStats().getMaxLife()/5) )
		{
		    deplacementString = ch.getDeplacement(-1,2);
		    actionString = ch.getSpells().get(0).getId()+":"+ Data.SELF;
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
