package com.ladinc.core.objects.balls;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.core.assets.Art;

public class PoolBall extends Ball
{
	ColourBall colour;

	public static final String RED_BALL = "RED_BALL";
	public static final String YELLOW_BALL = "YELLOW_BALL";
	public static final String BLACK_BALL = "BLACK_BALL";
	
	public PoolBall(World world, float x, float y, float ballSize, float density, float linDamp, ColourBall colour) 
	{
		super(world, x, y, getSpriteFromColour(colour), ballSize, density, linDamp);
		this.colour = colour;
	}
	
	public static Sprite getSpriteFromColour(ColourBall color)
	{
		switch (color) 
		{
			case Black:
				return getBlackBallSprite();
			case Red:
				return getRedBallSprite();
			case Yellow:
				return getYellowBallSprite();
		}
		
		return null;
	}
	
	public static Sprite getBlackBallSprite()
	{
		if(!Art.spriteTable.containsKey(BLACK_BALL))
		{
			Art.spriteTable.put(BLACK_BALL, new Sprite(Art.textureTable.get(Art.BALLS), 53 * 1, 0, 53, 52));
		}
		
		return Art.spriteTable.get(BLACK_BALL);
	}
	
	public static Sprite getRedBallSprite()
	{
		if(!Art.spriteTable.containsKey(RED_BALL))
		{
			Art.spriteTable.put(RED_BALL, new Sprite(Art.textureTable.get(Art.BALLS), 53 * 1, 0, 53, 52));
		}
		
		return Art.spriteTable.get(RED_BALL);
	}
	
	public static Sprite getYellowBallSprite()
	{
		if(!Art.spriteTable.containsKey(YELLOW_BALL))
		{
			Art.spriteTable.put(YELLOW_BALL, new Sprite(Art.textureTable.get(Art.BALLS), 53 * 1, 0, 53, 52));
		}
		
		return Art.spriteTable.get(YELLOW_BALL);
	}
	
	public static enum ColourBall{Red, Yellow, Black};

}
