package com.ladinc.core.player;

import com.ladinc.core.controllers.controls.IControls;
import com.ladinc.core.utilities.Enums.Team;

public class PlayerInfo 
{
	public IControls controls;
	public String playerName;
	public int playerId;
	public Team team;

	
	private static boolean[] ID_LIST = new boolean[8];
	
	public PlayerInfo(IControls controls)
	{
		this.controls = controls;
		this.playerId = getNextID();
	}
	
	public PlayerInfo(IControls controls, Team team)
	{
		this.controls = controls;
		this.team = team;
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
