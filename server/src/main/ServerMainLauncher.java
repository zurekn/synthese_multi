package main;

import com.mygdx.game.com.ServerMain;
import com.mygdx.game.data.Data;

public class ServerMainLauncher {
    public static void main (String[] arg) {
        Data.loadProperties();

        ServerMain server = new ServerMain();
    }
}
