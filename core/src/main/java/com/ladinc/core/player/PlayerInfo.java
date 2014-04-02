package com.ladinc.core.player;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.ladinc.core.assets.Art;
import com.ladinc.core.controllers.controls.IControls;
import com.ladinc.core.screen.GameScreen;
import com.ladinc.core.utilities.Enums.Team;

public class PlayerInfo {
	public IControls controls;
	public String playerName;
	public int playerId;
	
	public Sprite identifier;
	
	public Team team;
	
	private static boolean[] ID_LIST = new boolean[GameScreen.MAX_PLAYERS];
	
	public PlayerInfo(IControls controls) {
		this.controls = controls;
		this.playerId = getNextID();
	}
	
	public PlayerInfo(IControls controls, Team team) {
		this.controls = controls;
		this.team = team;
		this.playerId = getNextID();
	}
	
	public int getNextID()
	{
		for (int i = 0; i < PlayerInfo.ID_LIST.length; i++)
		{
			if (!PlayerInfo.ID_LIST[i])
			{
				ID_LIST[i] = true;
				return i;
			}
		}
		return -1;
	}
	
	public Sprite getIdentifierSprite()
	{
		if (this.identifier == null)
		{
			this.identifier = new Sprite(Art.textureTable.get(Art.IDENTIFIER),
					101 * this.playerId, 0, 101, 100);
		}
		
		return this.identifier;
	}
	
	public void drawIndentifier(SpriteBatch spriteBatch, int PIXELS_PER_METER,
			Body carBody)
	{
		if (this.controls != null && !this.controls.isAi())
		{
			Art.updateSprite(getIdentifierSprite(), spriteBatch,
					PIXELS_PER_METER, carBody);
		}
	}
	
	public void releaseId()
	{
		if (this.playerId >= 0)
		{
			ID_LIST[this.playerId] = false;
		}
		
		if (this.identifier != null)
		{
			this.identifier = null;
		}
	}
	
}
