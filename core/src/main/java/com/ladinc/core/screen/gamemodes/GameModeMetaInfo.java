package com.ladinc.core.screen.gamemodes;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.ladinc.core.ux.DescriptionScreenInfo;

public class GameModeMetaInfo 
{
	public Sprite image;
	public String name;
	public DescriptionScreenInfo description;
	public GameMode game;
	public boolean canHandleAi;
	
	
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
		return null;
	}

}
