package com.ladinc.core.screen.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ladinc.core.CarPartA;
import com.ladinc.core.assets.Art;
import com.ladinc.core.screen.gamemodes.teamselect.TeamSelectScreen;

public class GameModeSelect implements Screen
{

	private final CarPartA game;
	private final SpriteBatch spriteBatch;
	OrthographicCamera camera;
	Sprite messageSprite;
	
	public GameModeSelect(CarPartA game) {
		this.game = game;
		spriteBatch = new SpriteBatch();
		
		messageSprite = Art.getSprite(Art.DEMO_MESSAGE);
		messageSprite.setPosition(0, 0);
		
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, 1920, 1080);
	}
	
	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0, 0, 0f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		spriteBatch.setProjectionMatrix(camera.combined);
		
		spriteBatch.begin();
		messageSprite.draw(spriteBatch);
		spriteBatch.end();
		
		if ((this.game.controllerManager.checkForNewPlayer() != null)
				|| (Gdx.input.isTouched()))
		{
			startNewScreen();
		}
		
	}
	
	private void startNewScreen()
	{
		game.setScreen(new TeamSelectScreen(game));
		dispose();
	}
	
	@Override
	public void resize(int width, int height)
	{
		// TODO Auto-generated method stub
	}
	
	@Override
	public void show()
	{
		this.game.controllerManager.resetControllers();
	}
	
	@Override
	public void hide()
	{
		// TODO Auto-generated method stub
	}
	
	@Override
	public void pause()
	{
		// TODO Auto-generated method stub
	}
	
	@Override
	public void resume()
	{
		// TODO Auto-generated method stub
	}
	
	@Override
	public void dispose()
	{
		// TODO: DISPOSE OF STUFF
		spriteBatch.dispose();
	}
}
