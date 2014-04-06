package com.ladinc.core.vehicles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.core.objects.StartingPosition;
import com.ladinc.core.player.PlayerInfo;

public class NoBrakesValetCar extends Vehicle
{
	public NoBrakesValetCar(PlayerInfo player, World world, StartingPosition position,
			Sprite carSprite, Sprite wheelSprite) 
	{
		super();
		
		this.world = world;
		
		// TODO: Default Car Settings - Should be read from config?
		// Size
		this.width = 5f;
		this.length = 10f;
		
		// Handling
		this.maxSteerAngle = 20f;
		this.maxSpeed = 350f;
		this.power = 500f;
		
		this.wheelWidth = 1f;
		this.wheelLength = 2f;
		
		this.player = player;
		if (player != null)
		{
			this.controls = player.controls;
		}
		
		this.steer = Vehicle.STEER_NONE;
		this.accelerate = Vehicle.ACC_NONE;
		
		this.angle = position.angle;
		this.position = position.position;
		this.wheelAngle = 0;
		
		super.createBody(world);
		
		String collisionText = "";
		
		// initialize wheels
		this.wheelSprite = wheelSprite;
		createWheels();
		
		this.sprite = carSprite;
		
		startCar(-500f);
		
		
		this.body.setLinearDamping(0.2f);
//		for(Wheel w: getWheels(true))
//		{
//			w.body.setLinearDamping(0.2f);
//		}

	}
	
	public void startCar(float speed)
	{
		Vector2 forceVector = new Vector2(0, this.power
				* speed);
		
		
		// apply force to each wheel
		applyForceToWheels(forceVector);
	}
	
	@Override
	public void update(float deltaTime)
	{
		// 1. KILL SIDEWAYS VELOCITY
		killSideWaysVelocity();
				
		// 2. SET WHEEL ANGLE
		calculateWheelAngle(deltaTime);
				
				
		// update revolving wheels
		setAngleOfWheels();
	
		if(Math.abs(steer) > 0)
		{
			applyForceToWheels(new Vector2(0, -this.getSpeedKMH()));
		}
		
		Gdx.app.error("NBV Update","Car Speed: " + this.getSpeedKMH());
	}
}
