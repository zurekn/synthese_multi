//package test;

import com.mygdx.game.data.Data;
import com.mygdx.game.game.*;
import com.mygdx.game.game.Character;

public class g3 {
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
	
	
	public g3() {
	}
	
	public String run(Character ch)
	{
		    deplacementString = ch.getDeplacement(3,0);
		    deplacementString = ch.getDeplacement(1,-2);
		if(false== ((ch.researchCharacter(left)==null)?defaultBoolean:ch.researchCharacter(left).isCharacterInLine(up)) )
		{
		    deplacementString = ch.getDeplacement(-1,2);
		    deplacementString = ch.getDeplacement(-1,3);
		if(false== ((ch.researchCharacter(up)==null)?defaultBoolean:ch.researchCharacter(up).isCharacterInLine(up)) )
		{
		    deplacementString = ch.getDeplacement(2,1);
		    deplacementString = ch.getDeplacement(0,-2);
		}
		else
		{
		}
		}
		else
		{
		if(0.3== ((ch.researchCharacter(left)==null)?defaultFloat:ch.researchCharacter(left).getStats().getManaPercentage()) )
		{
		    deplacementString = ch.getDeplacement(-1,-1);
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
