package com.ladinc.core.screen.gamemodes.soccer;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.ladinc.core.CarPartA;
import com.ladinc.core.ai.SimpleAi;
import com.ladinc.core.assets.Art;
import com.ladinc.core.assets.Font;
import com.ladinc.core.collision.CollisionHelper;
import com.ladinc.core.objects.balls.Ball;
import com.ladinc.core.screen.gamemodes.GameModeMetaInfo;
import com.ladinc.core.screen.gamemodes.GenericLayout;
import com.ladinc.core.screen.gamemodes.GenericScreen;
import com.ladinc.core.screen.gamemodes.GameModeMetaInfo.GameMode;
import com.ladinc.core.screen.gamemodes.king.KingScreen;
import com.ladinc.core.screen.gamemodes.pong.PongScreen;
import com.ladinc.core.utilities.Enums.Team;
import com.ladinc.core.ux.DescriptionScreenInfo;

public class SoccerScreen extends GenericScreen 
{
	private float secondsLeft = 120;
	private int homeScore = 0;
	private int awayScore = 0;
	private Ball ball;
	
	private Sprite goalMessageSprite;
	
	private SoccerLayout soccerLayout;
	
	private static final float BALL_SIZE = 2.5f;
	
	BitmapFont bitmapFont;
	
	public SoccerScreen(CarPartA game) {
		super(game);
	}
	
	@Override
	public GenericLayout resetLayout()
	{
		soccerLayout = new SoccerLayout(world, worldWidth, worldHeight, center,
				4);
		return soccerLayout;
	}
	
	@Override
	public void preCarRender(float delta)
	{
		
	}
	
	@Override
	protected void renderUpdates(float delta)
	{
		Art.updateSprite(this.ball.sprite, spriteBatch, PIXELS_PER_METER,
				this.ball.body);
		updateTimer(delta);
		updateScore();
		if (this.proccessingGameOver)
		{
			this.finishMessage.draw(spriteBatch);
		}
		else if (processingGoal)
		{
			this.goalMessageSprite.draw(spriteBatch);
		}
	}
	
	private String timeLeft;
	
	private void updateTimer(float delta)
	{
		bitmapFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		
		timeLeft = calculateTimeLeft(delta);
		
		bitmapFont.draw(spriteBatch, timeLeft,
				(screenWidth / 2) - bitmapFont.getBounds(timeLeft).width / 2,
				screenHeight - 10);
	}
	
	private void updateScore()
	{
		bitmapFont.draw(spriteBatch, "" + homeScore, (screenWidth / 2) - 100
				- bitmapFont.getBounds(String.valueOf(homeScore)).width / 2,
				screenHeight - 10);
		bitmapFont.draw(spriteBatch, "" + awayScore, (screenWidth / 2) + 100
				- bitmapFont.getBounds(String.valueOf(homeScore)).width / 2,
				screenHeight - 10);
	}
	
	private void handleWin()
	{
		this.gameOverCoolOffTimer = 5.0f;
		this.proccessingGameOver = true;
		
		if(this.homeScore > this.awayScore)
		{
			this.teamWhoWon = Team.Home;
		}
		else if(this.awayScore > this.homeScore)
		{
			this.teamWhoWon = Team.Away;
		}
		
		this.incrementGameCount();
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
		
		int minutes = (int) secondsLeft / 60;
		int seconds = (int) secondsLeft % 60;
		
		String secondPrepend = (seconds < 10) ? "0" : "";
		
		return minutes + ":" + secondPrepend + seconds;
	}
	
	@Override
	public void initGame()
	{
		gameMode = GameMode.Soccar;
		
		bitmapFont = Font.fontTable.get(Font.CONST_50);
		
		this.colHelper = new CollisionHelper();
		world.setContactListener(this.colHelper);
		colh = (CollisionHelper) this.colHelper;
		
		this.soccerLayout.createGoals(world, BALL_SIZE * 2);
		
		this.backgroundSprite = this.soccerLayout.getPitchSprite();
		this.backgroundSprite.setPosition(0.0f, 0.0f);
		
		this.goalMessageSprite = Art.getSprite(Art.GOAL_OVERLAY);
		this.backgroundSprite.setPosition(0.0f, 0.0f);
		
		assignTeamSpritesToCars();
		
		recreateBall();
		
		for (SimpleAi ai : this.game.controllerManager.getAi())
		{
			ai.resetTimers();
		}
		
	}
	
	private void recreateBall()
	{
		if (ball != null)
		{
			world.destroyBody(ball.body);
		}
		
		ball = new Ball(world, center.x + Ball.ballOffsetX, center.y,
				getSoccerBallSrpite(), BALL_SIZE);
	}
	
	private Sprite ballSprite;
	
	private Sprite getSoccerBallSrpite()
	{
		if (ballSprite == null)
		{
			ballSprite = new Sprite(Art.textureTable.get(Art.BALLS), 0, 0, 53,
					52);
		}
		return ballSprite;
	}
	
	CollisionHelper colh;
	
	@Override
	public void customRender(float delta)
	{
		this.ball.update();
		
		if (!this.proccessingGameOver)
		{
			if (colh.newScore)
			{
				if (processingGoal)
				{
					// not interested in goals scored during processing goals
					colh.newScore = false;
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
				resetPlayersAndBall();
				processingGoal = false;
			}
		}
		
	}
	
	private void resetPlayersAndBall()
	{
		resetCars();
		assignTeamSpritesToCars();
		recreateBall();
		for (SimpleAi ai : this.game.controllerManager.getAi())
		{
			ai.resetAiBetweenLevels();
		}
	}
	
	@Override
	public void calculateAiMovements(float delta)
	{
		this.aiMove.position = this.ball.body.getWorldCenter();
		
		for (SimpleAi ai : this.game.controllerManager.getAi())
		{
			// ai.setDesiredPosition(aiMove);
			ai.setDesiredCoOrd(this.aiMove.position);
			ai.calculateMove(delta);
		}
	}
	
	private float goalCoolOffTimer = 0f;
	private boolean processingGoal = false;
	
	private void handleGoalScored()
	{
		if (colh.getLastScored() == Team.Home)
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
		
		goalCoolOffTimer = 5.0f;
		processingGoal = true;
	}

	@Override
	public DescriptionScreenInfo getScreenInfo() 
	{
		return SoccerScreen.generateScreenInfo();
	}
	
	public static DescriptionScreenInfo generateScreenInfo()
	{
		DescriptionScreenInfo info = new DescriptionScreenInfo();
		
		info.title = "Soc-Car";
		info.descriptionText = "Soccer with cars.";
		
		info.howToWinText = new ArrayList<String>();
		info.howToWinText.add("Score more goals that opposition before the timer runs out to win.");
		// TODO Auto-generated method stub
		return info;
	}
	
	public static GameModeMetaInfo getMetaInfo()
	{
		return new GameModeMetaInfo("Soc-Car", SoccerScreen.generateScreenInfo(), GameMode.Soccar, true);
	}
	
}