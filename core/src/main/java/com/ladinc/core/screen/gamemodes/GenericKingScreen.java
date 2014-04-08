package com.ladinc.core.screen.gamemodes;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.ladinc.core.CarPartA;
import com.ladinc.core.utilities.Enums.Team;

public abstract class GenericKingScreen extends GenericScreen {
	private static final float START_TIME = 30;
	
	private float homeSecondsLeft = START_TIME;
	private float awaySecondsLeft = START_TIME;
	
	protected BitmapFont bitmapFont;
	
	public GenericKingScreen(CarPartA game) {
		super(game);
	}
	
	protected void updateTimer(float delta, Team currentKingSide)
	{
		String homeTimeLeft = calculateHomeTimeLeft(delta,
				(currentKingSide == Team.Home));
		
		// Set for blue
		bitmapFont.setColor(0.3f, 0.5f, 1f, 1.0f);
		
		bitmapFont.draw(spriteBatch, homeTimeLeft, (screenWidth / 4)
				- bitmapFont.getBounds(homeTimeLeft).width / 2,
				screenHeight - 10);
		
		// Set for red
		bitmapFont.setColor(1f, 0.3f, 0.3f, 1.0f);
		
		String awayTimeLeft = calculateAwayTimeLeft(delta,
				(currentKingSide == Team.Away));
		
		bitmapFont.draw(spriteBatch, awayTimeLeft, (screenWidth / 4) * 3
				- bitmapFont.getBounds(awayTimeLeft).width / 4,
				screenHeight - 10);
	}
	
	protected abstract void handleWin();
	
	private String calculateHomeTimeLeft(float secondsPast,
			boolean isTimeBeingCountedDown)
	{
		if (!this.proccessingGameOver && isTimeBeingCountedDown
				&& !this.showDescriptionScreen)
		{
			homeSecondsLeft -= secondsPast;
			if (homeSecondsLeft < 0)
			{
				homeSecondsLeft = 0;
				handleWin();
			}
		}
		
		int seconds = (int) homeSecondsLeft;
		String secondPrepend = (seconds < 10) ? "0" : "";
		
		return secondPrepend + seconds;
	}
	
	private String calculateAwayTimeLeft(float secondsPast,
			boolean isTimeBeingCountedDown)
	{
		if (!this.proccessingGameOver && isTimeBeingCountedDown
				&& !this.showDescriptionScreen)
		{
			awaySecondsLeft -= secondsPast;
			if (awaySecondsLeft < 0)
			{
				awaySecondsLeft = 0;
				handleWin();
			}
		}
		
		int seconds = (int) awaySecondsLeft;
		String secondPrepend = (seconds < 10) ? "0" : "";
		
		return secondPrepend + seconds;
	}
}