package com.ladinc.core.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.core.assets.Art;
import com.ladinc.core.collision.CollisionInfo;
import com.ladinc.core.collision.CollisionInfo.CollisionObjectType;
import com.ladinc.core.utilities.Enums.Team;

public class FloorTileSensor 
{
	public static final String HOME_FLOOR_TILE = "HOME_FLOOR_TILE";
	public static final String AWAY_FLOOR_TILE = "AWAY_FLOOR_TILE";
	
	public static Sprite getSprite(Team team)
	{
		if(team ==Team.Home)
		{
			if(!Art.spriteTable.containsKey(HOME_FLOOR_TILE))
			{
				Art.spriteTable.put(HOME_FLOOR_TILE, new Sprite(Art.textureTable.get(Art.PAINT_TILES), 0,0, 99,99));
			}
			
			return Art.spriteTable.get(HOME_FLOOR_TILE);
		}
		else
		{
			if(!Art.spriteTable.containsKey(AWAY_FLOOR_TILE))
			{
				Art.spriteTable.put(AWAY_FLOOR_TILE, new Sprite(Art.textureTable.get(Art.PAINT_TILES), 99,0, 99,99));
			}
			
			return Art.spriteTable.get(AWAY_FLOOR_TILE);
		}
		
	}
	
	public float width, height;
	public Body body;
	
	public boolean assigned = false;
	private Team team;
	
	public void setTeam(Team t)
	{
		assigned = true;
		this.team = t;
	}
	
	public Team getTeam()
	{
		return this.team;
	}

	public FloorTileSensor(World world, float width, float height, Vector2 position)
	{
		super();
		this.width = width;
		this.height = height;
	    
		//initialize body 
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(position);
		bodyDef.angle = 0;
		bodyDef.fixedRotation = true;
		this.body = world.createBody(bodyDef);
	    
	    //initialize shape
		FixtureDef fixtureDef = new FixtureDef();
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(this.width / 2, this.height / 2);
		fixtureDef.shape=boxShape;
		fixtureDef.restitution=0.4f; //positively bouncy!
		fixtureDef.isSensor = true;
	    this.body.createFixture(fixtureDef);
	    
	    this.body.setUserData(new CollisionInfo("FloorSensor", CollisionObjectType.FloorSensor, this));
	    
	    boxShape.dispose();
	}
	
	public void updateSprite(Sprite sprite, SpriteBatch spriteBatch, int PIXELS_PER_METER)
	{
		Art.updateSprite(sprite, spriteBatch, PIXELS_PER_METER, this.body);
	}

}
