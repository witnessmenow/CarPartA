package com.ladinc.core.ai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.ladinc.core.controllers.controls.IControls;
import com.ladinc.core.objects.FloorTileSensor;
import com.ladinc.core.objects.StartingPosition;
import com.ladinc.core.utilities.Enums.Team;
import com.ladinc.core.vehicles.Vehicle;

public class SimpleAi implements IControls
{

	public StartingPosition desiredPos;
	private Vehicle aiVehicle;
	
	public Team team;
	
	private float steer;
	private int accelerate;
	
	public FloorTileSensor assignedTile;
	
	public Vehicle getVehicle()
	{
		return aiVehicle;
	}
	
	@Override
	public boolean isActive() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setActive(boolean active) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getAcceleration() {
		// TODO Auto-generated method stub
		return accelerate;
	}

	@Override
	public float getSteering() {
		//1 full right, -1 full left
		return steer;
	}

	@Override
	public boolean getHandbreakStatus() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getStartStatus() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getConfirmStatus() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getBackStatus() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setMenuInterest(boolean set) {
		// TODO Auto-generated method stub
		
	}
	
	public float timeOnTile = 0.0f;
	
	public void setFloorTileSensor(FloorTileSensor fts)
	{
		if(this.assignedTile != fts)
		{
			this.assignedTile = fts;
			timeOnTile = 0.0f;
		}
	}
	
	public void resetTimers()
	{
		remaingOutOfStuckTime = 0.0f;
		timeAtLowSpeed = 0.0f;
		timeOnTile = 0.0f;
		
	}
	
	public void setDesiredCoOrd(Vector2 coOrd)
	{
		if(coOrd != null)
		{
			if(desiredPos == null)
			{
				desiredPos = new StartingPosition(coOrd, 0);
			}
			else
			{
				desiredPos.position = coOrd;
			}
		}
	}
	
	public void resetAiBetweenLevels()
	{
		resetTimers();
		this.assignedTile = null;
		justAccelerateAndReverse = false;
	}
	
//	public void setDesiredPosition(StartingPosition pos)
//	{
//		desiredPos = pos;
//	}
	
	float timeAtAngle = 0.0f;
	float timeAtLowSpeed = 0.0f;
	float previousAngle = 0.0f;
	
	float remaingOutOfStuckTime = 0.0f;
	
	private boolean checkForStuck(float delta)
	{
		Gdx.app.debug("SimpleAi - checkForStuck",
				" Car velocity: " + this.aiVehicle.getSpeedKMH() + " Car Angle: " + this.carAngle);
		if(this.aiVehicle.getSpeedKMH() < 5)
		{
			timeAtLowSpeed += delta;
			
			if(timeAtLowSpeed > 1.5f)
			{
				timeAtLowSpeed = 0.0f;
				remaingOutOfStuckTime = 1.0f;
				
				lastUsedSteer = lastUsedSteer * (-1);
				
				return true;
			}
		}
		
		if(timeOnTile > 4f)
		{
			remaingOutOfStuckTime = 1.0f;
			
			lastUsedSteer = lastUsedSteer * (-1);
			timeOnTile = 0f;
		}
		
		return false;
	}
	
	public boolean justAccelerateAndReverse = false;
	
	public void calculateMove(float delta)
	{
		
		if(this.aiVehicle != null)
		{
			if(this.assignedTile != null)
			{
				this.timeOnTile += delta;
			}
			
			
			if(!justAccelerateAndReverse)
			{
				calculateCarAngleInWorldTerms();
				calculateRelativeAngle();
			
				
				if(remaingOutOfStuckTime > 0)
				{
					escapeStuck(delta);
				}
				else
				{
					checkForStuck(delta);
					steer = steeringDirect(carAngle, relativeAngle);
					accelerate = 1;
				}
				
				return;
			}
			else
			{
				steer = 0;
				
				if(this.desiredPos.position.y > this.aiVehicle.body.getWorldCenter().y + 5f)
				{
					accelerate = 1;
				}
				else if (this.desiredPos.position.y < this.aiVehicle.body.getWorldCenter().y - 5f)
				{
					accelerate = -1;
				}
				else
				{
					accelerate = 0;
				}
			}
		}
		else
		{
			steer = 0;
			accelerate = 0;
		}
		
	}
	
