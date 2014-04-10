package com.ladinc.core.screen.gamemodes.mower;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.core.objects.BoxProp;
import com.ladinc.core.objects.FloorTileSensor;
import com.ladinc.core.objects.StartingPosition;
import com.ladinc.core.screen.gamemodes.GenericLayout;
import com.ladinc.core.utilities.Enums.Team;

public class MowerLayout extends GenericLayout {
	private static final float GAP_BETWEEN_CARS = 12f;
	private static final float CAR_START_GAP_FROM_EDGE = 35f;
	
	private static final float GAP_BETWEEN_SIDEWALL_AND_EDGE = 10.5f;
	private static final float GAP_BETWEEN_TOPBOTTOMWALL_AND_EDGE = 8.5f;
	private static final float TILE_SIZE = 10f;
	
	public static final int TARGET_SCORE = 8 * 9;
	
	public ArrayList<FloorTileSensor> homeFloorSensors;
	public ArrayList<FloorTileSensor> awayFloorSensors;
	public ArrayList<FloorTileSensor> availableHomeFloorSensors;
	public ArrayList<FloorTileSensor> availableAwayFloorSensors;
	
	private final Sprite unCutSprite;
	private final Sprite cutSprite;
	
	public int homeScore = 0;
	public int awayScore = 0;
	
	public MowerLayout(World world, float worldWidth, float worldHeight,
			Vector2 center, int numberOfInnerWalls) {
		super(world, worldWidth, worldHeight, center, numberOfInnerWalls);
		
		unCutSprite = FloorTileSensor.getTeamSprite(Team.Home);
		cutSprite = FloorTileSensor.getTeamSprite(Team.Away);
	}
	
	@Override
	public StartingPosition getPlayerStartPoint(Team team, int playerTeamNumber)
	{
		if (team == Team.Away)
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
		// Face Right
		float direction = (float) Math.PI / 2;
		float x = CAR_START_GAP_FROM_EDGE;
		float y = this.getCenter().y;
		
		// 0 is ok with defaults
		if (playerTeamNumber != 0)
		{
			// because we are ingoring 0, we need to offset the rest of the
			// values
			playerTeamNumber -= 1;
			
			int dir = 0;
			if (playerTeamNumber % 2 == 0)
			{
				dir = 1;
			}
			else
			{
				dir = -1;
			}
			
			y = y + ((dir) * (((playerTeamNumber / 2) + 1) * GAP_BETWEEN_CARS));
		}
		
		return new StartingPosition(new Vector2(x, y), direction);
	}
	
	private StartingPosition getAwayStartingPosisiton(int playerTeamNumber)
	{
		// Face Left
		float direction = (float) (Math.PI + Math.PI / 2);
		float x = this.getWorldWidth() - CAR_START_GAP_FROM_EDGE;
		float y = this.getCenter().y;
		
		// 0 is ok with defaults
		if (playerTeamNumber != 0)
		{
			// because we are ingoring 0, we need to offset the rest of the
			// values
			playerTeamNumber -= 1;
			
			int dir = 0;
			if (playerTeamNumber % 2 == 0)
			{
				dir = -1;
			}
			else
			{
				dir = +1;
			}
			
			y = y + ((dir) * (((playerTeamNumber / 2) + 1) * GAP_BETWEEN_CARS));
		}
		
		return new StartingPosition(new Vector2(x, y), direction);
	}
	
	@Override
	public void createWorld(World world)
	{
		new BoxProp(world, getWorldWidth(), 1, new Vector2(getWorldWidth() / 2,
				GAP_BETWEEN_TOPBOTTOMWALL_AND_EDGE));// left
		new BoxProp(world, getWorldWidth(), 1, new Vector2(getWorldWidth() / 2,
				getWorldHeight() - GAP_BETWEEN_TOPBOTTOMWALL_AND_EDGE));
		
		new BoxProp(world, 1, getWorldHeight(), new Vector2(
				GAP_BETWEEN_SIDEWALL_AND_EDGE, getWorldHeight() / 2));// left
		new BoxProp(world, 1, getWorldHeight(), new Vector2(getWorldWidth()
				- GAP_BETWEEN_SIDEWALL_AND_EDGE, getWorldHeight() / 2));
		
		new BoxProp(world, TILE_SIZE, getWorldHeight(), new Vector2(
				getWorldWidth() / 2, getWorldHeight() / 2));
		
		createHomeTiles(world);
		createAwayTiles(world);
		
	}
	
