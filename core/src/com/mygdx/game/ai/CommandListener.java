package com.mygdx.game.ai;

import java.util.EventListener;

public interface CommandListener extends EventListener{
	
	void newAction(ActionEvent e);
}
