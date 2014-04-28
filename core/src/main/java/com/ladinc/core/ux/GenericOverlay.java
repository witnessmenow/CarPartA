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

public class GenericOverlay 
{
	protected Vector2 center;
	protected Sprite background;
	
	protected BitmapFont largeFont;
	protected BitmapFont smallerFont;
	
	protected float descriptionTextY;
	protected float titleY;
	protected float optionsY;
	
	protected float howToWinHeadingY;

	public List<MenuOptions> optionsList;
	
	public int selectedIndex = -1;
	
	private int screenHeight;
	private int screenWidth;
	
	protected boolean touchScreenCurrentlyPressed;
	protected float touchedX;
	protected float touchedY;
	
	public boolean optionPressed = false;
	
	public GenericOverlay(Vector2 c)
	{
		center = c;
		screenWidth = (int) c.x *2;
		screenHeight = (int) c.y *2;
		background = Art.getSprite(Art.GAME_DESCRIPTION_WINDOW);
		background.setPosition(center.x - background.getWidth()/2, center.y - background.getHeight()/2);
		
		largeFont = Font.fontTable.get(Font.PLAYBILL_75_BOLD);
		largeFont.setScale(1.5f);
		
		smallerFont = Font.fontTable.get(Font.PLAYBILL_50);
		
		descriptionTextY = center.y - background.getHeight()/2 + 120f;
		
		optionsY = center.y + background.getHeight()/2 - 175f;
		
		titleY = center.y + background.getHeight()/2 - 50f;
		
		optionsList = new ArrayList<MenuOptions>();
	}
	
	public void drawOverlay(SpriteBatch spriteBatch, float delta)
	{
		
		background.draw(spriteBatch);
		
		if(optionsList != null)
		{
			for(int i = 0; i < optionsList.size(); i++)
			{
				float tempY = optionsY - (i * (120f));
				
				if(i == selectedIndex)
				{
					largeFont.setColor(Color.RED);
				}
				else
				{
					largeFont.setColor(Color.BLUE);
					
					if(touchScreenCurrentlyPressed)
					{
						if(touchedY <= tempY && touchedY >= (tempY - largeFont.getBounds("A").height))
						{
							selectedIndex = i;
							optionPressed = true;
						}
					}
				}
				
				
				largeFont.draw(spriteBatch, optionsList.get(i).title, center.x - largeFont.getBounds(optionsList.get(i).title).width/2, tempY);
			}
			
			smallerFont.setColor(Color.DARK_GRAY);
			
			if(this.selectedIndex >= 0 && this.selectedIndex < optionsList.size())
			{
				for(int j = 0; j < optionsList.get(selectedIndex).descriptionText.size(); j++)
				{
					String tmp = optionsList.get(selectedIndex).descriptionText.get(j);
					float tempY = descriptionTextY - (50f*(j));
					smallerFont.draw(spriteBatch, tmp, center.x - smallerFont.getBounds(tmp).width/2, tempY);
				}
			}
		}
		
	}
	
	public void getTouched(boolean touched, float x, float y)
	{
		touchScreenCurrentlyPressed = Gdx.input.isTouched();
		touchedX = x;
		touchedY = y;
	}
	
	public void handleMovement(int movement)
	{
		if(optionsList != null)
		{
			int temp = selectedIndex - movement;
			
			if(temp < 0)
			{
				temp = optionsList.size() - 1;
			}
			else if(temp >= optionsList.size())
			{
				temp = 0;
			}
			
			selectedIndex = temp;
		}
	}
}
