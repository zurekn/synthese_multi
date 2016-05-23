package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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

    private Vector3 lastTouch = new Vector3();
    @Override
    public boolean keyDown(int keycode) {
        ClientGame stage = ClientGame.gameStage;
        if (debug) {
            Character currentCharacter = stage.getCurrentCharacter();
            if (stage.gameOn) {

                if (!currentCharacter.isNpc()) {
                    System.out.println("InputHandler, keycodeDownn : " + keycode);
                    try {
                        if (Input.Keys.LEFT == keycode)
                            stage.checkAction("m:" + (currentCharacter.getX() - 1) + ":" + currentCharacter.getY());
                        if (Input.Keys.RIGHT == keycode)
                            stage.checkAction("m:" + (currentCharacter.getX() + 1) + ":" + currentCharacter.getY());
                        if (Input.Keys.DOWN == keycode)
                            stage.checkAction("m:" + currentCharacter.getX() + ":" + (currentCharacter.getY() - 1));
                        if (Input.Keys.UP == keycode)
                            stage.checkAction("m:" + currentCharacter.getX() + ":" + (currentCharacter.getY() + 1));
                        if (Input.Keys.NUMPAD_8 == keycode)
                            stage.checkAction("s3:" + NORTH);
                        if (Input.Keys.NUMPAD_6 == keycode)
                            stage.checkAction("s9:" + EAST);
                        if (Input.Keys.NUMPAD_2 == keycode)
                            stage.checkAction("s10:" + SOUTH);
                        if (Input.Keys.NUMPAD_4 == keycode)
                            stage.checkAction("s4:" + WEST);
                        if (Input.Keys.NUMPAD_5 == keycode)
                            stage.checkAction("s1:" + SELF);
                    } catch (IllegalActionException e) {
                        System.err.println(e.getMessage());
                    }
                }
            }
            if (Input.Keys.SLASH == keycode) {
                currentCharacter.takeDamage(20, "magic");
            }
            if (Input.Keys.MINUS == keycode) {
                stage.start();
            }
            if (Input.Keys.PLUS == keycode) {
                try {
                    Random rand = new Random();
                    int x = rand.nextInt(BLOCK_NUMBER_X - 0) + 0;
                    int y = rand.nextInt(BLOCK_NUMBER_Y - 0) + 0;
                    stage.addChalenger(x, y, -1);
                } catch (IllegalCaracterClassException e) {
                    e.printStackTrace();
                } catch (IllegalMovementException e) {
                    e.printStackTrace();
                } catch (IllegalActionException e) {
                    e.printStackTrace();
                }
            }

            if (Input.Keys.L == keycode) {
                stage.gameEnded = true;
                stage.gameLose = true;
                stage.stopAllThread();
            }

            if (Input.Keys.W == keycode) {
                stage.gameEnded = true;
                stage.gameWin = true;
                stage.stopAllThread();
            }
        }
        if(Input.Keys.ESCAPE == keycode){
            Gdx.app.exit();
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Gdx.app.log(TAG, "Touch dragged on ["+screenX+"/"+screenY+"], pointer ["+pointer+"]");
        ClientGame.gameStage.getCamera().moveCamera((int) (lastTouch.x - screenX), (int) (lastTouch.y - screenY));
        lastTouch.set(screenX, screenY, 0);
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        GameStage stage = GameStage.gameStage;
        if(amount == 1 && stage.getCamera().zoom - 0.2f < CameraHandler.MIN_ZOOM){
            stage.getCamera().zoom += .2f;
        }
        else if(amount == -1 && stage.getCamera().zoom - 0.2f > CameraHandler.MAX_ZOOM ){
            stage.getCamera().zoom -= .2f;
        }
        stage.getCamera().reloadMapPosition();
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        GameStage stage = GameStage.gameStage;
        float ratio = initialDistance / distance; //I get this
        stage.getCamera().zoom = CameraHandler.initialScale * ratio; //This doesn't make sense to me because it seems like every time you pinch to zoom, it resets to the original zoom which is 1. So basically it wouldn't 'save' the zoom right?
        System.out.println(stage.getCamera().zoom); //Prints the camera zoom
        return false;
    }
}
