package com.ladinc.core.screen.gamemodes.hill;

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
import com.ladinc.core.screen.gamemodes.GenericKingScreen;
import com.ladinc.core.screen.gamemodes.IGenericLayout;
import com.ladinc.core.screen.gamemodes.GameModeMetaInfo.GameMode;
import com.ladinc.core.screen.gamemodes.capture.CaptureTheFlagScreen;
import com.ladinc.core.screen.gamemodes.king.KingScreen;
import com.ladinc.core.utilities.Enums.Team;
import com.ladinc.core.ux.DescriptionScreenInfo;
import com.ladinc.core.vehicles.Vehicle;

public class HillScreen extends GenericKingScreen {
	private BitmapFont scoreFont;
	
	private HillLayout hillLayout;
	private HillCollisionHelper colHelper;
	
	public HillScreen(CarPartA game) {
		super(game);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void initGame()
	{
		gameMode = GameMode.Hill;
		
		assignTeamSpritesToCars();
		
		bitmapFont = Font.fontTable.get(Font.CONST_50);
		scoreFont = Font.fontTable.get(Font.OCRA_80);
		scoreFont.setScale(2.0f);
		
		colHelper = new HillCollisionHelper();
		world.setContactListener(colHelper);
		
		this.hillLayout.colHelper = colHelper;
		
		this.backgroundSprite = Art.getSprite(Art.PAINTER_BACKGROUND);
		
		for (SimpleAi ai : this.game.controllerManager.getAi())
		{
			ai.resetAiBetweenLevels();
		}
	}
	
	@Override
	public void preCarRender(float delta)
	{
		// this stops the hill change timer from counting while description is
		// displayed.
		if (!this.showDescriptionScreen)
		{
			hillLayout.timeLeft -= delta;
		}
		hillLayout.drawSpritesForTiles(spriteBatch, PIXELS_PER_METER);
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
	
	@Override
	public IGenericLayout resetLayout()
	{
		hillLayout = new HillLayout(world, worldWidth, worldHeight, center, 0);
		return hillLayout;
	}
	
	@Override
	public void calculateAiMovements(float delta)
	{
		Vector2 desiredCoOrd = null;
		Vector2 hillCoOrdinates = null;
		boolean isHumanOnTheHill = isHumanOnHill();
		
		for (FloorTileSensor floorTileSensor : hillLayout.floorSensors)
		{
			if (floorTileSensor.assigned)
			{
				hillCoOrdinates = floorTileSensor.body.getWorldCenter();
				break;
			}
		}
		
		for (SimpleAi ai : this.game.controllerManager.getAi())
		{
			desiredCoOrd = hillCoOrdinates;
			// Check if AI or Player is on the hill. Move one car away. Leave
			// player there
			if (isHumanOnTheHill
					&& ai.getVehicle().player.team == colHelper.currentHillSide)
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
			
			if (desiredCoOrd != null)
			{
				ai.setDesiredCoOrd(desiredCoOrd);
				ai.calculateMove(delta);
			}
		}
	}
	
	private boolean isHumanOnHill()
	{
		boolean humanFromCurrentHillSideIsOnHill = false;
		ArrayList<PlayerInfo> humanPlayerInfo = this.game.controllerManager
				.getPlayers();
		for (PlayerInfo playerInfoOnHill : colHelper.carsOnHill)
		{
			if (playerInfoOnHill.team == colHelper.currentHillSide
					&& humanPlayerInfo.contains(playerInfoOnHill))
			{
				humanFromCurrentHillSideIsOnHill = true;
				break;
			}
		}
		return humanFromCurrentHillSideIsOnHill;
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
	
	@Override
	public DescriptionScreenInfo getScreenInfo()
	{
		return HillScreen.generateScreenInfo();
	}
	
	public static DescriptionScreenInfo generateScreenInfo()
	{
		DescriptionScreenInfo info = new DescriptionScreenInfo();
		
		info.title = "CAR-into-hill";
		info.descriptionText = "Car of the Hill";
		
		info.howToWinText = new ArrayList<String>();
		info.howToWinText
				.add("Decrease your team's timer by remaining in the hill!");
		info.howToWinText.add("The first team who's timer reaches 0 wins.");
		return info;
	}
	
	public static GameModeMetaInfo getMetaInfo()
	{
		return new GameModeMetaInfo("CAR-into-hill", HillScreen.generateScreenInfo(), GameMode.Hill, true);
	}
	
	@Override
	protected void renderUpdates(float delta)
	{
		updateTimer(delta, colHelper.currentHillSide);
		
		if (this.proccessingGameOver)
		{
			this.finishMessage.draw(spriteBatch);
		}
	}
	
	@Override
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
}