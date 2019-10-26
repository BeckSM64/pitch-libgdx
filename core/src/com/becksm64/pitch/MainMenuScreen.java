package com.becksm64.pitch;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MainMenuScreen implements Screen {

    Game game;
    OrthographicCamera cam;
    Stage stage;
    Table table;
    TextButton startButton;
    TextButton.TextButtonStyle buttonStyle;
    Label gameTitle;

    public MainMenuScreen(Game game) {

        this.game = game;
        cam = new OrthographicCamera();
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());//Set camera viewport to device screen size

        //Table and stage stuff
        stage = new Stage();
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        //table.debug();//Draws the table lines so you can see the actual layout

        //Create menu buttons and game title
        Gdx.input.setInputProcessor(stage);
        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = Pitch.font1;
        startButton = new TextButton("Start", buttonStyle);
        Skin skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
        gameTitle = new Label("Pitch", skin);
        gameTitle.setStyle(new Label.LabelStyle(Pitch.font2, Color.WHITE));

        //Add buttons and labels to the table
        table.add(gameTitle);
        table.row();//Move to next table row
        table.add(startButton);
    }

    @Override
    public void show() {

        //Listener for start button to complete action when clicked
        startButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game));//Start a new game if the start button is pressed
            }
        });
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

    }
}
