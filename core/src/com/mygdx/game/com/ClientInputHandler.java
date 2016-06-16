package com.mygdx.game.com;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.data.Data;
import com.mygdx.game.game.*;

/**
 * Class witch handle all input in the stage
 *
 * Created by nicolas on 09/03/2016.
 */
public class ClientInputHandler extends InputHandler {

   private static final String TAG = "ClientInputHandler";

   @Override
   public boolean touchUp(int screenX, int screenY, int pointer, int button) {
      Gdx.app.log(TAG, "Touch up on ["+screenX+"/"+screenY+"], pointer ["+pointer+"], button ["+button+"]");
      if(click && !Data.autoIA){
         ClientGame.gameStage.checkActionAtPosition(screenX, screenY);
      }
      return false;
   }
}
