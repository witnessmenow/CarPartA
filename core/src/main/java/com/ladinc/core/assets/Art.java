package com.ladinc.core.assets;

import java.util.HashMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;

public class Art {
	public static HashMap<String, Texture> textureTable = new HashMap<String, Texture>();
	public static final String LOGO = "LOGO";
	public static final String BALLS = "BALLS";
	public static final String CARS = "CARS";
	
	public static final String GAME_DESCRIPTION_WINDOW = "GAME_DESCRIPTION_WINDOW";
	
	public static final String CROWN = "CROWN";
	
	public static final String WHEELS = "WHEELS";
	public static final String IDENTIFIER = "IDENTIFIER";
	
	public static final String SOCCER_PITCH = "SOCCER_PITCH";
	public static final String GOAL_OVERLAY = "GOAL_OVERLAY";
	
	
	public static final String TOUCH_OVERLAY = "TOUCH_OVERLAY";
	public static final String FINISHED_OVERLAY= "FINISHED_OVERLAY";
	public static final String DEMO_MESSAGE= "DEMO_MESSAGE";
	public static final String GAME_MODE_IMAGES= "GAME_MODE_IMAGES";
	public static final String SELECTED_GAME_MODE_INDICATOR= "SELECTED_GAME_MODE_INDICATOR";
	public static final String GAME_MODE_OVERLAY= "GAME_MODE_OVERLAY";
	public static final String GAME_MODE_OVERLAY_TOUCH= "GAME_MODE_OVERLAY_TOUCH";
	
	//Team Select
	public static final String TEAM_SELECT_AREA = "TEAM_SELECT_AREA";
	public static final String START_GAME_CONFIRM = "START_GAME_CONFIRM";
	public static final String START_GAME_CONFIRM_TOUCH = "START_GAME_CONFIRM_TOUCH";
	public static final String INSTRUCTIONS_CONTROLLER = "INSTRUCTIONS_CONTROLLER";
	public static final String INSTRUCTIONS_TOUCH = "INSTRUCTIONS_TOUCH";
	
	
	public static final String RACE_BACKGROUND_1 = "RACE_BACKGROUND_1";
	
	
	//Pong
	public static final String PONG_BACKGROUND = "PONG_BACKGROUND";
	public static final String PONG_BALL = "PONG_BALL";
	
	//Painter
	public static final String PAINTER_BACKGROUND = "PAINTER_BACKGROUND";
	public static final String PAINT_TILES = "PAINT_TILES";
	
	//Mower
	public static final String MOWER_BACKGROUND = "MOWER_BACKGROUND";
	
	//Car Pool
	
	public static final String POOL_TABLE = "POOL_TABLE";
	
	public static HashMap<String, Sprite> spriteTable = new HashMap<String, Sprite>();
	
	public static void load()
	{
		loadTextures();
	}
	
