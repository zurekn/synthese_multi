package com.mygdx.game.hud;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.data.Data;
import com.mygdx.game.game.GameStage;
import com.mygdx.game.game.Player;

/**
 * Created by nicolas on 23/03/2016.
 */
public class PlayerHUD extends Actor {


    /* sprite used by the HUD*/
    private TextureRegion heartFull, heartHalf, heartEmpty;

    /* fast pointer on the currentPlayer*/
    private Player player;

    /* game stage  */
    private GameStage gameStage;

    /* dynamic and static messages*/
    private Array<Message> messages = new Array<Message>();

    /* Message padding*/
    private int padding = 10;

    /* For the buttons positioning */
    private Table table = new Table();

    /* game font */
    private BitmapFont font = Data.font;

    /* the fuel bar*/
    //private ProgressBar fuelBar = new ProgressBar(0f, player.getMaxFuel(), 0.1f, true, SKIN);
    //TODO find the progress bar skin needed

    public PlayerHUD(GameStage gameStage, Player player) {
        super();
        this.gameStage = gameStage;
        this.player = player;

    }

    @Override
    public void draw(Batch batch, float parentDelta) {

        /* display all messages */
            Message m;
            for (int i = 0; i < messages.size; i++) {
                m = messages.get(i);
                m.draw(batch, parentDelta);
                //font.draw(batch, m.getMessage(), (V_WIDTH / 2) - ((m.getMessage().length() / 2) * font.getXHeight()), 200 + m.getY() );
            }


    }

    @Override
    public void act(float delta) {
        super.act(delta);
        int i = 0;
        while (i < messages.size && i >= 0) {
            messages.get(i).act(delta);
            if (messages.get(i).isEnded(delta)) {
                /* remove the message if it reach time duration */
                messages.get(i).remove();
                messages.removeIndex(i);
                i--;
            }
            i++;
        }
    }

    public void pushMessage(Message m) {
        messages.add(m);
    }

    public void showEndMenu() {
        table.setVisible(true);
    }
}
