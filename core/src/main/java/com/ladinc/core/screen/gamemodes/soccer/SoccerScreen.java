package com.ladinc.core.screen.gamemodes.soccer;

import com.ladinc.core.CarPartA;
import com.ladinc.core.screen.gamemodes.GenericLayout;
import com.ladinc.core.screen.gamemodes.GenericScreen;

public class SoccerScreen extends GenericScreen {
	
	public SoccerScreen(CarPartA game) {
		super(game);
	}
	
	@Override
	public GenericLayout resetLayout()
	{
		return new SoccerLayout(world, worldWidth, worldHeight, center, 4);
	}
}