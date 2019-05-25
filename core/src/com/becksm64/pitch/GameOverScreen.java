package com.becksm64.pitch;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class GameOverScreen implements Screen {

    private Game game;
    private OrthographicCamera cam;
    private BitmapFont font;
    private BitmapFont font2;
    private Stage stage;
    private Table table;

    public GameOverScreen(Game game, int winner) {

        this.game = game;
        cam = new OrthographicCamera();
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());//Set camera viewport to device screen size

        //Font that is scalable
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/cour.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) (50 * Gdx.graphics.getDensity());
        font = generator.generateFont(parameter);
        parameter.size = (int) (25 * Gdx.graphics.getDensity());
        font2 = generator.generateFont(parameter);
        generator.dispose();//Get rid of generator after done making fonts

        //Stage and table for layout
        stage = new Stage();
        table = new Table();

        //Game over text
        Skin skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
        Label gameOverLabel = new Label("Game Over", skin);
        Label winnerLabel = new Label("Player " + winner + " wins!", skin);
        gameOverLabel.setStyle(new Label.LabelStyle(font, Color.WHITE));
        winnerLabel.setStyle(new Label.LabelStyle(font2, Color.WHITE));

        //Add everything to table and stage
        table.add(gameOverLabel);
        table.row();//Next row
        table.add(winnerLabel);
        table.setFillParent(true);
        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        //Clear the screen with some color
        Gdx.gl.glClearColor(0, 0.75f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        font.dispose();
    }
}
