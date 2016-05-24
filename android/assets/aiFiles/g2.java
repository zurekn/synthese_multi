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
		    deplacementString = ch.getDeplacement(-2,2);
		    deplacementString = ch.getDeplacement(-2,1);
		if(((ch.researchCharacter(left)==null)?defaultInt:ch.researchCharacter(left).getStats().getMaxMana()/2)< (ch.getStats().getMaxLife()) )
		{
		    deplacementString = ch.getDeplacement(1,3);
		    deplacementString = ch.getDeplacement(-2,3);
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