	private float lastUsedSteer = 1;
	
	private void escapeStuck(float delta) 
	{
		remaingOutOfStuckTime -= delta;
		
		steer = lastUsedSteer;
		accelerate = -1;
		
	}

	//Basically the default angle of the car is pointing down, so it needs to be converted relative to world
	private void calculateCarAngleInWorldTerms()
	{
		if(this.aiVehicle != null)
		{
			//Angle is provided in radians, convert to degrees
			carAngle = (this.aiVehicle.body.getAngle() * MathUtils.radiansToDegrees);
			
			Gdx.app.debug("SimpleAi - Car Angle",
					" Car Angle after converted from radians to degrees: " + carAngle);
			
			//when the car is facing down it has an angle of 0. To offest this so it reads as you would think (0 being left, 180 being right)
			//we need to take 90 degrees away from it
			carAngle = carAngle - 90.0f;
			
			Gdx.app.debug("SimpleAi - Car Angle",
					" Car Angle adjusted by 90 degrees so it is a world angle: " + carAngle);
			
			if(carAngle > 360.0f)
			{
				carAngle = carAngle % 360.0f;
				
			}
			else if(carAngle < 0.0f)
			{	
				
				carAngle = carAngle % 360.0f;
				
				carAngle = 360.0f + carAngle;
				
				//Adjust negative angles: -1 degree == 359 degrees
				
			}
			
			Gdx.app.debug("SimpleAi - Car Angle",
					" finalised car angle: " + carAngle);
		}
	}
	
	private void calculateRelativeAngle()
	{
		if(this.aiVehicle != null && relativeVector != null)
		{
			relativeVector.x = this.desiredPos.position.x - this.aiVehicle.body.getWorldCenter().x;
			relativeVector.y = this.desiredPos.position.y - this.aiVehicle.body.getWorldCenter().y;
			
			relativeAngle = relativeVector.angle();
			
			Gdx.app.debug("SimpleAi - Relative Angle",
					" Relative Vector: (" + relativeVector.x + "," + relativeVector.y + ") , Relative Angle: " +  relativeAngle);
		}
	}
	
	public void setVehicle(Vehicle vehicle)
	{
		this.aiVehicle = vehicle;
	}
	
	float carAngle;
	float relativeAngle;
	Vector2 relativeVector = new Vector2();
	
	float tempDir;
	//When car is facing right direction and ball is in front
	private float steeringDirect(float cAngle, float relAngle)
	{
		Gdx.app.debug("SimpleAi - steeringDirect",
				" Car Angle: " + cAngle + " , Relative Angle: " +  relAngle);
		
		tempDir = 0.0f;
		
		if( Math.abs((relAngle + 360.0f) - cAngle) < Math.abs(relAngle - cAngle))
		{
			relAngle = relAngle + 360.0f;
		}
		else if( Math.abs((relAngle - 360.0f) - cAngle) < Math.abs(relAngle - cAngle))
		{
			relAngle = relAngle - 360.0f;
		}
		
		Gdx.app.debug("SimpleAi - steeringDirect",
				" Adjusted Relative Angle: " +  relAngle);
		
		if(cAngle + 6f >= relAngle &&  cAngle - 6f <= relAngle)
		{
			tempDir = 0;
		}
		else if(relAngle > cAngle)
		{
//			if(cAngle + 180f < relAngle)
//				tempDir = 1;
//			else
				tempDir = -1;
		}
		else
		{
//			if(cAngle - 180f < relAngle)
//				tempDir = -1.0f;
//			else
				tempDir = 1;
		}
		
		Gdx.app.debug("SimpleAi - steeringDirect",
				" Calculated Direction (1=R, -1=L): " +  tempDir);
	
		return tempDir;
	}

	@Override
	public boolean isAi() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean getExtraButton1Status() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getExtraButton2Status() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getMenuXDireciton() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMenuYDireciton() {
		// TODO Auto-generated method stub
		return 0;
	}

}
