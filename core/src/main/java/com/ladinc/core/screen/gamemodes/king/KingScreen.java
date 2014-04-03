package com.ladinc.core.screen.gamemodes.king;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.ladinc.core.CarPartA;
import com.ladinc.core.ai.SimpleAi;
import com.ladinc.core.assets.Art;
import com.ladinc.core.assets.Font;
import com.ladinc.core.screen.gamemodes.GenericScreen;
import com.ladinc.core.screen.gamemodes.IGenericLayout;
import com.ladinc.core.screen.gamemodes.mower.MowerScreen;
import com.ladinc.core.utilities.Enums.Team;
import com.ladinc.core.ux.DescriptionScreenInfo;
import com.ladinc.core.vehicles.Vehicle;

public class KingScreen extends GenericScreen 
{
	private static final float START_TIME = 30;
	
	private float awaySecondsLeft = START_TIME;
	private float homeSecondsLeft = START_TIME;
	private BitmapFont bitmapFont;
	
	private KingLayout kingLayout;
	private KingCollisionHelper colHelper;
	
	public KingScreen(CarPartA game) {
		super(game);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void preCarRender(float delta)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void customRender(float delta)
	{
		if (this.proccessingGameOver)
		{
			if (processGameOverTimer(delta))
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
		
		colHelper = new KingCollisionHelper();
		
		this.world.setContactListener(colHelper);
		
		Random r = new Random();
		int index = r.nextInt(getVehicles().size());
		
		Vehicle v = getVehicles().get(index);
		
		v.king = true;
		colHelper.currentKingVehicle = v;
		colHelper.currentKingSide = v.player.team;
		
		for (SimpleAi ai : this.game.controllerManager.getAi())
		{
			ai.resetAiBetweenLevels();
		}
	}
	
	@Override
	protected void renderUpdates(float delta)
	{

		updateTimer(delta);

		if (this.proccessingGameOver)
		{
			this.finishMessage.draw(spriteBatch);
		}
		
	}
	
	@Override
	public void calculateAiMovements(float delta)
	{
		Vector2 desiredCoOrd = null;
		
		for (SimpleAi ai : this.game.controllerManager.getAi())
		{
			if (ai.getVehicle().king)
			{
				Vector2 temp = ai.getVehicle().body.getWorldCenter();
				
				desiredCoOrd = new Vector2();
				
				if (temp.x < worldWidth / 4)
				{
					// Over on the left
					desiredCoOrd.x = worldWidth;
					
				}
				else if (temp.x > (worldWidth / 4) * 3)
				{
					desiredCoOrd.x = 0f;
				}
				else
				{
					if (ai.desiredPos != null && ai.desiredPos.position != null)
					{
						desiredCoOrd.x = ai.desiredPos.position.x;
					}
					else
					{
						desiredCoOrd.x = 0;
					}
					
				}
				
				if (temp.y < worldHeight / 4)
				{
					desiredCoOrd.y = worldHeight;
				}
				else if (temp.y > (worldHeight / 4) * 3)
				{
					desiredCoOrd.y = 0f;
				}
				else
				{
					if (ai.desiredPos != null && ai.desiredPos.position != null)
					{
						desiredCoOrd.y = ai.desiredPos.position.y;
					}
					else
					{
						desiredCoOrd.y = 0;
					}
					
				}
			}
			else if (ai.getVehicle().player.team != colHelper.currentKingSide)
			{
				desiredCoOrd = colHelper.currentKingVehicle.body
						.getWorldCenter();
			}
			else if (ai.getVehicle().player.team == Team.Home)
			{
				for (Vehicle v : getVehicles())
				{
					if (!v.player.controls.isAi()
							&& (v.player.team == Team.Away))
					{
						desiredCoOrd = v.body.getWorldCenter();
						break;
					}
				}
			}
			else
			{
				for (Vehicle v : getVehicles())
				{
					if (!v.player.controls.isAi()
							&& (v.player.team == Team.Home))
					{
						desiredCoOrd = v.body.getWorldCenter();
						break;
					}
				}
			}
			
			if (desiredCoOrd != null)
			{
				ai.setDesiredCoOrd(desiredCoOrd);
				ai.calculateMove(delta);
			}
		}
	}
	
	private void handleWin()
	{
		this.gameOverCoolOffTimer = 5.0f;
		this.proccessingGameOver = true;
		this.colHelper.enableChange = false;
	}
	
	private String timeLeft;
	
	private void updateTimer(float delta)
	{
		// Set for blue
		bitmapFont.setColor(0.3f, 0.5f, 1f, 1.0f);
		
		timeLeft = calculateHomeTimeLeft(delta,
				(colHelper.currentKingSide == Team.Home));
		
		bitmapFont.draw(spriteBatch, timeLeft,
				(screenWidth / 4) - bitmapFont.getBounds(timeLeft).width / 2,
				screenHeight - 10);
		
		// Set for blue
		bitmapFont.setColor(1f, 0.3f, 0.3f, 1.0f);
		
		timeLeft = calculateAwayTimeLeft(delta,
				(colHelper.currentKingSide == Team.Away));
		
		bitmapFont.draw(spriteBatch, timeLeft, (screenWidth / 4) * 3
				- bitmapFont.getBounds(timeLeft).width / 4, screenHeight - 10);
	}
	
	private String calculateHomeTimeLeft(float secondsPast, boolean countDown)
	{
		if (!this.proccessingGameOver && countDown && !this.showDescriptionScreen)
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
	
	private String calculateAwayTimeLeft(float secondsPast, boolean countDown)
	{
		if (!this.proccessingGameOver && countDown && !this.showDescriptionScreen)
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

	@Override
	public DescriptionScreenInfo generateScreenInfo() 
	{
		DescriptionScreenInfo info = new DescriptionScreenInfo();
		
		info.title = "Car King";
		info.descriptionText = "It is good to be the king!";
		
		info.howToWinText = new ArrayList<String>();
		info.howToWinText.add("Take the crown by hitting into the car with it.");
		info.howToWinText.add("Each team's timer will only count when they have the crown.");
		info.howToWinText.add("The first team who's timer reaches 0 wins.");
		// TODO Auto-generated method stub
		return info;
	}
	
}