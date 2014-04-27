package com.ladinc.core.ux;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ladinc.core.assets.Art;
import com.ladinc.core.assets.Font;

public class AddAiOverlay 
{
	private static final int MAX_AI = 4;
	
	private static Sprite MINUS_SYMBOL = null;
	private static Sprite PLUS_SYMBOL = null;
	
	private static final String AI_TITLE = "Add AI Players?";
	private static final String HOME = "Home";
	private static final String AWAY = "Away";
	
	private static final String START_GAME = "Start Game!";
	
	private static final String DESC1 = "TIP: Most Modes work better ";
	private static final String DESC2 = "with less AI.";
	
	protected Vector2 center;
	protected Sprite background;
	
	protected BitmapFont largerFont;
	protected BitmapFont largeFont;
	protected BitmapFont smallerFont;
	
	protected float descriptionTextY;
	protected float titleY;
	protected float optionsY;
	protected float optionsY1;
	protected float optionsY2;
	
	protected float textX;
	protected float numberX;
	
	protected float howToWinHeadingY;
	
	public int selectedRowIndex = 0;
	public int selectedColIndex = 1;
	
	private int screenHeight;
	private int screenWidth;
	
	protected boolean touchScreenCurrentlyPressed;
	protected float touchedX;
	protected float touchedY;

	public int homeAi = 0;
	public int awayAi = 0;
	
	public boolean startGame = false;
	
	public AddAiOverlay(Vector2 c)
	{
		if(PLUS_SYMBOL == null)
		{
			PLUS_SYMBOL = new Sprite(Art.textureTable.get(Art.PLUS_MINUS_SYMBOLS), 70, 0, 70, 70);
		}
		
		if(MINUS_SYMBOL == null)
		{
			MINUS_SYMBOL = new Sprite(Art.textureTable.get(Art.PLUS_MINUS_SYMBOLS), 0, 0, 70, 70);
		}
		
		center = c;
		screenWidth = (int) c.x *2;
		screenHeight = (int) c.y *2;
		background = Art.getSprite(Art.GAME_DESCRIPTION_WINDOW);
		background.setPosition(center.x - background.getWidth()/2, center.y - background.getHeight()/2);
		
		largeFont = Font.fontTable.get(Font.PLAYBILL_75_BOLD);
		
		largerFont = Font.fontTable.get(Font.PLAYBILL_75_BOLD);
		
		largeFont.setScale(1.5f);
		//largerFont.setScale(2f);
		
		smallerFont = Font.fontTable.get(Font.PLAYBILL_50);
		
		descriptionTextY = center.y - background.getHeight()/2 + 120f;
		
		optionsY = center.y + background.getHeight()/2 - 225f;
		optionsY1 = optionsY -150f;
		optionsY2 = optionsY1 - 150f;
		
		titleY = center.y + background.getHeight()/2 - 50f;
		
		textX = (center.x - background.getWidth()/2) + background.getWidth()/4;
		numberX = (center.x - background.getWidth()/2) + ((background.getWidth()/4)*3);
		
	}
	
