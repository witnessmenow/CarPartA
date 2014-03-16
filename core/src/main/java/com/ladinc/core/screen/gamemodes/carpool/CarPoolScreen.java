package com.ladinc.core.screen.gamemodes.carpool;

import com.ladinc.core.CarPartA;
import com.ladinc.core.screen.gamemodes.GenericScreen;
import com.ladinc.core.screen.gamemodes.IGenericLayout;
import com.ladinc.core.screen.gamemodes.teamselect.TeamSelectLayout;

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

	@Override
	public void customRender(float delta) 
	{
		this.carPoolLayout.poolTable.updateBalls();
		
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
		this.backgroundSprite = this.carPoolLayout.poolTable.getPoolTableSprite();
		this.backgroundSprite.setPosition(0.0f, 0.0f);
		
		assignTeamSpritesToCars();
		
		this.carPoolLayout.poolTable.createPoolBalls(world);
		
	}

	@Override
	protected void renderUpdates(float delta) 
	{
		this.carPoolLayout.poolTable.updateBallSprites(spriteBatch, PIXELS_PER_METER);	
	}

}
