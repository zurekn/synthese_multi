//package test;

import com.mygdx.game.data.Data;
import com.mygdx.game.game.*;
import com.mygdx.game.game.Character;

public class g2 {
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
	
	
	public g2() {
	}
	
	public String run(Character ch)
	{
		if(false== ((ch.researchCharacter(right)==null)?defaultBoolean:ch.researchCharacter(right).isCharacterInLine(down)) )
		{
		    actionString = ch.getSpells().get(0).getId()+":"+ Data.SELF;
		    actionString = ch.getMaxDamagingSpellId()+":"+ Data.EAST;
		}
		if(((ch.researchCharacter(left)==null)?defaultInt:ch.researchCharacter(left).getStats().getArmor())== ((ch.researchCharacter(up)==null)?defaultInt:ch.researchCharacter(up).Portee(((ch.researchCharacter(up)==null)?defaultString:ch.researchCharacter(up).getMaxDamagingSpellId()))) )
		{
		    deplacementString = ch.getDeplacement(0,3);
		}
		else
		{
		if(((ch.researchCharacter(left)==null)?defaultBoolean:ch.researchCharacter(left).isCharacterInLine(down))== true )
		{
		    actionString = ch.getMaxDamagingSpellId()+":"+ Data.NORTH;
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
