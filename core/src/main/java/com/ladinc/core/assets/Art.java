package com.ladinc.core.assets;

import java.util.Hashtable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;

public class Art {
	public static Hashtable<String, Texture> textureTable = new Hashtable<String, Texture>();
	public static final String LOGO = "LOGO";
	public static final String BALLS = "BALLS";
	public static final String CARS = "CARS";
	
	public static final String WHEELS = "WHEELS";
	public static final String IDENTIFIER = "IDENTIFIER";
	
	public static final String SOCCER_PITCH = "SOCCER_PITCH";
	public static final String TEAM_SELECT_AREA = "TEAM_SELECT_AREA";
	public static final String TOUCH_OVERLAY = "TOUCH_OVERLAY";
	
	//Car Pool
	
	public static final String POOL_TABLE = "POOL_TABLE";
	
	public static Hashtable<String, Sprite> spriteTable = new Hashtable<String, Sprite>();
	
	public static void load()
	{
		loadTextures();
	}
	
	private static void loadTextures()
	{
		textureTable.put(LOGO,
				new Texture(Gdx.files.internal("libgdx-logo.png")));
		textureTable.put(BALLS, new Texture(Gdx.files.internal("balls.png")));
		textureTable.put(CARS, new Texture(Gdx.files.internal("CarSpritesheet.png")));
		textureTable.put(SOCCER_PITCH, new Texture(Gdx.files.internal("SocCar/GreenPitch.jpg")));
		textureTable.put(TEAM_SELECT_AREA, new Texture(Gdx.files.internal("teamSelect.png")));
		textureTable.put(WHEELS, new Texture(Gdx.files.internal("Wheel.png")));
		textureTable.put(IDENTIFIER, new Texture(Gdx.files.internal("Identfiers.png")));
		textureTable.put(TOUCH_OVERLAY, new Texture(Gdx.files.internal("touchOverlay.png")));
		textureTable.put(POOL_TABLE, new Texture(Gdx.files.internal("CarPool/carsnu.png")));
	}
	
	public static Sprite getTouchOverlay()
	{
		if(!spriteTable.containsKey(TOUCH_OVERLAY))
		{
			spriteTable.put(TOUCH_OVERLAY, new Sprite(Art.textureTable.get(TOUCH_OVERLAY)));
		}
		
		return spriteTable.get(TOUCH_OVERLAY);
	}
	
	public static void updateSprite(Sprite sprite, SpriteBatch spriteBatch, int PIXELS_PER_METER, Body body)
	{
		if(sprite != null && spriteBatch != null && body != null)
		{
			setSpritePosition(sprite, PIXELS_PER_METER, body);
	
			sprite.draw(spriteBatch);
		}
	}
	
	public static void setSpritePosition(Sprite sprite, int PIXELS_PER_METER, Body body)
	{
		
		sprite.setPosition(PIXELS_PER_METER * body.getPosition().x - sprite.getWidth()/2,
				PIXELS_PER_METER * body.getPosition().y  - sprite.getHeight()/2);
		sprite.setRotation((MathUtils.radiansToDegrees * body.getAngle()));
	}
	
}