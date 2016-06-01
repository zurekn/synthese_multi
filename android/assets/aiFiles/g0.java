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
		    actionString = ch.getSpells().get(0).getId()+":"+ Data.EAST;
		    actionString = ch.getSpells().get(0).getId()+":"+ Data.SELF;
		if((ch.getStats().getMaxLife()/2)!= (ch.Portee((ch.getMaxDamagingSpellId()))) )
		{
		    actionString = ch.getSpells().get(0).getId()+":"+ Data.SOUTH;
		    actionString = ch.getMaxHealingSpellId()+":"+ Data.NORTH;
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
