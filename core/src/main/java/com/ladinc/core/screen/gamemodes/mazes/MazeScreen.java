package com.ladinc.core.screen.gamemodes.mazes;

import com.ladinc.core.CarPartA;
import com.ladinc.core.screen.gamemodes.GenericScreen;

public class MazeScreen extends GenericScreen {
	
	public MazeScreen(CarPartA game) {
		super(game);
	}
	
	@Override
	public MazeLayout resetLayout()
	{
		return new MazeLayout(world, worldWidth, worldHeight, center, 4);
	}
	
	@Override
	protected void renderUpdates(float delta)
	{
		// TODO Auto-generated method stub
	}
	
	@Override
	public void initGame()
	{
		// TODO Auto-generated method stub
		
	}
}