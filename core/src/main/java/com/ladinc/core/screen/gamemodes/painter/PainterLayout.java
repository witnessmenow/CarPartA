package com.ladinc.core.screen.gamemodes.painter;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.core.objects.BoxProp;
import com.ladinc.core.objects.FloorTileSensor;
import com.ladinc.core.objects.SensorBox;
import com.ladinc.core.screen.gamemodes.GenericLayout;
import com.ladinc.core.utilities.Enums.Team;

public class PainterLayout extends GenericLayout
{

	private static final float GAP_BETWEEN_SIDEWALL_AND_EDGE = 10.5f;
	private static final float GAP_BETWEEN_TOPBOTTOMWALL_AND_EDGE = 8.5f;
	private static final float TILE_SIZE = 10f;
	
	public ArrayList<FloorTileSensor> floorSensors;
	
	public PainterLayout(World world, float worldWidth, float worldHeight,
			Vector2 center, int numberOfInnerWalls) {
		super(world, worldWidth, worldHeight, center, numberOfInnerWalls);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createWorld(World world) {
		
		
		new BoxProp(world, getWorldWidth(), 1, new Vector2 (getWorldWidth()/2, GAP_BETWEEN_TOPBOTTOMWALL_AND_EDGE));//left
		new BoxProp(world, getWorldWidth(), 1, new Vector2 (getWorldWidth()/2, getWorldHeight() - GAP_BETWEEN_TOPBOTTOMWALL_AND_EDGE));
		
		new BoxProp(world, 1, getWorldHeight(), new Vector2 (GAP_BETWEEN_SIDEWALL_AND_EDGE, getWorldHeight()/2));//left
		new BoxProp(world, 1, getWorldHeight(), new Vector2 (getWorldWidth() - GAP_BETWEEN_SIDEWALL_AND_EDGE, getWorldHeight()/2));
		
		createTiles(world);
	}
	
	private void createTiles(World world)
	{
		float tempX;
		float tempY;
		
		if(floorSensors == null)
		{
			floorSensors = new ArrayList<FloorTileSensor>();
		}
		else
		{
			floorSensors.clear();
		}
		
		for(int i = 0; i < 17; i++)
		{
			tempX = i*TILE_SIZE + TILE_SIZE/2 + GAP_BETWEEN_SIDEWALL_AND_EDGE + 0.5f;
			for(int j = 0; j < 9; j++)
			{
				tempY = j*TILE_SIZE + TILE_SIZE/2 + GAP_BETWEEN_TOPBOTTOMWALL_AND_EDGE + 0.5f;
				floorSensors.add(new FloorTileSensor(world, TILE_SIZE, TILE_SIZE, new Vector2(tempX, tempY)));
			}
		}
		
	}

}