	private static void loadTextures()
	{
		textureTable.put(LOGO,
				new Texture(Gdx.files.internal("libgdx-logo.png")));
		textureTable.put(BALLS, new Texture(Gdx.files.internal("balls.png")));
		textureTable.put(CARS, new Texture(Gdx.files.internal("Vehicles/CarSpritesheet.png")));
		
		textureTable.put(GAME_DESCRIPTION_WINDOW, new Texture(Gdx.files.internal("UI/GameDescriptionWindow.png")));
		textureTable.put(GAME_MODE_IMAGES, new Texture(Gdx.files.internal("Menus/GameModeImages.png")));
		textureTable.put(SELECTED_GAME_MODE_INDICATOR, new Texture(Gdx.files.internal("Menus/SelectedGameModeIndicator.png")));
		textureTable.put(GAME_MODE_OVERLAY, new Texture(Gdx.files.internal("Menus/GameModeOverlay.png")));
		textureTable.put(GAME_MODE_OVERLAY_TOUCH, new Texture(Gdx.files.internal("Menus/GameModeOverlayTouch.png")));
		
		textureTable.put(CROWN, new Texture(Gdx.files.internal("Vehicles/Crown.png")));
	
		textureTable.put(WHEELS, new Texture(Gdx.files.internal("Vehicles/Wheel.png")));
		textureTable.put(IDENTIFIER, new Texture(Gdx.files.internal("Identfiers.png")));
		textureTable.put(TOUCH_OVERLAY, new Texture(Gdx.files.internal("touchOverlay.png")));
		textureTable.put(POOL_TABLE, new Texture(Gdx.files.internal("CarPool/carsnu.png")));
		
		textureTable.put(TEAM_SELECT_AREA, new Texture(Gdx.files.internal("TeamSelect/teamSelect.png")));
		//textureTable.put(TEAM_SELECT_AREA, new Texture(Gdx.files.internal("TeamSelect/carselect screen.jpg")));
		textureTable.put(START_GAME_CONFIRM, new Texture(Gdx.files.internal("TeamSelect/StartGameConfirmDialog.png")));
		textureTable.put(START_GAME_CONFIRM_TOUCH, new Texture(Gdx.files.internal("TeamSelect/StartGameConfirmDialogTouch.png")));
		textureTable.put(INSTRUCTIONS_CONTROLLER, new Texture(Gdx.files.internal("TeamSelect/teamSelectControls.png")));
		textureTable.put(INSTRUCTIONS_TOUCH, new Texture(Gdx.files.internal("TeamSelect/teamSelectTouchControlls.png")));
		
		textureTable.put(SOCCER_PITCH, new Texture(Gdx.files.internal("SocCar/GreenPitch.jpg")));
		textureTable.put(GOAL_OVERLAY, new Texture(Gdx.files.internal("SocCar/GoalOverlay.png")));
		
		textureTable.put(RACE_BACKGROUND_1, new Texture(Gdx.files.internal("Maze/Maze1.png")));
		
		textureTable.put(PONG_BACKGROUND, new Texture(Gdx.files.internal("Pong/PongBackground.png")));
		textureTable.put(PONG_BALL, new Texture(Gdx.files.internal("Pong/pongBall.png")));
		
		textureTable.put(PAINTER_BACKGROUND, new Texture(Gdx.files.internal("Painter/PainterBackground.png")));
		textureTable.put(PAINT_TILES, new Texture(Gdx.files.internal("Painter/PaintTiles.png")));
		
		textureTable.put(MOWER_BACKGROUND, new Texture(Gdx.files.internal("Mower/MowerBackground.png")));
		
		textureTable.put(FINISHED_OVERLAY, new Texture(Gdx.files.internal("FinishedOverlay.png")));
		textureTable.put(DEMO_MESSAGE, new Texture(Gdx.files.internal("DemoWelcomeScreen.png")));
		
	}
	
	public static Sprite getTouchOverlay()
	{
		if(!spriteTable.containsKey(TOUCH_OVERLAY))
		{
			spriteTable.put(TOUCH_OVERLAY, new Sprite(Art.textureTable.get(TOUCH_OVERLAY)));
		}
		
		return spriteTable.get(TOUCH_OVERLAY);
	}
	
	public static void updateSprite(Sprite sprite, SpriteBatch spriteBatch, int PIXELS_PER_METER, Body body)
	{
		if(sprite != null && spriteBatch != null && body != null)
		{
			setSpritePosition(sprite, PIXELS_PER_METER, body);
	
			sprite.draw(spriteBatch);
		}
	}
	
	public static void setSpritePosition(Sprite sprite, int PIXELS_PER_METER, Body body)
	{
		
		sprite.setPosition(PIXELS_PER_METER * body.getPosition().x - sprite.getWidth()/2,
				PIXELS_PER_METER * body.getPosition().y  - sprite.getHeight()/2);
		sprite.setRotation((MathUtils.radiansToDegrees * body.getAngle()));
	}
	
	public static Sprite getSprite(String str)
	{
		if(!spriteTable.containsKey(str))
		{
			spriteTable.put(str, new Sprite(Art.textureTable.get(str)));
		}
		
		return spriteTable.get(str);
	}
	
}