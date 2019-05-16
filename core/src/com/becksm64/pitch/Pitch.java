package com.becksm64.pitch;

import com.badlogic.gdx.Game;

public class Pitch extends Game {
	
	@Override
	public void create () {
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {

	}
}
