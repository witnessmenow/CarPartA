package com.ladinc.core.screen.gamemodes.pong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.ladinc.core.collision.CollisionHelper;
import com.ladinc.core.collision.CollisionInfo;
import com.ladinc.core.collision.CollisionInfo.CollisionObjectType;
import com.ladinc.core.objects.balls.PongBall;
import com.ladinc.core.objects.balls.PoolBall;
import com.ladinc.core.vehicles.Vehicle;

public class PongCollisionHelper implements ContactListener
{
	private Vector2 center;
	public PongCollisionHelper(Vector2 center)
	{
		this.center = center;
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
        	
        	if(CollisionHelper.checkIfCollisionIsOfCertainBodies(bodyAInfo, bodyBInfo, CollisionObjectType.Wall, CollisionObjectType.BallSensor))
        	{
        		
        		if(bodyAInfo.type == CollisionObjectType.BallSensor)
        		{
        			PongBall pb = (PongBall)bodyAInfo.object;
        			pb.wallHit();
        		}
        		
        		if(bodyBInfo.type == CollisionObjectType.BallSensor)
        		{
        			PongBall pb = (PongBall)bodyBInfo.object;
        			pb.wallHit();
        		}

        		
        	}
        	else if(CollisionHelper.checkIfCollisionIsOfCertainBodies(bodyAInfo, bodyBInfo, CollisionObjectType.Vehicle, CollisionObjectType.BallSensor))
        	{
        		Vehicle v;
        		PongBall pb;
        		
        		if(bodyAInfo.type == CollisionObjectType.BallSensor)
        		{
        			v = (Vehicle)bodyBInfo.object;
        			pb = (PongBall)bodyAInfo.object;
        		}
        		else
        		{
        			v = (Vehicle)bodyAInfo.object;
        			pb = (PongBall)bodyBInfo.object;
        		}
        		
        		if(pb.body.getWorldCenter().x < this.center.x)
        		{
        			if((pb.body.getWorldCenter().x - pb.ballSize/2) >= (v.body.getWorldCenter().x + v.width/2 -0.5f))
        			{
        				pb.carHit();
        			}
        			else
        			{
        				pb.wallHit();
        			}
        		}
        		else
        		{
        			float ballX = pb.body.getWorldCenter().x + pb.ballSize/2;
        			float carX = v.body.getWorldCenter().x - v.width/2;
        			
        			if((pb.body.getWorldCenter().x + pb.ballSize/2) <= (v.body.getWorldCenter().x - v.width/2 + 0.5))
        			{
        				pb.carHit();
        			}
        			else
        			{
        				pb.wallHit();
        			}
        		}
        	}
        	
        	//Check for collision of two Vehicles
        	if(bodyAInfo.type == CollisionObjectType.Vehicle && bodyBInfo.type == CollisionObjectType.Vehicle)
        	{
        		//vehicleCollideIsDetected(bodyAInfo, bodyBInfo);
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
