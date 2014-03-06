package com.ladinc.core.controllers.controls;

public interface IControls {
	
	public boolean isActive();
	
	public void setActive(boolean active);
	
	public int getAcceleration();
	
	public float getSteering();
	
	public boolean getHandbreakStatus();
	
	public boolean getStartStatus();
	
	public boolean getConfirmStatus();
	
	public boolean getBackStatus();
	
	public void setMenuInterest(boolean set);
	
	// TODO: Add start etc
	
}
