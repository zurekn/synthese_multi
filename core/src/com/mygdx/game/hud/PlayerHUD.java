package com.mygdx.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.data.Data;
import com.mygdx.game.data.SpellData;
import com.mygdx.game.game.GameStage;
import com.mygdx.game.game.Player;
import com.mygdx.game.game.Spell;

/**
 * Created by nicolas on 23/03/2016.
 */
public class PlayerHUD {
    private static final String TAG = "HUD";
    /* fast pointer on the currentPlayer*/
    private Player player;

    /* game stage  */
    private GameStage gameStage;

    /* Message padding*/
    private int padding = 10;

    /* For the buttons positioning */
    private Table table = new Table();

    /* game font */
    private BitmapFont font = Data.font;

    private SelectBox <String>spellSelectBox;
    private List<String> list;
    /* bar*/
    //private ProgressBar fuelBar = new ProgressBar(0f, player.getMaxFuel(), 0.1f, true, SKIN);
    //TODO find the progress bar skin needed

    private Button button = new TextButton("Test", Data.SKIN);

    public PlayerHUD(GameStage gameStage, Player player) {
        super();
        this.gameStage = gameStage;
        this.player = player;
        init();
    }

    private void init(){
        Gdx.app.log(TAG, "Init the player HUD");

        Array<String> spells = new Array<String>();
        spells.add("Select spell");
        spells.add(" ");
        for(Spell s : player.getSpells())
            spells.add(s.getName());

        spellSelectBox = new SelectBox(Data.SKIN);
        spellSelectBox.setItems(spells);
        spellSelectBox.setPosition(Data.SCREEN_WIDTH - 220, Data.SCREEN_HEIGHT - 100);
        spellSelectBox.setSize(200, 50);
        spellSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Spell spell = player.getSpellByName(spellSelectBox.getSelected());
                if(spell != null) {
                    player.setSpellSelection(true);
                    player.setSpellSelected(spell);
                }else{
                    player.setSpellSelection(false);
                    player.setSpellSelected(null);
                }
            }
        });

        gameStage.addActor(spellSelectBox);

        Gdx.app.log(TAG, "Init spellSelectBox with : " + spellSelectBox.getItems().toString() + ", z index " + spellSelectBox.getZIndex() + ", " + spellSelectBox.getListeners());

        /*===List===*/
        /*
        list = new List<String>(Data.SKIN);
        list.setItems(spells);
        list.setSelectedIndex(0);
        list.setBounds(200, 200, 100, 30);
        list.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("List changed");
            }
        });
        gameStage.addActor(list);
        */
    }

    public void resetSpellSelection(){
        spellSelectBox.setSelectedIndex(0);
    }
}
