package com.ladinc.core.screen.gamemodes.painter;

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

public class PainterLayout extends GenericLayout {
	
	private static final float GAP_BETWEEN_SIDEWALL_AND_EDGE = 10.5f;
	private static final float GAP_BETWEEN_TOPBOTTOMWALL_AND_EDGE = 8.5f;
	private static final float TILE_SIZE = 10f;
	
	private static final float PLAYER_GAP_X = 8f;
	
	private final Sprite homeTile;
	private final Sprite awayTile;
	
	public int homeScore = 0;
	public int awayScore = 0;
	
	public ArrayList<FloorTileSensor> floorSensors;
	
	public PainterLayout(World world, float worldWidth, float worldHeight,
			Vector2 center, int numberOfInnerWalls) {
		super(world, worldWidth, worldHeight, center, numberOfInnerWalls);
		
		homeTile = FloorTileSensor.getSprite(Team.Home);
		awayTile = FloorTileSensor.getSprite(Team.Away);
	}
	
	@Override
	public StartingPosition getPlayerStartPoint(Team team, int playerTeamNumber)
	{
		float carPosX;
		float carPosY = getWorldHeight() / 2;
		
		int direction;
		
		if (team == Team.Away)
			direction = -1;
		else
			direction = 1;
		
		if (playerTeamNumber % 2 == 0)
		{
			carPosX = getWorldWidth() / 2 - (direction)
					* (((playerTeamNumber) + 1) * PLAYER_GAP_X);
		}
		else
		{
			carPosX = getWorldWidth() / 2 + (direction)
					* (((playerTeamNumber) + 1) * PLAYER_GAP_X);
		}
		
		return new StartingPosition(new Vector2(carPosX, carPosY), 0);
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
		
		createTiles(world);
	}
	
	private void createTiles(World world)
	{
		float tempX;
		float tempY;
		
		if (floorSensors == null)
		{
			floorSensors = new ArrayList<FloorTileSensor>();
		}
		else
		{
			floorSensors.clear();
		}
		
		for (int i = 0; i < 17; i++)
		{
			tempX = i * TILE_SIZE + TILE_SIZE / 2
					+ GAP_BETWEEN_SIDEWALL_AND_EDGE + 0.5f;
			for (int j = 0; j < 9; j++)
			{
				tempY = j * TILE_SIZE + TILE_SIZE / 2
						+ GAP_BETWEEN_TOPBOTTOMWALL_AND_EDGE + 0.5f;
				floorSensors.add(new FloorTileSensor(world, TILE_SIZE,
						TILE_SIZE, new Vector2(tempX, tempY)));
			}
		}
		
	}
	
	public void drawSpritesForTiles(SpriteBatch sp, int pixPerMeter)
	{
		for (FloorTileSensor fts : floorSensors)
		{
			if (fts.assigned)
			{
				if (fts.getTeam() == Team.Home)
				{
					fts.updateSprite(homeTile, sp, pixPerMeter);
				}
				else
				{
					fts.updateSprite(awayTile, sp, pixPerMeter);
				}
			}
		}
	}
	
	public void calculateScores()
	{
		for (FloorTileSensor fts : floorSensors)
		{
			if (fts.assigned)
			{
				if (fts.getTeam() == Team.Home)
				{
					homeScore++;
				}
				else
				{
					awayScore++;
				}
			}
		}
	}
	
}
