package com.ladinc.core.ai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.ladinc.core.controllers.controls.IControls;
import com.ladinc.core.objects.StartingPosition;
import com.ladinc.core.vehicles.Vehicle;

public class SimpleAi implements IControls
{

	StartingPosition desiredPos;
	Vehicle aiVehicle;
	
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
		//return 1;
		return steeringDirect();
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
	
	public void setVehicle(Vehicle vehicle)
	{
		this.aiVehicle = vehicle;
	}
	
	float carAngle;
	float relativeAngle;
	Vector2 relativeVector = new Vector2();
	
	//When car is facing right direction and ball is in front
	private float steeringDirect()
	{
		if(this.aiVehicle != null)
		{
			carAngle = (this.aiVehicle.body.getAngle() * MathUtils.radiansToDegrees);
			
			Gdx.app.error("SimpleAi",
					" carAngle1: " + carAngle);
			
			//if the car is facing down it has an angle of 0. To offest this so it reads as you would think
			//we need to take 90 degrees away from it
			carAngle = carAngle - 90.0f;
			
			Gdx.app.error("SimpleAi",
					" carAngle2: " + carAngle);
			
			if(carAngle > 360.0f)
			{
				carAngle = carAngle % 360.0f;
				
			}
			else if(carAngle < 0.0f)
			{	
				
				carAngle = carAngle % 360.0f;
				
				Gdx.app.error("SimpleAi",
						" carAngle3: " + carAngle);
				
				carAngle = 360.0f + carAngle;
				
			}
			//Purposely letting carAngle == 0.0f fall through
			
			Gdx.app.error("SimpleAi",
					" carAngle4: " + carAngle);
			
//			//if the car is facing down it has an angle of 0. To offest this so it reads as you would think
//			//we need to take 90 degrees away from it
//			carAngle = carAngle - 90.0f;
//			
//			Gdx.app.error("SimpleAi",
//					" carAngle3: " + carAngle);
//			
//			if(carAngle < 0.0f)
//			{
//				carAngle = 360.0f - carAngle;
//				
//				Gdx.app.error("SimpleAi",
//						" carAngle4: " + carAngle);
//			}
			
			relativeVector.x = this.desiredPos.position.x - this.aiVehicle.body.getWorldCenter().x;
			relativeVector.y = this.desiredPos.position.y - this.aiVehicle.body.getWorldCenter().y;
			
			relativeAngle = relativeVector.angle();
		}
		
		Gdx.app.error("SimpleAi",
				"relativeAngle: " + relativeAngle + " carAngle: " + carAngle);
		
		if( ((relativeAngle + 360.0f) - carAngle) < (relativeAngle - carAngle))
		{
			relativeAngle = relativeAngle + 360.0f;
		}
		
		if(carAngle + 6f >= relativeAngle &&  carAngle - 6f <= relativeAngle)
		{
			return 0;
		}
		
		if(relativeAngle > carAngle)
		{
			if(carAngle + 180f < relativeAngle)
				return  1.0f;
			else
				return -1;
		}
		else
		{
			return 1;
//			if(angleMovement - 180f < angleStick)
//				direction = -1.0f;
		}
	
		//return 0;
	}

	@Override
	public boolean isAi() {
		// TODO Auto-generated method stub
		return true;
	}

}
