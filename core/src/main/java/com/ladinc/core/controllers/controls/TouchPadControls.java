package com.ladinc.core.controllers.controls;

public class TouchPadControls implements IControls
{

	private boolean acceleratePressed;
	private boolean reversePressed;
	
	private boolean leftPressed;
	private boolean rightPressed;
	
	private boolean handbrakePressed;
	
	private boolean startButtonPressed;
	private boolean confirmButtonPressed;
	private boolean backButtonPressed;
	
	public boolean active;
	
	private boolean interestedInMenuPresses = false;
	
	private void checkForActive()
	{
		if (!active)
		{
			active = true;
		}
	}
	
	public void setAccelerateButton(boolean pressed)
	{
		if (!active && pressed)
		{
			checkForActive();
		}
		
		this.acceleratePressed = pressed;
	}
	
	public void setReverseButton(boolean pressed)
	{
		if (!active && pressed)
		{
			checkForActive();
		}
		
		this.reversePressed = pressed;
	}
	
	public void setLeftButton(boolean pressed)
	{
		if (!active && pressed)
		{
			checkForActive();
		}
		
		this.leftPressed = pressed;
	}
	
	public void setHandBreakButton(boolean pressed)
	{
		if (!active && pressed)
		{
			checkForActive();
		}
		
		this.handbrakePressed = pressed;
	}
	
	public void setRightButton(boolean pressed)
	{
		if (!active && pressed)
		{
			checkForActive();
		}
		
		this.rightPressed = pressed;
	}
	
	public void setStartStatus(boolean pressed)
	{
		if (!active && pressed)
		{
			checkForActive();
		}
		
		this.startButtonPressed = pressed;
	}
	
	public void setConfirmStatus(boolean pressed)
	{
		if(interestedInMenuPresses && pressed)
		{
			if (!active)
			{
				checkForActive();
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
				checkForActive();
			}
			
			this.backButtonPressed = true;
		}
		else
		{
			this.backButtonPressed = false;
		}
	}
	
	@Override
	public boolean isActive() {
		// TODO Auto-generated method stub
		return active;
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;
		
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
		if(this.rightPressed)
			return 1;
		if(this.leftPressed)
			return -1;
		
		return 0;
	}

	@Override
	public boolean getHandbreakStatus() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getStartStatus() {
		if(this.startButtonPressed)
		{
			//Dont allow one press be considered multiple
			this.startButtonPressed = false;
			return true;
		}
		return false;
	}

	@Override
	public boolean getConfirmStatus() {
		if(this.confirmButtonPressed)
		{
			//Dont allow one press be considered multiple
			this.confirmButtonPressed = false;
			return true;
		}
		return false;
	}

	@Override
	public boolean getBackStatus() {
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

}
