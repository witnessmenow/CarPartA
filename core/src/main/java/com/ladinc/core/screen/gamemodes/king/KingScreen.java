package com.ladinc.core.screen.gamemodes.king;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.ladinc.core.CarPartA;
import com.ladinc.core.ai.SimpleAi;
import com.ladinc.core.assets.Art;
import com.ladinc.core.assets.Font;
import com.ladinc.core.screen.gamemodes.GenericScreen;
import com.ladinc.core.screen.gamemodes.IGenericLayout;
import com.ladinc.core.screen.gamemodes.mower.MowerScreen;
import com.ladinc.core.screen.gamemodes.pong.PongScreen;
import com.ladinc.core.utilities.Enums.Team;
import com.ladinc.core.vehicles.Vehicle;

public class KingScreen extends GenericScreen
{
	private static final float START_TIME = 30;
	
	private float awaySecondsLeft = START_TIME;
	private float homeSecondsLeft = START_TIME;
	private BitmapFont bitmapFont;

	private KingLayout kingLayout;
	private KingColisionHelper colHelper;
	
	public KingScreen(CarPartA game) 
	{
		super(game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void preCarRender(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void customRender(float delta) 
	{
		if(this.proccessingGameOver)
		{
			if(processGameOverTimer(delta))
			{
				handleGameOver();
			}
		}
		
	}
	
	private void handleGameOver()
	{
		this.game.setScreen(new MowerScreen(game));
		dispose();
	}

	@Override
	public IGenericLayout resetLayout() 
	{
		kingLayout = new KingLayout(world, worldWidth, worldHeight, center, 0);
		return kingLayout;
	}

	@Override
	public void initGame() 
	{
		assignTeamSpritesToCars();

		this.backgroundSprite = Art.getSprite(Art.PAINTER_BACKGROUND);
		
		bitmapFont = Font.fontTable.get(Font.CONST_50);
		
		colHelper =  new KingColisionHelper();
		
		this.world.setContactListener(colHelper);
		
		Random r = new Random();
		int index = r.nextInt(getVehicles().size());
		
		Vehicle v = getVehicles().get(index);
		
		v.king = true;
		colHelper.currentKingVehicle = v;
		colHelper.currentKingSide = v.player.team;
		
		for (SimpleAi ai : this.game.controllerManager.getAi())
		{
			ai.resetTimers();
			ai.justAccelerateAndReverse = false;
		}
	}

	@Override
	protected void renderUpdates(float delta) 
	{
		updateTimer(delta);
		if(this.proccessingGameOver)
		{
			this.finishMessage.draw(spriteBatch);
		}
		
	}
	
	private void handleWin() 
	{
		this.gameOverCoolOffTimer = 5.0f;
    	this.proccessingGameOver = true;
		
	}
	
	
	private String timeLeft;
	private void updateTimer(float delta)
	{
		//Set for blue
		bitmapFont.setColor(0.3f, 0.5f, 1f, 1.0f);
		
		timeLeft = calculateHomeTimeLeft(delta, (colHelper.currentKingSide == Team.Home));
		
		bitmapFont.draw(spriteBatch, timeLeft,
				(screenWidth / 4) - bitmapFont.getBounds(timeLeft).width/2, screenHeight - 10);
		
		//Set for blue
		bitmapFont.setColor(1f, 0.3f, 0.3f, 1.0f);
		
		timeLeft = calculateAwayTimeLeft(delta, (colHelper.currentKingSide == Team.Away));
		
		bitmapFont.draw(spriteBatch, timeLeft,
				(screenWidth / 4)*3 - bitmapFont.getBounds(timeLeft).width/4, screenHeight - 10);
	}
	
	private String calculateHomeTimeLeft(float secondsPast, boolean countDown)
	{
		if(!this.proccessingGameOver && countDown)
		{

			homeSecondsLeft -= secondsPast;

			
			if (homeSecondsLeft < 0)
			{
				homeSecondsLeft = 0;
				handleWin();
			}
		}
		
		int seconds = (int) homeSecondsLeft ;
		
		String secondPrepend =  (seconds< 10)?"0":"";
		
		return secondPrepend + seconds;
	}
	
	private String calculateAwayTimeLeft(float secondsPast, boolean countDown)
	{
		if(!this.proccessingGameOver && countDown)
		{

			awaySecondsLeft -= secondsPast;

			
			if (awaySecondsLeft < 0)
			{
				awaySecondsLeft = 0;
				handleWin();
			}
		}
		
		int seconds = (int) awaySecondsLeft ;
		
		String secondPrepend =  (seconds< 10)?"0":"";
		
		return secondPrepend + seconds;
	}

}
