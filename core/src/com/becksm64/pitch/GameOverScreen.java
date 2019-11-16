package com.becksm64.pitch;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class GameOverScreen implements Screen
{
    private Game game;
    private OrthographicCamera cam;
    private Stage stage;
    private Batch batch;
    private TextButton newGameBtn;
    private TextButton quitBtn;

    public GameOverScreen(Game game, int winner)
    {
        this.game = game;
        batch = new SpriteBatch();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());//Set camera viewport to device screen size
        int padding = (int) (100 * Gdx.graphics.getDensity());

        //Stage and table for layout
        stage = new Stage();
        Table table = new Table();

        //Game over text
        Skin skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
        Label gameOverLabel = new Label("Game Over", skin);
        Label winnerLabel = new Label("Player " + winner + " wins!", skin);
        gameOverLabel.setStyle(new Label.LabelStyle(Pitch.font1, Color.WHITE));
        winnerLabel.setStyle(new Label.LabelStyle(Pitch.font4, Color.WHITE));

        //Buttons
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = Pitch.font4;
        newGameBtn = new TextButton("NEW GAME", style);
        quitBtn = new TextButton("QUIT", style);

        //Add everything to table and stage
        table.add(gameOverLabel);
        table.row();//Next row
        table.add(winnerLabel).padBottom(padding);
        table.row();
        table.add(newGameBtn);
        table.row();
        table.add(quitBtn);
        table.setFillParent(true);
        Gdx.input.setInputProcessor(stage);
        stage.addActor(table);
    }

    @Override
    public void show()
    {
        //Starts new game when clicked
        newGameBtn.addListener(new ChangeListener()
            {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    dispose();
                    game.setScreen(new GameScreen(game));
                }
            }
        );

        //Quits the app when clicked
        quitBtn.addListener(new ChangeListener()
            {
                @Override
                public void changed(ChangeEvent event, Actor actor)
                {
                    dispose();
                    game.dispose();
                    Gdx.app.exit();
                    System.exit(0);
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
        batch.draw(Pitch.background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch.end();//Stop drawing

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
        stage.dispose();
        batch.dispose();
    }
}
