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
		    deplacementString = ch.getDeplacement(0,2);
		if(0.5!= ((ch.researchCharacter(up)==null)?defaultFloat:ch.researchCharacter(up).getStats().getManaPercentage()) )
		{
		    deplacementString = ch.getDeplacement(-1,2);
		    actionString = ch.getSpells().get(0).getId()+":"+ Data.SOUTH;
		if(1> ((ch.researchCharacter(up)==null)?defaultFloat:ch.researchCharacter(up).getStats().getManaPercentage()) )
		{
		    actionString = ch.getSpells().get(0).getId()+":"+ Data.NORTH;
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
