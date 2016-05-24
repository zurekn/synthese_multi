package com.mygdx.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.data.Data;
import com.mygdx.game.exception.IllegalActionException;
import com.mygdx.game.exception.IllegalCaracterClassException;
import com.mygdx.game.exception.IllegalMovementException;

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
public class InputHandler implements InputProcessor, GestureDetector.GestureListener {

    private static final String TAG = "InputHandler";

    private Vector3 lastTouch = new Vector3();

    private boolean click = false;

    @Override
    public boolean keyDown(int keycode) {
        GameStage stage = GameStage.gameStage;
        if (debug) {
            Character currentCharacter = stage.getCurrentCharacter();
            if (stage.gameOn) {

                if (!currentCharacter.isNpc()) {
                    System.out.println("InputHandler, keycodeDownn : " + keycode);
                    try {
                        if (Input.Keys.LEFT == keycode)
                            stage.decodeAction("m:" + (currentCharacter.getX() - 1) + ":" + currentCharacter.getY());
                        if (Input.Keys.RIGHT == keycode)
                            stage.decodeAction("m:" + (currentCharacter.getX() + 1) + ":" + currentCharacter.getY());
                        if (Input.Keys.DOWN == keycode)
                            stage.decodeAction("m:" + currentCharacter.getX() + ":" + (currentCharacter.getY() - 1));
                        if (Input.Keys.UP == keycode)
                            stage.decodeAction("m:" + currentCharacter.getX() + ":" + (currentCharacter.getY() + 1));
                        if (Input.Keys.NUMPAD_8 == keycode)
                            stage.decodeAction("s2:" + NORTH);
                        if (Input.Keys.NUMPAD_6 == keycode)
                            stage.decodeAction("s2:" + EAST);
                        if (Input.Keys.NUMPAD_2 == keycode)
                            stage.decodeAction("s2:" + SOUTH);
                        if (Input.Keys.NUMPAD_4 == keycode)
                            stage.decodeAction("s2:" + WEST);
                        if (Input.Keys.NUMPAD_5 == keycode)
                            stage.decodeAction("s2:" + SELF);
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
    public boolean keyUp(int keycodecode) {

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //Gdx.app.log(TAG, "Touch down on ["+screenX+"/"+screenY+"], pointer ["+pointer+"], button ["+button+"]");
        lastTouch.set(screenX, screenY, 0);

        click = true;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Gdx.app.log(TAG, "Touch up on ["+screenX+"/"+screenY+"], pointer ["+pointer+"], button ["+button+"]");
        if(click && !Data.autoIA){
            GameStage.gameStage.checkActionAtPosition(screenX, screenY);
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        //Gdx.app.log(TAG, "Touch dragged on ["+screenX+"/"+screenY+"], pointer ["+pointer+"]");
        GameStage.gameStage.getCamera().moveCamera((int) (lastTouch.x - screenX), (int) (lastTouch.y - screenY));
        click = false;
        lastTouch.set(screenX, screenY, 0);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
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
    public boolean touchDown(float x, float y, int pointer, int button) {
        Gdx.app.log(TAG, "Touch down on ["+x+"/"+y+"], pointer ["+pointer+"], button ["+button+"]");
        lastTouch.set(x, y, 0);
        click = true;
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Gdx.app.log(TAG, "Tap on ["+x+"/"+y+"], count ["+count+"], button ["+button+"]");
        GameStage.gameStage.checkActionAtPosition((int)x, (int)y);
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        Gdx.app.log(TAG, "Long press on ["+x+"/"+y+"]");
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        Gdx.app.log(TAG, "Fling on ["+velocityX+"/"+velocityY+"], button ["+button+"]");
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        Gdx.app.log(TAG, "Pan on ["+x+"/"+y+"], delta ["+deltaX+"/"+deltaY+"]");

        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        Gdx.app.log(TAG, "Pan stop on ["+x+"/"+y+"], pointer ["+pointer+"], button ["+button+"]");

        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        GameStage stage = GameStage.gameStage;
        float ratio = initialDistance / distance; //I get this
        float newZoom = stage.getCamera().zoom * ratio;
        if(newZoom < CameraHandler.MIN_ZOOM){
            stage.getCamera().zoom = CameraHandler.MIN_ZOOM;
        }
        else if(newZoom > CameraHandler.MAX_ZOOM ){
            stage.getCamera().zoom = CameraHandler.MAX_ZOOM;
        }

        stage.getCamera().zoom = newZoom; //This doesn't make sense to me because it seems like every time you pinch to zoom, it resets to the original zoom which is 1. So basically it wouldn't 'save' the zoom right?
        stage.getCamera().reloadMapPosition();
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        Gdx.app.log(TAG, "Pinch on ");

        return false;
    }
}
