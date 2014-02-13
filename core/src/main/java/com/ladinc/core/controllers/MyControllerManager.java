package com.ladinc.core.controllers;

import java.util.ArrayList;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.ladinc.core.controllers.controls.IControls;
import com.ladinc.core.controllers.listeners.desktop.KeyboardAndMouseListener;
import com.ladinc.core.controllers.listeners.desktop.XboxListener;
import com.ladinc.core.player.PlayerInfo;
import com.ladinc.core.player.PlayerInfo.Team;

public class MyControllerManager {
	
	public ArrayList<IControls> inActiveControls;
	public ArrayList<PlayerInfo> players;
	
	public MyControllerManager() {
		inActiveControls = new ArrayList<IControls>();
		players = new ArrayList<PlayerInfo>();
		
		setUpControls();
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
			inActiveControls.add(keyboardAndMouseListener.controls);
			Gdx.input.setInputProcessor(keyboardAndMouseListener);
		}
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
				inActiveControls.add(xboxListener.controls);
				
				break;
			case Android:
				Gdx.app.debug("ControllerManager",
						"addControllerToList - Android");
				if (Ouya.runningOnOuya)
				{
					// Gdx.app.debug("ControllerManager",
					// "Added Listener for Ouya Controller");
					//
					// OuyaListener ouyaListener = new OuyaListener();
					// controller.addListener(ouyaListener);
					//
					// inActiveControls.add(ouyaListener.controls);
				}
				break;
			case WebGL:
				Gdx.app.debug("ControllerManager",
						"addControllerToList - WebGL/HTML5");
				break;
		}
	}
	
	private boolean checkIsControllerAlreadyActive(IControls cont)
	{
		if(this.players != null)
		{
			for(PlayerInfo player : this.players)
			{
				if(player.controls == cont)
					return true;
			}
		}
		
		return false;
	}
	
	public boolean checkForNewControllers()
	{
		ArrayList<IControls> tempControllers = (ArrayList<IControls>) inActiveControls
				.clone();
		boolean foundNew = false;
		
		for (IControls cont : tempControllers)
		{
			if (cont.isActive())
			{
				if (!checkIsControllerAlreadyActive(cont))
				{
					// cont.setIdentifier(getNextAvailableIdentifer());
					this.players.add(new PlayerInfo(cont, Team.home));
					this.inActiveControls.remove(cont);
					foundNew = true;
				}
			}
		}
		
		return foundNew;
	}
}