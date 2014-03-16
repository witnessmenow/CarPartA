package com.ladinc.core.screen.gamemodes.carpool;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.core.objects.PoolTable;
import com.ladinc.core.objects.StartingPosition;
import com.ladinc.core.screen.gamemodes.GenericLayout;
import com.ladinc.core.utilities.Enums.Team;

public class CarPoolLayout extends GenericLayout
{
	public PoolTable poolTable;
	
	public CarPoolLayout(World world, float worldWidth, float worldHeight,
			Vector2 center, int numberOfInnerWalls) 
	{
		super(world, worldWidth, worldHeight, center, numberOfInnerWalls);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createWorld(World world) 
	{
		this.poolTable = new PoolTable(world, getWorldHeight(), getWorldWidth(), getCenter());
		
	}
	
	@Override
	public StartingPosition getPlayerStartPoint(Team team, int playerTeamNumber)
	{
		return this.poolTable.getStartPosition(team, playerTeamNumber);
	}

}
