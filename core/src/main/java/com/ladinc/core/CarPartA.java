package com.ladinc.core;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.ladinc.core.assets.Assets;
import com.ladinc.core.controllers.MyControllerManager;
import com.ladinc.core.screen.MainMenuScreen;

public class CarPartA extends Game {
	
	public MyControllerManager controllerManager;
	
	@Override
	public void create()
	{
		Gdx.app.setLogLevel(Application.LOG_ERROR);
		Assets.load();
		controllerManager = new MyControllerManager();
		setScreen(new MainMenuScreen(this));
	}
	
	public static Screen getRandomGameScreen()
	{
		return null;
	}
}