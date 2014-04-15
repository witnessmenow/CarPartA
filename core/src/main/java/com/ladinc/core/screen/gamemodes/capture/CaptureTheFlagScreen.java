package com.ladinc.core.screen.gamemodes.capture;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.ladinc.core.CarPartA;
import com.ladinc.core.ai.SimpleAi;
import com.ladinc.core.assets.Art;
import com.ladinc.core.assets.Font;
import com.ladinc.core.objects.FloorTileSensor;
import com.ladinc.core.screen.gamemodes.GenericScreen;
import com.ladinc.core.screen.gamemodes.IGenericLayout;
import com.ladinc.core.screen.gamemodes.king.KingScreen;
import com.ladinc.core.utilities.Enums.Team;
import com.ladinc.core.ux.DescriptionScreenInfo;
import com.ladinc.core.vehicles.Vehicle;

public class CaptureTheFlagScreen extends GenericScreen {
	private float secondsLeft = 120;
	
	private CaptureTheFlagLayout captureTheFlagLayout;
	private CaptureTheFlagCollisionHelper colHelper;
	BitmapFont bitmapFont;
	
	public CaptureTheFlagScreen(CarPartA game) {
		super(game);
	}
	
	/*
	 * TODO: - Add timer and score and get them to work - Add AI to do something
	 * - Add Capture the flag stuff
	 */
	
	@Override
	public void initGame()
	{
		assignTeamSpritesToCars();
		
		bitmapFont = Font.fontTable.get(Font.CONST_50);
		
		colHelper = new CaptureTheFlagCollisionHelper();
		world.setContactListener(colHelper);
		
		this.captureTheFlagLayout.colHelper = colHelper;
		
		this.backgroundSprite = Art.getSprite(Art.PAINTER_BACKGROUND);
		
		for (SimpleAi ai : this.game.controllerManager.getAi())
		{
			ai.resetAiBetweenLevels();
		}
	}
	
	@Override
	public void calculateAiMovements(float delta)
	{
		Vector2 desiredCoOrd = null;
		for (SimpleAi ai : this.game.controllerManager.getAi())
		{
			if (ai.getVehicle().player.hasOpponentsFlag)
			{
				desiredCoOrd = getFlagLocation(ai.getVehicle().player.team);
			}
			else if (isTheFlagWithTheOpponents(ai.getVehicle().player.team))
			{
				Vehicle vehicleWithTheFlag = vehicleWithFlag(ai.getVehicle().player.team);
				desiredCoOrd = vehicleWithTheFlag.position;
			}
			else if (isTheFlagWithYourTeam(ai.getVehicle().player.team))
			{
				if (ai.getVehicle().player.team == Team.Home)
				{
					desiredCoOrd = attackRandomPlayer(getAwayVehicles(),
							Team.Home);
				}
				else
				{
					desiredCoOrd = attackRandomPlayer(getHomeVehicles(),
							Team.Away);
				}
			}
			else
			{
				desiredCoOrd = getOtherTeamsFlagLocation(ai.getVehicle().player.team);
			}
			
			ai.setDesiredCoOrd(desiredCoOrd);
			ai.calculateMove(delta);
		}
	}
	
	private Vector2 getOtherTeamsFlagLocation(Team team)
	{
		Vector2 captureTheFlagLocation = null;
		if (team == Team.Home)
		{
			captureTheFlagLocation = getFlagLocation(Team.Away);
		}
		else
		{
			captureTheFlagLocation = getFlagLocation(Team.Home);
		}
		return captureTheFlagLocation;
	}
	
	private FloorTileSensor retrieveTeamFlag(Team team)
	{
		FloorTileSensor captureTheFlagSensor;
		if (team == Team.Home)
		{
			captureTheFlagSensor = captureTheFlagLayout.floorSensors.get(0);
		}
		else
		{
			captureTheFlagSensor = captureTheFlagLayout.floorSensors.get(1);
		}
		return captureTheFlagSensor;
	}
	
	private Vector2 getFlagLocation(Team team)
	{
		return retrieveTeamFlag(team).body.getWorldCenter();
	}
	
	private Vector2 attackRandomPlayer(List<Vehicle> teamVehicles,
			Team teamToAttack)
	{
		Vector2 desiredCoOrd = null;
		for (Vehicle v : teamVehicles)
		{
			if ((v.player.team == teamToAttack))
			{
				desiredCoOrd = v.body.getWorldCenter();
				if (!v.player.aiAttacking)
				{
					v.player.aiAttacking = true;
					break;
				}
			}
		}
		
		return desiredCoOrd;
	}
	
	private Vehicle vehicleWithFlag(Team team)
	{
		Vehicle vehicleWithTheFlag;
		if (team == Team.Home)
		{
			vehicleWithTheFlag = colHelper.vehicleWithHomeFlag;
		}
		else
		{
			vehicleWithTheFlag = colHelper.vehicleWithAwayFlag;
		}
		return vehicleWithTheFlag;
	}
	
