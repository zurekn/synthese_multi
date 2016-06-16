package main;

import com.mygdx.game.com.Server;

import java.io.File;

public class ServerLauncher {

	public static void main (String[] arg) {
		File toto = new File("arborescenceQuiFaitChier.txt");
		System.out.println(toto.getAbsolutePath());

		new Server();
	}
}
