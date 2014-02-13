package com.ladinc.core.player;

import com.ladinc.core.controllers.controls.IControls;

public class PlayerInfo 
{
	public IControls controls;
	public String playerName;
	public int playerId;
	public Team team;

	public static enum Team{home, away}
	
	private static boolean[] ID_LIST = new boolean[8];
	
	public PlayerInfo(IControls controls)
	{
		this.controls = controls;
		this.playerId = getNextID();
	}
	
	public int getNextID()
	{
		for(int i = 0; i < PlayerInfo.ID_LIST.length; i++)
		{
			if(!PlayerInfo.ID_LIST[i])
			{
				return i;
			}
		}
		return -1;
	}
}
