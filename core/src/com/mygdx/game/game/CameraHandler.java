package com.mygdx.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.data.Data;

/**
 * Created by nicolas on 15/03/2016.
 */
public class CameraHandler extends OrthographicCamera {

    private static final String TAG = "CameraHandler";

    public static float initialScale = 1.0f;
    public static final float MAX_ZOOM = 0.2f;
    public static final float MIN_ZOOM = 2f;
    private Viewport viewPort;

    private boolean movementOn = true;

    public void init() {
        this.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //camera.position.set((Data.SCREEN_WIDTH / 2) - Data.MAP_X,(Data.SCREEN_HEIGHT / 2) - Data.MAP_Y, 0);
        int camX = Data.MAP_WIDTH / 2; // center the camera on the map
        int camY =  Data.MAP_HEIGHT / 2; // center the camera on the map

        this.position.set(camX, camY,0);
        Gdx.app.log(TAG,"Create camera at ["+camX+"/"+camY+"]");

        viewPort= new FitViewport(Data.SCREEN_WIDTH, Data.SCREEN_HEIGHT, this);
        this.update();
    }

    public void resize(int width, int height){
        viewPort.update(width, height);
    }

    public void moveCamera(int touchX, int touchY) {
        if(movementOn) {
            Vector3 v = new Vector3(touchX, -touchY, 0);

            //Gdx.app.log(TAG, "Translate camera of [" + v.x + "/" + v.y + "] from [" + this.position.x + "/" + this.position.y + "]");
            if (!cameraOutOfLimit(v)) {
                this.translate(v);
                Data.MAP_X = (Data.SCREEN_WIDTH / 2) - position.x;
                Data.MAP_Y = (Data.SCREEN_HEIGHT / 2) - position.y;
            }
        }
    }

    private boolean cameraOutOfLimit(Vector3 v) {
        Gdx.app.log(TAG, ""+(position.x)+" / "+(position.y));
        int left = (int) (position.x - v.x - Data.MAP_WIDTH * zoom/2);
        int right = (int) (position.x - v.x + Data.MAP_WIDTH * zoom/ 2);
        int top = (int) (position.y - v.y - Data.MAP_HEIGHT * zoom/ 2);
        int bottom = (int) (position.y - v.y +Data.MAP_HEIGHT * zoom/2);
        return false;
    }


}