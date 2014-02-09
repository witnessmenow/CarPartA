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

public class MyControllerManager {
	
	public ArrayList<IControls> inActiveControls;
	public ArrayList<IControls> controls;
	
	public MyControllerManager() {
		inActiveControls = new ArrayList<IControls>();
		controls = new ArrayList<IControls>();
		
		setUpControls();
	}
	
	private void setUpControls()
	{
		this.controls.clear();
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
	
	public boolean checkForNewControllers()
	{
		ArrayList<IControls> tempControllers = (ArrayList<IControls>) inActiveControls
				.clone();
		boolean foundNew = false;
		
		for (IControls cont : tempControllers)
		{
			if (cont.isActive())
			{
				if (!this.controls.contains(cont))
				{
					// cont.setIdentifier(getNextAvailableIdentifer());
					this.controls.add(cont);
					this.inActiveControls.remove(cont);
					foundNew = true;
				}
			}
		}
		
		return foundNew;
	}
}