package com.ladinc.core.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.core.collision.CollisionInfo;
import com.ladinc.core.collision.CollisionInfo.CollisionObjectType;

public class Ball {
	public Body body;
	float ballSize = 2f;
	public static final float ballOffsetX = 0f;
	public Sprite sprite;
	
	public Ball(World world, float x, float y, Sprite ballSprite, float ballSize) {
		this.ballSize = ballSize;
		createBallObject(world, x, y, ballSprite, false);
	}
	
	public Ball(World world, float x, float y, Sprite ballSprite) {
		createBallObject(world, x, y, ballSprite, false);
	}
	
	public Ball(World world, float x, float y, Sprite ballSprite,
			boolean networked) {
		
		createBallObject(world, x, y, ballSprite, networked);
	}
	
	private void createBallObject(World world, float x, float y,
			Sprite ballSprite, boolean networked)
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		this.body = world.createBody(bodyDef);
		CircleShape dynamicCircle = new CircleShape();
		dynamicCircle.setRadius(ballSize);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = dynamicCircle;
		fixtureDef.density = 0.25f;
		fixtureDef.friction = 0f;
		fixtureDef.restitution = 1f;
		
		this.body.createFixture(fixtureDef);
		this.sprite = ballSprite;
		
		this.body.setUserData(new CollisionInfo("Ball", CollisionObjectType.Ball));
		
		dynamicCircle.dispose();
	}
	
	public Vector2 getLocalVelocity()
	{
		/*
		 * returns balls's velocity vector relative to the car
		 */
		return this.body.getLocalVector(this.body
				.getLinearVelocityFromLocalPoint(new Vector2(0, 0)));
	}
	
	public void update()
	{
		
		 Vector2 currentVelocity = this.getLocalVelocity(); Vector2 position = this.getLocation();
		 
		 Gdx.app.debug("Ball Update",
				 "Ball Position - " + position +
						  "Ball Velocity - " + currentVelocity);
		  
		  float slowDownMultiplier = 0.75f;
		  
		  this.body.applyForce(this.body.getWorldVector(new Vector2( -(currentVelocity.x * (slowDownMultiplier)), -(currentVelocity.y * (slowDownMultiplier)))), position, true);
		 
	}
	
	public void resetPositionToStart(Vector2 startPoint)
	{
		this.body.setTransform(startPoint, 0f);
		this.body.setLinearVelocity(0f,0f);
		this.body.setAngularVelocity(0f);
	}
	
	public void networkUpdate(Vector2 velocity, Vector2 position)
	{
		this.body.setTransform(position, 0);
		// this.body.
	}
	
	public Vector2 getLocation()
	{
		return this.body.getWorldCenter();
	}
}