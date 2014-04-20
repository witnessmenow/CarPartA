package com.ladinc.core.screen.gamemodes.capture;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.core.assets.Art;
import com.ladinc.core.objects.BoxProp;
import com.ladinc.core.objects.FloorTileSensor;
import com.ladinc.core.objects.StartingPosition;
import com.ladinc.core.screen.gamemodes.GenericLayout;
import com.ladinc.core.utilities.Enums.Team;

public class CaptureTheFlagLayout extends GenericLayout {
	public ArrayList<FloorTileSensor> floorSensors;
	
	private static final float GAP_BETWEEN_SIDEWALL_AND_EDGE = 10.5f;
	private static final float GAP_BETWEEN_TOPBOTTOMWALL_AND_EDGE = 8.5f;
	private static final float TILE_SIZE = 10f;
	private static final float PLAYER_GAP_X = 8f;
	
	private final Sprite homeTile;
	private final Sprite awayTile;
	public float timeLeft = 0f;
	
	public CaptureTheFlagCollisionHelper colHelper;
	
	public CaptureTheFlagLayout(World world, float worldWidth,
			float worldHeight, Vector2 center, int numberOfInnerWalls) {
		super(world, worldWidth, worldHeight, center, numberOfInnerWalls);
		
		homeTile = FloorTileSensor.getTeamSprite(Team.Home);
		awayTile = FloorTileSensor.getTeamSprite(Team.Away);
	}
	
	@Override
	public StartingPosition getPlayerStartPoint(Team team, int playerTeamNumber)
	{
		float carPosX;
		float carPosY = getWorldHeight() / 2;
		
		int direction;
		
		if (team == Team.Away)
		{
			direction = -1;
		}
		else
		{
			direction = 1;
		}
		
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
	
	public void drawSpritesForTiles(SpriteBatch sp, int pixPerMeter)
	{
		for (FloorTileSensor fts : floorSensors)
		{
			if (fts.flagPresent)
			{
				assaignFlagTile(fts);
			}
			else
			{
				unassaignFlagTile(fts);
			}
			
			if (fts.getTeam() == Team.Home)
			{
				fts.updateSprite(homeTile, sp, pixPerMeter);
			}
			else if (fts.getTeam() == Team.Away)
			{
				fts.updateSprite(awayTile, sp, pixPerMeter);
			}
		}
	}
	
	private void createTiles(World world)
	{
		if (floorSensors == null)
		{
			floorSensors = new ArrayList<FloorTileSensor>();
		}
		else
		{
			floorSensors.clear();
		}
		
		float homeFloorTileXCoOrdinates = 1 * TILE_SIZE + TILE_SIZE / 2
				+ GAP_BETWEEN_SIDEWALL_AND_EDGE + 0.5f;
		float awayFloorTileXCoOrdinates = 15 * TILE_SIZE + TILE_SIZE / 2
				+ GAP_BETWEEN_SIDEWALL_AND_EDGE + 0.5f;
		float homeAndAwayFloorTileYCoOrdinates = 4 * TILE_SIZE + TILE_SIZE / 2
				+ GAP_BETWEEN_TOPBOTTOMWALL_AND_EDGE + 0.5f;
		FloorTileSensor homeFlagTile = new FloorTileSensor(world, TILE_SIZE,
				TILE_SIZE, new Vector2(homeFloorTileXCoOrdinates,
						homeAndAwayFloorTileYCoOrdinates));
		assaignFlagTile(homeFlagTile);
		
		FloorTileSensor awayFlagTile = new FloorTileSensor(world, TILE_SIZE,
				TILE_SIZE, new Vector2(awayFloorTileXCoOrdinates,
						homeAndAwayFloorTileYCoOrdinates));
		assaignFlagTile(awayFlagTile);
		
		homeFlagTile.setTeam(Team.Home);
		awayFlagTile.setTeam(Team.Away);
		floorSensors.add(homeFlagTile);
		floorSensors.add(awayFlagTile);
	}
	
	private void assaignFlagTile(FloorTileSensor fts)
	{
		fts.flagPresent = true;
		if (fts.flagSprite == null)
		{
			fts.flagSprite = Art.getSprite(Art.CROWN);
			fts.flagSprite.setScale(1.5f);
		}
	}
	
	private void unassaignFlagTile(FloorTileSensor fts)
	{
		fts.flagPresent = false;
		fts.flagSprite = null;
	}
}