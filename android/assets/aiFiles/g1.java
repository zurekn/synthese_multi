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
		if(((ch.researchCharacter(up)==null)?defaultInt:ch.researchCharacter(up).getStats().getArmor())<= ((ch.researchCharacter(left)==null)?defaultInt:ch.researchCharacter(left).getStats().getMaxMana()) )
		{
		    deplacementString = ch.getDeplacement(-1,-1);
		    deplacementString = ch.getDeplacement(-1,0);
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
