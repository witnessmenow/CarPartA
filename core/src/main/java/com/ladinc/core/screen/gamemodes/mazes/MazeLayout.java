package com.ladinc.core.screen.gamemodes.mazes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.core.objects.BoxProp;
import com.ladinc.core.objects.StartingPosition;
import com.ladinc.core.screen.gamemodes.GenericLayout;

public class MazeLayout extends GenericLayout {
	private float winBoxSize;
	
	public MazeLayout(World world, float worldWidth, float worldHeight,
			Vector2 center, int numberOfInnerWalls) {
		super(world, worldWidth, worldHeight, center, numberOfInnerWalls);
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
			
			getWalls().add(innerWalls[i - 1]);
		}
	}
	
	public float getWinBoxSize()
	{
		return winBoxSize;
	}
}