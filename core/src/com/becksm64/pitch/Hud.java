package com.becksm64.pitch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud {

    private Stage stage;
    private Label playerBid;

    public Hud(SpriteBatch batch) {

        Viewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport, batch);
        Table table = new Table();
        table.top();
        table.setFillParent(true);

        Skin skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
        Label highestBid = new Label("Highest Bid", skin);
        playerBid = new Label(" ", skin);
        highestBid.setStyle(new Label.LabelStyle(Pitch.font3, Color.WHITE));
        playerBid.setStyle(new Label.LabelStyle(Pitch.font3, Color.WHITE));
        table.add(highestBid).expandX().align(Align.left);//Have row take up full width of stage and align left
        table.row();
        table.add(playerBid).expandX().align(Align.left);

        stage.addActor(table);
    }

    public void setPlayerBid(String playerBid) {
        this.playerBid.setText(playerBid);
    }

    public Stage getStage() {
        return this.stage;
    }

    public void dispose() {
        this.stage.dispose();
    }
}
