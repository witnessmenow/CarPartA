package com.ladinc.core.controllers;

import java.util.ArrayList;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.ladinc.core.ai.SimpleAi;
import com.ladinc.core.controllers.controls.IControls;
import com.ladinc.core.controllers.listeners.android.OuyaListener;
import com.ladinc.core.controllers.listeners.android.TouchScreenListener;
import com.ladinc.core.controllers.listeners.desktop.KeyboardAndMouseListener;
import com.ladinc.core.controllers.listeners.desktop.XboxListener;
import com.ladinc.core.player.PlayerInfo;
import com.ladinc.core.utilities.Enums.Team;

public class MyControllerManager {
	
	public ArrayList<IControls> inActiveControls;
	public ArrayList<IControls> allControls;
	private final ArrayList<PlayerInfo> players;
	private final ArrayList<SimpleAi> ai;
	
	public Boolean hasTouchControls = false;
	
	public MyControllerManager() {
		inActiveControls = new ArrayList<IControls>();
		allControls = new ArrayList<IControls>();
		players = new ArrayList<PlayerInfo>();
		ai = new ArrayList<SimpleAi>();
		
		setUpControls();
	}
	
	public ArrayList<PlayerInfo> getPlayers()
	{
		return this.players;
	}
	
	public void removePlayerFromList(PlayerInfo p)
	{
		p.releaseId();
		this.players.remove(p);
	}
	
	private void setUpControls()
	{
		this.players.clear();
		this.inActiveControls.clear();
		
		for (Controller controller : Controllers.getControllers())
		{
			Gdx.app.debug("ControllerManager",
					"setUpControls - " + controller.getName());
			addControllerToList(controller);
			
		}
		
		/*
		 * Could we use stuff like this instead of application type?
		 * 
		 * boolean hardwareKeyboard = Gdx.input
		 * .isPeripheralAvailable(Peripheral.HardwareKeyboard);
		 */
		if (Gdx.app.getType() == ApplicationType.Desktop)
		{
			Gdx.app.debug("ControllerManager", "keyboard");
			KeyboardAndMouseListener keyboardAndMouseListener = new KeyboardAndMouseListener();
			addNewController(keyboardAndMouseListener.controls);
			Gdx.input.setInputProcessor(keyboardAndMouseListener);
			
		}
		else if (Gdx.app.getType() == ApplicationType.Android
				&& !Ouya.runningOnOuya)
		{
			TouchScreenListener tsl = new TouchScreenListener();
			addNewController(tsl.controls);
			Gdx.input.setInputProcessor(tsl);
			
			hasTouchControls = true;
		}
	}
	
	private void addNewController(IControls cont)
	{
		this.inActiveControls.add(cont);
		this.allControls.add(cont);
	}
	
	public void addControllerToList(Controller controller)
	{
		switch (Gdx.app.getType())
		{
			case Desktop:
				Gdx.app.debug("ControllerManager",
						"addControllerToList - Desktop");
				
				Gdx.app.debug("ControllerManager",
						"Added Listener for Windows Xbox Controller");
				
				XboxListener xboxListener = new XboxListener();
				controller.addListener(xboxListener);
				addNewController(xboxListener.controls);
				
				break;
			case Android:
				Gdx.app.debug("ControllerManager",
						"addControllerToList - Android");
				if (Ouya.runningOnOuya)
				{
					Gdx.app.debug("ControllerManager",
							"Added Listener for Ouya Controller");
					
					OuyaListener ouyaListener = new OuyaListener();
					controller.addListener(ouyaListener);
					addNewController(ouyaListener.controls);
				}
				break;
			case WebGL:
				Gdx.app.debug("ControllerManager",
						"addControllerToList - WebGL/HTML5");
				break;
			default:
				Gdx.app.debug("ControllerManager", "Format not supported");
				break;
		}
	}
	
	private boolean checkIsControllerAlreadyActive(IControls cont)
	{
		if (this.players != null)
		{
			for (PlayerInfo player : this.players)
			{
				if (player.controls == cont)
					return true;
			}
		}
		
		return false;
	}
	
	public void resetActiveStateOfControllers()
	{
		ArrayList<PlayerInfo> tempPlayersToRemove = new ArrayList<PlayerInfo>();
		IControls tempCont;
		for (PlayerInfo player : players)
		{
			tempCont = player.controls;
			tempCont.setActive(false);
			if (!player.controls.isAi())
			{
				this.inActiveControls.add(tempCont);
			}
			tempPlayersToRemove.add(player);
		}
		
		for (PlayerInfo player : tempPlayersToRemove)
		{
			removePlayerFromList(player);
		}
		
	}
	
	public void resetStateOfPlayer(PlayerInfo player)
	{
		if (player != null && players.contains(player))
		{
			player.controls.setActive(false);
			if (!player.controls.isAi())
			{
				this.inActiveControls.add(player.controls);
			}
			removePlayerFromList(player);
		}
		
	}
	
	public PlayerInfo checkForNewPlayer()
	{
		@SuppressWarnings("unchecked")
		ArrayList<IControls> tempControllers = (ArrayList<IControls>) inActiveControls
				.clone();
		
		for (IControls cont : tempControllers)
		{
			if (cont.isActive())
			{
				if (!checkIsControllerAlreadyActive(cont))
				{
					// cont.setIdentifier(getNextAvailableIdentifer());
					
					// Setting new player is mutating the object that is passed
					// as a reference
					PlayerInfo newPlayer = new PlayerInfo(cont, Team.Home);
					this.players.add(newPlayer);
					this.inActiveControls.remove(cont);
					return newPlayer;
				}
			}
		}
		
		return null;
	}
	
	public PlayerInfo checkPlayersForStartPress()
	{
		for (PlayerInfo player : players)
		{
			if (player.controls.getStartStatus())
				return player;
		}
		
		return null;
	}
	
	public void setMenuInterest(boolean set)
	{
		for (PlayerInfo player : players)
		{
			player.controls.setMenuInterest(set);
		}
		
	}
	
	public ArrayList<SimpleAi> getAi()
	{
		return this.ai;
	}
	
	public void addAi(SimpleAi ai, Team team)
	{
		this.ai.add(ai);
		PlayerInfo newPlayer = new PlayerInfo(ai, team);
		this.players.add(newPlayer);
		
	}
}