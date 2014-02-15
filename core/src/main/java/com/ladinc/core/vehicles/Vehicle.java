package com.ladinc.core.vehicles;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.core.controllers.controls.IControls;
import com.ladinc.core.player.PlayerInfo;

public class Vehicle {

	public Body body;
	float width, length, angle, maxSteerAngle, maxSpeed, power;
	float wheelAngle;
	public int steer, accelerate;
	public Vector2 position;
	public List<Wheel> wheels;

	public IControls controls;
	public PlayerInfo player;

	private float acceleration = 0f;

	public static final int STEER_NONE = 0;
	public static final int STEER_RIGHT = 1;
	public static final int STEER_LEFT = 2;

	public static final int ACC_NONE = 0;
	public static final int ACC_ACCELERATE = 1;
	public static final int ACC_BRAKE = 2;

	public Sprite sprite;

	private boolean handBrake;

	public void createBody(World world) {
		// init body
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(position);
		bodyDef.angle = angle;
		this.body = world.createBody(bodyDef);

		// init shape
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 1.0f;
		fixtureDef.friction = 0.6f; // friction when rubbing against other
									// shapes
		fixtureDef.restitution = 0.4f; // amount of force feedback when hitting
										// something. >0 makes the car bounce
										// off, it's fun!
		PolygonShape carShape = new PolygonShape();
		carShape.setAsBox(this.width / 2, this.length / 2);
		fixtureDef.shape = carShape;
		this.body.createFixture(fixtureDef);
	}

	public List<Wheel> getPoweredWheels() {
		List<Wheel> poweredWheels = new ArrayList<Wheel>();
		for (Wheel wheel : this.wheels) {
			if (wheel.powered)
				poweredWheels.add(wheel);
		}
		return poweredWheels;
	}

	public Vector2 getLocalVelocity() {
		/*
		 * returns car's velocity vector relative to the car
		 */
		return this.body.getLocalVector(this.body
				.getLinearVelocityFromLocalPoint(new Vector2(0, 0)));
	}

	public void controlVehicle() {

		if (this.controls != null) {
			// Steering
			if (this.controls.getSteering() > 0) {
				this.steer = Vehicle.STEER_RIGHT;
			} else if (this.controls.getSteering() < 0) {
				this.steer = Vehicle.STEER_LEFT;
			} else {
				this.steer = Vehicle.STEER_NONE;
			}
			//
			
			this.handBrake = this.controls.getHandbreakStatus();

			this.acceleration = this.controls.getAcceleration();
		}

		this.update(Gdx.app.getGraphics().getDeltaTime());
	}

	List<Wheel> tempWheels = new ArrayList<Wheel>();
	public List<Wheel> getWheels(Boolean revolving) 
	{
		tempWheels.clear();
		for (Wheel wheel : this.wheels) 
		{
			if (wheel.revolving == revolving)
				tempWheels.add(wheel);
		}
		return tempWheels;
	}

	public float getSpeedKMH() {
		Vector2 velocity = this.body.getLinearVelocity();
		float len = velocity.len();
		return (len / 1000) * 3600;
	}

	public void setSpeed(float speed) {
		/*
		 * speed - speed in kilometers per hour
		 */
		Vector2 velocity = this.body.getLinearVelocity();
		velocity = velocity.nor();
		velocity = new Vector2(velocity.x * ((speed * 1000.0f) / 3600.0f),
				velocity.y * ((speed * 1000.0f) / 3600.0f));
		this.body.setLinearVelocity(velocity);
	}

	public void destroyVehicle() {
		World world = this.body.getWorld();

		// update revolving wheels
		for (Wheel wheel : this.wheels) {
			world.destroyBody(wheel.body);
		}

		world.destroyBody(this.body);
	}

	private Vector2 handleAccelerationOld() {
		// 3. APPLY FORCE TO WHEELS
		Vector2 baseVector; // vector pointing in the direction force will be
							// applied to a wheel ; relative to the wheel.

		// if accelerator is pressed down and speed limit has not been reached,
		// go forwards
		if ((this.accelerate == Vehicle.ACC_ACCELERATE)
				&& (this.getSpeedKMH() < this.maxSpeed)) {

			if (this.getLocalVelocity().y < 20)
				baseVector = new Vector2(0, -2.2f);
			else
				baseVector = new Vector2(0, -1.1f);
		} else if (this.accelerate == Vehicle.ACC_BRAKE) {
			// braking, but still moving forwards - increased force
			if (this.getLocalVelocity().y < 0)
				baseVector = new Vector2(0f, 4f);
			// going in reverse - less force
			else
				baseVector = new Vector2(0f, 1f);
		} else if (this.accelerate == Vehicle.ACC_NONE) {
			// slow down if not accelerating
			baseVector = new Vector2(0, 0);
			if (this.getSpeedKMH() < 7)
				this.setSpeed(0);
			else if (this.getLocalVelocity().y < 0)
				baseVector = new Vector2(0, 0.7f);
			else if (this.getLocalVelocity().y > 0)
				baseVector = new Vector2(0, -0.7f);
		} else
			baseVector = new Vector2(0, 0);

		return baseVector;
	}

