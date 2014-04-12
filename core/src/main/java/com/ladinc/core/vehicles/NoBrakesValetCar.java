package com.ladinc.core.vehicles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.core.objects.StartingPosition;
import com.ladinc.core.player.PlayerInfo;

public class NoBrakesValetCar extends Vehicle
{
	public float speed = 300f;
	
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
		this.maxSteerAngle = 5f;
		this.maxSpeed = 500f;
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
		
		//startCar(-250f);
		
		
		
		this.body.setLinearDamping(0.2f);
//		for(Wheel w: getWheels(true))
//		{
//			w.body.setLinearDamping(0.2f);
//		}

	}
	private Vector2 zeroV = new Vector2(0, 0);
	
	public void startCar(float speed)
	{
		Vector2 forceVector = new Vector2(0, this.power
				* speed);
		applyLinearVelocityToWheels(zeroV);
		
		// apply force to each wheel
		applyForceToWheels(forceVector);
	}
	
	@Override
	public void update(float deltaTime)
	{
		// 1. KILL SIDEWAYS VELOCITY
		//killSideWaysVelocity();
				
		// 2. SET WHEEL ANGLE
		calculateWheelAngle(deltaTime);
				
				
		// update revolving wheels
		setAngleOfWheels();
		
		if(speed > 0f)
		{
			//this.body.setLinearVelocity(0f, 0f);
			startCar(-speed);
			
			if(Math.abs(steer) > 0)
			{
				this.speed = this.speed - 0.5f;
			}
			else
			{
				this.speed = this.speed - 0.2f;
			}
		}
		else
		{
			speed = 0f;
		}
		
		killSideWaysVelocity();
		
		//applyForceToWheels(handleAcceleration());
		float curSpeed = this.getSpeedKMH();
		Gdx.app.error("NBV Update","carSpeed: " + speed + "KMpH: " + curSpeed);
	}
	
	@Override
	protected Vector2 handleAcceleration()
	{
		Vector2 baseVector = new Vector2(0, 0);
		;
		
		Gdx.app.debug("Vehicle",
				"handleAcceleration: GetSpeedKMH=" + this.getSpeedKMH()
						+ ", getLocalVelocity.y=" + this.getLocalVelocity().y);
		
		if (!this.handBrake)
			currentMaxSpeed = this.maxSpeed;
		else
			currentMaxSpeed = this.maxSpeed / 2;
		
		if (this.acceleration > 0)
		{
			// Player is pressing the button to accelerate
			
			// Car is going backwards, apply a large force to go forward (break)
			if (this.getLocalVelocity().y > 0)
			{
				baseVector = new Vector2(0f, -4.5f);
			}
			else if (this.getSpeedKMH() < (currentMaxSpeed * this.acceleration))
			{
				// User needs to be going faster for how hard they are pressing
				if (this.getLocalVelocity().y < 20)
					baseVector = new Vector2(0, -2.2f);
				else
					baseVector = new Vector2(0, -1.1f);
			}
			
		}
		else if (this.acceleration < 0)
		{
			// Player is pressing the button to reverse
			
			// Car is going forward, apply breaks
			if (this.getLocalVelocity().y < 0)
			{
				baseVector = new Vector2(0f, 4.5f);
			}
			else if (this.getSpeedKMH() < (currentMaxSpeed * (-1) * this.acceleration))
			{
				
				if (this.getLocalVelocity().y > (-20))
					baseVector = new Vector2(0, 2.2f);
				else
					baseVector = new Vector2(0, 1.1f);
			}
		}
		else
		{
			
			if (this.getSpeedKMH() < 7)
				this.setSpeed(0);
			else if (this.getLocalVelocity().y < 0)
				baseVector = new Vector2(0, 0.7f);
			else if (this.getLocalVelocity().y > 0)
				baseVector = new Vector2(0, -0.7f);
		}
		
		return baseVector;
	}
}
