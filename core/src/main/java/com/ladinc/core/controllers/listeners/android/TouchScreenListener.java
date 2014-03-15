package com.ladinc.core.controllers.listeners.android;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.ladinc.core.controllers.controls.GamePadControls;
import com.ladinc.core.controllers.controls.TouchPadControls;

public class TouchScreenListener implements InputProcessor
{

	public TouchPadControls controls;
	private final int screenWidth;
	private final int screenHeight;
	
	public TouchScreenListener() {
		this.controls = new TouchPadControls();
		
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		
	}
	
	private void processTouch(int screenX, int screenY, int pointer, boolean touched, boolean moveEvent)
	{
		if(screenY > screenHeight/2)
		{
			//Its an action touch
			screenX = screenX - (screenWidth/4);
			if(screenX <= 0)
			{
				this.controls.setLeftButton(touched);
				if(touched)
					this.controls.setRightButton(false);
				Gdx.app.debug("TouchScreenListener",
						"setLeftButton: touched=" + String.valueOf(touched) );
				
				return;
				
			}
			
			screenX = screenX - (screenWidth/4);
			if(screenX <= 0)
			{
				this.controls.setRightButton(touched);
				if(touched)
					this.controls.setLeftButton(false);
				Gdx.app.debug("TouchScreenListener",
						"setRightButton: touched=" + String.valueOf(touched) );
				
				return;
			}
			
			screenX = screenX - (screenWidth/4);
			if(screenX <= 0)
			{
				this.controls.setReverseButton(touched);
				if(touched)
					this.controls.setAccelerateButton(false);
				Gdx.app.debug("TouchScreenListener",
						"setReverseButton: touched=" + String.valueOf(touched) );
				
				if(!moveEvent)
					this.controls.setBackStatus(touched);
				
				return;
			}
			
			screenX = screenX - (screenWidth/4);
			if(screenX <= 0)
			{
				this.controls.setAccelerateButton(touched);
				if(touched)
					this.controls.setReverseButton(false);
				Gdx.app.debug("TouchScreenListener",
						"setAccelerateButton: touched=" + String.valueOf(touched) );
				
				if(!moveEvent)
					this.controls.setConfirmStatus(touched);
				
				return;
			}
		}
		else
		{
			if(!moveEvent && (screenX > (this.screenWidth/4) && screenX < 3 * (this.screenWidth/4)))
			{
				this.controls.setStartStatus(touched);
			}
		}
	}
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) 
	{
		Gdx.app.error("TouchScreenListener",
				"touchDown: screenX=" + String.valueOf(screenX) + ", screenY=" + String.valueOf(screenY) + ", pointer=" + String.valueOf(pointer) + ", button=" + String.valueOf(button));
		
		processTouch(screenX, screenY,pointer, true, false);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Gdx.app.error("TouchScreenListener",
				"touchUp: screenX=" + String.valueOf(screenX) + ", screenY=" + String.valueOf(screenY) + ", pointer=" + String.valueOf(pointer) + ", button=" + String.valueOf(button));
		
		processTouch(screenX, screenY,pointer, false, false);
		
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Gdx.app.debug("TouchScreenListener",
				"touchDragged: screenX=" + String.valueOf(screenX) + ", screenY=" + String.valueOf(screenY) + ", pointer=" + String.valueOf(pointer));
		
		processTouch(screenX, screenY,pointer, true, true);
		
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
