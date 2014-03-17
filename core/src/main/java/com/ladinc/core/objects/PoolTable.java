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
	public List<PoolBall> pottedBalls =  new ArrayList<PoolBall>();
	
	public int pottedYellowBallsCount = 0;
	public int pottedRedBallsCount  = 0;
	
	private World world;
	
	public PoolTable(World world, float worldHeight, float worldWidth, Vector2 center)
	{
		this.world = world;
		
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
	
	public List<PoolBall> ballsToRemove = new ArrayList<PoolBall>();
	public void checkForPottedBall()
	{
		ballsToRemove.clear();
		
		for(PoolBall pb : this.activeBalls)
		{
			if(!pb.isActive)
			{
				this.pottedBalls.add(pb);
				this.ballsToRemove.add(pb);
				
				pb.destroyBodies(world);
				
				if(pb.colour == ColourBall.Red)
				{
					this.pottedRedBallsCount ++;
				}
				else if(pb.colour == ColourBall.Yellow)
				{
					this.pottedYellowBallsCount ++;
				}
			}
		}
		
		for(PoolBall pb : this.ballsToRemove)
		{
			this.activeBalls.remove(pb);
		}
	}
	
	public Team winningTeam;
	
	public Boolean checkForWin()
	{
		if(this.pottedRedBallsCount >= 7)
		{
			winningTeam = Team.Away;
			return true;
		}
		else if(this.pottedYellowBallsCount >= 7)
		{
			winningTeam = Team.Home;
			return true;
		}
		
		return false;
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
	
	public Vector2 getTriangleStartingPos()
	{
		return this.center;
	}
	
	public void createPoolBalls(World world, Vector2 startingPos)
	{
		float x = startingPos.x;
		float y = startingPos.y;
		float tempY;
		float tempX;
		
		//Adding first row
		activeBalls.add(new PoolBall(world, x, y, BALL_SIZE, BALL_DENSITY, BALL_LIN_DAMP, ColourBall.Yellow));
		
		//Second Row
		tempY = y - BALL_SIZE;
		tempX = x + ((BALL_SIZE*2)*0.9f);
		activeBalls.add(new PoolBall(world, tempX, tempY, BALL_SIZE, BALL_DENSITY, BALL_LIN_DAMP, ColourBall.Yellow));
		activeBalls.add(new PoolBall(world, tempX, tempY+(BALL_SIZE*2), BALL_SIZE, BALL_DENSITY, BALL_LIN_DAMP, ColourBall.Red));
		
		//thirdRow
		tempX = tempX + ((BALL_SIZE*2)*0.9f);
		tempY = y - (BALL_SIZE*2);
		activeBalls.add(new PoolBall(world, tempX, tempY, BALL_SIZE, BALL_DENSITY, BALL_LIN_DAMP, ColourBall.Red));
		activeBalls.add(new PoolBall(world, tempX, tempY+(BALL_SIZE*2), BALL_SIZE, BALL_DENSITY, BALL_LIN_DAMP, ColourBall.Black));
		activeBalls.add(new PoolBall(world, tempX, tempY+((BALL_SIZE*2)*2), BALL_SIZE, BALL_DENSITY, BALL_LIN_DAMP, ColourBall.Yellow));
		
		//4th Row
		tempX = tempX + ((BALL_SIZE*2)*0.9f);
		tempY = y - (BALL_SIZE*3);
		activeBalls.add(new PoolBall(world, tempX, tempY, BALL_SIZE, BALL_DENSITY, BALL_LIN_DAMP, ColourBall.Red));
		activeBalls.add(new PoolBall(world, tempX, tempY+(BALL_SIZE*2), BALL_SIZE, BALL_DENSITY, BALL_LIN_DAMP, ColourBall.Yellow));
		activeBalls.add(new PoolBall(world, tempX, tempY+((BALL_SIZE*2)*2), BALL_SIZE, BALL_DENSITY, BALL_LIN_DAMP, ColourBall.Red));
		activeBalls.add(new PoolBall(world, tempX, tempY+((BALL_SIZE*2)*3), BALL_SIZE, BALL_DENSITY, BALL_LIN_DAMP, ColourBall.Red));
		
		//5th Row
		tempX = tempX + ((BALL_SIZE*2)*0.9f);
		tempY = y - (BALL_SIZE*4);
		activeBalls.add(new PoolBall(world, tempX, tempY, BALL_SIZE, BALL_DENSITY, BALL_LIN_DAMP, ColourBall.Red));
		activeBalls.add(new PoolBall(world, tempX, tempY+(BALL_SIZE*2), BALL_SIZE, BALL_DENSITY, BALL_LIN_DAMP, ColourBall.Yellow));
		activeBalls.add(new PoolBall(world, tempX, tempY+((BALL_SIZE*2)*2), BALL_SIZE, BALL_DENSITY, BALL_LIN_DAMP, ColourBall.Red));
		activeBalls.add(new PoolBall(world, tempX, tempY+((BALL_SIZE*2)*3), BALL_SIZE, BALL_DENSITY, BALL_LIN_DAMP, ColourBall.Yellow));
		activeBalls.add(new PoolBall(world, tempX, tempY+((BALL_SIZE*2)*4), BALL_SIZE, BALL_DENSITY, BALL_LIN_DAMP, ColourBall.Yellow));
		
		
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
