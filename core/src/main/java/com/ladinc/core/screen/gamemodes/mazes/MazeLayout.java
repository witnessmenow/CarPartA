package com.ladinc.core.screen.gamemodes.mazes;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.core.objects.BoxProp;
import com.ladinc.core.objects.StartingPosition;
import com.ladinc.core.screen.gamemodes.GenericLayout;
import com.ladinc.core.utilities.Enums.Team;
import com.ladinc.core.vehicles.Vehicle;

public class MazeLayout extends GenericLayout {
	private float winBoxSize;
	
	public MazeLayout(World world, float worldWidth, float worldHeight,
			Vector2 center, int numberOfInnerWalls) {
		super(world, worldWidth, worldHeight, center, numberOfInnerWalls);
		
		this.winBoxSize = 16.7f;
	}
	
	public StartingPosition getTopStartPoint()
	{
		return new StartingPosition(new Vector2(getWorldWidth()
				- (getGapFromOuterEdge() + getPlayerGapX()), getWorldHeight()
				- (getGapFromOuterEdge() + getPlayerGapY())),
				(float) (Math.PI + Math.PI / 2));
	}
	
	public StartingPosition getBottomStartPoint()
	{
		return new StartingPosition(new Vector2(getGapFromOuterEdge()
				+ getPlayerGapX(), getGapFromOuterEdge() + getPlayerGapY()),
				(float) Math.PI / 2);
	}
	
	@Override
	public StartingPosition getPlayerStartPoint(Team team, int playerTeamNumber)
	{
		if(team == Team.Away)
		{
			if(playerTeamNumber % 2 == 0)
				return getTopStartPoint();
			else
				return getBottomStartPoint();
		}
		else
		{
			if(playerTeamNumber % 2 == 0)
				return getBottomStartPoint();
			else
				return getTopStartPoint();
		}
	}
	
	@Override
	public void createWorld(World world)
	{
		createOuterWalls(world, getWorldWidth(), getWorldHeight(), getCenter(),
				getGapFromOuterEdge());
		
		// make it even!
		createInnerWalls(world, getWorldWidth(), getWorldHeight(), getCenter(),
				getGapFromOuterEdge(), getNumberOfInnerWalls());
	}
	
	private void createOuterWalls(World world, float worldWidth,
			float worldHeight, Vector2 center, float gapFromOuterEdge)
	{
		// outer walls
		BoxProp topWall = new BoxProp(world, worldWidth, 1, new Vector2(
				worldWidth / 2, worldHeight - gapFromOuterEdge));// top
		
		BoxProp bottomWall = new BoxProp(world, worldWidth, 1, new Vector2(
				worldWidth / 2, gapFromOuterEdge)); // bottom Wall
		
		BoxProp leftWall = new BoxProp(world, 1, worldHeight, new Vector2(
				gapFromOuterEdge, worldHeight / 2));// left Wall
		
		BoxProp rightWall = new BoxProp(world, 1, worldHeight, new Vector2(
				worldWidth - gapFromOuterEdge, worldHeight / 2));// left Wall

		getWalls().add(topWall);
		getWalls().add(bottomWall);
		getWalls().add(leftWall);
		getWalls().add(rightWall);
	}
	
	private void createInnerWalls(World world, float worldWidth,
			float worldHeight, Vector2 center, float gapFromOuterEdge,
			int numberOfInnerWalls)
	{
		float gapForGettingThrough = 20f;
		float wallLength = worldWidth - gapForGettingThrough;
		BoxProp[] innerWalls = new BoxProp[numberOfInnerWalls];
		
		new BoxProp(world, wallLength, 1,
						new Vector2((worldWidth - 30.0f) / 2, 
								24.3f));

		 new BoxProp(world, wallLength, 1,
						new Vector2(gapForGettingThrough + (wallLength / 2), 45.1f));
				// innerwall 3 - starts at left
	
		new BoxProp(world, wallLength, 1,
				new Vector2((worldWidth - gapForGettingThrough) / 2, 
						worldHeight - 45.1f));

		new BoxProp(world, wallLength, 1,
					new Vector2(30.0f + (wallLength / 2), worldHeight - 24.3f));
	

		
	}
	
