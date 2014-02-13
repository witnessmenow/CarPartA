package com.ladinc.core.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ladinc.core.CarPartA;

public class MainMenuScreen implements Screen {
	private final CarPartA game;
	private final SpriteBatch batch;
	BitmapFont font;
	OrthographicCamera camera;
	
	public MainMenuScreen(CarPartA game) {
		this.game = game;
		batch = new SpriteBatch();
		font = new BitmapFont();
	}
	
	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		font.draw(batch, "Welcome to Car Part A!!! ", 100, 150);
		font.draw(batch, "Click anywhere to begin!", 100, 100);
		batch.end();
		
		this.game.controllerManager.checkForNewControllers();
		
		if (Gdx.input.isTouched())
		{
			game.setScreen(new DemoScreen(game));
			dispose();
		}
	}
	
	@Override
	public void resize(int width, int height)
	{
		// TODO Auto-generated method stub
	}
	
	@Override
	public void show()
	{
		// TODO Auto-generated method stub
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
		// batch.dispose();
	}
}