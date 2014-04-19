package com.ladinc.core.ux;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ladinc.core.assets.Art;
import com.ladinc.core.assets.Font;

public class DescriptionScreen 
{
	private static String HOW_TO_WIN = "How To Win";
	private static String INSTRUCTIONS = "Instructions";
	private static String PRESS_ANY_BUTTON_TO_START = "Press Any Button To Start";
	private static String PRESS_ANY_BUTTON_TO_CLOSE = "Press Any Button To Close";
	
	private float skipCoolDown = 0.5f;
	
	private Sprite descriptionBackground;
	
	private BitmapFont titleFont;
	private BitmapFont headingFont;
	private BitmapFont regularFont;
	
	private Vector2 center;
	
	public DescriptionScreenInfo info;
	
	private float titleY;
	private float descriptionY;
	private float howToWinHeadingY;
	private float howToWinTextY;
	private float skipTextY;
	
	public boolean allowSkip = false;
	
	public boolean menuMode = false;
	
	public DescriptionScreen(Vector2 center, DescriptionScreenInfo info)
	{
		this.info = info;
		
		this.center = center;
		
		descriptionBackground = Art.getSprite(Art.GAME_DESCRIPTION_WINDOW);
		
		descriptionBackground.setPosition(center.x - descriptionBackground.getWidth()/2, center.y - descriptionBackground.getHeight()/2);
		
		titleFont = Font.fontTable.get(Font.PLAYBILL_75_BOLD);
		titleFont.setScale(1.5f);
		headingFont = Font.fontTable.get(Font.PLAYBILL_50_BOLD);
		headingFont.setScale(1.2f);
		regularFont = Font.fontTable.get(Font.PLAYBILL_50);
		
		titleY = center.y + descriptionBackground.getHeight()/2 - 50f;
		descriptionY = center.y + descriptionBackground.getHeight()/2 - 175f;
		howToWinHeadingY = center.y + descriptionBackground.getHeight()/2 - 250f;
		howToWinTextY = center.y + descriptionBackground.getHeight()/2 - 315f;
		
		skipTextY = center.y - descriptionBackground.getHeight()/2 + 100f;
	}
	
	public void drawDescriptionScreen(SpriteBatch spriteBatch, float delta)
	{
		
		if(!allowSkip)
		{
			skipCoolDown = skipCoolDown - delta;
			
			if(skipCoolDown <= 0.0f)
			{
				allowSkip = true;
			}
		}
		
		descriptionBackground.draw(spriteBatch);
		
		titleFont.setColor(Color.BLACK);
		titleFont.draw(spriteBatch, info.title, center.x - titleFont.getBounds(info.title).width/2, titleY);
		
		regularFont.setColor(Color.DARK_GRAY);
		regularFont.draw(spriteBatch, info.descriptionText, center.x - regularFont.getBounds(info.descriptionText).width/2, descriptionY);
		
		headingFont.setColor(Color.BLACK);
		headingFont.draw(spriteBatch, INSTRUCTIONS, center.x - headingFont.getBounds(INSTRUCTIONS).width/2, howToWinHeadingY);
		
		for(int i = 0; i < info.howToWinText.size(); i++)
		{
			float tempY = howToWinTextY - (50f*(i));
			regularFont.draw(spriteBatch, info.howToWinText.get(i), center.x - regularFont.getBounds(info.howToWinText.get(i)).width/2, tempY);
		}
		

		if(!menuMode)
		{
			headingFont.draw(spriteBatch, PRESS_ANY_BUTTON_TO_START, center.x - headingFont.getBounds(PRESS_ANY_BUTTON_TO_START).width/2, skipTextY);
		}
		else
		{
			headingFont.draw(spriteBatch, PRESS_ANY_BUTTON_TO_CLOSE, center.x - headingFont.getBounds(PRESS_ANY_BUTTON_TO_CLOSE).width/2, skipTextY);
		}
		
	}

}
