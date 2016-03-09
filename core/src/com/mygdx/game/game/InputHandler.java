package com.mygdx.game.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
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
 * Created by nicolas on 09/03/2016.
 */
public class InputHandler implements InputProcessor {

    private WindowGame windowGame = WindowGame.getInstance();

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
                        if (Input.Keys.UP == keycode)
                            windowGame.decodeAction("m:" + currentCharacter.getX() + ":" + (currentCharacter.getY() - 1));
                        if (Input.Keys.DOWN == keycode)
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
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
