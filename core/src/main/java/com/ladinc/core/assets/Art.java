package com.ladinc.core.assets;

import java.util.Hashtable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Art {
	public static Hashtable<String, Texture> textureTable = new Hashtable<String, Texture>();
	public static final String LOGO = "LOGO";
	public static final String BALL = "BALL";
	
	public static void load()
	{
		loadTextures();
	}
	
	private static void loadTextures()
	{
		textureTable.put(LOGO,
				new Texture(Gdx.files.internal("libgdx-logo.png")));
		textureTable.put(BALL, new Texture(Gdx.files.internal("ball.png")));
	}
}