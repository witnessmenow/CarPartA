package com.ladinc.core.screen.gamemodes;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.ladinc.core.collision.CollisionInfo;
import com.ladinc.core.collision.CollisionInfo.CollisionObjectType;

public abstract class AbstractCollisionHelper implements ContactListener {
	public boolean enableChange = true;
	
	protected CollisionInfo getCollisionInfoFromFixture(Fixture fix)
	{
		CollisionInfo colInfo = null;
		
		if (fix != null)
		{
			Body body = fix.getBody();
			
			if (body != null)
			{
				colInfo = (CollisionInfo) body.getUserData();
			}
		}
		
		return colInfo;
	}
	
	protected boolean isTwoVehilesColliding(CollisionInfo bodyAInfo,
			CollisionInfo bodyBInfo)
	{
		return bodyAInfo.type == CollisionObjectType.Vehicle
				&& bodyBInfo.type == CollisionObjectType.Vehicle;
	}
	
	@Override
	public void endContact(Contact contact)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void preSolve(Contact contact, Manifold oldManifold)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse)
	{
		// TODO Auto-generated method stub
		
	}
}