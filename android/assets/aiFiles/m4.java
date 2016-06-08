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
		    deplacementString = ch.getDeplacement(-1,-2);
		if(0.5>= ((ch.researchCharacter(up)==null)?defaultFloat:ch.researchCharacter(up).getStats().getLifePercentage()) )
		{
		    deplacementString = ch.getDeplacement(-2,-2);
		if(((ch.researchCharacter(down)==null)?defaultInt:ch.researchCharacter(down).getStats().getMagicResist())== ((ch.researchCharacter(up)==null)?defaultInt:ch.researchCharacter(up).Portee(((ch.researchCharacter(up)==null)?defaultString:ch.researchCharacter(up).getSpells().get(0).getId()))) )
		{
		    deplacementString = ch.getDeplacement(1,3);
		}
		else
		{
		}
		}
		else
		{
		    actionString = ch.getMaxDamagingSpellId()+":"+ Data.SELF;
		if(0.2== (ch.getStats().getLifePercentage()) )
		{
		    deplacementString = ch.getDeplacement(-2,2);
		    deplacementString = ch.getDeplacement(-1,3);
		}
		else
		{
		    deplacementString = ch.getDeplacement(3,0);
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
