package com.ladinc.core.vehicles;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.ladinc.core.assets.Art;

public class Wheel {	
	/**
	 * Box2d works best with small values. If you use pixels directly you will
	 * get weird results -- speeds and accelerations not feeling quite right.
	 * Common practice is to use a constant to convert pixels to and from
	 * "meters".
	 */
	//public static final float PIXELS_PER_METER = 60.0f;

	public Vehicle vehcile ;//vehicle this wheel belongs to	
	private float width; // width in meters
	private float length; // length in meters
	public boolean revolving; // does this wheel revolve when steering?
	public boolean powered; // is this wheel powered?
	public Body body;
	
	private float posX, posY;

	public Sprite sprite;

	public Wheel(World world, Vehicle vehcile, float posX, float posY, float width, float length,
			boolean revolving, boolean powered, Sprite wheelSprite) {
		super();
		this.vehcile = vehcile;
		this.width = width;
		this.length = length;
		this.revolving = revolving;
		this.powered = powered;

		this.sprite = wheelSprite;
		
		this.posX = posX;
		this.posY = posY;

		createWheelBody(world);
	}
	
	public void createWheelBody(World world)
	{
		//init body 
				BodyDef bodyDef = new BodyDef();
				bodyDef.type = BodyDef.BodyType.DynamicBody;
				bodyDef.position.set(vehcile.body.getWorldPoint(new Vector2(posX, posY)));
				bodyDef.angle = vehcile.body.getAngle();
				this.body = world.createBody(bodyDef);

				//init shape
				FixtureDef fixtureDef = new FixtureDef();
				fixtureDef.density = 1.0f;
				fixtureDef.isSensor=true; //wheel does not participate in collision calculations: resulting complications are unnecessary
				PolygonShape wheelShape = new PolygonShape();
				wheelShape.setAsBox(this.width/2, this.length/2);
				fixtureDef.shape = wheelShape;
				this.body.createFixture(fixtureDef);
				wheelShape.dispose();

			    //create joint to connect wheel to body
			    if(this.revolving){
			    	RevoluteJointDef jointdef=new RevoluteJointDef();
			        jointdef.initialize(this.vehcile.body, this.body, this.body.getWorldCenter());
			        jointdef.enableMotor=false; //we'll be controlling the wheel's angle manually
				    world.createJoint(jointdef);
			    }else{
//			    	PrismaticJointDef jointdef=new PrismaticJointDef();
//			        jointdef.initialize(this.vehcile.body, this.body, this.body.getWorldCenter(), new Vector2(1, 0));
//			        jointdef.enableMotor=false;
//			        jointdef.enableLimit=true;
//			        jointdef.lowerTranslation=jointdef.upperTranslation=0;
//				    world.createJoint(jointdef);
			    	
			    	RevoluteJointDef jointdef=new RevoluteJointDef();
			        jointdef.initialize(this.vehcile.body, this.body, this.body.getWorldCenter());
			        jointdef.enableMotor=false; //we'll be controlling the wheel's angle manually
				    world.createJoint(jointdef);
			    }
	}

	public void setAngle (float angle){
	    /*
	    angle - wheel angle relative to car, in degrees
	    */
		this.body.setTransform(body.getPosition(), this.vehcile.body.getAngle() + (float) Math.toRadians(angle));
	};

	public Vector2 getLocalVelocity () {
	    /*returns get velocity vector relative to car
	    */
	    return this.vehcile.body.getLocalVector(this.vehcile.body.getLinearVelocityFromLocalPoint(this.body.getPosition()));
	};

	public Vector2 getDirectionVector () {
	    /*
	    returns a world unit vector pointing in the direction this wheel is moving
	    */
		Vector2 directionVector;
		if (this.getLocalVelocity().y > 0)
			directionVector = new Vector2(0,1);
		else
			directionVector = new Vector2(0,-1);

		return directionVector.rotate((float) Math.toDegrees(this.body.getAngle()));	    
	};


	public Vector2 getKillVelocityVector (){
	    /*
	    substracts sideways velocity from this wheel's velocity vector and returns the remaining front-facing velocity vector
	    */
	    Vector2 velocity = this.body.getLinearVelocity();
	    Vector2 sidewaysAxis=this.getDirectionVector();
	    float dotprod = velocity.dot(sidewaysAxis);
	    dotprod = dotprod - (dotprod/100);
	    return new Vector2(sidewaysAxis.x*dotprod, sidewaysAxis.y*dotprod);
	};

	public Vector2 getDampedVelocityVector (){
	    /*
	    substracts sideways velocity from this wheel's velocity vector and returns the remaining front-facing velocity vector
	    */
	    Vector2 velocity = this.body.getLinearVelocity();
	    Vector2 sidewaysAxis=this.getDirectionVector();
	    //sidewaysAxis.x = sidewaysAxis.x/2;
	    //sidewaysAxis.y = sidewaysAxis.y*1.2f;
	    
	    float dotprod = velocity.dot(sidewaysAxis);
	    
	    dotprod = dotprod - (dotprod/100);
	    return new Vector2(sidewaysAxis.x*dotprod/2, sidewaysAxis.y*dotprod/2);
	};

	public void killSidewaysVelocity (){
	    /*
	    removes all sideways velocity from this wheels velocity
	    */
	    this.body.setLinearVelocity(this.getKillVelocityVector());
	};

	public void dampSidewaysVelocity()
	{
		this.body.setLinearVelocity(this.getDampedVelocityVector());
	}

	public void updateSprite(SpriteBatch spriteBatch, int PIXELS_PER_METER)
	{
		if(this.sprite == null)
		{
			if(!Art.spriteTable.containsKey(Art.WHEELS))
			{
				int w = ((2) * (int) this.width * PIXELS_PER_METER)/3;
				int l = (int) this.length * PIXELS_PER_METER;
				Art.spriteTable.put(Art.WHEELS, new Sprite(Art.textureTable.get(Art.WHEELS), w, l ));
			}
			
			this.sprite =  Art.spriteTable.get(Art.WHEELS);
		}
		
		Art.updateSprite(this.sprite, spriteBatch, PIXELS_PER_METER, this.body);
	}
}
