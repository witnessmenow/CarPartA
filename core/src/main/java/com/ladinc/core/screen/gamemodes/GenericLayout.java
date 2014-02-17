package com.ladinc.core.screen.gamemodes;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.core.objects.BoxProp;
import com.ladinc.core.objects.StartingPosition;
import com.ladinc.core.player.PlayerInfo.Team;

public abstract class GenericLayout implements IGenericLayout {
	public boolean gameFinished = false;
	
	private final int numberOfInnerWalls;
	private final Vector2 center;
	private final float gapFromOuterEdge = 1f;
	private final float playerGapX = 7f;
	private final float playerGapY = 7f;
	private final float worldWidth;
	private final float worldHeight;
	private final List<Team> topStartPosList = new ArrayList<Team>();
	private final List<Team> bottomStartPosList = new ArrayList<Team>();
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
	public StartingPosition getPlayerStartPoint(Team team)
	{
		if (getTopStartPosList().size() < getBottomStartPosList().size())
		{
			getTopStartPosList().add(team);
			return getTopStartPoint();
		}
		else if (getBottomStartPosList().size() < getTopStartPosList().size())
		{
			getBottomStartPosList().add(team);
			return getBottomStartPoint();
		}
		else
		{
			// numbers are the same at top and bottom, spreading out the team as
			// best as possible
			int ownTeamTop = countTeamPlayersInPosition(team,
					getTopStartPosList());
			int ownTeamBottom = countTeamPlayersInPosition(team,
					getBottomStartPosList());
			
			if (ownTeamTop < ownTeamBottom)
			{
				getTopStartPosList().add(team);
				return getTopStartPoint();
			}
			else if (ownTeamBottom < ownTeamTop)
			{
				getBottomStartPosList().add(team);
				return getBottomStartPoint();
			}
		}
		
		getTopStartPosList().add(team);
		return getTopStartPoint();
	}
	
	public List<Team> getTopStartPosList()
	{
		return topStartPosList;
	}
	
	public List<Team> getBottomStartPosList()
	{
		return bottomStartPosList;
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
	
	public int getNumberOfInnerWalls()
	{
		return numberOfInnerWalls;
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
	
	private void createOuterWalls(World world, float worldWidth,
			float worldHeight, Vector2 center, float gapFromOuterEdge)
	{
		// outer walls
		BoxProp bottomWall = new BoxProp(world, worldWidth, 1, new Vector2(
				worldWidth / 2, gapFromOuterEdge)); // bottom Wall
		
		BoxProp leftWall = new BoxProp(world, 1, worldHeight, new Vector2(
				gapFromOuterEdge, worldHeight / 2));// left Wall
		
		BoxProp topWall = new BoxProp(world, worldWidth, 1, new Vector2(
				worldWidth / 2, worldHeight - gapFromOuterEdge));// top
		
		BoxProp rightWall = new BoxProp(world, 1, worldHeight, new Vector2(
				worldWidth - gapFromOuterEdge, worldHeight / 2));// left Wall
		
		walls.add(bottomWall);
		walls.add(leftWall);
		walls.add(topWall);
		walls.add(rightWall);
		
	}
	
	private void createInnerWalls(World world, float worldWidth,
			float worldHeight, Vector2 center, float gapFromOuterEdge,
			int numberOfInnerWalls)
	{
		float gapForGettingThrough = 20f;
		float wallLength = worldWidth - gapForGettingThrough;
		BoxProp[] innerWalls = new BoxProp[numberOfInnerWalls];
		
		for (int i = 1; i <= numberOfInnerWalls; i++)
		{
			if (i % 2 == 0)
			{
				// Should be starting from the right
				innerWalls[i - 1] = new BoxProp(world, wallLength, 1,
						new Vector2(gapForGettingThrough + (wallLength / 2), i
								* (worldHeight / (numberOfInnerWalls + 1))));
				// inner wall 2 - starts at right
			}
			else
			{
				innerWalls[i - 1] = new BoxProp(world, wallLength, 1,
						new Vector2((worldWidth - gapForGettingThrough) / 2, i
								* (worldHeight / (numberOfInnerWalls + 1))));
				// innerwall 3 - starts at left
			}
			
			this.walls.add(innerWalls[i - 1]);
		}
	}
	
	// public boolean checkForWin(Vector2 carCenter, String playerName)
	// {
	//
	// //TODO: this is completly based on the center of the car. More advanced
	// things need to be done to notice when the car is actually in the win box,
	// not just its center
	//
	// if(!this.gameFinished)
	// {
	// if(carCenter.x <= (this.center.x - 1))
	// {
	// //Car is too far left
	// return false;
	// }
	//
	// if(carCenter.x >= (this.center.x + 1))
	// {
	// //Car is too far right
	//
	// return false;
	// }
	//
	//
	// if(carCenter.y >= (this.center.y + winBoxSize/2))
	// {
	// //Car is too far up
	// return false;
	// }
	//
	// if(carCenter.y <= (this.center.y - winBoxSize/2))
	// {
	// //Car is too far down
	// return false;
	// }
	//
	//
	// System.out.println(playerName + " Wins!");
	// this.gameFinished = true;
	// return true;
	// }
	//
	// return false;
	//
	// }
}