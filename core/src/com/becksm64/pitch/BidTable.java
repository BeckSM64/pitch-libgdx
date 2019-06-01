package com.becksm64.pitch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class BidTable extends Table {

    private Stage stage;
    private TextButton bid0Btn;
    private TextButton bid2Btn;
    private TextButton bid3Btn;
    private TextButton bid4Btn;
    public ShapeRenderer rect;

    public BidTable() {

        stage = new Stage();//Create stage for table

        //Generate font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/cour.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) (30 * Gdx.graphics.getDensity());
        BitmapFont font72 = generator.generateFont(parameter);
        generator.dispose();
        int padding = (int) (12 * Gdx.graphics.getDensity());

        //Setup styles for buttons and labels
        Skin skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font72;

        //Setup buttons to take bids
        bid0Btn = new TextButton("0", style);
        bid2Btn = new TextButton("2", style);
        bid3Btn = new TextButton("3", style);
        bid4Btn = new TextButton("4", style);

        //Add buttons and labels to the table
        this.setFillParent(true);
        this.add(bid0Btn).pad(padding);
        this.add(bid2Btn).pad(padding);
        this.add(bid3Btn).pad(padding);
        this.add(bid4Btn).pad(padding);
        //this.debug();
        this.pack();
        System.out.println(this.getCell(bid0Btn).getActorX());
        rect = new ShapeRenderer();
        stage.addActor(this);
    }

    public TextButton getBid0Btn() {
        return bid0Btn;
    }

    public TextButton getBid2Btn() {
        return bid2Btn;
    }

    public TextButton getBid3Btn() {
        return bid3Btn;
    }

    public TextButton getBid4Btn() {
        return bid4Btn;
    }

    public void drawBorder() {
        this.rect.begin(ShapeRenderer.ShapeType.Line);
        this.rect.setColor(Color.BLACK);
        this.rect.rect(this.getCell(bid0Btn).getActorX(), this.getCell(bid0Btn).getActorY(), this.getCell(bid0Btn).getActorWidth() * 13, this.getCell(bid0Btn).getActorHeight());
        this.rect.end();
    }

    public Stage getStage() {
        return this.stage;
    }

    public void dispose() {
        this.stage.dispose();
    }
}
