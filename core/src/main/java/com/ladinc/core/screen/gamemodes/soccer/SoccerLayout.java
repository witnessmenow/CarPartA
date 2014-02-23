package com.ladinc.core.screen.gamemodes.soccer;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.core.objects.BoxProp;
import com.ladinc.core.objects.StartingPosition;
import com.ladinc.core.screen.gamemodes.GenericLayout;

public class SoccerLayout extends GenericLayout {
	private static final float GOAL_WIDTH = 10f;
	private static final float GOAL_HEIGHT = 10f;
	private final float timerAndScoringSpace = 2f;
	
	public SoccerLayout(World world, float worldWidth, float worldHeight,
			Vector2 center, int numInnerWalls) {
		super(world, worldWidth, worldHeight, center, numInnerWalls);
	}
	
	@Override
	public StartingPosition getTopStartPoint()
	{
		return new StartingPosition(
				new Vector2(
						getWorldWidth()
								- (getGapFromOuterEdge() + getPlayerGapX() + GOAL_WIDTH),
						getWorldHeight()
								- (getGapFromOuterEdge() + getPlayerGapY() + GOAL_HEIGHT)),
				(float) (Math.PI + Math.PI / 2));
	}
	
	@Override
	public StartingPosition getBottomStartPoint()
	{
		return new StartingPosition(new Vector2(getGapFromOuterEdge()
				+ getPlayerGapX() + GOAL_WIDTH, getGapFromOuterEdge()
				+ getPlayerGapY() + GOAL_HEIGHT), (float) Math.PI / 2);
	}
	
	@Override
	public void createWorld(World world)
	{
		createPitch(world, getWorldWidth(), getWorldHeight(), getCenter(),
				getGapFromOuterEdge(), getTimerAndScoringSpace());
		createGoals();
	}
	
	private void createGoals()
	{
		// TODO Create Goal section
		
	}
	
	private void createPitch(World world, float worldWidth, float worldHeight,
			Vector2 center, float gapFromOuterEdge, float timerAndScoringSpace)
	{
		// Touchlines
		BoxProp touchLineWallTop = new BoxProp(world, worldWidth - GOAL_WIDTH,
				1, new Vector2(worldWidth / 2, worldHeight - gapFromOuterEdge
						- timerAndScoringSpace));
		BoxProp touchLineWallBot = new BoxProp(world, worldWidth - GOAL_WIDTH,
				1, new Vector2(worldWidth / 2, gapFromOuterEdge));
		
		// Goal on the left
		BoxProp goalLineWallLeftTop = new BoxProp(world, 1, (worldHeight / 2)
				- GOAL_HEIGHT, new Vector2(GOAL_WIDTH - gapFromOuterEdge,
				worldHeight - ((worldHeight / 2 - GOAL_HEIGHT / 2) / 2)));
		
		BoxProp goalLineWallLeftGoalWidthTop = new BoxProp(world, GOAL_WIDTH,
				1, new Vector2(gapFromOuterEdge,
						(worldHeight + GOAL_HEIGHT) / 2));
		
		BoxProp goalLineWallLeftGoalHeight = new BoxProp(world, 1, GOAL_HEIGHT,
				new Vector2(gapFromOuterEdge, (worldHeight) / 2));
		
		BoxProp goalLineWallLeftGoalWidthBottom = new BoxProp(world,
				GOAL_WIDTH, 1, new Vector2(gapFromOuterEdge,
						(worldHeight - GOAL_HEIGHT) / 2));
		
		BoxProp goalLineWallLeftBottom = new BoxProp(world, 1,
				(worldHeight / 2) - GOAL_HEIGHT, new Vector2(GOAL_WIDTH
						- gapFromOuterEdge,
						(worldHeight / 2 - GOAL_HEIGHT / 2) / 2));
		
		// Goal on the right
		BoxProp goalLineWallRightTop = new BoxProp(world, 1, (worldHeight / 2)
				- GOAL_HEIGHT, new Vector2(worldWidth - GOAL_WIDTH
				- gapFromOuterEdge, worldHeight
				- ((worldHeight / 2 - GOAL_HEIGHT / 2) / 2)));
		
		BoxProp goalLineWallRightGoalWidthTop = new BoxProp(world, GOAL_WIDTH,
				1, new Vector2(worldWidth - gapFromOuterEdge,
						(worldHeight + GOAL_HEIGHT) / 2));
		
		BoxProp goalLineWallRightGoalHeight = new BoxProp(world, 1,
				GOAL_HEIGHT, new Vector2(worldWidth - gapFromOuterEdge,
						(worldHeight) / 2));
		
		BoxProp goalLineWallRightGoalWidthBottom = new BoxProp(world,
				GOAL_WIDTH, 1, new Vector2(worldWidth - gapFromOuterEdge,
						(worldHeight - GOAL_HEIGHT) / 2));
		
		BoxProp goalLineWallRightBottom = new BoxProp(world, 1,
				(worldHeight / 2) - GOAL_HEIGHT, new Vector2(worldWidth
						- GOAL_WIDTH - gapFromOuterEdge,
						(worldHeight / 2 - GOAL_HEIGHT / 2) / 2));
		
		getWalls().add(touchLineWallBot);
		getWalls().add(touchLineWallTop);
		
		getWalls().add(goalLineWallLeftTop);
		getWalls().add(goalLineWallLeftGoalWidthTop);
		getWalls().add(goalLineWallLeftGoalHeight);
		getWalls().add(goalLineWallLeftGoalWidthBottom);
		getWalls().add(goalLineWallLeftBottom);
		getWalls().add(goalLineWallRightTop);
		getWalls().add(goalLineWallRightGoalWidthTop);
		getWalls().add(goalLineWallRightGoalHeight);
		getWalls().add(goalLineWallRightGoalWidthBottom);
		getWalls().add(goalLineWallRightBottom);
	}
	
	public float getTimerAndScoringSpace()
	{
		return timerAndScoringSpace;
	}
}