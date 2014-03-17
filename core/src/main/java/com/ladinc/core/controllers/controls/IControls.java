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
	
	public boolean getExtraButton1Status();
	
	public boolean getExtraButton2Status();
	
	public void setMenuInterest(boolean set);
	
	public boolean isAi();
	
	// TODO: Add start etc
	
}
