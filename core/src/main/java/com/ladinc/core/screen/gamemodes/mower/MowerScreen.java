package com.ladinc.core.screen.gamemodes.mower;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.ladinc.core.CarPartA;
import com.ladinc.core.ai.SimpleAi;
import com.ladinc.core.assets.Art;
import com.ladinc.core.objects.FloorTileSensor;
import com.ladinc.core.screen.gamemodes.GenericScreen;
import com.ladinc.core.screen.gamemodes.IGenericLayout;
import com.ladinc.core.screen.gamemodes.painter.PainterCollisionHelper;
import com.ladinc.core.screen.gamemodes.painter.PainterLayout;
import com.ladinc.core.screen.gamemodes.pong.PongScreen;
import com.ladinc.core.utilities.Enums.Team;
import com.ladinc.core.vehicles.Vehicle;

public class MowerScreen extends GenericScreen
{
	private MowerLayout mowerLayout;
	private PainterCollisionHelper colHelper;

	public MowerScreen(CarPartA game) 
	{
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
		this.game.setScreen(new PongScreen(game));
		dispose();
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
		assignTeamSpritesToCars();
		//Should be assigning mower sprites
		
		//For the moment the paint collider will do the same job
		colHelper =  new PainterCollisionHelper();
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
			if(ai.assignedTile == null || ai.assignedTile.assigned || ai.timeOnTile > 2f)
			{
				Random r = new Random();
				int index = 0;
				if(ai.getVehicle().player.team == Team.Home)
				{
					if(mowerLayout.availableHomeFloorSensors != null && mowerLayout.availableHomeFloorSensors.size() > 0)
					{
						index = r.nextInt(mowerLayout.availableHomeFloorSensors.size());
						
						ai.setFloorTileSensor(mowerLayout.availableHomeFloorSensors.get(index));
					}
					
				}
				else
				{
					if(mowerLayout.availableAwayFloorSensors != null && mowerLayout.availableAwayFloorSensors.size() > 0)
					{
						index = r.nextInt(mowerLayout.availableAwayFloorSensors.size());
						
						ai.setFloorTileSensor(mowerLayout.availableAwayFloorSensors.get(index));
					}
				}
			}
			

			
			if(ai.assignedTile != null)
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
		
	}

	@Override
	protected void renderUpdates(float delta) 
	{
		if(!proccessingGameOver)
		{
			if(mowerLayout.homeScore >= MowerLayout.TARGET_SCORE)
			{
				handleWin();
			}
			else if(mowerLayout.awayScore >= MowerLayout.TARGET_SCORE)
			{
				handleWin();
			}
		}
		else
		{
			this.finishMessage.draw(spriteBatch);
		}
		
	}

}
