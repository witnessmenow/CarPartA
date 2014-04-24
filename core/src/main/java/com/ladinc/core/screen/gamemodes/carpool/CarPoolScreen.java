package com.ladinc.core.screen.gamemodes.carpool;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.ladinc.core.CarPartA;
import com.ladinc.core.ai.SimpleAi;
import com.ladinc.core.objects.balls.PoolBall;
import com.ladinc.core.objects.balls.PoolBall.ColourBall;
import com.ladinc.core.screen.gamemodes.GameModeMetaInfo;
import com.ladinc.core.screen.gamemodes.GenericScreen;
import com.ladinc.core.screen.gamemodes.IGenericLayout;
import com.ladinc.core.screen.gamemodes.GameModeMetaInfo.GameMode;
import com.ladinc.core.screen.gamemodes.soccer.SoccerScreen;
import com.ladinc.core.utilities.Enums.Team;
import com.ladinc.core.ux.DescriptionScreenInfo;

public class CarPoolScreen extends GenericScreen 
{
	private CarPoolLayout carPoolLayout;
	private ColourBall colourIntertedIn;
	private float shortestDistanceBetweenAiAndBall = 0.0f;
	
	public CarPoolScreen(CarPartA game) {
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
		if (this.carPoolLayout.poolTable.checkForWin()
				&& !this.proccessingGameOver)
		{
			handleWin();
		}
		
		if (!this.proccessingGameOver)
		{
			this.carPoolLayout.poolTable.checkForPottedBall();
		}
		else
		{
			if (processGameOverTimer(delta))
			{
				handleGameOver();
			}
		}
		
		this.carPoolLayout.poolTable.updateBalls();
		
	}
	
	@Override
	public IGenericLayout resetLayout()
	{
		this.carPoolLayout = new CarPoolLayout(world, worldWidth, worldHeight,
				center, 0);
		return this.carPoolLayout;
	}
	
	@Override
	public void initGame()
	{
		gameMode = GameMode.CarPool;
		
		this.colHelper = new CarPoolColisionHelper();
		world.setContactListener(this.colHelper);
		
		this.backgroundSprite = this.carPoolLayout.poolTable
				.getPoolTableSprite();
		this.backgroundSprite.setPosition(0.0f, 0.0f);
		
		assignTeamSpritesToCars();
		
		this.carPoolLayout.poolTable.createPoolBalls(world,
				this.carPoolLayout.poolTable.getTriangleStartingPos());
		
		for (SimpleAi ai : this.game.controllerManager.getAi())
		{
			ai.resetAiBetweenLevels();
		}
		
	}
	
	@Override
	public void calculateAiMovements(float delta)
	{
		
		for (SimpleAi ai : this.game.controllerManager.getAi())
		{
			this.shortestDistanceBetweenAiAndBall = 0.0f;
			
			colourIntertedIn = ColourBall.Red;
			
			Vector2 carPos = ai.getVehicle().body.getWorldCenter();
			
			Vector2 desiredCoOrd = null;
			
			if (ai.team == Team.Home)
				colourIntertedIn = ColourBall.Yellow;
			
			for (PoolBall pb : this.carPoolLayout.poolTable.activeBalls)
			{
				if (pb.colour == colourIntertedIn)
				{
					desiredCoOrd = pb.body.getWorldCenter();
					break;
					
					// Originally was going to make it chase the closest ball,
					// but it actually works better if he focusing on one ball
					// at a time.
					
					// float tempDistanceBetweenBallAndAi =
					// pb.body.getWorldCenter().dst2(carPos);
					// if(tempDistanceBetweenBallAndAi <
					// shortestDistanceBetweenAiAndBall ||
					// shortestDistanceBetweenAiAndBall == 0.0f )
					// {
					// shortestDistanceBetweenAiAndBall =
					// tempDistanceBetweenBallAndAi;
					// desiredCoOrd = pb.body.getWorldCenter();
					// }
				}
			}
			
			ai.setDesiredCoOrd(desiredCoOrd);
			ai.calculateMove(delta);
		}
	}
	
	@Override
	protected void renderUpdates(float delta)
	{
		this.carPoolLayout.poolTable.updateBallSprites(spriteBatch,
				PIXELS_PER_METER);
		if (this.proccessingGameOver)
		{
			this.finishMessage.draw(spriteBatch);
		}
	}
	
	private void handleWin()
	{
		this.gameOverCoolOffTimer = 5.0f;
		this.proccessingGameOver = true;
	}
	
	private void handleGameOver()
	{
		this.game.setScreen(new SoccerScreen(game));
		dispose();
	}

	@Override
	public DescriptionScreenInfo getScreenInfo() 
	{
		return CarPoolScreen.generateScreenInfo();
	}
	
	public static DescriptionScreenInfo generateScreenInfo()
	{
		DescriptionScreenInfo info = new DescriptionScreenInfo();
		
		info.title = "Car-Pool";
		info.descriptionText = "Pool/Billards with cars.";
		
		info.howToWinText = new ArrayList<String>();
		info.howToWinText.add("Pot your team's colour balls, as shown on the top of the screen.");
		info.howToWinText.add("The first team to pot all their balls wins.");
		info.howToWinText.add("Cars that are potted will respawn.");
		info.howToWinText.add("");
		info.howToWinText.add("Black ball is mysterious .... tbh it currently does nothing.");
		// TODO Auto-generated method stub
		return info;
	}
	
	public static GameModeMetaInfo getMetaInfo()
	{
		return new GameModeMetaInfo("Car-Pool", CarPoolScreen.generateScreenInfo(), GameMode.CarPool, true);
	}
}