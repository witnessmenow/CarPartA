package com.ladinc.core.screen.gamemodes.hill;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.ladinc.core.collision.CollisionInfo;
import com.ladinc.core.collision.CollisionInfo.CollisionObjectType;
import com.ladinc.core.screen.gamemodes.AbstractCollisionHelper;
import com.ladinc.core.utilities.Enums.Team;
import com.ladinc.core.vehicles.Vehicle;

public class HillCollisionHelper extends AbstractCollisionHelper {
	
	public Team currentHillSide;
	
	public boolean enableChange = true;
	
	@Override
	public void beginContact(Contact contact)
	{
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		
		CollisionInfo bodyAInfo = getCollisionInfoFromFixture(fixtureA);
		CollisionInfo bodyBInfo = getCollisionInfoFromFixture(fixtureB);
		
		if (bodyAInfo != null && bodyBInfo != null)
		{
			// Check for collision of two Vehicles
			if (bodyAInfo.type == CollisionObjectType.Vehicle
					&& bodyBInfo.type == CollisionObjectType.Vehicle)
			{
				Vehicle v1 = (Vehicle) bodyAInfo.object;
				Vehicle v2 = (Vehicle) bodyBInfo.object;
				
				currentHillSide = v2.player.team;
				if (v1.getSpeedKMH() > v2.getSpeedKMH())
				{
					spinVehicle(v2);
				}
				else
				{
					spinVehicle(v1);
				}
			}
			else if (currentHillSide == null)
			{
				if (bodyAInfo.type == CollisionObjectType.FloorSensor
						&& bodyBInfo.type == CollisionObjectType.Vehicle)
				{
					Vehicle v2 = (Vehicle) bodyBInfo.object;
					currentHillSide = v2.player.team;
				}
				else if (bodyBInfo.type == CollisionObjectType.FloorSensor
						&& bodyAInfo.type == CollisionObjectType.Vehicle)
				{
					Vehicle v1 = (Vehicle) bodyAInfo.object;
					currentHillSide = v1.player.team;
				}
			}
		}
	}
	
	private void spinVehicle(Vehicle vehicle)
	{
		if (vehicle.spinoutTimeRemaining <= 0f)
		{
			vehicle.spinoutTimeRemaining = 1.5f;
		}
	}
}