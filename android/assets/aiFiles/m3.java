//package test;

import com.mygdx.game.data.Data;
import com.mygdx.game.game.*;
import com.mygdx.game.game.Character;

public class m3 {
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
	
	
	public m3() {
	}
	
	public String run(Character ch)
	{
		    deplacementString = ch.getDeplacement(0,3);
		    deplacementString = ch.getDeplacement(0,3);
		if(0.3< ((ch.researchCharacter(right)==null)?defaultFloat:ch.researchCharacter(right).getStats().getLifePercentage()) )
		{
		    actionString = ch.getSpells().get(0).getId()+":"+ Data.EAST;
		    actionString = ch.getSpells().get(0).getId()+":"+ Data.WEST;
		if(((ch.researchCharacter(up)==null)?defaultInt:ch.researchCharacter(up).getStats().getLife())== ((ch.researchCharacter(up)==null)?defaultInt:ch.researchCharacter(up).getStats().getMaxLife()/3) )
		{
		    deplacementString = ch.getDeplacement(-1,-1);
		}
		}
		else
		{
		    deplacementString = ch.getDeplacement(2,-2);
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
