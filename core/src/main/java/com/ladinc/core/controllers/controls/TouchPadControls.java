package com.ladinc.core.controllers.controls;

public class TouchPadControls implements IControls
{

	private boolean acceleratePressed;
	private boolean reversePressed;
	
	private boolean leftPressed;
	private boolean rightPressed;
	
	private boolean handbrakePressed;
	
	public boolean active;
	
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getConfirmStatus() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getBackStatus() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setMenuInterest(boolean set) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isAi() {
		// TODO Auto-generated method stub
		return false;
	}

}
