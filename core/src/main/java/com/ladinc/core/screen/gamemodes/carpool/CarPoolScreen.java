package com.ladinc.core.screen.gamemodes.carpool;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.ladinc.core.CarPartA;
import com.ladinc.core.ai.SimpleAi;
import com.ladinc.core.assets.CarsHelper;
import com.ladinc.core.objects.balls.PoolBall;
import com.ladinc.core.objects.balls.PoolBall.ColourBall;
import com.ladinc.core.screen.gamemodes.GameModeMetaInfo;
import com.ladinc.core.screen.gamemodes.GenericScreen;
import com.ladinc.core.screen.gamemodes.IGenericLayout;
import com.ladinc.core.screen.gamemodes.GameModeMetaInfo.GameMode;
import com.ladinc.core.screen.gamemodes.soccer.SoccerScreen;
import com.ladinc.core.utilities.Enums.Team;
import com.ladinc.core.ux.DescriptionScreenInfo;
import com.ladinc.core.vehicles.Vehicle;

public class CarPoolScreen extends GenericScreen 
{
	private CarPoolLayout carPoolLayout;
	private ColourBall colourIntertedIn;
	private float shortestDistanceBetweenAiAndBall = 0.0f;
	
	private Sprite homeTeamCarSprite;
	private Sprite homeTeamBallSprite;
	
	private Sprite awayTeamCarSprite;
	private Sprite awayTeamBallSprite;

	
	CarPoolColisionHelper poolColHelper;
	
	public CarPoolScreen(CarPartA game) {
		super(game);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void preCarRender(float delta)
	{
		// TODO Auto-generated method stub
		
	}
	
	private void handleBlackPotted(Team team)
	{
		if(team == Team.Home)
		{
			for(Vehicle v : this.getHomeVehicles())
			{
				v.respawnTimeRemaining = 5f;
			}
		}
		else if(team == Team.Away)
		{
			for(Vehicle v : this.getAwayVehicles())
			{
				v.respawnTimeRemaining = 5f;
			}
		}
		
	}
	
	@Override
	public void customRender(float delta)
	{
		if(this.poolColHelper.blackPotted)
		{
			this.poolColHelper.blackPotted = false;
			handleBlackPotted(this.poolColHelper.lastTeamToTouchBlack);
			
		}
		
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
		
		poolColHelper = new CarPoolColisionHelper();
		this.colHelper = poolColHelper;
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
		
		homeTeamCarSprite = new Sprite(CarsHelper.getTeamCar(Team.Home));
		awayTeamCarSprite = new Sprite(CarsHelper.getTeamCar(Team.Away));
		
		homeTeamCarSprite.setRotation(MathUtils.radiansToDegrees * ((float) Math.PI/2));
		homeTeamCarSprite.setPosition(this.screenWidth/4 - 130f, screenHeight - 110f);
		
		awayTeamCarSprite.setRotation(MathUtils.radiansToDegrees * ((float) Math.PI/2) * 3);
		awayTeamCarSprite.setPosition(this.screenWidth/4 + 210f, screenHeight - 110f);
		
		homeTeamBallSprite = new Sprite(PoolBall.getYellowBallSprite());
		awayTeamBallSprite = new Sprite(PoolBall.getRedBallSprite());
		
		awayTeamBallSprite.setPosition(this.screenWidth/4 + 100f, screenHeight - 85f);
		
		homeTeamBallSprite.setPosition(this.screenWidth/4 - 20f, screenHeight - 85f);
		
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
		
		drawInfo(spriteBatch);
		
		if (this.proccessingGameOver)
		{
			this.finishMessage.draw(spriteBatch);
		}
	}
	
	private void handleWin()
	{
		this.teamWhoWon = this.carPoolLayout.poolTable.winningTeam;
		
		incrementGameCount();
		
		this.gameOverCoolOffTimer = 5.0f;
		this.proccessingGameOver = true;
	}
	
	public void drawInfo(SpriteBatch sb)
	{
		
		homeTeamCarSprite.draw(sb);
		awayTeamCarSprite.draw(sb);
		
		homeTeamBallSprite.draw(sb);
		awayTeamBallSprite.draw(sb);
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
		info.howToWinText.add("Potting the black ball will pot your entire team.");
		// TODO Auto-generated method stub
		return info;
	}
	
	public static GameModeMetaInfo getMetaInfo()
	{
		return new GameModeMetaInfo("Car-Pool", CarPoolScreen.generateScreenInfo(), GameMode.CarPool, true);
	}
}