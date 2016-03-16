package com.mygdx.game.game;

import java.util.Scanner;

import com.mygdx.game.data.Data;

/**
 * Used for the debug fonction, 
 * The class wait the input from keybord for a movement
 * @author bob
 *
 */
public class MovementHandler implements Runnable {

	// For debug
	private Scanner sc;
	private String str;

	private GameStage stage;

	public MovementHandler(GameStage stage) {
		this.stage = stage;
		init();
	}

	public void init() {
		if (Data.debug) {
			sc = new Scanner(System.in);
		}
	}

	private String randomMove() {
		String s = "";

		return s;
	}

	public void run() {
		while (true) {
			if (Data.debug) {

				str = sc.nextLine();
				stage.move(str);
			}
		}

	}

}
