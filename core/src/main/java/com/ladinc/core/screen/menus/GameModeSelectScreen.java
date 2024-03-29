package com.ladinc.core.screen.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ladinc.core.CarPartA;
import com.ladinc.core.assets.Art;
import com.ladinc.core.assets.Font;
import com.ladinc.core.controllers.controls.IControls;
import com.ladinc.core.controllers.controls.TouchPadControls;
import com.ladinc.core.player.PlayerInfo;
import com.ladinc.core.screen.gamemodes.GameModeMetaInfo;
import com.ladinc.core.screen.gamemodes.GameModeMetaInfo.GameMode;
import com.ladinc.core.screen.gamemodes.teamselect.TeamSelectScreen;
import com.ladinc.core.ux.DescriptionScreen;

public class GameModeSelectScreen implements Screen
{

	private static final int NUM_OF_COLS = 5;
	private static final int NUM_OF_ROWS = 3;
	
	private final CarPartA game;
	private final SpriteBatch spriteBatch;
	OrthographicCamera camera;
	Sprite messageSprite;
	
	// Used for sprites etc
	private int screenWidth;
	private int screenHeight;
	
	private boolean confirmationScreenVisible = false;
	protected boolean showDescriptionScreen = false;
	
	private Sprite confirmSprite;
	
	private Sprite overlaySprite;
	
	private float menuMovementCoolDown = 0.3f;
	
	private boolean[][] populatedSlots = new boolean[NUM_OF_ROWS][NUM_OF_COLS];
	private int selectedRow = 0;
	private int selectedCol = 0;
	
	private BitmapFont titleFont;
	
	private GameModeMetaInfo currentSelectedGame;
	private DescriptionScreen descriptionScreen;
	
	private boolean touchScreenCurrentlyPressed = false;
	float touchedX;
	float touchedY;
	
