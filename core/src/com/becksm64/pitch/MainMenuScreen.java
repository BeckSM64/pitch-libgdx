package com.becksm64.pitch;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainMenuScreen implements Screen
{
    private Game game;
    private OrthographicCamera cam;
    private Stage stage;
    private TextButton startButton;
    private Batch batch;
    private Texture spadeImage;
    private Viewport viewport;

    public MainMenuScreen(Game game)
    {
        this.game = game;
        batch = new SpriteBatch();
        spadeImage = new Texture(Gdx.files.internal("suits/spade_xxxhdpi.png"));
        cam = new OrthographicCamera();
        viewport = new ScreenViewport(cam);

        //Table and stage stuff
        stage = new Stage(viewport);
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

        batch.setProjectionMatrix(cam.combined);
        batch.begin();//Start drawing

        //Draw background image
        batch.draw(Pitch.background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        //Draw assets on screen
        batch.draw(
                spadeImage,
                (viewport.getWorldWidth() / 2) - Card.WIDTH * 2,
                (viewport.getWorldHeight() / 2) - Card.WIDTH * 2,
                Card.WIDTH * 4,
                Card.WIDTH * 4
        );

        batch.end();//Stop drawing

        cam.update();
        stage.draw();
    }

    @Override
    public void resize(int width, int height)
    {
        viewport.update(width, height);
        stage.getViewport().update(width, height, true);
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
        batch.dispose();
        spadeImage.dispose();
        stage.dispose();
    }
}
