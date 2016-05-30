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
		    deplacementString = ch.getDeplacement(-2,1);
		    deplacementString = ch.getDeplacement(0,-2);
		if(1== ((ch.researchCharacter(down)==null)?defaultFloat:ch.researchCharacter(down).getStats().getLifePercentage()) )
		{
		    deplacementString = ch.getDeplacement(2,0);
		if(((ch.researchCharacter(down)==null)?defaultInt:ch.researchCharacter(down).Portee(((ch.researchCharacter(down)==null)?defaultString:ch.researchCharacter(down).getSpells().get(0).getId())))<= ((ch.researchCharacter(right)==null)?defaultInt:ch.researchCharacter(right).getStats().getMaxMana()) )
		{
		    deplacementString = ch.getDeplacement(-1,2);
		}
		}
		else
		{
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
