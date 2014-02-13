package com.ladinc.core.controllers.controls;

import com.badlogic.gdx.math.Vector2;
import com.ladinc.core.controllers.listeners.desktop.KeyboardAndMouseListener;

public class KeyboardControls implements IControls {
	private final Vector2 rotationReferencePoint;
	private final Vector2 rotationMovementPoint;
	
	private final Vector2 actualRotationPoint;
	
	private final Vector2 movement;
	
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
	public int getAcceleration() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getSteering() {
		// TODO Auto-generated method stub
		return 0;
	}
}