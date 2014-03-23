package com.ladinc.core.screen.gamemodes.pong;

import com.ladinc.core.CarPartA;
import com.ladinc.core.objects.balls.PongBall;
import com.ladinc.core.screen.gamemodes.GenericScreen;
import com.ladinc.core.screen.gamemodes.IGenericLayout;
import com.ladinc.core.vehicles.Vehicle;

public class PongScreen extends GenericScreen{

	private PongLayout pongLayout;
	private PongBall pongBall;
	
	private PongCollisionHelper colHelper;
	
	public PongScreen(CarPartA game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void preCarRender(float delta) {
		// TODO Auto-generated method stub
		
	}

	private boolean pongBallMoving = false;
	
	@Override
	public void customRender(float delta) 
	{
		if(!pongBallMoving)
		{
			this.pongBallMoving = true;
			this.pongBall.startBall();
		}
		
	}

	@Override
	public IGenericLayout resetLayout() 
	{
		pongLayout =  new PongLayout(world, worldWidth, worldHeight, center, 0);
		return pongLayout;
	}

	@Override
	public void initGame() 
	{
		assignTeamSpritesToCars();
		
		colHelper =  new PongCollisionHelper(center);
		
		world.setContactListener(colHelper);
		
		pongBall = new PongBall(world, this.center.x, this.center.y, null);
		
		for(Vehicle v : getVehicles())
		{
			v.disableSteering = true;
		}
		
	}

	@Override
	protected void renderUpdates(float delta) {
		// TODO Auto-generated method stub
		
	}

}
