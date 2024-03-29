package com.ladinc.core.screen.gamemodes;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.core.CarPartA;
import com.ladinc.core.ai.SimpleAi;
import com.ladinc.core.assets.Art;
import com.ladinc.core.assets.CarsHelper;
import com.ladinc.core.controllers.controls.IControls;
import com.ladinc.core.controllers.controls.TouchPadControls;
import com.ladinc.core.objects.StartingPosition;
import com.ladinc.core.player.PlayerInfo;
import com.ladinc.core.screen.GameScreen;
import com.ladinc.core.screen.MainMenuScreen;
import com.ladinc.core.screen.gamemodes.GameModeMetaInfo.GameMode;
import com.ladinc.core.screen.menus.GameModeSelectScreen;
import com.ladinc.core.utilities.Enums.Team;
import com.ladinc.core.ux.DescriptionScreen;
import com.ladinc.core.ux.DescriptionScreenInfo;
import com.ladinc.core.ux.MenuOptions.Options;
import com.ladinc.core.ux.menus.endgame.EndGameOverlay;
import com.ladinc.core.vehicles.Car;
import com.ladinc.core.vehicles.Vehicle;

public abstract class GenericScreen extends GameScreen implements Screen {
	private final List<Vehicle> vehicles;
	private GenericLayout layout;
	
	public ContactListener colHelper;
	
	public Sprite backgroundSprite;
	public Sprite touchOverlaySprite;
	public Sprite finishMessage;
	
	protected float gameOverCoolOffTimer = 0f;
	protected boolean proccessingGameOver = false;
	
	protected DescriptionScreen descriptionScreen = null;
	protected boolean showDescriptionScreen = false;
	
	protected boolean showEndGameOverlay = false;
	
	protected float menuMovementCoolDown = 0.3f;
	
	protected GameMode gameMode;
	
	protected EndGameOverlay endGameOverlay;
	
	protected Team teamWhoWon;
	
	protected boolean touchScreenCurrentlyPressed = false;
	protected float touchedX;
	protected float touchedY;
	
	protected boolean showTouchScreenOverlayIfApplicaple = true;
	
	public GenericScreen(CarPartA game) {
		this.game = game;
		spriteBatch = new SpriteBatch();
		vehicles = new ArrayList<Vehicle>();
		
		this.screenWidth = 1920;
		this.screenHeight = 1080;
		
		this.worldWidth = this.screenWidth / PIXELS_PER_METER;
		this.worldHeight = this.screenHeight / PIXELS_PER_METER;
		
		this.center = new Vector2(worldWidth / 2, worldHeight / 2);
		
		this.debugRenderer = new Box2DDebugRenderer();
		
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, this.screenWidth, this.screenHeight);
		
