package com.ladinc.core.screen.gamemodes;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.ladinc.core.assets.Art;
import com.ladinc.core.ux.DescriptionScreenInfo;

public class GameModeMetaInfo 
{
	public Sprite image;
	public String name;
	public DescriptionScreenInfo description;
	public GameMode game;
	public boolean canHandleAi;
	
	public boolean isSelected;
	
	public static final int IMAGE_WIDTH = 250;
	public static final int IMAGE_HEIGHT = 175;
	public static HashMap<GameMode, Sprite> imageTable = new HashMap<GameMode, Sprite>();
	
	public GameModeMetaInfo(String n, DescriptionScreenInfo d, GameMode g, boolean ai)
	{
		this.name = n;
		this.image = GameModeMetaInfo.getSprite(g);
		this.description = d;
		this.game = g;
		this.canHandleAi = ai;
	}
	public static enum GameMode {
		Soccar, CarPool, Amazing, Mower, Pong, King, Hill, Painter, Capture
	};
	
	public static Sprite getSprite(GameMode gm)
	{
		//Image may already by in hashmap
		if(!imageTable.containsKey(gm))
		{
			int x = 0;
			int y = 0;
			
			switch (gm) 
			{
				case Amazing:
					break;
				case Capture:
					break;
				case CarPool:
					break;
				case Hill:
					break;
				case King:
					break;
				case Mower:
					break;
				case Painter:
					break;
				case Pong:
					break;
				case Soccar:
					x = 0;
					y = 0;
					break;
				default:
					break;
	
			}
			
			imageTable.put(gm, getSpriteFromTexture(x,y));
		}
		
		return imageTable.get(gm);
	}
	
	private static Sprite getSpriteFromTexture(int x, int y)
	{
		return new Sprite(Art.textureTable.get(Art.GAME_MODE_IMAGES), x * IMAGE_WIDTH, y * IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);
	}

	
	private static Sprite selectedGameModeSprite = null;
	
	public static Sprite getSelectedGameModeSprite()
	{
		if(selectedGameModeSprite == null)
		{
			selectedGameModeSprite = Art.getSprite(Art.SELECTED_GAME_MODE_INDICATOR);
		}
		
		return selectedGameModeSprite;
	}
}
