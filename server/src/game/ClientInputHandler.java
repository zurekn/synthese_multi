package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.data.Data;
import com.mygdx.game.exception.IllegalActionException;
import com.mygdx.game.exception.IllegalCaracterClassException;
import com.mygdx.game.exception.IllegalMovementException;
import com.mygdx.game.game.*;
import com.mygdx.game.game.Character;

import java.util.Random;

import static com.mygdx.game.data.Data.BLOCK_NUMBER_X;
import static com.mygdx.game.data.Data.BLOCK_NUMBER_Y;
import static com.mygdx.game.data.Data.EAST;
import static com.mygdx.game.data.Data.NORTH;
import static com.mygdx.game.data.Data.SELF;
import static com.mygdx.game.data.Data.SOUTH;
import static com.mygdx.game.data.Data.WEST;
import static com.mygdx.game.data.Data.debug;

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
