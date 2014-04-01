package com.ladinc.core.screen.gamemodes.king;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.core.objects.BoxProp;
import com.ladinc.core.objects.StartingPosition;
import com.ladinc.core.screen.gamemodes.GenericLayout;
import com.ladinc.core.utilities.Enums.Team;

public class KingLayout extends GenericLayout
{

	private static final float GAP_BETWEEN_CARS = 12f;
	private static final float CAR_START_GAP_FROM_EDGE = 35f;
	
	private static final float GAP_BETWEEN_SIDEWALL_AND_EDGE = 10.5f;
	private static final float GAP_BETWEEN_TOPBOTTOMWALL_AND_EDGE = 8.5f;
	
	public KingLayout(World world, float worldWidth, float worldHeight,
			Vector2 center, int numberOfInnerWalls) 
	{
		super(world, worldWidth, worldHeight, center, numberOfInnerWalls);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createWorld(World world) 
	{
		new BoxProp(world, getWorldWidth(), 1, new Vector2 (getWorldWidth()/2, GAP_BETWEEN_TOPBOTTOMWALL_AND_EDGE));//left
		new BoxProp(world, getWorldWidth(), 1, new Vector2 (getWorldWidth()/2, getWorldHeight() - GAP_BETWEEN_TOPBOTTOMWALL_AND_EDGE));
		
		new BoxProp(world, 1, getWorldHeight(), new Vector2 (GAP_BETWEEN_SIDEWALL_AND_EDGE, getWorldHeight()/2));//left
		new BoxProp(world, 1, getWorldHeight(), new Vector2 (getWorldWidth() - GAP_BETWEEN_SIDEWALL_AND_EDGE, getWorldHeight()/2));
		
	}
	
	@Override
	public StartingPosition getPlayerStartPoint(Team team, int playerTeamNumber)
	{
		if(team == Team.Away)
		{
			return getAwayStartingPosisiton(playerTeamNumber);
		}
		else
		{
			return getHomeStartingPosition(playerTeamNumber);
		}
	}
	
	private StartingPosition getHomeStartingPosition(int playerTeamNumber)
	{
		//Face Right
		float direction = (float) Math.PI/2;
		float x = CAR_START_GAP_FROM_EDGE;
		float y = this.getCenter().y;
		
		//0 is ok with defaults
		if(playerTeamNumber != 0)
		{
			//because we are ingoring 0, we need to offset the rest of the values
			playerTeamNumber -= 1;
			
			int dir = 0;
			if(playerTeamNumber % 2 == 0)
			{
				dir = 1;
			}
			else
			{
				dir = -1;
			}
			
			y = y + ((dir)*((float)(((playerTeamNumber/2) + 1)*GAP_BETWEEN_CARS)));
		}
		
		return new StartingPosition(new Vector2(x, y), direction);
	}
	
	private StartingPosition getAwayStartingPosisiton(int playerTeamNumber)
	{
		//Face Left
		float direction = (float) (Math.PI + Math.PI/2);
		float x = this.getWorldWidth() - CAR_START_GAP_FROM_EDGE;
		float y = this.getCenter().y;
		
		//0 is ok with defaults
		if(playerTeamNumber != 0)
		{
			//because we are ingoring 0, we need to offset the rest of the values
			playerTeamNumber -= 1;
			
			int dir = 0;
			if(playerTeamNumber % 2 == 0)
			{
				dir = -1;
			}
			else
			{
				dir = +1;
			}
			
			y = y + ((dir)*((float)(((playerTeamNumber/2) + 1)*GAP_BETWEEN_CARS)));
		}
		
		return new StartingPosition(new Vector2(x, y), direction);
	}
	

}
