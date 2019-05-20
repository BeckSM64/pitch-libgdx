package com.becksm64.pitch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class BidTable extends Table {

    private TextButton bid0Btn;
    private TextButton bid1Btn;
    private TextButton bid2Btn;
    private TextButton bid3Btn;
    private TextButton bid4Btn;

    public BidTable() {

        //Generate font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/cour.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 72;
        BitmapFont font72 = generator.generateFont(parameter);
        generator.dispose();

        //Setup styles for buttons and labels
        Skin skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font72;

        //Setup buttons to take bids
        bid0Btn = new TextButton("0", style);
        bid1Btn = new TextButton("1", style);
        bid2Btn = new TextButton("2", style);
        bid3Btn = new TextButton("3", style);
        bid4Btn = new TextButton("4", style);

        //Add buttons and labels to the table
        this.setFillParent(true);
        this.add(bid0Btn).pad(50);
        this.add(bid1Btn).pad(50);
        this.add(bid2Btn).pad(50);
        this.add(bid3Btn).pad(50);
        this.add(bid4Btn).pad(50);
        this.debug();
    }

    public TextButton getBid0Btn() {
        return bid0Btn;
    }

    public TextButton getBid1Btn() {
        return bid1Btn;
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
}
