package com.ladinc.core.screen.gamemodes.mazes;

import com.ladinc.core.CarPartA;
import com.ladinc.core.ai.SimpleAi;
import com.ladinc.core.assets.Art;
import com.ladinc.core.screen.gamemodes.GenericScreen;
import com.ladinc.core.screen.gamemodes.carpool.CarPoolScreen;

public class MazeScreen extends GenericScreen {
	
	private MazeLayout mazeLayout;
	
	public MazeScreen(CarPartA game) {
		super(game);
	}
	
	@Override
	public MazeLayout resetLayout()
	{
		mazeLayout = new MazeLayout(world, worldWidth, worldHeight, center, 4);
		return mazeLayout;
	}
	
	@Override
	protected void renderUpdates(float delta)
	{
		if(this.proccessingGameOver)
		{
			this.finishMessage.draw(spriteBatch);
		}
	}
	
	@Override
	public void initGame()
	{
		assignTeamSpritesToCars();
		
		this.backgroundSprite = Art.getSprite(Art.RACE_BACKGROUND_1);
		this.backgroundSprite.setPosition(0, 0);
		
		for (SimpleAi ai : this.game.controllerManager.getAi())
		{
			ai.resetTimers();
			ai.justAccelerateAndReverse = false;
		}
		
	}
	
	private void handleWin()
	{
    	this.gameOverCoolOffTimer = 5.0f;
    	this.proccessingGameOver = true;
	}
	
	@Override
	public void calculateAiMovements(float delta)
	{	
		for (SimpleAi ai : this.game.controllerManager.getAi())
		{
			//ai.setDesiredPosition(aiMove);
			ai.setDesiredCoOrd(this.mazeLayout.calculateAiTarget(ai.getVehicle().body.getWorldCenter()));
			ai.calculateMove(delta);
		}
	}

	@Override
	public void customRender(float delta) 
	{
		if(!this.proccessingGameOver)
		{
			if (this.mazeLayout.checkForWinForAllCars(getVehicles()) != null)
			{
				handleWin();
			}
		}
		else
		{
			if(processGameOverTimer(delta))
			{
				handleGameOver();
			}
		}
		
	}
	
	private void handleGameOver()
	{
		this.game.setScreen(new CarPoolScreen(game));
		dispose();
	}

	@Override
	public void preCarRender(float delta) {
		// TODO Auto-generated method stub
		
	}
}