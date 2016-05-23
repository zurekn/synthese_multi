package gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.data.Data;
import com.mygdx.game.game.GameStage;

/**
 * Created by gregory on 18/05/16.
 */
public class LogHandler{
    private ScrollPane pane ;
    private Table table, container;

    public LogHandler(GameStage stage){
        /*this.table = new Table();
        this.container = new Table();
        scroll = new ScrollPane(table);
        container.add(scroll).width(500f).height(500f);
        container.row();*/


        container = new Table();
        stage.addActor(container);
        container.setFillParent(true);

        table = new Table();
        table.left();
        pane = new ScrollPane(table, Data.SKIN);
        pane.layout();


       // table.setBounds(0,0,Data.SCREEN_WIDTH, Data.SCREEN_HEIGHT);

        container.add(pane).expand().fill().colspan(4);
        container.row();
    }

    public void addMessage(String mes){
        Label lbl = new Label(mes, Data.SKIN);
        lbl.setAlignment(Align.left, Align.center);
        table.add(lbl).left();
        table.row();
    }
}
