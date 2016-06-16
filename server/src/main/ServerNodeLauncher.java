package main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.com.ServerNode;
import com.mygdx.game.data.Data;

public class ServerNodeLauncher {
    public static void main(String[] arg) {
        Data.loadProperties();

        Data.setForServer();

        Data.checkValuesIni("paramTI.ini");

        try{
            ServerNode.getInstance();
        } catch(NumberFormatException e){
            System.err.println("Invalid port format, must be a number");
        } catch(IndexOutOfBoundsException e){
            System.err.println("Format: YourJar.jar ip_address port");
        }

    }
}