	public GameModeSelectScreen(CarPartA game) {
		this.game = game;
		spriteBatch = new SpriteBatch();
		
		messageSprite = Art.getSprite(Art.DEMO_MESSAGE);
		messageSprite.setPosition(0, 0);
		
		this.screenWidth = 1920;
		this.screenHeight = 1080;
		
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, this.screenWidth, this.screenHeight);
	}
	
	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0, 0, 0f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		spriteBatch.setProjectionMatrix(camera.combined);
		
		spriteBatch.begin();
		//messageSprite.draw(spriteBatch);
		drawGameImages(spriteBatch);
		
		drawGameTitleText(spriteBatch);
		
		this.overlaySprite.draw(spriteBatch);
		if(this.showDescriptionScreen)
		{	
			this.descriptionScreen.drawDescriptionScreen(spriteBatch, delta);
		}
		else if(confirmationScreenVisible)
		{
			confirmSprite.draw(spriteBatch);
		}
		spriteBatch.end();
		
		
		
		if(menuMovementCoolDown <= 0)
		{
			checkForInputs();
		}
		else
		{
			this.menuMovementCoolDown = this.menuMovementCoolDown - delta;
		}
		
		
	}
	
	private void drawGameTitleText(SpriteBatch sb)
	{
		titleFont.setColor(Color.WHITE);
		titleFont.draw(spriteBatch, this.currentSelectedGame.name, this.screenWidth/2 - titleFont.getBounds(this.currentSelectedGame.name).width/2, (this.screenHeight/6)*5);
	}
	
	private void drawGameImages(SpriteBatch sb)
	{
		int col = 0;
		int row = 0;
		
		float gapBetweenImages = 75f;
		float gapAtEdge = 185f;
		
		float startingY = this.screenHeight/2;
		
		float tempX;
		float tempY;
		
		for(GameModeMetaInfo gmmi : this.game.gameModes)
		{
			tempY = startingY - (row * (GameModeMetaInfo.IMAGE_HEIGHT + gapBetweenImages));
			
			tempX = gapAtEdge + (col * (GameModeMetaInfo.IMAGE_WIDTH + gapBetweenImages));
			
			gmmi.image.setPosition(tempX, tempY);
			gmmi.image.draw(sb);
			
			if(this.selectedCol == col && this.selectedRow == row)
			{
				Sprite selectedIndicator = GameModeMetaInfo.getSelectedGameModeSprite();
				selectedIndicator.setPosition(tempX - 15f, tempY - 15f);
				selectedIndicator.draw(sb);
				
				this.currentSelectedGame = gmmi;
			}
			
			if(touchScreenCurrentlyPressed)
			{
				if(touchedY >= tempY && touchedY <= tempY + GameModeMetaInfo.IMAGE_HEIGHT)
				{
					if(touchedX >= tempX && touchedX <= tempX + GameModeMetaInfo.IMAGE_WIDTH)
					{
						this.selectedCol = col;
						this.selectedRow = row;
					}
				}
			}
			
			populatedSlots[row][col] = true;
			
			col++;
			
			if(col >= NUM_OF_COLS)
			{
				col = 0;
				row++;
			}
		}
	}
	
	private void attemptToMoveX(int move)
	{
		
		int col = this.selectedCol + move;
		
		if(col < 0)
		{
			col = NUM_OF_COLS -1;
			while(!populatedSlots[selectedRow][col])
			{
				col --;
			}
			
			this.selectedCol = col;
			return;
		}
		else if(col > NUM_OF_COLS - 1)
		{
			this.selectedCol = 0;
			return;
		}
		else
		{
			if(populatedSlots[selectedRow][col])
			{
				this.selectedCol = col;
				return;
			}
			else
			{

				this.selectedCol = 0;
				return;

			}
		}
	}
	
	private void attemptToMoveY(int move)
	{
		
		int row = this.selectedRow + move;
		
		if(row < 0)
		{
			row = NUM_OF_ROWS -1;
			while(!populatedSlots[row][this.selectedCol])
			{
				row --;
			}
			
			this.selectedRow = row;
			return;
		}
		else if(row > NUM_OF_ROWS - 1)
		{
			this.selectedRow = 0;
			return;
		}
		else
		{
			if(populatedSlots[row][this.selectedCol])
			{
				this.selectedRow = row;
				return;
			}
			else
			{

				this.selectedRow = 0;
				return;

			}
		}
	}
	
	private void checkForInputs()
	{
		//lets assume its going to return early and we need to set the cool down
		this.menuMovementCoolDown = 0.3f;
		
		touchScreenCurrentlyPressed = Gdx.input.isTouched();
		touchedX = ((float)Gdx.input.getX()/(float)Gdx.graphics.getWidth())*(float)this.screenWidth;
		touchedY = (float)this.screenHeight - ((float)Gdx.input.getY()/(float)Gdx.graphics.getHeight())*(float)this.screenHeight;
		
		boolean startTouchPressed = (touchedY <=  200f) && touchScreenCurrentlyPressed;
		
		IControls tempCont;
		for (PlayerInfo player : this.game.controllerManager.getPlayers())
		{
			tempCont = player.controls;
			if(confirmationScreenVisible)
			{
				if (tempCont.getConfirmStatus())
				{
					startNewScreen();
					return;
				}
				else if (tempCont.getBackStatus())
				{
					confirmationScreenVisible = false;
					return;
				}
			}
			else if(showDescriptionScreen)
			{
				if(tempCont.isActive())
				{
					showDescriptionScreen = false;
					return;
				}
			}
			else
			{
				
				if(tempCont instanceof TouchPadControls)
				{
					if(startTouchPressed)
					{
						confirmationScreenVisible = true;
						return;
					}
				}
				else
				{
				
					int moveX = tempCont.getMenuXDireciton();
					int moveY = tempCont.getMenuYDireciton();
					
					if(tempCont.getStartStatus() || tempCont.getConfirmStatus() || startTouchPressed)
					{
						confirmationScreenVisible = true;
						return;
					}
//					else if(tempCont.getConfirmStatus())
//					{
//						showDescriptionScreen = true;
//						this.game.controllerManager.resetActiveStateOfControllers();
//						
//						if(this.descriptionScreen == null)
//						{
//							this.descriptionScreen = new DescriptionScreen(new Vector2(screenWidth/2, screenHeight/2), this.currentSelectedGame.description);
//						}
//						else
//						{
//							this.descriptionScreen.info = this.currentSelectedGame.description;
//						}
//						
//						this.descriptionScreen.menuMode = true;
//						
//						return;
//					}
					else if(tempCont.getBackStatus())
					{
						showDescriptionScreen = true;
						this.game.controllerManager.resetActiveStateOfControllers();
						return;
					}
					else if(moveX != 0)
					{
						attemptToMoveX(moveX);
						return;
					}
					else if(moveY != 0)
					{
						attemptToMoveY(moveY);
						return;
					}
				}
			}
			

		}
		
		//it didnt return early, so lets remove the cool down
		this.menuMovementCoolDown = 0.0f;
	}
	
	
	private void startNewScreen()
	{
		this.game.setGameMode(this.currentSelectedGame.game);
		dispose();
	}
	
	@Override
	public void resize(int width, int height)
	{
		// TODO Auto-generated method stub
	}
	
	@Override
	public void show()
	{
		this.game.controllerManager.resetActiveStateOfControllers();
		this.game.controllerManager.setMenuInterest(true);
		
		if (this.game.controllerManager.hasTouchControls)
		{
			this.confirmSprite = Art.getSprite(Art.START_GAME_CONFIRM_TOUCH);
		}
		else
		{
			this.confirmSprite = Art.getSprite(Art.START_GAME_CONFIRM);
		}
		
		this.confirmSprite.setPosition(0.0f, 0.0f);
		
		if (this.game.controllerManager.hasTouchControls)
		{
			this.overlaySprite = Art.getSprite(Art.GAME_MODE_OVERLAY_TOUCH);
		}
		else
		{
			this.overlaySprite = Art.getSprite(Art.GAME_MODE_OVERLAY);
		}
		
		this.overlaySprite.setPosition(0.0f, 0.0f);
		
		titleFont = Font.fontTable.get(Font.PLAYBILL_75_BOLD);
		titleFont.setScale(1.5f);
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
		// TODO: DISPOSE OF STUFF
		spriteBatch.dispose();
	}
}
