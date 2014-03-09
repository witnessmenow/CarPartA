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
	public static final String BALL = "BALL";
	public static final String CARS = "CARS";
	
	public static final String WHEELS = "WHEELS";
	
	public static final String SOCCER_PITCH = "SOCCER_PITCH";
	public static final String TEAM_SELECT_AREA = "TEAM_SELECT_AREA";
	
	public static Hashtable<String, Sprite> spriteTable = new Hashtable<String, Sprite>();
	
	public static void load()
	{
		loadTextures();
	}
	
	private static void loadTextures()
	{
		textureTable.put(LOGO,
				new Texture(Gdx.files.internal("libgdx-logo.png")));
		textureTable.put(BALL, new Texture(Gdx.files.internal("ball.png")));
		textureTable.put(CARS, new Texture(Gdx.files.internal("CarSpritesheet.png")));
		textureTable.put(SOCCER_PITCH, new Texture(Gdx.files.internal("pitch.png")));
		textureTable.put(TEAM_SELECT_AREA, new Texture(Gdx.files.internal("teamSelect.png")));
		textureTable.put(WHEELS, new Texture(Gdx.files.internal("Wheel.png")));
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