	public Vector2 calculateAiTarget(Vector2 position)
	{
		if(position.y < 24.3f)
		{
			if(position.x < getWorldWidth() - 65.0f)
			{
				return new Vector2(getWorldWidth() - 20.0f, 5.0f);
			}
			else
			{
				return new Vector2(getWorldWidth() - 20.0f, 30.0f);
			}
		}
		else if(position.y < 45.1f)
		{
			if(position.x > 60.0f)
			{
				return new Vector2 (20.0f, 25.0f);
			}
			else
			{
				return new Vector2(20.0f, 60.0f);
			}
		}
		else if(position.y >  getWorldHeight() - 24.3f)
		{
			if(position.x > 68.0f)
			{
				return new Vector2(20.0f,getWorldHeight() - 5.0f);
			}
			else
			{
				return new Vector2(20.0f, getWorldHeight() -  30.0f);
			}
		}
		else if(position.y > getWorldHeight() - 45.1f)
		{
			if(position.x < getWorldWidth() - 60.0f)
			{
				return new Vector2 (getWorldWidth() - 20.0f,  getWorldHeight() -  25.0f);
			}
			else
			{
				return new Vector2(getWorldWidth() - 20.0f,  getWorldHeight() -  60.0f);
			}
		}
		
		return getCenter();
	}
	
	public Vehicle checkForWinForAllCars(List<Vehicle> vehicles)
	{
		for(Vehicle v : vehicles)
		{
			if(checkForWin(v))
			{
				return v;
			}
		}
		
		return null;
	}
	
	
	private Vector2 tempVector;
	public boolean checkForWin(Vehicle vehicle)
    {
		
		tempVector = vehicle.body.getWorldCenter();
		
		//TODO: this is completly based on the center of the car. More advanced things need to be done to notice when the car is actually in the win box, not just its center
		
		if(tempVector.x <= (getCenter().x - 1))
    	{
			//Car is too far left
    		return false;
    	}
		
		if(tempVector.x >= (getCenter().x + 1))
    	{
			//Car is too far right
			
    		return false;
    	}
		
		
    	if(tempVector.y >= (getCenter().y + winBoxSize/2))
    	{
    		//Car is too far up
    		return false;
    	}
    	
    	if(tempVector.y <= (getCenter().y - winBoxSize/2))
    	{
    		//Car is too far down
    		return false;
    	}
    	

		return true;

    }
	
//	private void createInnerWalls(World world, float worldWidth,
//			float worldHeight, Vector2 center, float gapFromOuterEdge,
//			int numberOfInnerWalls)
//	{
//		float gapForGettingThrough = 20f;
//		float wallLength = worldWidth - gapForGettingThrough;
//		BoxProp[] innerWalls = new BoxProp[numberOfInnerWalls];
//		
//		for (int i = 1; i <= numberOfInnerWalls; i++)
//		{
//			if (i % 2 == 0)
//			{
//				// Should be starting from the right
//				innerWalls[i - 1] = new BoxProp(world, wallLength, 1,
//						new Vector2(gapForGettingThrough + (wallLength / 2), i
//								* (worldHeight / (numberOfInnerWalls + 1))));
//				// inner wall 2 - starts at right
//			}
//			else
//			{
//				innerWalls[i - 1] = new BoxProp(world, wallLength, 1,
//						new Vector2((worldWidth - gapForGettingThrough) / 2, i
//								* (worldHeight / (numberOfInnerWalls + 1))));
//				// innerwall 3 - starts at left
//			}
//			
//			getWalls().add(innerWalls[i - 1]);
//		}
//	}
	
	public float getWinBoxSize()
	{
		return winBoxSize;
	}
}