package com.ladinc.core.ux.menus.endgame;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
	
	public EndGameOverlay(Vector2 c)
	{
		super(c);
		optionsList.add(PLAY_AGAIN);
		optionsList.add(SELECT_ANOTHER);
		optionsList.add(EXIT);
	}
}
