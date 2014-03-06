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
		controls.active = true;
		Gdx.app.debug("KeyBoardAndMouseListener",
				"keyDown: keycode=" + String.valueOf(keyCode));

		handleButtonPresses(keyCode, true);
		
		return true;
	}
	
	@Override
	public boolean keyUp(int keyCode)
	{
		Gdx.app.debug("KeyBoardAndMouseListener",
				"keyUp: keycode=" + String.valueOf(keyCode));
		handleButtonPresses(keyCode, false);
		return true;
	}
	
	private void handleButtonPresses(int buttonId, Boolean set)
	{
		if (KeyboardMapper.isAccelerateButton(buttonId))
		{
			controls.setAccelerateButton(set);
		}
		else if(KeyboardMapper.isDeaccelerateButton(buttonId))
		{
			controls.setReverseButton(set);
		}
		
		if(KeyboardMapper.isHandbrakeButton(buttonId))
		{
			controls.setHandBreakButton(set);
		}
		
		if (KeyboardMapper.isRotateLeftButton(buttonId))
		{
			this.controls.setLeftButton(set);
		}
		else if (KeyboardMapper.isRotateRightButton(buttonId))
		{
			this.controls.setRightButton(set);
		}
		
		if(KeyboardMapper.isStartButton(buttonId))
		{
			this.controls.setStartStatus(set);
		}
		else if (KeyboardMapper.isConfirmButton(buttonId))
		{
			this.controls.setConfirmStatus(set);
		}
		else if (KeyboardMapper.isBackButton(buttonId))
		{
			this.controls.setBackStatus(set);
		}
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