package com.ladinc.core.screen.gamemodes.carpool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.ladinc.core.collision.CollisionHelper;
import com.ladinc.core.collision.CollisionInfo;
import com.ladinc.core.collision.CollisionInfo.CollisionObjectType;
import com.ladinc.core.objects.balls.PoolBall;
import com.ladinc.core.objects.balls.PoolBall.ColourBall;
import com.ladinc.core.utilities.Enums.Team;
import com.ladinc.core.vehicles.Vehicle;

public class CarPoolColisionHelper implements ContactListener
{
	public static final float RESPAWN_TIME = 5.0f;

	public boolean blackPotted = false;
	public Team lastTeamToTouchBlack = Team.Neutral;
	
	@Override
	public void beginContact(Contact contact) 
	{
		Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        
        CollisionInfo bodyAInfo = getCollisionInfoFromFixture(fixtureA);
    	CollisionInfo bodyBInfo = getCollisionInfoFromFixture(fixtureB);

        
        if(bodyAInfo != null && bodyBInfo != null)
        {
        	
        	Gdx.app.debug("beginContact", "between " + bodyAInfo.type.toString() + " and " + bodyBInfo.type.toString());
        	
        	if(CollisionHelper.checkIfCollisionIsOfCertainBodies(bodyAInfo, bodyBInfo, CollisionObjectType.Pocket, CollisionObjectType.BallSensor))
        	{
        		PoolBall pb;
        		if(bodyAInfo.type == CollisionObjectType.BallSensor)
        		{
        			pb = (PoolBall)bodyAInfo.object;
        			pb.isActive = false;
        		}
        		else
        		{
        			pb = (PoolBall)bodyBInfo.object;
        			pb.isActive = false;
        		}
        		
        		if(pb.colour == ColourBall.Black)
        		{
        			if(pb.lastPlayerToHit != null)
        			{
        				this.blackPotted = true;
        				this.lastTeamToTouchBlack = pb.lastPlayerToHit.team;
        			}
        		}

        		
        	}
        	else if(CollisionHelper.checkIfCollisionIsOfCertainBodies(bodyAInfo, bodyBInfo, CollisionObjectType.Pocket, CollisionObjectType.VehicleSensor))
        	{
        		if(bodyAInfo.type == CollisionObjectType.VehicleSensor)
        		{
        			Vehicle v = (Vehicle)bodyAInfo.object;
        			v.respawnTimeRemaining = RESPAWN_TIME;
        		}
        		
        		if(bodyBInfo.type == CollisionObjectType.VehicleSensor)
        		{
        			Vehicle v = (Vehicle)bodyBInfo.object;
        			v.respawnTimeRemaining = RESPAWN_TIME;
        		}
        	}
        	
        	//Check for collision of two Vehicles
        	if(bodyAInfo.type == CollisionObjectType.Vehicle && bodyBInfo.type == CollisionObjectType.Vehicle)
        	{
        		//vehicleCollideIsDetected(bodyAInfo, bodyBInfo);
        	}
        	
        	if(bodyAInfo.type == CollisionObjectType.Vehicle && bodyBInfo.type == CollisionObjectType.Ball)
        	{
        		PoolBall pb;
        		Vehicle v;
        		if(bodyAInfo.type == CollisionObjectType.Ball)
        		{
        			pb = (PoolBall)bodyAInfo.object;
        			v = (Vehicle)bodyBInfo.object;
        		}
        		else
        		{
        			pb = (PoolBall)bodyBInfo.object;
        			v = (Vehicle)bodyAInfo.object;
        		}
        		
        		pb.lastPlayerToHit = v.player;
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
