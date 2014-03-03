package com.ladinc.core.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.core.utilities.Enums.Direction;
import com.ladinc.core.utilities.Enums.Side;

public class Goal 
{
	BoxProp side1;
    BoxProp side2;
    BoxProp back;
    
    public Body scoringZone;
    
    Direction facing;
    Side side;
    
    float postLength;
    float goalThickness;
    float barLength;
    
    float goalLineX;
	float goalY;
	
	public Goal(World world, float x, float y, Direction facing, float postLength, float goalThickness, float barLength, Side side, float compensationSize)
	{
		this.postLength = postLength;
		this.goalThickness = goalThickness;
		this.barLength = barLength;
		
		int direction = 1;
    	
    	this.facing = facing;
    	this.side = side;
    	
    	
		if(facing == Direction.left)
			direction = -1;
		
		this.back = new BoxProp(world, goalThickness, barLength, new Vector2 (x , y));
    	
    	float postsXCoord = x + (direction * (-(goalThickness/2) + (postLength/2))); 
    	
    	this.side1 = new BoxProp(world, postLength, goalThickness, new Vector2 ( postsXCoord  , y -(barLength/2 +  goalThickness/2)));
    	this.side2 = new BoxProp(world, postLength, goalThickness, new Vector2 ( postsXCoord  , y +(barLength/2 +  goalThickness/2)));
    	
    	this.goalLineX = x + (direction *(postLength - (goalThickness/2)));
    	this.goalY = y;
    	
    	createScoringZone(world, compensationSize);
	}
	
	//Compensation Size is if you want to allow for something like the ball being completely over the line
	//in this case you would set the compensation size to be the width of the ball
	//Set to 0 to not enable it.
	public void createScoringZone(World world, float copensationSize)
	{
		Vector2 zoneCenter = new Vector2();
		
		float ySize = this.side2.body.getPosition().y - this.side1.body.getPosition().y;
		zoneCenter.y = this.side2.body.getPosition().y - ySize/2;
		
		float xSize;
		
		if (side == Side.Home)
		{
			xSize = (this.back.body.getPosition().x - goalThickness/2 + postLength - copensationSize) - (this.back.body.getPosition().x + goalThickness/2);
			zoneCenter.x =(this.back.body.getPosition().x + goalThickness/2) + xSize/2;
		}
		else
		{
			xSize = (this.back.body.getPosition().x - goalThickness/2) - (this.back.body.getPosition().x + goalThickness/2 - postLength + copensationSize);
			zoneCenter.x =(this.back.body.getPosition().x - goalThickness/2) - xSize/2;
		}
		
		ySize -= goalThickness;
		
		//init body 
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(zoneCenter);
		this.scoringZone = world.createBody(bodyDef);

		//init shape
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 1.0f;
		fixtureDef.isSensor=true;
		PolygonShape zoneShape = new PolygonShape();
		zoneShape.setAsBox(xSize/2, ySize/2);
		fixtureDef.shape = zoneShape;
		this.scoringZone.createFixture(fixtureDef);
		
		//this.scoringZone.setUserData(new CollisionInfo("", CollisionObjectType.ScoreZone, side));
		
		zoneShape.dispose();
	}

}
