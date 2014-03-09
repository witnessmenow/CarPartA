package com.ladinc.core.collision;

import com.ladinc.core.utilities.Enums.Side;

public class CollisionInfo 
{
	public String text;
	public CollisionObjectType type;
	public Object object;
	
	public Side side;

	
	public CollisionInfo(String text, CollisionObjectType type)
	{
		this.text = text;		
		this.type = type;
	}
	
	public CollisionInfo(String text, CollisionObjectType type, Side side)
	{
		this.text = text;		
		this.type = type;
		this.side = side;
	}
	
	public CollisionInfo(String text, CollisionObjectType type, Object object)
	{
		this.text = text;		
		this.type = type;
		this.object = object;
	}
	
	public static enum CollisionObjectType{Wall, Vehicle, ScoreZone, Ball};
}
