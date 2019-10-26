package com.becksm64.pitch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class BidTable extends Table {

    private Stage stage;
    private TextButton bid0Btn;
    private TextButton bid2Btn;
    private TextButton bid3Btn;
    private TextButton bid4Btn;

    public BidTable() {

        stage = new Stage();//Create stage for table
        int padding = (int) (12 * Gdx.graphics.getDensity());

        //Setup styles for buttons and labels
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = Pitch.font4;

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
        this.pack();
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

    public Stage getStage() {
        return this.stage;
    }

    public void dispose() {
        this.stage.dispose();
    }
}
