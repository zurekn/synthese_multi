package main;

import com.mygdx.game.com.ServerNode;
import com.mygdx.game.data.Data;

public class ServerNodeLauncher {
    public static void main(String[] arg) {
        Data.loadProperties();

        Data.setForServer();

        Data.checkValuesIni("paramTI.ini");

        try{
            boolean ok = false;
            int port  = Integer.parseInt(arg[1]);
            do {
                try {
                    ServerNode.getInstance(arg[0], port);
                }catch(Exception e){
                    port++;
                }
            }while(!ok);
            while(ServerNode.getInstance().getThread().isAlive()){};
        } catch(NumberFormatException e){
            System.err.println("Invalid port format, must be a number");
        } catch(IndexOutOfBoundsException e){
            System.err.println("Format: YourJar.jar ip_address port");
        }

    }
}