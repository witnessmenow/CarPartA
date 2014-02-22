package com.ladinc.core.screen.gamemodes.soccer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.ladinc.core.CarPartA;
import com.ladinc.core.screen.gamemodes.GenericLayout;
import com.ladinc.core.screen.gamemodes.GenericScreen;

public class SoccerScreen extends GenericScreen {
	private int timeLeft = 0;
	BitmapFont bitmapFont = new BitmapFont();
	
	public SoccerScreen(CarPartA game) {
		super(game);
	}
	
	@Override
	public GenericLayout resetLayout()
	{
		return new SoccerLayout(world, worldWidth, worldHeight, center, 4);
	}
	
	@Override
	protected void renderUpdates(float delta)
	{
		updateTimer();
		updateScore();
	}
	
	private void updateTimer()
	{
		bitmapFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		bitmapFont.draw(spriteBatch, "" + timeLeft, 25, 100);
		timeLeft += 1;
		Gdx.app.debug("render: Generic", "timeLeft: " + timeLeft);
	}
	
	private void updateScore()
	{
		
	}
}