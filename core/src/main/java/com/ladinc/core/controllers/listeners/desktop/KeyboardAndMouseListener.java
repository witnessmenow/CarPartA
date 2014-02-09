package com.ladinc.core.controllers.listeners.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.ladinc.core.controllers.controls.KeyboardControls;
import com.ladinc.core.controllers.mapping.KeyboardMapper;

public class KeyboardAndMouseListener implements InputProcessor {
	
	public KeyboardControls controls;
	
	public KeyboardAndMouseListener() {
		this.controls = new KeyboardControls(this);
	}
	
	@Override
	public boolean keyDown(int keyCode)
	{
		Gdx.app.debug("KeyBoardAndMouseListener",
				"keyDown: keycode=" + String.valueOf(keyCode));
		if (KeyboardMapper.isAccelerateButton(keyCode))
		{
			// Increase acceleration
		}
		else if (KeyboardMapper.isDeaccelerateButton(keyCode))
		{
			// decrease acceleration
		}
		
		if (KeyboardMapper.isRotateLeftButton(keyCode))
		{
			// Rotate car left
		}
		else if (KeyboardMapper.isRotateRightButton(keyCode))
		{
			// Rotate car right
		}
		
		return true;
	}
	
	@Override
	public boolean keyUp(int keycode)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean keyTyped(char character)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean scrolled(int amount)
	{
		// TODO Auto-generated method stub
		// Zoom the camera in and out - Set max and min amount
		return false;
	}
}