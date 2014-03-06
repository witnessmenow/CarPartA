package com.ladinc.core.objects;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.core.vehicles.Vehicle;

public class TeamSelectArea 
{
	private Vector2 center;
	
	private float worldWidth;
	private float worldHeight;
	
	private float playerGapX;
	private float playerGapY;
	
	public int carsInHomeArea = 0;
	public int carsInAwayArea = 0;
	
	public float homeTextX;
	public float awayTextX;
	public float commonTextY;
	
	public TeamSelectArea(World world, float worldWidth, float worldHeight, Vector2 center, float gapFromOuterEdge, float playerGapX, float playerGapY)
	{		
		this.center = center;
		
		this.worldHeight = worldHeight;
		this.worldWidth = worldWidth;
		
		this.playerGapX = playerGapX;
		this.playerGapY = playerGapY;
		
		createOuterWalls(world, worldWidth, worldHeight, center, gapFromOuterEdge);
		
		createInnerWalls(world, worldWidth, worldHeight, center, gapFromOuterEdge);
		
		this.homeTextX = (gapFromOuterEdge + ((worldWidth/4)-gapFromOuterEdge)/2)*10;
		this.awayTextX = (worldWidth*10)-homeTextX;
		
		this.commonTextY = ((worldHeight/2)*10) + 50; 		
	}
	
	private void createOuterWalls(World world, float worldWidth, float worldHeight, Vector2 center, float gapFromOuterEdge)
	{
		
	    //outer walls
	    //bottomWall 
	    new BoxProp(world, worldWidth, 1, new Vector2 (worldWidth/2, gapFromOuterEdge));
	    
	    //leftWall
	    new BoxProp(world, 1, worldHeight/2, new Vector2 (gapFromOuterEdge, worldHeight/4));
	    
	    //topWall
	    new BoxProp(world,  worldWidth, 1, new Vector2 (worldWidth/2,worldHeight/2));
	    
	    //rightWall
	    new BoxProp(world, 1, worldHeight/2, new Vector2 (worldWidth - gapFromOuterEdge, worldHeight/4));
	    
	}
	
	private void createInnerWalls(World world, float worldWidth, float worldHeight, Vector2 center, float gapFromOuterEdge)
	{
	    
	    //leftInnerWall 
		new BoxProp(world, 1, worldHeight/4, new Vector2 ((worldWidth/4), 3*(worldHeight/8))); 
	    
	    //rightInnerWall 
		new BoxProp(world, 1, worldHeight/4, new Vector2 (3*(worldWidth/4), 3*(worldHeight/8)));

	}
	
	public StartingPosition getStartPosition(int carNumber)
	{
		float carPosX;
		
		if(carNumber%2 == 0)
		{
			carPosX = this.center.x - (float)(((carNumber/2) + 1)*playerGapX);
		}
		else
		{
			carPosX = this.center.x + (float)(((carNumber/2) + 1)*playerGapX);
		}
		
		return new StartingPosition(new Vector2(carPosX, this.center.y - playerGapY),
				0f);
		
	}
	
	// This is not the fanciest way of doing this, a contact listener would be
	// But I can't think of a good reason why it would be better, in fact its more complicated!
	public void checkTeams(List<Vehicle> vehicles)
	{
		carsInHomeArea = 0;
		carsInAwayArea = 0;
		
		for (Vehicle vehicle : vehicles) 
		{
			//Check for Home Area
			if(checkVehicleInHomeArea(vehicle))
			{
				this.carsInHomeArea++;
			}
			else if(checkVehicleInAwayArea(vehicle))
			{
				this.carsInAwayArea++;
			}
		}
	}
	
	public void displayNumbersInTeam(BitmapFont font, SpriteBatch spriteBatch)
	{
		String blueMessage = String.valueOf(this.carsInHomeArea) + " HOME";
		//Set for blue
		font.setColor(0f, 0f, 1f, 1.0f);
        font.draw(spriteBatch, blueMessage, homeTextX - font.getBounds(blueMessage).width/2, commonTextY);
        
      //Set for red
        String redMessage = String.valueOf(this.carsInAwayArea) + " AWAY";
      	font.setColor(1f, 0f, 0f, 1.0f);
        font.draw(spriteBatch, redMessage, awayTextX - font.getBounds(redMessage).width/2, commonTextY);
	}
	
	private boolean checkVehicleInHomeArea(Vehicle vehicle)
	{
		return (vehicle.body.getPosition().x <= worldWidth/4); 
	}
	
	private boolean checkVehicleInAwayArea(Vehicle vehicle)
	{
		return (vehicle.body.getPosition().x >= 3*(worldWidth/4)); 
	}
}
