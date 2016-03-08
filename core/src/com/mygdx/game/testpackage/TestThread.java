package com.mygdx.game.testpackage;

import com.mygdx.game.ai.AIHandler;

public class TestThread {

	public static void main(String[] args) {
		AIHandler ai = AIHandler.getInstance();
		ai.begin();
	}

}
