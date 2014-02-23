package com.ladinc.core.screen.gamemodes.soccer;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.ladinc.core.CarPartA;
import com.ladinc.core.assets.Art;
import com.ladinc.core.objects.Ball;
import com.ladinc.core.screen.gamemodes.GenericLayout;
import com.ladinc.core.screen.gamemodes.GenericScreen;

public class SoccerScreen extends GenericScreen {
	private float secondsLeft = 5;
	private final int homeScore = 0;
	private final int awayScore = 0;
	private Ball ball;
	
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
		updateTimer(delta);
		updateScore();
	}
	
	private void updateTimer(float delta)
	{
		bitmapFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		bitmapFont.draw(spriteBatch, calculateTimeLeft(delta),
				(screenWidth / 2) - 15, screenHeight - 10);
	}
	
	private void updateScore()
	{
		bitmapFont.draw(spriteBatch, "" + homeScore, (screenWidth / 2) - 50,
				screenHeight - 10);
		bitmapFont.draw(spriteBatch, "" + awayScore, (screenWidth / 2) + 50,
				screenHeight - 10);
	}
	
	private String calculateTimeLeft(float secondsPast)
	{
		if (secondsLeft != 0)
		{
			secondsLeft -= secondsPast;
			if (secondsLeft < 0)
			{
				secondsLeft = 0;
			}
		}
		int milliSeconds = (int) (1000 * (secondsLeft - (int) secondsLeft));
		int minutes = (int) secondsLeft / 60;
		int seconds = (int) secondsLeft;
		return minutes + ":" + seconds + ":" + milliSeconds;
	}
	
	@Override
	public void initGame()
	{
		ball = new Ball(world, center.x + Ball.ballOffsetX, center.y,
				new Sprite(Art.textureTable.get(Art.BALL), 0, 0, 40, 40));
	}
}