package com.ladinc.core.screen.gamemodes.pong;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.core.objects.BoxProp;
import com.ladinc.core.objects.SensorBox;
import com.ladinc.core.objects.StartingPosition;
import com.ladinc.core.screen.gamemodes.GenericLayout;
import com.ladinc.core.utilities.Enums.Team;

public class PongLayout extends GenericLayout
{

	private static final float PLAYER_GAP_X = 6f; //Distance between cars
	private static final float GAP_FROM_EDGE = 8f;
	
	public PongLayout(World world, float worldWidth, float worldHeight,
			Vector2 center, int numberOfInnerWalls) {
		super(world, worldWidth, worldHeight, center, numberOfInnerWalls);
		// TODO Auto-generated constructor stub
	}

	SensorBox homeScoreArea;
	SensorBox awayScoreArea;
	
	@Override
	public void createWorld(World world) {
		
		
		new BoxProp(world, getWorldWidth(), 1, new Vector2 (getWorldWidth()/2, 5f));//left
		new BoxProp(world, getWorldWidth(), 1, new Vector2 (getWorldWidth()/2, getWorldHeight() - 5f));
		
		homeScoreArea = new SensorBox(world, 1, getWorldHeight(), new Vector2 (1f, getWorldHeight()/2), Team.Home);//left
		awayScoreArea = new SensorBox(world, 1, getWorldHeight(), new Vector2 (getWorldWidth() - 1f, getWorldHeight()/2), Team.Away);
		
	}
	
	@Override
	public StartingPosition getPlayerStartPoint(Team team, int playerTeamNumber)
	{
		if(team == Team.Away)
		{
			return new StartingPosition(new Vector2((getWorldWidth() - GAP_FROM_EDGE) - (PLAYER_GAP_X*playerTeamNumber), getCenter().y), 0);
		}
		else
		{
			return new StartingPosition(new Vector2((0 + GAP_FROM_EDGE) + (PLAYER_GAP_X*playerTeamNumber), getCenter().y), 0);
		}
	}

}
