package com.becksm64.pitch;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.becksm64.pitch.UI.MainMenuScreen;

public class Pitch extends Game
{
	//Global fonts
	public static BitmapFont font1, font2, font3, font4;

	//Global background image
	public static Texture background;

	@Override
	public void create ()
	{
		//Create static background image for whole appp
		background = new Texture("background.jpg");

		//Create static font that is scalable
		FreeTypeFontGenerator.setMaxTextureSize(4096);
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/chewy.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = (int) (50 * Gdx.graphics.getDensity());
		font1 = generator.generateFont(parameter);
		parameter.size = (int) (123 * Gdx.graphics.getDensity());//Make second font different size
		font2 = generator.generateFont(parameter);
		parameter.size = (int) (15 * Gdx.graphics.getDensity());
		font3 = generator.generateFont(parameter);
		parameter.size = (int) (30 * Gdx.graphics.getDensity());
		font4 = generator.generateFont(parameter);
		generator.dispose();//Get rid of generator after done making fonts

		//Set the main menu screen as the first screen
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render ()
	{
		super.render();
	}
	
	@Override
	public void dispose ()
	{
		font1.dispose();
		font2.dispose();
		font3.dispose();
		font4.dispose();
		background.dispose();
	}
}
