package com.ladinc.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.ladinc.core.assets.Assets;
import com.ladinc.core.controllers.MyControllerManager;
import com.ladinc.core.screen.MainMenuScreen;
import com.ladinc.core.screen.gamemodes.GameModeMetaInfo;
import com.ladinc.core.screen.gamemodes.GameModeMetaInfo.GameMode;
import com.ladinc.core.screen.gamemodes.capture.CaptureTheFlagScreen;
import com.ladinc.core.screen.gamemodes.carpool.CarPoolScreen;
import com.ladinc.core.screen.gamemodes.hill.HillScreen;
import com.ladinc.core.screen.gamemodes.king.KingScreen;
import com.ladinc.core.screen.gamemodes.mazes.MazeScreen;
import com.ladinc.core.screen.gamemodes.mower.MowerScreen;
import com.ladinc.core.screen.gamemodes.painter.PainterScreen;
import com.ladinc.core.screen.gamemodes.pong.PongScreen;
import com.ladinc.core.screen.gamemodes.soccer.SoccerScreen;
import com.ladinc.core.screen.menus.GameModeSelectScreen;

public class CarPartA extends Game {
	
	public MyControllerManager controllerManager;
	
	public List<GameModeMetaInfo> gameModes;
	
	@Override
	public void create()
	{
		Gdx.app.setLogLevel(Application.LOG_ERROR);
		Assets.load();
		initGameModes();
		controllerManager = new MyControllerManager();
		setScreen(new MainMenuScreen(this));
		//setScreen(new GameModeSelectScreen(this));
	}
	
	public void initGameModes()
	{
		gameModes = new ArrayList<GameModeMetaInfo>();
		
		gameModes.add(SoccerScreen.getMetaInfo());
		gameModes.add(CaptureTheFlagScreen.getMetaInfo());
		gameModes.add(CarPoolScreen.getMetaInfo());
		gameModes.add(HillScreen.getMetaInfo());
		gameModes.add(KingScreen.getMetaInfo());
		gameModes.add(MazeScreen.getMetaInfo());
		gameModes.add(MowerScreen.getMetaInfo());
		gameModes.add(PainterScreen.getMetaInfo());
		gameModes.add(PongScreen.getMetaInfo());

	}
	
	public void setRandomGameMode()
	{
		if(this.gameModes != null)
		{
			Random r = new Random();
			
			int index = r.nextInt(this.gameModes.size());
			
			setGameMode(this.gameModes.get(index).game);
		}
		else
		{
			setScreen(new MainMenuScreen(this));
		}
	}
	
	public void setGameMode(GameMode gm)
	{
		switch (gm) 
		{
			case Amazing:
				setScreen(new MazeScreen(this));
				break;
			case Capture:
				setScreen(new CaptureTheFlagScreen(this));
				break;
			case CarPool:
				setScreen(new CarPoolScreen(this));
				break;
			case Hill:
				setScreen(new HillScreen(this));
				break;
			case King:
				setScreen(new KingScreen(this));
				break;
			case Mower:
				setScreen(new MowerScreen(this));
				break;
			case Painter:
				setScreen(new PainterScreen(this));
				break;
			case Pong:
				setScreen(new PongScreen(this));
				break;
			case Soccar:
				setScreen(new SoccerScreen(this));
				break;
			default:
				break;


		}
	}
}