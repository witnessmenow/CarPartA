package com.ladinc.core.screen.gamemodes.nobrakes;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.ladinc.core.CarPartA;
import com.ladinc.core.ai.SimpleAi;
import com.ladinc.core.player.PlayerInfo;
import com.ladinc.core.screen.gamemodes.GenericScreen;
import com.ladinc.core.screen.gamemodes.IGenericLayout;
import com.ladinc.core.utilities.Enums.Team;
import com.ladinc.core.ux.DescriptionScreenInfo;
import com.ladinc.core.vehicles.Car;
import com.ladinc.core.vehicles.NoBrakesValetCar;
import com.ladinc.core.vehicles.Vehicle;

public class NoBrakesScreen extends GenericScreen
{
	private ArrayList<PlayerInfo> homePlayers;
	private ArrayList<PlayerInfo> awayPlayers;
	
	private ArrayList<Vehicle> parkedVehicles;
	
	private NoBrakesLayout noBrakesLayout;

	public NoBrakesScreen(CarPartA game) 
	{
		super(game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void preCarRender(float delta) 
	{
		// TODO Auto-generated method stub
		
	}

	private float coolDown = 0.0f;
	
	@Override
	public void customRender(float delta) 
	{
		if(coolDown > 0)
		{
			coolDown = coolDown - delta;
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.P))
		{
			if(coolDown <= 0)
			{
				updateParkedCarsList();
				spawnNewCars();
				coolDown = 1f;
			}
		}
		
	}

	@Override
	public IGenericLayout resetLayout() 
	{
		noBrakesLayout = new NoBrakesLayout(world, worldWidth, worldHeight, center, 0);
		return noBrakesLayout;
	}

	@Override
	public void initGame() 
	{
		populateTeamArrays();
		
		this.parkedVehicles = new ArrayList<Vehicle>();
		
	}

	@Override
	protected void renderUpdates(float delta) 
	{
		for (Vehicle v : this.parkedVehicles)
		{
			v.updateSprite(spriteBatch, PIXELS_PER_METER);
		}
		
	}

	@Override
	public DescriptionScreenInfo generateScreenInfo() 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected void createCarsForPlayers()
	{
		//Do nothing! NBV starts with no cars on screen
	}
	
	private void updateParkedCarsList()
	{
		if(parkedVehicles == null)
		{
			parkedVehicles = new ArrayList<Vehicle>();
		}
		
		for(Vehicle v : getVehicles())
		{
			parkedVehicles.add(v);
			v.team = v.player.team;
			v.player = null;
		}
		
		getVehicles().clear();
	}
	
	private void spawnNewCars()
	{		
		spawnTeamsCars(this.homePlayers, Team.Home);
		spawnTeamsCars(this.awayPlayers, Team.Away);
		
		assignTeamSpritesToCars();
	}
	
	private void spawnTeamsCars(ArrayList<PlayerInfo> players, Team team)
	{
		int startPlayer1 = 0;
		int startPlayer2 = 0;
		
		Vehicle tempCar;
		
		if(players != null && players.size() > 0)
		{
			Random r = new Random();
			if(players.size() == 1)
			{
				tempCar = new NoBrakesValetCar(players.get(0), this.world,
						this.noBrakesLayout.getPlayerStartPoint(team, r.nextInt(1)),
						null, null);
				
				getVehicles().add(tempCar);
			}
			else
			{
				startPlayer1 = r.nextInt(players.size());
				do
				{
					startPlayer2  = r.nextInt(players.size());
				}while(startPlayer1 != startPlayer2);
				
				tempCar = new NoBrakesValetCar(players.get(startPlayer1), this.world,
						this.noBrakesLayout.getPlayerStartPoint(team, 0),
						null, null);
				
				getVehicles().add(tempCar);
				
				tempCar = new NoBrakesValetCar(players.get(startPlayer2), this.world,
						this.noBrakesLayout.getPlayerStartPoint(team, 1),
						null, null);
				
				getVehicles().add(tempCar);
			}
		}
	}
	
	private void populateTeamArrays()
	{
		this.homePlayers = new ArrayList<PlayerInfo>();
		this.awayPlayers = new ArrayList<PlayerInfo>();
		
		PlayerInfo tempPlayer;
		
		for (int i = 0; i < this.game.controllerManager.getPlayers().size(); i++)
		{
			tempPlayer = this.game.controllerManager.getPlayers().get(i);
			
			if (tempPlayer.team == Team.Home)
			{
				homePlayers.add(tempPlayer);
			}
			else if (tempPlayer.team == Team.Away)
			{
				awayPlayers.add(tempPlayer);
			}
			
		}
	}

}
