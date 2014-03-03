package com.ladinc.core.controllers.mapping;

import com.badlogic.gdx.Input.Keys;

public class KeyboardMapper {
	public static final String id = "Desktop Controls";
	
	public static boolean isAccelerateButton(int keyCode)
	{
		return keyCode == Keys.W || keyCode == Keys.UP;
	}
	
	public static boolean isDeaccelerateButton(int keyCode)
	{
		return keyCode == Keys.S || keyCode == Keys.DOWN;
	}
	
	public static boolean isRotateLeftButton(int keyCode)
	{
		return keyCode == Keys.A || keyCode == Keys.LEFT;
	}
	
	public static boolean isRotateRightButton(int keyCode)
	{
		return keyCode == Keys.D || keyCode == Keys.RIGHT;
	}
	
	public static boolean isHandbrakeBurron(int keyCode)
	{
		return keyCode == Keys.SPACE;
	}
}