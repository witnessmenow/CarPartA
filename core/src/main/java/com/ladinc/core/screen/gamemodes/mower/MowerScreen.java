package com.ladinc.core.screen.gamemodes.mower;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.ladinc.core.CarPartA;
import com.ladinc.core.ai.SimpleAi;
import com.ladinc.core.assets.Art;
import com.ladinc.core.screen.gamemodes.GameModeMetaInfo;
import com.ladinc.core.screen.gamemodes.GenericScreen;
import com.ladinc.core.screen.gamemodes.IGenericLayout;
import com.ladinc.core.screen.gamemodes.GameModeMetaInfo.GameMode;
import com.ladinc.core.screen.gamemodes.mazes.MazeScreen;
import com.ladinc.core.screen.gamemodes.painter.PainterCollisionHelper;
import com.ladinc.core.screen.gamemodes.pong.PongScreen;
import com.ladinc.core.utilities.Enums.Team;
import com.ladinc.core.ux.DescriptionScreenInfo;

public class MowerScreen extends GenericScreen 
{
	private MowerLayout mowerLayout;
	private PainterCollisionHelper colHelper;
	
	public MowerScreen(CarPartA game) {
		super(game);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void preCarRender(float delta)
	{
		mowerLayout.drawSpritesForTiles(spriteBatch, PIXELS_PER_METER);
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
		mowerLayout = new MowerLayout(world, worldWidth, worldHeight, center, 0);
		return mowerLayout;
	}
	
	@Override
	public void initGame()
	{
		gameMode = GameMode.Mower;
		
		assignTeamSpritesToCars();
		// Should be assigning mower sprites
		
		// For the moment the paint collider will do the same job
		colHelper = new PainterCollisionHelper();
		world.setContactListener(colHelper);
		
		this.backgroundSprite = Art.getSprite(Art.MOWER_BACKGROUND);
		this.backgroundSprite.setPosition(0, 0);
		
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
			if (ai.assignedTile == null || ai.assignedTile.assigned
					|| ai.timeOnTile > 2f)
			{
				Random r = new Random();
				int index = 0;
				if (ai.getVehicle().player.team == Team.Home)
				{
					if (mowerLayout.availableHomeFloorSensors != null
							&& mowerLayout.availableHomeFloorSensors.size() > 0)
					{
						index = r.nextInt(mowerLayout.availableHomeFloorSensors
								.size());
						
						ai.setFloorTileSensor(mowerLayout.availableHomeFloorSensors
								.get(index));
					}
					
				}
				else
				{
					if (mowerLayout.availableAwayFloorSensors != null
							&& mowerLayout.availableAwayFloorSensors.size() > 0)
					{
						index = r.nextInt(mowerLayout.availableAwayFloorSensors
								.size());
						
						ai.setFloorTileSensor(mowerLayout.availableAwayFloorSensors
								.get(index));
					}
				}
			}
			
			if (ai.assignedTile != null)
			{
				ai.setDesiredCoOrd(ai.assignedTile.body.getWorldCenter());
				ai.calculateMove(delta);
			}
		}
	}
	
	private void handleWin()
	{
		this.gameOverCoolOffTimer = 5.0f;
		this.proccessingGameOver = true;
		
		this.colHelper.enableTileChange = false;
		
		this.incrementGameCount();
		
	}
	
	@Override
	protected void renderUpdates(float delta)
	{
		if (!proccessingGameOver)
		{
			if (mowerLayout.homeScore >= MowerLayout.TARGET_SCORE)
			{
				this.teamWhoWon = Team.Home;
				handleWin();
			}
			else if (mowerLayout.awayScore >= MowerLayout.TARGET_SCORE)
			{
				this.teamWhoWon = Team.Away;
				handleWin();
			}
		}
		else
		{
			this.finishMessage.draw(spriteBatch);
		}
		
	}

	@Override
	public DescriptionScreenInfo getScreenInfo() 
	{
		return MowerScreen.generateScreenInfo();
	}
	
	public static DescriptionScreenInfo generateScreenInfo()
	{
		DescriptionScreenInfo info = new DescriptionScreenInfo();
		
		info.title = "Motor Mower";
		info.descriptionText = "Lawn Mowing. (WIP - some imagination needed!)";
		
		info.howToWinText = new ArrayList<String>();
		info.howToWinText.add("Cut each section of grass on your teams side by driving over it.");
		info.howToWinText.add("The first team to cut all their grass wins.");
		// TODO Auto-generated method stub
		return info;
	}
	
	public static GameModeMetaInfo getMetaInfo()
	{
		return new GameModeMetaInfo("Motor Mower", MowerScreen.generateScreenInfo(), GameMode.Mower, true);
	}
	
}
