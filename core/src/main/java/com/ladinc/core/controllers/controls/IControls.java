package com.ladinc.core.controllers.controls;

public interface IControls {
	
	public boolean isActive();
	
	public void setActive(boolean active);
	
	public int getAcceleration();
	
	public float getSteering();
	
	public boolean getHandbreakStatus();
	
	// TODO: Add start etc
	
}
