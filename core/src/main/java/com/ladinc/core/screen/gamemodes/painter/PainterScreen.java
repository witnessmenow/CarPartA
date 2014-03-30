package com.ladinc.core.screen.gamemodes.painter;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.ladinc.core.CarPartA;
import com.ladinc.core.ai.SimpleAi;
import com.ladinc.core.assets.Art;
import com.ladinc.core.assets.Font;
import com.ladinc.core.player.PlayerInfo;
import com.ladinc.core.screen.gamemodes.GenericScreen;
import com.ladinc.core.screen.gamemodes.IGenericLayout;
import com.ladinc.core.screen.gamemodes.mazes.MazeScreen;
import com.ladinc.core.screen.gamemodes.pong.PongScreen;
import com.ladinc.core.utilities.Enums.Team;
import com.ladinc.core.vehicles.Vehicle;

public class PainterScreen extends GenericScreen
{
	private float secondsLeft = 30;
	private BitmapFont bitmapFont;
	private BitmapFont scoreFont;
	
	private PainterLayout painterLayout;
	private PainterCollisionHelper colHelper;
	
	public PainterScreen(CarPartA game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void preCarRender(float delta) 
	{
		painterLayout.drawSpritesForTiles(spriteBatch, PIXELS_PER_METER);
		
	}

	@Override
	public void customRender(float delta) 
	{
		if(this.proccessingGameOver)
		{
			if(processGameOverTimer(delta))
			{
				handleGameOver();
			}
		}
		
	}
	
	private void handleGameOver()
	{
		this.game.setScreen(new PongScreen(game));
		dispose();
	}

	@Override
	public IGenericLayout resetLayout() {
		painterLayout = new PainterLayout(world, worldWidth, worldHeight, center, 0);
		return painterLayout;
	}

	@Override
	public void initGame() {
		assignTeamSpritesToCars();
		
		bitmapFont = Font.fontTable.get(Font.CONST_50);

		scoreFont = Font.fontTable.get(Font.OCRA_80);
		
		scoreFont.setScale(2.0f);
		
		colHelper =  new PainterCollisionHelper();
		
		world.setContactListener(colHelper);
		
		this.backgroundSprite = Art.getSprite(Art.PAINTER_BACKGROUND);
		
		for (SimpleAi ai : this.game.controllerManager.getAi())
		{
			ai.resetTimers();
			ai.justAccelerateAndReverse = false;
		}
	}
	
	private String timeLeft;
	private void updateTimer(float delta)
	{
		bitmapFont.setColor(0f, 0f, 0f, 1.0f);
		
		timeLeft = calculateTimeLeft(delta);
		
		bitmapFont.draw(spriteBatch, timeLeft,
				(screenWidth / 2) - bitmapFont.getBounds(timeLeft).width/2, screenHeight - 10);
	}
	
	private String calculateTimeLeft(float secondsPast)
	{
		if (secondsLeft != 0)
		{
			secondsLeft -= secondsPast;
			if (secondsLeft < 0)
			{
				secondsLeft = 0;
			}
		}
		else if(!this.proccessingGameOver)
		{
			handleWin();
		}
		
		int seconds = (int) secondsLeft ;
		
		String secondPrepend =  (seconds< 10)?"0":"";
		
		return secondPrepend + seconds;
	}
	
	private int homeScore;
	private int awayScore;
	private String tempScore;
	
	private void updateScore()
	{	
		
		if(homeScore < 10)
		{
			tempScore = "0" + homeScore;
		}
		else
		{
			tempScore = String.valueOf(homeScore);
		}
		
    	//Set for blue
		scoreFont.setColor(0.3f, 0.5f, 1f, 1.0f);
		
		scoreFont.draw(spriteBatch, tempScore, (screenWidth / 2) - 150 - scoreFont.getBounds(tempScore).width/2,
				screenHeight - 90);
		
		if(awayScore < 10)
		{
			tempScore = "0" + awayScore;
		}
		else
		{
			tempScore = String.valueOf(awayScore);
		}
		
		scoreFont.setColor(1f, 0.3f, 0.3f, 1.0f);
		
		scoreFont.draw(spriteBatch, tempScore, (screenWidth / 2) + 150 - scoreFont.getBounds(tempScore).width/2,
				screenHeight - 90);
	}

	private void handleWin() 
	{
		this.gameOverCoolOffTimer = 5.0f;
    	this.proccessingGameOver = true;
    	
    	this.colHelper.enableTileChange = false;
    	this.painterLayout.calculateScores();
    	
    	this.homeScore = this.painterLayout.homeScore;
    	this.awayScore = this.painterLayout.awayScore;
		
	}
	
	@Override
	public void calculateAiMovements(float delta)
	{
		Vector2 desiredCoOrd = null;
		
		for (SimpleAi ai : this.game.controllerManager.getAi())
		{
			if(ai.getVehicle().player.team == Team.Home)
			{
				for(Vehicle v : getVehicles())
				{
					if(!v.player.controls.isAi() && (v.player.team == Team.Away))
					{
						desiredCoOrd = v.body.getWorldCenter();
						break;
					}
				}
			}
			else
			{
				for(Vehicle v : getVehicles())
				{
					if(!v.player.controls.isAi() && (v.player.team == Team.Home))
					{
						desiredCoOrd = v.body.getWorldCenter();
						break;
					}
				}
			}
			
			if(desiredCoOrd != null)
			{
				ai.setDesiredCoOrd(desiredCoOrd);
				ai.calculateMove(delta);
			}
		}
	}

	@Override
	protected void renderUpdates(float delta) 
	{
		updateTimer(delta);
		if(this.proccessingGameOver)
		{
			updateScore();
			this.finishMessage.draw(spriteBatch);
		}
		
	}

}