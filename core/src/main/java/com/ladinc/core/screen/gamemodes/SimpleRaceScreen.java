package com.ladinc.core.screen.gamemodes;

import com.ladinc.core.CarPartA;
import com.ladinc.core.screen.gamemodes.mazes.MazeLayout;
import com.ladinc.core.ux.DescriptionScreenInfo;

public class SimpleRaceScreen extends GenericScreen {
	
	public SimpleRaceScreen(CarPartA game) {
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

	@Override
	public void customRender(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preCarRender(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DescriptionScreenInfo getScreenInfo() {
		// TODO Auto-generated method stub
		return null;
	}
}