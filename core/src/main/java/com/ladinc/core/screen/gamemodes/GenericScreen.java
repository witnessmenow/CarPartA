package com.ladinc.core.screen.gamemodes;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.core.CarPartA;
import com.ladinc.core.collision.CollisionHelper;
import com.ladinc.core.player.PlayerInfo;
import com.ladinc.core.player.PlayerInfo.Team;
import com.ladinc.core.screen.GameScreen;
import com.ladinc.core.vehicles.Car;
import com.ladinc.core.vehicles.Vehicle;

public abstract class GenericScreen extends GameScreen implements Screen {
	private final List<Vehicle> vehicles;
	private GenericLayout layout;
	
	public CollisionHelper colHelper;
	
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
		
		colHelper = new CollisionHelper();
	}
	
	protected boolean allowVehicleControl = true;
	protected boolean allowWorldPhyics = true;
	
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
		renderUpdates(delta);
		spriteBatch.end();
		debugRenderer.render(world, camera.combined.scale(PIXELS_PER_METER,
				PIXELS_PER_METER, PIXELS_PER_METER));
	}
	
	public void calculateMovements(float delta)
	{
		if(allowVehicleControl)
		{
			for (Vehicle v : this.vehicles)
			{
				v.controlVehicle();
			}
		}
		
		customRender(delta);
		
		if(allowWorldPhyics)
		{
			world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);
			world.clearForces();
		}
	}
	
	public abstract void customRender(float delta);
	
	@Override
	public void resize(int width, int height)
	{
		// TODO Auto-generated method stub
	}
	
	public void resetGame()
	{
		this.layout = (GenericLayout) resetLayout();
		initGame();
		createCarsForPlayers();
	}
	
	@Override
	public void show()
	{
		this.world = new World(new Vector2(0.0f, 0.0f), true);
		world.setContactListener(this.colHelper);
		resetGame();
	}
	
	public abstract IGenericLayout resetLayout();
	
	public abstract void initGame();
	
	protected abstract void renderUpdates(float delta);
	
	private int homePlayerCount = 0;
	private int awayPlayerCount = 0;
	
	protected void createCarsForPlayers()
	{
		Vehicle tempCar;
		this.vehicles.clear();
		PlayerInfo tempPlayer;
		
		this.homePlayerCount = 0;
		this.awayPlayerCount = 0;
		
		for (int i = 0; i < this.game.controllerManager.players.size(); i++)
		{
			tempPlayer = this.game.controllerManager.players.get(i);
			
			int teamId = 0;
			
			if(tempPlayer.team == Team.home)
			{
				teamId = this.homePlayerCount;
				this.homePlayerCount ++;
			}
			else if(tempPlayer.team == Team.away)
			{
				teamId = this.awayPlayerCount;
				this.awayPlayerCount ++;
			}
			
			tempCar = new Car(tempPlayer, this.world,
					this.layout.getPlayerStartPoint(tempPlayer.team, teamId), null,
					null);
			
			this.vehicles.add(tempCar);
		}
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
}