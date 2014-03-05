package com.ladinc.core.screen.gamemodes.teamselect;

import com.ladinc.core.CarPartA;
import com.ladinc.core.player.PlayerInfo;
import com.ladinc.core.screen.gamemodes.GenericLayout;
import com.ladinc.core.screen.gamemodes.GenericScreen;
import com.ladinc.core.vehicles.Car;
import com.ladinc.core.vehicles.Vehicle;

public class TeamSelectScreen extends GenericScreen
{
	
	//Need a better place for this!
	private static final int MAX_PLAYERS = 8;
	
	public TeamSelectScreen(CarPartA game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	private TeamSelectLayout teamSelectLayout;
	
	@Override
	protected void createCarsForPlayers()
	{
		Vehicle tempCar;
		getVehicles().clear();
		PlayerInfo tempPlayer;
		
			
		for( int i = 0; i < 8; i++)
		{
			
			tempCar = new Car(	null, 
								world, 
								this.teamSelectLayout.teamSelectArea.getStartPosition(i), 
								null,
								null);
			
			getVehicles().add(tempCar);
		}
	}

	@Override
	public GenericLayout resetLayout() 
	{
		this.teamSelectLayout = new TeamSelectLayout(world, worldWidth, worldHeight, center, 0);
		return this.teamSelectLayout;
	}

	@Override
	public void initGame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void renderUpdates(float delta) {
		// TODO Auto-generated method stub
		
	}

}
