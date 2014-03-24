package com.ladinc.core.screen.gamemodes.carpool;

import com.badlogic.gdx.math.Vector2;
import com.ladinc.core.CarPartA;
import com.ladinc.core.ai.SimpleAi;
import com.ladinc.core.collision.CollisionHelper;
import com.ladinc.core.objects.balls.PoolBall;
import com.ladinc.core.objects.balls.PoolBall.ColourBall;
import com.ladinc.core.screen.gamemodes.GenericScreen;
import com.ladinc.core.screen.gamemodes.IGenericLayout;
import com.ladinc.core.screen.gamemodes.soccer.SoccerScreen;
import com.ladinc.core.screen.gamemodes.teamselect.TeamSelectLayout;
import com.ladinc.core.utilities.Enums.Team;

public class CarPoolScreen extends GenericScreen
{

	private CarPoolLayout carPoolLayout;
	
	public CarPoolScreen(CarPartA game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void preCarRender(float delta) {
		// TODO Auto-generated method stub
		
	}
	
	private void handleWin()
	{
    	this.gameOverCoolOffTimer = 5.0f;
    	this.proccessingGameOver = true;
	}

	@Override
	public void customRender(float delta) 
	{
		if(this.carPoolLayout.poolTable.checkForWin() && !this.proccessingGameOver)
		{
			handleWin();
		}
		
		if(!this.proccessingGameOver)
		{
			this.carPoolLayout.poolTable.checkForPottedBall();
		}
		else
		{
			if(processGameOverTimer(delta))
			{
				handleGameOver();
			}
		}
		
		this.carPoolLayout.poolTable.updateBalls();
		
	}
	
	private void handleGameOver()
	{
		this.game.setScreen(new SoccerScreen(game));
		dispose();
	}

	@Override
	public IGenericLayout resetLayout() 
	{
		this.carPoolLayout = new CarPoolLayout(world, worldWidth, worldHeight, center, 0);
		return this.carPoolLayout;
	}

	@Override
	public void initGame() 
	{
		this.colHelper = new CarPoolColisionHelper();
		world.setContactListener(this.colHelper);
		
		this.backgroundSprite = this.carPoolLayout.poolTable.getPoolTableSprite();
		this.backgroundSprite.setPosition(0.0f, 0.0f);
		
		assignTeamSpritesToCars();
		
		this.carPoolLayout.poolTable.createPoolBalls(world, this.carPoolLayout.poolTable.getTriangleStartingPos());
		
		for (SimpleAi ai : this.game.controllerManager.getAi())
		{
			ai.resetTimers();
			ai.justAccelerateAndReverse = false;
		}
		
	}
	
	private float shortestDistanceBetweenAiAndBall = 0.0f;
	private ColourBall colourIntertedIn;
	
	
	@Override
	public void calculateAiMovements(float delta)
	{
		
		for (SimpleAi ai : this.game.controllerManager.getAi())
		{
			this.shortestDistanceBetweenAiAndBall = 0.0f;
			
			colourIntertedIn = ColourBall.Red;
			
			Vector2 carPos = ai.getVehicle().body.getWorldCenter();
			
			Vector2 desiredCoOrd = null;
			
			if(ai.team == Team.Home)
				colourIntertedIn = ColourBall.Yellow;
			
			for(PoolBall pb : this.carPoolLayout.poolTable.activeBalls)
			{
				if(pb.colour == colourIntertedIn)
				{
					desiredCoOrd = pb.body.getWorldCenter();
					break;
					
					//Originally was going to make it chase the closest ball, but it actually works better if he focusing on one ball at a time.
					
//					float tempDistanceBetweenBallAndAi = pb.body.getWorldCenter().dst2(carPos);
//					if(tempDistanceBetweenBallAndAi < shortestDistanceBetweenAiAndBall || shortestDistanceBetweenAiAndBall == 0.0f )
//					{
//						shortestDistanceBetweenAiAndBall = tempDistanceBetweenBallAndAi;
//						desiredCoOrd = pb.body.getWorldCenter();
//					}
				}
			}
			
			ai.setDesiredCoOrd(desiredCoOrd);
			ai.calculateMove(delta);
		}
	}

	@Override
	protected void renderUpdates(float delta) 
	{
		this.carPoolLayout.poolTable.updateBallSprites(spriteBatch, PIXELS_PER_METER);	
		if(this.proccessingGameOver)
		{
			this.finishMessage.draw(spriteBatch);
		}
	}

}