	private boolean isTheFlagWithYourTeam(Team team)
	{
		boolean isTheFlagWithYourTeam = false;
		if (colHelper.vehicleWithHomeFlag != null)
		{
			if (team == Team.Away)
			{
				isTheFlagWithYourTeam = true;
			}
		}
		else if (colHelper.vehicleWithAwayFlag != null)
		{
			if (team == Team.Home)
			{
				isTheFlagWithYourTeam = true;
			}
		}
		return isTheFlagWithYourTeam;
	}
	
	private boolean isTheFlagWithTheOpponents(Team team)
	{
		boolean isTheFlagWithTheOpponents = false;
		if (colHelper.vehicleWithHomeFlag != null)
		{
			if (team == Team.Home)
			{
				isTheFlagWithTheOpponents = true;
			}
		}
		else if (colHelper.vehicleWithAwayFlag != null)
		{
			if (team == Team.Away)
			{
				isTheFlagWithTheOpponents = true;
			}
		}
		return isTheFlagWithTheOpponents;
	}
	
	@Override
	public IGenericLayout resetLayout()
	{
		captureTheFlagLayout = new CaptureTheFlagLayout(world, worldWidth,
				worldHeight, center, 0);
		return captureTheFlagLayout;
	}
	
	@Override
	public void preCarRender(float delta)
	{
		// Draw flag tiles
		captureTheFlagLayout.drawSpritesForTiles(spriteBatch, PIXELS_PER_METER);
	}
	
	@Override
	public void customRender(float delta)
	{
		if (this.proccessingGameOver)
		{
			if (processGameOverTimer(delta))
			{
				handleGameOver();
			}
		}
	}
	
	private void handleGoalScored()
	{
	}
	
	private void updateTimer(float delta)
	{
		bitmapFont.setColor(1f, 0f, 0f, 0f);
		
		String timeLeft = calculateTimeLeft(delta);
		
		bitmapFont.draw(spriteBatch, timeLeft,
				(screenWidth / 2) - bitmapFont.getBounds(timeLeft).width / 2,
				screenHeight - 10);
	}
	
	private String calculateTimeLeft(float secondsPast)
	{
		if (!this.showDescriptionScreen)
		{
			if (secondsLeft != 0)
			{
				secondsLeft -= secondsPast;
				if (secondsLeft < 0)
				{
					secondsLeft = 0;
				}
			}
			else if (!this.proccessingGameOver)
			{
				handleWin();
			}
		}
		
		int minutes = (int) secondsLeft / 60;
		int seconds = (int) secondsLeft % 60;
		
		String secondPrepend = (seconds < 10) ? "0" : "";
		
		return minutes + ":" + secondPrepend + seconds;
	}
	
	private void updateScore()
	{
		bitmapFont.setColor(1.0f, 0f, 0f, 0f);
		bitmapFont.draw(
				spriteBatch,
				"" + colHelper.homeTeamScore,
				(screenWidth / 2)
						- 100
						- bitmapFont.getBounds(String
								.valueOf(colHelper.homeTeamScore)).width / 2,
				screenHeight - 10);
		
		bitmapFont.setColor(0f, 1.0f, 0f, 0f);
		bitmapFont.draw(
				spriteBatch,
				"" + colHelper.awayTeamScore,
				(screenWidth / 2)
						+ 100
						- bitmapFont.getBounds(String
								.valueOf(colHelper.homeTeamScore)).width / 2,
				screenHeight - 10);
	}
	
	@Override
	protected void renderUpdates(float delta)
	{
		reassignFlag();
		updateTimer(delta);
		
		updateScore();
		if (this.proccessingGameOver)
		{
			this.finishMessage.draw(spriteBatch);
		}
	}
	
	private void reassignFlag()
	{
		if (colHelper.vehicleWithAwayFlag == null)
		{
			retrieveTeamFlag(Team.Away).assigned = false;
		}
		else if (colHelper.vehicleWithHomeFlag == null)
		{
			retrieveTeamFlag(Team.Home).assigned = false;
		}
	}
	
	@Override
	public DescriptionScreenInfo generateScreenInfo()
	{
		DescriptionScreenInfo info = new DescriptionScreenInfo();
		
		info.title = "CARpture the flag!";
		info.descriptionText = "Capture the Flag!";
		
		info.howToWinText = new ArrayList<String>();
		info.howToWinText.add("Get points by stealing your opponents flag!");
		info.howToWinText
				.add("The team with the highest score at the end of the timer wins.");
		return info;
	}
	
	protected void handleWin()
	{
		this.gameOverCoolOffTimer = 5.0f;
		this.proccessingGameOver = true;
	}
	
	private void handleGameOver()
	{
		this.game.setScreen(new KingScreen(game));
		dispose();
	}
	/* Skip here */
}