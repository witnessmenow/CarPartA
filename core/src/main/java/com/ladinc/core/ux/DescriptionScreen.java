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
	private Sprite descriptionBackground;
	
	private BitmapFont titleFont;
	private BitmapFont headingFont;
	private BitmapFont regularFont;
	
	private Vector2 center;
	
	private DescriptionScreenInfo info;
	
	private float titleY;
	
	public DescriptionScreen(Vector2 center, DescriptionScreenInfo info)
	{
		this.info = info;
		
		descriptionBackground = Art.getSprite(Art.GAME_DESCRIPTION_WINDOW);
		
		descriptionBackground.setPosition(center.x - descriptionBackground.getWidth()/2, center.y - descriptionBackground.getHeight()/2);
		
		titleFont = Font.fontTable.get(Font.STENCIL_75_BOLD);
		headingFont = Font.fontTable.get(Font.STENCIL_50_BOLD);
		regularFont = Font.fontTable.get(Font.STENCIL_50);
		
		titleY = center.y + descriptionBackground.getHeight()/2 - 50f;
	}
	
	public void drawDescriptionScreen(SpriteBatch spriteBatch)
	{
		descriptionBackground.draw(spriteBatch);
		
		titleFont.setColor(Color.BLACK);
		titleFont.draw(spriteBatch, info.title, center.x - titleFont.getBounds(info.title).width, titleY);
		
	}

}
