package com.ladinc.core.screen.gamemodes.soccer;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.core.assets.Art;
import com.ladinc.core.objects.BoxProp;
import com.ladinc.core.objects.Goal;
import com.ladinc.core.objects.StartingPosition;
import com.ladinc.core.screen.gamemodes.GenericLayout;
import com.ladinc.core.utilities.Enums.Direction;
import com.ladinc.core.utilities.Enums.Team;

public class SoccerLayout extends GenericLayout {
	private static final float GOAL_WIDTH = 15f;
	private static final float GOAL_HEIGHT = 25f;
	private static final float POST_THICKNESS = 1f;
	private static final float GAP_BETWEEN_GOAL_AND_EDGE = 0.5f;
	private static final float Y_AXIS_GAP  = 10f;
	
	private static final float CAR_START_GAP_FROM_EDGE = 35f;
	private static final float GAP_BETWEEN_CARS = 12f;
	
	private final float timerAndScoringSpace = 2f;
	
	public Goal homeGoal;
	public Goal awayGoal;
	
	public SoccerLayout(World world, float worldWidth, float worldHeight,
			Vector2 center, int numInnerWalls) {
		super(world, worldWidth, worldHeight, center, numInnerWalls);
	}
	
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
		this.homeGoal = new Goal(world, GAP_BETWEEN_GOAL_AND_EDGE + POST_THICKNESS/2 , getCenter().y, Direction.right, GOAL_WIDTH, POST_THICKNESS, GOAL_HEIGHT, Team.Home, ballSize);
		
		this.awayGoal = new Goal(world, (getWorldWidth() - POST_THICKNESS/2 - GAP_BETWEEN_GOAL_AND_EDGE) , getCenter().y, Direction.left, GOAL_WIDTH, POST_THICKNESS, GOAL_HEIGHT, Team.Away, ballSize);
		
	}
	
	private void createPitch(World world, float worldWidth, float worldHeight,
			Vector2 center, float gapFromOuterEdge, float timerAndScoringSpace)
	{
		
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
		this.addWall(goalLineWallLeftBottom);
		this.addWall(goalLineWallRightTop);
		this.addWall(goalLineWallRightBottom);
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
	
	public Sprite getPitchSprite()
	{
		if(!Art.spriteTable.containsKey(Art.SOCCER_PITCH))
		{
			Art.spriteTable.put(Art.SOCCER_PITCH, new Sprite(Art.textureTable.get(Art.SOCCER_PITCH)));
		}
		
		return Art.spriteTable.get(Art.SOCCER_PITCH);
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
	
	public float getTimerAndScoringSpace()
	{
		return timerAndScoringSpace;
	}
}