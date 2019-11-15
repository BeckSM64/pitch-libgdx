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

public class MainMenuScreen implements Screen
{
    private Game game;
    private OrthographicCamera cam;
    private Stage stage;
    private TextButton startButton;

    public MainMenuScreen(Game game)
    {
        this.game = game;
        cam = new OrthographicCamera();

        //Set camera viewport to device screen size
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //Table and stage stuff
        stage = new Stage();
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        //Create menu buttons and game title
        Gdx.input.setInputProcessor(stage);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = Pitch.font1;
        startButton = new TextButton("Start", buttonStyle);
        Skin skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
        Label gameTitle = new Label("Pitch", skin);
        gameTitle.setStyle(new Label.LabelStyle(Pitch.font2, Color.WHITE));

        //Add buttons and labels to the table
        table.add(gameTitle);
        table.row();//Move to next table row
        table.add(startButton);
    }

    @Override
    public void show()
    {
        //Listener for start button to complete action when clicked
        startButton.addListener(new ChangeListener()
            {
                @Override
                public void changed(ChangeEvent event, Actor actor)
                {
                    //Start a new game if the start button is pressed
                    game.setScreen(new GameScreen(game));
                }
            }
        );
    }

    @Override
    public void render(float delta)
    {
        //Clear the screen with some color
        Gdx.gl.glClearColor(0, 0.75f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();
        stage.draw();
    }

    @Override
    public void resize(int width, int height)
    {
        
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {

    }
}
