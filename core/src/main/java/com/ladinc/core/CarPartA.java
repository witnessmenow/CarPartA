package com.ladinc.core;

import com.badlogic.gdx.Game;
import com.ladinc.core.screen.MainMenuScreen;

public class CarPartA extends Game {

	@Override
	public void create () {
		//Assets - Static class that loads audio and graphics
		//Settings - Static class that loads progress of game (high score) and other stuff
		setScreen(new MainMenuScreen(this));
	}
}