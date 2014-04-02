package com.ladinc.core.screen.gamemodes;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.core.objects.BoxProp;
import com.ladinc.core.objects.StartingPosition;
import com.ladinc.core.utilities.Enums.Team;

public abstract class GenericLayout implements IGenericLayout {
	public boolean gameFinished = false;
	
	private final int numberOfInnerWalls;
	private final Vector2 center;
	private final float gapFromOuterEdge = 1f;
	private final float playerGapX = 7f;
	private final float playerGapY = 7f;
	private final float worldWidth;
	private final float worldHeight;
	private final List<BoxProp> walls = new ArrayList<BoxProp>();
	
	public GenericLayout(World world, float worldWidth, float worldHeight,
			Vector2 center, int numberOfInnerWalls) {
		this.worldHeight = worldHeight;
		this.worldWidth = worldWidth;
		this.center = center;
		this.numberOfInnerWalls = numberOfInnerWalls;
		createWorld(world);
	}
	
	// TODO: I think this can be simplified
	public StartingPosition getPlayerStartPoint(Team team, int playerTeamNumber)
	{
		// if (getTopStartPosList().size() < getBottomStartPosList().size())
		// {
		// getTopStartPosList().add(team);
		// return getTopStartPoint();
		// }
		// else if (getBottomStartPosList().size() <
		// getTopStartPosList().size())
		// {
		// getBottomStartPosList().add(team);
		// return getBottomStartPoint();
		// }
		// else
		// {
		// // numbers are the same at top and bottom, spreading out the team as
		// // best as possible
		// int ownTeamTop = countTeamPlayersInPosition(team,
		// getTopStartPosList());
		// int ownTeamBottom = countTeamPlayersInPosition(team,
		// getBottomStartPosList());
		//
		// if (ownTeamTop < ownTeamBottom)
		// {
		// getTopStartPosList().add(team);
		// return getTopStartPoint();
		// }
		// else if (ownTeamBottom < ownTeamTop)
		// {
		// getBottomStartPosList().add(team);
		// return getBottomStartPoint();
		// }
		// }
		//
		// getTopStartPosList().add(team);
		// return getTopStartPoint();
		
		return new StartingPosition(getCenter(), 0.0f);
	}
	
	protected int countTeamPlayersInPosition(Team team, List<Team> list)
	{
		int count = 0;
		for (Team t : list)
		{
			if (t == team)
			{
				count++;
			}
		}
		
		return count;
	}
	
	public float getWorldWidth()
	{
		return worldWidth;
	}
	
	public float getWorldHeight()
	{
		return worldHeight;
	}
	
	public float getGapFromOuterEdge()
	{
		return gapFromOuterEdge;
	}
	
	public float getPlayerGapX()
	{
		return playerGapX;
	}
	
	public float getPlayerGapY()
	{
		return playerGapY;
	}
	
	public Vector2 getCenter()
	{
		return center;
	}
	
	public List<BoxProp> getWalls()
	{
		return walls;
	}
	
	public void addWall(BoxProp w)
	{
		this.walls.add(w);
	}
	
	public int getNumberOfInnerWalls()
	{
		return numberOfInnerWalls;
	}
}