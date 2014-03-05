package com.ladinc.core.screen.gamemodes.teamselect;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.core.objects.StartingPosition;
import com.ladinc.core.objects.TeamSelectArea;
import com.ladinc.core.screen.gamemodes.GenericLayout;

public class TeamSelectLayout extends GenericLayout
{

	private static final float GAP_FROM_EDGE = 8f;
	private static final float PLAYER_GAP_X = 8f; //Distance between cars
	private static final float PLAYER_GAP_Y = 8f; //Distance from Center
	
	public TeamSelectArea teamSelectArea;
	
	public TeamSelectLayout(World world, float worldWidth, float worldHeight,
			Vector2 center, int numberOfInnerWalls) {
		super(world, worldWidth, worldHeight, center, numberOfInnerWalls);
		// TODO Auto-generated constructor stub
	}

	@Override
	public StartingPosition getTopStartPoint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StartingPosition getBottomStartPoint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createWorld(World world) 
	{
		this.teamSelectArea = new TeamSelectArea(world, getWorldWidth(), getWorldHeight(), getCenter(), GAP_FROM_EDGE, PLAYER_GAP_X, PLAYER_GAP_Y);	
	}

}
