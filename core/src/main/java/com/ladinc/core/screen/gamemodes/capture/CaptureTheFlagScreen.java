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
import com.ladinc.core.player.PlayerInfo;
import com.ladinc.core.screen.gamemodes.GameModeMetaInfo;
import com.ladinc.core.screen.gamemodes.GameModeMetaInfo.GameMode;
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
	 * TODO: - Add better AI - the attack random player is attacking random
	 * players all the time I think. The current AI isn't brilliant. Fix it up
	 * later to include one AI on a team to go get the flag and better ways to
	 * attack players - Add something to show who has the flag
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
			PlayerInfo aiInfo = ai.getVehicle().player;
			if (isOpponentsFlagWithPlayer(aiInfo))
			{
				desiredCoOrd = getFlagLocation(aiInfo.team);
			}
			else if (isTheFlagWithTheOpponents(aiInfo.team))
			{
				desiredCoOrd = getVehicleWithFlagLocation(aiInfo.team);
			}
			else if (isTheFlagWithYourTeam(aiInfo.team))
			{
				if (aiInfo.team == Team.Home)
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
				desiredCoOrd = getOtherTeamsFlagLocation(aiInfo.team);
			}
			
			ai.setDesiredCoOrd(desiredCoOrd);
			ai.calculateMove(delta);
		}
	}
	
	private boolean isOpponentsFlagWithPlayer(PlayerInfo playerInfo)
	{
		return playerInfo.hasOpponentsFlag;
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
	
	private Vector2 getVehicleWithFlagLocation(Team team)
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
		return vehicleWithTheFlag.position;
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
		if (colHelper.homeFlag == null)
		{
			colHelper.homeFlag = captureTheFlagLayout.floorSensors.get(0);
		}
		if (colHelper.awayFlag == null)
		{
			colHelper.awayFlag = captureTheFlagLayout.floorSensors.get(1);
		}
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
		bitmapFont.setColor(1f, 0f, 0f, 1f);
		
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
		bitmapFont.setColor(1.0f, 0f, 0f, 1f);
		bitmapFont.draw(
				spriteBatch,
				"" + colHelper.homeTeamScore,
				(screenWidth / 2)
						- 100
						- bitmapFont.getBounds(String
								.valueOf(colHelper.homeTeamScore)).width / 2,
				screenHeight - 10);
		
		bitmapFont.setColor(0f, 1.0f, 0f, 1f);
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
			retrieveTeamFlag(Team.Away).flagPresent = true;
		}
		else if (colHelper.vehicleWithHomeFlag == null)
		{
			retrieveTeamFlag(Team.Home).flagPresent = true;
		}
	}
	
	@Override
	public DescriptionScreenInfo getScreenInfo()
	{
		
		return CaptureTheFlagScreen.generateScreenInfo();
	}
	
	public static DescriptionScreenInfo generateScreenInfo()
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
	
	public static GameModeMetaInfo getMetaInfo()
	{
		return new GameModeMetaInfo("CARpture The Flag",
				CaptureTheFlagScreen.generateScreenInfo(), GameMode.Capture,
				true);
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