package com.ladinc.core.screen.gamemodes.nobrakes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.core.objects.StartingPosition;
import com.ladinc.core.screen.gamemodes.GenericLayout;
import com.ladinc.core.utilities.Enums.Team;

public class NoBrakesLayout extends GenericLayout
{

	public NoBrakesLayout(World world, float worldWidth, float worldHeight,
			Vector2 center, int numberOfInnerWalls) 
	{
		super(world, worldWidth, worldHeight, center, numberOfInnerWalls);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createWorld(World world) 
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public StartingPosition getPlayerStartPoint(Team team, int playerTeamNumber)
	{
//		if (team == Team.Away)
//		{
//			return getAwayStartingPosisiton(playerTeamNumber);
//		}
//		else
//		{
//			return getHomeStartingPosition(playerTeamNumber);
//		}
		
		return new StartingPosition(getCenter(), 0);
	}

}
