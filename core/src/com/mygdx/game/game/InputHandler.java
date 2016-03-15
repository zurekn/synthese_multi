package com.mygdx.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputListener;
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
 * Class witch handle all input in the WindowGame
 *
 * Created by nicolas on 09/03/2016.
 */
public class InputHandler implements InputProcessor, GestureDetector.GestureListener {

    private WindowGame windowGame = WindowGame.getInstance();

    private static final String TAG = "InputHandler";

    private Vector3 lastTouch = new Vector3();
    @Override
    public boolean keyDown(int keycode) {
        if (debug) {
            Character currentCharacter = windowGame.getCurrentCharacter();
            if (windowGame.gameOn) {

                if (!currentCharacter.isNpc()) {
                    System.out.println("InputHandler, keycodeDownn : " + keycode);
                    try {
                        if (Input.Keys.LEFT == keycode)
                            windowGame.decodeAction("m:" + (currentCharacter.getX() - 1) + ":" + currentCharacter.getY());
                        if (Input.Keys.RIGHT == keycode)
                            windowGame.decodeAction("m:" + (currentCharacter.getX() + 1) + ":" + currentCharacter.getY());
                        if (Input.Keys.DOWN == keycode)
                            windowGame.decodeAction("m:" + currentCharacter.getX() + ":" + (currentCharacter.getY() - 1));
                        if (Input.Keys.UP == keycode)
                            windowGame.decodeAction("m:" + currentCharacter.getX() + ":" + (currentCharacter.getY() + 1));
                        if (Input.Keys.NUMPAD_8 == keycode)
                            windowGame.decodeAction("s3:" + NORTH);
                        if (Input.Keys.NUMPAD_6 == keycode)
                            windowGame.decodeAction("s9:" + EAST);
                        if (Input.Keys.NUMPAD_2 == keycode)
                            windowGame.decodeAction("s10:" + SOUTH);
                        if (Input.Keys.NUMPAD_4 == keycode)
                            windowGame.decodeAction("s4:" + WEST);
                        if (Input.Keys.NUMPAD_5 == keycode)
                            windowGame.decodeAction("s1:" + SELF);
                    } catch (IllegalActionException e) {
                        System.err.println(e.getMessage());
                    }
                }
            }
            if (Input.Keys.SLASH == keycode) {
                currentCharacter.takeDamage(20, "magic");
            }
            if (Input.Keys.MINUS == keycode) {
                windowGame.start();
            }
            if (Input.Keys.PLUS == keycode) {
                try {
                    Random rand = new Random();
                    int x = rand.nextInt(BLOCK_NUMBER_X - 0) + 0;
                    int y = rand.nextInt(BLOCK_NUMBER_Y - 0) + 0;
                    windowGame.addChalenger(x, y, -1);
                } catch (IllegalCaracterClassException e) {
                    e.printStackTrace();
                } catch (IllegalMovementException e) {
                    e.printStackTrace();
                } catch (IllegalActionException e) {
                    e.printStackTrace();
                }
            }

            if (Input.Keys.L == keycode) {
                windowGame.gameEnded = true;
                windowGame.gameLose = true;
                windowGame.stopAllThread();
            }

            if (Input.Keys.W == keycode) {
                windowGame.gameEnded = true;
                windowGame.gameWin = true;
                windowGame.stopAllThread();
            }


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
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        //Gdx.app.log(TAG, "Touch up on ["+screenX+"/"+screenY+"], pointer ["+pointer+"], button ["+button+"]");

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        //Gdx.app.log(TAG, "Touch dragged on ["+screenX+"/"+screenY+"], pointer ["+pointer+"]");
        WindowGame.getInstance().getCamera().moveCamera((int) (lastTouch.x - screenX), (int) (lastTouch.y - screenY));
        lastTouch.set(screenX, screenY, 0);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        if(amount == 1 && WindowGame.getInstance().getCamera().zoom - 0.2f < CameraHandler.MIN_ZOOM){
            WindowGame.getInstance().getCamera().zoom += .2f;
        }
        else if(amount == -1 && WindowGame.getInstance().getCamera().zoom - 0.2f > CameraHandler.MAX_ZOOM ){
            WindowGame.getInstance().getCamera().zoom -= .2f;
        }
        return false;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Gdx.app.log(TAG, "Touch down on ["+x+"/"+y+"], pointer ["+pointer+"], button ["+button+"]");
        lastTouch.set(x, y, 0);
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Gdx.app.log(TAG, "Tap on ["+x+"/"+y+"], count ["+count+"], button ["+button+"]");
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
        float ratio = initialDistance / distance; //I get this
        WindowGame.getInstance().getCamera().zoom = CameraHandler.initialScale * ratio; //This doesn't make sense to me because it seems like every time you pinch to zoom, it resets to the original zoom which is 1. So basically it wouldn't 'save' the zoom right?
        System.out.println(WindowGame.getInstance().getCamera().zoom); //Prints the camera zoom
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        Gdx.app.log(TAG, "Pinch on ");

        return false;
    }
}
