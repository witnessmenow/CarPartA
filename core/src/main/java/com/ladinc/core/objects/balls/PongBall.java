package com.ladinc.core.objects.balls;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.ladinc.core.assets.Art;
import com.ladinc.core.collision.CollisionInfo;
import com.ladinc.core.collision.CollisionInfo.CollisionObjectType;

public class PongBall 
{
	
	private static final float STARTING_SPEED = 20f;
	
	public Sprite sprite;
	public Body body;
	public float ballSize = 4f;
	
	public World world;
	
	public PongBall(World world, float x, float y, Sprite ballSprite) 
	{
		this.sprite = ballSprite;
		this.world = world;
		createPongBody(world, x, y);
	}
	
	public void createPongBody(World world, float x, float y)
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(x, y));
		
		this.body = world.createBody(bodyDef);

		PolygonShape sensorShape = new PolygonShape();
		sensorShape.setAsBox(this.ballSize / 2, this.ballSize / 2);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 1f;
		fixtureDef.isSensor=true;
		fixtureDef.shape = sensorShape;
		
		this.body.createFixture(fixtureDef);
	    
	    this.body.setUserData(new CollisionInfo("", CollisionObjectType.BallSensor, this));
	}
	
	public void destroyBody()
	{
		if(this.body != null)
		{
			this.world.destroyBody(this.body);
			this.body = null;
		}
	}
	
	public void updateBall()
	{
		if(hasWallBeenHit)
		{
			hasWallBeenHit = false;
			body.setLinearVelocity(body.getLinearVelocity().x, (body.getLinearVelocity().y)*(-1));
		}
	}
	
	private boolean hasWallBeenHit = false;
	
	public void wallHit()
	{
		//this.hasWallBeenHit = true;
		body.setLinearVelocity(body.getLinearVelocity().x, (body.getLinearVelocity().y)*(-1));
	}
	
	public void carHit()
	{
		body.setLinearVelocity(body.getLinearVelocity().x*(-1.5f), (body.getLinearVelocity().y)*(1.5f));
	}
	
	public void startBall()
	{
		if(Math.random() >= 0.5f)
		{
			body.setLinearVelocity(STARTING_SPEED, STARTING_SPEED);
		}
		else
		{
			body.setLinearVelocity(-STARTING_SPEED, STARTING_SPEED);
		}
	}
	
	public void updateSprite(SpriteBatch spriteBatch, int PIXELS_PER_METER)
	{
		Art.updateSprite(this.sprite, spriteBatch, PIXELS_PER_METER, this.body);
	}

}