	private void createHomeTiles(World world)
	{
		float tempX;
		float tempY;
		
		if (homeFloorSensors == null)
		{
			homeFloorSensors = new ArrayList<FloorTileSensor>();
		}
		else
		{
			homeFloorSensors.clear();
		}
		
		if (availableHomeFloorSensors == null)
		{
			availableHomeFloorSensors = new ArrayList<FloorTileSensor>();
		}
		else
		{
			availableHomeFloorSensors.clear();
		}
		
		for (int i = 0; i < 8; i++)
		{
			tempX = i * TILE_SIZE + TILE_SIZE / 2
					+ GAP_BETWEEN_SIDEWALL_AND_EDGE + 0.5f;
			for (int j = 0; j < 9; j++)
			{
				tempY = j * TILE_SIZE + TILE_SIZE / 2
						+ GAP_BETWEEN_TOPBOTTOMWALL_AND_EDGE + 0.5f;
				FloorTileSensor tempFTS = new FloorTileSensor(world, TILE_SIZE,
						TILE_SIZE, new Vector2(tempX, tempY));
				homeFloorSensors.add(tempFTS);
				availableHomeFloorSensors.add(tempFTS);
			}
		}
		
	}
	
	private void createAwayTiles(World world)
	{
		float tempX;
		float tempY;
		
		if (awayFloorSensors == null)
		{
			awayFloorSensors = new ArrayList<FloorTileSensor>();
		}
		else
		{
			awayFloorSensors.clear();
		}
		
		if (availableAwayFloorSensors == null)
		{
			availableAwayFloorSensors = new ArrayList<FloorTileSensor>();
		}
		else
		{
			availableAwayFloorSensors.clear();
		}
		
		for (int i = 9; i < 17; i++)
		{
			tempX = i * TILE_SIZE + TILE_SIZE / 2
					+ GAP_BETWEEN_SIDEWALL_AND_EDGE + 0.5f;
			for (int j = 0; j < 9; j++)
			{
				tempY = j * TILE_SIZE + TILE_SIZE / 2
						+ GAP_BETWEEN_TOPBOTTOMWALL_AND_EDGE + 0.5f;
				FloorTileSensor tempFTS = new FloorTileSensor(world, TILE_SIZE,
						TILE_SIZE, new Vector2(tempX, tempY));
				awayFloorSensors.add(tempFTS);
				availableAwayFloorSensors.add(tempFTS);
			}
		}
		
	}
	
	public void drawSpritesForTiles(SpriteBatch sp, int pixPerMeter)
	{
		homeScore = 0;
		awayScore = 0;
		
		for (FloorTileSensor fts : homeFloorSensors)
		{
			if (fts.assigned)
			{
				homeScore++;
				fts.updateSprite(cutSprite, sp, pixPerMeter);
				if (availableHomeFloorSensors.contains(fts))
				{
					availableHomeFloorSensors.remove(fts);
				}
			}
			else
			{
				fts.updateSprite(unCutSprite, sp, pixPerMeter);
			}
		}
		
		for (FloorTileSensor fts : awayFloorSensors)
		{
			if (fts.assigned)
			{
				awayScore++;
				fts.updateSprite(cutSprite, sp, pixPerMeter);
				if (availableAwayFloorSensors.contains(fts))
				{
					availableAwayFloorSensors.remove(fts);
				}
			}
			else
			{
				fts.updateSprite(unCutSprite, sp, pixPerMeter);
			}
		}
	}
	
}
