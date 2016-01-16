package com.mygdx.game.imageprocessing;

import java.util.EventListener;

public interface MovementListener  extends EventListener{

	void newMovement(MovementEvent e);
	
}

