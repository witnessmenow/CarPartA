package com.ladinc.core.vehicles;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.core.controllers.controls.IControls;
import com.ladinc.core.objects.StartingPosition;
import com.ladinc.core.player.PlayerInfo;

public class Car extends Vehicle {

	public Car(PlayerInfo player, World world, StartingPosition position, Sprite carSprite, Sprite wheelSprite) 
	{
		super();

		//TODO: Default Car Settings - Should be read from config?
		//Size
		this.width = 5f;
		this.length = 10f;

		//Handling
		this.maxSteerAngle = 20f;
		this.maxSpeed = 350f;
		this.power = 500f;

		float wheelWidth = 1f;
		float wheelLength = 2f;

		this.player = player;
		if(player != null)
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
//		Team team = null;
//		if(player != null)
//		{
//			collisionText = player.playerName;
//			team = player.playerTeam;
//		}
//
//		this.body.setUserData(new CollisionInfo(collisionText, CollisionObjectType.car, team));

		//initialize wheels
		float wheelXPos = this.width/2;
		float wheelYPos = (0.6f) * this.length/2;

		this.wheels = new ArrayList<Wheel>();
		this.wheels.add(new Wheel(world, this, -(wheelXPos), (-wheelYPos), wheelWidth, wheelLength, true,  true, wheelSprite)); //top left
		this.wheels.add(new Wheel(world, this, (wheelXPos), (-wheelYPos), wheelWidth, wheelLength, true,  true, wheelSprite)); //top right
		this.wheels.add(new Wheel(world, this, -(wheelXPos), wheelYPos, wheelWidth, wheelLength, false,  false, wheelSprite)); //back left
		this.wheels.add(new Wheel(world, this, (wheelXPos), wheelYPos, wheelWidth, wheelLength, false,  false, wheelSprite)); //back right


		this.sprite = carSprite;
	}

}