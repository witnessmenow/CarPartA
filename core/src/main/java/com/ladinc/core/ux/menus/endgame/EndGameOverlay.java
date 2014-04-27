package com.ladinc.core.ux.menus.endgame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ladinc.core.assets.Art;
import com.ladinc.core.assets.Font;
import com.ladinc.core.ux.GenericOverlay;
import com.ladinc.core.ux.MenuOptions;
import com.ladinc.core.ux.MenuOptions.Options;

public class EndGameOverlay extends GenericOverlay
{
	private static MenuOptions PLAY_AGAIN = new MenuOptions("Play Again", "Play the same game mode again.", Options.restart);
	private static MenuOptions SELECT_ANOTHER = new MenuOptions("Select Another Game", "Choose a different game mode.", "Teams and scores are preserved.", Options.newGame);
	private static MenuOptions EXIT = new MenuOptions("Quit", "Quit to the main menu.", "Teams and scores will be reset", Options.quit);
	
	public int homeWins = 0;
	public int awayWins = 0;
	public int totalGames = 0;
	
	private float homeScoreX;
	private float awayScoreX;
	
	public EndGameOverlay(Vector2 c)
	{
		super(c);
		optionsList.add(PLAY_AGAIN);
		optionsList.add(SELECT_ANOTHER);
		optionsList.add(EXIT);
		
		homeScoreX = (center.x - background.getWidth()/2) + background.getWidth()/5;
		awayScoreX = (center.x - background.getWidth()/2) + ((background.getWidth()/5)*4);
	}
	
	public void displayEndGameOverlay(SpriteBatch sb, float delta)
	{
		this.drawOverlay(sb, delta);
		
		smallerFont.setColor(Color.DARK_GRAY);
		String tmp = "Game " + totalGames;
		smallerFont.draw(sb, tmp, center.x - smallerFont.getBounds(tmp).width/2, titleY);
		
		largeFont.setColor(Color.BLUE);
		tmp = String.valueOf(homeWins);
		largeFont.draw(sb, tmp, homeScoreX - largeFont.getBounds(tmp).width/2, titleY);
		
		largeFont.setColor(Color.RED);
		tmp = String.valueOf(awayWins);
		largeFont.draw(sb, tmp, awayScoreX - largeFont.getBounds(tmp).width/2, titleY);
	}
	
	
}
