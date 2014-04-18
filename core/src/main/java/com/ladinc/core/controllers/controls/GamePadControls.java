package com.ladinc.core.controllers.controls;

import com.badlogic.gdx.math.Vector2;
import com.ladinc.core.controllers.listeners.GenericControllerListener;

public class GamePadControls implements IControls {
	
	private final float TRIGGER_DEADZONE = 0.350f;
	
	private final Vector2 leftMovement;
	
	private final GenericControllerListener listener;
	
	private final Vector2 rightMovement;
	
	private boolean handbreakPressed;
	private boolean acceleratePressed;
	private boolean reversePressed;
	
	public boolean active;
	
	private boolean startButtonPressed;
	private boolean confirmButtonPressed;
	private boolean backButtonPressed;
	
	private boolean extraButton1Pressed;
	private boolean extraButton2Pressed;
	
	private boolean interestedInMenuPresses = false;
	
	public GamePadControls(GenericControllerListener listen) {
		this.listener = listen;
		this.leftMovement = new Vector2(0, 0);
		this.rightMovement = new Vector2(0, 0);
	}
	
	private void activateController()
	{
		active = true;
	}
	
	public static enum AnalogStick {
		left, right
	};
	
	public void setAnalogMovementX(AnalogStick stick, float x)
	{
		switch (stick)
		{
			case left:
				leftMovement.x = processAndActivate(x, TRIGGER_DEADZONE);
				break;
			case right:
				rightMovement.x = processAndActivate(x, TRIGGER_DEADZONE);
				break;
		
		}
	}
	
	public void setAnalogMovementY(AnalogStick stick, float y)
	{
		// inverting direction so up is up
		
		switch (stick)
		{
			case left:
				leftMovement.y = (-1)
						* (processAndActivate(y, TRIGGER_DEADZONE));
				break;
			case right:
				rightMovement.y = (-1)
						* (processAndActivate(y, TRIGGER_DEADZONE));
				break;
		
		}
	}
	
	public float processAndActivate(float movement, float deadzone)
	{
		float processedMovement = processMovment(movement, deadzone);
		
		if (!active && processedMovement > 0)
		{
			activateController();
		}
		
		return processedMovement;
	}
	
	public float processMovment(float movement, float deadzone)
	{
		// Overall power ignoring direction
		float power = movement;
		
		if (movement < 0)
			power = movement * (-1);
		
		if (power <= deadzone)
		{
			// The direction being moved is not greater than the deadzone,
			// ignoring movement
			return 0f;
		}
		else
		{
			// if(!active)
			// activateController();
			
			// have a meaningful value for X
			float movementAbovePower = power - deadzone;
			float availablePower = 1f - deadzone;
			movementAbovePower = movementAbovePower / availablePower;
			
			if (movement > 0)
			{
				return movementAbovePower;
			}
			else
			{
				// convert result to take into account direction
				return (-1) * movementAbovePower;
			}
		}
		
	}
	
	public void setAccelerateButton(boolean pressed)
	{
		if (!active && pressed)
		{
			activateController();
		}
		
		this.acceleratePressed = pressed;
	}
	
	public void setReverseButton(boolean pressed)
	{
		if (!active && pressed)
		{
			activateController();
		}
		
		this.reversePressed = pressed;
	}
	
	public void setHandBreakButton(boolean pressed)
	{
		if (!active && pressed)
		{
			activateController();
		}
		
		this.handbreakPressed = pressed;
	}
	
	@Override
	public boolean isActive()
	{
		return this.active;
	}

	@Override
	public int getAcceleration() 
	{
		if(this.acceleratePressed)
			return 1;
		if(this.reversePressed)
			return -1;
		
		return 0;
	}

	@Override
	public float getSteering() 
	{
		return leftMovement.x;
	}

	@Override
	public boolean getHandbreakStatus() 
	{
		// TODO Auto-generated method stub
		return this.handbreakPressed;
	}

	@Override
	public void setActive(boolean active) 
	{
		this.active = active;
		
	}

	public void setStartStatus(boolean pressed)
	{
		if (!active && pressed)
		{
			activateController();
		}
		
		this.startButtonPressed = pressed;
	}
	
	public void setConfirmStatus(boolean pressed)
	{
		if(interestedInMenuPresses && pressed)
		{
			if (!active)
			{
				activateController();
			}
			
			this.confirmButtonPressed = true;
		}
		else
		{
			this.confirmButtonPressed = false;
		}
	}
	
	public void setBackStatus(boolean pressed)
	{
		if(interestedInMenuPresses && pressed)
		{
			if (!active)
			{
				activateController();
			}
			
			this.backButtonPressed = true;
		}
		else
		{
			this.backButtonPressed = false;
		}
	}
	
	@Override
	public boolean getStartStatus() 
	{
		if(this.startButtonPressed)
		{
			//Dont allow one press be considered multiple
			this.startButtonPressed = false;
			return true;
		}
		return false;
	}

	@Override
	public boolean getConfirmStatus() 
	{
		if(this.confirmButtonPressed)
		{
			//Dont allow one press be considered multiple
			this.confirmButtonPressed = false;
			return true;
		}
		return false;
	}

	@Override
	public boolean getBackStatus() 
	{
		if(this.backButtonPressed)
		{
			//Dont allow one press be considered multiple
			this.backButtonPressed = false;
			return true;
		}
		return false;
	}

	@Override
	public void setMenuInterest(boolean set) {
		this.interestedInMenuPresses = set;
		
	}

	@Override
	public boolean isAi() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean getExtraButton1Status() 
	{
		if(this.extraButton1Pressed)
		{
			//Dont allow one press be considered multiple
			this.extraButton1Pressed = false;
			return true;
		}
		return false;
	}

	public void setExtraButton1Pressed(boolean extraButton1Pressed) 
	{
		if (!active && extraButton1Pressed)
		{
			activateController();
		}
		
		this.extraButton1Pressed = extraButton1Pressed;
		
	}

	@Override
	public boolean getExtraButton2Status() 
	{
		if(this.extraButton2Pressed)
		{
			//Dont allow one press be considered multiple
			this.extraButton2Pressed = false;
			return true;
		}
		return false;
	}

	public void setExtraButton2Pressed(boolean extraButton2Pressed) 
	{
		
		if (!active && extraButton2Pressed)
		{
			activateController();
		}
		
		this.extraButton2Pressed = extraButton2Pressed;
	}

	@Override
	public int getMenuXDireciton() 
	{
		if(leftMovement.x > 0)
		{
			return 1;
		}
		else if (leftMovement.x < 0)
		{
			return -1;
		}

		return 0;
	}

	@Override
	public int getMenuYDireciton() 
	{
		if(leftMovement.y > 0)
		{
			return 1;
		}
		else if (leftMovement.y < 0)
		{
			return -1;
		}
		
		return 0;
	}
	
}
