package com.ladinc.core.objects.balls;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.ladinc.core.assets.Art;
import com.ladinc.core.collision.CollisionInfo;
import com.ladinc.core.collision.CollisionInfo.CollisionObjectType;

public class PoolBall extends Ball
{
	public ColourBall colour;
	
	public Boolean isActive = true;
	
	public Body sensorBody;

	public static final String RED_BALL = "RED_BALL";
	public static final String YELLOW_BALL = "YELLOW_BALL";
	public static final String BLACK_BALL = "BLACK_BALL";
	
	public PoolBall(World world, float x, float y, float ballSize, float density, float linDamp, ColourBall colour) 
	{
		super(world, x, y, getSpriteFromColour(colour), ballSize, density, linDamp);
		this.colour = colour;
		
		createSensorBody(world, x, y);
		
	}
	
	private void createSensorBody(World world, float x, float y)
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		
		this.sensorBody = world.createBody(bodyDef);

		CircleShape dynamicCircle = new CircleShape();
		dynamicCircle.setRadius(0.1f);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 1f;
		fixtureDef.isSensor=true;
		fixtureDef.shape = dynamicCircle;
		
		this.sensorBody.createFixture(fixtureDef);

	    PrismaticJointDef jointdef=new PrismaticJointDef();
        jointdef.initialize(this.body, this.sensorBody, this.sensorBody.getWorldCenter(), new Vector2(1, 1));
        jointdef.enableLimit=true;
        jointdef.lowerTranslation=jointdef.upperTranslation=0;
	    world.createJoint(jointdef);
	    
	    this.sensorBody.setUserData(new CollisionInfo("", CollisionObjectType.BallSensor, this));
		
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
			Art.spriteTable.put(RED_BALL, new Sprite(Art.textureTable.get(Art.BALLS), 53 * 3, 0, 53, 52));
		}
		
		return Art.spriteTable.get(RED_BALL);
	}
	
	public static Sprite getYellowBallSprite()
	{
		if(!Art.spriteTable.containsKey(YELLOW_BALL))
		{
			Art.spriteTable.put(YELLOW_BALL, new Sprite(Art.textureTable.get(Art.BALLS), 53 * 2, 0, 53, 52));
		}
		
		return Art.spriteTable.get(YELLOW_BALL);
	}
	
	public void destroyBodies(World world)
	{
		world.destroyBody(body);
		world.destroyBody(sensorBody);
		
		this.body.setUserData(null);
		this.sensorBody.setUserData(null);
		
		this.body = null;
		this.sensorBody = null;
	}
	
	public static enum ColourBall{Red, Yellow, Black};

}
