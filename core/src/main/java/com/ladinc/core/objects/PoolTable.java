package com.ladinc.core.objects;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.core.assets.Art;
import com.ladinc.core.collision.CollisionInfo;
import com.ladinc.core.collision.CollisionInfo.CollisionObjectType;
import com.ladinc.core.objects.balls.Ball;
import com.ladinc.core.objects.balls.PoolBall;
import com.ladinc.core.objects.balls.PoolBall.ColourBall;
import com.ladinc.core.utilities.BodyEditorLoader;
import com.ladinc.core.utilities.Enums.Team;

public class PoolTable 
{
	public Sprite sprite;
	public Body bumpers;
	public Body pockets;
	
	private float worldWidth;
	private float worldHeight;
	
	private Vector2 center;
	
	private static final float PLAYER_GAP_Y = 8f;
	
	private static final float BALL_SIZE  = 2.5f;
	private static final float BALL_DENSITY  = 3f;
	private static final float BALL_LIN_DAMP  = 0.2f;
	
	public List<PoolBall> activeBalls =  new ArrayList<PoolBall>();
	
	public PoolTable(World world, float worldHeight, float worldWidth, Vector2 center)
	{
		this.center = center;
		
		this.worldHeight = worldHeight;
		this.worldWidth = worldWidth;
		
		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("CarPool/carpool.json"));
		
		sprite = getPoolTableSprite();
		sprite.setPosition(0, 0);
		
		BodyDef bd = new BodyDef();
	    bd.position.set(0, 0);
		
	    FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 1.0f;
		
		this.bumpers = world.createBody(bd);
		
		loader.attachFixture(bumpers, "poolWall", fixtureDef, 192);
		
		//Init pockets
		BodyDef bd2 = new BodyDef();
		bd2.position.set(0, 0);
		bd2.type = BodyType.DynamicBody;

	    FixtureDef fixtureDef2 = new FixtureDef();
	    fixtureDef2.density = 1.0f;
	    fixtureDef2.isSensor=true;
	    
	    this.pockets = world.createBody(bd2);
	    
	    loader.attachFixture(pockets, "pockets", fixtureDef2, 192);
	    
	    this.pockets.setUserData(new CollisionInfo("", CollisionObjectType.Pocket));
	    this.bumpers.setUserData(new CollisionInfo("", CollisionObjectType.Wall));
	}
	
	public Sprite getPoolTableSprite()
	{
		if(!Art.spriteTable.containsKey(Art.POOL_TABLE))
		{
			Art.spriteTable.put(Art.POOL_TABLE,new Sprite(Art.textureTable.get(Art.POOL_TABLE)));
		}
		
		return Art.spriteTable.get(Art.POOL_TABLE);
	}
	
	public StartingPosition getStartPosition(Team team, int carTeamNumber)
	{
		float carPosX = this.worldWidth/4;
		float carPosY;
		
		int direction;
		
		if(team == Team.Away)
			direction = -1;
		else
			direction = 1;
		
		
		
		if(carTeamNumber%2 == 0)
		{
			carPosY = this.worldHeight/2 - (direction)*(float)(((carTeamNumber) + 1)*PLAYER_GAP_Y);
		}
		else
		{
			carPosY = this.worldHeight/2 + (direction)*(float)(((carTeamNumber) + 1)*PLAYER_GAP_Y);
		}
		
		return new StartingPosition(new Vector2(carPosX, carPosY), (float) Math.PI/2);
		
	}
	
	public void createPoolBalls(World world)
	{
		activeBalls.add(new PoolBall(world, this.center.x, this.center.y, BALL_SIZE, BALL_DENSITY, BALL_LIN_DAMP, ColourBall.Black));
	}
	
	public void updateBallSprites(SpriteBatch spriteBatch, int PIXELS_PER_METER)
	{
		for(Ball b : this.activeBalls)
		{
			b.updateSprite(spriteBatch, PIXELS_PER_METER);
		}
	}
	
	public void updateBalls()
	{
		for(Ball b : this.activeBalls)
		{
			b.update();
		}
	}

}
