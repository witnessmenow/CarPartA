package com.ladinc.core.screen.gamemodes.mazes;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.core.objects.BoxProp;
import com.ladinc.core.objects.StartingPosition;
import com.ladinc.core.player.PlayerInfo.Team;

public class SimpleMaze 
{
	
	public boolean gameFinished = false;
	
	private Vector2 center;
	float winBoxSize;
	
	public List<BoxProp> walls;
	
	public int numberOfInnerWalls;
	
	private float gapFromOuterEdge = 1f;
	private float playerGapX = 7f;
	private float playerGapY = 7f;
	
	private float worldWidth;
	private float worldHeight;
	
	
	private List<Team> topStartPosList;
	private List<Team> bottomStartPosList;
	
	
	
	
	public SimpleMaze(World world, float worldWidth, float worldHeight, Vector2 center, int numInnerWalls)
	{
		this.walls = new ArrayList<BoxProp>();
		
		this.numberOfInnerWalls = numInnerWalls;
		
		this.topStartPosList = new ArrayList<Team>();
		this.bottomStartPosList = new ArrayList<Team>();
		
		this.winBoxSize = worldHeight/(numberOfInnerWalls + 1);
		
		this.center = center;
		
		
		this.worldHeight = worldHeight;
		this.worldWidth = worldWidth;
		
		createOuterWalls(world, worldWidth, worldHeight, center, gapFromOuterEdge);
		
		//make it even!
		createInnerWalls(world, worldWidth, worldHeight, center, gapFromOuterEdge, numberOfInnerWalls);

	}
	
	private StartingPosition getTopStartPoint()
	{
		return new StartingPosition(new Vector2 (worldWidth - (gapFromOuterEdge + playerGapX) , worldHeight - (gapFromOuterEdge + playerGapY)), (float) (Math.PI + Math.PI/2));
	}
	
	private StartingPosition getBottomStartPoint()
	{
		return new StartingPosition(new Vector2 (gapFromOuterEdge + playerGapX , gapFromOuterEdge + playerGapY), (float) Math.PI/2);
	}
	
	public StartingPosition getPlayerStartPoint(Team team)
	{
		if(this.topStartPosList.size() < this.bottomStartPosList.size())
		{
			this.topStartPosList.add(team);
			return getTopStartPoint();
		}
		else if(this.bottomStartPosList.size() < this.topStartPosList.size())
		{
			this.bottomStartPosList.add(team);
			return getBottomStartPoint();
		}
		else
		{
			//numbers are the same at top and bottom, spreading out the team as best as possible
			
			int ownTeamTop = countTeamPlayersInPosition(team, this.topStartPosList);
			int ownTeamBottom = countTeamPlayersInPosition(team, this.bottomStartPosList);
			
			if(ownTeamTop < ownTeamBottom)
			{
				this.topStartPosList.add(team);
				return getTopStartPoint();
			}
			else if(ownTeamBottom < ownTeamTop)
			{
				this.bottomStartPosList.add(team);
				return getBottomStartPoint();
			}	
		}
		
		this.topStartPosList.add(team);
		return getTopStartPoint();
	}
	
	private int countTeamPlayersInPosition(Team team, List<Team> list)
	{
		int count = 0;
		for(Team t : list)
		{
			if(t == team)
				count ++;
		}
		
		return count;
	}
	
//	public boolean checkForWin(Vector2 carCenter, String playerName)
//    {
//		
//		//TODO: this is completly based on the center of the car. More advanced things need to be done to notice when the car is actually in the win box, not just its center
//		
//		if(!this.gameFinished)
//		{
//			if(carCenter.x <= (this.center.x - 1))
//	    	{
//				//Car is too far left
//	    		return false;
//	    	}
//			
//			if(carCenter.x >= (this.center.x + 1))
//	    	{
//				//Car is too far right
//				
//	    		return false;
//	    	}
//			
//			
//	    	if(carCenter.y >= (this.center.y + winBoxSize/2))
//	    	{
//	    		//Car is too far up
//	    		return false;
//	    	}
//	    	
//	    	if(carCenter.y <= (this.center.y - winBoxSize/2))
//	    	{
//	    		//Car is too far down
//	    		return false;
//	    	}
//	    	
//	
//			System.out.println(playerName + " Wins!");
//			this.gameFinished = true;
//			return true;
//		}
//		
//		return false;
//
//    }
	
	private void createOuterWalls(World world, float worldWidth, float worldHeight, Vector2 center, float gapFromOuterEdge)
	{
		
	    //outer walls
	    BoxProp bottomWall = new BoxProp(world, worldWidth, 1, new Vector2 (worldWidth/2, gapFromOuterEdge)); //bottom Wall
	    
	    BoxProp leftWall = new BoxProp(world, 1, worldHeight, new Vector2 (gapFromOuterEdge, worldHeight/2));//left Wall
	    
	    BoxProp topWall = new BoxProp(world,  worldWidth, 1, new Vector2 (worldWidth/2,worldHeight-gapFromOuterEdge));//top
	    
	    BoxProp rightWall = new BoxProp(world, 1, worldHeight, new Vector2 (worldWidth - gapFromOuterEdge, worldHeight/2));//left Wall
	    
	    walls.add(bottomWall);
	    walls.add(leftWall);
	    walls.add(topWall);
	    walls.add(rightWall);
	    
	}
	
	private void createInnerWalls(World world, float worldWidth, float worldHeight, Vector2 center, float gapFromOuterEdge, int numberOfInnerWalls)
	{
		
		float gapForGettingThrough = 20f;
		
		float wallLength =  worldWidth - gapForGettingThrough;
		
		BoxProp[] innerWalls = new BoxProp[numberOfInnerWalls];
		
		for (int i = 1; i <= numberOfInnerWalls; i++)
		{
			if (i%2 == 0)
			{
				//Should be starting from the right
				innerWalls[i-1] = new BoxProp(world, wallLength, 1 , new Vector2 (gapForGettingThrough + (wallLength/2), i*(worldHeight/(numberOfInnerWalls+1)))); //inner wall 2 - starts at right
			}
			else
			{
				innerWalls[i-1] = new BoxProp(world, wallLength, 1 , new Vector2 ((worldWidth - gapForGettingThrough)/2, i*(worldHeight/(numberOfInnerWalls+1)))); //inner wall 3 - starts at left
			}
			
			this.walls.add(innerWalls[i-1]);
		}
		
		
	}

}

