package com.ladinc.core.screen.gamemodes.soccer;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.core.objects.BoxProp;
import com.ladinc.core.objects.Goal;
import com.ladinc.core.objects.StartingPosition;
import com.ladinc.core.screen.gamemodes.GenericLayout;
import com.ladinc.core.utilities.Enums.Direction;
import com.ladinc.core.utilities.Enums.Side;

public class SoccerLayout extends GenericLayout {
	private static final float GOAL_WIDTH = 15f;
	private static final float GOAL_HEIGHT = 25f;
	private static final float POST_THICKNESS = 1f;
	private static final float GAP_BETWEEN_GOAL_AND_EDGE = 0.5f;
	private static final float Y_AXIS_GAP  = 10f;
	
	private final float timerAndScoringSpace = 2f;
	
	public Goal homeGoal;
	public Goal awayGoal;
	
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
	
	
	public void createWorld(World world)
	{
		createPitch(world, getWorldWidth(), getWorldHeight(), getCenter(),
				getGapFromOuterEdge(), getTimerAndScoringSpace());
	}
	
	public void createGoals(World world, Float ballSize)
	{
		//Create Home Goal
		this.homeGoal = new Goal(world, GAP_BETWEEN_GOAL_AND_EDGE + POST_THICKNESS/2 , getCenter().y, Direction.right, GOAL_WIDTH, POST_THICKNESS, GOAL_HEIGHT, Side.Home, ballSize);
		
		this.awayGoal = new Goal(world, (getWorldWidth() - POST_THICKNESS/2 - GAP_BETWEEN_GOAL_AND_EDGE) , getCenter().y, Direction.left, GOAL_WIDTH, POST_THICKNESS, GOAL_HEIGHT, Side.Away, ballSize);
		
	}
	
	private void createPitch(World world, float worldWidth, float worldHeight,
			Vector2 center, float gapFromOuterEdge, float timerAndScoringSpace)
	{
//		// Touchlines
//		BoxProp touchLineWallTop = new BoxProp(world, worldWidth - GOAL_WIDTH,
//				1, new Vector2(worldWidth / 2, worldHeight - gapFromOuterEdge
//						- timerAndScoringSpace));
//		BoxProp touchLineWallBot = new BoxProp(world, worldWidth - GOAL_WIDTH,
//				1, new Vector2(worldWidth / 2, gapFromOuterEdge));
//		
//		// Goal on the left
//		BoxProp goalLineWallLeftTop = new BoxProp(world, 1, (worldHeight / 2)
//				- GOAL_HEIGHT, new Vector2(GOAL_WIDTH - gapFromOuterEdge,
//				worldHeight - ((worldHeight / 2 - GOAL_HEIGHT / 2) / 2)));
//		
//		BoxProp goalLineWallLeftGoalWidthTop = new BoxProp(world, GOAL_WIDTH,
//				1, new Vector2(gapFromOuterEdge,
//						(worldHeight + GOAL_HEIGHT) / 2));
//		
//		BoxProp goalLineWallLeftGoalHeight = new BoxProp(world, 1, GOAL_HEIGHT,
//				new Vector2(gapFromOuterEdge, (worldHeight) / 2));
//		
//		BoxProp goalLineWallLeftGoalWidthBottom = new BoxProp(world,
//				GOAL_WIDTH, 1, new Vector2(gapFromOuterEdge,
//						(worldHeight - GOAL_HEIGHT) / 2));
//		
//		BoxProp goalLineWallLeftBottom = new BoxProp(world, 1,
//				(worldHeight / 2) - GOAL_HEIGHT, new Vector2(GOAL_WIDTH
//						- gapFromOuterEdge,
//						(worldHeight / 2 - GOAL_HEIGHT / 2) / 2));
//		
//		// Goal on the right
//		BoxProp goalLineWallRightTop = new BoxProp(world, 1, (worldHeight / 2)
//				- GOAL_HEIGHT, new Vector2(worldWidth - GOAL_WIDTH
//				- gapFromOuterEdge, worldHeight
//				- ((worldHeight / 2 - GOAL_HEIGHT / 2) / 2)));
//		
//		BoxProp goalLineWallRightGoalWidthTop = new BoxProp(world, GOAL_WIDTH,
//				1, new Vector2(worldWidth - gapFromOuterEdge,
//						(worldHeight + GOAL_HEIGHT) / 2));
//		
//		BoxProp goalLineWallRightGoalHeight = new BoxProp(world, 1,
//				GOAL_HEIGHT, new Vector2(worldWidth - gapFromOuterEdge,
//						(worldHeight) / 2));
//		
//		BoxProp goalLineWallRightGoalWidthBottom = new BoxProp(world,
//				GOAL_WIDTH, 1, new Vector2(worldWidth - gapFromOuterEdge,
//						(worldHeight - GOAL_HEIGHT) / 2));
//		
//		BoxProp goalLineWallRightBottom = new BoxProp(world, 1,
//				(worldHeight / 2) - GOAL_HEIGHT, new Vector2(worldWidth
//						- GOAL_WIDTH - gapFromOuterEdge,
//						(worldHeight / 2 - GOAL_HEIGHT / 2) / 2));
		
		float touchlineLength = (worldHeight/2) - ((GOAL_HEIGHT + (POST_THICKNESS))/2);
		
		BoxProp touchLineWallBot = new BoxProp(world, worldWidth, 1, new Vector2 (worldWidth/2,Y_AXIS_GAP)); //bottom

	    BoxProp goalLineWallLeftBottom = new BoxProp(world, 1, touchlineLength, new Vector2 (GOAL_WIDTH, touchlineLength/2));//left
	    BoxProp goalLineWallLeftTop = new BoxProp(world, 1, touchlineLength, new Vector2 (GOAL_WIDTH, worldHeight-(touchlineLength/2)));//left

	    BoxProp touchLineWallTop = new BoxProp(world,  worldWidth, 1, new Vector2 (worldWidth/2,worldHeight-Y_AXIS_GAP));//top

	    BoxProp goalLineWallRightBottom = new BoxProp(world, 1, touchlineLength, new Vector2 (worldWidth - (GOAL_WIDTH), touchlineLength/2));//right
	    BoxProp goalLineWallRightTop = new BoxProp(world, 1, touchlineLength, new Vector2 (worldWidth - (GOAL_WIDTH), worldHeight-(touchlineLength/2)));
		
		
		this.addWall(touchLineWallBot);
		this.addWall(touchLineWallTop);
		
		this.addWall(goalLineWallLeftTop);
//		getWalls().add(goalLineWallLeftGoalWidthTop);
//		getWalls().add(goalLineWallLeftGoalHeight);
//		getWalls().add(goalLineWallLeftGoalWidthBottom);
		this.addWall(goalLineWallLeftBottom);
		this.addWall(goalLineWallRightTop);
//		getWalls().add(goalLineWallRightGoalWidthTop);
//		getWalls().add(goalLineWallRightGoalHeight);
//		getWalls().add(goalLineWallRightGoalWidthBottom);
		this.addWall(goalLineWallRightBottom);
	}
	
	public float getTimerAndScoringSpace()
	{
		return timerAndScoringSpace;
	}
}