	private float currentMaxSpeed;
	
	private Vector2 handleAcceleration() {
		Vector2 baseVector = new Vector2(0, 0);
		;

		Gdx.app.debug("Vehicle",
				"handleAcceleration: GetSpeedKMH=" + this.getSpeedKMH()
						+ ", getLocalVelocity.y=" + this.getLocalVelocity().y);

		if(!this.handBrake)
			currentMaxSpeed = this.maxSpeed;
		else
			currentMaxSpeed = this.maxSpeed/2;
		
		if (this.acceleration > 0) {
			// Player is pressing the button to accelerate

			// Car is going backwards, apply a large force to go forward (break)
			if (this.getLocalVelocity().y > 0) {
				baseVector = new Vector2(0f, -4.5f);
			} else if (this.getSpeedKMH() < (currentMaxSpeed * this.acceleration)) {
				// User needs to be going faster for how hard they are pressing
				if (this.getLocalVelocity().y < 20)
					baseVector = new Vector2(0, -2.2f);
				else
					baseVector = new Vector2(0, -1.1f);
			}

		} else if (this.acceleration < 0) {
			// Player is pressing the button to reverse

			// Car is going forward, apply breaks
			if (this.getLocalVelocity().y < 0) {
				baseVector = new Vector2(0f, 4.5f);
			} else if (this.getSpeedKMH() < (currentMaxSpeed * (-1) * this.acceleration)) {

				if (this.getLocalVelocity().y > (-20))
					baseVector = new Vector2(0, 2.2f);
				else
					baseVector = new Vector2(0, 1.1f);
			}
		} else {

			if (this.getSpeedKMH() < 7)
				this.setSpeed(0);
			else if (this.getLocalVelocity().y < 0)
				baseVector = new Vector2(0, 0.7f);
			else if (this.getLocalVelocity().y > 0)
				baseVector = new Vector2(0, -0.7f);
		}

		return baseVector;
	}

	private float backWheelAngle;
	
	public void update(float deltaTime) {

		// 1. KILL SIDEWAYS VELOCITY

		if (false) {

			for (Wheel wheel : wheels) {
				if (wheel.powered)
					wheel.killSidewaysVelocity();
				// else
				// wheel.dampSidewaysVelocity();
			}
		} else {
			for (Wheel wheel : wheels) {
				wheel.killSidewaysVelocity();
			}

		}

		// 2. SET WHEEL ANGLE

		// calculate the change in wheel's angle for this update
		float incr = (this.maxSteerAngle) * deltaTime * 5;

		if (this.steer == Vehicle.STEER_LEFT) {
			this.wheelAngle = Math.min(Math.max(this.wheelAngle, 0) + incr,
					this.maxSteerAngle); // increment angle without going over
											// max steer
		} else if (this.steer == Vehicle.STEER_RIGHT) {
			this.wheelAngle = Math.max(Math.min(this.wheelAngle, 0) - incr,
					-this.maxSteerAngle); // decrement angle without going over
											// max steer
		} else {
			this.wheelAngle = 0;
		}

		Gdx.app.debug("Vehicle",
				"update: WheelAngle=" + this.wheelAngle);
		
		// update revolving wheels
		for (Wheel wheel : this.getWheels(true)) 
		{
			wheel.setAngle(this.wheelAngle);
		}
		
		if(this.handBrake)
		{
			this.backWheelAngle = (float) (((-1) * this.wheelAngle)/(1.5));
		}
		else
		{
			this.backWheelAngle = 0f;
		}
		
		for (Wheel wheel : this.getWheels(false)) 
		{
			wheel.setAngle(this.backWheelAngle);
		}

		Vector2 baseVector = handleAcceleration();

		// multiply by engine power, which gives us a force vector relative to
		// the wheel
		Vector2 forceVector = new Vector2(this.power * baseVector.x, this.power
				* baseVector.y);

		// apply force to each wheel
		for (Wheel wheel : this.getPoweredWheels()) {
			Vector2 position = wheel.body.getWorldCenter();
			wheel.body.applyForce(wheel.body.getWorldVector(new Vector2(
					forceVector.x, forceVector.y)), position, true);
		}

		// System.out.println(this.playerName + "'s Car Speed: " +
		// this.getSpeedKMH());
		// if going very slow, stop - to prevent endless sliding

	}

	public void updateSprite(SpriteBatch spriteBatch, int PIXELS_PER_METER) {

		// Update Wheels Sprites
		for (Wheel wheel : wheels) {
			wheel.updateSprite(spriteBatch, PIXELS_PER_METER);
		}

		// Update Car Body Sprite
		// SpriteHelper.updateSprite(this.sprite, spriteBatch, PIXELS_PER_METER,
		// this.body);
	}
}
