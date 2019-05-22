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
    private Label player1High;
    private Label player2High;
    private Label player3High;
    private Label player4High;
    private Label player1Jack;
    private Label player2Jack;
    private Label player3Jack;
    private Label player4Jack;
    private Label player1Low;
    private Label player2Low;
    private Label player3Low;
    private Label player4Low;
    private Label player1Game;
    private Label player2Game;
    private Label player3Game;
    private Label player4Game;
    private TextButton nextRoundBtn;

    public ScoreTable() {

        //Generate font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/cour.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) (30 * Gdx.graphics.getDensity());
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();//Get rid of generator after done making fonts
        int padding = (int) (12 * Gdx.graphics.getDensity());
        System.out.println(padding);

        //Create skin for labels for score table
        Skin skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
        Label playerTitle = new Label("Player", skin);
        playerTitle.setStyle(new Label.LabelStyle(font, Color.WHITE));
        Label highTitle = new Label("High", skin);
        highTitle.setStyle(new Label.LabelStyle(font, Color.WHITE));
        Label jackTitle = new Label("Jack", skin);
        jackTitle.setStyle(new Label.LabelStyle(font, Color.WHITE));
        Label lowTitle = new Label("Low", skin);
        lowTitle.setStyle(new Label.LabelStyle(font, Color.WHITE));
        Label gameTitle = new Label("Game", skin);
        gameTitle.setStyle(new Label.LabelStyle(font, Color.WHITE));
        Label totalScoreTitle = new Label("Score", skin);
        totalScoreTitle.setStyle(new Label.LabelStyle(font, Color.WHITE));
        Label player1 = new Label("1", skin);
        player1.setStyle(new Label.LabelStyle(font, Color.WHITE));
        Label player2 = new Label("2", skin);
        player2.setStyle(new Label.LabelStyle(font, Color.WHITE));
        Label player3 = new Label("3", skin);
        player3.setStyle(new Label.LabelStyle(font, Color.WHITE));
        Label player4 = new Label("4", skin);
        player4.setStyle(new Label.LabelStyle(font, Color.WHITE));

        //Create labels for player scores to be displayed
        player1Score = new Label("0", skin);
        player1Score.setStyle(new Label.LabelStyle(font, Color.WHITE));
        player2Score = new Label("0", skin);
        player2Score.setStyle(new Label.LabelStyle(font, Color.WHITE));
        player3Score = new Label("0", skin);
        player3Score.setStyle(new Label.LabelStyle(font, Color.WHITE));
        player4Score = new Label("0", skin);
        player4Score.setStyle(new Label.LabelStyle(font, Color.WHITE));
        player1High = new Label("-", skin);
        player1High.setStyle(new Label.LabelStyle(font, Color.WHITE));
        player2High = new Label("-", skin);
        player2High.setStyle(new Label.LabelStyle(font, Color.WHITE));
        player3High = new Label("-", skin);
        player3High.setStyle(new Label.LabelStyle(font, Color.WHITE));
        player4High = new Label("-", skin);
        player4High.setStyle(new Label.LabelStyle(font, Color.WHITE));
        player1Jack = new Label("-", skin);
        player1Jack.setStyle(new Label.LabelStyle(font, Color.WHITE));
        player2Jack = new Label("-", skin);
        player2Jack.setStyle(new Label.LabelStyle(font, Color.WHITE));
        player3Jack = new Label("-", skin);
        player3Jack.setStyle(new Label.LabelStyle(font, Color.WHITE));
        player4Jack = new Label("-", skin);
        player4Jack.setStyle(new Label.LabelStyle(font, Color.WHITE));
        player1Low = new Label("-", skin);
        player1Low.setStyle(new Label.LabelStyle(font, Color.WHITE));
        player2Low = new Label("-", skin);
        player2Low.setStyle(new Label.LabelStyle(font, Color.WHITE));
        player3Low = new Label("-", skin);
        player3Low.setStyle(new Label.LabelStyle(font, Color.WHITE));
        player4Low = new Label("-", skin);
        player4Low.setStyle(new Label.LabelStyle(font, Color.WHITE));
        player1Game = new Label("-", skin);
        player1Game.setStyle(new Label.LabelStyle(font, Color.WHITE));
        player2Game = new Label("-", skin);
        player2Game.setStyle(new Label.LabelStyle(font, Color.WHITE));
        player3Game = new Label("-", skin);
        player3Game.setStyle(new Label.LabelStyle(font, Color.WHITE));
        player4Game = new Label("-", skin);
        player4Game.setStyle(new Label.LabelStyle(font, Color.WHITE));
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font;
        style.fontColor = Color.BLACK;
        nextRoundBtn = new TextButton("Next Round", style);

        //Add labels to table
        this.setFillParent(true);
        this.add(playerTitle).padRight(padding);//First row
        this.add(player1).padRight(padding);
        this.add(player2).padRight(padding);
        this.add(player3).padRight(padding);
        this.add(player4).padRight(padding);
        this.row();
        this.add(highTitle).padRight(padding).align(Align.left);//Second row
        this.add(player1High).padRight(padding);
        this.add(player2High).padRight(padding);
        this.add(player3High).padRight(padding);
        this.add(player4High).padRight(padding);
        this.row();
        this.add(jackTitle).padRight(padding).align(Align.left);//Third row
        this.add(player1Jack).padRight(padding);
        this.add(player2Jack).padRight(padding);
        this.add(player3Jack).padRight(padding);
        this.add(player4Jack).padRight(padding);
        this.row();
        this.add(lowTitle).padRight(padding).align(Align.left);//Fourth row
        this.add(player1Low).padRight(padding);
        this.add(player2Low).padRight(padding);
        this.add(player3Low).padRight(padding);
        this.add(player4Low).padRight(padding);
        this.row();
        this.add(gameTitle).padRight(padding).align(Align.left);//Fifth row
        this.add(player1Game).padRight(padding);
        this.add(player2Game).padRight(padding);
        this.add(player3Game).padRight(padding);
        this.add(player4Game).padRight(padding);
        this.row();
        this.add(totalScoreTitle).padRight(padding).align(Align.left);//Sixth row
        this.add(player1Score).padRight(padding);
        this.add(player2Score).padRight(padding);
        this.add(player3Score).padRight(padding);
        this.add(player4Score).padRight(padding);
        this.row();
        this.add(nextRoundBtn).colspan(5).padTop(padding);
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

    public void setPlayerHigh(int playerIndex) {
        if(playerIndex == 0)
            this.player1High.setText("1");
        else if(playerIndex == 1)
            this.player2High.setText("1");
        else if(playerIndex == 2)
            this.player3High.setText("1");
        else
            this.player4High.setText("1");
    }

    public void setPlayerJack(int playerIndex) {
        if(playerIndex >= 0) {
            if (playerIndex == 0)
                this.player1Jack.setText("1");
            else if (playerIndex == 1)
                this.player2Jack.setText("1");
            else if (playerIndex == 2)
                this.player3Jack.setText("1");
            else
                this.player4Jack.setText("1");
        }
    }

    public void setPlayerLow(int playerIndex) {
        if(playerIndex == 0)
            this.player1Low.setText("1");
        else if(playerIndex == 1)
            this.player2Low.setText("1");
        else if(playerIndex == 2)
            this.player3Low.setText("1");
        else
            this.player4Low.setText("1");
    }

    public void setPlayerGame(int playerIndex) {
        if(playerIndex == 0)
            this.player1Game.setText("1");
        else if(playerIndex == 1)
            this.player2Game.setText("1");
        else if(playerIndex == 2)
            this.player3Game.setText("1");
        else
            this.player4Game.setText("1");
    }

    public void resetPointLabels() {
        this.player1High.setText("-");
        this.player2High.setText("-");
        this.player3High.setText("-");
        this.player4High.setText("-");
        this.player1Jack.setText("-");
        this.player2Jack.setText("-");
        this.player3Jack.setText("-");
        this.player4Jack.setText("-");
        this.player1Low.setText("-");
        this.player2Low.setText("-");
        this.player3Low.setText("-");
        this.player4Low.setText("-");
        this.player1Game.setText("-");
        this.player2Game.setText("-");
        this.player3Game.setText("-");
        this.player4Game.setText("-");
    }
}
