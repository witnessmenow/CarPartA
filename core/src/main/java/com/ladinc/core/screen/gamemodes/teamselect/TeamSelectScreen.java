package com.ladinc.core.screen.gamemodes.teamselect;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.ladinc.core.CarPartA;
import com.ladinc.core.ai.SimpleAi;
import com.ladinc.core.assets.Art;
import com.ladinc.core.assets.CarsHelper;
import com.ladinc.core.assets.Font;
import com.ladinc.core.controllers.controls.IControls;
import com.ladinc.core.player.PlayerInfo;
import com.ladinc.core.screen.gamemodes.GenericLayout;
import com.ladinc.core.screen.gamemodes.GenericScreen;
import com.ladinc.core.screen.gamemodes.hill.HillScreen;
import com.ladinc.core.screen.gamemodes.king.KingScreen;
import com.ladinc.core.screen.gamemodes.nobrakes.NoBrakesScreen;
import com.ladinc.core.utilities.Enums.Team;
import com.ladinc.core.ux.DescriptionScreenInfo;
import com.ladinc.core.vehicles.Car;
import com.ladinc.core.vehicles.Vehicle;

public class TeamSelectScreen extends GenericScreen {
	
	private Sprite confirmSprite;
	private Sprite controllsSprite;
	
	public int aIHomeTeam = 0;
	public int aIAwayTeam = 0;
	
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
		
		for (int i = 0; i < MAX_PLAYERS; i++)
		{
			
			tempCar = new Car(null, world,
					this.teamSelectLayout.teamSelectArea.getStartPosition(i),
					CarsHelper.getDefaultCar(), null);
			
			getVehicles().add(tempCar);
		}
	}
	
	@Override
	public GenericLayout resetLayout()
	{
		this.teamSelectLayout = new TeamSelectLayout(world, worldWidth,
				worldHeight, center, 0);
		return this.teamSelectLayout;
	}
	
	@Override
	public void initGame()
	{
		font = Font.fontTable.get(Font.CONST_50);
		
		this.game.controllerManager.resetControllers();
		
		this.backgroundSprite = this.teamSelectLayout.getTeamAreaSprite();
		this.backgroundSprite.setPosition(0.0f, 0.0f);
		
		if (this.game.controllerManager.hasTouchControls)
		{
			this.controllsSprite = Art.getSprite(Art.INSTRUCTIONS_TOUCH);
			this.confirmSprite = Art.getSprite(Art.START_GAME_CONFIRM_TOUCH);
		}
		else
		{
			this.controllsSprite = Art.getSprite(Art.INSTRUCTIONS_CONTROLLER);
			this.confirmSprite = Art.getSprite(Art.START_GAME_CONFIRM);
		}
		
		this.controllsSprite.setPosition(0.0f, 0.0f);
		this.confirmSprite.setPosition(0.0f, 0.0f);
	}
	
	private BitmapFont font;
	
	@Override
	protected void renderUpdates(float delta)
	{
		
		this.controllsSprite.draw(spriteBatch);
		
		this.teamSelectLayout.teamSelectArea.displayNumbersInTeam(font,
				spriteBatch, this.aIHomeTeam, this.aIAwayTeam);
		
		if (menuMode)
		{
			this.confirmSprite.draw(spriteBatch);
		}
		
	}
	
	private PlayerInfo tempPlayer = null;
	
	private boolean menuMode = false;
	
	private void setMenuMode(boolean set)
	{
		if (set)
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
		
		if (tempPlayer != null)
		{
			assignPlayerToCar(tempPlayer);
		}
		
		aiCreationLoop(delta);
		
		this.teamSelectLayout.teamSelectArea.checkTeams(getVehicles());
		
		tempPlayer = this.game.controllerManager.checkPlayersForStartPress();
		if (tempPlayer != null)
		{
			setMenuMode(!menuMode);
		}
		
		if (menuMode)
		{
			for (IControls cont : this.game.controllerManager.allControls)
			{
				if (cont.getConfirmStatus())
				{
					handleStartNextScreen();
				}
				else if (cont.getBackStatus())
				{
					handleQuitToLastScreen();
				}
			}
		}
		
	}
	
	private void handleStartNextScreen()
	{
		// Remove players who are not active, assign active ones to right team
		
		for (Vehicle v : this.getVehicles())
		{
			if (v.player != null)
			{
				
				if (this.teamSelectLayout.teamSelectArea
						.checkVehicleInHomeArea(v))
				{
					v.player.team = Team.Home;
				}
				else if (this.teamSelectLayout.teamSelectArea
						.checkVehicleInAwayArea(v))
				{
					v.player.team = Team.Away;
				}
				else
				{
					// player is not in either area, remove from active player
					// list.
					this.game.controllerManager.resetStateOfPlayer(v.player);
				}
			}
		}
		
		//game.setScreen(new NoBrakesScreen(game));
		//game.setScreen(new HillScreen(game));
		 game.setScreen(new KingScreen(game));
		// game.setScreen(new MowerScreen(game));
		// game.setScreen(new PainterScreen(game));
		// game.setScreen(new PongScreen(game));
		// game.setScreen(new SoccerScreen(game));
		// game.setScreen(new CarPoolScreen(game));
		// game.setScreen(new MazeScreen(game));
		dispose();
	}
	
	private float aiCoolDown = 0;
	
	private void aiCreationLoop(float delta)
	{
		if (aiCoolDown > 0)
		{
			aiCoolDown -= delta;
		}
		
		for (IControls cont : this.game.controllerManager.allControls)
		{
			if (cont.getExtraButton1Status())
			{
				// aiCoolDown = 0.2f;
				if (this.aIHomeTeam < 4)
				{
					createAIPlayer(Team.Home);
					this.aIHomeTeam++;
				}
			}
			else if (cont.getExtraButton2Status())
			{
				// aiCoolDown = 0.2f;
				if (this.aIAwayTeam < 4)
				{
					createAIPlayer(Team.Away);
					this.aIAwayTeam++;
				}
			}
		}
		
	}
	
	private void createAIPlayer(Team team)
	{
		
		SimpleAi ai = new SimpleAi();
		ai.team = team;
		this.game.controllerManager.addAi(ai, team);
		
	}
	
	private void handleQuitToLastScreen()
	{
		setMenuMode(false);
	}
	
	private void assignPlayerToCar(PlayerInfo newPlayer)
	{
		for (Vehicle v : this.getVehicles())
		{
			if (v.player == null)
			{
				v.setPlayerInfo(newPlayer);
				return;
			}
		}
		
	}
	
	@Override
	public void preCarRender(float delta)
	{
		if (this.teamSelectLayout.teamSelectArea.carsInHomeArea
				+ this.teamSelectLayout.teamSelectArea.carsInAwayArea == 0)
		{
			this.teamSelectLayout.teamSelectArea.displayMessageInSelectArea(
					font, spriteBatch, "Drive Into An Area To Join A Team!");
		}
		else
		{
			this.teamSelectLayout.teamSelectArea
					.displayMessageInSelectArea(font, spriteBatch,
							"Press Start When All Players Are Ready");
		}
		
	}

	@Override
	public DescriptionScreenInfo generateScreenInfo() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
