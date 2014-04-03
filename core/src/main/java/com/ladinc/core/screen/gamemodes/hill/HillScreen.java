package com.ladinc.core.screen.gamemodes.hill;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.ladinc.core.CarPartA;
import com.ladinc.core.ai.SimpleAi;
import com.ladinc.core.assets.Art;
import com.ladinc.core.assets.Font;
import com.ladinc.core.screen.gamemodes.GenericScreen;
import com.ladinc.core.screen.gamemodes.IGenericLayout;
import com.ladinc.core.screen.gamemodes.mower.MowerScreen;

public class HillScreen extends GenericScreen {
	private static final float START_TIME = 30;
	
	private float awaySecondsLeft = START_TIME;
	private float homeSecondsLeft = START_TIME;
	private BitmapFont bitmapFont;
	
	private HillLayout hillLayout;
	private HillCollisionHelper colHelper;
	
	private String timeLeft;
	
	public HillScreen(CarPartA game) {
		super(game);
		// TODO Auto-generated constructor stub
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
	
	@Override
	public IGenericLayout resetLayout()
	{
		hillLayout = new HillLayout(world, worldWidth, worldHeight, center, 0);
		return hillLayout;
	}
	
	@Override
	public void initGame()
	{
		assignTeamSpritesToCars();
		
		this.backgroundSprite = Art.getSprite(Art.PAINTER_BACKGROUND);
		bitmapFont = Font.fontTable.get(Font.CONST_50);
		
		// Setup Collision
		colHelper = new HillCollisionHelper();
		this.world.setContactListener(colHelper);
		
		// Setup Hill spot
		colHelper.currentHillSide = null;
		
		for (SimpleAi ai : this.game.controllerManager.getAi())
		{
			ai.resetAiBetweenLevels();
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
			}/*
			 * else if (ai.getVehicle().player.team !=
			 * colHelper.currentKingSide) { desiredCoOrd =
			 * colHelper.currentKingVehicle.body .getWorldCenter(); } else if
			 * (ai.getVehicle().player.team == Team.Home) { for (Vehicle v :
			 * getVehicles()) { if (!v.player.controls.isAi() && (v.player.team
			 * == Team.Away)) { desiredCoOrd = v.body.getWorldCenter(); break; }
			 * } } else { for (Vehicle v : getVehicles()) { if
			 * (!v.player.controls.isAi() && (v.player.team == Team.Home)) {
			 * desiredCoOrd = v.body.getWorldCenter(); break; } } }
			 */
			if (desiredCoOrd != null)
			{
				ai.setDesiredCoOrd(desiredCoOrd);
				ai.calculateMove(delta);
			}
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
	public void preCarRender(float delta)
	{
		// TODO Auto-generated method stub
	}
	
	private void handleGameOver()
	{
		this.game.setScreen(new MowerScreen(game));
		dispose();
	}
	
	private void handleWin()
	{
		this.gameOverCoolOffTimer = 5.0f;
		this.proccessingGameOver = true;
		this.colHelper.enableChange = false;
	}
	
	private void updateTimer(float delta)
	{
		// Set for blue
		bitmapFont.setColor(0.3f, 0.5f, 1f, 1.0f);
		
		timeLeft = calculateHomeTimeLeft(delta, true);
		// (colHelper.currentKingSide == Team.Home));
		
		bitmapFont.draw(spriteBatch, timeLeft,
				(screenWidth / 4) - bitmapFont.getBounds(timeLeft).width / 2,
				screenHeight - 10);
		
		// Set for blue
		bitmapFont.setColor(1f, 0.3f, 0.3f, 1.0f);
		
		timeLeft = calculateAwayTimeLeft(delta, false);
		// (colHelper.currentKingSide == Team.Away));
		
		bitmapFont.draw(spriteBatch, timeLeft, (screenWidth / 4) * 3
				- bitmapFont.getBounds(timeLeft).width / 4, screenHeight - 10);
	}
	
	private String calculateHomeTimeLeft(float secondsPast, boolean countDown)
	{
		if (!this.proccessingGameOver && countDown)
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
		if (!this.proccessingGameOver && countDown)
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