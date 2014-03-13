package com.ladinc.core.ai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.ladinc.core.controllers.controls.IControls;
import com.ladinc.core.objects.StartingPosition;
import com.ladinc.core.vehicles.Vehicle;

public class SimpleAi implements IControls
{

	private StartingPosition desiredPos;
	private Vehicle aiVehicle;
	
	private float steer;
	private float accelerate;
	
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
		return 1;
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
	
	public void setDesiredPosition(StartingPosition pos)
	{
		desiredPos = pos;
	}
	
	public void calculateMove()
	{
		calculateCarAngleInWorldTerms();
		calculateRelativeAngle();
		
		steer = steeringDirect(carAngle, relativeAngle);
		
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
		if(this.aiVehicle != null)
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
		Gdx.app.error("SimpleAi - steeringDirect",
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
		
		Gdx.app.error("SimpleAi - steeringDirect",
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
		
		Gdx.app.error("SimpleAi - steeringDirect",
				" Calculated Direction (1=R, -1=L): " +  tempDir);
	
		return tempDir;
	}

	@Override
	public boolean isAi() {
		// TODO Auto-generated method stub
		return true;
	}

}
