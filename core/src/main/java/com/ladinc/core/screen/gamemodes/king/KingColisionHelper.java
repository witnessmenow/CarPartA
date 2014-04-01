package com.ladinc.core.screen.gamemodes.king;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.ladinc.core.collision.CollisionInfo;
import com.ladinc.core.collision.CollisionInfo.CollisionObjectType;
import com.ladinc.core.utilities.Enums.Team;
import com.ladinc.core.vehicles.Vehicle;

public class KingColisionHelper implements ContactListener
{

	public Team currentKingSide;
	public Vehicle currentKingVehicle;
	
	@Override
	public void beginContact(Contact contact) 
	{
		Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        
        CollisionInfo bodyAInfo = getCollisionInfoFromFixture(fixtureA);
    	CollisionInfo bodyBInfo = getCollisionInfoFromFixture(fixtureB);

        
        if(bodyAInfo != null && bodyBInfo != null)
        {
        	//Check for collision of two Vehicles
        	if(bodyAInfo.type == CollisionObjectType.Vehicle && bodyBInfo.type == CollisionObjectType.Vehicle)
        	{
        		Vehicle v1 = (Vehicle)bodyAInfo.object;
        		Vehicle v2 = (Vehicle)bodyBInfo.object;
        		
        		if(v1.king)
        		{
        			v1.king = false;
        			v2.king = true;
        			
        			currentKingSide = v2.player.team;
        			currentKingVehicle = v2;
        		}
        		else if(v2.king)
        		{
        			v2.king = false;
        			v1.king = true;
        			
        			currentKingSide = v1.player.team;
        			currentKingVehicle = v1;
        		}
        		
        	}	
        }
		
	}
	
	private CollisionInfo getCollisionInfoFromFixture(Fixture fix)
	{	
		CollisionInfo colInfo = null;
		
		if(fix != null)
        {
			Body body = fix.getBody();
			
			if(body != null)
			{
				colInfo = (CollisionInfo) body.getUserData();
			}
        }
		
		return colInfo;
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

}
