package com.ladinc.core.screen;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.core.CarPartA;

public class GameScreen {
	
	protected OrthographicCamera camera;
	protected SpriteBatch spriteBatch;

    protected World world;
    
    protected Box2DDebugRenderer debugRenderer;
    
    //Used for sprites etc
    protected int screenWidth;
	protected int screenHeight;
    
    //Used for Box2D
    protected float worldWidth;
    protected float worldHeight;
    protected int PIXELS_PER_METER = 10;  
    
    protected Vector2 center;
    
    protected CarPartA game;

}
