package com.ladinc.core.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainMenuScreen implements Screen{
	private Game game;
	private Texture texture;
	private SpriteBatch batch;
	float elapsed;
	
	public MainMenuScreen(Game game){
		this.game = game;
		batch = new SpriteBatch();
		texture = new Texture(Gdx.files.internal("libgdx-logo.png"));
	}
	
	@Override
	public void render(float delta) {
		draw(delta);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	private void draw(float delta){
		elapsed += delta;
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(texture, 100+100*(float)Math.cos(elapsed), 100+25*(float)Math.sin(elapsed));
		batch.end();
	}
}