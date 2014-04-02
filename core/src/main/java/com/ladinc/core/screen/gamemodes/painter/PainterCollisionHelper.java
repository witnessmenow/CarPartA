package com.ladinc.core.screen.gamemodes.painter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.ladinc.core.collision.CollisionHelper;
import com.ladinc.core.collision.CollisionInfo;
import com.ladinc.core.collision.CollisionInfo.CollisionObjectType;
import com.ladinc.core.objects.FloorTileSensor;
import com.ladinc.core.screen.gamemodes.AbstractCollisionHelper;
import com.ladinc.core.vehicles.Vehicle;

public class PainterCollisionHelper extends AbstractCollisionHelper {
	
	public boolean enableTileChange = true;
	
	@Override
	public void beginContact(Contact contact)
	{
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		
		CollisionInfo bodyAInfo = getCollisionInfoFromFixture(fixtureA);
		CollisionInfo bodyBInfo = getCollisionInfoFromFixture(fixtureB);
		
		if (bodyAInfo != null && bodyBInfo != null)
		{
			
			Gdx.app.debug("beginContact",
					"between " + bodyAInfo.type.toString() + " and "
							+ bodyBInfo.type.toString());
			
			if (CollisionHelper.checkIfCollisionIsOfCertainBodies(bodyAInfo,
					bodyBInfo, CollisionObjectType.VehicleSensor,
					CollisionObjectType.FloorSensor)
					&& enableTileChange)
			{
				Vehicle v;
				FloorTileSensor fts;
				
				if (bodyAInfo.type == CollisionObjectType.FloorSensor)
				{
					v = (Vehicle) bodyBInfo.object;
					fts = (FloorTileSensor) bodyAInfo.object;
				}
				else
				{
					v = (Vehicle) bodyAInfo.object;
					fts = (FloorTileSensor) bodyBInfo.object;
				}
				
				if (v.player != null)
				{
					fts.setTeam(v.player.team);
				}
			}
		}
	}
}