package com.ladinc.core.controllers.controls;

import com.badlogic.gdx.math.Vector2;
import com.ladinc.core.controllers.listeners.desktop.KeyboardAndMouseListener;

public class KeyboardControls implements IControls {
	private final Vector2 rotationReferencePoint;
	private final Vector2 rotationMovementPoint;
	
	private final Vector2 actualRotationPoint;
	
	private final Vector2 movement;
	
	private boolean acceleratePressed;
	private boolean reversePressed;
	
	private boolean leftPressed;
	private boolean rightPressed;
	
	private boolean handbrakePressed;
	
	private boolean startButtonPressed;
	private boolean confirmButtonPressed;
	private boolean backButtonPressed;
	
	private boolean interestedInMenuPresses = false;
	
	public KeyboardAndMouseListener listener;
	
	public KeyboardControls(KeyboardAndMouseListener listen) {
		this.listener = listen;
		this.rotationReferencePoint = new Vector2(0, 0);
		this.rotationMovementPoint = new Vector2(0, 0);
		this.actualRotationPoint = new Vector2(0, 0);
		this.movement = new Vector2(0, 0);
	}
	
	public boolean active;
	
	private void checkForActive()
	{
		if (!active)
		{
			active = true;
		}
	}
	
	public void setRotationReferencePoint(float x, float y)
	{
		checkForActive();
		
		rotationReferencePoint.x = x;
		rotationReferencePoint.y = y;
	}
	
	public void setRotationMovementPoint(float x, float y)
	{
		checkForActive();
		
		rotationMovementPoint.x = x;
		rotationMovementPoint.y = y;
	}
	
	public void setMovementX(float x)
	{
		checkForActive();
		
		movement.x = x;
	}
	
	public void setMovementY(float y)
	{
		checkForActive();
		
		movement.y = y;
	}
	
	public Vector2 getMovementInput()
	{
		// TODO Auto-generated method stub
		return movement;
	}
	
	public Vector2 getRotationInput()
	{
		actualRotationPoint.x = rotationMovementPoint.x;
		// actualRotationPoint.y = (rotationMovementPoint.y)*(-1);
		actualRotationPoint.y = (rotationMovementPoint.y);
		
		return actualRotationPoint;
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
	
	public boolean getDivePressed()
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean isActive()
	{
		// TODO Auto-generated method stub
		return this.active;
	}
	
	public boolean isRotationRelative()
	{
		// TODO Auto-generated method stub
		return true;
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
		return this.handbrakePressed;
	}
	
	@Override
	public void setActive(boolean active) 
	{
		this.active = active;
		
	}

	public void setStartStatus(boolean pressed)
	{
		checkForActive();
		
		this.startButtonPressed = pressed;
	}
	
	public void setConfirmStatus(boolean pressed)
	{
		if(interestedInMenuPresses && pressed)
		{
			checkForActive();
			
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
			checkForActive();
			
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
}