		this.touchOverlaySprite = Art.getTouchOverlay();
		this.finishMessage = Art.getSprite(Art.FINISHED_OVERLAY);
		this.finishMessage.setPosition(0.0f, 0.0f);
	}
	
	protected boolean allowVehicleControl = true;
	protected boolean allowWorldPhyics = true;
	
	private boolean previousSkipDescriptionValue;
	
	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0, 0f, 0f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		calculateMovements(delta);
		
		// tell the camera to update its matrices.
		camera.update();
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		
		if (this.backgroundSprite != null)
		{
			this.backgroundSprite.draw(spriteBatch);
		}
		
		preCarRender(delta);
		
		for (Vehicle v : this.vehicles)
		{
			if (v.player != null && !v.getDestroyedStatus())
			{
				v.player.drawIndentifier(spriteBatch, PIXELS_PER_METER, v.body);
			}
		}
		
		for (Vehicle v : this.vehicles)
		{
			v.updateSprite(spriteBatch, PIXELS_PER_METER);
		}
		
		renderUpdates(delta);
		
		if (showDescriptionScreen && descriptionScreen != null)
		{
			descriptionScreen.drawDescriptionScreen(spriteBatch, delta);
			if (descriptionScreen.allowSkip && !previousSkipDescriptionValue)
			{
				this.game.controllerManager.resetActiveStateOfControllers();
			}
			
			if (this.game.controllerManager.checkForActiveControllers()
					&& descriptionScreen.allowSkip)
			{
				skipDescriptionScreen();
			}
			
			previousSkipDescriptionValue = this.descriptionScreen.allowSkip;
		}
		
		if(this.showEndGameOverlay && endGameOverlay != null)
		{
			if(menuMovementCoolDown <= 0)
			{
				checkForInputs();
			}
			else
			{
				this.menuMovementCoolDown = this.menuMovementCoolDown - delta;
			}
			endGameOverlay.displayEndGameOverlay(spriteBatch, delta);
		}
		
		if (this.game.controllerManager.hasTouchControls && showTouchScreenOverlayIfApplicaple)
		{
			this.touchOverlaySprite.draw(spriteBatch);
		}
		spriteBatch.end();
		// debugRenderer.render(world,
		// camera.combined.scale(PIXELS_PER_METER,PIXELS_PER_METER,
		// PIXELS_PER_METER));
	}
	
	public void enableDescriptionScreen()
	{
		// This is so we can detect a button press to skip the description
		// screen
		this.game.controllerManager.resetActiveStateOfControllers();
		previousSkipDescriptionValue = this.descriptionScreen.allowSkip;
		showDescriptionScreen = true;
		allowVehicleControl = false;
		allowWorldPhyics = false;
	}
	
	public void skipDescriptionScreen()
	{
		showDescriptionScreen = false;
		allowVehicleControl = true;
		allowWorldPhyics = true;
	}
	
	public StartingPosition aiMove = new StartingPosition(new Vector2(0, 0), 0);
	
	public void calculateAiMovements(float delta)
	{
		
	}
	
	public void calculateMovements(float delta)
	{
		if (allowVehicleControl)
		{
			
			calculateAiMovements(delta);
			
			for (Vehicle v : this.vehicles)
			{
				v.controlVehicle();
			}
		}
		
		customRender(delta);
		
		if (allowWorldPhyics)
		{
			world.step(Gdx.app.getGraphics().getDeltaTime(), 6, 6);
			// world.step(1/60f, 6, 6);
			world.clearForces();
		}
	}
	
	public abstract void preCarRender(float delta);
	
	public abstract void customRender(float delta);
	
	@Override
	public void resize(int width, int height)
	{
		// TODO Auto-generated method stub
	}
	
	public void resetGame()
	{
		this.layout = (GenericLayout) resetLayout();
		createCarsForPlayers();
		initGame();
	}
	
	@Override
	public void show()
	{
		this.world = new World(new Vector2(0.0f, 0.0f), true);
		
		this.touchOverlaySprite.setPosition(0, 0);
		
		DescriptionScreenInfo tempInfo = getScreenInfo();
		if (tempInfo != null)
		{
			this.descriptionScreen = new DescriptionScreen(new Vector2(
					screenWidth / 2, screenHeight / 2), tempInfo);
			enableDescriptionScreen();
		}
		
		resetGame();
	}
	
	protected void incrementGameCount()
	{
		this.game.gameNumber++;
		
		if(this.teamWhoWon != null)
		{
			if(this.teamWhoWon == Team.Home)
			{
				this.game.homeTeamScore++;
			}
			else if(this.teamWhoWon == Team.Away)
			{
				this.game.awayTeamScore++;
			}
		}
	}
	
	protected boolean processGameOverTimer(float delta)
	{
		
		if (gameOverCoolOffTimer > 0)
		{
			gameOverCoolOffTimer = gameOverCoolOffTimer - delta;
		}
		else
		{
			return true;
		}
		
		return false;
	}
	
	public abstract IGenericLayout resetLayout();
	
	public abstract void initGame();
	
	protected abstract void renderUpdates(float delta);
	
	protected int homePlayerCount = 0;
	protected int awayPlayerCount = 0;
	
	protected void createCarsForPlayers()
	{
		Vehicle tempCar;
		this.vehicles.clear();
		PlayerInfo tempPlayer;
		
		this.homePlayerCount = 0;
		this.awayPlayerCount = 0;
		
		for (int i = 0; i < this.game.controllerManager.getPlayers().size(); i++)
		{
			tempPlayer = this.game.controllerManager.getPlayers().get(i);
			
			int teamId = 0;
			
			if (tempPlayer.team == Team.Home)
			{
				teamId = this.homePlayerCount;
				this.homePlayerCount++;
			}
			else if (tempPlayer.team == Team.Away)
			{
				teamId = this.awayPlayerCount;
				this.awayPlayerCount++;
			}
			
			tempCar = new Car(tempPlayer, this.world,
					this.layout.getPlayerStartPoint(tempPlayer.team, teamId),
					null, null);
			
			this.vehicles.add(tempCar);
			
			if (tempPlayer.controls != null && tempPlayer.controls.isAi())
			{
				SimpleAi ai = (SimpleAi) tempPlayer.controls;
				ai.setVehicle(tempCar);
			}
		}
	}
	
	protected void enableEndGameOverlay()
	{
		showTouchScreenOverlayIfApplicaple = false;
		
		if(!this.showEndGameOverlay)
		{
			this.allowVehicleControl = false;
			this.showEndGameOverlay = true;
			
			this.game.controllerManager.resetActiveStateOfControllers();
			this.game.controllerManager.setMenuInterest(true);
			
			if(this.endGameOverlay == null)
				this.endGameOverlay = new EndGameOverlay(new Vector2(this.screenWidth/2, this.screenHeight/2));
			
			this.endGameOverlay.homeWins = this.game.homeTeamScore;
			this.endGameOverlay.awayWins = this.game.awayTeamScore;
			this.endGameOverlay.totalGames = this.game.gameNumber;
			
		}
	}
	
	protected void handleGameOver()
	{
		if(this.game.showEndGameMenu)
		{
			enableEndGameOverlay();
		}
	}
	
	protected boolean alredyGotJustTouched = false;
	
	private void checkForInputs()
	{
		//lets assume its going to return early and we need to set the cool down
		this.menuMovementCoolDown = 0.3f;
		
		//We dont want the player still touching the screen from the game to be considered an action
		if(!alredyGotJustTouched)
		{
			if(Gdx.input.justTouched())
			{
				touchScreenCurrentlyPressed = true;
				alredyGotJustTouched = true;
			}
		}
		else
		{
			if(Gdx.input.isTouched())
			{
				touchScreenCurrentlyPressed = true;
			}
			else
			{
				alredyGotJustTouched = false;
			}
		}
		touchedX = ((float)Gdx.input.getX()/(float)Gdx.graphics.getWidth())*(float)this.screenWidth;
		touchedY = (float)this.screenHeight - ((float)Gdx.input.getY()/(float)Gdx.graphics.getHeight())*(float)this.screenHeight;
		
		boolean startTouchPressed = (touchedY <=  200f) && touchScreenCurrentlyPressed;
		
		endGameOverlay.getTouched(touchScreenCurrentlyPressed, touchedX, touchedY);
		
		IControls tempCont;
		for (PlayerInfo player : this.game.controllerManager.getPlayers())
		{
			tempCont = player.controls;
			boolean confirmationScreenVisible = false;
			if(confirmationScreenVisible)
			{
//				if (tempCont.getConfirmStatus())
//				{
//					startNewScreen();
//					return;
//				}
//				else if (tempCont.getBackStatus())
//				{
//					confirmationScreenVisible = false;
//					return;
//				}
			}
			else
			{
				
				if(tempCont instanceof TouchPadControls)
				{
					if(endGameOverlay.optionPressed)
					{
						handleAction();
						return;
					}
				}
				else
				{
				
					int moveX = tempCont.getMenuXDireciton();
					int moveY = tempCont.getMenuYDireciton();
					
					if(tempCont.getStartStatus() || tempCont.getConfirmStatus() || endGameOverlay.optionPressed)
					{
						handleAction();
						return;
					}
					else if(moveY != 0)
					{
						endGameOverlay.handleMovement(moveY);
						return;
					}
				}
			}
			

		}
		
		//it didnt return early, so lets remove the cool down
		this.menuMovementCoolDown = 0.0f;
	}
	
	private void handleAction()
	{
		Options opt = endGameOverlay.optionsList.get(endGameOverlay.selectedIndex).option;
		
		switch(opt)
		{
			case newGame:
				this.game.setScreen(new GameModeSelectScreen(game));
				break;
			case restart:
				this.game.setGameMode(gameMode);
				break;
			case quit:
				this.game.setScreen(new MainMenuScreen(game));
				break;
		}
		
		dispose();
	}
	
	@Override
	public void hide()
	{
		// TODO Auto-generated method stub
	}
	
	@Override
	public void pause()
	{
		// TODO Auto-generated method stub
	}
	
	@Override
	public void resume()
	{
		// TODO Auto-generated method stub
	}
	
	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub
	}
	
	public GenericLayout getLayout()
	{
		return layout;
	}
	
	public List<Vehicle> getVehicles()
	{
		return this.vehicles;
	}
	
	public List<Vehicle> getHomeVehicles()
	{
		List<Vehicle> homeTeamVehicleList = new ArrayList<Vehicle>();
		for (Vehicle vehicle : this.vehicles)
		{
			if (vehicle.player.team == Team.Home)
			{
				homeTeamVehicleList.add(vehicle);
			}
		}
		return homeTeamVehicleList;
	}
	
	public List<Vehicle> getAwayVehicles()
	{
		List<Vehicle> awayTeamVehicleList = new ArrayList<Vehicle>();
		for (Vehicle vehicle : this.vehicles)
		{
			if (vehicle.player.team == Team.Away)
			{
				awayTeamVehicleList.add(vehicle);
			}
		}
		return awayTeamVehicleList;
	}
	
	public void resetCars()
	{
		for (Vehicle v : this.vehicles)
		{
			v.destroyVehicle();
		}
		
		createCarsForPlayers();
	}
	
	public void assignTeamSpritesToCars()
	{
		for (Vehicle v : getVehicles())
		{
			if (v.player != null)
			{
				v.sprite = CarsHelper.getTeamCar(v.player.team);
			}
		}
	}
	
	public abstract DescriptionScreenInfo getScreenInfo();
}