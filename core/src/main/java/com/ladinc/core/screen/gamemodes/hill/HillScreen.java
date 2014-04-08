package com.ladinc.core.screen.gamemodes.hill;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.ladinc.core.CarPartA;
import com.ladinc.core.ai.SimpleAi;
import com.ladinc.core.assets.Art;
import com.ladinc.core.assets.Font;
import com.ladinc.core.screen.gamemodes.GenericKingScreen;
import com.ladinc.core.screen.gamemodes.IGenericLayout;
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
		assignTeamSpritesToCars();
		
		bitmapFont = Font.fontTable.get(Font.CONST_50);
		scoreFont = Font.fontTable.get(Font.OCRA_80);
		scoreFont.setScale(2.0f);
		
		colHelper = new HillCollisionHelper();
		world.setContactListener(colHelper);
		
		this.backgroundSprite = Art.getSprite(Art.PAINTER_BACKGROUND);
		
		for (SimpleAi ai : this.game.controllerManager.getAi())
		{
			ai.resetAiBetweenLevels();
		}
	}
	
	@Override
	public void preCarRender(float delta)
	{
		hillLayout.timeLeft -= delta;
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
		
		for (SimpleAi ai : this.game.controllerManager.getAi())
		{
			if (ai.getVehicle().player.team == Team.Home)
			{
				for (Vehicle v : getVehicles())
				{
					if (!v.player.controls.isAi()
							&& (v.player.team == Team.Away))
					{
						desiredCoOrd = v.body.getWorldCenter();
						break;
					}
				}
			}
			else
			{
				for (Vehicle v : getVehicles())
				{
					if (!v.player.controls.isAi()
							&& (v.player.team == Team.Home))
					{
						desiredCoOrd = v.body.getWorldCenter();
						break;
					}
				}
			}
			
			if (desiredCoOrd != null)
			{
				ai.setDesiredCoOrd(desiredCoOrd);
				ai.calculateMove(delta);
			}
		}
	}
	
	@Override
	public DescriptionScreenInfo generateScreenInfo()
	{
		DescriptionScreenInfo info = new DescriptionScreenInfo();
		
		info.title = "CARrauntoohil";
		info.descriptionText = "Car of the Hill";
		
		info.howToWinText = new ArrayList<String>();
		info.howToWinText
				.add("Decrease your team's timer by remaining in the hill!");
		info.howToWinText.add("The first team who's timer reaches 0 wins.");
		return info;
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