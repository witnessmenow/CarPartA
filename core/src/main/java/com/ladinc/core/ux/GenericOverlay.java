package com.ladinc.core.ux;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
	
	protected float howToWinHeadingY;

	public List<MenuOptions> optionsList;
	
	public int selectedIndex = 0;
	
	public GenericOverlay(Vector2 c)
	{
		center = c;
		background = Art.getSprite(Art.GAME_DESCRIPTION_WINDOW);
		background.setPosition(center.x - background.getWidth()/2, center.y - background.getHeight()/2);
		
		largeFont = Font.fontTable.get(Font.PLAYBILL_75_BOLD);
		largeFont.setScale(1.5f);
		
		smallerFont = Font.fontTable.get(Font.PLAYBILL_50);
		
		descriptionTextY = center.y - background.getHeight()/2 + 100f;
		
		titleY = center.y + background.getHeight()/2 - 50f;
		
		optionsList = new ArrayList<MenuOptions>();
	}
}
