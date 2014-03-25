package com.ladinc.core.screen.gamemodes.painter;

import com.ladinc.core.CarPartA;
import com.ladinc.core.assets.Art;
import com.ladinc.core.screen.gamemodes.GenericScreen;
import com.ladinc.core.screen.gamemodes.IGenericLayout;

public class PainterScreen extends GenericScreen
{
	private PainterLayout painterLayout;
	
	public PainterScreen(CarPartA game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void preCarRender(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void customRender(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IGenericLayout resetLayout() {
		painterLayout = new PainterLayout(world, worldWidth, worldHeight, center, 0);
		return painterLayout;
	}

	@Override
	public void initGame() {
		assignTeamSpritesToCars();
		
		
		this.backgroundSprite = Art.getSprite(Art.PAINTER_BACKGROUND);
	}

	@Override
	protected void renderUpdates(float delta) {
		// TODO Auto-generated method stub
		
	}

}