	public void drawOverlay(SpriteBatch spriteBatch, float delta)
	{
		
		background.draw(spriteBatch);
		
		largeFont.setColor(Color.BLACK);
		largeFont.draw(spriteBatch, AI_TITLE, center.x - largeFont.getBounds(AI_TITLE).width/2, titleY);
		
		
		largeFont.draw(spriteBatch, HOME, textX - largeFont.getBounds(HOME).width/2, optionsY1);
		largeFont.draw(spriteBatch, AWAY, textX - largeFont.getBounds(AWAY).width/2, optionsY2);
		
		String tmp = String.valueOf(homeAi);
		largeFont.draw(spriteBatch, tmp, numberX - largeFont.getBounds(tmp).width/2, optionsY1);
		
		tmp = String.valueOf(awayAi);
		largeFont.draw(spriteBatch, tmp, numberX - largeFont.getBounds(tmp).width/2, optionsY2  );
		
		if(selectedRowIndex == 0)
		{
			largeFont.setColor(Color.RED);
		}
		else
		{
			largeFont.setColor(Color.BLUE);
		}
		largeFont.draw(spriteBatch, START_GAME, center.x - largeFont.getBounds(START_GAME).width/2, optionsY);
		
		if(selectedRowIndex == 1 && selectedColIndex == 0)
		{
			MINUS_SYMBOL.setColor(Color.RED);
		}
		else
		{
			MINUS_SYMBOL.setColor(Color.BLUE);
		}
		MINUS_SYMBOL.setPosition((numberX - 100f) - 35f, optionsY1 - 70f);
		MINUS_SYMBOL.draw(spriteBatch);
		
		
		if(selectedRowIndex == 1 && selectedColIndex == 1)
		{
			PLUS_SYMBOL.setColor(Color.RED);
		}
		else
		{
			PLUS_SYMBOL.setColor(Color.BLUE);
		}
		PLUS_SYMBOL.setPosition((numberX + 100f) - 35f, optionsY1 - 70f);
		PLUS_SYMBOL.draw(spriteBatch);

		
		if(selectedRowIndex == 2 && selectedColIndex == 0)
		{
			MINUS_SYMBOL.setColor(Color.RED);
		}
		else
		{
			MINUS_SYMBOL.setColor(Color.BLUE);
		}
		MINUS_SYMBOL.setPosition((numberX - 100f) - 35f, optionsY2 - 70f);
		MINUS_SYMBOL.draw(spriteBatch);
		
		if(selectedRowIndex == 2 && selectedColIndex == 1)
		{
			PLUS_SYMBOL.setColor(Color.RED);
		}
		else
		{
			PLUS_SYMBOL.setColor(Color.BLUE);
		}
		PLUS_SYMBOL.setPosition((numberX + 100f) - 35f, optionsY2 - 70f);
		PLUS_SYMBOL.draw(spriteBatch);
		
			
			
		smallerFont.setColor(Color.DARK_GRAY);
			
		smallerFont.draw(spriteBatch, DESC1, center.x - smallerFont.getBounds(DESC1).width/2, descriptionTextY);
		smallerFont.draw(spriteBatch, DESC2, center.x - smallerFont.getBounds(DESC2).width/2, descriptionTextY - 50f);
		
	}
	
	public void handleTouched(boolean touched, float x, float y)
	{
		touchScreenCurrentlyPressed = Gdx.input.isTouched();
		if(touched)
		{
			float tempY = optionsY - largeFont.getBounds(START_GAME).height/2;
			
			if(y <= tempY && y >= (optionsY - largeFont.getBounds("A").height))
			{
				//Row 0
				if(x >= (center.x - largeFont.getBounds(START_GAME).width/2) && x <= (center.x + largeFont.getBounds(START_GAME).width/2))
				{
					startGame = true;
					return;
				}
			}
			
			tempY = optionsY1 - 70f;
			if(y >= tempY && y <= (optionsY1))
			{
				float tempx = numberX - 100f - 35f;
				//Row 1
				
				
				if(x >= tempx && x <= (tempx + 70f))
				{
					if(homeAi > 0)
					{
						homeAi--;
						return;
					}
				}
				
				tempx =  numberX + 100f - 35f;
				
				if(x >= tempx && x <= (tempx + 70f))
				{
					if(homeAi < MAX_AI)
					{
						homeAi++;
						return;
					}
				}
				
			}
			
			tempY = optionsY2 - 70f;
			if(y >= tempY && y <= (optionsY2))
			{
				float tempx = numberX - 100f - 35f;
				//Row 1
				
				
				if(x >= tempx && x <= (tempx + 70f))
				{
					if(awayAi > 0)
					{
						awayAi--;
						return;
					}
				}
				
				tempx =  numberX + 100f - 35f;
				
				if(x >= tempx && x <= (tempx + 70f))
				{
					if(awayAi < MAX_AI)
					{
						awayAi++;
						return;
					}
				}
			}
		}
	}
	
	public void handleControllerPress()
	{
		if(selectedRowIndex == 1)
		{
			if(selectedColIndex == 0 && homeAi > 0)
			{
				homeAi--;
			}
			else if(selectedColIndex == 1 && homeAi < MAX_AI)
			{
				homeAi++;
			}
			
		}
		else if(selectedRowIndex == 2)
		{
			if(selectedColIndex == 0 && awayAi > 0)
			{
				awayAi--;
			}
			else if(selectedColIndex == 1 && awayAi < MAX_AI)
			{
				awayAi++;
			}
			
		}
	}
	
	public void handleMoveX(int move)
	{
		selectedColIndex = selectedColIndex + move;
		
		if(selectedColIndex > 1)
		{
			selectedColIndex = 0;
		}
		else if(selectedColIndex < 0)
		{
			selectedColIndex = 1;
		}
	}
	
	public void handleMoveY(int move)
	{
		selectedRowIndex = selectedRowIndex - move;
		
		if(selectedRowIndex > 2)
		{
			selectedRowIndex = 0;
		}
		else if(selectedRowIndex < 0)
		{
			selectedRowIndex = 2;
		}
	}


}
