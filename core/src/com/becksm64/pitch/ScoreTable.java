package com.becksm64.pitch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

public class ScoreTable extends Table {

    private Label player1Score;
    private Label player2Score;
    private Label player3Score;
    private Label player4Score;
    private TextButton nextRoundBtn;

    public ScoreTable() {

        //Generate font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/cour.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 72;
        BitmapFont font72 = generator.generateFont(parameter);
        generator.dispose();//Get rid of generator after done making fonts

        //Create skin for labels for score table
        Skin skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
        Label playerTitle = new Label("Player", skin);
        playerTitle.setStyle(new Label.LabelStyle(font72, Color.WHITE));
        Label highTitle = new Label("High", skin);
        highTitle.setStyle(new Label.LabelStyle(font72, Color.WHITE));
        Label jackTitle = new Label("Jack", skin);
        jackTitle.setStyle(new Label.LabelStyle(font72, Color.WHITE));
        Label lowTitle = new Label("Low", skin);
        lowTitle.setStyle(new Label.LabelStyle(font72, Color.WHITE));
        Label gameTitle = new Label("Game", skin);
        gameTitle.setStyle(new Label.LabelStyle(font72, Color.WHITE));
        Label totalScoreTitle = new Label("Score", skin);
        totalScoreTitle.setStyle(new Label.LabelStyle(font72, Color.WHITE));
        Label player1 = new Label("1", skin);
        player1.setStyle(new Label.LabelStyle(font72, Color.WHITE));
        Label player2 = new Label("2", skin);
        player2.setStyle(new Label.LabelStyle(font72, Color.WHITE));
        Label player3 = new Label("3", skin);
        player3.setStyle(new Label.LabelStyle(font72, Color.WHITE));
        Label player4 = new Label("4", skin);
        player4.setStyle(new Label.LabelStyle(font72, Color.WHITE));

        //Create labels for player scores to be displayed
        player1Score = new Label("0", skin);
        player1Score.setStyle(new Label.LabelStyle(font72, Color.WHITE));
        player2Score = new Label("0", skin);
        player2Score.setStyle(new Label.LabelStyle(font72, Color.WHITE));
        player3Score = new Label("0", skin);
        player3Score.setStyle(new Label.LabelStyle(font72, Color.WHITE));
        player4Score = new Label("0", skin);
        player4Score.setStyle(new Label.LabelStyle(font72, Color.WHITE));
        nextRoundBtn = new TextButton("Next Round", skin);

        //Add labels to table
        this.setFillParent(true);
        this.add(playerTitle).padRight(50);//First row
        this.add(player1).padRight(50);
        this.add(player2).padRight(50);
        this.add(player3).padRight(50);
        this.add(player4).padRight(50);
        this.row();
        this.add(highTitle).padRight(50).align(Align.left);//Second row
        this.row();
        this.add(jackTitle).padRight(50).align(Align.left);//Third row
        this.row();
        this.add(lowTitle).padRight(50).align(Align.left);//Fourth row
        this.row();
        this.add(gameTitle).padRight(50).align(Align.left);//Fifth row
        this.row();
        this.add(totalScoreTitle).padRight(50).align(Align.left);//Sixth row
        this.add(player1Score).padRight(50);
        this.add(player2Score).padRight(50);
        this.add(player3Score).padRight(50);
        this.add(player4Score).padRight(50);
        this.row();
        this.add(nextRoundBtn).colspan(5);
        //this.debug();//Draws the table lines so you can see the actual layout
    }

    public TextButton getNextRoundBtn() {
        return nextRoundBtn;
    }

    public void setPlayer1Score(int player1Score) {
        this.player1Score.setText(Integer.toString(player1Score));
    }

    public void setPlayer2Score(int player2Score) {
        this.player2Score.setText(Integer.toString(player2Score));
    }

    public void setPlayer3Score(int player3Score) {
        this.player3Score.setText(Integer.toString(player3Score));
    }

    public void setPlayer4Score(int player4Score) {
        this.player4Score.setText(Integer.toString(player4Score));
    }
}
