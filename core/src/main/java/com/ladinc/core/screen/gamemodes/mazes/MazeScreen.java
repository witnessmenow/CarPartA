package com.ladinc.core.screen.gamemodes.mazes;

import java.util.ArrayList;

import com.ladinc.core.CarPartA;
import com.ladinc.core.ai.SimpleAi;
import com.ladinc.core.assets.Art;
import com.ladinc.core.screen.gamemodes.GameModeMetaInfo;
import com.ladinc.core.screen.gamemodes.GenericScreen;
import com.ladinc.core.screen.gamemodes.GameModeMetaInfo.GameMode;
import com.ladinc.core.screen.gamemodes.king.KingScreen;
import com.ladinc.core.screen.gamemodes.painter.PainterScreen;
import com.ladinc.core.utilities.Enums.Team;
import com.ladinc.core.ux.DescriptionScreenInfo;
import com.ladinc.core.vehicles.Vehicle;

public class MazeScreen extends GenericScreen 
{
	
	private MazeLayout mazeLayout;
	
	public MazeScreen(CarPartA game) {
		super(game);
	}
	
	@Override
	public MazeLayout resetLayout()
	{
		mazeLayout = new MazeLayout(world, worldWidth, worldHeight, center, 4);
		return mazeLayout;
	}
	
	@Override
	protected void renderUpdates(float delta)
	{
		if (this.proccessingGameOver)
		{
			this.finishMessage.draw(spriteBatch);
		}
	}
	
	@Override
	public void initGame()
	{
		gameMode = GameMode.Amazing;
		
		assignTeamSpritesToCars();
		
		this.backgroundSprite = Art.getSprite(Art.RACE_BACKGROUND_1);
		this.backgroundSprite.setPosition(0, 0);
		
		for (SimpleAi ai : this.game.controllerManager.getAi())
		{
			ai.resetAiBetweenLevels();
		}
		
	}
	
	private void handleWin()
	{
		this.incrementGameCount();
		
		this.gameOverCoolOffTimer = 5.0f;
		this.proccessingGameOver = true;
	}
	
	@Override
	public void calculateAiMovements(float delta)
	{
		for (SimpleAi ai : this.game.controllerManager.getAi())
		{
			// ai.setDesiredPosition(aiMove);
			ai.setDesiredCoOrd(this.mazeLayout.calculateAiTarget(ai
					.getVehicle().body.getWorldCenter()));
			ai.calculateMove(delta);
		}
	}
	
	@Override
	public void customRender(float delta)
	{
		if (!this.proccessingGameOver)
		{
			Vehicle v = this.mazeLayout.checkForWinForAllCars(getVehicles());
			if (v != null)
			{
				this.teamWhoWon = v.player.team;
				handleWin();
			}
		}
		else
		{
			if (processGameOverTimer(delta))
			{
				handleGameOver();
			}
		}
		
	}
	
	@Override
	public void preCarRender(float delta)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public DescriptionScreenInfo getScreenInfo() 
	{
		return MazeScreen.generateScreenInfo();
	}
	
	public static DescriptionScreenInfo generateScreenInfo()
	{
		DescriptionScreenInfo info = new DescriptionScreenInfo();
		
		info.title = "A-Maze-ing";
		info.descriptionText = "The worst \"maze\" ever.";
		
		info.howToWinText = new ArrayList<String>();
		info.howToWinText.add("First team with a member to reache the center wins.");
		// TODO Auto-generated method stub
		return info;
	}
	
	public static GameModeMetaInfo getMetaInfo()
	{
		return new GameModeMetaInfo("A-Maze-ing", MazeScreen.generateScreenInfo(), GameMode.Amazing, true);
	}
}