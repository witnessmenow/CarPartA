package com.ladinc.core.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.ladinc.core.collision.CollisionInfo.CollisionObjectType;
import com.ladinc.core.utilities.Enums.Team;

public class CollisionHelper implements ContactListener
{
	
	public CollisionHelper()
	{
	}
	
	private Team lastScore;
	public boolean newScore = false;
	
	public Team getLastScored()
	{
		newScore = false;
		return lastScore;
	}

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
        	
        	if((bodyAInfo.type == CollisionObjectType.ScoreZone && bodyBInfo.type == CollisionObjectType.Ball) || 
        			(bodyAInfo.type == CollisionObjectType.Ball && bodyBInfo.type == CollisionObjectType.ScoreZone))
        	{

        		//Goal Scored
        		Gdx.app.log("beginContact", "Goal Scored!");
        		
        		if(bodyAInfo.type == CollisionObjectType.ScoreZone)
        			lastScore = bodyAInfo.team;
        		else
        			lastScore = bodyBInfo.team;
        		
        		newScore = true;
        		
        	}
        	//Check for collision of two Vehicles
        	if(bodyAInfo.type == CollisionObjectType.Vehicle && bodyBInfo.type == CollisionObjectType.Vehicle)
        	{
        		vehicleCollideIsDetected(bodyAInfo, bodyBInfo);
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

	
	private void vehicleCollideIsDetected(CollisionInfo bodyA, CollisionInfo bodyB)
	{
		Gdx.app.debug("Collision Helper", "Vehicle collide");
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
