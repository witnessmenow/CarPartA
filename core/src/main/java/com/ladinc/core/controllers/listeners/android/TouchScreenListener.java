package com.ladinc.core.controllers.listeners.android;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

public class TouchScreenListener implements InputProcessor{

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
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Gdx.app.error("TouchScreenListener",
				"touchUp: screenX=" + String.valueOf(screenX) + ", screenY=" + String.valueOf(screenY) + ", pointer=" + String.valueOf(pointer) + ", button=" + String.valueOf(button));
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Gdx.app.error("TouchScreenListener",
				"touchUp: screenX=" + String.valueOf(screenX) + ", screenY=" + String.valueOf(screenY) + ", pointer=" + String.valueOf(pointer));
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
