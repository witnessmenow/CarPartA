package com.ladinc.core.screen.gamemodes.teamselect;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.ladinc.core.CarPartA;
import com.ladinc.core.assets.Art;
import com.ladinc.core.assets.CarsHelper;
import com.ladinc.core.controllers.controls.IControls;
import com.ladinc.core.player.PlayerInfo;
import com.ladinc.core.screen.gamemodes.GenericLayout;
import com.ladinc.core.screen.gamemodes.GenericScreen;
import com.ladinc.core.screen.gamemodes.soccer.SoccerScreen;
import com.ladinc.core.utilities.Enums.Team;
import com.ladinc.core.vehicles.Car;
import com.ladinc.core.vehicles.Vehicle;

public class TeamSelectScreen extends GenericScreen
{
	
	private List<PlayerInfo> assignedPlayers;
	
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
			
		for( int i = 0; i < 8; i++)
		{
			
			tempCar = new Car(	null, 
								world, 
								this.teamSelectLayout.teamSelectArea.getStartPosition(i), 
								CarsHelper.getDefaultCar(),
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
	public void initGame() 
	{
		this.game.controllerManager.resetActiveStateOfControllers();
		
		this.backgroundSprite = this.teamSelectLayout.getTeamAreaSprite();
		this.backgroundSprite.setPosition(0.0f, 0.0f);
		
	}

	@Override
	protected void renderUpdates(float delta) {

		this.teamSelectLayout.teamSelectArea.displayNumbersInTeam(new BitmapFont(), spriteBatch);
		
	}

	private PlayerInfo tempPlayer = null;
	
	private boolean menuMode = false;
	
	private void setMenuMode(boolean set)
	{
		if(set)
		{
			menuMode = true;
			this.allowVehicleControl = false;
			this.allowWorldPhyics = false;
		}
		else
		{
			menuMode = false;
			this.allowVehicleControl = true;
			this.allowWorldPhyics = true;
		}
		
		this.game.controllerManager.setMenuInterest(menuMode);
	}
	
	@Override
	public void customRender(float delta) 
	{
		tempPlayer = this.game.controllerManager.checkForNewPlayer();
		
		if(tempPlayer != null)
		{
			assignPlayerToCar(tempPlayer);	
		}
		
		this.teamSelectLayout.teamSelectArea.checkTeams(getVehicles());
		
		tempPlayer = this.game.controllerManager.checkPlayersForStartPress();
		if(tempPlayer != null)
		{
			setMenuMode(!menuMode);
		}
		
		if(menuMode)
		{
			for (PlayerInfo player : this.game.controllerManager.players)
			{
				if(player.controls.getConfirmStatus())
				{
					handleStartNextScreen();
				}
				else if (player.controls.getBackStatus())
				{
					handleQuitToLastScreen();
				}
			}
		}
		
	}
	
	private void handleStartNextScreen()
	{
		//Remove players who are not active, assign active ones to right team
		for (Vehicle v : this.getVehicles())
		{
			if(v.player != null)
			{
				
				if(this.teamSelectLayout.teamSelectArea.checkVehicleInHomeArea(v))
				{
					v.player.team = Team.Home;
				}
				else if (this.teamSelectLayout.teamSelectArea.checkVehicleInAwayArea(v))
				{
					v.player.team = Team.Away;
				}
				else
				{
					//player is not in either area, remove from active player list.
					this.game.controllerManager.resetStateOfPlayer(v.player);
				}
			}
		}
		
		game.setScreen(new SoccerScreen(game));
		dispose();
	}
	
	private void handleQuitToLastScreen()
	{
		setMenuMode(false);
	}
	
	
	private void assignPlayerToCar(PlayerInfo newPlayer)
	{
		for(Vehicle v : this.getVehicles())
		{
			if(v.player == null)
			{
				v.setPlayerInfo(newPlayer);
				return;
			}
		}
		
	}

	@Override
	public void preCarRender(float delta) {
		// TODO Auto-generated method stub
		
	}

}
