package com.ladinc.core.screen.gamemodes.pong;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.ladinc.core.CarPartA;
import com.ladinc.core.ai.SimpleAi;
import com.ladinc.core.assets.Art;
import com.ladinc.core.assets.Font;
import com.ladinc.core.objects.balls.PongBall;
import com.ladinc.core.screen.gamemodes.GameModeMetaInfo;
import com.ladinc.core.screen.gamemodes.GenericScreen;
import com.ladinc.core.screen.gamemodes.IGenericLayout;
import com.ladinc.core.screen.gamemodes.GameModeMetaInfo.GameMode;
import com.ladinc.core.screen.gamemodes.mazes.MazeScreen;
import com.ladinc.core.screen.gamemodes.painter.PainterScreen;
import com.ladinc.core.utilities.Enums.Team;
import com.ladinc.core.ux.DescriptionScreenInfo;
import com.ladinc.core.vehicles.Vehicle;

public class PongScreen extends GenericScreen 
{
	
	private PongLayout pongLayout;
	private PongBall pongBall;
	
	private float secondsLeft = 60;
	private int homeScore = 0;
	private int awayScore = 0;
	
	private BitmapFont scoreFont;
	private BitmapFont bitmapFont;
	
	private PongCollisionHelper colHelper;
	
	public PongScreen(CarPartA game) {
		super(game);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void preCarRender(float delta)
	{
		// TODO Auto-generated method stub
		
	}
	
	private String tempScore = "";
	
	private void updateScore()
	{
		scoreFont.setColor(0.75f, 0.75f, 0.75f, 1.0f);
		
		if (homeScore < 10)
		{
			tempScore = "0" + homeScore;
		}
		else
		{
			tempScore = String.valueOf(homeScore);
		}
		scoreFont.draw(spriteBatch, tempScore, (screenWidth / 2) - 150
				- scoreFont.getBounds(tempScore).width / 2, screenHeight - 60);
		
		if (awayScore < 10)
		{
			tempScore = "0" + awayScore;
		}
		else
		{
			tempScore = String.valueOf(awayScore);
		}
		
		scoreFont.draw(spriteBatch, tempScore, (screenWidth / 2) + 150
				- scoreFont.getBounds(tempScore).width / 2, screenHeight - 60);
	}
	
	private boolean pongBallMoving = false;
	
	@Override
	public void customRender(float delta)
	{
		if (!pongBallMoving)
		{
			if (resetCoolOffTimer > 0)
			{
				resetCoolOffTimer = resetCoolOffTimer - delta;
			}
			else
			{
				this.pongBallMoving = true;
				this.pongBall.startBall();
			}
		}
		
		if (!this.proccessingGameOver)
		{
			if (colHelper.newScore)
			{
				if (processingGoal)
				{
					// not interested in goals scored during processing goals
					colHelper.newScore = false;
				}
				else
				{
					handleGoalScored();
				}
			}
			
		}
		else
		{
			if (processGameOverTimer(delta))
			{
				handleGameOver();
			}
		}
		
		if (processingGoal)
		{
			if (goalCoolOffTimer > 0)
			{
				goalCoolOffTimer = goalCoolOffTimer - delta;
			}
			else
			{
				resetBall();
				processingGoal = false;
			}
		}
		
	}
	
	private float resetCoolOffTimer = 0f;
	
	private void resetBall()
	{
		this.pongBall.destroyBody();
		this.pongBall.createPongBody(world, center.x, getY());
		resetCoolOffTimer = 1f;
		this.pongBallMoving = false;
		
	}
	
	private float getY()
	{
		return center.y;
	}
	
	private void handleGameOver()
	{
		this.game.setScreen(new MazeScreen(game));
		dispose();
	}
	
	private float goalCoolOffTimer = 0f;
	private boolean processingGoal = false;
	
	private void handleGoalScored()
	{
		if (colHelper.getLastScored() == Team.Home)
		{
			// Goal was in home's new, goal for away team
			awayScore++;
			// lastSideToScore = Side.Away;
		}
		else
		{
			homeScore++;
			// lastSideToScore = Side.Home;
		}
		
		goalCoolOffTimer = 3.0f;
		processingGoal = true;
	}
	
	@Override
	public IGenericLayout resetLayout()
	{
		pongLayout = new PongLayout(world, worldWidth, worldHeight, center, 0);
		return pongLayout;
	}
	
	@Override
	public void initGame()
	{
		assignTeamSpritesToCars();
		bitmapFont = Font.fontTable.get(Font.CONST_50);
		
		colHelper = new PongCollisionHelper(center);
		
		scoreFont = Font.fontTable.get(Font.OCRA_80);
		scoreFont.setColor(0.75f, 0.75f, 0.75f, 1.0f);
		
		scoreFont.setScale(2.0f);
		
		world.setContactListener(colHelper);
		
		this.backgroundSprite = Art.getSprite(Art.PONG_BACKGROUND);
		this.backgroundSprite.setPosition(0, 0);
		
		pongBall = new PongBall(world, this.center.x, this.center.y,
				Art.getSprite(Art.PONG_BALL));
		resetCoolOffTimer = 1f;
		
		for (Vehicle v : getVehicles())
		{
			v.disableSteering = true;
			v.body.setFixedRotation(true);
		}
		
		for (SimpleAi ai : this.game.controllerManager.getAi())
		{
			ai.resetAiBetweenLevels();
			ai.justAccelerateAndReverse = true;
		}
		
	}
	
	@Override
	public void calculateAiMovements(float delta)
	{
		this.aiMove.position = this.pongBall.body.getWorldCenter();
		
		for (SimpleAi ai : this.game.controllerManager.getAi())
		{
			// ai.setDesiredPosition(aiMove);
			ai.setDesiredCoOrd(this.aiMove.position);
			ai.calculateMove(delta);
		}
	}
	
	@Override
	protected void renderUpdates(float delta)
	{
		updateScore();
		updateTimer(delta);
		
		pongBall.updateSprite(spriteBatch, PIXELS_PER_METER);
		
		if (this.proccessingGameOver)
		{
			this.finishMessage.draw(spriteBatch);
		}
		
	}
	
	private String timeLeft;
	
	private void updateTimer(float delta)
	{
		bitmapFont.setColor(0.75f, 0.75f, 0.75f, 1.0f);
		
		timeLeft = calculateTimeLeft(delta);
		
		bitmapFont.draw(spriteBatch, timeLeft,
				(screenWidth / 2) - bitmapFont.getBounds(timeLeft).width / 2,
				screenHeight - 60);
	}
	
	private String calculateTimeLeft(float secondsPast)
	{
		if(!this.showDescriptionScreen)
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
		
		int seconds = (int) secondsLeft;
		
		String secondPrepend = (seconds < 10) ? "0" : "";
		
		return secondPrepend + seconds;
	}
	
	private void handleWin()
	{
		this.gameOverCoolOffTimer = 5.0f;
		this.proccessingGameOver = true;
		
	}

	@Override
	public DescriptionScreenInfo getScreenInfo() 
	{

		return PongScreen.generateScreenInfo();
	}
	
	public static DescriptionScreenInfo generateScreenInfo()
	{
		DescriptionScreenInfo info = new DescriptionScreenInfo();
		
		info.title = "Pong-Cars";
		info.descriptionText = "Pong with cars.";
		
		info.howToWinText = new ArrayList<String>();
		info.howToWinText.add("Only accelerate and reverse are enabled (no steering).");
		info.howToWinText.add("Score more points than the oposition before the timer runs out to win");
		// TODO Auto-generated method stub
		return info;
	}
	
	public static GameModeMetaInfo getMetaInfo()
	{
		return new GameModeMetaInfo("Pong-Cars", PongScreen.generateScreenInfo(), GameMode.Pong, true);
	}
	
}
