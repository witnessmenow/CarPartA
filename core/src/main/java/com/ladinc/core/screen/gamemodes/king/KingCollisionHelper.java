package com.ladinc.core.screen.gamemodes.king;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.ladinc.core.collision.CollisionInfo;
import com.ladinc.core.collision.CollisionInfo.CollisionObjectType;
import com.ladinc.core.screen.gamemodes.AbstractCollisionHelper;
import com.ladinc.core.utilities.Enums.Team;
import com.ladinc.core.vehicles.Vehicle;

public class KingCollisionHelper extends AbstractCollisionHelper {
	
	public Team currentKingSide;
	public Vehicle currentKingVehicle;
	
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
				
				if (v1.king && v2.spinoutTimeRemaining <= 0f && enableChange)
				{
					v1.king = false;
					v2.king = true;
					
					currentKingSide = v2.player.team;
					currentKingVehicle = v2;
					v1.spinoutTimeRemaining = 1.5f;
				}
				else if (v2.king && v1.spinoutTimeRemaining <= 0f
						&& enableChange)
				{
					v2.king = false;
					v1.king = true;
					
					currentKingSide = v1.player.team;
					currentKingVehicle = v1;
					v2.spinoutTimeRemaining = 1.5f;
				}
			}
		}
	}
}