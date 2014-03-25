package com.ladinc.core.assets;

import java.util.HashMap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.ladinc.core.utilities.Enums.Team;

public class CarsHelper 
{
	private static final int CAR_SPRITE_WIDTH = 50;
	private static final int CAR_SPRITE_LENGTH = 100;
	
	public static final String DEFAULT_CAR_ID = "DEFAULT";
	public static final String IRELAND_CAR_ID = "IRELAND";
	public static final String SPAIN_CAR_ID = "SPAIN";
	
	public static HashMap<String, Sprite> carTable = new HashMap<String, Sprite>();
	
	public static Sprite getDefaultCar()
	{
		if(!carTable.containsKey(DEFAULT_CAR_ID))
		{
			carTable.put(DEFAULT_CAR_ID, getCarAtLocation(4,1));
		}
		
		return carTable.get(DEFAULT_CAR_ID);
	}
	
	public static Sprite getTeamCar(Team side)
	{
		if (side == Team.Home)
		{
			if(!carTable.containsKey(IRELAND_CAR_ID))
			{
				carTable.put(IRELAND_CAR_ID, getCarAtLocation(0,3));
			}
			
			return carTable.get(IRELAND_CAR_ID);
		}
		else
		{
			if(!carTable.containsKey(SPAIN_CAR_ID))
			{
				carTable.put(SPAIN_CAR_ID, getCarAtLocation(3,6));
			}
			
			return carTable.get(SPAIN_CAR_ID);
		}
		
	}
	
	public static Sprite getCarAtLocation(int col, int row)
	{
		Texture carTexture = Art.textureTable.get(Art.CARS);
		
		return new Sprite(carTexture,col*CAR_SPRITE_WIDTH, row*CAR_SPRITE_LENGTH , CAR_SPRITE_WIDTH, CAR_SPRITE_LENGTH);
	}

}
